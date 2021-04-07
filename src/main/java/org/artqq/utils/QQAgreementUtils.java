package org.artqq.utils;
import com.qq.jce.wup.UniPacket;
import java.security.SecureRandom;
import java.util.Random;
import org.artqq.ArtBot;
import org.artqq.MD5;
import org.artqq.android.AndroidInfo;
import org.artqq.protobuf.highway.HwData;
import org.artqq.utils.bytes.ByteObject;

public class QQAgreementUtils {
    public static <T> T decodePacket(byte[] src, String FuncName, T t){
        try{
            UniPacket uniPacket = new UniPacket(true);
            uniPacket.setEncodeName("UTF-8");
            uniPacket.decode(src);
            return uniPacket.getByClass(FuncName, t);
        }catch (Exception e){
            System.out.println(e);
            System.out.println("decodePacketError " + HexUtil.bytesToHexString(src));
            return null;
        }
    }
    
    public static <T> UniPacket decodePacketRetUni(byte[] src, String FuncName, T t){
        try{
            UniPacket uniPacket = new UniPacket(true);
            uniPacket.setEncodeName("UTF-8");
            uniPacket.decode(src);
            return uniPacket;
        }catch (Exception e){
            System.out.println("decodePacket2Error " + HexUtil.bytesToHexString(src));
            return null;
        }
    }
    
    public static <T> byte[] encodePacket(T t, String ServantName, String funcName, String mapName, int requestid){
        try{
            UniPacket uniPacket = new UniPacket(true);
            uniPacket.setEncodeName("UTF-8");
            uniPacket.setFuncName(funcName);
            uniPacket.setServantName(ServantName);
            uniPacket.put(mapName, t);
            uniPacket.setRequestId(requestid);
            return uniPacket.encode();
        }catch (Exception e){
            return null;
        }
    }

    public static byte[] make_hw_packet(HwData data){
        /*int cmdid = 0;
         switch(cmd){
         case "PicUp.Echo":{
         cmdid = 0;
         break;
         }
         case "PicUp.DataUp":{
         cmdid = 77;
         break;
         }
         }*/
        ByteObject byte_obj = new ByteObject();
        byte_obj.WriteString("(");
        byte_obj.WriteInt(data.head == null ? 0 : data.head.length);
        byte_obj.WriteInt(data.data == null ? 0 : data.data.length);
        if(data.head != null) byte_obj.WriteBytes(data.head);
        if(data.data != null) byte_obj.WriteBytes(data.data);
        byte_obj.WriteString(")");
        return byte_obj.toByteArray();
    }
    
    public static byte[] passwordMd5ByOne(String password){
        return MD5.toMD5Byte(password.getBytes());
    }
    
    public static byte[] passwordMd5ByTwo(long uin, String password){
        byte[] password_md51 = passwordMd5ByOne(password);
        byte[] md5s = new byte[24];
        System.arraycopy(password_md51, 0, md5s, 0, password_md51.length);
        BytesUtil.int64_to_buf(md5s, 16, uin);
        return MD5.toMD5Byte(md5s);
    }
    
    public static int SessionPack = 11;
    public static int DefaultPack = 10;
    
