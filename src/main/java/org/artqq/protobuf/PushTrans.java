package org.artqq.protobuf;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.artqq.Code;
import org.artqq.utils.bytes.ByteObject;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author luoluo
 * @date 2020/8/29 0:04
 */
public class PushTrans {
	@Protobuf(order = 1, fieldType = FieldType.UINT64)
	public long from_uin;

	@Protobuf(order = 2, fieldType = FieldType.UINT64)
	public long to_uin;

	@Protobuf(order = 3, fieldType = FieldType.INT32)
	public int msg_type;

	@Protobuf(order = 4, fieldType = FieldType.INT32)
	public int msg_sub_type;

	@Protobuf(order = 5, fieldType = FieldType.UINT32)
	public int msg_seq;

	@Protobuf(order = 6, fieldType = FieldType.UINT64)
	public long msg_uid;

	@Protobuf(order = 7, fieldType = FieldType.INT32)
	public int msg_time;

	@Protobuf(order = 8, fieldType = FieldType.INT32)
	public int real_msg_time;

	@Protobuf(order = 9, fieldType = FieldType.STRING)
	public String nick_name;

	@Protobuf(order = 10, fieldType = FieldType.BYTES)
	public byte[] msg_data;

	@Protobuf(order = 11, fieldType = FieldType.UINT32)
	public int srv_ip;


	public static PushTrans decode(byte[] src){
		try {
			return ProtobufProxy.create(PushTrans.class).decode(new ByteObject(src).disUseData(4).readRestBytes());
		} catch(IOException e) {
			if(Code.isDebug){
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "PushTrans{" +
			"from_uin=" + from_uin +
			", to_uin=" + to_uin +
			", msg_type=" + msg_type +
			", msg_sub_type=" + msg_sub_type +
			", msg_seq=" + msg_seq +
			", msg_uid=" + msg_uid +
			", msg_time=" + msg_time +
			", real_msg_time=" + real_msg_time +
			", nick_name='" + nick_name + '\'' +
			", msg_data=" + Arrays.toString(msg_data) +
			", srv_ip=" + srv_ip +
			'}';
	}
}
