package org.artqq.protobuf.BDH;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.FieldType;

public class BDH {
    public static class UploadPicExtInfo {
        @Protobuf(fieldType = FieldType.STRING, order = 1)
        public String msg_resid;
        // 待补全
    }
}