    public static byte[] encodeRequest(ArtBot bot, int ssoSeq, String serviceCmd, byte[] src, byte[]... addValue){
        byte[] token = null;
        byte[] key = bot.bot_type == 0 ? new byte[16] : bot.recorder.getUseKey();
        if(addValue.length > 0){
            if(addValue.length > 0 && addValue[0] != null)
                token = addValue[0];
            if(addValue.length > 1 && addValue[1] != null)
                key = addValue[1];
        }
        ByteObject request_head = new ByteObject();
        if(bot.bot_type == 0 || serviceCmd.equals("Heartbeat.Alive") || serviceCmd.equals("StatSvc.register")){
            request_head.WriteInt(DefaultPack);
        }else if(bot.bot_type == 1){
            request_head.WriteInt(SessionPack);
        }
        if(serviceCmd.equals("Heartbeat.Alive")){
            request_head.WriteByte(0);
        }else if(serviceCmd.equals("StatSvc.register")){
            request_head.WriteByte(1);
        }else if(bot.bot_type == 0){
            request_head.WriteByte(2);
        }else if(bot.bot_type == 1){
            request_head.WriteByte(1);
        }
        if(serviceCmd.equals("wtlogin.login") && bot.bot_type == 0){
            request_head.WriteInt(0 + 4);
        }else if(serviceCmd.equals("StatSvc.register") && token != null) {
            request_head.WriteBytesAndLen(token, 4);
        }else if(serviceCmd.equals("Heartbeat.Alive")) {
            request_head.WriteInt(0 + 4);
        }else{
            request_head.WriteInt(ssoSeq);
        }
        if(serviceCmd.equals("Heartbeat.Alive")){
            request_head.WriteShort(0);
        }else{
            request_head.WriteByte(0);
        }
        if(serviceCmd.equals("Heartbeat.Alive")){
            request_head.WriteInt(1328);
            ByteObject alivePacket = new ByteObject();
            alivePacket.WriteInt(ssoSeq);
            alivePacket.WriteInt(bot.agreement_info.appid);
            alivePacket.WriteInt(bot.agreement_info.appid);
            alivePacket.WriteHexStr("01 00 00 00 00 00 00 00 00 00 01 00"); // 固定
            alivePacket.WriteBytesAndLen(new byte[0], 4); // TGT
            alivePacket.WriteStringAndLen(serviceCmd, 4);
            alivePacket.WriteBytesAndLen(bot.agreement_info.outPacketSessionId, 4);
            alivePacket.WriteBytesAndLen(AndroidInfo.imei, 4);
            alivePacket.WriteBytesAndLen(bot.sigInfo.in_ksid, 4);
            alivePacket.WriteBytesAndShortLen(bot.agreement_info.agreement_ver.getBytes(), 2);
            alivePacket.WriteInt(4);
            alivePacket.WriteInt(4);
            alivePacket.putStartSize(0);
            request_head.WriteBytes(alivePacket.toByteArray()).putStartSize(4);
            // System.out.println(HexUtil.bytesToHexString(request_head.toByteArray()));
            return request_head.toByteArray();
        }else{
            request_head.WriteStringAndLen(String.valueOf(bot.getUin()), 4);
        }
        ByteObject cmd_head = new ByteObject();
        if(serviceCmd.equals("wtlogin.login") && bot.bot_type == 0){
            cmd_head.WriteInt(ssoSeq);
            cmd_head.WriteInt(bot.agreement_info.appid);
            cmd_head.WriteInt(bot.agreement_info.appid);
            cmd_head.WriteInt(16777216).WriteInt(0).WriteInt(0);
            cmd_head.WriteInt(0 + 4);
            cmd_head.WriteStringAndLen(serviceCmd, 4);
            cmd_head.WriteBytesAndLen(bot.agreement_info.outPacketSessionId, 4);
            cmd_head.WriteBytesAndLen(AndroidInfo.imei, 4);
            cmd_head.WriteInt(0 + 4);
            cmd_head.WriteBytesAndShortLen(bot.agreement_info.agreement_ver.getBytes(), 2);
        }else if(token != null && serviceCmd.equals("StatSvc.register")){
            cmd_head.WriteInt(ssoSeq);
            cmd_head.WriteInt(bot.agreement_info.appid);
            cmd_head.WriteInt(bot.agreement_info.appid);
            cmd_head.WriteHexStr("01 00 00 00 00 00 00 00 00 00 01 00");
            cmd_head.WriteBytesAndLen(bot.sigInfo.TGT.clone(), 4);
            cmd_head.WriteStringAndLen(serviceCmd, 4);
            cmd_head.WriteBytesAndLen(bot.agreement_info.outPacketSessionId, 4);
            cmd_head.WriteBytesAndLen(AndroidInfo.imei, 4);
            cmd_head.WriteBytesAndLen(bot.sigInfo.in_ksid, 4);
            cmd_head.WriteBytesAndShortLen(bot.agreement_info.agreement_ver.getBytes(), 2);
        }else{
            cmd_head.WriteStringAndLen(serviceCmd, 4);
            cmd_head.WriteBytesAndLen(bot.agreement_info.outPacketSessionId, 4);
            cmd_head.WriteInt(4);
        }
        cmd_head.putStartSize(4);
        ByteObject source = new ByteObject(src);
        long len = 0;
        if(source.length() > 0)
             len = source.readInt();
        if(serviceCmd.equals("wtlogin.login")){
            source.clean();
            source.WriteShort(bot.agreement_info.protocolVersion);
            source.WriteShort(0x810);
            source.WriteShort(1);
            source.WriteInt(bot.getUin());
            source.WriteHexStr("03 | 87 | 00 | 00 00 00 02 | 00 00 00 00 | 00 00 00 00");
            source.WriteByte(2); // 固定
            source.WriteByte(1); // 固定
            source.WriteBytes(bot.recorder.randomKey);
            source.WriteShort(305);
            source.WriteShort(bot.recorder.ecdh.get_pub_key_ver());
            source.WriteBytesAndShortLen(bot.recorder.getPubKey());
            source.WriteBytes(src);
            source.WriteByte(3);
            source.putStartSmallSize(3);
            source.ReWriteByte(2);
            source.putStartSize(4);
        }else{
            if(len > 8192 || len != source.length()){
                source.putStartSize(4);
            }
        }
        cmd_head.WriteBytes(source.toByteArray());
        request_head.WriteBytes( cmd_head.encypt(key) );
        request_head.putStartSize(4);
        return request_head.toByteArray();
    }
    
