package org.artbot;
import com.google.gson.JsonObject;

/**
 * @author luoluo
 */
public class MessageInfo {
    public MsgFrom msgFrom = MsgFrom.ArtQQ;
    public final GroupInfo groupInfo = new GroupInfo();
    public final UinInfo uinInfo = new UinInfo();

    /**
     * 消息id
     */
    public int msgId;

    /**
     * 消息发送时间
     */
    public Long msgTime;

    /**
     * replyQQ 被回复人QQ
     * replyId 被回复消息Id
     * replyMsg 回复标题
     */
    public Long replyUin;
    public Integer replyId;
    public String replyMsg;

    public boolean isAnonymous = false;
    public boolean isMyAnonymous = false;

    public static class UinInfo {
        public Long uinCode;
        public String uinName;

        /**
         * 如果处于群消息，则该值为群昵称，否则为空
         */
        public String groupCard = "";


        /**
         * appid int 目标的APPID 作用：获取对方用什么版本的QQ聊天
         * honor JsonArray [] 获得的群荣誉 例如：群龙王
         */
        public JsonObject otherInfo = new JsonObject();
    }
    
    public static class GroupInfo {
        public Long groupCode;
        public String groupName;
    }
}
