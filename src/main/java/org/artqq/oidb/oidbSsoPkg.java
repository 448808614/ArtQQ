package org.artqq.oidb;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import java.io.IOException;

import org.artqq.utils.bytes.ByteObject;

public class oidbSsoPkg {
    @Protobuf(fieldType = FieldType.INT32, order = 1)
    public int command;
    
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    public int service_type;
    
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    public int result = 0;
    
    @Protobuf(fieldType = FieldType.BYTES, order = 4)
    public byte[] bodybuffer;
    
    @Protobuf(fieldType = FieldType.STRING, order = 5)
    public String error_msg;
    
    // ArtQQ除非重大更想，否则将不再上行支持新版协议
    @Protobuf(fieldType = FieldType.STRING, order = 6)
    public String client_version = "android 8.4.1";
    
    public byte[] toByteArray(){
        try {
            return ProtobufProxy.create(oidbSsoPkg.class).encode(this);
        } catch (IOException e) {
            return new byte[0];
        }
    }
    
    public static oidbSsoPkg decode(byte[] source){
        try {
            return ProtobufProxy.create(oidbSsoPkg.class).decode(new ByteObject(source).disUseData(4).readRestBytes());
        } catch (IOException e) {
            return null;
        }
    }
}
