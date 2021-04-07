package org.artqq.jce.online;

import com.qq.taf.jce.JceInputStream;
import com.qq.taf.jce.JceStruct;
import org.helper.Desc;

/**
 * @author luoluo
 * @date 2020/8/22 10:58
 */
public class MsgInfo extends JceStruct implements Cloneable {
	@Desc(desc = "群号")
	public long fromUin;
	public long msgTime;
	public int msgType;
	public int msgSeq;
	public byte[] vMsg = null;

	private static final byte[] cache_vMsg = {};

	@Override
	public void readFrom(JceInputStream paramJceInputStream) {
		fromUin = paramJceInputStream.read(fromUin, 0, true);
		msgTime = paramJceInputStream.read(msgTime, 1, true);
		msgType = paramJceInputStream.read(msgType, 2, true);
		msgSeq = paramJceInputStream.read(msgSeq, 3, true);
		vMsg = paramJceInputStream.read(cache_vMsg, 6, false);
	}
}