    public static long GetBkn(String skey) {
        return getG_TK(skey);
    }
    
    public static int getG_TK(String str) {
        int i = 5381;
        for (int i2 = 0; i2 < str.length(); i2++) {
            i += (i << 5) + str.charAt(i2);
        }
        return Integer.MAX_VALUE & i;
    }
    
    public static String get_mpasswd() {
        try {
            byte[] seed = SecureRandom.getSeed(16);
            String str = "";
            int i = 0;
            while (true) {
                int i2 = i;
                String str2 = str;
                if (i2 >= seed.length) {
                    return str2;
                }
                str = str2 + String.valueOf((char) ((new Random().nextBoolean() ? 97 : 65) + Math.abs(seed[i2] % 26)));
                i = i2 + 1;
            }
        } catch (Throwable th) {
            return "1234567890123456";
        }
    }
    
    public static long groupCode2GroupUin(long groupcode){
        long calc = groupcode / 1000000L;
        while(true){
            if(calc >= 0 && calc <= 10){
                calc += 202 - 0;
            }else if(calc >= 11 && calc <= 19){
                calc += 480 - 11;
            }else if(calc >= 20 && calc <= 66){
                calc += 2100 - 20;
            }else if(calc >= 67 && calc <= 156){
                calc += 2010 - 67;
            }else if(calc >= 157 && calc <= 209){
                calc += 2147 - 157;
            }else if(calc >= 210 && calc <= 309){
                calc += 4100 - 210;
            }else if(calc >= 310 && calc <= 499){
                calc += 3800 - 310;
            }else{
                break;
            }
        }
        return calc * 1000000L + groupcode % 1000000L; 
    }

    public static long groupUin2GroupCode(long groupuin){
        long calc = groupuin / 1000000L;
        while(true){
            if(calc >= 0 + 202 && calc + 202 <= 10){
                calc -= 202 - 0;
            }else if(calc >= 11 + 480 && calc <= 19 + 480){
                calc -= 480 - 11;
            }else if(calc >= 20 + 2100 && calc <= 66 + 2100){
                calc -= 2100 - 20;
            }else if(calc >= 67 + 2010 && calc <= 156 + 2010){
                calc -= 2010 - 67;
            }else if(calc >= 157 + 2147 && calc <= 209 + 2147){
                calc -= 2147 - 157;
            }else if(calc >= 210 + 4100 && calc <= 309 + 4100){
                calc -= 4100 - 210;
            }else if(calc >= 310 + 3800 && calc <= 499 + 3800){
                calc -= 3800 - 310;
            }else{
                break;
            }
        }
        return calc * 1000000L + groupuin % 1000000L; 
    }
}
