package org.artqq.oidb;

import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import org.artqq.utils.bytes.ByteObject;

import java.io.IOException;

/**
 * @author luoluo
 * @date 2020/9/5 0:43
 */
public class oidb_0x55c {
	public static class Req {
		public static byte[] toByteArray(long groupId, long uin, boolean isDelete){
			oidbSsoPkg ssoPkg = new oidbSsoPkg();
			ByteObject src = new ByteObject();
			src.WriteInt(groupId);
			src.WriteInt(uin);
			src.WriteBoolean(!isDelete);
			ssoPkg.bodybuffer = src.toByteArray();
			ssoPkg.command = 0x55c;
			ssoPkg.service_type = 1;
			return ssoPkg.toByteArray();
		}
	}
}
