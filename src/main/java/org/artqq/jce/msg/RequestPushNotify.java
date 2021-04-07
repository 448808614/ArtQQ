package org.artqq.jce.msg;

import com.qq.taf.jce.JceInputStream;
import com.qq.taf.jce.JceStruct;
import org.artqq.jce.online.MsgInfo;

/**
 * @author luoluo
 * @date 2020/8/28 1:02
 */
public class RequestPushNotify extends JceStruct {
	static byte[] cache_bytes_server_buf = new byte[1];
	static MsgInfo cache_stMsgInfo = new MsgInfo();
	static byte[] cache_vNotifyCookie = new byte[1];
	public byte[] bytes_server_buf;
	public byte cType;
	public long lBindedUin;
	public long lUin;
	public String msg_ctrl_buf = "";
	public long ping_flag;
	public MsgInfo stMsgInfo;
	public String strCmd = "";
	public String strService = "";
	public int svrip;
	public int usMsgType;
	public byte[] vNotifyCookie;
	public int wGeneralFlag;
	public int wUserActive;

	@Override
	public void readFrom(JceInputStream jceInputStream) {
		this.lUin = jceInputStream.read(this.lUin, 0, true);
		this.cType = jceInputStream.read(this.cType, 1, true);
		this.strService = jceInputStream.readString(2, true);
		this.strCmd = jceInputStream.readString(3, true);
		this.vNotifyCookie = jceInputStream.read(cache_vNotifyCookie, 4, false);
		this.usMsgType = jceInputStream.read(this.usMsgType, 5, false);
		this.wUserActive = jceInputStream.read(this.wUserActive, 6, false);
		this.wGeneralFlag = jceInputStream.read(this.wGeneralFlag, 7, false);
		this.lBindedUin = jceInputStream.read(this.lBindedUin, 8, false);
		this.stMsgInfo = (MsgInfo) jceInputStream.readV2(cache_stMsgInfo, 9, false);
		this.msg_ctrl_buf = jceInputStream.readString(10, false);
		this.bytes_server_buf = jceInputStream.read(cache_bytes_server_buf, 11, false);
		this.ping_flag = jceInputStream.read(this.ping_flag, 12, false);
		this.svrip = jceInputStream.read(this.svrip, 13, false);
	}
}
