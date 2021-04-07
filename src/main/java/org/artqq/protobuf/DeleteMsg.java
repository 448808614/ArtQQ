package org.artqq.protobuf;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.artqq.ArtBot;
import org.artqq.Code;
import org.artqq.utils.QQAgreementUtils;
import org.artqq.utils.TCPSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luoluo
 * @date 2020/8/28 3:28
 */
public class DeleteMsg {
	public static class Req {
		@Protobuf(order = 1, fieldType = FieldType.OBJECT)
		public ArrayList<Item> msg_items;

		public byte[] toByteArray(){
			try {
				return ProtobufProxy.create(Req.class).encode(this);
			} catch (IOException e) {
				if(Code.isDebug)
					e.printStackTrace();
			}
			return null;
		}
	}

	public static class Item {
		@Protobuf(order = 1, fieldType = FieldType.UINT64)
		public long from_uin;

		@Protobuf(order = 2, fieldType = FieldType.UINT64)
		public long to_uin;

		@Protobuf(order = 3, fieldType = FieldType.UINT32)
		public int msg_type;

		@Protobuf(order = 4, fieldType = FieldType.UINT32)
		public int msg_seq;

		@Protobuf(order = 5, fieldType = FieldType.UINT64)
		public long uid;

		@Protobuf(order = 7, fieldType = FieldType.BYTES)
		public byte[] sig = {};

		@Override
		public String toString() {
			return "Item{" +
				"from_uin=" + from_uin +
				", to_uin=" + to_uin +
				", msg_type=" + msg_type +
				", msg_seq=" + msg_seq +
				", uid=" + uid +
				'}';
		}
	}

	public static void deleteFriendMsg(ArtBot bot, List<GetMsg.Msgs> msgs){
		Req req = new Req();
		req.msg_items = new ArrayList<>();
		for(GetMsg.Msgs m : msgs){
			for(msg_comm.Msg msg: m.msgs){
				Item item = new Item();
				item.from_uin = msg.msg_head.from_uin;
				item.to_uin = msg.msg_head.to_uin;
				item.msg_type = 187;
				item.msg_seq = msg.msg_head.msg_seq;
				item.uid = msg.msg_head.msg_uid;
				// System.out.println(item.toString());
				req.msg_items.add(item);
			}
		}
		int seq = bot.recorder.nextSsoSeq();
		byte[] data = QQAgreementUtils.encodeRequest(bot, seq, "MessageSvc.PbDeleteMsg", req.toByteArray());
		getClient(bot).send(data);
	}

	private static TCPSocket getClient(ArtBot bot){
		if(bot.data.containsKey("client")){
			return (TCPSocket) bot.data.get("client");
		}
		return null;
	}
}
