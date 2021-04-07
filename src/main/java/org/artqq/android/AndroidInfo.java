package org.artqq.android;
import org.artqq.utils.VirtualAndroidUtil;
import org.artqq.MD5;
import org.artqq.Code;
import org.artqq.utils.Tools;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;

// 以下参数未尽允许建议不要修改
public class AndroidInfo {
    public static void initByRand(){
        imei = VirtualAndroidUtil.getIMEI().getBytes();
        imsi = VirtualAndroidUtil.getImsi().getBytes();
        macAddress = VirtualAndroidUtil.getMacAddr().getBytes();
        isInit = true;
    }
    
    public static void initByFile(String filePath){
        try {
            String text = new String(Tools.readFileBytes(filePath));
            initByJson(text);
        } catch (Exception e) {
            initByRand();
        }
        saveConfigToFile(filePath);
    }

    public static void initByJson(String _json){
        JsonObject json = Tools.parseJsonObject(_json);
        imei = json.get("imei").getAsString().getBytes();
        imsi = json.get("imsi").getAsString().getBytes();
        macAddress = json.get("macAddress").getAsString().getBytes();
        isInit = true;
    }

    private static void saveConfigToFile(String path){
        JsonObject obj = new JsonObject();
        obj.addProperty("imei", new String(imei));
        obj.addProperty("imsi", new String(imsi));
        obj.addProperty("macAddress", new String(macAddress));
        try {
            Tools.saveFile(path, obj.toString());
        } catch (IOException e) {
            throw new UnknownError("Protocol configuration save failed\n"+e.toString());
        }   
    }

    /**
     * 是否初始化完毕AndroidInfo
     */
    public static boolean isInit = false;

    /**
     * 是否处于JDK环境 不可变更
     */
    public static boolean isJdk = false;
    /**
     * 设备名
     */
    public static byte[] machine_name = "小米9".getBytes();
    /**
     * 设备类型
     * ios android windows
     */
    public static byte[] machine_type = "android".getBytes();
    /**
     * 厂商
     */
    public static byte[] machine_manufacturer = "Xiaomi".getBytes();
    /**
     * 系统版本
     */
    public static String machine_version = "8.1.0";
    /**
     * 系统SDK版本
     */
    public static int machine_version_number = 27;
    /**
     * WIFI名
     */
    public static byte[] WifiSSID = "TP-LINK_HiJL".getBytes();
    /**
     * 网络状态
     * 建议看tlv_124.java
     */
    public static String apn = "wifi";
    /**
     * 网络类型
     * 如果apn不是wifi必须修改networktype
     */
    public static int network_type = Code.Online_Type_WIFI;
    public static byte[] imei;
    public static byte[] imsi;
    public static byte[] macAddress;
    
    public static byte[] getGuid(){
        if(imei == null || macAddress == null){
            return "%4;7t>;28<fc.5*6".getBytes();
        }
        return VirtualAndroidUtil.getGuid(MD5.toMD5Byte(new String(macAddress) + new String(imei)));
    }
    
    public static byte[] getTgtKey(){
        return MD5.toMD5Byte(getGuid());
    }
    
    public static byte[] getmacAddressMd5(){
        return MD5.toMD5Byte(macAddress);
    }
}
