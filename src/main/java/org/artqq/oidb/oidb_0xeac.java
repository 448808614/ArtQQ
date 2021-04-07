package org.artqq.oidb;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.artqq.utils.RandomUtil;

import java.io.IOException;

/**
 * @author luoluo
 * @date 2020/8/16 5:36
 */
public class oidb_0xeac {
	public static class ReqBody {
		@Protobuf(order = 1, fieldType = FieldType.UINT64)
		public long group_code;

		@Protobuf(order = 2, fieldType = FieldType.UINT32)
		public int msg_seq;

		@Protobuf(order = 3, fieldType = FieldType.UINT32)
		public int msg_random = RandomUtil.randomInt(0, 1000000000);

		public byte[] toByteArray(){
			try {
				return ProtobufProxy.create(ReqBody.class).encode(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
