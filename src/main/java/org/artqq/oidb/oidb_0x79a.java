package org.artqq.oidb;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import java.io.IOException;

public class oidb_0x79a {
	public static class Req {
		@Protobuf(order = 1, fieldType = FieldType.UINT64)
		public long group_code;

		@Protobuf(order = 2, fieldType = FieldType.UINT32)
		public int source;

		public byte[] toByteArray(){
			try {
				return ProtobufProxy.create(Req.class).encode(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public static class Resp {
		@Protobuf(fieldType = FieldType.OBJECT, order = 1)
		public oidb_0x88d.GroupInfo info;

		@Protobuf(order = 4, fieldType = FieldType.UINT32)
		public int tribe_status;

		@Protobuf(order = 5, fieldType = FieldType.UINT64)
		public long tribe_id;

		@Protobuf(order = 6, fieldType = FieldType.UINT32)
		public int modify_countdown;

		@Protobuf(order = 7, fieldType = FieldType.STRING)
		public String tribe_name;
	}

}
