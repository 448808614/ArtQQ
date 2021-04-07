package org.artqq.jce.summary;

import com.qq.taf.jce.JceInputStream;
import com.qq.taf.jce.JceOutputStream;
import com.qq.taf.jce.JceStruct;

public class ReqHead extends JceStruct {
    public int iVersion = 1;

    public ReqHead() {
    }

    public ReqHead(int i) {
        this.iVersion = i;
    }

    @Override
    public void writeTo(JceOutputStream jceOutputStream) {
        jceOutputStream.write(this.iVersion, 0);
    }

    @Override
    public void readFrom(JceInputStream jceInputStream) {
        this.iVersion = jceInputStream.read(this.iVersion, 0, true);
    }
}
