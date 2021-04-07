package org.artqq.Wtlogin;

import com.qq.taf.client.ClientPow;
import org.artqq.utils.HexUtil;
import org.artqq.utils.bytes.ByteObject;
import org.artqq.ArtBot;

import java.util.Base64;
import java.util.Random;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.artqq.utils.QQAgreementUtils;
import org.artqq.android.AndroidInfo;
import org.artqq.Code;
import org.artqq.MD5;
import org.artqq.protobuf.DeviceReport;
import org.artqq.utils.BytesUtil;
import org.artqq.utils.RandomUtil;

public class _Tlv {
    public _Tlv(ArtBot bot){
        this.bot = bot;
    }
    
    private final ArtBot bot;
    private final int randInt = BytesUtil.get_rand_32();
    private final long buildTime = 0x5f1e8e18;
    private final long nowTime = System.currentTimeMillis() / 1000;
    int _db_buf_ver = 1;
    int _sso_ver = 12;
    byte[] iPKEY = new byte[2];
    int localId = 2052;
    int _ping_version = 1;
    int _TGTGTVer = 4;
    String build_ver = "6.0.0.2435";
    
    public static _Tlv getTlv(ArtBot bot){
        return new _Tlv(bot);
    }
    
    public byte[] get_tlv_1(byte[] ip){
        if(ip == null || ip.length <= 0 || ip.length > 4){
            ip = new byte[4];
        }
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x1);
        builder.WriteShort(1);
        builder.WriteInt(0);
        builder.WriteInt(bot.getUin());
        builder.WriteInt(nowTime);
        builder.WriteBytes(ip);
        builder.WriteShort(0);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_2(String code, byte[] codeToken){
        // code = CAPTCHA CONTENT
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x2);
        builder.WriteShort(0); // sig_ver
        builder.WriteStringAndShortLen(code);
        builder.WriteBytesAndShortLen(codeToken);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_8(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x8);
        builder.WriteShort(0); // The default is 0.
        builder.WriteInt(localId);
        builder.WriteShort(0);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_18(){
        int _sso_version = 1536;
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x18);
        builder.WriteShort(_ping_version);
        builder.WriteInt(_sso_version);
        builder.WriteInt(16);
        builder.WriteInt(0);
        builder.WriteInt(bot.getUin());
        builder.WriteShort(0);
        builder.WriteShort(0);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_100(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x100);
        builder.WriteShort(_db_buf_ver);
        builder.WriteInt(_sso_ver);
        builder.WriteInt(16); 
        builder.WriteInt(bot.agreement_info.appid); //  过假锁(设备锁) 原本是appid
        builder.WriteInt(0);
        builder.WriteInt(34869472);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_104(byte[] token){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x104);
        builder.WriteString(token);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_106(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x106);
        ByteObject source = new ByteObject();
        source.WriteShort(_TGTGTVer);
        source.WriteInt(0);
        source.WriteInt(_sso_ver);
        source.WriteInt(16);
        source.WriteInt(0);
        source.WriteInt(0);
        source.WriteInt(bot.getUin());
        source.WriteInt(nowTime);
        source.WriteBytes(new byte[4]);
        source.WriteByte(1);
        source.WriteBytes(QQAgreementUtils.passwordMd5ByOne(bot.getPassword()));
        source.WriteBytes(AndroidInfo.getTgtKey());
        source.WriteInt(0);
        source.WriteBoolean(bot.agreement_info.isGuidAvailable);
        if(bot.agreement_info.isGuidAvailable){
            source.WriteBytes(AndroidInfo.getGuid());
        }
        source.WriteInt(bot.agreement_info.appid);
        source.WriteInt(bot.login_type);
        source.WriteStringAndShortLen(String.valueOf(bot.getUin()));
        source.WriteShort(0);
        builder.WriteBytes(source.encypt(QQAgreementUtils.passwordMd5ByTwo(bot.getUin(), bot.getPassword())));
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_107(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x107);
        builder.WriteShort(0);
        builder.WriteByte(0);
        builder.WriteShort(0);
        builder.WriteByte(1);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_109(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x109);
        builder.WriteBytes(AndroidInfo.getGuid());
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_116(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x116);
        builder.WriteByte(0);
        builder.WriteInt(bot.agreement_info.mMiscBitmap);
        builder.WriteInt(bot.agreement_info.mSubSigMap);
        builder.WriteByte(1);
        builder.WriteInt(1600000226L);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_124(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x124);
        builder.WriteBytesAndShortLen(AndroidInfo.machine_type);
        builder.WriteBytesAndShortLen(AndroidInfo.machine_version.getBytes());
        builder.WriteShort(AndroidInfo.network_type);
        builder.WriteStringAndShortLen("China Mobile GSM");
        builder.WriteStringAndLen(AndroidInfo.apn);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_128(){
        int flag = 0 | (17 << 24 & 0xFF000000) ;
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x128);
        builder.WriteShort(0);
        builder.WriteBoolean(bot.agreement_info.isGuidFromFileNull);
        builder.WriteBoolean(bot.agreement_info.isGuidAvailable);
        builder.WriteBoolean(bot.agreement_info.isGuidChanged);
        builder.WriteInt(flag);
        builder.WriteBytesAndShortLen(AndroidInfo.machine_name);
        builder.WriteBytesAndShortLen(AndroidInfo.getGuid());
        builder.WriteBytesAndShortLen(AndroidInfo.machine_manufacturer);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_141(){
        int _version = 1;
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x141);
        builder.WriteShort(_version);
        builder.WriteStringAndShortLen("China Mobile GSM");
        builder.WriteShort(AndroidInfo.network_type);
        builder.WriteBytesAndShortLen(AndroidInfo.apn.getBytes());
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_142(){
        int _version = 0;
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x142);
        builder.WriteShort(_version);
        builder.WriteBytesAndShortLen(bot.agreement_info.package_name);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_144(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x144);
        
