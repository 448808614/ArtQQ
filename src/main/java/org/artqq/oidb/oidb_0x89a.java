package org.artqq.oidb;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import java.io.IOException;

public class oidb_0x89a {
    public static final class Req {
        @Protobuf(fieldType = FieldType.UINT64, order = 1)
        public Long groupid;

        @Protobuf(fieldType = FieldType.OBJECT, order = 2)
        public GroupInfo group_info;
        
        public byte[] toByteArray(){
            try {
                return ProtobufProxy.create(Req.class).encode(this);
            } catch (IOException e) {
                return new byte[0];
            }
        }
    }
    
    public static class GroupInfo {
        @Protobuf(fieldType = FieldType.INT64, order = 17)
        public long shutup_time;   
    }
}
