package org.artqq.exceptions;

public enum LoginEnum {
    /**
     * 登录成功
     */
    LoginSuccess("登陆成功", -1),
    /**
     * 密码错误
     */
    PasswordError("密码错误", 0),
    /**
     * 服务器连接失败
     */
    ServerFailed("服务器连接失败", 1),
    /**
     * 协议出现错误
     */
    ProtocolError("协议构建错误", 2),
    /**
     * 账号冻结
     */
    AccountFreeze("账号冻结", 3),
    /**
     * 设备锁验证错误
     */
    DeviceLockCaptchaError("设备锁验证码申请次数过多", 4),
    DeviceLockCaptchaCodeError("设备锁验证码错误", 5),
    /**
     * 滑块出现错误
     */
    CaptchaTicketError("滑块验证码错误", 6),
    /**
     * 未知用户
     */
    UnUser("账号未启用", 9),
    UnknownError("未知错误", 7),
    CaptchaError("滑块错误", 8);
    
    
    private LoginEnum(String str, int type){
        this.msg = str;
        this.type = type;
    }
    
    public String msg;
    public int type;
    
    public String getMsg(){
        return msg;
    }
    
    public int getType(){
        return type;
    }
    
    public void setType(int t){
        type = t;
    }
    
    public void setMsg(String m){
        msg = m;
    }
    
}
