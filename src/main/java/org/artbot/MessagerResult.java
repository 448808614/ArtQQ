package org.artbot;

public enum MessagerResult {
    /**
     * 一般是协议层错误，携带错误联系秋洛即可
     */
    Error("发送错误", 0),
    /**
     * 发送成功不意味着发出，只是把包提交给了腾讯
     */
    Success("发送成功", 1),
    /**
     * 例如：声明是群消息但是没有群号
     */
    InputError("输入参数有误", 2),
    /**
     * 消息超过可发送消息的最大长度
     */
    MessageLengthMax("爸爸，你这个消息长过头了", 3),
    /**
     * 消息内没有消息体或消息体无效
     */
    EmptyMessage("爸爸，求求你给我个不是空的消息吧！记得里面的消息要有效，别给我个图片链接还是404的就行", 4);

    MessagerResult(String msg, int id){
        this.msg = msg;
        this.id = id;
    }

    public final String msg;
    public final int id;
}
