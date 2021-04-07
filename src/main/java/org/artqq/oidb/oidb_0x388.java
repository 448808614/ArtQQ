package org.artqq.oidb;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import java.io.IOException;
import java.util.Random;

public class oidb_0x388 {
    public static class ReqBody {
        /*
         4_rpt_msg_getimg_url_req
         5_rpt_msg_tryup_ptt_req
         6_rpt_msg_getptt_url_req
         7_command_id
         8_rpt_msg_del_img_req
         1001_extension*/
        @Protobuf(fieldType = FieldType.INT32, order = 1)
        public int net_type = 3; // wifi

        @Protobuf(fieldType = FieldType.INT32, order = 2)
        public int subcmd = 1;

        @Protobuf(fieldType = FieldType.OBJECT, order = 3)
        public TryUpImgReq msg_tryup_img_req;

        @Protobuf(fieldType = FieldType.OBJECT, order = 5)
        public TryUpPttReq msg_tryup_ptt_req;

        @Protobuf(fieldType = FieldType.INT32, order = 7)
        public int command_id;

        @Protobuf(fieldType = FieldType.BYTES, order = 1001)
        public byte[] extension;
        
        public byte[] toByteArray(){
            try {
                return ProtobufProxy.create(ReqBody.class).encode(this);
            } catch (IOException e) {
                System.out.println(e);
            }
            return null;
        }
    }
    
    public static class RspBody {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public long client_ip;

        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public long subcmd;

        @Protobuf(fieldType = FieldType.OBJECT, order = 3)
        public TryUpImgRsp rpt_msg_tryup_img_rsp;

        @Protobuf(fieldType = FieldType.OBJECT, order = 5)
        public TryUpPttRsp msg_tryup_ptt_rsp;

        public static RspBody decode(byte[] src){
            try {
                return ProtobufProxy.create(RspBody.class).decode(src);
            } catch (IOException e) {}
            return null;
        }
    }

    public static class TryUpPttRsp {
        @Protobuf(order = 1, fieldType = FieldType.INT64)
        public long file_id;

        @Protobuf(order = 2, fieldType = FieldType.INT32)
        public int result;

        @Protobuf(order = 3, fieldType = FieldType.BYTES)
        public byte[] fail_msg;

        @Protobuf(order = 4, fieldType = FieldType.BOOL)
        public boolean file_exit;

        @Protobuf(order = 5, fieldType = FieldType.UINT64)
        public Long up_ip;

        @Protobuf(order = 6, fieldType = FieldType.UINT32)
        public Integer up_port;

        @Protobuf(order = 7, fieldType = FieldType.BYTES)
        public byte[] up_ukey;

        @Protobuf(order = 8, fieldType = FieldType.INT64)
        public long fileid;

        @Protobuf(order = 9, fieldType = FieldType.INT64)
        public long up_offset;

        @Protobuf(order = 10, fieldType = FieldType.INT64)
        public long block_size;

        @Protobuf(order = 11, fieldType = FieldType.BYTES)
        public byte[] file_key;

        @Protobuf(order = 12, fieldType = FieldType.INT32)
        public int channel_type;

        // @Protobuf(order = 26, fieldType = FieldType.OBJECT)
        // public Object rpt_msg_up_ip6;

        @Protobuf(order = 27, fieldType = FieldType.BYTES)
        public byte[] client_ip6;
    }

    public static class TryUpPttReq {
        @Protobuf(order = 1, fieldType = FieldType.INT64)
        public long group_code;

        @Protobuf(order = 2, fieldType = FieldType.INT64)
        public long src_uin;

        @Protobuf(order = 3, fieldType = FieldType.INT64)
        public long file_id = 0;

        @Protobuf(order = 4, fieldType = FieldType.BYTES)
        public byte[] file_md5;

        @Protobuf(order = 5, fieldType = FieldType.INT64)
        public long file_size;

        @Protobuf(order = 6, fieldType = FieldType.BYTES)
        public byte[] file_name;
        // xxx.amr

        @Protobuf(order = 7, fieldType = FieldType.INT32)
        public int src_term = 5;

        @Protobuf(order = 8, fieldType = FieldType.INT32)
        public int platform_type = 9;

        @Protobuf(order = 9, fieldType = FieldType.INT32)
        public int bu_type = 0;

        @Protobuf(order = 10, fieldType = FieldType.BYTES)
        public byte[] build_ver;

        @Protobuf(order = 11, fieldType = FieldType.INT32)
        public Integer inner_ip;

        @Protobuf(order = 12, fieldType = FieldType.INT32)
        public int voice_length = 0;

        @Protobuf(order = 13, fieldType = FieldType.BOOL)
        public boolean new_up_chan = true;

        @Protobuf(order = 14, fieldType = FieldType.INT32)
        public int codec = 1;

        @Protobuf(order = 15, fieldType = FieldType.INT32)
        public int voice_type = 1;

