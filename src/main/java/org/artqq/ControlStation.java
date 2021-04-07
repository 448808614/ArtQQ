package org.artqq;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.qq.jce.wup.UniPacket;
import org.artqq.Wtlogin.Register;
import org.artqq.android.AndroidInfo;
import org.artqq.jce.favorite.Favorite;
import org.artqq.jce.favorite.ReqHead;
import org.artqq.jce.friend.FriendInfo;
import org.artqq.jce.friend.GetFriendList;
import org.artqq.jce.friend.GroupInfo;
import org.artqq.jce.friend.VipBaseInfo;
import org.artqq.jce.group.*;
import org.artqq.jce.summary.RespHead;
import org.artqq.jce.summary.SummaryCard;
import org.artqq.oidb.*;
import org.artqq.protobuf.MsgWithDraw;
import org.artqq.protobuf.generalflags;
import org.artqq.protobuf.group_member_info;
import org.artqq.protobuf.wording_storage;
import org.artqq.utils.HexUtil;
import org.artqq.utils.QQAgreementUtils;
import org.artqq.utils.RandomUtil;
import org.artqq.utils.TCPSocket;
import org.artqq.utils.bytes.ByteObject;

import java.util.ArrayList;
import java.util.Objects;

public class ControlStation {
    public ControlStation(ArtBot bot){
        this.bot = bot;
    }
    
    private final ArtBot bot;

    /**
     * @author luoluo
     * @date 2020/9/5
     * @param groupId 群号
     * @param uin 被操作人
     * @param isDelete 是否删除管理员
     * @desc 设置群管理员
     */
    public void setGroupAdmin(long groupId, long uin, boolean isDelete){
        byte[] src = oidb_0x55c.Req.toByteArray(groupId, uin, isDelete);
        int seq = bot.recorder.nextSsoSeq();
        Objects.requireNonNull(getClient()).send(QQAgreementUtils.encodeRequest(bot, seq, "OidbSvc.0x55c_1", src));
    }

    /**
     * @author luoluo
     * @date 2020/8/27
     * @param groupCode 群号
     * @param uin 对方QQ
     * @param text 称号，为空则为取消头衔
     * @desc 设置群头衔
     */
    public void setSpecialTitle(long groupCode, long uin, String text) {
        if(text == null) {
            text = "";
        }
        oidb_0x8fc.Req req = new oidb_0x8fc.Req();
        req.groupid = groupCode;
        req.mem_info = new oidb_0x8fc.MemInfo();
        req.mem_info.uin = uin;
        req.mem_info.special_title = text.getBytes();
        req.mem_info.special_title_expire_time = 4294967295L;
        // 设置特殊称号过期时间
        oidbSsoPkg pkg = new oidbSsoPkg();
        pkg.command = 0x8fc;
        pkg.service_type = 2;
        pkg.bodybuffer = req.toByteArray();
        Objects.requireNonNull(getClient()).send(QQAgreementUtils.encodeRequest(bot, bot.recorder.nextSsoSeq(), "OidbSvc.0x8fc_2", pkg.toByteArray()));
    }

    /**
     * @author luoluo
     * @date 2020/8/16
     * @param groupCode 群号
     * @param msgSeq 消息id
     * @param isDelete 是否移除精华
     * @desc 设置精华消息
     */
    public void setUpEssentialMessage(long groupCode, int msgSeq, boolean isDelete){
        oidb_0xeac.ReqBody req = new oidb_0xeac.ReqBody();
        req.group_code = groupCode;
        req.msg_seq = msgSeq;
        oidbSsoPkg pkg = new oidbSsoPkg();
        pkg.command = 0xeac;
        pkg.service_type = isDelete ? 2 : 1;
        pkg.client_version = "android " + bot.agreement_info.package_version;
        pkg.bodybuffer = req.toByteArray();
        Objects.requireNonNull(getClient()).send( QQAgreementUtils.encodeRequest(bot, bot.recorder.nextSsoSeq(), "OidbSvc.0xeac_" + pkg.service_type, pkg.toByteArray()) );
    }

    /**
     * @author luoluo
     * @date 2020/8/15
     * @param groupCode 群号
     * @param allow 状态
     * @desc 设置是否开启群匿名
     */
    public void setTroopAnonymousChat(long groupCode, boolean allow){
        ByteObject config = new ByteObject();
        config.WriteInt(groupCode);
        config.WriteBoolean(allow);
        oidbSsoPkg pkg = new oidbSsoPkg();
        pkg.command = 1384;
        pkg.service_type = 22;
        pkg.client_version = "android " + bot.agreement_info.package_version;
        pkg.bodybuffer = config.toByteArray();
        Objects.requireNonNull(getClient()).send( QQAgreementUtils.encodeRequest(bot, bot.recorder.nextSsoSeq(), "OidbSvc.0x568_22", pkg.toByteArray()) );
    }

