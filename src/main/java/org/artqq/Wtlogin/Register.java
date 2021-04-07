package org.artqq.Wtlogin;

import com.qq.jce.wup.UniPacket;
import com.qq.taf.jce.JceInputStream;
import com.qq.taf.jce.JceOutputStream;
import com.qq.taf.jce.JceStruct;
import java.util.ArrayList;
import org.artqq.ArtBot;
import org.artqq.android.AndroidInfo;
import org.artqq.utils.HexUtil;
import org.artqq.utils.QQAgreementUtils;

public class Register extends JceStruct {
    public static class Req extends JceStruct {
        public byte bIsOnline = 0;
        public byte bIsSetStatus = 0;
        public byte bIsShowOnline = 0;
        public byte bKikPC = 0;
        public byte bKikWeak = 0;
        public byte bOnlinePush = 0;
        public byte bOpenPush = 1;
        public byte bRegType = 1;
        public byte bSetMute = 0;
        public byte bSlientPush = 0;
        public byte[] _0x769_reqbody = null;
        public byte cConnType = 0;
        public byte cNetType = 0;
        public int iBatteryStatus = 0;
        public long iLargeSeq = 0;
        public long iLastWatchStartTime = 0;
        public int iLocaleID = 2052;
        public long iOSVersion = 0;
        public int iStatus = 11;
        // 11上线===21离线===31离开===41隐身===51忙碌
        public long lBid = 1 | 2 | 4;
        public long lCpId = 0;
        public long lUin = 0;
        public String sBuildVer = "";
        public String sChannelNo = "";
        public String sOther = "";
        public String strDevName = new String(AndroidInfo.machine_name);
        public String strDevType = new String(AndroidInfo.machine_manufacturer);
        public String strIOSIdfa = "";
        public String strOSVer = AndroidInfo.machine_version;
        public String strVendorName = "[u]" + new String(AndroidInfo.machine_name);
        public String strVendorOSName = "?LMY48G test-keys;ao";
        public long timeStamp = 189;
        public long uExtOnlineStatus = 0;
        public long uNewSSOIp = 0;
        public long uOldSSOIp = 0;
        public ArrayList vecBindUin = null;
        public byte[] vecDevParam = null;
        public byte[] vecGuid = null;
        public byte[] vecServerBuf = null;

        @Override
        public void writeTo(JceOutputStream jceOutputStream) {
            jceOutputStream.write(this.lUin, 0);
            jceOutputStream.write(this.lBid, 1);
            jceOutputStream.write(this.cConnType, 2);
            jceOutputStream.write(this.sOther, 3);
            jceOutputStream.write(this.iStatus, 4);
            jceOutputStream.write(this.bOnlinePush, 5);
            jceOutputStream.write(this.bIsOnline, 6);
            jceOutputStream.write(this.bIsShowOnline, 7);
            jceOutputStream.write(this.bKikPC, 8);
            jceOutputStream.write(this.bKikWeak, 9);
            jceOutputStream.write(this.timeStamp, 10);
            jceOutputStream.write(this.iOSVersion, 11);
            jceOutputStream.write(this.cNetType, 12);
            if (this.sBuildVer != null) {
                jceOutputStream.write(this.sBuildVer, 13);
            }
            jceOutputStream.write(this.bRegType, 14);
            if (this.vecDevParam != null) {
                jceOutputStream.write(this.vecDevParam, 15);
            }
            if (this.vecGuid != null) {
                jceOutputStream.write(this.vecGuid, 16);
            }
            jceOutputStream.write(this.iLocaleID, 17);
            jceOutputStream.write(this.bSlientPush, 18);
            if (this.strDevName != null) {
                jceOutputStream.write(this.strDevName, 19);
            }
            if (this.strDevType != null) {
                jceOutputStream.write(this.strDevType, 20);
            }
            if (this.strOSVer != null) {
                jceOutputStream.write(this.strOSVer, 21);
            }
            jceOutputStream.write(this.bOpenPush, 22);
            jceOutputStream.write(this.iLargeSeq, 23);
            jceOutputStream.write(this.iLastWatchStartTime, 24);
            if (this.vecBindUin != null) {
                jceOutputStream.write(this.vecBindUin, 25);
            }
            jceOutputStream.write(this.uOldSSOIp, 26);
            jceOutputStream.write(this.uNewSSOIp, 27);
            if (this.sChannelNo != null) {
                jceOutputStream.write(this.sChannelNo, 28);
            }
            jceOutputStream.write(this.lCpId, 29);
            if (this.strVendorName != null) {
                jceOutputStream.write(this.strVendorName, 30);
            }
            if (this.strVendorOSName != null) {
                jceOutputStream.write(this.strVendorOSName, 31);
            }
            if (this.strIOSIdfa != null) {
                jceOutputStream.write(this.strIOSIdfa, 32);
            }
            if (this._0x769_reqbody != null) {
                jceOutputStream.write(this._0x769_reqbody, 33);
            }
            jceOutputStream.write(this.bIsSetStatus, 34);
            if (this.vecServerBuf != null) {
                jceOutputStream.write(this.vecServerBuf, 35);
            }
            jceOutputStream.write(this.bSetMute, 36);
            jceOutputStream.write(this.uExtOnlineStatus, 38);
            jceOutputStream.write(this.iBatteryStatus, 39);
        }
    }
    
