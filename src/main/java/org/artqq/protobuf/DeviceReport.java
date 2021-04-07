package org.artqq.protobuf;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import java.io.IOException;

public class DeviceReport {
    @Protobuf(fieldType = FieldType.STRING, order = 1)
    public String bootloader = "unknown";
    
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    public String version = "Linux version 3.4.0-g5170b88-dirty (xzl@xzl-All-Series) (gcc version 4.8 (GCC) ) #60 SMP PREEMPT Mon Jul 30 18:18:14 CST 2018(100066)";
    
    @Protobuf(fieldType = FieldType.STRING, order = 3)
    public String codename = "REL";
    
    @Protobuf(fieldType = FieldType.STRING, order = 4)
    public String incremental = "eng.frank.20191025.014142";

    @Protobuf(fieldType = FieldType.STRING, order = 5)
    public String fingerprint = "Android/aosp_arm/generic:5.1.1/LMY48G/10250142:user/test-keys";
    
    @Protobuf(fieldType = FieldType.STRING, order = 6)
    public String boot_id = "";
    
    @Protobuf(fieldType = FieldType.STRING, order = 7)
    public String android_id = "cf44fe72cc39171c";
    
    @Protobuf(fieldType = FieldType.STRING, order = 8)
    public String baseband = "no message";
    
    @Protobuf(fieldType = FieldType.STRING, order = 9)
    public String inner_ver = "eng.frank.20191025.014142";
    
    public byte[] toByteArray(){
        try {
            return ProtobufProxy.create(DeviceReport.class).encode(this);
        } catch (IOException e) {
            return new byte[0];
        }
    }
}
