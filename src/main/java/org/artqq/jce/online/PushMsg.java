package org.artqq.jce.online;

import com.qq.taf.jce.JceInputStream;
import com.qq.taf.jce.JceOutputStream;
import com.qq.taf.jce.JceStruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class PushMsg extends JceStruct {
    static Map<String, byte[]> cache_mPreviews = new HashMap();
    static ArrayList<MsgInfo> cache_vMsgInfos = new ArrayList<>();
    static byte[] cache_vSyncCookie = new byte[1];
    public long lUin;
    public Map<String, byte[]> mPreviews;
    public int svrip;
    public long uMsgTime;
    public ArrayList<MsgInfo> vMsgInfos;
    public byte[] vSyncCookie;
    public int wGeneralFlag;
    public int wUserActive;

    static {
        cache_vMsgInfos.add(new MsgInfo());
        cache_vSyncCookie[0] = 0;
        byte[] bArr = new byte[1];
        bArr[0] = 0;
        cache_mPreviews.put("", bArr);
    }

    @Override
    public void readFrom(JceInputStream jceInputStream) {
        this.lUin = jceInputStream.read(this.lUin, 0, true);
        this.uMsgTime = jceInputStream.read(this.uMsgTime, 1, true);
        this.vMsgInfos = (ArrayList) jceInputStream.readV2(cache_vMsgInfos, 2, true);
        this.svrip = jceInputStream.read(this.svrip, 3, true);
        this.vSyncCookie = jceInputStream.read(cache_vSyncCookie, 4, false);
        this.mPreviews = (Map) jceInputStream.readV2(cache_mPreviews, 6, false);
        this.wUserActive = jceInputStream.read(this.wUserActive, 7, false);
        this.wGeneralFlag = jceInputStream.read(this.wGeneralFlag, 12, false);
    }
}
