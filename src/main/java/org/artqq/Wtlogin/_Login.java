package org.artqq.Wtlogin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import org.artqq.util.CommonResult;
import org.artqq.util.TenCentCaptchaUtils;
import org.artqq.ArtBot;
import org.artqq.ReceiveData;
import org.artqq.android.AndroidInfo;
import org.artqq.utils.*;
import org.artqq.utils.bytes.ByteObject;
import org.artqq.ThreadManager;

public class _Login extends Thread {
    public _Login(ArtBot bot, BlockingQueue queue){
        this.bot = bot;
        this.queue = queue;
    }
    
    public ArtBot bot;
    public boolean isOver = false;
    public int result = -1;
    public BlockingQueue queue;
    boolean rock = false;
    int rockbackType = 1;
    int captId = 0;

    @Override
    public void run() {
        super.run();
        System.out.println("[Bot]Begin loginning.");
        sendWtLogin();
    }
   
    public void sendWtLogin(){
        if(SocketUtil.isRunning(bot.agreement_info.serverHost, bot.agreement_info.serverPort)){
            try {
                TCPSocket client = new TCPSocket(bot.agreement_info.serverHost, bot.agreement_info.serverPort);
                if(client.code != 0){
                    result = -200;
                    over();
                }else{
                    bot.data.put("client", client);
                    ReceiveData receive = new ReceiveData(bot);
                    bot.data.put("receive", receive);
                    int seq = bot.recorder.nextSsoSeq();
                    _Tlv t = _Tlv.getTlv(bot);
                    byte[] soucre = t.make_tlv_body(seq).encypt(bot.recorder.getShareKey());
                    byte[] sendData = QQAgreementUtils.encodeRequest(bot, seq, "wtlogin.login", soucre);
                    ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("wtlogin.login"){
                            public boolean helpJudgement(){
                                if(bot.bot_type == 0)
                                    return true;
                                else 
                                    return false;
                            }
                    });
                    client.send(sendData);
                    handle.waitPacket();
                    if (handle.source == null) {
                        result = -100;
                        over();
                    }else{
                        ByteObject wt = new ByteObject(handle.source);
                        parseWt(wt, client, null);
                    }
                }
            } catch (IOException e) {
                result = -200;
                over();
            }
        }else{
            result = -200;
            over();
        }
    }
    
    public void parseWt(ByteObject wt, TCPSocket client, HashMap<Integer, byte[]> smap){
        wt.readInt(); // len
        wt.readByte(); // 02
        wt.readShort(); // shortLen
        wt.readShort(); // 8001
        wt.readShort(); // 0x810
        wt.readShort(); // 0x1
        wt.readInt(); // uin
        wt.readBytes(2);
        int type = wt.readByte() & 0xff;
        System.out.println("WtType："+type);
        ByteObject tv = new ByteObject( Tools.TeaDecrypt(wt.readBytes(wt.data.length - wt.position - 1), type == 180 ? bot.recorder.randomKey: bot.recorder.getShareKey() ));
        HashMap<Integer, byte[]> map = ParseTlv( smap == null ? new HashMap<Integer, byte[]>() : smap , tv, bot, true);
        switch(type){
            case 0:{
                onLoginSuccess(map, client);
                break;
            }
            case 1:{ // 密码错误
                result = 1;
                bot.bot_type = -1;
                over();
                break;
            }
            case 2:{
                captId = 1;
                onCaptcha(map, client);
                break;
            }
            case 237:{
                System.out.println("该账户未被启用！！！");
                result = -35;
                over();
                break;
            } // 滑块没有滑
            case 6:{ // 滑块ticket错误
                System.out.println("网络环境错误，禁止登录！");
                result = -100;
                over();
                break;
            }
            case 9:{ // 协议错误
                result = -100;
                bot.bot_type = -1;
                over();
                break;
            }
            case 40:{ // 冻结
                result = type;
                bot.bot_type = -1;
                over();
                break;
            }
            case 160:{
                if(rock != true){
                    onDeviceLock(map, client);
                }else
                    onSuccessSendVC(map, client);
                break;
            }
            case 161:
            case 162:{
                System.out.println("短信额度限制B：今日短信登录尝试次数过多，请等待一天后再试。");
                result = -102;
                bot.bot_type = -1;
                over();
                break;
            }
            case 163:{
                System.out.println("设备锁验证码错误！请重新尝试登陆！");
                result = -103;
                bot.bot_type = -1;
                over();
                break;
            }
            case 180 :{
                System.out.println("回滚加载中...正在回滚...");
                onRollBack(map, client);
                break;
            }
            case 204:{ // 设备锁二层验证
                onLoginDevlock_G(map, client);
                break;
            }
            case 239:{
                onDeviceLock(map, client);
                break;
            }
            default :{
                bot.bot_type = -1;
                result = type;
                over();
            }
        }
    }

    int rockTime = 0;

    private void onRollBack(HashMap<Integer, byte[]> map, TCPSocket client) {
        if(map.containsKey(0x161)){
            if(rockbackType < 0) {
                AndroidInfo.isJdk = false;
                rockbackType = -1;
            }else if(rockbackType == 2){
                bot.agreement_info.serverHost = "113.96.12.224";
                bot.agreement_info.serverPort = 8080;
            }else{
                rockbackType = -1;
                AndroidInfo.isJdk = true;
            }
            map = ParseTlv(map, new ByteObject(map.get(0x161)), bot, false);
            if(map.containsKey(0x172)) bot.sigInfo.rollbackSig = map.get(0x172);
            final int seq = bot.recorder.nextSsoSeq();
            _Tlv t = _Tlv.getTlv(bot);
            byte[] soucre = t.make_tlv_body(seq).encypt(bot.recorder.getShareKey());
            byte[] sendData = QQAgreementUtils.encodeRequest(bot, seq, "wtlogin.login", soucre);
            ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("wtlogin.login"){
                    public boolean helpJudgement(){
                        if(bot.bot_type == 0 && ssoSeq == seq)
                            return true;
                        else 
                            return false;
                    }
            });
            client.send(sendData);
            handle.waitPacket();
            parseWt(new ByteObject(handle.source), client, map);
        }else{
            result = -100;
            over();
        }
    }

    public void onSuccessSendVC(HashMap<Integer, byte[]> map, TCPSocket client) {
        Scanner sc = new Scanner(System.in);   
        System.out.println("请输入验证码：");  
        String code = sc.next();
        ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("wtlogin.login"){
            public boolean helpJudgement(){
                if(bot.bot_type == 0)
                    return true;
                else 
                    return false;
            }
        });
        client.send(VerifyCode.sendVerifyCode(bot, code, map.get(0x104), map.get(0x174), map.get(0x402)));
        handle.waitPacket();
        parseWt(new ByteObject(handle.source), client, map);
    }
    
    
    private void onLoginDevlock_G(HashMap<Integer, byte[]> map, TCPSocket client) {
        byte[] send = VerifyCode.passVerify(bot, map.get(0x104), map.get(0x402), map.get(0x403));
        ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("wtlogin.login"){
            public boolean helpJudgement(){
                 if(bot.bot_type == 0)
                    return true;
                 else 
                    return false;
            }
        });
        client.send(send);
        handle.waitPacket();
        parseWt(new ByteObject(handle.source), client, map);   
    }

    // 滑块
    private void onCaptcha(HashMap<Integer, byte[]> map, TCPSocket client) {
        System.out.println("进入滑块！！！");
        if(captId == 1){
            if(map.containsKey(0x192)){
                String url = new String(map.get(0x192));
                CommonResult<Map<String, String>> result = TenCentCaptchaUtils.INSTANCE.identifyByUrl(url);
                if(result.getCode() != 200){
                    this.result = 100;
                    over();
                    return;
                }
                String ticket  = result.getT().get("ticket");
                System.out.println("滑块Ticket：" + ticket + "\nLen:" + ticket.length());
                ByteObject sidr = new ByteObject();
                sidr.WriteShort(2, 5);
                _Tlv tlv = _Tlv.getTlv(bot);
                sidr.WriteBytes(
                    tlv.get_tlv_193(ticket),
                    tlv.get_tlv_8(),
                    tlv.get_tlv_104(map.get(0x104)),
                    tlv.get_tlv_116(),
                    tlv.get_tlv_547(map.get(0x546))
                );
                final int seq = bot.recorder.nextSsoSeq();
                ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("wtlogin.login"){
                    @Override
                    public boolean helpJudgement(){
                        if(bot.bot_type == 0 && ssoSeq == seq) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                byte[] soucre = sidr.encypt(bot.recorder.getShareKey());
                byte[] sendData = QQAgreementUtils.encodeRequest(bot, seq, "wtlogin.login", soucre);
                client.send(sendData);
                handle.waitPacket();
                parseWt(new ByteObject(handle.source), client, map);
            }else{
                result = -34;
                over();
            }
        }

    }

    // 登陆成功
    private void onLoginSuccess(HashMap<Integer, byte[]> map, TCPSocket client) {
        // T161 does not require parsing because it does not require
        long creationTime = System.currentTimeMillis() / 1000;
        long expireTime = creationTime + 2160000;
        if(map.containsKey(0x119)){
            HashMap<Integer, byte[]> map_119 = ParseTlv(new HashMap<Integer, byte[]>(), new ByteObject(Tools.TeaDecrypt(map.get(0x119), AndroidInfo.getTgtKey())), bot, false);
            for(Map.Entry<Integer, byte[]> entry : map_119.entrySet()){
                int key = entry.getKey();
                byte[] value = entry.getValue().clone();
                switch(key){
                    case 0x103 :{
                        bot.sigInfo.userStWebSig  = value;
                        bot.sigInfo.userStWebSig_create_time = creationTime;
                        bot.sigInfo.userStWebSig_expire_time = expireTime;
                        break;
                    }
                    case 0x108 :{
                        bot.sigInfo.in_ksid = value;
                        break;
                    }
                    case 0x10a :{
                        bot.sigInfo.TGT = value;
                        break;
                    }
                    case 0x10d :{
                        bot.sigInfo.TGTKey = value;
                        break;
                    }
                    case 0x10e :{
                        bot.sigInfo.userSt_Key = value;
                        break;
                    }
                    case 0x114 :{
                        bot.sigInfo.userStSig = value;
                        bot.sigInfo.userStSig_create_time = creationTime;
                        break;
                    }
                    case 0x118 :
                    case 0x550:
                    case 0x163:
                    case 0x11d:
                    case 0x10c:
                    case 0x522:
                    case 0x138 :
                    case 0x16a:
                    case 0x106 : {
                        // This is an empty Tlv.
                        // If it is not empty, the content is a string.
                        break;
                    }
                    case 0x11a :{
                        ByteObject t11a = new ByteObject(value);
                        int face = t11a.readShortNumber().toInt();
                        int age = t11a.readByteNumber().toInt();
                        int gender = t11a.readByteNumber().toInt();
                        String nick = t11a.readString( t11a.readByteNumber().toInt() );
                        bot.data.put("face", face);
                        bot.data.put("age", age);
                        bot.data.put("gender", gender);
                        bot.data.put("nick", nick);
                        break;
                    }
                    case 0x11f :{
                        ByteObject t11f = new ByteObject(value);
                        if(t11f.length() >= 4)
                            bot.sigInfo.app_pri = t11f.readInt();
                        else
                            bot.sigInfo.app_pri = 4294967295L;
                        break;
                    }
                    case 0x120 :{
                        bot.sigInfo.sKey  = new String(value);
                        bot.sigInfo.sKey_create_time = creationTime;
                        bot.sigInfo.sKey_expire_time = expireTime;
                        break;
                    }
                    case 0x130 :{
                        System.out.println("The content of the TLV is the client time difference and IP.");
                        // The content of the TLV is the client time difference and IP.
                        break;
                    }
                    case 0x133 :{
                        bot.sigInfo.wtSessionTicket = value;
                        bot.sigInfo.wtSessionTicketCreatTime = creationTime;
                        break;
                    }
                    case 0x134 :{
                        bot.sigInfo.wtSessionTicketKey = value;
                        break;
                    }
                    case 0x143 :{
                        bot.sigInfo.D2 = value;
                        break;
                    }
                    case 0x16d :{
                        String src = new String(value);
                        bot.sigInfo.psKey = src;
                        bot.sigInfo.psKey_create_time = creationTime;
                        break;
                    }
                    case 0x305 :{
                        bot.sigInfo.D2Key = value;
                        bot.recorder.setUseKey(value.clone());
                        break;
                    }
                    case 0x512 :{
                        ByteObject t512 = new ByteObject(value);
                        int size = t512.readShort();
                        for(int i = 0;i < size;i++){
                            String domain = t512.readStringByShortLen();
                            String pskey = t512.readStringByShortLen();
                            String p4token = t512.readStringByShortLen();
                            HashMap<String, String> vMap = new HashMap<String, String>();
                            vMap.put("pskey", pskey);
                            vMap.put("p4token", p4token);
                            vMap.put("createTime", String.valueOf(creationTime));
                            vMap.put("expireTime", String.valueOf(expireTime));
                            // System.out.println(domain + "||" + pskey);
                            bot.sigInfo.pSKeyAndPt4Token.put(domain, vMap);
                        }
                        break;
                    }
                    case 0x528 :
                    case 0x530 : {
                        bot.sigInfo.loginTLVMap.put(key, value);
                        break;
                    }
                    case 0x537 :{
                        ByteObject t537 = new ByteObject(value);
                        long uin = t537.readInt();
                        byte[] ip = t537.readBytes(t537.readByte() & 0xff);
                        long time = t537.readInt();
                        long version = t537.readInt();
                        break;
                    }
                    default :{
                        System.out.println("Can't Parse 0x" + Integer.toHexString(key));
                        break;
                    }
                }
            }
            // 登陆成功 开始上线
            ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("StatSvc.register"){
                public boolean helpJudgement(){
                    if(bot.bot_type == 0)
                        return true;
                    else 
                        return false;
                }
            });
            client.send(Register.getReq(bot));
            handle.waitPacket();
            Register.Resp resp = Register.getResp(handle.source);
            if(resp.cReplyCode == 0){
                bot.bot_type = 1;
                System.out.println("登陆成功！连接服务器：" + resp.strClientIP + ":" + resp.iClientPort);
                ThreadManager.newInstance().addExecuteTask(new HeartbeatAlive(bot));
                result = 0;
                over();
            }else{
                result = resp.cReplyCode;
                System.out.println("登陆失败！错误原因：" + resp.strResult);
                over();
            }
        }else{
            result = -100;
            over();
        }
    }

    // 在非安全设备登陆
    private void onDeviceLock(HashMap<Integer, byte[]> map, TCPSocket client) {
        System.out.println("Begin Device Lock Resolution");
        String tips = new String(map.get(0x17e));
        String phone = new String(map.get(0x178));
        System.out.println("提示：" + tips.replace("\n", "，") + "\n验证手机号：" + phone);
        ReceiveData.HandlePacket handle = bot.addHandlePacket(new ReceiveData.HandlePacket("wtlogin.login"){
            public boolean helpJudgement(){
                if(bot.bot_type == 0)
                    return true;
                else 
                    return false;
            }
        });
        
        // 申请腾讯验证码
        client.send(VerifyCode.getVerifyCode(bot, map.get(0x104), map.get(0x174)));
        
        handle.waitPacket();
        
        rock = true;
        parseWt(new ByteObject(handle.source), client, map);
    }
  
    public void over(){
        try {
            queue.put("End");
        } catch (InterruptedException e) {}
    }
    
    public static HashMap<Integer, byte[]> ParseTlv(HashMap<Integer, byte[]> tlv_map, ByteObject tv, ArtBot bot, boolean _09){
        if(_09){
            int gone = tv.readShort();
            byte login_type = tv.readByte();
        }
        int tlv_count = tv.readShort();
        for(int i = 0; i < tlv_count;i++){
            int tlv_id = tv.readShort();
            int tlv_size = tv.readShort();
            byte[] tlv_body = tv.readBytes(tlv_size);
            // System.out.println("Prase Tlv" + Integer.toHexString(tlv_id));
            // System.out.println(HexUtil.bytesToHexString(tlv_body));
            // System.out.println(new String(tlv_body));
            tlv_map.put(tlv_id, tlv_body);
        }
        return tlv_map;
    }
    
}
