package org.artqq.jce.group;

import com.qq.taf.jce.JceInputStream;
import com.qq.taf.jce.JceOutputStream;
import com.qq.taf.jce.JceStruct;

public class TroopNumSimplify extends JceStruct {
    public long GroupCode;
    public long dwGroupFlagExt;
    public long dwGroupInfoSeq;
    public long dwGroupRankSeq;
    
    @Override
    public void writeTo(JceOutputStream jceOutputStream) {
        jceOutputStream.write(this.GroupCode, 0);
        jceOutputStream.write(this.dwGroupInfoSeq, 1);
        jceOutputStream.write(this.dwGroupFlagExt, 2);
        jceOutputStream.write(this.dwGroupRankSeq, 3);
    }

    @Override
    public void readFrom(JceInputStream jceInputStream) {
        this.GroupCode = jceInputStream.read(this.GroupCode, 0, true);
        this.dwGroupInfoSeq = jceInputStream.read(this.dwGroupInfoSeq, 1, false);
        this.dwGroupFlagExt = jceInputStream.read(this.dwGroupFlagExt, 2, false);
        this.dwGroupRankSeq = jceInputStream.read(this.dwGroupRankSeq, 3, false);
    }
}
