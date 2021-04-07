package org.artqq.protobuf.highway;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import java.io.IOException;

public class DataHighwayHead {
    @Protobuf(fieldType = FieldType.UINT32, order = 1)
    public int version = 1;

    @Protobuf(fieldType = FieldType.STRING, order = 2, required = true)
    public String uin;

    @Protobuf(fieldType = FieldType.STRING, order = 3, required = true)
    public String command;

    @Protobuf(fieldType = FieldType.UINT32, order = 4)
    public int seq;

    @Protobuf(fieldType = FieldType.UINT32, order = 5)
    public int retryTimes = 0;

    @Protobuf(fieldType = FieldType.UINT32, order = 6)
    public int appid;

    @Protobuf(fieldType = FieldType.UINT32, order = 7)
    public int dataflag;

    @Protobuf(fieldType = FieldType.UINT32, order = 8)
    public int commandId = 0;

    @Protobuf(fieldType = FieldType.STRING, order = 9)
    public String buildVer;

    @Protobuf(fieldType = FieldType.UINT32, order = 10)
    public int localeId = 2052;

    @Protobuf(fieldType = FieldType.UINT32, order = 11)
    public int env_id = 2052;

    public byte[] toByteArray(){
        try {
            return ProtobufProxy.create(DataHighwayHead.class).encode(this);
        } catch (IOException e) {
        }
        return null;
    }
}