        @Protobuf(order = 16, fieldType = FieldType.INT32)
        public int bu_id;
    }
    
    public static class TryUpImgReq {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public long group_code;

        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public long src_uin;

        @Protobuf(fieldType = FieldType.INT64, order = 3)
        public long file_id = 0;

        @Protobuf(fieldType = FieldType.BYTES, order = 4)
        public byte[] file_md5;

        @Protobuf(fieldType = FieldType.INT64, order = 5)
        public long file_size;

        @Protobuf(fieldType = FieldType.BYTES, order = 6)
        public byte[] file_name;
        // xxx.artqq

        @Protobuf(fieldType = FieldType.INT64, order = 7)
        public long src_term = 5;

        @Protobuf(fieldType = FieldType.INT64, order = 8)
        public long platform_type = 9;

        @Protobuf(fieldType = FieldType.INT64, order = 9)
        public long bu_type = 1;

        @Protobuf(fieldType = FieldType.INT64, order = 10)
        public long pic_width = 1080;

        @Protobuf(fieldType = FieldType.INT64, order = 11)
        public long pic_height = 1980;

        @Protobuf(fieldType = FieldType.INT64, order = 12)
        public long pic_type = 1003;

        @Protobuf(fieldType = FieldType.BYTES, order = 13)
        public byte[] build_ver = "fuck you tencent qq".getBytes();

        @Protobuf(fieldType = FieldType.INT64, order = 14)
        public long inner_ip;

        @Protobuf(fieldType = FieldType.INT64, order = 15)
        public long app_pic_type = 1052;

        @Protobuf(fieldType = FieldType.INT64, order = 16)
        public long original_pic;

        @Protobuf(fieldType = FieldType.BYTES, order = 17)
        public byte[] file_index;

        @Protobuf(fieldType = FieldType.INT64, order = 18)
        public long dst_uin;

        @Protobuf(fieldType = FieldType.INT64, order = 19)
        public long srv_upload = 0;

        @Protobuf(fieldType = FieldType.BYTES, order = 20)
        public byte[] transfer_url;
    }

    public static class TryUpImgRsp {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public Long file_id = null;

        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public long result;

        @Protobuf(fieldType = FieldType.STRING, order = 3)
        public String fail_msg;

        @Protobuf(fieldType = FieldType.BOOL, order = 4)
        public boolean bool_file_exit = false;

        @Protobuf(fieldType = FieldType.OBJECT, order = 5)
        public ImgInfo msg_img_info;

        @Protobuf(fieldType = FieldType.INT64, order = 6)
        public Long rpt_up_ip;

        @Protobuf(fieldType = FieldType.INT32, order = 7)
        public Integer rpt_up_port;

        @Protobuf(fieldType = FieldType.BYTES, order = 8)
        public byte[] up_ukey;

        @Protobuf(fieldType = FieldType.INT64, order = 9)
        public Long fileid = null;

        @Protobuf(fieldType = FieldType.INT64, order = 10)
        public long up_offset;

        @Protobuf(fieldType = FieldType.INT64, order = 11)
        public long block_size;

        @Protobuf(fieldType = FieldType.BOOL, order = 12)
        public boolean bool_new_big_chan = false;

        @Protobuf(fieldType = FieldType.OBJECT, order = 26)
        public IPv6Info rpt_msg_up_ip6;

        @Protobuf(fieldType = FieldType.BYTES, order = 27)
        public byte[] client_ip6;

        @Protobuf(fieldType = FieldType.OBJECT, order = 1001)
        public TryUpInfo4Busi msg_info4busi;
    }

    public static class TryUpInfo4Busi {
        @Protobuf(fieldType = FieldType.BYTES, order = 1)
        public byte[] down_domain;

        @Protobuf(fieldType = FieldType.BYTES, order = 2)
        public byte[] thumb_down_url;

        @Protobuf(fieldType = FieldType.BYTES, order = 3)
        public byte[] original_down_url;

        @Protobuf(fieldType = FieldType.BYTES, order = 4)
        public byte[] big_down_url;

        @Protobuf(fieldType = FieldType.BYTES, order = 5)
        public byte[] file_resid;
    }


    public static class IPv6Info {
        @Protobuf(fieldType = FieldType.BYTES, order = 1)
        public byte[] ip6;

        @Protobuf(fieldType = FieldType.INT32, order = 2)
        public int port;
    }

    public static class ImgInfo {
        @Protobuf(fieldType = FieldType.BYTES, order = 1)
        public byte[] file_md5;

        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public long file_type;

        @Protobuf(fieldType = FieldType.INT64, order = 3)
        public long file_size;

        @Protobuf(fieldType = FieldType.INT64, order = 4)
        public long file_width;

        @Protobuf(fieldType = FieldType.INT64, order = 5)
        public long file_height;
    }
}
