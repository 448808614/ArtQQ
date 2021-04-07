package org.artqq;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.artbot.HandleMessage;
import org.artbot.Messenger;
import org.artqq.crypt.EcdhCrypt;
import org.artqq.Wtlogin._Login;
import org.artqq.android.AndroidInfo;
import org.artqq.exceptions.AccountIllegalException;
import org.artqq.exceptions.LoginEnum;
import org.artqq.utils.BytesUtil;
import org.artqq.utils.HexUtil;

public final class ArtBot {
    public ArtBot(long qq, String password) {
        if(qq < 10000 || qq > 4000000000L || password == null){
            throw new AccountIllegalException();
        }else if(!AndroidInfo.isInit){
            throw new RuntimeException("AndroidInfo isn't init, this is a error.");
        }
        bot_number = qq;
        bot_password = password;
        useAndroidQQ();
        // 默认使用AndroidQQ

        data.put("handleFriendMsg", true);
    }

    /**
     * 11上线
     * 21离线
     * 31离开
     * 41隐身
     * 51忙碌
     *
     * 预置的在线状态
     * */
    public int online_status = 11;
    public int ex_online_status = 11;

    public int bot_type = 0;
    /**
        0 >>> 未登录
        1 >>> 已登陆
        -1 >>> Bot作废
        Bot一旦作废，不可以再次使用来登陆，否则会出现错误！
    */
    public int login_type = Code.Login_Type_Password;
    // 登陆类型
    private long bot_number = -1;
    private String bot_password = "";
    public AgreementInfo agreement_info = new AgreementInfo();
    public Record recorder = new Record();
    public ConcurrentHashMap<Object, Object> data = new ConcurrentHashMap<>();
    /**
     client --> TCPSocket 开始登录时写入
     receive --> ReceiveData 开始登陆时写入
     face --> int 登陆成功写入    ||
     gender --> int 登陆成功写入  || 登陆的机器人的年龄，性别，名字
     age --> int 登陆成功写入     ||
     nick --> String 登陆成功写入 ||
     cache_friend_list --> JsonObject 缓存的好友与分组列表 || 在获取完一次后，在这里缓存
     GroupCode-MemList --> JsonObject 某群的成员列表      || 
     SimplifyGroupList --> JsonObject 缓存的群列表        ||
     handleMsg --> 消息处理器列表 Vector<HandleMessage> || 开始添加消息处理器时写入
     isConcurrent --> boolean 是否并发处理消息 || 默认false 你可以自己put一个进来让他变成true
     AnonyName --> String 我的匿名 || 第一次发送匿名消息后写入
     AnonyBody --> 缓存在本地的匿名信息
     handleFriendMsg --> 是否处理好友消息 || boolean
    */
    public BotSigInfo sigInfo = new BotSigInfo();
    
    public synchronized LoginEnum login(){
        synchronized(ArtBot.class){
            BlockingQueue queue = new ArrayBlockingQueue(1);
            _Login login = new _Login(this, queue);
            ThreadManager.newInstance().addExecuteTask(login);
            try {
                queue.take();
            } catch (InterruptedException e) {
                return LoginEnum.UnknownError;
            }
            if(login.result == 0){
                return LoginEnum.LoginSuccess;
            }else if (login.result == -200) {
                return LoginEnum.ServerFailed;
            }else if(login.result == -100){
                return LoginEnum.ProtocolError;
            }else if(login.result == -102){
                return LoginEnum.DeviceLockCaptchaError;
            }else if(login.result == -103){
                return LoginEnum.DeviceLockCaptchaCodeError;
            }else if(login.result == 1){
                return LoginEnum.PasswordError;
            }else if(login.result == -34){
                return LoginEnum.CaptchaError;
            }else if(login.result == -35){
                return LoginEnum.UnUser;
            }else if(login.result == 40){
                return LoginEnum.AccountFreeze;
            }else if(login.result == 100){
                return LoginEnum.CaptchaTicketError;
            }else{
                return LoginEnum.UnknownError;
            } 
        }
        // 客户端错误 result < 0
        // 服务端错误 result > 0
    }


