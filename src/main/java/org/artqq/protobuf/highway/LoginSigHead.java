package org.artqq.protobuf.highway;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.FieldType;

public class LoginSigHead {
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    public Long loginsigType = null;

    @Protobuf(fieldType = FieldType.BYTES, order = 2)
    public byte[] loginsig;

}

