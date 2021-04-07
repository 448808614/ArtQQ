package org.artqq;

public class Code {
    /**
     * 是否处于调试模式
     */
    public static final boolean isDebug = true;

    public static byte[] default_tea_key = new byte[16];

    /**
     * 密码
     * 短信验证码
     * 二维码
     * 快速登录
     */
    public static int Login_Type_Password = 1;
    public static int Login_Type_TextMsg = 3;
    public static int Login_Type_QrCookie = 2;
    public static int Login_Type_PTSIG = 4;

    /**
     class WtloginHelper$A1SRC -->
     public static final int A1SRC_PASSWORD = 1;
     public static final int A1SRC_PTSIG = 4;
     public static final int A1SRC_QUICKLOGIN = 2;
     public static final int A1SRC_SMS = 3;
    */


    /**
     * 移动在线
     * WIFI在线
     * 其它
     */
    public static int Online_Type_Mobile = 1;
    public static int Online_Type_WIFI = 2;
    public static int Online_Type_Other = 0;
    
    /**
    LoginSourceType
     public static final int IM = 10;
     public static final int QQConn = 20;
     public static final int QQWallet = 40;
     public static final int unknown = 0;
     public static final int webView = 30;
     public static final int webViewForceRefresh = 31;
     public static final int webViewOnTimeRefresh = 32;
    */
    
}
