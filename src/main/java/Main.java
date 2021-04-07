import org.artbot.Messenger;
import org.artbot.MsgFrom;
import org.artqq.ArtBot;
import org.artqq.android.AndroidInfo;
import org.artqq.exceptions.LoginEnum;

import java.math.BigDecimal;

public class Main {
        public static void main(String[] args) throws Exception {
                AndroidInfo.initByFile("D:\\IDEA\\Projects\\ArtQQ-JVM\\info.json");
                // 准备IMEI等信息·

                // ArtBot bot = new ArtBot(203411690, "911586ABc");

                ArtBot bot = new ArtBot(1877130542L, "911586abc");

                bot.setOnlineStatus(11);
                // 设置预置在线状态
                // bot.useAndroidTim(); use AndroidTim

                LoginEnum result = bot.login();
                if(result != LoginEnum.LoginSuccess){
                        System.exit(1);
                        return;
                }

                // [===========[ 哥哥姐姐们，看不懂注释，请拿出百度翻译去翻译英文，还不会就找爷
                //=============================================================================================================
                // 设置在线状态
                bot.getControl().setOnlineStatus(11, 1040);
                // bot.getControl().setAutoReply("主人，有事吗？！");
                // 设置自动回复
                //=============================================================================================================
                // JsonObject friend_and_group = bot.getControl().getFriendList(false);
                // System.out.println(friend_and_group);
                // 获取好友和分组列表
                //=============================================================================================================
                // System.out.println( bot.getControl().giveFavorite(1372362033L, 1438) );
                // 点赞呗
                //=============================================================================================================
                // bot.getUin();
                // 获取当前登陆的QQ
                //==============================================================================================================
                // 获取群成员列表
                // System.out.println( bot.getControl().getGroupMember(1109012399, false) );
                //==============================================================================================================
                // bot.getControl().groupBan(1109012399, 0L, 0);
                // 群禁言 禁言时间是0可以解禁
                // uin = 0 banTime = 1 为全员禁言
                // uin = 0 banTime = 0 为全员解禁
                //==============================================================================================================
                // bot.getControl().withdrawGroupMsg(1109012399L, 4114);
                // 群消息撤回
                //==============================================================================================================
                // bot.getControl().modifyGroupCard(1109012399, 1372362033, "世界霸主");
                // 修改群名片
                //==============================================================================================================
                // System.out.println( bot.getControl().getGroupList(true) );
                // 获取群列表
                //=============================================================================================================
                // System.out.println( bot.getControl().getMultiTroopInfo(1109012399L) );
                // 获取群资料 只可以获取自己在的群哦！
                //=============================================================================================================
                // System.out.println( bot.getControl().getGroupMemberCardInfo(1109012399, 1372362033) );
                // 获取群友资料 不可以跨群哦！ (如果群内没有这个人，则获取失败
                //=============================================================================================================
                // System.out.println(bot.getControl().getUserInfoBySearchFriend(1372362033));
                // 获取QQ用户信息
                //=============================================================================================================
                // bot.getControl().setTroopAnonymousChat(1109012399, true);
                // 开关群匿名
                //=============================================================================================================
                // System.out.println(bot.getControl().generate_anonymous(1109012399).anony_rsp.anony_name);
                // 获取群匿名昵称
                //=============================================================================================================
                // 设置群特殊头衔
                // bot.getControl().setSpecialTitle(1109012399, 1372362033, "秋洛哦");
                //=============================================================================================================



                //>>>>>>>>>>>>>>>>>>>>>>>>API功能
                // System.out.println( bot.getApiBot().getTroopList() );
                // 获取群列表
                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

                Messenger msg = bot.getMessager();
                //
                // msg.message_info.msgFrom = MsgFrom.Friend;
                // msg.message_info.uinInfo.UinCode = 1372362033L;
                // 好友消息
                //
                msg.message_info.msgFrom = MsgFrom.Group;
                msg.message_info.groupInfo.groupCode = 1109012399L;
                // 群消息
                //=============================================================================================================
                // 简易的群消息发送<文字消息>
                // long st = System.currentTimeMillis();
                // msg.addMsg("msg", "！测试发送延迟：" + (System.currentTimeMillis() - st) + "\n");
                // msg.addMsg("msg", "！测试发送延迟：" + (System.currentTimeMillis() - st));
                // msg.send();
                //=============================================================================================================
                // Messager msg = bot.getMessager();
                // msg.message_info.msgFrom = MsgFrom.Group;
                // msg.message_info.groupInfo.GroupCode = 1109012399L;
                // msg.addMsg("msg", "！测试发送图片：");
                // flash 是闪图开关
                // animation 秀图类型 40000~40006
                // 如果闪图为true则这个图片优先是闪图
                // msg.addMsg("img", "https://s1.ax1x.com/2020/08/05/asmnWd.artqq", "flash", "false", "animation", "0");
                // msg.addMsg("img", "3024D54B46FC55D4F370E704194951E5.artqq", "flash", "false", "animation", "0");
                // 仅支持链接发送图片！你可以选择把本地图片转成链接的格式
                // msg.addMsg("img", "ABCDABCDABCDABCD.artqq");
                // 也可以这样发送
                // msg.addMsg("shake", "");
                // 发送窗口抖动
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                // 因为戳一戳会随着次数变大，所以size就是戳一戳的次数
                // poke后面的0是戳一戳type，改了之后可以发敲一敲什么的
                // Type( 1 => 戳一戳, 2 => 比心, 3 => 点赞, 4 => 心碎, 5 => 666, 6 => 放大招 )
                // msg.addMsg("poke", "126", "id", "2011", "size", "1");
                // msg.addMsg("animation", "2", "size", "99999", "name", "秋洛的爱");
                // Type( 1 => 赞, 2 => 爱心, 3 => 哈哈, 4 => 猪头, 5 => 炸弹, 6 => 屎, 7 => 亲亲, 8 => 药丸,  9 => 榴莲, 10 => 略略略, 11 => 平底锅, 12 => 钞票)
                // msg.addMsg("at", "1372362033", "space", "true");
                // space == 艾特是否带空格
                // msg.addMsg("face", "1");
                // face QQ小表情
                // msg.addMsg("flashMsg", "■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■", "id", "2002");
                // QQ闪字
                // msg.addMsg("json", "{\"app\":\"com.tencent.miniapp_01\",\"desc\":\"\",\"view\":\"view_8C8E89B49BE609866298ADDFF2DBABA4\",\"ver\":\"1.0.0.19\",\"prompt\":\"[QQ小程序]哔哩哔哩\",\"appID\":\"\",\"sourceName\":\"\",\"actionData\":\"\",\"actionData_A\":\"\",\"sourceUrl\":\"\",\"meta\":{\"detail_1\":{\"appid\":\"1109937557\",\"title\":\"哔哩哔哩\",\"desc\":\"史上最没面子女鬼【阅片无数58】\",\"icon\":\"https:\\/\\/miniapp.gtimg.cn\\/public\\/appicon\\/432b76be3a548fc128acaa6c1ec90131_200.artqq\",\"preview\":\"qqadapt.qpic.cn\\/adapt\\/0\\/abefe54f-929d-93ca-d0ea-348e3b3c7f59\\/0?pt=0&ek=1#kp=1&sce=70-0-0\",\"url\":\"m.q.qq.com\\/a\\/s\\/5281930130823b3488db538256877cef\",\"scene\":0,\"host\":{\"uin\":203411690,\"nick\":\"luo\"},\"shareTemplateId\":\"8C8E89B49BE609866298ADDFF2DBABA4\",\"shareTemplateData\":{},\"qqdocurl\":\"https:\\/\\/www.bilibili.com\\/video\\/BV1Lp4y1q7Ly\"}},\"config\":{\"type\":\"normal\",\"width\":0,\"height\":0,\"forward\":1,\"autoSize\":0},\"text\":\"\",\"extraApps\":[],\"sourceAd\":\"\",\"extra\":\"\"}");
                // msg.addMsg("xml", "<?xml version=\'1.0\' encoding=\'UTF-8\' standalone=\'yes\' ?><msg serviceID=\"35\" templateID=\"1\" action=\"viewMultiMsg\" brief=\"[聊天记录]\" m_resid=\"BJ+lvMVFBoUjeUQz0+MpugknTksgojGxywFpqA/XhOOW6yFysNWs986cRW0ANmyy\" m_fileName=\"6857569877441139602\" tSum=\"8\" sourceMsgId=\"0\" url=\"\" flag=\"3\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"1\" advertiser_id=\"0\" aid=\"0\"><title size=\"34\" maxLines=\"2\" lineSpace=\"12\">群聊的聊天记录</title><title size=\"26\" color=\"#777777\" maxLines=\"2\" lineSpace=\"12\">秋洛喵哦~:  @孤久、成瘾↑ json卡片写完了</title><title size=\"26\" color=\"#777777\" maxLines=\"2\" lineSpace=\"12\">孤久、成瘾↑:  嗯</title><title size=\"26\" color=\"#777777\" maxLines=\"2\" lineSpace=\"12\">孤久、成瘾↑:  我也终于搞清楚对象类型和方法了</title><title size=\"26\" color=\"#777777\" maxLines=\"2\" lineSpace=\"12\">孤久、成瘾↑:  [笑]</title><hr hidden=\"false\" style=\"0\" /><summary size=\"26\" color=\"#777777\">查看8条转发消息</summary></item><source name=\"聊天记录\" icon=\"\" action=\"\" appid=\"-1\" /></msg>", "serviceID", "35");
                // 聊天记录的xml的serviceID是35，该id，必须与xml代码中写的serviceID一模一样 ID可以不写默认为0，可能导致卡片发不出来
                // msg.addMsg("dice", "6");
                // 骰子
                // msg.addMsg("bface", "269", "name", "/暗中观察");
                // QQ新版表情
                //=============================================================================================================
                // msg.message_info.replyId = 8681;
                // msg.message_info.replyMsg = "自定义回复标题";

                // msg.addMsg("ptt", "file:///C:/Users/pc/Documents/Tencent%20Files/1372362033/FileRecv/group_20200727224602000.amr");
                // msg.addMsg("ptt", "61FAAB3C591EF09DF9E6955823B7C29D.amr");

                System.out.println( msg.send() );
                //=============================================================================================================

                bot.addHandleMessage(new HandleMsg());

        }
}
