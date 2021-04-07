package org.artqq.protobuf.highway;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import java.io.IOException;

public class LongMsg {
    public static class RspBody {
        @Protobuf(fieldType = FieldType.UINT32, order = 1)
        public int subcmd;

        @Protobuf(fieldType = FieldType.OBJECT, order = 2)
        public MsgUpRsp msg_up_rsp;

        // 尚未补全
    }

    public static class MsgUpRsp {
        @Protobuf(fieldType = FieldType.UINT32, order = 1)
        public int result;

        @Protobuf(fieldType = FieldType.UINT64, order = 2)
        public long msg_id;

        @Protobuf(fieldType = FieldType.STRING, order = 3)
        public String msg_resid;
    }

    public static class ReqBody {
        @Protobuf(fieldType = FieldType.UINT32, order = 1)
        public int subcmd = 1;

        @Protobuf(fieldType = FieldType.UINT32, order = 2)
        public int term_type = 5;

        @Protobuf(fieldType = FieldType.UINT32, order = 3)
        public int platform_type = 9;

        @Protobuf(fieldType = FieldType.OBJECT, order = 4)
        public MsgUpReq msg_up_req;

        @Protobuf(fieldType = FieldType.OBJECT, order = 5)
        public MsgDownReq msg_down_req;

        @Protobuf(fieldType = FieldType.OBJECT, order = 6)
        public MsgDeleteReq msg_del_req;

        @Protobuf(fieldType = FieldType.UINT32, order = 10)
        public int agent_type;

        @Protobuf(fieldType = FieldType.UINT32, order = 11)
        public int busi_type;

        public byte[] toByteArray(){
            try {
                return ProtobufProxy.create(ReqBody.class).encode(this);
            } catch (IOException e) {
                return null;
            }
        }
    }

    public static class MsgDeleteReq {

    }

    public static class MsgDownReq {

    }

    public static class MsgUpReq {
        @Protobuf(fieldType = FieldType.UINT32, order = 1)
        public int msg_type = 3;

        @Protobuf(fieldType = FieldType.UINT64, order = 2)
        public long dst_uin;

        @Protobuf(fieldType = FieldType.UINT32, order = 3)
        public int msg_id;

        @Protobuf(fieldType = FieldType.BYTES, order = 4)
        public byte[] msg_content;
        // gzip压缩消息

        @Protobuf(fieldType = FieldType.UINT32, order = 5)
        public int store_type = 2;

        @Protobuf(fieldType = FieldType.BYTES, order = 6)
        public byte[] msg_ukey;

        @Protobuf(fieldType = FieldType.UINT32, order = 7)
        public int need_cache ;

        public byte[] toByteArray(){
            try {
                return ProtobufProxy.create(MsgUpReq.class).encode(this);
            } catch (IOException e) {
                return null;
            }
        }

    }
}

