package org.artqq.Wtlogin;

import org.artqq.utils.QQAgreementUtils;
import org.artqq.utils.bytes.ByteObject;
import org.artqq.ArtBot;

public class VerifyCode {
    /**
     * 与腾讯服务器提交获取设备锁验证码请求
     *
     * @param bot Main
     * @param dt104 t104
     * @param dt174 t174
     * @return 包体
     */
    public static byte[] getVerifyCode(ArtBot bot, byte[] dt104, byte[] dt174){
        _Tlv t = _Tlv.getTlv(bot);
        int seq_ = bot.recorder.nextSsoSeq();
        byte[] t8 = t.get_tlv_8();
        byte[] t104 = t.get_tlv_104(dt104);
        byte[] t116 = t.get_tlv_116();
        byte[] t174 = t.get_tlv_174(dt174);
        byte[] t17a = t.get_tlv_17a(9);
        byte[] t197 = t.get_tlv_197();
        byte[] t542 = t.get_tlv_542();
        ByteObject bo = new ByteObject();
        bo.WriteShort(8);
        bo.WriteShort(7);
        bo.WriteBytes(t8);
        bo.WriteBytes(t104);
        bo.WriteBytes(t116);
        bo.WriteBytes(t174);
        bo.WriteBytes(t17a);
        bo.WriteBytes(t197);
        bo.WriteBytes(t542);
        return QQAgreementUtils.encodeRequest(bot, seq_, "wtlogin.login", bo.encypt(bot.recorder.getShareKey()));
    }

    /**
     * 提交验证码
     */
    public static byte[] sendVerifyCode(ArtBot bot, String code, byte[] dt104, byte[] dt174, byte[] dt402){
        _Tlv t = _Tlv.getTlv(bot);
        int seq_ = bot.recorder.nextSsoSeq();
        byte[] t8 = t.get_tlv_8();
        byte[] t104 = t.get_tlv_104(dt104);
        byte[] t116 = t.get_tlv_116();
        byte[] t174 = t.get_tlv_174(dt174);
        byte[] t17c = t.get_tlv_17c(code);
        byte[] t401 = t.get_tlv_401(dt402);
        byte[] t198 = t.get_tlv_198();
        byte[] t542 = t.get_tlv_542();
        ByteObject bo = new ByteObject();
        bo.WriteShort(7);
        bo.WriteShort(8);
        bo.WriteBytes(t8);
        bo.WriteBytes(t104);
        bo.WriteBytes(t116);
        bo.WriteBytes(t174);
        bo.WriteBytes(t17c);
        bo.WriteBytes(t401);
        bo.WriteBytes(t198);
        bo.WriteBytes(t542);
        return QQAgreementUtils.encodeRequest(bot, seq_, "wtlogin.login", bo.encypt(bot.recorder.getShareKey()));
    }

    /**
     * 通过设备锁
     */
    public static byte[] passVerify(ArtBot bot, byte[] dt104, byte[] dt402, byte[] dt403){
        _Tlv t = _Tlv.getTlv(bot);
        int seq_ = bot.recorder.nextSsoSeq();
        byte[] t8 = t.get_tlv_8();
        byte[] t104 = t.get_tlv_104(dt104);
        byte[] t116 = t.get_tlv_116();
        byte[] t401 = t.get_tlv_401(dt402);
        byte[] t402 = t.get_tlv_402(dt402);
        byte[] t403 = t.get_tlv_403(dt403);       
        ByteObject bo = new ByteObject();
        bo.WriteShort(20);
        bo.WriteShort(6);
        bo.WriteBytes(t8);
        bo.WriteBytes(t104);
        bo.WriteBytes(t116);
        bo.WriteBytes(t401);
        bo.WriteBytes(t402);
        bo.WriteBytes(t403);     
        return QQAgreementUtils.encodeRequest(bot, seq_, "wtlogin.login", bo.encypt(bot.recorder.getShareKey()));
    }
}
