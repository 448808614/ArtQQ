
import com.google.gson.JsonObject;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.artbot.HandleMessage;
import org.artbot.Messenger;
import org.artqq.ArtBot;
import org.artbot.MsgFrom;
import org.artbot.util.ArtUtil;
import org.artqq.utils.HexUtil;
import org.artqq.utils.OkHttpUtil;
import org.artqq.utils.RandomUtil;
import org.artqq.utils.Tools;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;

public class HandleMsg extends HandleMessage {
    static JsonObject parse = new JsonObject();

    /**
     * 发送AQ码
     * String s = messager.getText("msg");
     * messager.clearMsg();
     * messager.addMsg("msg", s);
     * messager.send();
     */
    @Override
    public int handleGroupMessage(Messenger messenger, long uin, long groupid, int msgid, String msg) {
        ArtBot bot = messenger.getBot();
        if(bot.getUin() == uin){
            return GoneMessage;
        }

        String AQCode = messenger.toString();
        // 获取当前消息的AQ码 AQCode === msg

        if(msg.matches("发送(.*)")){
            messenger.clearMsg();
            String old = msg.replaceAll("发送(.*)", "$1");
            String code = ArtUtil.decode(old);
            // System.out.println(old + "\n========\n" + code);
            Messenger m = messenger.parseAQCode(code);
            m.message_info.msgFrom = MsgFrom.Group;
            m.message_info.groupInfo.groupCode = messenger.message_info.groupInfo.groupCode;
            m.send();
            return HandleMessage.HijackMessage;
        }else if(msg.equals("赞")){
            System.out.println(bot.getControl().giveFavorite(messenger.message_info.uinInfo.uinCode, 10));

            messenger.clearMsg();
            messenger.addMsg("msg", "赞你大爷");
            // messager.message_info.groupInfo.GroupCode = ;
            // 发起群临时消息 必须带上群号
            messenger.message_info.msgFrom = MsgFrom.Temporary;
            messenger.send();

        }else if(msg.equals("我的信息")) {
            messenger.clearMsg();
            messenger.addMsg("msg", bot.getControl().getUserInfoBySearchFriend(messenger.message_info.uinInfo.uinCode).toString());
            messenger.send();
            messenger.clearMsg();
            messenger.addMsg("msg", bot.getControl().getGroupMemberCardInfo(messenger.message_info.groupInfo.groupCode, messenger.message_info.uinInfo.uinCode).toString());
            messenger.send();
        }else if(msg.equals("群成员列表")) {
            messenger.clearMsg();
            messenger.addMsg("msg", bot.getControl().getGroupMember(messenger.message_info.groupInfo.groupCode, true).toString());
            messenger.send();
        }else if(msg.equals("打开一起听歌")){
            bot.getApiBot().setQunMediaState(groupid, true);
        }else if(msg.equals("关闭一起听歌")){
            bot.getApiBot().setQunMediaState(groupid, false);
        }else if(msg.equals("签到")) {
            bot.getApiBot().GroupQianDao(groupid, "自动签到");
        }else if(msg.equals("上管理")) {
            bot.getControl().setGroupAdmin(groupid, uin, false);
        }else if(msg.equals("下管理")) {
            bot.getControl().setGroupAdmin(groupid, uin, true);
        }else if(msg.matches("acg|cu|CU|Cu|ACG|ACg|Acg|acG|cU|不够(色|涩)")) {
            OkHttpUtil okHttpUtil = new OkHttpUtil();
            okHttpUtil.useDefault_User_Agent();
            Response response = okHttpUtil.getData("https://api.ixiaowai.cn/api/api.php?return=json");
            String url = null;
            boolean falsh = false;
            int rand = RandomUtil.randomInt(0, 100);
            if ( response.code() == 200 && rand < 34){
                try {
                    url = Tools.parseJsonObject(response.body().string()).get("imgurl").getAsString();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            } else {
                Data father = new Yaml().loadAs(getClass().getResourceAsStream("data.yml"), Data.class);
                try {
                    Info normal = father.normal.get(RandomUtil.randomInt(0, father.normal.size()));
                    String u = normal.url;
                    String p = String.valueOf(normal.pid);
                    if ( rand > 79 ) {
                        Info R18 = father.R18.get(RandomUtil.randomInt(0, father.R18.size()));
                        u = R18.url;
                        p = String.valueOf(R18.pid);
                        falsh = true;
                    }
                    Connection.Response response_ = Jsoup
                        .connect(u)
                        .followRedirects(true)
                        .timeout(180000)
                        .ignoreContentType(true)
                        .userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_7; ja-jp) AppleWebKit/533.20.25 (KHTML, like Gecko) Version/5.0.4 Safari/533.20.27")
                        .referrer("https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" + p)
                        .ignoreHttpErrors(true)
                        .maxBodySize(100000000)
                        .execute();
                    url = "hex://" + HexUtil.bytesToHexString(response_.bodyAsBytes());
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            messenger.clearMsg();
            messenger.addMsg("at", String.valueOf(messenger.message_info.uinInfo.uinCode));
            messenger.addMsg("img", url, "flash", String.valueOf(falsh));
            messenger.send();
        }else if(messenger.message_info.groupInfo.groupCode == 1109012399){
            //--> AQ码转文本发送
            //--》 方法1
            // String enc_AQCode = AQUtils.encode(AQCode);
            // 转码
            // Messager m = messager.parseAQCode(enc_AQCode);
            // m.send();

            //--》 方法2
            messenger.clearMsg(); // 清空消息构建器

            messenger.message_info.replyId = messenger.message_info.msgId;
            messenger.message_info.replyMsg = "自定义回复标题";
            messenger.message_info.replyUin = messenger.message_info.uinInfo.uinCode;

            if(msg.contains("精华")){
                bot.getControl().setUpEssentialMessage(groupid, msgid, false);

                bot.getApiBot().setGroupCard(groupid, uin, "傻叉");
            }


            messenger.addMsg("msg", AQCode);
            messenger.send();
        }

        return GoneMessage;
    }

    @Override
    public void handleGroupFunctionSwitch(long groupid, long uin, int funType, int funStatus) {
        Messenger msg  = bot.getMessager();
        msg.message_info.groupInfo.groupCode = groupid;
        msg.message_info.msgFrom = MsgFrom.Group;
        String name = "";
        switch(funType){
            case 0xe :{
                name = "匿名功能被" + uin + "设置为" + funStatus;
                break;
            }
            case 0x10:{
                name = "坦白说被" + uin + "设置为" + funStatus;
                break;
            }
            case 24 :{
                name = "上传相册被" + uin + "设置为" + funStatus;
                break;
            }
            case 25 :{
                name = "上传文件被" + uin + "设置为" + funStatus;
                break;
            }
            case 44:{
                name = "管理员上下位:" + uin + "被设置为" + funStatus;
                break;
            }
            case 55:{
                name = "群内临时会话被" + uin + "设置为" + funStatus;
                break;
            }
            case 59:{
                name = "发起新的群聊被" + uin + "设置为" + funStatus;
                break;
            }
            case 76:{
                name = "其中的uin为群主，不是设置一起写开关的人\n" + "一起写被" + uin + "设置为" + funStatus;
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + funType);
        }
        msg.addMsg("msg", name);
        // msg.send();
    }

    @Override
    public void handleGroupBan(long groupid, long admin, long uin, int time) {
        Messenger msg  = bot.getMessager();
        msg.message_info.groupInfo.groupCode = groupid;
        msg.message_info.msgFrom = MsgFrom.Group;
        msg.addMsg("msg", uin + "被" + admin + "禁言了" + time + "秒");
        // msg.send();
    }

    @Override
    public void handleGroupWithDraw(long groupid, long admin, int msgSeq, long msgTime, long msgSender) {
        Messenger msg  = bot.getMessager();
        msg.message_info.groupInfo.groupCode = groupid;
        msg.message_info.msgFrom = MsgFrom.Group;
        msg.addMsg("msg", "在" + groupid + "内，" + msgSender + "的一个在" + msgTime + "发的ID为" + msgSeq + "的消息,被" + admin + "撤回了" );
        // msg.send();
    }

    @Override
    public int handleFriendMessage(Messenger messenger, long uin, int msgid, String msg) {
        messenger.clearMsg();
        messenger.addMsg("msg", "...");
        messenger.send();
        // System.out.println(msg);

        return 0;
    }

    @Override
    public int handleTemporaryMessage(Messenger messenger, long uin, int msgid, String msg) {
        return GoneMessage;
    }

    @Override
    public void handleGroupNameChange(long groupid, long uin, String name) {
        Messenger msg  = bot.getMessager();
        msg.message_info.groupInfo.groupCode = groupid;
        msg.message_info.msgFrom = MsgFrom.Group;
        msg.addMsg("msg", groupid + "的名字被" + uin + "改成了" + name);
        // msg.send();
    }

    @Override
    public void handleGroupMemberChange(int type, long groupid, long admin, long uin, String nick, long time) {
        if(type == 2){
            Messenger msg  = bot.getMessager();
            msg.message_info.groupInfo.groupCode = groupid;
            msg.message_info.msgFrom = MsgFrom.Group;
            msg.addMsg("msg", "在" + groupid + "内，" + uin + "[" + nick + "]" + "于" + time + "加入了我们");
            // msg.send();
        }else if(type == 1){
            Messenger msg  = bot.getMessager();
            msg.message_info.groupInfo.groupCode = groupid;
            msg.message_info.msgFrom = MsgFrom.Group;
            msg.addMsg("msg", "在" + groupid + "内，" + uin + "[" + nick + "]" + "于" + time + "退出了");
            // msg.send();
        }else if(type == 3){
            Messenger msg  = bot.getMessager();
            msg.message_info.groupInfo.groupCode = groupid;
            msg.message_info.msgFrom = MsgFrom.Group;
            msg.addMsg("msg", "在" + groupid + "内，" + uin + "[" + nick + "]" + "于" + time + "被" + admin + "邀请进群");
            // msg.send();
        }else if(type == 4){
            Messenger msg  = bot.getMessager();
            msg.message_info.groupInfo.groupCode = groupid;
            msg.message_info.msgFrom = MsgFrom.Group;
            msg.addMsg("msg", "在" + groupid + "内，" + uin + "[" + nick + "]" + "于" + time + "被" + admin + "踢出");
            // msg.send();
        }
    }

}
