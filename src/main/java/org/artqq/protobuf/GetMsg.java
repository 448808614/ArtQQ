package org.artqq.protobuf;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.artqq.ArtBot;
import org.artqq.Code;
import org.artqq.utils.QQAgreementUtils;
import org.artqq.utils.RandomUtil;
import org.artqq.utils.TCPSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luoluo
 * @date 2020/8/28 1:17
 */
public class GetMsg {
	public static class Req {
		@Protobuf(fieldType = FieldType.ENUM, order = 1)
		public SyncFlag sync_flag;

		@Protobuf(order = 2, fieldType = FieldType.BYTES)
		public byte[] sync_cookie;

		@Protobuf(order = 3, fieldType = FieldType.UINT32)
		public int ramble_flag = 1;

		@Protobuf(order = 4, fieldType = FieldType.UINT32)
		public int latest_ramble_number = 20;

		@Protobuf(order = 5, fieldType = FieldType.UINT32)
		public int other_ramble_number = 3;

		@Protobuf(order = 6, fieldType = FieldType.UINT32)
		public int online_sync_flag = 1;

		@Protobuf(order = 7, fieldType = FieldType.UINT32)
		public int context_flag = 1;

		@Protobuf(order = 8, fieldType = FieldType.UINT32)
		public int whisper_session_id = 0;

		@Protobuf(order = 9, fieldType = FieldType.UINT32)
		public int msg_req_type;

		@Protobuf(order = 10, fieldType = FieldType.BYTES)
		public byte[] pubaccount_cookie;

		@Protobuf(order = 11, fieldType = FieldType.BYTES)
		public byte[] msg_ctrl_buf;

		@Protobuf(order = 12, fieldType = FieldType.BYTES)
		public byte[] server_buf = {};

		public byte[] toByteArray(){
			try {
				return ProtobufProxy.create(Req.class).encode(this);
			} catch (IOException ignored) { }
			return null;
		}
	}

	public static class Resp {
		@Protobuf(order = 1, fieldType = FieldType.UINT32)
		public int result;

		@Protobuf(order = 3, fieldType = FieldType.BYTES)
		public byte[] sync_cookie;

		@Protobuf(order = 4, fieldType = FieldType.UINT32)
		public int sync_flag;

		@Protobuf(order = 5, fieldType = FieldType.OBJECT)
		public List<Msgs> msgs;

		@Protobuf(order = 7, fieldType = FieldType.UINT32)
		public int msg_rsp_type;

		@Protobuf(order = 8, fieldType = FieldType.BYTES)
		public byte[] pub_account_cookie;

		@Protobuf(order = 10, fieldType = FieldType.BYTES)
		public byte[] msg_ctrl_buf;
	}

	public static Resp decode(byte[] src){
		try {
			return ProtobufProxy.create(Resp.class).decode(src);
		} catch(IOException e) {
			if(Code.isDebug)
				e.printStackTrace();
		}
		return null;
	}

	public static void getFriendMsg(ArtBot bot){
		Req req = new Req();
		req.sync_flag = SyncFlag.START;
		req.ramble_flag = 0;
		req.other_ramble_number = 3;
		req.msg_req_type = 0;
		req.sync_cookie = bot.data.containsKey("SyncCookie") ? (byte[]) bot.data.get("SyncCookie") : null;
		req.pubaccount_cookie = bot.data.containsKey("PbAcCookie") ? (byte[]) bot.data.get("PbAcCookie") : null;
		req.msg_ctrl_buf = bot.data.containsKey("MsgCtrlBuf") ? (byte[]) bot.data.get("MsgCtrlBuf") : null;
		int seq = bot.recorder.nextSsoSeq();
		byte[] data = QQAgreementUtils.encodeRequest(bot, seq, "MessageSvc.PbGetMsg", req.toByteArray());
		getClient(bot).send(data);
	}

	private static TCPSocket getClient(ArtBot bot){
		if(bot.data.containsKey("client")){
			return (TCPSocket) bot.data.get("client");
		}
		return null;
	}

	public static class Msgs {
		@Protobuf(order = 1, fieldType = FieldType.UINT32)
		public int last_read_time;

		@Protobuf(order = 2, fieldType = FieldType.UINT64)
		public long peer_uin;

		@Protobuf(order = 4, fieldType = FieldType.OBJECT)
		public ArrayList<msg_comm.Msg> msgs;

		@Protobuf(order = 8, fieldType = FieldType.UINT32)
		public int c2c_type;

		@Protobuf(order = 9, fieldType = FieldType.UINT32)
		public int s_type;
	}

	public static class SyncCookie {
		@Protobuf(order = 1, fieldType = FieldType.UINT64)
		public long client_time = System.currentTimeMillis() / 1000;

		@Protobuf(order = 2, fieldType = FieldType.UINT64)
		public long msg_time = System.currentTimeMillis() / 1000;

		@Protobuf(order = 3, fieldType = FieldType.UINT64)
		public long msg_rand = RandomUtil.randomInt(0, 100000000);

		@Protobuf(order = 4, fieldType = FieldType.UINT64)
		public long random = RandomUtil.randomInt(0, 1000000000);
	}

	public enum SyncFlag {
		START,
		CONTINUE,
		STOP
	}
}
