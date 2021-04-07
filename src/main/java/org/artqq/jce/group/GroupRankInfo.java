package org.artqq.jce.group;

import com.qq.taf.jce.JceInputStream;
import com.qq.taf.jce.JceOutputStream;
import com.qq.taf.jce.JceStruct;
import java.util.ArrayList;

public class GroupRankInfo extends JceStruct {
    static ArrayList<LevelRankPair> cache_LevelRankPair = new ArrayList<>();
    public byte cGroupRankSysFlag;
    public byte cGroupRankUserFlag;
    public byte cGroupRankUserFlagNew;
    public long dwGroupCode;
    public long dwGroupRankSeq;
    public long dwOfficeMode;
    public String strAdminName = "";
    public String strOwnerName = "";
    public ArrayList<LevelRankPair> vecRankMap;
    public ArrayList<LevelRankPair> vecRankMapNew;

    @Override
    public void writeTo(JceOutputStream jceOutputStream) {
        jceOutputStream.write(this.dwGroupCode, 0);
        jceOutputStream.write(this.cGroupRankSysFlag, 1);
        jceOutputStream.write(this.cGroupRankUserFlag, 2);
        if (this.vecRankMap != null) {
            jceOutputStream.write(this.vecRankMap, 3);
        }
        jceOutputStream.write(this.dwGroupRankSeq, 4);
        if (this.strOwnerName != null) {
            jceOutputStream.write(this.strOwnerName, 5);
        }
        if (this.strAdminName != null) {
            jceOutputStream.write(this.strAdminName, 6);
        }
        jceOutputStream.write(this.dwOfficeMode, 7);
        jceOutputStream.write(this.cGroupRankUserFlagNew, 8);
        if (this.vecRankMapNew != null) {
            jceOutputStream.write(this.vecRankMapNew, 9);
        }
    }

    static {
        cache_LevelRankPair.add(new LevelRankPair());
    }

    @Override
    public void readFrom(JceInputStream jceInputStream) {
        this.dwGroupCode = jceInputStream.read(this.dwGroupCode, 0, true);
        this.cGroupRankSysFlag = jceInputStream.read(this.cGroupRankSysFlag, 1, false);
        this.cGroupRankUserFlag = jceInputStream.read(this.cGroupRankUserFlag, 2, false);
        this.vecRankMap = (ArrayList) jceInputStream.readV2(cache_LevelRankPair, 3, false);
        this.dwGroupRankSeq = jceInputStream.read(this.dwGroupRankSeq, 4, false);
        this.strOwnerName = jceInputStream.readString(5, false);
        this.strAdminName = jceInputStream.readString(6, false);
        this.dwOfficeMode = jceInputStream.read(this.dwOfficeMode, 7, false);
        this.cGroupRankUserFlagNew = jceInputStream.read(this.cGroupRankUserFlagNew, 8, false);
        this.vecRankMapNew = (ArrayList) jceInputStream.readV2(cache_LevelRankPair, 9, false);
    }
}

