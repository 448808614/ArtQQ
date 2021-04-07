package org.artqq;

import com.google.gson.JsonArray;
import com.qq.jce.wup.UniPacket;
import org.artbot.HandleMessage;
import org.artbot.Messenger;
import org.artbot.MsgFrom;
import org.artqq.jce.online.MsgInfo;
import org.artqq.jce.online.PushMsg;
import org.artqq.oidb.oidbSsoPkg;
import org.artqq.protobuf.*;
import org.artqq.utils.HexUtil;
import org.artqq.utils.TCPSocket;
import org.artqq.utils.bytes.ByteObject;
import org.artqq.utils.zLibUtils;
import org.helper.Desc;

import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;


public class ReceiveData {
    public Vector<HandlePacket> vector = new Vector<>();
    public ArtBot bot;
    private long uin;
    private final ConcurrentHashMap<Long, List<Long>> fmsgSeqs = new ConcurrentHashMap<>();

    public static final String[] gone = {
        "ConfigPushSvc.PushReq",
        "ConfigPushSvc.PushDomain"
    }; //可以直接无视的包
    
    public static final String[] MessagePacket = {
        "OnlinePush.PbPushGroupMsg",
        "MessageSvc.PbGetMsg",
        "OnlinePush.PbPushTransMsg"
    }; // 消息处理包

    public static final String[] FunctionPacket = {
        "OnlinePush.ReqPush"
        // "OidbSvc.0x55c_1"
    }; // 行为处理包

    private static List<Integer> divs = new ArrayList<>();

    public ReceiveData(ArtBot bot){
        this.bot = bot;
        uin = bot.getUin();
        ThreadManager.newInstance().addExecuteTask(new BuiltThread());
    }

