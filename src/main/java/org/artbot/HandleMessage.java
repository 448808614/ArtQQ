package org.artbot;

import org.artqq.ArtBot;

public abstract class HandleMessage {
    /**
     * HijackMessage 劫持消息禁止其它消息处理器处理
     * GoneMessage 允许其他消息处理器处理消息（隐藏本消息处理器存在）
     * PassMessage 无计划
     */
    public static int HijackMessage = 1;
    public static int GoneMessage = 2;

    public ArtBot bot = null;

    /**
     * 群消息处理
     *
     * @param messenger 消息处理器
     * @param uin 消息发送者
     * @param groupid 消息来源群号
     * @param msgid 消息标识
     * @param msg 消息内容
     * @return int 消息处理结束后操作
     */
    public abstract int handleGroupMessage(Messenger messenger, long uin, long groupid, int msgid, String msg);

    /**
     * 群事件处理
     *
     * @param groupid 来源群号
     * @param uin 操作人
     * @param funType 事件类型
     * @param funStatus 事件状态码
     */
    public abstract void handleGroupFunctionSwitch(long groupid, long uin, int funType, int funStatus);

    /**
     * 群禁言事件处理
     *
     * @param groupid 来源群号
     * @param admin 操作人
     * @param uin 被操作人
     * @param time 禁言时间（秒）
     */
    public abstract void handleGroupBan(long groupid, long admin, long uin, int time);


    /**
     * 群消息撤回事件
     *
     * @param groupid 来源群号
     * @param admin 操作人
     * @param msgSeq 消息标识
     * @param msgTime 消息发送时间
     * @param msgSender 消息来源人
     */
    public abstract void handleGroupWithDraw(long groupid, long admin, int msgSeq, long msgTime, long msgSender);


    /**
     * 处理好友消息
     *
     * @param messenger 消息构建器
     * @param uin 消息发送者
     * @param msgid 消息1id
     * @param msg 消息内容
     * @return int 消息处理结束后操作
     */
    public abstract int handleFriendMessage(Messenger messenger, long uin, int msgid, String msg) ;


    /**
     * 处理私聊消息
     *
     * @param messenger 消息构建器
     * @param uin 消息来源人
     * @param msgid 消息id
     * @param msg 消息内容
     * @return 消息处理结束后操作
     */
    public abstract int handleTemporaryMessage(Messenger messenger, long uin, int msgid, String msg) ;

    /**
     * 群名字发生变更事件
     *
     * @param groupid 来源群号
     * @param uin 操作人
     * @param name 变更后
     */
    public abstract void handleGroupNameChange(long groupid, long uin, String name);

    /**
     * 群成员数量变化
     * @param type 类型
     * {
     *      value    1   退出（主动，
     *      value    3   邀请（被动，
     *      value    2   加群（主动
     *      value    4   踢出
     * }
     * @param groupid 群号
     * @param admin 操作人
     * @param uin 变化人
     * @param nick 变化人名字
     * @param time 时间
     */
    public abstract void handleGroupMemberChange(int type, long groupid, long admin, long uin, String nick, long time);
}
