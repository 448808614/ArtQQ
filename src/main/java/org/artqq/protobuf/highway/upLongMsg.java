package org.artqq.protobuf.highway;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import java.io.IOException;
import java.util.Random;
import org.artqq.ArtBot;
import org.artqq.MD5;
import org.artqq.protobuf.BDH.BDH;
import org.artqq.protobuf.MessageSvc;
import org.artqq.protobuf.msg_comm;
import org.artqq.utils.GZIPUtils;
import org.artqq.utils.QQAgreementUtils;
import org.artqq.utils.TCPSocket;

public class upLongMsg {
    public static class NewServiceTicket {
        @Protobuf(fieldType = FieldType.BYTES, order = 1)
        public byte[] signature;

        @Protobuf(fieldType = FieldType.BYTES, order = 2)
        public byte[] ukey;
    }

    public static class LongMsgReq {
        @Protobuf(fieldType = FieldType.OBJECT, order = 1)
        public msg_comm.Msg msg;

        public byte[] toByteArray(){
            try {
                return ProtobufProxy.create(LongMsgReq.class).encode(this);
            } catch (IOException e) {
                return null;
            }
        }
    }

    public static class LongMsgParse {
        @Protobuf(fieldType = FieldType.OBJECT, order = 1)
        public DataHighwayHead dataHead;

        @Protobuf(fieldType = FieldType.OBJECT, order = 2)
        public SigHead sigHead;

        @Protobuf(fieldType = FieldType.OBJECT, order = 7)
        public BDH.UploadPicExtInfo msg_info;
    }


    public static String getResId(byte[] in){
        try {
            String resid = ProtobufProxy.create(LongMsgParse.class).decode(in).msg_info.msg_resid;
            return resid;
        } catch (IOException e) {
            System.out.println(e);
        }
        return "RESID获取失败X2";
    }

    public static byte[] group_LongMessageUp(ArtBot bot, long to_uin, MessageSvc.MsgBody msgbody){
        HwData data = new HwData();
        LongMsgReq msgReq = new LongMsgReq();
        msg_comm.Msg msg = new msg_comm.Msg();
        msgReq.msg = msg;
        msg.msg_head = new msg_comm.MsgHead();
        msg.msg_head.from_uin = bot.getUin();
        msg.msg_head.msg_type = 82;
        msg.msg_head.msg_seq = bot.recorder.nextMessageSeq();
        msg.msg_head.msg_time = Math.toIntExact(System.currentTimeMillis() / 1000);
        msg.msg_head.msg_uid = new Random().nextLong();
        msg.msg_head.group_info = new msg_comm.GroupInfo();
        msg.msg_head.group_info.group_code = to_uin;
        msg.msg_head.group_info.group_card = "ArtLongMessage";
        msg.msg_head.mutiltrans_head = new msg_comm.MutilTransHead();
        msg.msg_head.mutiltrans_head.status = 0L;
        msg.msg_head.mutiltrans_head.msgId = 1L;
        msg.msg_body = msgbody;
        byte[] msgData_n = msgReq.toByteArray();
        LongMsg.ReqBody req = new LongMsg.ReqBody();
        req.subcmd = 1;
        req.term_type = 5;
        req.platform_type = 9;
        req.msg_up_req = new LongMsg.MsgUpReq();
        req.msg_up_req.dst_uin = to_uin;
        req.msg_up_req.msg_type = 3;
        req.msg_up_req.store_type = 2;
        req.msg_up_req.msg_content = GZIPUtils.compress(msgData_n);
        byte[] msgData = req.toByteArray();
        if(msgData == null) return null;
        SigHead sigHead = new SigHead();
        DataHighwayHead highwayHead = new DataHighwayHead();
        highwayHead.command = "PicUp.DataUp";
        highwayHead.commandId = 77;
        // long_msg 77
        highwayHead.dataflag = 4096;
        highwayHead.appid = bot.agreement_info.appid;
        highwayHead.uin = String.valueOf(bot.getUin());
        highwayHead.seq = bot.recorder.nextHwSeq();
        sigHead.dataoffset = 0;
        sigHead.md5 = MD5.toMD5Byte(msgData);
        sigHead.file_md5 = MD5.toMD5Byte(msgData);
        sigHead.datalength = msgData.length;
        sigHead.filesize = msgData.length;
        data.head = new HwPacket(highwayHead, sigHead).toByteArray();
        data.data = msgData;
        return QQAgreementUtils.make_hw_packet(data);
    }
    
    public static String getMsgResId(ArtBot bot, long to_uin, MessageSvc.MsgBody msgbody){
        try {
            TCPSocket socket = new TCPSocket(bot.sigInfo.highwayServer, bot.sigInfo.highwayPort);
            socket.send(group_LongMessageUp(bot, to_uin, msgbody));
            HwData data = socket.readHwData(10000);
            return getResId(data.head);
        } catch (IOException e) {
            System.out.println(e);
        }
        return "RESID获取失败";
    }
    
}

