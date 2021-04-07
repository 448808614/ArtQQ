package org.artqq.protobuf;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import java.io.IOException;

public class MsgWithDraw {
    public static class Req {
        @Protobuf(fieldType = FieldType.OBJECT, order = 1)
        public C2CMsgWithDraw.Req c2c_with_draw;
        
        @Protobuf(fieldType = FieldType.OBJECT, order = 2)
        public GroupMsgWithDraw.Req group_with_draw;
        
        public byte[] toByteArray(){
            try {
                return ProtobufProxy.create(Req.class).encode(this);
            } catch (IOException e) {
                return new byte[0];
            }
        }
    }
    
    public static class Resp {
        @Protobuf(fieldType = FieldType.OBJECT, order = 1)
        public C2CMsgWithDraw.Resp c2c_with_draw;

        @Protobuf(fieldType = FieldType.OBJECT, order = 2)
        public GroupMsgWithDraw.Resp group_with_draw;
        
        public static Resp decode(byte[] src){
            try {
                return ProtobufProxy.create(Resp.class).decode(src);
            } catch (IOException e) {
                return null;
            }
        }
    }
    
    
    public static class GroupMsgWithDraw {
        public static class Req {
            @Protobuf(fieldType = FieldType.INT64, order = 1)
            public long sub_cmd;

            @Protobuf(fieldType = FieldType.INT64, order = 2)
            public long group_type;

            @Protobuf(fieldType = FieldType.INT64, order = 3)
            public long group_code;

            @Protobuf(fieldType = FieldType.OBJECT, order = 4)
            public MessageInfo msg_list;

            @Protobuf(fieldType = FieldType.BYTES, order = 5)
            public byte[] user_def;
        }
        
        public static class Resp {
            @Protobuf(fieldType = FieldType.INT64, order = 1)
            public long result;

            @Protobuf(fieldType = FieldType.STRING, order = 2)
            public String errmsg;
            
            @Protobuf(fieldType = FieldType.INT64, order = 3)
            public long sub_cmd;
            
            @Protobuf(fieldType = FieldType.INT64, order = 4)
            public long group_type;

            @Protobuf(fieldType = FieldType.INT64, order = 5)
            public long group_code;
        }
    }
    
    public static class C2CMsgWithDraw {
        public static class Req {
            @Protobuf(fieldType = FieldType.OBJECT, order = 1)
            public MsgInfo msg_info;

            @Protobuf(fieldType = FieldType.INT64, order = 2)
            public long long_message_flag;

            @Protobuf(fieldType = FieldType.BYTES, order = 3)
            public byte[] reserved;

            @Protobuf(fieldType = FieldType.INT64, order = 4)
            public long sub_cmd;
        }
        
        public static class Resp {
            @Protobuf(fieldType = FieldType.INT64, order = 1)
            public long result;
            
            @Protobuf(fieldType = FieldType.STRING, order = 2)
            public String errmsg;
            
            @Protobuf(fieldType = FieldType.INT64, order = 3)
            public long msg_status;
            
            @Protobuf(fieldType = FieldType.INT64, order = 4)
            public long sub_cmd;
        }
    }
  
    public static class MsgInfo {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public long from_uin;

        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public long to_uin;

        @Protobuf(fieldType = FieldType.INT64, order = 3)
        public long msg_seq;

        @Protobuf(fieldType = FieldType.INT64, order = 4)
        public long msg_uid;

        @Protobuf(fieldType = FieldType.INT64, order = 5)
        public long msg_time;

        @Protobuf(fieldType = FieldType.INT64, order = 6)
        public long msg_random;

        @Protobuf(fieldType = FieldType.INT64, order = 7)
        public long pkg_num;

        @Protobuf(fieldType = FieldType.INT64, order = 8)
        public long pkg_index;

        @Protobuf(fieldType = FieldType.INT64, order = 9)
        public long div_seq;

        @Protobuf(fieldType = FieldType.INT64, order = 10)
        public long msg_type;

        @Protobuf(fieldType = FieldType.OBJECT, order = 11)
        public MessageSvc.RoutingHead routing_head;
    }
    
    public static class MessageInfo {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public long msg_seq;
        
        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public long msg_random;
        
        @Protobuf(fieldType = FieldType.INT64, order = 3)
        public long msg_type;
        
        @Protobuf(fieldType = FieldType.INT64, order = 3)
        public long resv_flag;
    }
}