    public void parseMessage(ByteObject bf){
        long Alen_ = bf.readInt();
        long pack_type = bf.readInt();
        byte enc_type = bf.readByte();
        bf.readByte();
        String qq = bf.readString(bf.readInt() - 4);
        if(!qq.equals(String.valueOf(uin))) return;
        byte[] key = null;
        switch(enc_type){
            case 0:
                System.out.println("ReceiveCmd：Heartbeat.Alive");
                // 裸包 不用理
                return;
            case 1:
                key = bot.recorder.getUseKey();
                break;
            case 2:
                key = new byte[16];
                break;
        }
        byte[] dec_body = bf.decryptRestData(key);
        ByteObject body_dec = new ByteObject( dec_body );
        ByteObject head_ = body_dec.readBytesObj( body_dec.readInt() - 4 ).toByteObject();
        int ssoSeq = head_.readIntNumber().toInt();
        head_.readBytes(8);
        final String cmdName = head_.readString(head_.readInt() - 4);
        if(Code.isDebug)
            System.out.println("ReceiveCmd：" + cmdName);
        if(isGoneHandle(cmdName)) return;
        byte[] cookie = head_.readBytes(head_.readInt()-4 );
        int InclusionType = head_.readIntNumber().toInt();
        long body_len = body_dec.readInt();
        byte[] src = body_dec.readBytes( body_len- 4);
        switch (InclusionType){
            case 0:
                break;
            case 4:
                break;
            case 1:
                src = zLibUtils.uncompress(src);
                break;
            default:
        }
        src = new ByteObject(src).putStartSize(4).toByteArray();
        if(src == null) return;
        // System.out.println(HexUtil.bytesToHexString(src));
        if(cmdName.equals("MessageSvc.PushNotify") && (boolean) bot.data.getOrDefault("handleFriendMsg", false)){
            GetMsg.getFriendMsg(bot);
            return;
        }
        int handle_id;
        boolean matches = false;
        for(int i = 0; i < vector.size(); i++){
            HandlePacket handle = vector.elementAt(i);
            handle.source = src.clone();
            handle.ssoSeq = ssoSeq;
            if(handle.cmd.equals(cmdName) && handle.helpJudgement() && !handle.is_over()){
                handle_id = handle.id;
                handle.over();
                remove(i, handle_id);
                matches = true;
                break;
            }else if(handle.is_over()){
                handle_id = handle.id;
                remove(i, handle_id);
                i--;
            }else{
                handle.source = null;
                handle.obj = null;
                handle.ssoSeq = 0;
            }
        }
        if(isMessagePacket(cmdName) && !matches){
            byte[] finalSrc = src.clone();

            if("MessageSvc.PbGetMsg".equals(cmdName)){
                GetMsg.Resp resp = GetMsg.decode(new ByteObject(finalSrc).disUseData(4).readRestBytes());
                assert resp != null;
                if(resp.result == 0){
                    bot.data.put("SyncCookie", resp.sync_cookie);
                    bot.data.put("PbAcCookie", resp.pub_account_cookie);
                    bot.data.put("MsgCtrlBuf", resp.msg_ctrl_buf != null ? resp.msg_ctrl_buf : new byte[0]);
                    if(resp.msgs != null){
                        for(GetMsg.Msgs m : resp.msgs){
                            for(msg_comm.Msg msg: m.msgs){
                                if( msg.msg_head.msg_time <= m.last_read_time || msg.msg_head.msg_time < System.currentTimeMillis() / 1000 ) {
                                    continue;
                                }
                                switch(msg.msg_head.msg_type){
                                    case 732:
                                        break;
                                    case 141:
                                    case 166 :{
                                        Messenger mm = ParseMsg(msg, cmdName);
                                        List<Long> list = fmsgSeqs.getOrDefault(mm.message_info.uinInfo.uinCode, new ArrayList<>());
                                        for(long code : list) if(code == mm.message_info.msgId) return;
                                        list.add((long) mm.message_info.msgId);
                                        fmsgSeqs.put(mm.message_info.uinInfo.uinCode, list);
                                        loadMsg(null, cmdName, null, mm);
                                        break;
                                    }
                                    case 34:
                                    case 33 :{
                                        long uin = msg.msg_head.auth_uin;
                                        int time = msg.msg_head.msg_time;
                                        ByteObject content = new ByteObject(msg.msg_body.msg_content);
                                        if(uin == bot.getUin()){
                                            // By acty.java
                                            System.out.println("33 is error");


                                        }else{
                                            long groupCode = msg.msg_head.from_uin;
                                            long groupId = content.readInt();
                                            byte value = content.readByte();
                                            int type = content.disUseData(4).readByte() & 0xff;
                                            if(type == 0x83){
                                                // 邀请入群
                                                GroupFunctionNotice notice = new GroupFunctionNotice();
                                                notice.admin = content.readInt();
                                                notice.uin = uin;
                                                notice.name = msg.msg_head.auth_nick == null ? msg.msg_head.from_nick.getBytes() : msg.msg_head.auth_nick.getBytes();
                                                notice.groupid = groupId;
                                                notice.time = time;
                                                notice.type = 3;
                                                loadMsg(null, cmdName, notice, null);
                                            }else if(type == 0x82 || type == 0x2){
                                                // 主动入群
                                                GroupFunctionNotice notice = new GroupFunctionNotice();
                                                notice.admin = uin;
                                                notice.uin = uin;
                                                notice.name = msg.msg_head.auth_nick == null ? msg.msg_head.from_nick.getBytes() : msg.msg_head.auth_nick.getBytes();
                                                notice.groupid = groupId;
                                                notice.time = time;
                                                notice.type = 2;
                                                loadMsg(null, cmdName, notice, null);
                                                // } else if(type == 0x2){
                                                // 回答问题后主动加群
                                            } else {
                                                System.out.println("Unkonw PbGetMsg Type:" + type);
                                            }
                                        }
                                        break;
                                    }
                                    case 84:{
                                        // 有人申请进群
                                        System.out.println(HexUtil.bytesToHexString(msg.msg_body.msg_content));
                                        ByteObject content = new ByteObject(msg.msg_body.msg_content);
                                        long groupId = content.readInt();
                                        byte status = content.readByte();
                                        long uin = content.readInt();
                                        
                                        break;
                                    }
                                    default:
                                        System.out.println("Unkonwn C2C Type：" + msg.msg_head.msg_type);
                                        break;
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("MessageSvc.PbGetMsg.result isn't 0");
                }
                DeleteMsg.deleteFriendMsg(bot, resp.msgs);
                return;
            }else if("OnlinePush.PbPushTransMsg".equals(cmdName)){
                PushTrans pushTrans = PushTrans.decode(finalSrc);
                // System.out.println(pushTrans.toString());
                if(hasSameMsg(pushTrans.msg_seq, -2020_8_29)){ return; }
                if(pushTrans.msg_type == 44){
                    ByteObject data = new ByteObject(pushTrans.msg_data);
                    long groupid = data.readInt();
                    byte value = data.readByte();
                    byte status = data.readByte();
                    if(status == 0 || status == 1){
                        long uin = data.readInt();
                        GroupFunctionNotice groupFunctionNotice = new GroupFunctionNotice();
                        groupFunctionNotice.type = pushTrans.msg_type;
                        groupFunctionNotice.uin = uin;
                        groupFunctionNotice.groupid = groupid;
                        groupFunctionNotice.value = status > 0 ? 1 : 0;
                        loadMsg(null, cmdName, groupFunctionNotice, null);
                    }
                }else if(pushTrans.msg_type == 34){
                    ByteObject data = new ByteObject(pushTrans.msg_data);
                    long groupid = data.readInt();
                    byte value = data.readByte();
                    long uin = data.readInt();
                    int type = data.readByte() & 0xff;
                    if( type == 0x83 ){
                        GroupFunctionNotice notice = new GroupFunctionNotice();
                        notice.type = 4;
                        notice.groupid = groupid;
                        notice.admin = data.readInt();
                        notice.uin = uin;
                        notice.name = pushTrans.nick_name == null ? new byte[0] : pushTrans.nick_name.getBytes();
                        notice.time = pushTrans.real_msg_time;
                        loadMsg(null, cmdName, notice, null);
                    } else if( type == 0x82 ){
                        GroupFunctionNotice notice = new GroupFunctionNotice();
                        notice.type = 1;
                        notice.groupid = groupid;
                        notice.admin = uin;
                        notice.uin = uin;
                        notice.name = pushTrans.nick_name == null ? new byte[0] : pushTrans.nick_name.getBytes();
                        notice.time = pushTrans.real_msg_time;
                        loadMsg(null, cmdName, notice, null);
                    }
                }
                return;
            }
            final byte[] source = finalSrc.clone();
            final msg_onlinepush resp = msg_onlinepush.decode(new ByteObject(source).disUseData(4).readRestBytes());
            assert resp != null;
            if(resp.msg.content_head.pkg_num > resp.msg.content_head.pkg_index + 1 && !isSameMsg(resp.msg.content_head.div_seq)){ // 分段解析
                final int div_seq = resp.msg.content_head.div_seq;
                final List<MessageSvc.MsgBody.RichText> list = new ArrayList<>();
                list.add(resp.msg.content_head.pkg_index, resp.msg.msg_body.rich_text);
                ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket(cmdName){
                    @Override
                    public boolean helpJudgement() {
                        msg_onlinepush rsp = msg_onlinepush.decode(new ByteObject(source).disUseData(4).readRestBytes());
                        if(rsp.msg.content_head.div_seq == div_seq){
                            list.add(rsp.msg.content_head.pkg_index, rsp.msg.msg_body.rich_text);
                            if(list.size() == rsp.msg.content_head.pkg_num){
                                for(MessageSvc.MsgBody.RichText richText : list){
                                    resp.msg.msg_body.rich_text.elems.addAll(richText.elems);
                                }
                                loadMsg(resp, cmdName, null, null);
                                return true;
                            }
                        }
                        return false;
                    }
                });
            }else{
                loadMsg(resp, cmdName, null, null);
            }
        } else if(isFunctionPacket(cmdName) && !matches){
            byte[] finalSrc = src.clone();
                /*if(cmdName.equals(FunctionPacket[1])){
                    oidbSsoPkg oidbSsoPkg = org.artqq.oidb.oidbSsoPkg.decode(finalSrc);
                    ByteObject source = new ByteObject(oidbSsoPkg.bodybuffer);
                    long groupId = source.readInt();
                    long uin = source.readInt();
                    int value = source.readBoolean() ? 1 : 0;
                    GroupFunctionNotice notice = new GroupFunctionNotice();
                    notice.type = 44;
                    notice.uin = uin;
                    notice.value = value;
                    notice.groupid = groupId;
                    loadMsg(null, "OnlinePush.ReqPush", notice, null);
                    return;
                }*/
            UniPacket uni = new UniPacket(true);
            uni.setEncodeName("utf-8");
            uni.decode(finalSrc);
            PushMsg m = uni.getByClass("req", new PushMsg());
            long msgTime = m.uMsgTime;
            for(MsgInfo info : m.vMsgInfos){
                if(hasSameMsg(info.msgSeq, -2020_08_28)) continue;
                GroupFunctionNotice notice = new GroupFunctionNotice();
                long groupid = info.fromUin;
                ByteObject cookies = new ByteObject(info.vMsg);
                long ck_groupid = cookies.readInt();
                int type = cookies.readByte() & 0xff;
                long uin = -1;
                if(cookies.readByte() == 1){
                    uin = cookies.readInt();
                }else{
                    switch(type){
                        case 0x10:{
                            int len = cookies.readByteNumber().toInt();
                            byte[] pb_src = cookies.readBytes(len);
                            NotifyMsg notifyMsg = NotifyMsg.decode(pb_src);
                            assert notifyMsg != null;
                            if(notifyMsg.msg_graytips != null) {
                                if(notifyMsg.service_type == -1 && notifyMsg.type == 1) {
                                    if(notifyMsg.msg_graytips.content.contains("坦白说")){
                                        notice.groupid = ck_groupid;
                                        notice.admin = 0;
                                        notice.type = 0x10;
                                        notice.value = notifyMsg.msg_graytips.content.contains("开") ? 1 : 0;
                                    }else if(notifyMsg.msg_graytips.content.contains("群内临时会话")){
                                        notice.groupid = ck_groupid;
                                        notice.admin = notifyMsg.msg_graytips.content.contains("群主") ? 1 : 0;
                                        notice.type = 55;
                                        notice.value = notifyMsg.msg_graytips.content.contains("禁止") ? 0 : 1;
                                    }else if(notifyMsg.msg_graytips.content.contains("新的群聊")){
                                        notice.groupid = ck_groupid;
                                        notice.admin = notifyMsg.msg_graytips.content.contains("群主") ? 1 : 0;
                                        notice.type = 59;
                                        notice.value = notifyMsg.msg_graytips.content.contains("禁止") ? 0 : 1;
                                    }
                                    System.out.println(notifyMsg.msg_graytips.content);
                                } else if(notifyMsg.service_type == 12) {
                                    notice.groupid = ck_groupid;
                                    notice.admin = notifyMsg.sender_uin;
                                    notice.type = 46;
                                    notice.name = notifyMsg.msg_graytips.content.getBytes();
                                }
                            }
                            else if(notifyMsg.type == 24 && notifyMsg.service_type == 23 && notifyMsg.msg_group_info_change != null){
                                notice.admin = notifyMsg.sender_uin;
                                notice.groupid = groupid;
                                notice.type = 76;
                                if(notifyMsg.msg_group_info_change.group_flagext3 == 1 || notifyMsg.msg_group_info_change.group_flagext3 == -1){
                                    notice.type = -123456786;
                                }else{
                                    notice.value = notifyMsg.msg_group_info_change.group_flagext3 > 1000000000 ? 0 : 1;
                                }
                            }
                            loadMsg(null, cmdName, notice, null);
                            break;
                        }
                        case 15 :{ // 上传控制
                            int status = cookies.disUseData(1).readShort();
                            int wType = cookies.readIntNumber().toInt();
                            if(wType == 1){
                                notice.admin = 0;
                                notice.value = status == 1 ? 0 : 1;
                                notice.groupid = groupid;
                                notice.type = 24;
                            }else if(wType == 2){
                                notice.admin = 0;
                                notice.value = status > 1 ? 0 : 1;
                                notice.groupid = groupid;
                                notice.type = 25;
                            }
                            loadMsg(null, cmdName, notice, null);
                            break;
                        }
                        case 17:{
                            byte[] pb_src = cookies.readBytes(cookies.readByteNumber().toInt());
                            NotifyMsg notifyMsg = NotifyMsg.decode(pb_src);
                            assert notifyMsg != null;
                            notice.groupid = notifyMsg.group_code;
                            notice.value = notifyMsg.msg_recall.info.get(0).seq;
                            notice.admin = notifyMsg.msg_recall.uin;
                            notice.uin = notifyMsg.msg_recall.info.get(0).from_uin;
                            notice.type = 17;
                            notice.time = notifyMsg.msg_recall.info.get(0).time;
                            loadMsg(null, cmdName, notice, null);
                            break;
                        }
                        case 20:{
                            break;
                        }
                        default:
                            System.out.println("Unknown function:" + type);
                    }
                    return;
                }
                long endTime = cookies.readInt();
                int otype = cookies.readShort();
                switch(type){
                    case 0xc:{ // 群禁言
                        notice.groupid = ck_groupid;
                        notice.type = 0xc;
                        notice.admin = uin;
                        notice.uin = cookies.readInt();
                        int ban = (int) cookies.readInt();
                        notice.value = notice.uin == 0 ? (ban > 0 ? 1 : 0) : ban;
                        loadMsg(null, cmdName, notice, null);
                        break;
                    }
                    case 0xe:{ // 匿名开关
                        notice.groupid = ck_groupid;
                        notice.type = 0xe;
                        notice.admin = uin;
                        notice.value = endTime > System.currentTimeMillis() / 1000 ? 0 : 1;
                        loadMsg(null, cmdName, notice, null);
                        break;
                    }
                    default:{
                    }
                }

            }
        }



    }

    public boolean hasSameMsg(long seq, long key){
        List<Long> msgSeqs = fmsgSeqs.getOrDefault(key, new ArrayList<>());
        for(long s : msgSeqs){
            if(s == seq)
                return true;
        }
        msgSeqs.add(seq);
        fmsgSeqs.put(key, msgSeqs);
        return false;
    }

    public void loadMsg(final msg_onlinepush resp, String cmdName, final GroupFunctionNotice functionNotice, Messenger m){
        ThreadManager.newInstance().addExecuteTask(new Thread("HandleMsg"){
            @Override
            public void run(){
                try {
                    if(bot.data.containsKey("handleMsg")){
                        Vector<HandleMessage> handles = (Vector<HandleMessage>) bot.data.get("handleMsg");
                        boolean isConcurrent = (boolean) bot.data.get("isConcurrent");
                        for(int i = handles.size() - 1; i >= 0; i--){
                            final HandleMessage handle = handles.get(i);
                            handle.bot = bot;
                            Messenger msg = resp != null ? ParseMsg(resp.msg, cmdName) : m;
                            if(isConcurrent){
                                ThreadManager.newInstance().addExecuteTask(new Thread("HandleMsg"){
                                    @Override
                                    public void run(){
                                        switch(cmdName){
                                            case "OnlinePush.PbPushGroupMsg":{
                                                if(msg == null) {
                                                    return;
                                                }
                                                handle.handleGroupMessage(msg, msg.message_info.uinInfo.uinCode, msg.message_info.groupInfo.groupCode, msg.message_info.msgId, msg.toString());
                                                break;
                                            }
                                            case "OnlinePush.PbPushTransMsg":
                                            case "OnlinePush.ReqPush":{
                                                switch(functionNotice.type){
                                                    case 4:
                                                    case 1:{
                                                        handle.handleGroupMemberChange(functionNotice.type, functionNotice.groupid, functionNotice.admin, functionNotice.uin, new String(functionNotice.name), functionNotice.time);
                                                        break;
                                                    }
                                                    case 0xc:{
                                                        handle.handleGroupBan(functionNotice.groupid, functionNotice.admin, functionNotice.uin, functionNotice.value);
                                                        break;
                                                    }
                                                    case 17:{
                                                        handle.handleGroupWithDraw(functionNotice.groupid, functionNotice.admin, functionNotice.value, functionNotice.time, functionNotice.uin);
                                                        break;
                                                    }
                                                    case 24:
                                                    case 25:
                                                    case 0x10:
                                                    case 76:
                                                    case 55:
                                                    case 59:
                                                    case 0xe:{
                                                        handle.handleGroupFunctionSwitch(functionNotice.groupid, functionNotice.admin, functionNotice.type, functionNotice.value);
                                                        break;
                                                    }
                                                    case 44:{
                                                        handle.handleGroupFunctionSwitch(functionNotice.groupid, functionNotice.uin, functionNotice.type, functionNotice.value);
                                                        break;
                                                    }
                                                    case 46:{
                                                        handle.handleGroupNameChange(functionNotice.groupid, functionNotice.admin, new String(functionNotice.name));
                                                        break;
                                                    }

                                                    default:
                                                        throw new IllegalStateException("Unexpected value: " + functionNotice.type);
                                                } // switch
                                                break;
                                            }
                                            case "MessageSvc.PbGetMsg":{
                                                if(msg == null){
                                                    switch(functionNotice.type){
                                                        case 3:
                                                        case 2: {
                                                            handle.handleGroupMemberChange(functionNotice.type, functionNotice.groupid, functionNotice.admin, functionNotice.uin, new String(functionNotice.name), functionNotice.time);
                                                            break;
                                                        }
                                                    }
                                                }else {
                                                    if(msg.message_info.msgFrom == MsgFrom.Friend) {
                                                        handle.handleFriendMessage(msg, msg.message_info.uinInfo.uinCode, msg.message_info.msgId, msg.toString());
                                                    } else if(msg.message_info.msgFrom == MsgFrom.Temporary) {
                                                        handle.handleTemporaryMessage(msg, msg.message_info.uinInfo.uinCode, msg.message_info.msgId, msg.toString());
                                                    }
                                                }
                                                break;
                                            }
                                            default:
                                        }
                                    }
                                });
                            }else{
                                int ret = HandleMessage.GoneMessage;
                                switch(cmdName){
                                    case "OnlinePush.PbPushGroupMsg":{
                                        if(msg == null) {
                                            return;
                                        }
                                        ret = handle.handleGroupMessage(msg, msg.message_info.uinInfo.uinCode, msg.message_info.groupInfo.groupCode, msg.message_info.msgId, msg.toString());
                                        break;
                                    }
                                    case "OnlinePush.PbPushTransMsg":
                                    case "OnlinePush.ReqPush":{
                                        switch(functionNotice.type){
                                            case 4:
                                            case 1:{
                                                handle.handleGroupMemberChange(functionNotice.type, functionNotice.groupid, functionNotice.admin, functionNotice.uin, new String(functionNotice.name), functionNotice.time);
                                                break;
                                            }
                                            case 0xc:{
                                                handle.handleGroupBan(functionNotice.groupid, functionNotice.admin, functionNotice.uin, functionNotice.value);
                                                break;
                                            }
                                            case 17:{
                                                handle.handleGroupWithDraw(functionNotice.groupid, functionNotice.admin, functionNotice.value, functionNotice.time, functionNotice.uin);
                                                break;
                                            }
                                            case 24:
                                            case 25:
                                            case 0x10:
                                            case 76:
                                            case 55:
                                            case 59:
                                            case 0xe:{
                                                handle.handleGroupFunctionSwitch(functionNotice.groupid, functionNotice.admin, functionNotice.type, functionNotice.value);
                                                break;
                                            }
                                            case 44:{
                                                handle.handleGroupFunctionSwitch(functionNotice.groupid, functionNotice.uin, functionNotice.type, functionNotice.value);
                                                break;
                                            }
                                            case 46:{
                                                handle.handleGroupNameChange(functionNotice.groupid, functionNotice.admin, new String(functionNotice.name));
                                                break;
                                            }
                                            default:
                                                throw new IllegalStateException("Unexpected value: " + functionNotice.type);
                                        } // switch
                                        break;
                                    }
                                    case "MessageSvc.PbGetMsg":{
                                        if(msg == null){
                                            switch(functionNotice.type){
                                                case 3:
                                                case 2: {
                                                    handle.handleGroupMemberChange(functionNotice.type, functionNotice.groupid, functionNotice.admin, functionNotice.uin, new String(functionNotice.name), functionNotice.time);
                                                    break;
                                                }
                                            }
                                        }else {
                                            if(msg.message_info.msgFrom == MsgFrom.Friend) {
                                                ret = handle.handleFriendMessage(msg, msg.message_info.uinInfo.uinCode, msg.message_info.msgId, msg.toString());
                                            } else if(msg.message_info.msgFrom == MsgFrom.Temporary) {
                                                ret = handle.handleTemporaryMessage(msg, msg.message_info.uinInfo.uinCode, msg.message_info.msgId, msg.toString());
                                            }
                                        }
                                        break;
                                    }
                                    default:
                                }
                                if(ret == HandleMessage.HijackMessage){
                                    return;
                                }
                            } // if(isConcurrent)
                        } // for
                    }
                }catch(Throwable th){
                    System.out.println("ParseMessageError：" + th);
                    th.printStackTrace();
                }
            } // run()
        });
    }

    @Desc(desc = "解析消息体")
    public Messenger ParseMsg(msg_comm.Msg msg_src, String cmdName){
        Messenger msg = new Messenger(bot);
        msg.message_info.msgId = msg_src.msg_head.msg_seq;
        msg.message_info.msgTime = Long.valueOf(msg_src.msg_head.msg_time);
        msg.message_info.uinInfo.otherInfo.addProperty("appid", msg_src.msg_head.from_instid);
        boolean isAction = false;
        switch(cmdName){
            case "MessageSvc.PbGetMsg":{
                if(msg_src.msg_head.msg_type == 141){
                    msg.message_info.uinInfo.uinCode = msg_src.msg_head.from_uin;
                    msg.message_info.groupInfo.groupCode = msg_src.msg_head.c2c_tmp_msg_head.groupCode;
                    msg.message_info.msgFrom = MsgFrom.Temporary;
                }else if(msg_src.msg_head.msg_type == 166){
                    msg.message_info.uinInfo.uinCode = msg_src.msg_head.from_uin;
                    msg.message_info.msgFrom = MsgFrom.Friend;
                }
                break;
            }
            case "OnlinePush.PbPushGroupMsg" :{
                msg.message_info.uinInfo.uinCode = msg_src.msg_head.from_uin;
                msg.message_info.uinInfo.uinName = msg_src.msg_head.group_info.group_card;
                msg.message_info.uinInfo.groupCard = msg_src.msg_head.group_info.group_card;
                msg.message_info.groupInfo.groupCode = msg_src.msg_head.group_info.group_code;
                msg.message_info.groupInfo.groupName = msg_src.msg_head.group_info.group_name;
                msg.message_info.msgFrom = MsgFrom.Group;
                break;
            } // case msg
        }

        {
            /* 开始消息 */
            boolean isReadReply = false;
            long replyUin = -1;

            if(msg_src.msg_body.rich_text.ptt != null) {
                isAction = true;
                msg.addMsg("ptt", msg_src.msg_body.rich_text.ptt.file_name.replaceAll("[{]|[}]|[-]", ""), "magic", msg_src.msg_body.rich_text.ptt.pb_reserve.change_voice != 0 ? "true" : "false", "time", String.valueOf(msg_src.msg_body.rich_text.ptt.time));
            }

            for(int i = 0; i < msg_src.msg_body.rich_text.elems.size(); i++) {
                MessageSvc.MsgBody.RichText.Elem elem = msg_src.msg_body.rich_text.elems.get(i);
                if(elem.src_msg != null) {
                    msg.message_info.replyId = elem.src_msg.orig_seqs;
                    msg.message_info.replyMsg = elem.src_msg.elems.data.name;
                    isReadReply = true;
                    msg.message_info.replyUin = elem.src_msg.sender_uin;
                } else if(elem.text != null) {
                    if(elem.text.attr_6_buf != null) {
                        ByteObject bo = new ByteObject(elem.text.attr_6_buf);
                        int startPos = bo.readShort();
                        long strLen = bo.readInt();
                        byte flag = bo.readByte();
                        long uin = bo.readInt();
                        if(isReadReply) {
                            isReadReply = false;
                        } else {
                            msg.addMsg("at", String.valueOf(uin), "msg", elem.text.str, "flag", String.valueOf(flag));
                        }
                    } else {
                        msg.addMsg("msg", elem.text.str);
                    }
                } else if(elem.custom_face != null) {
                    int animation = 0;
                    if(elem.custom_face.pb_reserve != null && elem.custom_face.pb_reserve.msg_image_show != null) {
                        animation = elem.custom_face.pb_reserve.msg_image_show.int32_effect_id;
                    }
                    String[] m;
                    if(animation == 0) {
                        m = new String[]{"img", elem.custom_face.str_file_path.replaceAll("[{]|[}]|[-]", "")};
                    } else if(!isAction) {
                        m = new String[]{"img", elem.custom_face.str_file_path.replaceAll("[{]|[}]|[-]", ""), "animation", String.valueOf(animation)};
                        isAction = true;
                    } else {
                        continue;
                    }
                    msg.addMsg(m);
                } else if(elem.common_elem != null) switch(elem.common_elem.service_type) {
                    case 2: { // 戳一戳
                        hummer_commelem.MsgElemInfo_servtype2 t2 = hummer_commelem.MsgElemInfo_servtype2.decode(elem.common_elem.pb_elem);
                        assert t2 != null;
                        msg.addMsg("poke", String.valueOf(t2.poke_type), "id", String.valueOf(t2.vaspoke_id), "size", String.valueOf(t2.poke_strength), "name", new String(t2.vaspoke_name));
                        isAction = true;
                        break;
                    }
                    case 3: { // 闪图
                        hummer_commelem.MsgElemInfo_servtype3 t3 = hummer_commelem.MsgElemInfo_servtype3.decode(elem.common_elem.pb_elem);
                        assert t3 != null;
                        msg.addMsg("img", t3.flash_troop_pic.str_file_path, "flash", "true");
                        isAction = true;
                        break;
                    }
                    case 14: {
                        hummer_commelem.MsgElemInfo_servtype14 t14 = hummer_commelem.MsgElemInfo_servtype14.decode(elem.common_elem.pb_elem);
                        assert t14 != null;
                        msg.addMsg("flashMsg", t14.getText(), "id", String.valueOf(t14.id));
                        isAction = true;
                        break;
                    }
                    case 23: {
                        hummer_commelem.MsgElemInfo_servtype23 t23 = hummer_commelem.MsgElemInfo_servtype23.decode(elem.common_elem.pb_elem);
                        assert t23 != null;
                        msg.addMsg("animation", String.valueOf(t23.face_type), "size", String.valueOf(t23.face_bubble_count), "name", new String(t23.face_summary));
                        isAction = true;
                        break;
                    }
                    case 33: {
                        hummer_commelem.MsgElemInfo_servtype33 t33 = hummer_commelem.MsgElemInfo_servtype33.decode(elem.common_elem.pb_elem);
                        assert t33 != null;
                        msg.addMsg("bface", String.valueOf(t33.index), "name", new String(t33.text));
                        break;
                    }
                } // case comm
                else if(elem.shake_window != null) {
                    msg.addMsg("shake", "");
                    isAction = true;
                } else if(elem.face != null) {
                    msg.addMsg("face", String.valueOf(elem.face.index));
                } else if(elem.light_app != null) {
                    msg.addMsg("json", elem.light_app.getText());
                    isAction = true;
                } else if(elem.rich_msg != null) {
                    msg.addMsg("xml", elem.rich_msg.getText(), "serviceID", String.valueOf(elem.rich_msg.service_id));
                    isAction = true;
                } else if(elem.market_face != null) {
                    if(elem.market_face.tab_id == 11464) {
                        String v = String.valueOf(Integer.valueOf(new String(elem.market_face.mobileparam).split("value=")[1].split(";")[0].trim()) + 1);
                        msg.addMsg("dice", v);
                    } else {
                        msg.addMsg("mface", String.valueOf(elem.market_face.tab_id));
                    }
                    isAction = true;
                } else if(elem.general_flags != null) {
                    JsonArray ids = new JsonArray();
                    if(elem.general_flags.pb_reserve != null && elem.general_flags.pb_reserve.hudong_mark != null) {
                        if(elem.general_flags.pb_reserve.hudong_mark.id != null) {
                            for(int id : elem.general_flags.pb_reserve.hudong_mark.id) {
                                ids.add(id);
                            }
                        }
                        msg.message_info.uinInfo.otherInfo.addProperty("level", elem.general_flags.pb_reserve.hudong_mark.level);
                    } else {
                        msg.message_info.uinInfo.otherInfo.addProperty("level", 0);
                    }
                    msg.message_info.uinInfo.otherInfo.add("id", ids);
                    if(elem.general_flags.long_text_resid != null) {
                        msg.message_info.uinInfo.otherInfo.addProperty("long_msg_resid", elem.general_flags.long_text_resid);
                        msg.message_info.uinInfo.otherInfo.addProperty("long_msg_flag", elem.general_flags.long_text_flag);
                    }
                } else if(elem.anon_group_msg != null) {
                    msg.message_info.isAnonymous = true;
                    msg.message_info.uinInfo.otherInfo.addProperty("anon_nick", elem.anon_group_msg.anon_nick);
                    msg.message_info.uinInfo.otherInfo.addProperty("expire_time", elem.anon_group_msg.expire_time);
                    msg.message_info.uinInfo.otherInfo.addProperty("head_portrait", elem.anon_group_msg.head_portrait);
                    msg.message_info.uinInfo.otherInfo.addProperty("rank_color", elem.anon_group_msg.rank_color);
                    if(bot.data.containsKey("AnonyName")) {
                        String anony_name = String.valueOf(bot.data.getOrDefault("AnonyName", ""));
                        msg.message_info.isMyAnonymous = anony_name.equals(elem.anon_group_msg.anon_nick);
                    }
                }


            }
        }

        while(isAction){
            Iterator<Map<String, ArrayList<String>>> iterator = msg.message_list.iterator();
            while (iterator.hasNext()){
                Map<String, ArrayList<String>> msg_package = iterator.next();
                ArrayList<String> indexs = msg_package.get("index");
                String msg_main = msg_package.get(indexs.get(0)).get(0) == null ? "" : msg_package.get(indexs.get(0)).get(0);
                String index = indexs.get(0);
                if(index.equals("msg") || index.equals("face") || index.equals("at")){
                    iterator.remove();
                }
            }
            break;
        }

        return msg;
    }
    
    public void remove(int k, int id){
        HandlePacket handle = vector.elementAt(k);
        if(handle != null && handle.id == id){
            vector.removeElementAt(k);
        }else if(handle == null){
            vector.removeIf(next -> next.id == id);
        }
    }
    
    public static boolean isMessagePacket(String cmdName){
        for(String n : MessagePacket){
            if(n.equals(cmdName.trim()))
                return true;
        }
        return false;
    }

    public static boolean isFunctionPacket(String cmdName){
        for(String n : FunctionPacket){
            if(n.equals(cmdName.trim()))
                return true;
        }
        return false;
    }

    public static boolean isGoneHandle(String cmdName){
        for(String n : gone){
            if(n.equals(cmdName.trim()))
                return true;
        }
        return false;
    }

    public static boolean isSameMsg(int div){
        for(int n : divs){
            if(n == div)
                return true;
        }
        return false;
    }

    public class BuiltThread extends Thread {
        @Override
        public void run() {
            boolean isWhile = true;
            while(isWhile){
                try{
                    if(bot.bot_type == -1){
                        break;
                    }else if(bot.data.containsKey("client")){
                        TCPSocket client = (TCPSocket) bot.data.get("client");
                        byte[] data = client.read(10000);
                        if(data != null){
                            ByteObject obj = new ByteObject(data);
                            parseMessage(obj);
                        }
                    }
                }catch(Throwable e){
                    if(e instanceof java.net.SocketException){
                        if(bot.bot_type != -1){
                            isWhile = false;
                            bot.bot_type = 0;
                            System.out.println("正在尝试重新连接服务器...");
                            reRegister();
                        }
                    }else if(e instanceof SocketTimeoutException){}
                    else if(Code.isDebug)
                        e.printStackTrace();
                }
            }
        }
        
        public void reRegister(){
            int time = 0;
            while(bot.bot_type == 0){
                System.out.println("第" + time + "次重新连接：" + bot.login() );
                try {
                    sleep(5000);
                } catch (InterruptedException e) {}
                time++;
            }
        }
        
    }
    
    public static class HandlePacket {
        public HandlePacket(String cmdName){
            this.cmd = cmdName;
        }
        
        public int id = new Random().nextInt();
        public String cmd;
        public byte[] source;
        // 源代码
        public boolean over = false;
        public BlockingQueue queue = new ArrayBlockingQueue(1);
        public Object obj;
        public int ssoSeq;
        
        public boolean helpJudgement(){
            return true;
        }
        
        public void over(){
            over = true;
            try {
                queue.put("End");
            } catch (InterruptedException e) {}
        }
        
        public boolean is_over(){
            return over;
        }
        
        public void waitPacket(){
            try {
                Thread.sleep(600);
                if(!is_over())
                    queue.take();
            } catch (InterruptedException e) {}
        }
    }

    private final static class GroupFunctionNotice {
        public long groupid;
        public long uin;
        public int value;
        public int time;
        public int type;
        public long admin;
        public byte[] name;
    }
    
}

