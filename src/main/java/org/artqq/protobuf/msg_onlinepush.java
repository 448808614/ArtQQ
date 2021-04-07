package org.artqq.protobuf;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import java.io.IOException;

public class msg_onlinepush {
    @Protobuf(fieldType = FieldType.OBJECT, order = 1)
    public msg_comm.Msg msg;

    @Protobuf(fieldType = FieldType.INT64, order = 2)
    public long svrip;

    @Protobuf(fieldType = FieldType.BYTES, order = 3)
    public byte[] push_token;

    @Protobuf(fieldType = FieldType.INT64, order = 4)
    public long ping_flag;

    @Protobuf(fieldType = FieldType.INT64, order = 5)
    public long general_flag;

    @Protobuf(fieldType = FieldType.INT64, order = 6)
    public long bind_uin;
    
    public static msg_onlinepush decode(byte[] src){
        try {
            return ProtobufProxy.create(msg_onlinepush.class).decode(src);
        } catch (IOException e) {}
        return null;
    }
}

