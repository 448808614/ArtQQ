package org.artqq.protobuf;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.helper.Desc;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author luoluo
 * @date 2020/8/22 14:28
 */
public class NotifyMsg {
	@Protobuf(order = 1, fieldType = FieldType.INT32)
	public int type;

	@Protobuf(order = 2, fieldType = FieldType.UINT64)
	public long msg_time;

	@Protobuf(order = 3, fieldType = FieldType.UINT64)
	public long msg_expires;

	@Protobuf(order = 4, fieldType = FieldType.UINT64)
	public long group_code;

	@Protobuf(order = 5, fieldType = FieldType.OBJECT)
	public GrayTips msg_graytips;

	@Protobuf(order = 11, fieldType = FieldType.OBJECT)
	public MsgReCall msg_recall;

	@Protobuf(order = 13, fieldType = FieldType.UINT32)
	public int service_type = -1;

	/*
	@Protobuf(order = 6, fieldType = FieldType.OBJECT)
	public Object msg_messagebox;

	@Protobuf(order = 7, fieldType = FieldType.OBJECT)
	public Object msg_floatedtips;

	@Protobuf(order = 8, fieldType = FieldType.OBJECT)
	public Object msg_toptips;

	@Protobuf(order = 9, fieldType = FieldType.OBJECT)
	public Object msg_redtips;

	@Protobuf(order = 10, fieldType = FieldType.OBJECT)
	public Object msg_group_notify;

	@Protobuf(order = 12, fieldType = FieldType.OBJECT)
	public Object msg_theme_notify;

	@Protobuf(order = 14, fieldType = FieldType.OBJECT)
	public Object msg_objmsg_update;

	@Protobuf(order = 15, fieldType = FieldType.OBJECT)
	public Object msg_werewolf_push;

	@Protobuf(order = 16, fieldType = FieldType.OBJECT)
	public Object stcm_game_state;

	@Protobuf(order = 17, fieldType = FieldType.OBJECT)
	public Object apllo_msg_push;

	@Protobuf(order = 18, fieldType = FieldType.OBJECT)
	public Object msg_goldtips;

	@Protobuf(order = 20, fieldType = FieldType.OBJECT)
	public Object msg_miniapp_notify;
	*/

	@Protobuf(order = 21, fieldType = FieldType.UINT64)
	public long sender_uin;

	/*
	@Protobuf(order = 22, fieldType = FieldType.OBJECT)
	public Object msg_luckybag_notify;

	@Protobuf(order = 23, fieldType = FieldType.OBJECT)
	public Object msg_troopformtips_push;

	@Protobuf(order = 24, fieldType = FieldType.OBJECT)
	public Object msg_media_push;

	@Protobuf(order = 26, fieldType = FieldType.OBJECT)
	public Object general_gray_tip;

	@Protobuf(order = 27, fieldType = FieldType.OBJECT)
	public Object msg_video_push;

	@Protobuf(order = 28, fieldType = FieldType.OBJECT)
	public Object lbs_share_change_plus_info;

	@Protobuf(order = 29, fieldType = FieldType.OBJECT)
	public Object msg_sing_push;

	@Protobuf(order = 31, fieldType = FieldType.OBJECT)
	public Object group_announce_tbc_info;

	@Protobuf(order = 32, fieldType = FieldType.OBJECT)
	public Object qq_vedio_game_push_info;

	@Protobuf(order = 33, fieldType = FieldType.OBJECT)
	public Object qq_group_digest_msg;

	@Protobuf(order = 34, fieldType = FieldType.OBJECT)
	public Object study_room_member_msg;
	 */

	@Protobuf(order = 30, fieldType = FieldType.OBJECT)
	public GroupInfoChange msg_group_info_change;

	public static NotifyMsg decode(byte[] src){
		try {
			return ProtobufProxy.create(NotifyMsg.class).decode(src);
		} catch(IOException e) { }
		return null;
	}

	public static class MsgReCall {
		@Desc(desc = "操作者")
		@Protobuf(order = 1, fieldType = FieldType.UINT64)
		public long uin;

		@Protobuf(order = 3, fieldType = FieldType.OBJECT)
		public ArrayList<Info> info;

		public static class Info {
			@Protobuf(order = 1, fieldType = FieldType.UINT32)
			public int seq;

			@Protobuf(order = 2, fieldType = FieldType.UINT32)
			public int time;

			@Protobuf(order = 6, fieldType = FieldType.UINT64)
			public long from_uin;
		}
	}

	public static class GroupInfoChange {
		@Protobuf(order = 1, fieldType = FieldType.UINT32)
		public int group_honor_switch;

		@Protobuf(order = 2, fieldType = FieldType.UINT32)
		public int group_member_level_switch;

		@Protobuf(order = 3, fieldType = FieldType.UINT32)
		public int group_flagext4;

		@Protobuf(order = 4, fieldType = FieldType.UINT32)
		public int appeal_deadline;

		@Protobuf(order = 5, fieldType = FieldType.UINT32)
		public int group_flag;

		@Protobuf(order = 7, fieldType = FieldType.UINT32)
		public int group_flagext3 = -1;

		@Protobuf(order = 8, fieldType = FieldType.UINT32)
		public int group_class_ext;
	}

	public static class GrayTips {
		@Protobuf(order = 1, fieldType = FieldType.UINT32)
		public int show_lastest;

		@Protobuf(order = 2, fieldType = FieldType.STRING)
		public String content;

		@Protobuf(order = 3, fieldType = FieldType.UINT32)
		public int remind;

		@Protobuf(order = 4, fieldType = FieldType.BYTES)
		public byte[] brief;

		@Protobuf(order = 5, fieldType = FieldType.UINT64)
		public long receiver_uin;

		@Protobuf(order = 6, fieldType = FieldType.UINT32)
		public int reliao_admin_opt;

		@Protobuf(order = 7, fieldType = FieldType.UINT32)
		public int robot_group_opt;
	}
}
