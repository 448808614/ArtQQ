package org.artqq.jce.group;

import com.qq.taf.jce.JceInputStream;
import com.qq.taf.jce.JceOutputStream;
import com.qq.taf.jce.JceStruct;

public class TroopInfoV2 extends JceStruct {
    public byte cGroupOption;
    public long dwCertificationType;
    public long dwGroupClassExt;
    public long dwGroupCode;
    public long dwGroupFlagExt;
    public long dwGroupOwnerUin;
    public long dwGroupUin;
    public int memberNum;
    public String strFingerMemo = "";
    public String strGroupMemo = "";
    public String strGroupName = "";
    public int wGroupFace;
    
    @Override
    public void writeTo(JceOutputStream jceOutputStream) {
        jceOutputStream.write(this.dwGroupUin, 0);
        jceOutputStream.write(this.dwGroupCode, 1);
        jceOutputStream.write(this.strGroupName, 2);
        jceOutputStream.write(this.strGroupMemo, 3);
        jceOutputStream.write(this.dwGroupOwnerUin, 4);
        jceOutputStream.write(this.dwGroupClassExt, 5);
        jceOutputStream.write(this.wGroupFace, 6);
        if (this.strFingerMemo != null) {
            jceOutputStream.write(this.strFingerMemo, 7);
        }
        jceOutputStream.write(this.cGroupOption, 8);
        jceOutputStream.write(this.memberNum, 9);
        jceOutputStream.write(this.dwGroupFlagExt, 10);
        jceOutputStream.write(this.dwCertificationType, 11);
    }

    @Override
    public void readFrom(JceInputStream jceInputStream) {
        this.dwGroupUin = jceInputStream.read(this.dwGroupUin, 0, true);
        this.dwGroupCode = jceInputStream.read(this.dwGroupCode, 1, true);
        this.strGroupName = jceInputStream.readString(2, true);
        this.strGroupMemo = jceInputStream.readString(3, true);
        this.dwGroupOwnerUin = jceInputStream.read(this.dwGroupOwnerUin, 4, false);
        this.dwGroupClassExt = jceInputStream.read(this.dwGroupClassExt, 5, false);
        this.wGroupFace = jceInputStream.read(this.wGroupFace, 6, false);
        this.strFingerMemo = jceInputStream.readString(7, false);
        this.cGroupOption = jceInputStream.read(this.cGroupOption, 8, false);
        this.memberNum = jceInputStream.read(this.memberNum, 9, false);
        this.dwGroupFlagExt = jceInputStream.read(this.dwGroupFlagExt, 10, false);
        this.dwCertificationType = jceInputStream.read(this.dwCertificationType, 11, false);
    }
}
