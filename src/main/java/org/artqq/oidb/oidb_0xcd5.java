package org.artqq.oidb;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import java.io.IOException;

import org.artqq.protobuf.wording_storage;

public class oidb_0xcd5 {
    public static class Req{
        @Protobuf(fieldType = FieldType.INT32, order = 1)
        public int service_type;

        @Protobuf(fieldType = FieldType.OBJECT, order = 2)
        public GetDataReq getdata_req;
        
        @Protobuf(fieldType = FieldType.OBJECT, order = 3)
        public SetDataReq setdata_req;
        
        @Protobuf(fieldType = FieldType.OBJECT, order = 4)
        public SetDataReq check_req;
        
        public byte[] toByteArray(){
            try {
                return ProtobufProxy.create(Req.class).encode(this);
            } catch (IOException e) {
                return new byte[0];
            }
        }
    }
    
    public static class CheckReplyReq {
    }
    
    public static class SetDataReq {
        @Protobuf(fieldType = FieldType.OBJECT, order = 1)
        public wording_storage.WordingCfg data;
    }
    
    public static class GetDataReq {
        @Protobuf(fieldType = FieldType.INT32, order = 1)
        public int query_type;
    }
}
