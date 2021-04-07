package org.artqq.protobuf.highway;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import org.artqq.oidb.oidb_0x388;

import java.io.IOException;

public class HwPacket {
    public HwPacket(){}

    public HwPacket(DataHighwayHead dataHead, SigHead seg){
        this.dataHead = dataHead;
        sigHead = seg;
    }

    @Protobuf(fieldType = FieldType.OBJECT, order = 1)
    public DataHighwayHead dataHead;

    @Protobuf(fieldType = FieldType.OBJECT, order = 2)
    public SigHead sigHead;

    @Protobuf(fieldType = FieldType.OBJECT, order = 3)
    public ExtendInfo extendinfo;

    @Protobuf(fieldType = FieldType.INT64, order = 4)
    public long timestamp;

    @Protobuf(fieldType = FieldType.OBJECT, order = 5)
    public LoginSigHead msgLoginSigHead;

    public byte[] toByteArray(){
        try {
            return ProtobufProxy.create(HwPacket.class).encode(this);
        } catch (IOException e) {
        }
        return null;
    }

    public static class ExtendInfo {
        @Protobuf(fieldType = FieldType.INT32, order = 1)
        public int sub_cmd_id = 3;

        @Protobuf(fieldType = FieldType.INT32, order = 2)
        public int session_id = 3;

        @Protobuf(fieldType = FieldType.OBJECT, order = 5)
        public oidb_0x388.TryUpPttReq msg_ptt_Info;
    }
}

