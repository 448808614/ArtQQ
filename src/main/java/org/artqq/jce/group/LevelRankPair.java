package org.artqq.jce.group;

import com.qq.taf.jce.JceInputStream;
import com.qq.taf.jce.JceOutputStream;
import com.qq.taf.jce.JceStruct;

public class LevelRankPair extends JceStruct {
    public long dwLevel;
    public String strRank = "";
    
    @Override
    public void readFrom(JceInputStream jceInputStream) {
        this.dwLevel = jceInputStream.read(this.dwLevel, 0, false);
        this.strRank = jceInputStream.readString(1, false);
    }
}