    /**
     * 使用AndroidQQ
     */
    public void useAndroidQQ(){
        agreement_info.appid = 537065283;
        agreement_info.package_name = "com.tencent.mobileqq".getBytes();
        agreement_info.outPacketSessionId = BytesUtil.random_byte(4);
        agreement_info.agreement_ver = "|" + new String(AndroidInfo.imsi) + "|A8.4.5.6b9ba755";
        agreement_info.package_version = "8.4.5";
        agreement_info.package_md5 = HexUtil.hexStringToByte("d3da805e44a86f1f197a89ea56055045");

        agreement_info.serverHost = "msfwifi.3g.qq.com";
        agreement_info.serverPort = 8080;
        // 端口和登陆地址
    }

    /**
     * 使用AndroidTim
     */
    public void useAndroidTim(){
        agreement_info.appid = 537063883;
        agreement_info.package_name = "com.tencent.tim".getBytes();
        agreement_info.outPacketSessionId = HexUtil.hexStringToByte("3C 52 BB 1E");
        agreement_info.agreement_ver = "||A2.5.4.e5ad0e87";
        agreement_info.package_version = "2.5.4";
        agreement_info.package_md5 = HexUtil.hexStringToByte("a6b745bf24a2c277527716f6f36eb68d");
        agreement_info.serverHost = "msfwifi.3g.qq.com";
        agreement_info.serverPort = 8080;
    }
    
    public void setPassword(String word){
        if(word!=null)
            this.bot_password = word;
    }
    
    public void setUin(long qq){
        if(qq > 10000 || qq < 4000000000L){
            this.bot_number = qq;
        }
    }

    public void setOnlineStatus(int online_status) {
        this.online_status = online_status;
    }

    public void setEx_online_status(int ex_online_status) {
        this.ex_online_status = ex_online_status;
    }

    public int getEx_online_status() {
        return ex_online_status;
    }

    public int getOnlineStatus() {
        return online_status;
    }

    public void addHandleMessage(HandleMessage handleMsg){
        Vector<HandleMessage> vector;
        if(data.containsKey("handleMsg")){
            vector = (Vector<HandleMessage>) data.get("handleMsg");
        }else{
            vector = new Vector<HandleMessage>();
            data.put("handleMsg", vector);
            data.put("isConcurrent", false);
        }
        if(handleMsg != null)
            vector.add(handleMsg);
    }
    
    public ReceiveData.HandlePacket addHandlePacket(ReceiveData.HandlePacket handle){
        if(data.containsKey("receive") && handle != null){
            ReceiveData receive = (ReceiveData) data.get("receive");
            receive.vector.add(handle);
        }
        return handle;
    }
    
    public long getUin(){
        return bot_number;
    }
    
    public String getPassword(){
        return bot_password;
    }

    /**
     * 获取操作中心 （协议层）
     */
    public ControlStation getControl(){
        return new ControlStation(this);
    }

    /**
     * 获取消息构建器
     */
    public Messenger getMessager(){
        if(bot_type == 0){
            return null;
        }
        return new Messenger(this);
    }

    /**
     * 获取操作中心 （HttpApi层）
     */
    public ApiBot getApiBot(){
        return new ApiBot(this);
    }

    public static class AgreementInfo {
        public int appid = -1;
        public String package_version = null;
        public byte[] package_name;
        public byte[] package_md5;
        public byte[] outPacketSessionId;
        public String agreement_ver;
        public String builder_ver = "8.4.5.1468";
        public int protocolVersion = 8001;
        
        public String serverHost = "";
        public int serverPort = 0;
        
        public int mMainSigMap = 16724722;
        public int mMiscBitmap = 0x08f7ff7c;
        public long mOpenAppid = 0x2a9e5427;
        public int mSubSigMap = 66560;
        public boolean isGuidAvailable = true;
        public boolean isGuidChanged = false;
        public boolean isGuidFromFileNull = false;
    }
    
    public class Record {
        public Record(){
            ecdh = new EcdhCrypt();
        }
        
        public final EcdhCrypt ecdh;
        private final AtomicInteger Hw_seqFactory = new AtomicInteger(new Random().nextInt(100000));
        private final AtomicInteger LargeSeqFactory = new AtomicInteger(0);
        private final AtomicInteger ssoSeqFactory = new AtomicInteger(0);
        private final AtomicInteger requestIdFactory = new AtomicInteger(1017089978);
        private final AtomicInteger messageSequenceId = new AtomicInteger(22911);
        public byte[] randomKey = BytesUtil.RandomKey(); 
        public byte[] userfulKey = new byte[16]; 
        
