package org.artqq.jce;

import com.qq.taf.jce.JceOutputStream;
import com.qq.taf.jce.JceStruct;
import org.artqq.utils.QQAgreementUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class report extends JceStruct {
	public Map<String, ArrayList<byte[]>> log_string = new HashMap<>();
	public byte encoding;
	public int seq_no;

	@Override
	public void writeTo(JceOutputStream paramJceOutputStream) {
		paramJceOutputStream.write(this.log_string, 0);
		paramJceOutputStream.write(this.encoding, 2);
		paramJceOutputStream.write(this.seq_no, 3);
	}

	public static byte[] getReport(int requestId, String... msg){
		report r = new report();
		for(String text : msg){
			ArrayList<byte[]> add = new ArrayList<>();
			add.add(text.getBytes());
			r.log_string.put(text, add);
		}
		return QQAgreementUtils.encodePacket(r,
			"QQService.CliLogSvc.MainServantObj",
			"UpLoadReq",
			"Data",
			requestId
		);
	}

}
