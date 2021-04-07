package org.artqq.oidb;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import java.io.IOException;
import java.util.List;

/**
 * @author luoluo
 * @date 2020/8/27 1:11
 */
public class oidb_0x8fc {
	public static class Req {
		@Protobuf(fieldType = FieldType.UINT64, order = 1, required = true)
		public long groupid;

		@Protobuf(fieldType = FieldType.UINT32, order = 2)
		public int show_flag;

		@Protobuf(fieldType = FieldType.OBJECT, order = 3)
		public MemInfo mem_info;

		public byte[] toByteArray(){
			try {
				return ProtobufProxy.create(Req.class).encode(this);
			} catch (IOException e) {
				return new byte[0];
			}
		}
	}

	public static class MemInfo {
		@Protobuf(order = 1, fieldType = FieldType.UINT64)
		public long uin;

		@Protobuf(order = 2, fieldType = FieldType.UINT32)
		public int point;

		@Protobuf(order = 3, fieldType = FieldType.UINT32)
		public int active_day;

		@Protobuf(order = 4, fieldType = FieldType.UINT32)
		public int level;

		@Protobuf(order = 5, fieldType = FieldType.BYTES)
		public byte[] special_title;

		@Protobuf(order = 6, fieldType = FieldType.UINT64)
		public long special_title_expire_time;

		@Protobuf(order = 7, fieldType = FieldType.BYTES)
		public byte[] uin_name;

		@Protobuf(order = 8, fieldType = FieldType.BYTES)
		public byte[] member_card_name;

		@Protobuf(order = 9, fieldType = FieldType.BYTES)
		public byte[] phone;

		@Protobuf(order = 10, fieldType = FieldType.BYTES)
		public byte[] email;

		@Protobuf(order = 11, fieldType = FieldType.BYTES)
		public byte[] remark;

		@Protobuf(order = 12, fieldType = FieldType.UINT32)
		public int gender;

		@Protobuf(order = 13, fieldType = FieldType.BYTES)
		public byte[] job;

		@Protobuf(order = 14, fieldType = FieldType.UINT32)
		public int tribe_level;

		@Protobuf(order = 15, fieldType = FieldType.UINT32)
		public int tribe_point;

		@Protobuf(order = 16, fieldType = FieldType.OBJECT)
		public CardElem rpt_rich_card_name;

		@Protobuf(order = 17, fieldType = FieldType.BYTES)
		public byte[] comm_rich_card_name;
	}

	public static class CardElem {
		public static final int CARD_TYPE_TEXT = 1;
		public static final int CARD_TYPE_XC = 2;

		@Protobuf(order = 1, fieldType = FieldType.UINT32)
		public int card_type;

		@Protobuf(order = 2, fieldType = FieldType.BYTES)
		public byte[] value;
	}

}
