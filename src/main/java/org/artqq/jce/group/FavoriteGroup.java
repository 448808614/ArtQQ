package org.artqq.jce.group;

import com.qq.taf.jce.JceInputStream;
import com.qq.taf.jce.JceOutputStream;
import com.qq.taf.jce.JceStruct;

public class FavoriteGroup extends JceStruct {
    public long dwGroupCode;
    public long dwOpenTimestamp;
    public long dwSnsFlag = 1;
    public long dwTimestamp;

    @Override
    public void writeTo(JceOutputStream jceOutputStream) {
        jceOutputStream.write(this.dwGroupCode, 0);
        jceOutputStream.write(this.dwTimestamp, 1);
        jceOutputStream.write(this.dwSnsFlag, 2);
        jceOutputStream.write(this.dwOpenTimestamp, 3);
    }

    @Override
    public void readFrom(JceInputStream jceInputStream) {
        this.dwGroupCode = jceInputStream.read(this.dwGroupCode, 0, true);
        this.dwTimestamp = jceInputStream.read(this.dwTimestamp, 1, false);
        this.dwSnsFlag = jceInputStream.read(this.dwSnsFlag, 2, false);
        this.dwOpenTimestamp = jceInputStream.read(this.dwOpenTimestamp, 3, false);
    }
}

