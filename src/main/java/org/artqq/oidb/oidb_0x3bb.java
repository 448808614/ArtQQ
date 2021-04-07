package org.artqq.oidb;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import java.io.IOException;

/**
 * @author luoluo
 * @date 2020/8/16 3:26
 */
public class oidb_0x3bb {
	public static class ReqBody {
		@Protobuf(order = 2, fieldType = FieldType.UINT64)
		public long group_code;

		@Protobuf(order = 1, fieldType = FieldType.UINT64)
		public long uin;
	}

	public static class Resp {
		@Protobuf(order = 1, fieldType = FieldType.INT32)
		public int ret;

		@Protobuf(order = 2, fieldType = FieldType.UINT64)
		public long group_code;

		@Protobuf(order = 3, fieldType = FieldType.STRING)
		public String anony_name;

		@Protobuf(order = 4, fieldType = FieldType.UINT32)
		public int portrait_index;

		@Protobuf(order = 5, fieldType = FieldType.UINT32)
		public int bubble_index;

		@Protobuf(order = 6, fieldType = FieldType.UINT32)
		public int expired_time;

		@Protobuf(order = 10, fieldType = FieldType.OBJECT)
		public AnonyStatus anony_status;

		@Protobuf(order = 15, fieldType = FieldType.STRING)
		public String color;

		public static class AnonyStatus {
			@Protobuf(order = 1, fieldType = FieldType.UINT32)
			public int forbid_talking;

			@Protobuf(order = 10, fieldType = FieldType.STRING)
			public String err_msg;
		}
	}

	public static class Body {
		@Protobuf(order = 1, fieldType = FieldType.UINT32)
		public int cmd;

		@Protobuf(order = 10, fieldType = FieldType.OBJECT)
		public ReqBody anony_req;

		@Protobuf(order = 11, fieldType = FieldType.OBJECT)
		public Resp anony_rsp;

		public byte[] toByteArray(){
			try {
				return ProtobufProxy.create(Body.class).encode(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		public static Body decode(byte[] src){
			try {
				return ProtobufProxy.create(Body.class).decode(src);
			} catch (IOException e) {}
			return null;
		}
	}

}