        public synchronized int nextHwSeq() {
            int incrementAndGet;
            synchronized (Record.class) {
                incrementAndGet = Hw_seqFactory.incrementAndGet();
                if (incrementAndGet > 1000000) {
                    Hw_seqFactory.set(new Random().nextInt(1060000));
                }
            }
            return incrementAndGet;
        }
        
        public int nextLardgeSeq(){
            int seq = LargeSeqFactory.addAndGet(4);
            return seq;
        }
        
        public synchronized int nextSsoSeq() {
            int seq = ssoSeqFactory.addAndGet(2);
            return seq;
        }
        
        public synchronized int nextRequestId() {
            int id;
            synchronized (Record.class) {
                id = requestIdFactory.addAndGet(2);
                if (id > Integer.MAX_VALUE - 100) {
                    requestIdFactory.set(1017089978);
                }
            }
            return id;
        }
        
        public synchronized int nextMessageSeq() {
            int seq = messageSequenceId.addAndGet(2);
            if (seq > Integer.MAX_VALUE - 100) {
                requestIdFactory.set(new Random().nextInt(22911));
            }
            return seq;
        }
        
        public synchronized byte[] getUseKey(){
            synchronized(Record.class){
                if(sigInfo.D2Key != null){
                    setUseKey(sigInfo.D2Key.clone());
                }
                return userfulKey.clone();
            }
        }
        
        public synchronized byte[] setUseKey(byte[] key){
            synchronized(Record.class){
                userfulKey = key;
                return userfulKey;
            }
        }
        
        public byte[] getPubKey(){
            return ecdh.get_c_pub_key();
        }
        
        public byte[] getShareKey(){
            return ecdh.get_g_share_key();
        }
    }
    
    public class BotSigInfo {
        public byte[] in_ksid;
        public HashMap<Integer, byte[]> loginTLVMap = new HashMap<Integer, byte[]>();
        // 0x530, 0x528 ↑
        
        public HashMap<String, HashMap<String, String>> pSKeyAndPt4Token = new HashMap<>();
        // ↑domain
        //        ↓ key --> pskey and pt4token and createTime and expireTime
        // pSKeyAndPt4Token.get("qun.qq.com").get("pskey")
        
        public String sKey;
        public long sKey_create_time;
        public long sKey_expire_time;
        
        public String psKey;
        public long psKey_create_time;
        
        public byte[] D2;
        public byte[] D2Key = null;
        
        public byte[] userStWebSig;
        public long userStWebSig_create_time;
        public long userStWebSig_expire_time;
        
        public byte[] userStSig;
        public long userStSig_create_time;
        public byte[] userSt_Key;
        
        public byte[] wtSessionTicket;
        public long wtSessionTicketCreatTime;
        public byte[] wtSessionTicketKey = null;
        
        public long app_pri;
        
        public byte[] TGT;
        public byte[] TGTKey;
        
        public byte[] rollbackSig = null;
        
        public String highwayServer = "112.60.8.142";
        public int highwayPort = 80;
        
        
        // 以下皆为未使用
        public byte[] openid;
        public byte[] openkey;
        public long _A2_create_time;
        public long _A2_expire_time;
        public long _D2_create_time;
        public long _D2_expire_time;
        public byte[] _DA2;
        public byte[] _G;
        public byte[] _access_token;
        public long _access_token_create_time;
        public byte[] _aqSig;
        public long _aqSig_create_time;
        public long _create_time;
        public byte[] _device_token;
        public byte[] _dpwd;
        public byte[] _en_A1;
        public int _login_bitmap;
        public byte[] _lsKey;
        public long _lsKey_create_time;
        public long _lsKey_expire_time;
        public byte[] _noPicSig;
        public long _openkey_create_time;
        public byte[] _pay_token;
        public byte[] _pf;
        public byte[] _pfKey;
        public byte[] _pt4Token;
        public byte[] _randseed;
        public byte[] _sid;
        public long _sid_create_time;
        public long _sid_expire_time;
        public byte[] _superKey;
        public byte[] _userA5;
        public long _userA5_create_time;
        public byte[] _userA8;
        public long _userA8_create_time;
        public long _userA8_expire_time;
        public byte[] _userSig64;
        public long _userSig64_create_time;
        public long _vKey_expire_time;
        public byte[] _vkey;
        public long _vkey_create_time;
        public transient long cacheUpdateStamp;
        public int mainSigMap;
    }
}
