package com.qq.taf.jce;

import java.io.Serializable;
import org.artqq.utils.BytesUtil;

public abstract class JceStruct implements Serializable {
    public static final byte BYTE = 0;
    public static final byte SHORT = 1;
    public static final byte INT = 2;
    public static final byte LONG = 3;
    public static final byte FLOAT = 4;
    public static final byte DOUBLE = 5;
    public static final byte STRING1 = 6;
    public static final byte STRING4 = 7;
    public static final byte MAP = 8;
    public static final byte LIST = 9;
    public static final byte STRUCT_BEGIN = 10;
    public static final byte STRUCT_END = 11;
    public static final byte ZERO_TAG = 12;
    public static final byte SIMPLE_LIST = 13;
    public static final int JCE_MAX_STRING_LENGTH = 104857600;

    public void writeTo(JceOutputStream paramJceOutputStream){};

    public void readFrom(JceInputStream paramJceInputStream){};

    public JceStruct newInit(){
        return null;
    }

    public JceStruct read(byte[] b){
        readFrom(new JceInputStream(b));
        return this;
    }

    public JceStruct readJce(byte[] res){
        res = BytesUtil.subByte(res, 0, res.length-1);
        res = BytesUtil.subByte(res, 1, res.length-1);
        readFrom(new JceInputStream(res));
        return this;
    }

    public byte[] toByteArray() {
        JceOutputStream os = new JceOutputStream();
        writeTo(os);
        return os.toByteArray();
    }

    public byte[] toJceStructByteArray() {
        JceOutputStream os = new JceOutputStream();
        writeTo(os);
        byte[] n = os.toByteArray();
        n = BytesUtil.byteMerger(new byte[]{0x0a}, n);
        n = BytesUtil.byteMerger(n, new byte[]{0x0b});
        return n;
    }
}