    public static class Resp extends JceStruct {
        static byte[] cache_0x769_rspbody = new byte[1];
        public byte bCrashFlag = 0;
        public byte bLargeSeqUpdate = 0;
        public byte bLogQQ = 0;
        public byte bNeedKik = 0;
        public byte bUpdateFlag = 0;
        public byte[] _0x769_rspbody = null;
        public Byte cReplyCode = 0;
        public int iClientPort = 0;
        public int iHelloInterval = 300;
        public long iLargeSeq = 0;
        public int iStatus = 0;
        public long lBid = 0;
        public long lServerTime = 0;
        public long lUin = 0;
        public String strClientIP = "";
        public String strResult = "";
        public long timeStamp = 0;
        public long uClientAutoStatusInterval = 600;
        public long uClientBatteryGetInterval = 86400;
        public long uExtOnlineStatus = 0;

        @Override
        public void readFrom(JceInputStream jceInputStream) {
            this.lUin = jceInputStream.read(this.lUin, 0, true);
            this.lBid = jceInputStream.read(this.lBid, 1, true);
            this.cReplyCode = jceInputStream.read(this.cReplyCode, 2, true);
            this.strResult = jceInputStream.readString(3, true);
            this.lServerTime = jceInputStream.read(this.lServerTime, 4, false);
            this.bLogQQ = jceInputStream.read(this.bLogQQ, 5, false);
            this.bNeedKik = jceInputStream.read(this.bNeedKik, 6, false);
            this.bUpdateFlag = jceInputStream.read(this.bUpdateFlag, 7, false);
            this.timeStamp = jceInputStream.read(this.timeStamp, 8, false);
            this.bCrashFlag = jceInputStream.read(this.bCrashFlag, 9, false);
            this.strClientIP = jceInputStream.readString(10, false);
            this.iClientPort = jceInputStream.read(this.iClientPort, 11, false);
            this.iHelloInterval = jceInputStream.read(this.iHelloInterval, 12, false);
            this.iLargeSeq = jceInputStream.read(this.iLargeSeq, 13, false);
            this.bLargeSeqUpdate = jceInputStream.read(this.bLargeSeqUpdate, 14, false);
            this._0x769_rspbody = jceInputStream.read(cache_0x769_rspbody, 15, false);
            this.iStatus = jceInputStream.read(this.iStatus, 16, false);
            this.uExtOnlineStatus = jceInputStream.read(this.uExtOnlineStatus, 17, false);
            this.uClientBatteryGetInterval = jceInputStream.read(this.uClientBatteryGetInterval, 18, false);
            this.uClientAutoStatusInterval = jceInputStream.read(this.uClientAutoStatusInterval, 19, false);
        }
    }
    
    public static byte[] getReq(ArtBot bot){
        int ssoSeq = bot.recorder.nextSsoSeq();
        Req req = new Req();
        req.iStatus = bot.getOnlineStatus();
        req.lUin = bot.getUin();
        req.vecGuid = AndroidInfo.getGuid();
        req.iLargeSeq = bot.recorder.nextLardgeSeq();    
        req._0x769_reqbody = HexUtil.hexStringToByte("0A 04 08 2E 10 00 0A 05 08 9B 02 10 00 ");
        byte[] source = QQAgreementUtils.encodePacket(req, "PushService", "SvcReqRegister", "SvcReqRegister",0);
        if(source == null || source.length <= 0){
            throw new RuntimeException("无法完成上线操作…");
        }
        return QQAgreementUtils.encodeRequest(bot, ssoSeq, "StatSvc.register", source, bot.sigInfo.D2.clone(), bot.sigInfo.D2Key.clone());
    }
    
    public static Resp getResp(byte[] src){
        return QQAgreementUtils.decodePacket(src, "SvcRespRegister", new Resp());
    }
}


class BindUin extends JceStruct {
    static byte[] cache_Skey = new byte[1];
    public long lUin = 0;
    public byte[] sKey = null;
    public int iStatus = 11;

    public BindUin() {
    }

    @Override
    public void writeTo(JceOutputStream jceOutputStream) {
        jceOutputStream.write(this.lUin, 0);
        jceOutputStream.write(this.sKey, 1);
        jceOutputStream.write(this.iStatus, 2);
    }
}
