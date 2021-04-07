package org.artqq;

import com.google.gson.JsonObject;
import okhttp3.Response;
import org.artqq.ArtBot;
import org.artqq.utils.OkHttpUtil;
import org.artqq.utils.QQAgreementUtils;
import org.artqq.utils.Tools;

import java.io.IOException;
import java.util.HashMap;

/**
 * 该类所有接口皆有可能作废
 * @author luoluo
 */
public class ApiBot {
    public static final String QQ_UA = "Mozilla/5.0 (Linux; Android 10; M2002J9E Build/QKQ1.191222.002; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/77.0.3865.120 MQQBrowser/6.2 TBS/045328 Mobile Safari/537.36 V1_AND_SQ_8.4.5_1468_YYB_D QQ/8.4.5.4745 NetType/WIFI WebP/0.3.0 Pixel/1080 StatusBarHeight/70 SimpleUISwitch/0 QQTheme/1000 InMagicWin/0";
    public static final String QUN_URL = "https://qun.qq.com/cgi-bin/";
    public static final String QUN_WEB_URL = "https://web.qun.qq.com/cgi-bin/";
    public static final String QUA = "V1_AND_SQ_8.4.5_1468_YYB_D";

    public ApiBot(ArtBot bot){
        this.bot = bot;
    }

    private final ArtBot bot;

    /**
     * @author luoluo
     * @date 2020/8/27
     * @return com.google.gson.JsonObject
     * @desc 获取QQ群列表
     */
    public JsonObject getTroopList(){
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.setUserAgent(QQ_UA);
        addCookies(okHttpUtil, 0);
        Response response = okHttpUtil.postData(QUN_URL + "qun_mgr/get_group_list", "bkn=" + QQAgreementUtils.GetBkn(bot.sigInfo.sKey));
        int code = response.code();
        if(code == 200){
            try {
                return Tools.parseJsonObject(response.body().string());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @author luoluo
     * @date 2020/9/4
     * @return com.google.gson.JsonObject
     * @desc 获取好友列表
     */
    public JsonObject getFriendList(){
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.setUserAgent(QQ_UA);
        addCookies(okHttpUtil, 0);
        Response response = okHttpUtil.postData(QUN_URL + "qun_mgr/get_friend_list", "bkn=" + QQAgreementUtils.GetBkn(bot.sigInfo.sKey));
        int code = response.code();
        if(code == 200){
            try {
                return Tools.parseJsonObject(response.body().string());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取群成员列表
     *
     * @param groupId 群号
     * @param startIndex 开始索引
     * @param endIndex 结束索引
     * @return JsonObject
     */
    public JsonObject getGroupMember(long groupId, int startIndex, int endIndex){
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.setUserAgent(QQ_UA);
        addCookies(okHttpUtil, 0);
        Response response = okHttpUtil.postData(QUN_URL + "qun_mgr/search_group_members", "bkn=" + QQAgreementUtils.GetBkn(bot.sigInfo.sKey) + "&sort=0&gc=" + groupId + "&st=" + startIndex + "&end=" + endIndex);
        int code = response.code();
        if(code == 200){
            try {
                return Tools.parseJsonObject(response.body().string());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 设置群名片
     *
     * @param groupId 群号
     * @param uin 目标
     * @param card 群名片
     * @return JsonObject
     */
    public JsonObject setGroupCard(long groupId, long uin, String card){
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.setUserAgent(QQ_UA);
        addCookies(okHttpUtil, 0);
        Response response = okHttpUtil.postData(QUN_URL + "qun_mgr/set_group_card", "bkn=" + QQAgreementUtils.GetBkn(bot.sigInfo.sKey) + "&gc=" + groupId + "&u=" + uin + "&name=" + card);
        int code = response.code();
        if(code == 200){
            try {
                return Tools.parseJsonObject(response.body().string());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @author luoluo
     * @date 2020/8/27
     * @return com.google.gson.JsonObject
     * @desc 获取个人信息
     */
    public JsonObject getMyInfoByQun(){
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.setUserAgent(QQ_UA);
        addCookies(okHttpUtil, 0);
        Response response = okHttpUtil.postData(QUN_URL + "qunwelcome/myinfo?callback=?&bkn=" + QQAgreementUtils.GetBkn(bot.sigInfo.sKey), "");
        int code = response.code();
        if(code == 200){
            try {
                return Tools.parseJsonObject(response.body().string());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @author luoluo
     * @date 2020/8/29
     * @param groupid 群号
     * @param state 状态
     * @return com.google.gson.JsonObject
     * @desc 设置群一起听歌状态
     */
    public JsonObject setQunMediaState(long groupid, boolean state){
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.setUserAgent(QQ_UA);
        addCookies(okHttpUtil, 0);
        String bkn = String.valueOf(QQAgreementUtils.GetBkn(bot.sigInfo.sKey));
        Response response = okHttpUtil.getData(QUN_WEB_URL +
            "media/set_media_state?t=0.4057753239630908&g_tk=" + bkn +
            "&state=" + (state ? 1 : 0) +
            "&gcode=" + groupid +
            "&qua=" + QUA +
            "&uin=" + bot.getUin() +
            "&format=json&inCharset=utf-8&outCharset=utf-8"
        );
        int code = response.code();
        if(code == 200){
            try {
                return Tools.parseJsonObject(response.body().string());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 群签到 （废除）
     * 无法实现群签到接口
     *
     * @param groupId 群号
     * @param text 签到内容
     */
    public void GroupQianDao(long groupId, String text){
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.setUserAgent(QQ_UA);
        okHttpUtil.addHeader("Referer", String.format("https://qun.qq.com/qqweb/m/qun/checkin/index.html?_bid=2485&_wv=67108867&gc=%s&state=1&from=appstore_aio", groupId));
        addCookies(okHttpUtil, 1);
        String bkn = String.valueOf(QQAgreementUtils.GetBkn(bot.sigInfo.sKey));
        HashMap<String, String> map = new HashMap<>();
        map.put("client", "2");
        map.put("bkn", bkn);
        map.put("gallery_info", "{\"category_id\":9,\"page\":0,\"pic_id\":120}");
        map.put("template_data", "");
        map.put("template_id", "[object Object]");
        map.put("lgt", "0");
        map.put("lat", "0");
        map.put("poi", "");
        map.put("pic_id", "");
        map.put("gc", String.valueOf(groupId));
        map.put("text", text);
        try {
            Response response = okHttpUtil.postData("https://qun.qq.com/cgi-bin/qiandao/sign/publish?retry=1&#60wnsres=%7B%22msg%22%3A%22timeout%22%7D", map);
            int code = response.code();
            if(code != 200){
                System.out.println("Group QianDao is Error!!!");
            }else{
                System.out.println(response.body().string());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void addCookies(OkHttpUtil okHttpUtil, int type){
        String pskey = bot.sigInfo.psKey;
        switch(type){
            case 1:
            case 0:{
                pskey = bot.sigInfo.pSKeyAndPt4Token.get("qun.qq.com").get("pskey");
                break;
            }
        }
        okHttpUtil.addHeader("Cookie",
            "p_uin=o" + bot.getUin() +
            ";uin=" + bot.getUin() +
            ";qq_local_id=2052;gdt_uid=0_" + bot.getUin()+";" +
            "skey=" + bot.sigInfo.sKey +
            ";p_skey=" + pskey +
                (type == 1 ? ";traceid=0" : "")
            );
        okHttpUtil.addHeader("x-requested-with", new String(bot.agreement_info.package_name));
    }

}