    /**
     * @author luoluo
     * @date 2020/8/16
     * @param groupid 群号
     * @return org.artqq.oidb.oidb_0x3bb.Body
     * @desc [不开放] 获取匿名
     */
    public oidb_0x3bb.Body generate_anonymous(long groupid){
        oidb_0x3bb.Body reqBody = new oidb_0x3bb.Body();
        reqBody.cmd = 1;
        reqBody.anony_req = new oidb_0x3bb.ReqBody();
        reqBody.anony_req.group_code = groupid;
        reqBody.anony_req.uin = bot.getUin();
        int seq = bot.recorder.nextSsoSeq();
        ReceiveData.HandlePacket hanlde = bot.addHandlePacket(new ReceiveData.HandlePacket("group_anonymous_generate_nick.group"){
            @Override
            public boolean helpJudgement() {
                if(ssoSeq == seq){
                    source = new ByteObject(source).disUseData(4).readRestBytes();
                    return true;
                }
                return false;
            }
        });
        Objects.requireNonNull(getClient()).send( QQAgreementUtils.encodeRequest(bot, seq, "group_anonymous_generate_nick.group", reqBody.toByteArray()) );
        hanlde.waitPacket();
        oidb_0x3bb.Body body = oidb_0x3bb.Body.decode(hanlde.source);
        assert body != null;
        if(body.anony_rsp != null){
            bot.data.put("AnonyName", body.anony_rsp.anony_name);
            return body;
        }else{
            return generate_anonymous(groupid);
        }
    }

    // 暂不可用
    public JsonObject getGroupAllInfo(long groupCode){
        oidbSsoPkg pkg = new oidbSsoPkg();
        pkg.command = 0x79a;
        pkg.service_type = 1;
        pkg.client_version = "android " + bot.agreement_info.package_version;
        oidb_0x79a.Req _79a = new oidb_0x79a.Req();
        _79a.group_code = groupCode;
        _79a.source = 1;
        pkg.bodybuffer = _79a.toByteArray();
        TCPSocket client = getClient();
        if(client == null) {
            return null;
        }
        final int seq = bot.recorder.nextSsoSeq();
        ReceiveData.HandlePacket hanlde = bot.addHandlePacket(new ReceiveData.HandlePacket("OidbSvc.0x79a_1"){
            @Override
            public boolean helpJudgement() {
                return ssoSeq == seq;
            }
        });
        client.send( QQAgreementUtils.encodeRequest(bot, seq, "OidbSvc.0x79a_1", pkg.toByteArray()) );
        return null;
    }