        ByteObject source = new ByteObject();
        source.WriteShort(5); // tlv size
        source.WriteBytes(get_tlv_16e());
        source.WriteBytes(get_tlv_52d());
        source.WriteBytes(get_tlv_109());
        source.WriteBytes(get_tlv_128());
        source.WriteBytes(get_tlv_124());
        
        builder.WriteBytes(source.encypt(AndroidInfo.getTgtKey()));
        
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_145(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x145);
        builder.WriteBytes(AndroidInfo.getGuid());
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_147(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x147);
        builder.WriteInt(16);
        builder.WriteBytesAndShortLen(bot.agreement_info.package_version.getBytes());
        builder.WriteBytesAndShortLen(bot.agreement_info.package_md5);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_154(int seq){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x154);
        builder.WriteInt(seq);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_166(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x166);
        builder.WriteByte(1);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_16e(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x16e);
        builder.WriteBytes(AndroidInfo.machine_name);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_172(byte[] dt172){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x172);
        builder.WriteBytes(dt172);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_174(byte[] token){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x174);
        builder.WriteBytes(token);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_17c(String code){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x17c);
        builder.WriteStringAndShortLen(code);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_177(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x177);
        builder.WriteByte(1);
        builder.WriteInt(buildTime);
        builder.WriteStringAndLen(build_ver);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_17a(long j){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x17a);
        builder.WriteInt(j);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_187(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x187);
        builder.WriteBytes(AndroidInfo.getmacAddressMd5());
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_188(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x188);
        builder.WriteBytes(AndroidInfo.getGuid());
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_191(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x191);
        builder.WriteByte(0x82);
        return builder.toByteArray();
    }

    public byte[] get_tlv_193(String ticket){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x193);
        builder.WriteBytes(ticket.getBytes());
        return builder.toByteArray();
    }

    public byte[] get_tlv_194(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x194);
        builder.WriteBytes(MD5.toMD5Byte(AndroidInfo.imsi));
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_197(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x197);
        builder.WriteByte(0);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_198(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x198);
        builder.WriteByte(0);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_201(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x201);
        builder.WriteStringAndShortLen("");
        builder.WriteStringAndShortLen("");
        builder.WriteStringAndShortLen("qq");
        builder.WriteStringAndShortLen("");
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_202(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x202);
        builder.WriteBytesAndShortLen(AndroidInfo.WifiSSID);
        builder.WriteBytesAndShortLen( ("\"" + AndroidInfo.apn + "\"").getBytes());
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_511(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x511);
        builder.WriteHexStr("00 0E 01 00 0A 74 65 6E 70 61 79 2E 63 6F 6D 01 00 11 6F 70 65 6E 6D 6F 62 69 6C 65 2E 71 71 2E 63 6F 6D 01 00 0B 64 6F 63 73 2E 71 71 2E 63 6F 6D 01 00 0E 63 6F 6E 6E 65 63 74 2E 71 71 2E 63 6F 6D 01 00 0C 71 7A 6F 6E 65 2E 71 71 2E 63 6F 6D 01 00 0A 76 69 70 2E 71 71 2E 63 6F 6D 01 00 0A 71 75 6E 2E 71 71 2E 63 6F 6D 01 00 0B 67 61 6D 65 2E 71 71 2E 63 6F 6D 01 00 0C 71 71 77 65 62 2E 71 71 2E 63 6F 6D 01 00 09 74 69 2E 71 71 2E 63 6F 6D 01 00 0D 6F 66 66 69 63 65 2E 71 71 2E 63 6F 6D 01 00 0B 6D 61 69 6C 2E 71 71 2E 63 6F 6D 01 00 09 71 7A 6F 6E 65 2E 63 6F 6D 01 00 0A 6D 6D 61 2E 71 71 2E 63 6F 6D");
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_401(byte[] t402){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x401);
        
        ByteObject bo = new ByteObject();
        bo.WriteBytes(AndroidInfo.getGuid());
        bo.WriteBytes(QQAgreementUtils.get_mpasswd().getBytes());
        bo.WriteBytes(t402);
        
        builder.WriteBytes(MD5.toMD5Byte(bo.toByteArray()));
        // guid + get_mpasswd() + t402
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_402(byte[] t402){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x402);
        builder.WriteBytes(t402);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_403(byte[] t403){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x403);
        builder.WriteBytes(t403);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_516(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x516);
        builder.WriteInt(0);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_521(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x521);
        builder.WriteInt(0);
        builder.WriteShort(0);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_525(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x525);
        builder.WriteShort(1);
        builder.WriteBytes(get_tlv_536());
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_52d(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x52d);
        DeviceReport device = new DeviceReport();
        builder.WriteBytes(device.toByteArray());
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_536(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x536);
        builder.WriteByte(1);
        builder.WriteByte(0);
        return builder.toByteArray();
    }
    
    public byte[] get_tlv_542(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x542);
        builder.WriteByte(0);
        builder.WriteByte(0);
        return builder.toByteArray();
    }

    public byte[] get_tlv_544(){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x544);
        builder.WriteInt(2009);
        builder.WriteInt(0);
        builder.WriteShort(46);
        builder.WriteBytesAndShortLen(HexUtil.hexStringToByte("9d89492e0a3777cde8369503a26b4cf7cc6b6d8aefe3ef49829d79f40c53aaea"));
        builder.WriteShort(8);
        builder.WriteInt(0);
        builder.WriteInt(17933);
        builder.WriteHexStr("00 02 08 00 00 00 04 00 00 00 03 00 00 00 01 74 05 29 41 95 35 4D 4D 4D 45 65 4E 4E 66 45 00 14 63 6F 6D 2E 74 65 6E 63 65 6E 74 2E 6D 6F 62 69 6C 65 71 71 41 36 42 37 34 35 42 46 32 34 41 32 43 32 37 37 35 32 37 37 31 36 46 36 46 33 36 45 42 36 38 44 05 B9 20 BE 00 00 00 00 00 00 03 00 00 01 00 10 D5 DB 24 C7 C9 DF 0C BB 5E 78 2A B2 D8 F9 FA FC");
        return builder.toByteArray();
    }

    public byte[] get_tlv_547(byte[] t546){
        tlv_builder builder = new tlv_builder();
        builder.WriteCmd(0x547);
        if(t546 != null) {
            builder.WriteBytes(new ClientPow().getPow(t546));
        }
        return builder.toByteArray();
    }

    public static final int[] tlv_number = {0x18, 0x1, 0x106, 0x116, 0x100, 0x107, 0x108, 0x104, 0x142, 0x112, 0x144, 0x145, 0x147, 0x166, 0x16a, 0x154, 0x141, 0x8, 0x511, 0x172, 0x185, 0x400, 0x187, 0x188, 0x194, 0x191, 0x201, 0x202, 0x177, 0x516, 0x521, 0x525, 0x529, 0x318, 0x544, 0x545, 0x548};
    
    public ByteObject make_tlv_body(int requestSsoSeq){
        ByteObject ret = new ByteObject();
        ByteObject tlv = new ByteObject();
        int size = 0;
        for(int number : tlv_number){
            byte[] data = null;
            switch(number){
                case 0x18:{
                    data = get_tlv_18();
                    break;
                }
                case 0x8 : {
                    data = get_tlv_8();
                    break;
                }
                case 0x1 :{
                    data = get_tlv_1(null);
                    break;
                }
                case 0x100:{
                    data = get_tlv_100();
                    break;
                }
                case 0x106 :{
                    data = get_tlv_106();
                    break;
                }
                case 0x107 :{
                    data = get_tlv_107();
                    break;
                }
                case 0x116 :{
                    data = get_tlv_116();
                    break;
                }
                case 0x141 :{
                    data = get_tlv_141();
                    break;
                }
                case 0x142 :{
                    data = get_tlv_142();
                    break;
                }
                case 0x144 :{
                    data = get_tlv_144();
                    break;
                }
                case 0x145 :{
                    data = get_tlv_145();
                    break;
                }
                case 0x147 :{
                    data = get_tlv_147();
                    break;
                }
                case 0x154 :{
                    data = get_tlv_154(requestSsoSeq);
                    break;
                }
                case 0x166 :{
                    data = get_tlv_166();
                    break;
                }
                case 0x172 :{
                    if(bot.sigInfo.rollbackSig != null){
                        data = get_tlv_172(bot.sigInfo.rollbackSig);
                    }
                    break;
                }
                case 0x177 :{
                    data = get_tlv_177();
                    break;
                }
                case 0x187 :{
                    data = get_tlv_187();
                    break;
                }
                case 0x188 :{
                    data = get_tlv_188();
                    break;
                }
                case 0x191 :{
                    data = get_tlv_191();
                    break;
                }
                case 0x194 :{
                    data = get_tlv_194();
                    break;
                }
                case 0x201 :{
                    data = get_tlv_201();
                    break;
                }
                case 0x202 :{
                    data = get_tlv_202();
                    break;
                }
                case 0x511 :{
                    data = get_tlv_511();
                    break;
                }
                case 0x516 :{
                    data = get_tlv_516();
                    break;
                }
                case 0x521 :{
                    data = get_tlv_521();
                    break;
                }
                case 0x525 :{
                    data = get_tlv_525();
                    break;
                }
                case 0x544 :{
                    data = get_tlv_544();
                    break;
                }
            }
            if(data != null){
                size++;
                tlv.WriteBytes(data);
            }
        }
        ret.WriteShort(9);
        ret.WriteShort(size);
        ret.WriteBytes(tlv.toByteArray());
        return ret;
    }
    
    public static class tlv_builder extends ByteObject {
        int cmd;

        public void WriteCmd(int ver){
            cmd = ver;
        }
        
        @Override
        public byte[] toByteArray() {
            byte[] data = super.toByteArray();
            int len = length();
            clean();
            ByteObject ret = new ByteObject();
            ret.WriteShort(cmd);
            ret.WriteShort(len);
            ret.WriteBytes(data);
            return ret.toByteArray();
        }
        
    }
}
