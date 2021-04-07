package org.artbot;

import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.artbot.util.ArtUtil;
import org.artqq.ArtBot;
import org.artqq.Code;
import org.artqq.MD5;
import org.artqq.ReceiveData;
import org.artqq.oidb.oidb_0x352;
import org.artqq.oidb.oidb_0x388;
import org.artqq.oidb.oidb_0x3bb;
import org.artqq.protobuf.MessageSvc;
import org.artqq.protobuf.highway.*;
import org.artqq.protobuf.hummer_commelem;
import org.artqq.protobuf.msg_comm;
import org.artqq.utils.*;
import org.artqq.utils.bytes.ByteObject;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Messenger {
    public Messenger(ArtBot bot){
        this.bot = bot;
    }

    private final ArtBot bot;
    public final List<Map<String, ArrayList<String>>> message_list = new ArrayList<>();
    public final MessageInfo message_info = new MessageInfo();

    // 清空消息构建器
    public Messenger clearMsg(){
        message_list.clear();
        return this;
    }

    public String getText(String str){
        StringBuffer buffer = new StringBuffer();
        for (Map<String, ArrayList<String>> stringArrayListMap : message_list) {
            ArrayList<String> strings = stringArrayListMap.get(str);
            if (strings != null) {
                for (String string : strings) {
                    buffer.append(string);
                }
            }
        }
        return buffer.toString();
    }

    public String getTextMsg() {
        return getText("msg");
    }

    public List<String> getMsg(String key){
        List<String> l = new ArrayList<>();
        for (Map<String, ArrayList<String>> stringArrayListMap : message_list) {
            ArrayList<String> strings = stringArrayListMap.get(key);
            if (strings != null) {
                l.addAll(strings);
            }
        }
        return l;
    }

    public Messenger addMsg(String... str_arr){
        HashMap<String, ArrayList<String>> hash_str_arr = new HashMap<>();
        for (int i = 0;i < str_arr.length;i++){
            if (i + 2 <= str_arr.length){
                String key = str_arr[i], msg = str_arr[++i];
                ArrayList<String> index = null;
                if( hash_str_arr.containsKey("index")) {
                    index = hash_str_arr.get("index");
                } else {
                    index = new ArrayList<>();
                    hash_str_arr.put("index", index);
                }
                index.add(key);
                ArrayList<String> list = null;
                if( hash_str_arr.containsKey(key)) {
                    list = hash_str_arr.get(key);
                } else {
                    list = new ArrayList<>();
                    hash_str_arr.put(key, list);
                }
                list.add(msg);
            }
        }
        message_list.add(hash_str_arr);
        return this;
    }

    public Messenger addMsgOnTop(String... sarr){
        HashMap<String, ArrayList<String>> hash_str_arr = new HashMap<String, ArrayList<String>>();
        for (int i = 0;i < sarr.length;i++){
            if (i + 2 <= sarr.length){
                String key = sarr[i], msg = sarr[++i];
                ArrayList<String> index = null;
                if( hash_str_arr.containsKey("index")) {
                    index = hash_str_arr.get("index");
                } else {
                    index = new ArrayList<>();
                    hash_str_arr.put("index", index);
                }
                index.add(key);
                ArrayList<String> list = null;
                if( hash_str_arr.containsKey(key)) {
                    list = hash_str_arr.get(key);
                } else {
                    list = new ArrayList<>();
                    hash_str_arr.put(key, list);
                }
                list.add(msg);
            }
        }
        message_list.add(0, hash_str_arr);
        return this;
    }

    public <T> T getAddContent(String msg_name, String add_name, int Pos, T defaultResult){
        int i = 0;
        try{
            for(Map<String, ArrayList<String>> m : message_list){
                if(m.get(msg_name) != null && Pos == i){
                    String text = m.get(add_name) != null ? m.get(add_name).get(0) : String.valueOf(defaultResult);
                    if( defaultResult instanceof Boolean){
                        return (T) Boolean.valueOf( text );
                    }else if( defaultResult instanceof Integer){
                        return (T) Integer.valueOf( text );
                    }else if( defaultResult instanceof Long){
                        return (T) Long.valueOf( text );
                    }else if( defaultResult instanceof String){
                        return (T) String.valueOf( text );
                    }else if( defaultResult instanceof Byte){
                        return (T) Byte.valueOf( text );
                    }else if( defaultResult instanceof Short){
                        return (T) Short.valueOf( text );
                    }else if( defaultResult instanceof Float){
                        return (T) Float.valueOf( text );
                    }else if( defaultResult instanceof Double){
                        return (T) Double.valueOf( text );
                    }else{
                        return defaultResult;
                    }
                }
                i++;
            }
        }catch(Exception e){
            return defaultResult;
        }
        return defaultResult;
    }

    public int size(){
        return message_list.size();
    }

    public ArtBot getBot(){
        return bot;
    }

    public Messenger newInstance(){
        return new Messenger(bot);
    }

    public void addAll(List<Map<String, ArrayList<String>>> m_list){
        message_list.addAll(m_list);
    }

    public MessagerResult send(){
        // 不要给我组一些奇怪的包，出错了与我无关
        final MessageSvc.PbSendMsg msg = new MessageSvc.PbSendMsg();
        msg.routing_head = Util.makeRoutingHead(message_info);
        if(msg.routing_head == null){
            return MessagerResult.InputError;
        }
        msg.msg_body = new MessageSvc.MsgBody();
        msg.msg_body.rich_text = new MessageSvc.MsgBody.RichText();

        msg.msg_seq = bot.recorder.nextMessageSeq();
        msg.msg_rand = new Random().nextInt();
        switch(message_info.msgFrom){
            case Group:
                msg.msg_via = 1;
                break;
            case Friend:
                msg.msg_via = 2;
                break;
            default:
        }

        boolean hasAction = false;
        HashMap<Long, String> nicks;
        if(bot.data.containsKey("nicks")){
            nicks = (HashMap<Long, String>) bot.data.get("nicks");
        }else{
            nicks = new HashMap<Long, String>();
            bot.data.put("nicks", nicks);
        }
        List<MessageSvc.MsgBody.RichText.Elem> elems = new ArrayList<MessageSvc.MsgBody.RichText.Elem>();


        MessageSvc.MsgBody.RichText.Elem.GeneralFlags general_flags = new MessageSvc.MsgBody.RichText.Elem.GeneralFlags();

        if(message_info.replyId != null){
            if(message_info.replyMsg == null) {
                message_info.replyMsg = "";
            }
            MessageSvc.MsgBody.RichText.Elem source_elem = new MessageSvc.MsgBody.RichText.Elem();
            MessageSvc.MsgBody.RichText.Elem.SourceMsg source = new MessageSvc.MsgBody.RichText.Elem.SourceMsg();
            source.orig_seqs = message_info.replyId;
            source.to_uin = 0;
            source.sender_uin = message_info.replyUin;
            source.time = System.currentTimeMillis() / 1000;
            source.flag = 1;
            source.elems.data.name = message_info.replyMsg;
            source.type = 0;
            source_elem.src_msg = source;
            elems.add(source_elem);
        }

        if(message_info.isAnonymous){
            oidb_0x3bb.Body body = (oidb_0x3bb.Body) bot.data.get("AnonyBody");
            if(body == null || body.anony_rsp.expired_time <= System.currentTimeMillis()/1000 ){
                body = bot.getControl().generate_anonymous(msg.routing_head.to_uin);
            }
            if(body.anony_rsp.ret == 0 && body.anony_rsp.anony_status.forbid_talking == 0){
                bot.data.put("AnonyBody", body);
                MessageSvc.MsgBody.RichText.Elem anony_elem = new MessageSvc.MsgBody.RichText.Elem();
                MessageSvc.MsgBody.RichText.Elem.AnonymousGroupMsg anonymousGroupMsg = new MessageSvc.MsgBody.RichText.Elem.AnonymousGroupMsg();
                anonymousGroupMsg.flags = 2;
                anonymousGroupMsg.anon_nick = body.anony_rsp.anony_name;
                anonymousGroupMsg.head_portrait = body.anony_rsp.portrait_index;
                anonymousGroupMsg.expire_time = body.anony_rsp.expired_time;
                anonymousGroupMsg.bubble_id = body.anony_rsp.bubble_index;
                anonymousGroupMsg.rank_color = body.anony_rsp.color;
                anony_elem.anon_group_msg = anonymousGroupMsg;
                elems.add(0, anony_elem);
            }
        }

        boolean isPtt = false;

        for(int i = 0; i < size(); i++){
            int msg_type = 0;
            Map<String, ArrayList<String>> msg_package = message_list.get(i);
            ArrayList<String> indexs = msg_package.get("index");
            MessageSvc.MsgBody.RichText.Elem elem = new MessageSvc.MsgBody.RichText.Elem();
            switch(indexs.get(0)){
                case "msg" :{
                    MessageSvc.MsgBody.RichText.Elem.Text text = new MessageSvc.MsgBody.RichText.Elem.Text();
                    text.str = msg_package.get(indexs.get(0)).get(0);
                    elem.text = text;
                    msg_type = 1;
                    break;
                }
                case "img" :{
                    msg_type = 2;
                    boolean flash = getAddContent(indexs.get(0), "flash", i, false);
                    int animation = getAddContent(indexs.get(0), "animation", i, 0);
                    String msg_main = msg_package.get(indexs.get(0)).get(0);
                    if(msg_main.trim().matches("^([A-Z0-9]{10,32})(\\.|)([0-9_a-zA-z_]+|)")){
                        switch(message_info.msgFrom){
                            case Group:{
                                MessageSvc.MsgBody.RichText.Elem.CustomFace faceImage = new MessageSvc.MsgBody.RichText.Elem.CustomFace();
                                faceImage.str_file_path = msg_main;
                                faceImage.file_id = new Random().nextInt();
                                faceImage.server_ip = 3070487484L;
                                faceImage.server_port = 80;
                                faceImage.server_port = 80;
                                faceImage.file_type = 66;
                                faceImage.signature = bot.agreement_info.package_md5;
                                faceImage.useful = 1;
                                faceImage.md5 = HexUtil.HexString2Bytes( msg_main.split("\\.")[0] );
                                faceImage.biz_type = 0;
                                faceImage.image_type = 1003;
                                faceImage.width = 1980;
                                faceImage.source = 200;
                                faceImage.origin = 0;
                                faceImage.pb_reserve = new MessageSvc.MsgBody.RichText.Elem.CustomFace.ResvAttr();
                                faceImage.pb_reserve.source = 2;
                                if(flash){ // 闪图
                                    MessageSvc.MsgBody.RichText.Elem.CommonElem comm_elem = new MessageSvc.MsgBody.RichText.Elem.CommonElem();
                                    comm_elem.service_type = 3;
                                    hummer_commelem.MsgElemInfo_servtype3 t3 = new hummer_commelem.MsgElemInfo_servtype3();
                                    t3.flash_troop_pic = faceImage;
                                    faceImage.pb_reserve.source = 6;
                                    comm_elem.pb_elem = t3.toByteArray();
                                    elem.common_elem = comm_elem;
                                    addNotice(elems, "[闪图]请使用最新版本QQ接收。");
                                }else if(animation != 0){
                                    faceImage.pb_reserve.source = 6;
                                    faceImage.pb_reserve.msg_image_show = new MessageSvc.MsgBody.RichText.Elem.CustomFace.ResvAttr.AnimationImageShow();
                                    faceImage.pb_reserve.msg_image_show.int32_effect_id = animation;
                                    elem.custom_face = faceImage;
                                }else{
                                    elem.custom_face = faceImage;
                                }
                                break;
                            }
                            case Friend :{
                                MessageSvc.MsgBody.RichText.Elem.NotOnlineImage notOnlineImg = new MessageSvc.MsgBody.RichText.Elem.NotOnlineImage();
                                notOnlineImg.file_path = msg_main;
                                notOnlineImg.file_len = new Random().nextInt();
                                notOnlineImg.img_type = 1000;
                                notOnlineImg.pic_md5 = HexUtil.HexString2Bytes( msg_main.split("\\.")[0] );
                                notOnlineImg.pic_height = 1080;
                                notOnlineImg.pic_width = 1980;
                                notOnlineImg.download_path = "/" + bot.getUin() + "-273052113-" + msg_main.split("\\.")[0];
                                notOnlineImg.res_id = "/" + bot.getUin() + "-273052113-" + msg_main.split("\\.")[0];
                                if(flash){
                                    hummer_commelem.MsgElemInfo_servtype3 t3 = new hummer_commelem.MsgElemInfo_servtype3();
                                    t3.flash_c2c_pic = notOnlineImg;
                                    MessageSvc.MsgBody.RichText.Elem.CommonElem comm = new MessageSvc.MsgBody.RichText.Elem.CommonElem();
                                    comm.pb_elem = t3.toByteArray();
                                    comm.service_type = 3;
                                    elem.common_elem = comm;
                                }else{
                                    elem.not_online_image = notOnlineImg;
                                }
                                break;
                            }
                            default:
                                throw new IllegalStateException("Unexpected value: " + message_info.msgFrom);
                        }
                    }
                    else if(msg_main.startsWith("http") || msg_main.startsWith("file") || msg_main.startsWith("assets") || msg_main.startsWith("hex://")){
                        byte[] img_src = null;
                        try {
                            if ( msg_main.startsWith("hex://")) {
                                img_src = HexUtil.hexStringToByte(msg_main.substring(6));
                            } else
                                img_src = new OkHttpUtil().useDefault_User_Agent().getData(msg_main).body().bytes();
                        } catch (IOException e) {
                            try {
                                img_src = Tools.readFileBytes(msg_main);
                            } catch(IOException ee) {
                                if(Code.isDebug) {
                                    ee.printStackTrace();
                                }
                            }
                            if(Code.isDebug) {
                                e.printStackTrace();
                            }
                        }
                        if(img_src == null) {
                            break;
                        }
                        if(Util.PicUp(message_info.msgFrom, bot, img_src, msg.routing_head.to_uin)){
                            switch(message_info.msgFrom){
                                case Group :{
                                    MessageSvc.MsgBody.RichText.Elem.CustomFace faceImage = new MessageSvc.MsgBody.RichText.Elem.CustomFace();
                                    faceImage.str_file_path = MD5.toMD5(img_src) + ".jpg";
                                    faceImage.file_id = new Random().nextInt();
                                    faceImage.server_ip = 3070487484L;
                                    faceImage.server_port = 80;
                                    faceImage.server_port = 80;
                                    faceImage.file_type = 66;
                                    faceImage.signature = bot.agreement_info.package_md5;
                                    faceImage.useful = 1;
                                    faceImage.md5 = MD5.toMD5Byte(img_src);
                                    faceImage.biz_type = 0;
                                    faceImage.image_type = 1003;
                                    faceImage.size =  img_src.length;
                                    faceImage.height = 1080;
                                    faceImage.pb_reserve = new MessageSvc.MsgBody.RichText.Elem.CustomFace.ResvAttr();
                                    faceImage.pb_reserve.source = 2;
                                    if(flash){
                                        MessageSvc.MsgBody.RichText.Elem.CommonElem comm_elem = new MessageSvc.MsgBody.RichText.Elem.CommonElem();
                                        comm_elem.service_type = 3;
                                        hummer_commelem.MsgElemInfo_servtype3 t3 = new hummer_commelem.MsgElemInfo_servtype3();
                                        t3.flash_troop_pic = faceImage;
                                        faceImage.pb_reserve.source = 6;
                                        comm_elem.pb_elem = t3.toByteArray();
                                        elem.common_elem = comm_elem;
                                        addNotice(elems, "[闪图]请使用最新版本QQ接收。");
                                    }else if(animation != 0){
                                        faceImage.pb_reserve.source = 6;
                                        faceImage.pb_reserve.msg_image_show = new MessageSvc.MsgBody.RichText.Elem.CustomFace.ResvAttr.AnimationImageShow();
                                        faceImage.pb_reserve.msg_image_show.int32_effect_id = animation;
                                        elem.custom_face = faceImage;
                                    }else{
                                        elem.custom_face = faceImage;
                                    }
                                    break;
                                }
                                case Friend :{
                                    MessageSvc.MsgBody.RichText.Elem.NotOnlineImage notOnlineImg = new MessageSvc.MsgBody.RichText.Elem.NotOnlineImage();
                                    notOnlineImg.file_path = MD5.toMD5(img_src) + ".jpg";
                                    notOnlineImg.file_len = img_src.length;
                                    notOnlineImg.img_type = 1000;
                                    notOnlineImg.pic_md5 = MD5.toMD5Byte(img_src);
                                    notOnlineImg.pic_height = 1080;
                                    notOnlineImg.pic_width = 1980;
                                    notOnlineImg.download_path = "/" + bot.getUin() + "-273052113-" + MD5.toMD5(img_src);
                                    notOnlineImg.res_id = "/" + bot.getUin() + "-273052113-" + MD5.toMD5(img_src);
                                    if(flash){
                                        hummer_commelem.MsgElemInfo_servtype3 t3 = new hummer_commelem.MsgElemInfo_servtype3();
                                        t3.flash_c2c_pic = notOnlineImg;
                                        MessageSvc.MsgBody.RichText.Elem.CommonElem comm = new MessageSvc.MsgBody.RichText.Elem.CommonElem();
                                        comm.pb_elem = t3.toByteArray();
                                        comm.service_type = 3;
                                        elem.common_elem = comm;
                                    }else{
                                        elem.not_online_image = notOnlineImg;
                                    }
                                    break;
                                }
                                default:
                                    throw new IllegalStateException("Unexpected value: " + message_info.msgFrom);
                            } // switch msgFrom

                        }
                    }
                    break;
                }
                case "shake" :{
                    msg_type = 3;
                    MessageSvc.MsgBody.RichText.Elem.ShakeWindow shake = new MessageSvc.MsgBody.RichText.Elem.ShakeWindow();
                    shake.type = 1;
                    shake.uin = bot.getUin();
                    elem.shake_window = shake;
                    addNotice(elems, "[窗口抖动]请使用最新版QQ查看。");
                    break;
                }
                case "poke" :{
                    try {
                        String msg_main = msg_package.get(indexs.get(0)).get(0);
                        int id = getAddContent(indexs.get(0), "id", i, new Random().nextInt());
                        int size = getAddContent(indexs.get(0), "size", i, 0);
                        MessageSvc.MsgBody.RichText.Elem.CommonElem common = new MessageSvc.MsgBody.RichText.Elem.CommonElem();
                        common.service_type = 2;
                        common.business_type = Integer.parseInt(msg_main);
                        hummer_commelem.MsgElemInfo_servtype2 t2 = new hummer_commelem.MsgElemInfo_servtype2();
                        t2.poke_type = Integer.parseInt(msg_main);
                        t2.double_hit = 0;
                        t2.vaspoke_id = id;
                        String name = "戳一戳";
                        switch(t2.poke_type){
                            case 1 :
                                name = "戳一戳";
                                break;
                            case 2 :
                                name = "比心";
                                break;
                            case 3 :
                                name = "点赞";
                                break;
                            case 4 :
                                name = "心碎";
                                break;
                            case 5 :
                                name = "666";
                                break;
                            case 6 :
                                name = "放大招";
                                break;
                            case 126 :{
                                switch(id){
                                    case 2000 :{
                                        name = "敲门";
                                        break;
                                    }
                                    case 2001 :{
                                        name = "抓一下";
                                        break;
                                    }
                                    case 2002 :{
                                        name = "碎屏";
                                        break;
                                    }
                                    case 2003 :{
                                        name = "勾引";
                                        break;
                                    }
                                    case 2004 :{
                                        name = "手雷";
                                        break;
                                    }
                                    case 2005 :{
                                        name = "结印";
                                        break;
                                    }
                                    case 2006 :{
                                        name = "召唤术";
                                        break;
                                    }
                                    case 2007 :{
                                        name = "玫瑰花";
                                        break;
                                    }
                                    case 2008 :{
                                        name = "新年快乐";
                                        break;
                                    }
                                    case 2009 :{
                                        name = "让你皮";
                                        break;
                                    }
                                    case 2010 :{
                                        name = "嗨起来";
                                        break;
                                    }
                                    case 2011 :{
                                        name = "宝贝球";
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        t2.vaspoke_name = name.getBytes();
                        t2.vaspoke_minver = "7.2.0".getBytes();
                        t2.poke_strength = size;
                        common.pb_elem = ProtobufProxy.create(hummer_commelem.MsgElemInfo_servtype2.class).encode(t2);
                        elem.common_elem = common;
                        msg_type = 4;
                        addNotice(elems, "[戳一戳]请使用新版QQ看看查看。");
                    } catch (Exception e) {
                        if(Code.isDebug) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                case "animation" :{
                    try {
                        int type = Integer.parseInt( msg_package.get(indexs.get(0)).get(0) );
                        int size = getAddContent(indexs.get(0), "size", i, 0);
                        String name = getAddContent(indexs.get(0), "name", i, "动画消息");
                        MessageSvc.MsgBody.RichText.Elem.CommonElem common = new MessageSvc.MsgBody.RichText.Elem.CommonElem();
                        common.service_type = 23;
                        common.business_type = type;
                        hummer_commelem.MsgElemInfo_servtype23 t23 = new hummer_commelem.MsgElemInfo_servtype23();
                        t23.face_type = type;
                        t23.face_bubble_count = size;
                        t23.face_summary = name.getBytes();
                        common.pb_elem = ProtobufProxy.create(hummer_commelem.MsgElemInfo_servtype23.class).encode(t23);
                        elem.common_elem = common;
                        msg_type = 5;
                    }catch(Exception e){
                        if(Code.isDebug) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                case "at":{
                    try{
                        boolean space = getAddContent(indexs.get(0), "space", i, true);
                        long uin = Long.parseLong( msg_package.get(indexs.get(0)).get(0) );
                        MessageSvc.MsgBody.RichText.Elem.Text text = new MessageSvc.MsgBody.RichText.Elem.Text();
                        String nick;
                        if(nicks.containsKey(uin)){
                            nick = nicks.get(uin);
                        }else if(uin == 0){
                            nick = "全体成员";
                        }else{
                            nick = bot.getControl().getGroupMemberCardInfo(msg.routing_head.to_uin, uin).get("nick").getAsString();
                            nicks.put(uin, nick);
                        }
                        text.str = "@" + nick + (space ? " ": "");
                        ByteObject bo = new ByteObject();
                        bo.WriteShort(1); // start pos
                        bo.WriteInt(text.str != null ? text.str.getBytes().length : 0); // textLen
                        bo.WriteByte( (byte) (uin == 0 ? 1 : 0) ); // flag 0 艾特某人 1 艾特全体
                        bo.WriteInt(uin);
                        bo.WriteShort(0);
                        text.attr_6_buf = bo.toByteArray();
                        elem.text = text;
                        msg_type = 6;
                    }catch(Exception e){
                        if(Code.isDebug) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                case "face" :{
                    try{
                        MessageSvc.MsgBody.RichText.Elem.Face face = new MessageSvc.MsgBody.RichText.Elem.Face();
                        face.index = Integer.parseInt( msg_package.get(indexs.get(0)).get(0) );
                        elem.face = face;
                        msg_type = 7;
                    }catch(Exception e){
                        if(Code.isDebug) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                case "flashMsg": {
                    int id = getAddContent(indexs.get(0), "id", i, 2000);
                    MessageSvc.MsgBody.RichText.Elem.CommonElem common = new MessageSvc.MsgBody.RichText.Elem.CommonElem();
                    common.service_type = 14;
                    common.business_type = 0;
                    hummer_commelem.MsgElemInfo_servtype14 t14 = new hummer_commelem.MsgElemInfo_servtype14();
                    String text = msg_package.get(indexs.get(0)).get(0);
                    t14.id = id;
                    t14.setInfo(text);
                    try {
                        common.pb_elem = ProtobufProxy.create(hummer_commelem.MsgElemInfo_servtype14.class).encode(t14);
                        elem.common_elem = common;
                        msg_type = 8;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    addNotice(elems, text);
                    break;
                }
                case "json" :{
                    MessageSvc.MsgBody.RichText.Elem.LightAppElem light = new MessageSvc.MsgBody.RichText.Elem.LightAppElem();
                    light.setJsonSrc(msg_package.get(indexs.get(0)).get(0));
                    elem.light_app = light;
                    msg_type = 9;
                    break;
                }
                case "xml" :{
                    String xml = msg_package.get(indexs.get(0)).get(0);
                    MessageSvc.MsgBody.RichText.Elem.RichMsg rich = new MessageSvc.MsgBody.RichText.Elem.RichMsg();
                    rich.service_id = getAddContent(indexs.get(0), "serviceID", i, 0);
                    rich.setXmlSrc(xml.getBytes());
                    rich.rand = new Random().nextInt();
                    elem.rich_msg = rich;
                    msg_type = 10;
                    break;
                }
                case "dice" :{
                    try{
                        int point = Integer.parseInt( msg_package.get(indexs.get(0)).get(0) );
                        MessageSvc.MsgBody.RichText.Elem.MarketFace mface = new MessageSvc.MsgBody.RichText.Elem.MarketFace();
                        mface.face_name = "[骰子]";
                        mface.item_type = point;
                        mface.face_info = 1;
                        mface.face_id = HexUtil.HexString2Bytes("4823D3ADB15DF08014CE5D6796B76EE1");
                        mface.tab_id = 11464;
                        mface.sub_type = 3;
                        mface.key = "409e2a69b16918f9".getBytes();
                        mface.media_type = 0;
                        mface.image_height = 200;
                        mface.image_width = 200;
                        mface.mobileparam = ("rscType?1;value=" + (point - 1)).getBytes();
                        mface.pb_reserve.emoji_type = 1;
                        mface.pb_reserve.voice_item_height_arr = 0;
                        elem.market_face = mface;
                        hasAction = true;
                        general_flags.pb_reserve.doutu_msg_type = 1;
                        msg_type = 11;
                    }catch(Exception e){
                        if(Code.isDebug) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                case "bface" :{
                    try {
                        MessageSvc.MsgBody.RichText.Elem.CommonElem comm = new MessageSvc.MsgBody.RichText.Elem.CommonElem();
                        int index = Integer.parseInt(msg_package.get(indexs.get(0)).get(0));
                        hummer_commelem.MsgElemInfo_servtype33 t33 = new hummer_commelem.MsgElemInfo_servtype33();
                        t33.index = index;
                        t33.text = getAddContent(indexs.get(0), "name", i, "新表情").getBytes();
                        t33.compat = t33.text;
                        comm.pb_elem = t33.toByteArray();
                        comm.business_type = 1;
                        comm.service_type = 33;
                        elem.common_elem = comm;
                        msg_type = 12;
                    } catch (Throwable th){
                        if(Code.isDebug) {
                            th.printStackTrace();
                        }
                    }
                    break;
                }
                case "ptt":{
                    // msg_type = 13;
                    isPtt = true;
                    hasAction = true;
                    String msg_main = msg_package.get(indexs.get(0)).get(0);
                    boolean magic = getAddContent(indexs.get(0), "magic", i, false);
                    int time = getAddContent(indexs.get(0), "time", i, -1);
                    if(msg_main.trim().matches("^([A-Z0-9]{10,32})(\\.)([0-9_a-zA-z_]+|)")) {
                        if(message_info.msgFrom == MsgFrom.Group) {
                            msg.msg_body.rich_text.ptt = new MessageSvc.MsgBody.RichText.Ptt();
                            msg.msg_body.rich_text.ptt.file_md5 = HexUtil.hexStringToByte(msg_main.split("\\.")[0]);
                            msg.msg_body.rich_text.ptt.file_type = 4;
                            msg.msg_body.rich_text.ptt.file_name = msg_main;
                            msg.msg_body.rich_text.ptt.file_size = new Random().nextInt();
                            msg.msg_body.rich_text.ptt.pb_reserve.change_voice = magic ? 1 : 0;
                            msg.msg_body.rich_text.ptt.time = time != -1 ? time : 3;
                        }
                    }else if(msg_main.startsWith("http") || msg_main.startsWith("file") || msg_main.startsWith("assets")) {
                        byte[] pttSrc;
                        try {
                            if( msg_main.startsWith("http")){
                                pttSrc = new OkHttpUtil().useDefault_User_Agent().getData(msg_main).body().bytes();
                            } else {
                                pttSrc = Tools.readFileBytes(msg_main.substring(8).replace("%20", " "));
                            }
                        } catch (IOException e) {
                            if(Code.isDebug) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        if(Util.GroupPttUp(bot, pttSrc, msg.routing_head.to_uin)) {
                            msg.msg_body.rich_text.ptt = new MessageSvc.MsgBody.RichText.Ptt();
                            msg.msg_body.rich_text.ptt.file_md5 = MD5.toMD5Byte(pttSrc);
                            msg.msg_body.rich_text.ptt.file_type = 4;
                            msg.msg_body.rich_text.ptt.file_name = MD5.toMD5(pttSrc) + ".amr";
                            msg.msg_body.rich_text.ptt.file_size = pttSrc.length;
                            msg.msg_body.rich_text.ptt.pb_reserve.change_voice = 0;
                        }
                    }
                    break;
                }
                default:
                    if(Code.isDebug) {
                        throw new IllegalStateException("Unexpected value: " + indexs.get(0));
                    }
            }
            if(msg_type != 0) {
                elems.add(elem);
            }
        }

        if((elems.size() <= 0 || message_list.size() <= 0 ) && !isPtt){
            return MessagerResult.EmptyMessage;
        }

        msg.content_head = Util.makeContentHead(hasAction);

        // ============================================================================
        MessageSvc.MsgBody.RichText.Elem general_elem = new MessageSvc.MsgBody.RichText.Elem();
        general_flags.pendant_id = 0;
        general_flags.glamour_level = 0;
        general_elem.general_flags = general_flags;
        if( (getText("msg").getBytes().length + getText("xml").getBytes().length + getText("json").getBytes().length) > 450 || getMsg("img").size() > 3 || getMsg("face").size() >= 100 || getMsg("bface").size() >= 100){
            if( getText("msg").getBytes().length + getText("xml").getBytes().length + getText("json").getBytes().length > (1024 * 1024 * 3)){
                return MessagerResult.MessageLengthMax;
            }
            MessageSvc.MsgBody body = new MessageSvc.MsgBody();
            body.rich_text = new MessageSvc.MsgBody.RichText();
            body.rich_text.elems = elems;
            String resid = upLongMsg.getMsgResId(bot, msg.routing_head.to_uin, body);
            // System.out.println("ResID:" + resid);
            MessageSvc.MsgBody.RichText.Elem elem = new MessageSvc.MsgBody.RichText.Elem();
            MessageSvc.MsgBody.RichText.Elem.RichMsg rich = new MessageSvc.MsgBody.RichText.Elem.RichMsg();
            rich.service_id = 35;
            rich.setXmlSrc(("<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"35\" templateID=\"1\" action=\"viewMultiMsg\" brief=\"[长消息]来自QQ的分享\" m_resid=\""+resid+"\" m_fileName=\"6844889514862377669\" sourceMsgId=\"0\" url=\"\" flag=\"3\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"1\"><title size=\"26\" color=\"#777777\" maxLines=\"2\" lineSpace=\"12\">点击查看长消息...</title><hr hidden=\"false\" style=\"0\" /><summary>点击查看完整消息</summary></item><source name=\"聊天记录\" icon=\"\" action=\"\" appid=\"-1\" /></msg>").getBytes());
            elem.rich_msg = rich;
            MessageSvc.MsgBody.RichText.Elem text_elem = new MessageSvc.MsgBody.RichText.Elem();
            MessageSvc.MsgBody.RichText.Elem.Text text = new MessageSvc.MsgBody.RichText.Elem.Text();
            text.str = "[长消息]请使用新版QQ查看！";
            text_elem.text = text;
            msg.msg_body.rich_text.elems.add(text_elem);
            msg.msg_body.rich_text.elems.add(elem);

            general_flags.long_text_flag = 1;
            general_flags.long_text_resid = resid;
            msg.msg_body.rich_text.elems.add(general_elem);

        }else{
            elems.add(general_elem);
            msg.msg_body.rich_text.elems = elems;
        }
        // ============================================================================

        ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("MessageSvc.PbSendMsg"){
            @Override
            public boolean helpJudgement(){
                byte[] source = new ByteObject(super.source).disUseData(4).readRestBytes();
                MessageSvc.PbSendMsgResp resp = MessageSvc.PbSendMsgResp.decode(source);
                // 到时候把这里打印的内容输出到控制台即可
                if(resp.result == 120){
                    System.out.println("处于被禁言状态，无法发炎");
                }else if(resp.result == 110){
                    System.out.println("被拒绝发送消息");
                }else if(resp.result == 0){
                    System.out.println("发送成功");
                }else if(resp.result == 34) {
                    System.out.println("消息太长啦！");
                }else if(resp.result == 1){
                    System.out.println("消息包错误");
                }else if(resp.result == 130){
                    System.out.println("匿名消息被禁止");
                }else if(resp.result == 131){
                    System.out.println("匿名用户被禁言");
                }else if(resp.result == 299){
                    System.out.println("受到每分钟发言次数限制");
                }else{
                    System.out.println("未知错误：" + resp.result);
                }
                return true;
            }
        });
        getClient().send( QQAgreementUtils.encodeRequest(bot, bot.recorder.nextSsoSeq(), "MessageSvc.PbSendMsg", msg.toByteArray()) );
        handle.waitPacket();
        return MessagerResult.Success;
    }

    private void addNotice(List<MessageSvc.MsgBody.RichText.Elem> elems, String str){
        MessageSvc.MsgBody.RichText.Elem elem = new MessageSvc.MsgBody.RichText.Elem();
        MessageSvc.MsgBody.RichText.Elem.Text text = new MessageSvc.MsgBody.RichText.Elem.Text();
        text.str = str;
        elem.text = text;
        elems.add(elem);
    }

    private TCPSocket getClient(){
        if(bot.data.containsKey("client")){
            return (TCPSocket) bot.data.get("client");
        }
        return null;
    }

    @Override
    public String toString() {
        return ArtUtil.messagerToArtCode(this);
    }

    public Messenger parseAQCode(String aq){
        return ArtUtil.parseArtCode(bot, aq);
    }

    private static class Util {

        public static boolean PicUp(MsgFrom msgFrom, ArtBot bot, byte[] img_src, long to_uin) {
            switch(msgFrom){
                case Group :
                    return GroupPicUp(bot, img_src, to_uin);
                case Friend :
                    return C2CPicUp(bot, img_src, to_uin);
            }
            return false;
        }

        public static MessageSvc.ContentHead makeContentHead(boolean action){
            MessageSvc.ContentHead content_head = new MessageSvc.ContentHead();
            content_head.div_seq = (int) -(System.currentTimeMillis() - System.currentTimeMillis());
            content_head.pkg_index = 0;
            content_head.pkg_num = action ? 0 : 1;
            return content_head;
        }

        public static MessageSvc.RoutingHead makeRoutingHead(MessageInfo msg_info){
            MessageSvc.RoutingHead routing_head = new MessageSvc.RoutingHead();
            switch(msg_info.msgFrom){
                case ArtQQ : return null;
                case Group :{
                    if(msg_info.groupInfo.groupCode == null || msg_info.groupInfo.groupCode < 10000 || msg_info.groupInfo.groupCode > 4000000000L){
                        return null;
                    }
                    MessageSvc.RoutingHead.Grp grp = new MessageSvc.RoutingHead.Grp();
                    grp.group_code = msg_info.groupInfo.groupCode;
                    routing_head.grp = grp;
                    routing_head.to_uin = msg_info.groupInfo.groupCode;
                    break;
                }
                case Temporary:{
                    if(msg_info.uinInfo.uinCode == null || msg_info.uinInfo.uinCode < 10000 || msg_info.uinInfo.uinCode > 4000000000L){
                        return null;
                    }
                    if(msg_info.groupInfo.groupCode == null || msg_info.groupInfo.groupCode < 10000 || msg_info.groupInfo.groupCode > 4000000000L){

                    }else {
                        MessageSvc.RoutingHead.Grp_tmp grp_tmp = new MessageSvc.RoutingHead.Grp_tmp();
                        grp_tmp.to_uin = msg_info.uinInfo.uinCode;
                        grp_tmp.group_uin = msg_info.groupInfo.groupCode;
                        routing_head.grp_tmp = grp_tmp;
                        routing_head.to_uin = msg_info.uinInfo.uinCode;
                    }
                    break;
                }
                case Friend :{
                    if(msg_info.uinInfo.uinCode == null || msg_info.uinInfo.uinCode < 10000 || msg_info.uinInfo.uinCode > 4000000000L){
                        return null;
                    }
                    MessageSvc.RoutingHead.C2C c2c = new MessageSvc.RoutingHead.C2C();
                    c2c.to_uin = msg_info.uinInfo.uinCode;
                    routing_head.c2C = c2c;
                    routing_head.to_uin = msg_info.uinInfo.uinCode;
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + msg_info.msgFrom);
            }
            return routing_head;
        }

        public static boolean GroupPttUp(ArtBot bot, byte[] src, long to_uin){
            if(src == null || src.length <= 0) {
                return false;
            }
            oidb_0x388.ReqBody req = new oidb_0x388.ReqBody();
            req.msg_tryup_ptt_req = new oidb_0x388.TryUpPttReq();
            req.subcmd = 3;
            req.msg_tryup_ptt_req.group_code = to_uin;
            req.msg_tryup_ptt_req.src_uin = bot.getUin();
            req.msg_tryup_ptt_req.voice_length = 1;
            req.msg_tryup_ptt_req.bu_type = 4;
            req.msg_tryup_ptt_req.voice_type = 1;
            req.msg_tryup_ptt_req.file_md5 = MD5.toMD5Byte(src);
            req.msg_tryup_ptt_req.file_size = src.length;
            req.msg_tryup_ptt_req.file_name = (MD5.toMD5(src) + ".amr").getBytes();
            req.msg_tryup_ptt_req.build_ver = bot.agreement_info.builder_ver.getBytes();
            final int seq = bot.recorder.nextSsoSeq();
            ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("PttStore.GroupPttUp"){
                @Override
                public boolean helpJudgement(){
                    oidb_0x388.RspBody rsp = oidb_0x388.RspBody.decode(new ByteObject(source).disUseData(4).readRestBytes());
                    if(seq == ssoSeq){
                        obj = rsp;
                        return true;
                    }
                    return false;
                }
            });
            getClient(bot).send(QQAgreementUtils.encodeRequest(bot, seq, "PttStore.GroupPttUp", req.toByteArray()));
            handle.waitPacket();
            oidb_0x388.RspBody rsp = (oidb_0x388.RspBody) handle.obj;
            if(rsp.msg_tryup_ptt_rsp.file_exit){
                return true;
            }else{
                try {
                    String url = "http://" +
                        BytesUtil.LongToIp(rsp.msg_tryup_ptt_rsp.up_ip, bot.sigInfo.highwayServer) +
                        ":" +
                        rsp.msg_tryup_ptt_rsp.up_port + "?ver=4679&ukey=" + HexUtil.bytesToHexString(rsp.msg_tryup_ptt_rsp.up_ukey) +
                        "&filekey=" + HexUtil.bytesToHexString(rsp.msg_tryup_ptt_rsp.file_key) +
                        "&filesize=" + src.length +
                        "&range=0&bmd5=" + MD5.toMD5(src) + "&" +
                        "mType=pttGu&voice_codec=1";
                    OkHttpClient client = new OkHttpClient();
                    MediaType type = MediaType.parse("text/plain");
                    RequestBody body = RequestBody.create(type, src);
                    Request request = new Request.Builder()
                        .url(url)
                        .method("POST", body)
                        .build();
                    client.newCall(request).execute().code();
                    return true;
                } catch(IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }

        public static boolean GroupPicUp(ArtBot bot, byte[] src, long to_uin){
            if(src == null || src.length <= 0) {
                return false;
            }
            oidb_0x388.ReqBody req = new oidb_0x388.ReqBody();
            req.msg_tryup_img_req = new oidb_0x388.TryUpImgReq();
            req.msg_tryup_img_req.group_code = to_uin;
            req.msg_tryup_img_req.src_uin = bot.getUin();
            req.msg_tryup_img_req.file_md5 = MD5.toMD5Byte(src);
            req.msg_tryup_img_req.file_size = src.length;
            req.msg_tryup_img_req.file_name = (HexUtil.bytesToHexString(MD5.toMD5Byte(src)) + ".jpg").getBytes();
            req.msg_tryup_img_req.build_ver = bot.agreement_info.builder_ver.getBytes();
            final int seq = bot.recorder.nextSsoSeq();
            ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("ImgStore.GroupPicUp"){
                @Override
                public boolean helpJudgement(){
                    oidb_0x388.RspBody rsp = oidb_0x388.RspBody.decode(new ByteObject(source).disUseData(4).readRestBytes());
                    if(ssoSeq == seq){
                        obj = rsp;
                        return true;
                    }
                    return false;
                }
            });
            getClient(bot).send(QQAgreementUtils.encodeRequest(bot, seq, "ImgStore.GroupPicUp", req.toByteArray()));
            handle.waitPacket();
            oidb_0x388.RspBody rsp = (oidb_0x388.RspBody) handle.obj;
            if(!rsp.rpt_msg_tryup_img_rsp.bool_file_exit) {
                bot.sigInfo.highwayServer = BytesUtil.LongToIp(rsp.rpt_msg_tryup_img_rsp.rpt_up_ip, "112.60.8.142");
                bot.sigInfo.highwayPort = rsp.rpt_msg_tryup_img_rsp.rpt_up_port;
                HwPacket packet = new HwPacket();
                DataHighwayHead highwayHead = new DataHighwayHead();
                highwayHead.command = "PicUp.DataUp";
                highwayHead.commandId = 8;
                highwayHead.dataflag = 4096;
                highwayHead.uin = String.valueOf(bot.getUin());
                highwayHead.seq = bot.recorder.nextHwSeq();
                SigHead sigHead = new SigHead();
                sigHead.md5 = MD5.toMD5Byte(src);
                sigHead.serviceticket = rsp.rpt_msg_tryup_img_rsp.up_ukey;
                sigHead.rtcode = 0;
                sigHead.flag = 0;
                sigHead.filesize = src.length;
                sigHead.datalength = src.length;
                packet.dataHead = highwayHead;
                packet.sigHead = sigHead;
                HwData hwData = new HwData();
                hwData.head = packet.toByteArray();
                hwData.data = src;
                try {
                    TCPSocket client = new TCPSocket(bot.sigInfo.highwayServer, bot.sigInfo.highwayPort);
                    client.send(QQAgreementUtils.make_hw_packet(hwData));
                    client.readHwData(10000);
                } catch(Exception e) {
                    if(Code.isDebug) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }

        public static boolean C2CPicUp(ArtBot bot, byte[] src, long to_uin){
            if(src == null || src.length <= 0) {
                return false;
            }
            oidb_0x352.Req req = new oidb_0x352.Req();
            req.msg_tryup_img_req = new oidb_0x352.TryUpImgReq();
            req.msg_tryup_img_req.dst_uin = to_uin;
            req.msg_tryup_img_req.src_uin = bot.getUin();
            req.msg_tryup_img_req.file_md5 = MD5.toMD5Byte(src);
            req.msg_tryup_img_req.file_size = src.length;
            req.msg_tryup_img_req.file_name = (HexUtil.bytesToHexString(MD5.toMD5Byte(src)) + ".jpg").getBytes();
            req.msg_tryup_img_req.build_ver = bot.agreement_info.builder_ver.getBytes();
            final int seq = bot.recorder.nextSsoSeq();
            ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("LongConn.OffPicUp"){
                @Override
                public boolean helpJudgement(){
                    oidb_0x352.Resp rsp = oidb_0x352.Resp.decode(new ByteObject(source).disUseData(4).readRestBytes());
                    if(ssoSeq == seq){
                        obj = rsp;
                        return true;
                    }
                    return false;
                }
            });
            getClient(bot).send(QQAgreementUtils.encodeRequest(bot, seq, "LongConn.OffPicUp", req.toByteArray()));
            handle.waitPacket();
            oidb_0x352.Resp rsp = (oidb_0x352.Resp) handle.obj;
            if(rsp.msg_tryup_img_rsp.file_exit){
                System.out.println("图片存在于腾讯服务器，正在直接发送！！！");
            }else{
                HwPacket packet = new HwPacket();
                DataHighwayHead highwayHead = new DataHighwayHead();
                highwayHead.command = "PicUp.DataUp";
                highwayHead.commandId = 1;
                highwayHead.dataflag = 4096;
                highwayHead.uin = String.valueOf(bot.getUin());
                highwayHead.seq = bot.recorder.nextHwSeq();
                SigHead sigHead = new SigHead();
                sigHead.md5 = MD5.toMD5Byte(src);
                sigHead.serviceticket = rsp.msg_tryup_img_rsp.up_ukey;
                sigHead.rtcode = 0;
                sigHead.flag = 0;
                sigHead.dataoffset = 0;
                sigHead.filesize = src.length;
                sigHead.datalength = src.length;
                packet.dataHead = highwayHead;
                packet.sigHead = sigHead;
                HwData hwData = new HwData();
                hwData.head = packet.toByteArray();
                hwData.data = src;
                try {
                    TCPSocket client = new TCPSocket(BytesUtil.LongToIp(rsp.msg_tryup_img_rsp.up_ip, "112.60.8.142"), rsp.msg_tryup_img_rsp.up_port);
                    client.send(QQAgreementUtils.make_hw_packet(hwData));
                    client.readHwData(10000);
                } catch (Exception e) {
                    if(Code.isDebug) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }

        private static TCPSocket getClient(ArtBot bot){
            return (TCPSocket) bot.data.get("client");
        }

    }
}