    /**
     * @author luoluo
     * @date 2020/8/14
     * @param groupCode 群号
     * @param msgSeq 消息标识
     * @return org.artqq.ControlStation.WithdrawMsg
     * @desc 撤回群消息
     */
    public WithdrawMsg withdrawGroupMsg(long groupCode, long msgSeq){
        MsgWithDraw.Req req = new MsgWithDraw.Req();
        req.group_with_draw = new MsgWithDraw.GroupMsgWithDraw.Req();
        req.group_with_draw.sub_cmd = 1;
        req.group_with_draw.group_type = 0;
        req.group_with_draw.group_code = groupCode;
        req.group_with_draw.msg_list = new MsgWithDraw.MessageInfo();
        req.group_with_draw.msg_list.msg_seq = msgSeq;
        req.group_with_draw.msg_list.msg_random = RandomUtil.randomInt(1000, 951372012);
        final int seq = bot.recorder.nextSsoSeq();
        ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("PbMessageSvc.PbMsgWithDraw"){
                @Override
                public boolean helpJudgement(){
                    return seq == ssoSeq;
                }
        });
        Objects.requireNonNull(getClient()).send( QQAgreementUtils.encodeRequest(bot, seq, "PbMessageSvc.PbMsgWithDraw", req.toByteArray()) );
        handle.waitPacket();
        MsgWithDraw.Resp resp = MsgWithDraw.Resp.decode(new ByteObject(handle.source).disUseData(4).readRestBytes());
        assert resp != null;
        if(resp.group_with_draw.result == 0) {
            return WithdrawMsg.Success;
        } else {
            return WithdrawMsg.Error;
        }
    }

    /**
     * @author luoluo
     * @date 2020/8/14
     * @param groupCode 群号
     * @param uin QQ
     * @return com.google.gson.JsonObject
     * @desc 获取群成员信息 仅可获取同群群友信息
     */
    public JsonObject getGroupMemberCardInfo(final long groupCode, final long uin){
        if(groupCode < 10000 || groupCode > 4000000000L || uin < 10000 || uin > 4000000000L){
            return null;
        }
        group_member_info.Req req = new group_member_info.Req();
        req.uin = uin;
        req.group_code = groupCode;
        req.client_type = 1;
        req.new_client = true;
        req.rich_card_name_ver = 1;
        final int seq = bot.recorder.nextSsoSeq();
        ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("group_member_card.get_group_member_card_info"){
            @Override
            public boolean helpJudgement(){
                byte[] source = new ByteObject(super.source).disUseData(4).readRestBytes();
                group_member_info.Resp resp = group_member_info.Resp.decode(source);
                if(ssoSeq == seq){
                    obj = resp;
                    return true;
                }
                return false;
            }
        });
        Objects.requireNonNull(getClient()).send( QQAgreementUtils.encodeRequest(bot, seq, "group_member_card.get_group_member_card_info", req.toByteArray()) );
        handle.waitPacket();
        group_member_info.Resp resp = (group_member_info.Resp) handle.obj;
        JsonObject ret = new JsonObject();
        if(resp.msg_meminfo == null || resp.msg_meminfo.nick == null){
            ret.addProperty("result", -1);
            ret.addProperty("nick", "Unkonwn");
        }else{
            ret.addProperty("is_friend", resp.msg_meminfo.is_friend);
            ret.addProperty("card", resp.msg_meminfo.card);
            ret.addProperty("concern_type", resp.msg_meminfo.concern_type);
            ret.addProperty("role", resp.msg_meminfo.role);
            ret.addProperty("nick", resp.msg_meminfo.nick);
            ret.addProperty("location", resp.msg_meminfo.location);
            ret.addProperty("age", resp.msg_meminfo.age);
            ret.addProperty("group_level", resp.msg_meminfo.lev);
            ret.addProperty("join_time", resp.msg_meminfo.join);
            ret.addProperty("last_speak_time", resp.msg_meminfo.last_speak);
            ret.addProperty("is_vip", resp.msg_meminfo.is_vip);
            ret.addProperty("is_year_vip", resp.msg_meminfo.is_year_vip);
            ret.addProperty("is_super_vip", resp.msg_meminfo.is_super_vip);
            ret.addProperty("is_super_qq", resp.msg_meminfo.is_super_qq);
            ret.addProperty("vip_level", resp.msg_meminfo.vip_lev);
            ret.addProperty("special_title", new String(resp.msg_meminfo.special_title));
            ret.addProperty("special_title_expire_time", resp.msg_meminfo.special_title_expire_time);
            ret.addProperty("job", new String(resp.msg_meminfo.job));
            ret.addProperty("phone", new String(resp.msg_meminfo.phone_num));
            ret.addProperty("level", resp.msg_meminfo.level);
            ret.addProperty("flower_count", resp.msg_meminfo.msg_flower_entry != null ? resp.msg_meminfo.msg_flower_entry.flower_count : 0);
            ret.addProperty("result", 0);
            ret.addProperty("credit", resp.msg_meminfo.credit);
            ret.addProperty("is_concerned", resp.msg_meminfo.is_concerned);
            ret.addProperty("sex", resp.msg_meminfo.sex);
            JsonObject honors = new JsonObject();
            if(resp.msg_meminfo.group_honor != null) {
                try{
                    JsonArray ids = new JsonArray();
                    generalflags.ResvAttr.Honor honor = generalflags.ResvAttr.Honor.decode(resp.msg_meminfo.group_honor);
                    if(honor != null){
                        for(int id : honor.id){
                            ids.add(id);
                        }
                        honors.addProperty("level", honor.level);
                    } else {
                        honors.addProperty("level", 0);
                    }
                    honors.add("id", ids);
                }catch(Exception e){
                    if(Code.isDebug) {
                        e.printStackTrace();
                    }
                }
            }
            ret.add("honor", honors);
        }
        return ret;
    }

    /**
     * @author luoluo
     * @date 2020/8/14
     * @param uin QQ号
     * @return com.google.gson.JsonObject
     * @desc 获取非好友用户信息（好友搜索）
     */
    public JsonObject getUserInfoBySearchFriend(long uin) {
        final int requestId = bot.recorder.nextRequestId();
        final int seq = bot.recorder.nextSsoSeq();
        SummaryCard.Req req = new SummaryCard.Req();
        req.uin = uin;
        req.comeFrom = 31;
        req.qzoneFeedTimestamp = 0;
        req.isFriend = 0;
        req.groupCode = 0;
        req.groupUin = 0;
        req.seed = HexUtil.hexStringToByte("0C 12 5F 29 52 2F 22 5D 7B 9E B3 30 01");
        req.searchName = "";
        req.getControl = 69181;
        req.addFriendSource = 30001;
        req.secureSig = new byte[1];
        req.tinyId = 0;
        req.likeSource = 1;
        req.reqMedalWallInfo = 1;
        {
            req.req0x5ebFieldId = new ArrayList<>();
            req.req0x5ebFieldId.add(27225);
            req.req0x5ebFieldId.add(27224);
            req.req0x5ebFieldId.add(42122);
            req.req0x5ebFieldId.add(42121);
            req.req0x5ebFieldId.add(27236);
            req.req0x5ebFieldId.add(27238);
            req.req0x5ebFieldId.add(42167);
            req.req0x5ebFieldId.add(42172);
            req.req0x5ebFieldId.add(40324);
            req.req0x5ebFieldId.add(42284);
            req.req0x5ebFieldId.add(42326);
            req.req0x5ebFieldId.add(42325);
            req.req0x5ebFieldId.add(42356);
            req.req0x5ebFieldId.add(42363);
            req.req0x5ebFieldId.add(42361);
            req.req0x5ebFieldId.add(42367);
            req.req0x5ebFieldId.add(42377);
        }
        req.reqNearbyGodInfo = 1;
        req.reqCommLabel = 0;
        req.reqExtendCard = 1;
        req.richCardNameVer = 1;
        ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("SummaryCard.ReqSummaryCard") {
            @Override
            public boolean helpJudgement() {
                return ssoSeq == seq;
            }
        });
        UniPacket uniPacket = new UniPacket(true);
        uniPacket.setEncodeName("UTF-8");
        uniPacket.setFuncName("ReqSummaryCard");
        uniPacket.setServantName("SummaryCardServantObj");
        uniPacket.put("ReqSummaryCard", req);
        uniPacket.put("ReqHead", new org.artqq.jce.summary.ReqHead(2));
        uniPacket.setRequestId(requestId);
        Objects.requireNonNull(getClient()).send(QQAgreementUtils.encodeRequest(bot, seq, "SummaryCard.ReqSummaryCard", uniPacket.encode()));
        handle.waitPacket();
        UniPacket uni = new UniPacket();
        uni.setEncodeName("UTF-8");
        uni.decode(handle.source);
        SummaryCard.Rsp rsp = uni.getByClass("RespSummaryCard", new SummaryCard.Rsp());
        org.artqq.jce.summary.RespHead head = uni.getByClass("RespHead", new org.artqq.jce.summary.RespHead());
        JsonObject data = new JsonObject();
        data.addProperty("city", rsp.city == null ? "" : rsp.city);
        if(head != null && head.vCookies != null){
            data.addProperty("cookies", HexUtil.bytesToHexString(head.vCookies));
        }
        data.addProperty("age", rsp.age);
        data.addProperty("birthday", rsp.birthday);
        data.addProperty("contactName", rsp.contactName == null ? "" : rsp.contactName);
        data.addProperty("country", rsp.country == null ? "" : rsp.country);
        data.addProperty("email", rsp.Email == null ? "" : rsp.Email);
        data.addProperty("face", rsp.face);
        data.addProperty("level", rsp.level);
        data.addProperty("login_days", rsp.LoginDays);
        data.addProperty("login_desc", rsp.LoginDesc);
        data.addProperty("nick", rsp.Nick == null ? "" : rsp.Nick);
        data.addProperty("mobile", rsp.Mobile == null ? "" : rsp.Mobile);
        data.addProperty("sex", rsp.sex);
        data.addProperty("space_name", rsp.SpaceName == null ? "" : rsp.Mobile);
        data.addProperty("show_name", rsp.ShowName == null ? "" : rsp.ShowName);
        data.addProperty("status", rsp.Status == null ? "" : rsp.Status);
        data.addProperty("user_flag", rsp.userFlag);
        data.addProperty("template_id", rsp.TemplateId);
        data.addProperty("vote_count", rsp.voteCount);
        data.addProperty("province", rsp.Province == null ? "" : rsp.Province);
        data.addProperty("photo_count", rsp.photoCount);
        if(rsp.addQuestion != null){
            JsonArray questions = new JsonArray();
            for(String str : rsp.addQuestion) {
                questions.add(str == null ? "" : str);
            }
            data.add("add_question", questions);
        }
        if(rsp.heartInfo != null){
            data.addProperty("heart_count", rsp.heartInfo.iHeartCount);
        }
        return data;
    }

    /**
     * @author luoluo
     * @date 2020/8/14
     * @param groupCodes 群号
     * @return com.google.gson.JsonObject
     * @desc 获取群信息
     */
    public JsonObject getMultiTroopInfo(long... groupCodes){
        JsonObject result = new JsonObject();
        result.addProperty("result", -1);
        if(groupCodes == null) {
            return result;
        }
        final int requestId = bot.recorder.nextRequestId();
        GetMultiTroopInfo.Req req = new GetMultiTroopInfo.Req();
        req.uin = bot.getUin();
        for(long code : groupCodes){
            req.groupCode.add(code);
        }
        req.richInfo = 1;
        int seq = bot.recorder.nextSsoSeq();
        ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("friendlist.GetMultiTroopInfoReq"){
            @Override
            public boolean helpJudgement(){
                GetMultiTroopInfo.Resp resp = QQAgreementUtils.decodePacket(source.clone(), "GMTIRESP", new GetMultiTroopInfo.Resp());
                if(seq == ssoSeq){
                    obj = resp;
                    return true;
                }
                return false;
            }
        });
        Objects.requireNonNull(getClient()).send( QQAgreementUtils.encodeRequest(bot, seq, "friendlist.GetMultiTroopInfoReq", QQAgreementUtils.encodePacket(req, "mqq.IMService.FriendListServiceServantObj",  "GetMultiTroopInfoReq", "GMTIREQ", requestId)) );
        handle.waitPacket();
        GetMultiTroopInfo.Resp resp = (GetMultiTroopInfo.Resp) handle.obj;
        result.addProperty("result", resp.result);
        if(resp.result != 0) {
            return result;
        }
        for(TroopInfoV2 info : resp.vecTroopInfo){
            JsonObject ret = new JsonObject();
            ret.addProperty("GroupName", info.strGroupName);
            ret.addProperty("GroupMemo", info.strGroupMemo);
            ret.addProperty("FingerMemo", info.strFingerMemo);
            ret.addProperty("GroupCode", info.dwGroupCode);
            ret.addProperty("GroupUin", info.dwGroupUin);
            ret.addProperty("GroupOwnerUin", info.dwGroupOwnerUin);
            ret.addProperty("MemberNum", info.memberNum);
            ret.addProperty("CertificationType", info.dwCertificationType);
            ret.addProperty("GroupClassExt", info.dwGroupClassExt);
            ret.addProperty("GroupFlagExt", info.dwGroupFlagExt);
            result.add(String.valueOf(info.dwGroupCode), ret);
        }
        return result;
    }

    /**
     * @author luoluo
     * @date 2020/8/14
     * @param isRefresh 是否刷新缓存
     * @return com.google.gson.JsonObject
     * @desc 获取群列表
     */
    public JsonObject getGroupList(boolean isRefresh){
        if(!isRefresh){
            if(bot.data.containsKey("SimplifyGroupList")){
                return (JsonObject) bot.data.get("SimplifyGroupList");
            }else{
                return getGroupList(true);
            }
        }else{
            final int requestId = bot.recorder.nextRequestId();
            int seq = bot.recorder.nextSsoSeq();
            ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("friendlist.GetTroopListReqV2"){
                @Override
                public boolean helpJudgement(){
                    GetTroopList.Resp resp = QQAgreementUtils.decodePacket(source.clone(), "GetTroopListRespV2", new GetTroopList.Resp());
                    if(ssoSeq == seq){
                        obj = resp;
                        return true;
                    }
                    return false;
                }
            });
            GetTroopList.SimplifyReq req = new GetTroopList.SimplifyReq();
            req.uin = bot.getUin();
            req.getMSFMsgFlag = 0;
            req.groupFlagExt = 1;
            req.shVersion = 7;
            req.companyId = 0;
            req.versionNum = 1;
            req.getLongGroupName = 1;
            byte[] data = QQAgreementUtils.encodeRequest(bot, seq, "friendlist.GetTroopListReqV2", QQAgreementUtils.encodePacket(req, "mqq.IMService.FriendListServiceServantObj",  "GetTroopListReqV2Simplify", "GetTroopListReqV2Simplify", requestId));
            Objects.requireNonNull(getClient()).send(data);
            handle.waitPacket();
            JsonObject ret = new JsonObject();
            GetTroopList.Resp resp = (GetTroopList.Resp) handle.obj;
            ret.addProperty("result", resp.result);
            if(resp.result != 0){
                return ret;
            }
            ret.addProperty("count", resp.troopcount);
            JsonArray groupArray = new JsonArray();
            int i = 0;
            for(TroopNum troopinfo : resp.vecTroopList){
                GroupRankInfo rankInfo = resp.vecTroopRank.get(i);
                JsonObject groupInfo = new JsonObject();
                groupInfo.addProperty("GroupCode", troopinfo.GroupCode);
                groupInfo.addProperty("GroupUin", troopinfo.GroupUin);
                groupInfo.addProperty("OwnerUin", troopinfo.dwGroupOwnerUin);
                groupInfo.addProperty("MemberNum", troopinfo.dwMemberNum);
                groupInfo.addProperty("GroupName", troopinfo.strGroupName);
                groupInfo.addProperty("MaxMemNum", troopinfo.dwMaxGroupMemberNum);
                groupInfo.addProperty("AdminName", rankInfo.strAdminName);
                groupInfo.addProperty("OwnerName", rankInfo.strOwnerName);
                JsonArray rankArray = new JsonArray();
                for(LevelRankPair rank : rankInfo.vecRankMap){
                    rankArray.add(rank.strRank);
                }
                groupInfo.add("RankMap", rankArray);
                JsonArray NewrankArray = new JsonArray();
                for(LevelRankPair rank : rankInfo.vecRankMapNew){
                    NewrankArray.add(rank.strRank);
                }
                groupInfo.add("NewRankMap", NewrankArray);
                groupArray.add(groupInfo);
                i++;
            }
            ret.add("groups", groupArray);
            return ret;
        }
    }

    /**
     * @author luoluo
     * @date 2020/8/14
     * @param groupCode 群号
     * @param uin 目标
     * @param card 昵称
     * @return org.artqq.ControlStation.ModifyCard
     * @desc 修改群名片
     */
    public ModifyCard modifyGroupCard(final long groupCode, long uin, String card){
        if(card == null || card.getBytes().length > 60.|| card.length() <= 0){
            return ModifyCard.CardLenghtError;
        }else if(groupCode < 10000 || groupCode > 4000000000L || uin < 10000 || uin > 4000000000L){
            return ModifyCard.InputParameterError;
        }
        final int requestId = bot.recorder.nextRequestId();
        ArrayList<UinInfo> uininfos = new ArrayList<UinInfo>();
        ModifyGroupCard.Req req = new ModifyGroupCard.Req();
        req.groupCode = groupCode;
        req.newSeq = 0;
        req.zero = 0; 
        UinInfo info = new UinInfo();
        info.uin = uin;
        info.flag = 1;
        info.name = card;
        info.gender = -1;
        uininfos.add(info);
        req.uinInfo = uininfos;
        int seq = bot.recorder.nextSsoSeq();
        byte[] data = QQAgreementUtils.encodeRequest(bot, seq, "friendlist.ModifyGroupCardReq", QQAgreementUtils.encodePacket(req, "mqq.IMService.FriendListServiceServantObj",  "ModifyGroupCardReq", "MGCREQ", requestId));
        ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("friendlist.ModifyGroupCardReq"){
            @Override
            public boolean helpJudgement(){
                if(seq == ssoSeq){
                    obj = QQAgreementUtils.decodePacket(source, "MGCRESP", new ModifyGroupCard.Resp());
                    return true;
                }
                return false;
            }
        });
        Objects.requireNonNull(getClient()).send(data);
        handle.waitPacket();
        ModifyGroupCard.Resp resp = (ModifyGroupCard.Resp) handle.obj;
        if(resp.result == 0){
            return ModifyCard.Success;
        }else{
            return ModifyCard.UnauthorizedOperation;
        }
    }

    /**
     * @author luoluo
     * @date 2020/8/14
     * @param groupCode 群号
     * @param uin 目标
     * @param banTime 禁言时间（单位：秒）
     * @return org.artqq.ControlStation.GroupBan
     * @desc 群禁言
     */
    public GroupBan groupBan(final long groupCode, long uin, int banTime){
        if(banTime > 2592000 || banTime < 0){
            return GroupBan.BanTimeError;
        }else if(uin == 0 && (banTime == 1 || banTime == 0)){
            oidbSsoPkg pkg = new oidbSsoPkg();
            pkg.service_type = 0;
            pkg.command = 0x89a;
            oidb_0x89a.Req o89a = new oidb_0x89a.Req();
            o89a.groupid = groupCode;
            o89a.group_info = new oidb_0x89a.GroupInfo();
            o89a.group_info.shutup_time = (banTime == 1 ? 26843545 : 0);
            pkg.bodybuffer = o89a.toByteArray();
            Objects.requireNonNull(getClient()).send( QQAgreementUtils.encodeRequest(bot, bot.recorder.nextSsoSeq(), "OidbSvc.0x89a_0", pkg.toByteArray()) );
            return GroupBan.SendSuccess;
        }else if(uin == 0){
            return GroupBan.BanTimeError;
        }else if(groupCode < 10000 || groupCode > 4000000000L){
            return GroupBan.GroupCodeError;
        }else if(uin < 10000 || uin > 4000000000L){
            return GroupBan.UinError;
        }
        ByteObject bo = new ByteObject();
        bo.WriteInt(groupCode);
        bo.WriteByte(32);
        bo.WriteShort(0 + 1); // 批量禁言数量
        bo.WriteInt(uin);
        bo.WriteInt(banTime);
        oidbSsoPkg pkg = new oidbSsoPkg();
        pkg.command = 0x570;
        pkg.service_type = 8;
        pkg.bodybuffer = bo.toByteArray();
        int seq = bot.recorder.nextSsoSeq();
        ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("OidbSvc.0x570_8"){
            @Override
            public boolean helpJudgement(){
                byte[] buffer = Objects.requireNonNull(oidbSsoPkg.decode(source)).bodybuffer;
                if(new ByteObject(buffer).readInt() == groupCode && ssoSeq == seq){
                    obj = new ByteObject(buffer);
                    return true;
                }
                return false;
            }
        });
        Objects.requireNonNull(getClient()).send( QQAgreementUtils.encodeRequest(bot, seq, "OidbSvc.0x570_8", pkg.toByteArray()) );
        handle.waitPacket();
        return GroupBan.SendSuccess;
    }

    /**
     * @author luoluo
     * @date 2020/8/14
     * @param msg 日志内容
     * @desc 推送日志
     */
    public void sendReport(String... msg){
        byte[] src = org.artqq.jce.report.getReport(bot.recorder.nextRequestId(), msg);
        byte[] sendData = QQAgreementUtils.encodeRequest(bot, bot.recorder.nextSsoSeq(), "CliLogSvc.UploadReq", src);
        Objects.requireNonNull(getClient()).send(sendData);
    }

    /**
     * @author luoluo
     * @date 2020/8/14
     * @param groupCode 群号
     * @param isRefresh 是否刷新缓存
     * @return com.google.gson.JsonObject
     * @desc 获取群成员列表
     */
    public JsonObject getGroupMember(long groupCode, boolean isRefresh){
        if(isRefresh && bot.data.containsKey(groupCode + "-MemList")){
            return (JsonObject) bot.data.get(groupCode + "-MemList");
        }
        final int requestId = bot.recorder.nextRequestId();
        GetTroopMemberList.Req getTroopMem = new GetTroopMemberList.Req();
        getTroopMem.uin = bot.getUin();
        getTroopMem.GroupCode = groupCode;
        getTroopMem.GroupUin = QQAgreementUtils.groupCode2GroupUin(groupCode);
        getTroopMem.Version = 2;
        getTroopMem.ReqType = 1;
        getTroopMem.GetListAppointTime = 0;
        getTroopMem.cRichCardNameVer = 1;
        int seq = bot.recorder.nextSsoSeq();
        byte[] data = QQAgreementUtils.encodeRequest(bot, seq, "friendlist.getTroopMemberList", QQAgreementUtils.encodePacket(getTroopMem, "mqq.IMService.FriendListServiceServantObj",  "GetTroopMemberListReq", "GTML", requestId));
        ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("friendlist.getTroopMemberList"){
            @Override
            public boolean helpJudgement(){
                if(ssoSeq == seq){
                    obj = QQAgreementUtils.decodePacket(source, "GTMLRESP", new GetTroopMemberList.Resp());
                    return true;
                }
                return false;
            }
        });
        Objects.requireNonNull(getClient()).send(data);
        handle.waitPacket();
        JsonObject ret = new JsonObject();
        GetTroopMemberList.Resp resp = (GetTroopMemberList.Resp) handle.obj;
        if(resp.result == 0 && resp.errorCode == 0){
            ret.addProperty("result", resp.result);
            ret.addProperty("errCode", resp.errorCode);
            ret.addProperty("msg", "获取成功");
            JsonArray memArr = new JsonArray();
            for(TroopMemberInfo info : resp.vecTroopMember){
                JsonObject m = new JsonObject();
                m.addProperty("nick", info.Nick);
                m.addProperty("age", info.Age);
                m.addProperty("gender", info.Gender);
                m.addProperty("status", info.Status);
                m.addProperty("uin", info.MemberUin);
                m.addProperty("showName", info.sShowName);
                m.addProperty("special_title", info.sSpecialTitle);
                m.addProperty("vip_type", info.dwVipType);
                m.addProperty("vip_level", info.dwVipLevel);
                m.addProperty("shut_up_time", info.dwShutupTimestap);
                m.addProperty("member_level", info.dwMemberLevel);
                m.addProperty("join_time", info.dwJoinTime);
                m.addProperty("last_speak_time", info.dwLastSpeakTime);
                m.addProperty("group_level", info.dwGlobalGroupLevel);
                m.addProperty("is_shielded", info.cShielded);
                m.addProperty("is_concerned", info.cConcerned);
                m.addProperty("flag", info.dwFlag);
                m.addProperty("flag_ext", info.dwFlagExt);
                m.addProperty("apollo_flag", info.cApolloFlag);
                if(info.vecGroupHonor != null) {
                    JsonObject honors = new JsonObject();
                    generalflags.ResvAttr.Honor honor = generalflags.ResvAttr.Honor.decode(info.vecGroupHonor);
                    JsonArray ids = new JsonArray();
                    if(honor != null){
                        for(int id : honor.id){
                            ids.add(id);
                        }
                    }
                    assert honor != null;
                    honors.addProperty("level", honor.level);
                    honors.add("id", ids);
                    m.add("group_honor", honors);
                }
                memArr.add(m);
            }
            ret.add("data", memArr);
        }else{
            ret.addProperty("result", resp.result);
            ret.addProperty("errCode", resp.errorCode);
            ret.addProperty("msg", "请检查该Bot是否在群聊内，否则无法获取群员列表！");
        }
        bot.data.put(groupCode + "-MemList", ret);
        return ret;
    }

    /**
     * @author luoluo
     * @date 2020/8/14
     * @param uin 目标
     * @param time 次数
     * @return org.artqq.ControlStation.FavoriteEnum
     * @desc 点赞（无好友限制）
     */
    public FavoriteEnum giveFavorite(final long uin, int time) {
        if( time > 20 || time <= 0){
            return FavoriteEnum.TimeError;
        }
        final int requestId = bot.recorder.nextRequestId();
        Favorite.Req favorite = new Favorite.Req();
        ReqHead head = new ReqHead();
        head.lUIN = bot.getUin();
        head.iSeq = requestId;
        favorite.lMID = uin;
        favorite.emSource = 5;
        favorite.iCount = time;
        head.vCookies = HexUtil.HexString2Bytes("0C 18 00 01 06 01 31 16 01 35");
        favorite.stHeader = head;
        int seq = bot.recorder.nextSsoSeq();
        ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("VisitorSvc.ReqFavorite"){
            @Override
            public boolean helpJudgement(){
                if(ssoSeq == seq){
                    obj = QQAgreementUtils.decodePacket(source, "RespFavorite", new Favorite.Resp());
                    return true;
                }
                return false;
            }
        });
        Objects.requireNonNull(getClient()).send(QQAgreementUtils.encodeRequest(bot, seq, "VisitorSvc.ReqFavorite", QQAgreementUtils.encodePacket(favorite, "VisitorSvc", "ReqFavorite", "ReqFavorite", requestId)));
        handle.waitPacket();
        Favorite.Resp resp = (Favorite.Resp) handle.obj;
        System.out.println(resp.stHeader.strResult);
        if(resp.stHeader.iReplyCode == 0){
            return FavoriteEnum.Success;
        }else if(resp.stHeader.iReplyCode == 51){
            return FavoriteEnum.TimeMax;
        }
        return FavoriteEnum.LimitFavorite;
    }

    /**
     * @author luoluo
     * @date 2020/8/14
     * @param isRefresh 是否刷新缓存
     * @return com.google.gson.JsonObject
     * @desc 获取好友列表
     */
    public synchronized JsonObject getFriendList(boolean isRefresh){
        JsonObject ret = new JsonObject();
        JsonArray groupArray = new JsonArray();
        JsonArray friendArray = new JsonArray();
        if(!isRefresh && bot.data.containsKey("cache_friend_list")){
            return (JsonObject) bot.data.get("cache_friend_list");
        }
        synchronized(ControlStation.class){
            boolean exit = true;
            byte[] data = null; 
            final int seq = bot.recorder.nextSsoSeq();
            while(exit){
                final int requestid = bot.recorder.nextRequestId() ;
                ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("friendlist.getFriendGroupList"){
                        @Override
                        public boolean helpJudgement(){
                            if(seq == ssoSeq){
                                obj = QQAgreementUtils.decodePacket(source, "FLRESP", new GetFriendList.Resp());
                                return true;
                            }
                            return false;
                        }
                });
                if(data == null) {
                    Objects.requireNonNull(getClient()).send(QQAgreementUtils.encodeRequest(bot, seq, "friendlist.getFriendGroupList", GetFriendList.getFriendGroupList(requestid - 2 ,bot, 0, 100, 0, 0, 0)));
                } else {
                    getClient().send(QQAgreementUtils.encodeRequest(bot, seq, "friendlist.getFriendGroupList",data));
                }
                handle.waitPacket();
                GetFriendList.Resp resp = (GetFriendList.Resp) handle.obj;
                for(GroupInfo info : resp.vecGroupInfo){
                    boolean has = false;
                    JsonObject group = new JsonObject();
                    group.addProperty("name", info.groupname);
                    group.addProperty("id", info.groupId);
                    group.addProperty("friend_count", info.friend_count);
                    group.addProperty("online_friend_count", info.online_friend_count);
                    for(int i = 0; i < groupArray.size(); i++){
                        JsonObject grp = groupArray.get(i).getAsJsonObject();
                        if(grp.get("id").getAsByte() == group.get("id").getAsByte()){
                            has = true;
                        }
                    }
                    if( ! has ){
                        groupArray.add(group);
                    }
                }
                for(FriendInfo info : resp.vecFriendInfo){
                    boolean has = false;
                    JsonObject friend = new JsonObject();
                    friend.addProperty("uin", info.friendUin);
                    friend.addProperty("groupId", info.groupId);
                    friend.addProperty("nick", info.nick);
                    friend.addProperty("remark", info.remark);
                    friend.addProperty("showName", info.sShowName);
                    friend.addProperty("termDesc", info.strTermDesc);
                    friend.addProperty("status", info.status); // 忙碌什么的在线状态
                    friend.addProperty("extOnlineStatus", info.uExtOnlineStatus); // 比如Tim中这种在线状态
                    VipBaseInfo vpbi = info.oVipInfo;
                    // 不做解析 可自行扩展 建议看 VipBaseInfo.java
                    for(int i = 0; i < friendArray.size(); i++){
                        JsonObject ufriend = friendArray.get(i).getAsJsonObject();
                        if(ufriend.get("uin").getAsLong() == friend.get("uin").getAsLong()){
                            has = true;
                        }
                    }
                    if(! has ) {
                        friendArray.add(friend);
                    }
                }
                if(resp.totoal_friend_count > friendArray.size() || resp.totoal_group_count > groupArray.size()){
                    int now_friend_get = friendArray.size() - 1;
                    int now_group_get = groupArray.size() - 1;
                    int next_friend_get = now_friend_get + 100;
                    int next_group_get = now_group_get + 20;
                    if(resp.totoal_friend_count < next_friend_get) {
                        next_friend_get = resp.totoal_friend_count;
                    }
                    
                    if(resp.totoal_group_count < next_group_get) {
                        next_group_get = resp.totoal_group_count;
                    }
                    
                    if(next_friend_get == now_friend_get){
                        now_friend_get = next_friend_get - 100;
                    }
                    
                    data = GetFriendList.getFriendGroupList(requestid, bot, now_friend_get, next_friend_get, now_group_get, next_group_get, 0);
                }else{
                    exit = false;
                }        
            }
            ret.add("group", groupArray);
            ret.add("friend", friendArray);
        }
        return ret;
    }

    /**
     * @author luoluo
     * @date 2020/8/14
     * @param onlineType 11上线===21离线===31离开===41隐身===51忙碌
     * @param Extype {
     *             0 在线|Q我吧,
     *             1040 星座运势,
     *             1025 在家旅游,
     *             1030 今日天气,
     *             1000 我的电量,
     *             1016 碎觉ing,
     *             1018 学习ing,
     *             1032 熬夜ing,
     *             1050 打球ing,
     *             1051 裂爱ing,
     *             1052 我没事,
     *             1027 Tim中,
     *             1011 信号弱,
     *             1024 在线学习,
     *             1017 游戏ing,
     *             1022 度假ing,
     *             1019 吃饭ing,
     *             1021 煲剧ing,
     *             1028 我在听歌
     *         }
     * @desc 设置在线状态
     */
    public void setOnlineStatus(int onlineType, int Extype){
        int ssoSeq = bot.recorder.nextSsoSeq();
        Register.Req req = new Register.Req();
        req.lUin = bot.getUin();
        req.vecGuid = AndroidInfo.getGuid();
        req.iLargeSeq = bot.recorder.nextLardgeSeq();    
        req._0x769_reqbody = HexUtil.hexStringToByte("0A 04 08 2E 10 00 0A 05 08 9B 02 10 00 ");
        if(Extype != 0){
            req.bIsSetStatus = 1;
            req.uExtOnlineStatus = Extype;
        }else if(onlineType != bot.getOnlineStatus()){
            setOnlineStatus(onlineType, 0);
        }
        req.iStatus = onlineType;
        byte[] source = QQAgreementUtils.encodePacket(req, "PushService", "SvcReqRegister", "SvcReqRegister", bot.recorder.nextRequestId());
        byte[] send = QQAgreementUtils.encodeRequest(bot, ssoSeq, "StatSvc.SetStatusFromClient", source);
        Objects.requireNonNull(getClient()).send(send);
    }

    /**
     * @author luoluo
     * @date 2020/8/14
     * @param content 回复内容
     * @desc 设置自动回复内容
     */
    public void setAutoReply(String... content){
        oidbSsoPkg pkg = new oidbSsoPkg();
        pkg.command = 0xcd5;
        pkg.service_type = 0;
        pkg.client_version = "android 8.4.1";
        oidb_0xcd5.Req cd5 = new oidb_0xcd5.Req();
        cd5.service_type = 2;
        oidb_0xcd5.SetDataReq setdata = new oidb_0xcd5.SetDataReq();
        wording_storage.WordingCfg words = new wording_storage.WordingCfg();
        words.auto_reply_flag = 1;
        words.select_id = 1;
        for(String text : content){
            words.addReplyMessage(text);
        }
        setdata.data = words;
        cd5.setdata_req = setdata;
        pkg.bodybuffer = cd5.toByteArray();
        TCPSocket client = getClient();
        assert client != null;
        client.send( QQAgreementUtils.encodeRequest(bot, bot.recorder.nextSsoSeq(), "OidbSvc.0xcd5", pkg.toByteArray()) );
    }

    /**
     * @author luoluo
     * @date 2020/8/14
     * @return org.artqq.utils.TCPSocket
     * @desc 获取连接器
     */
    private TCPSocket getClient(){
        if(bot.data.containsKey("client")){
            return (TCPSocket) bot.data.get("client");
        }
        return null;
    }
    
    private enum ModifyCard {
        Success("修改成功"),
        CardLenghtError("卡片长度有误"),
        UnauthorizedOperation("无权操作"),
        InputParameterError("输入参数有误");
        ModifyCard(String text){
            this.text = text;
        }
        public final String text;
    }
    
    private enum FavoriteEnum {
        Success("点赞成功"),
        TimeError("点赞次数错误"),
        TimeMax("点赞上限"),
        LimitFavorite("点赞错误");

        FavoriteEnum(String text){
            this.text = text;
        }

        public final String text;
    }
    
    private enum GroupBan {
        SendSuccess("禁言发送成功"),
        BanTimeError("禁言时间错误"),
        GroupCodeError("群号错误"),
        UinError("被禁言人账号错误");
        
        GroupBan(String text){
            this.text = text;
        }
        
        public final String text;
    }

    private enum WithdrawMsg {
        Success("撤回成功"),
        Error("撤回失败");

        WithdrawMsg(String text){
            this.text = text;
        }

        public final String text;
    }
}
