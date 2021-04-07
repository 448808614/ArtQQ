package org.artqq.oidb;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import java.io.IOException;

public class oidb_0x352 {
    public static class Req {
        @Protobuf(fieldType = FieldType.INT32, order = 1)
        public int subcmd = 1;

        @Protobuf(fieldType = FieldType.OBJECT, order = 2)
        public TryUpImgReq msg_tryup_img_req;

        /*
         @Protobuf(fieldType = FieldType.OBJECT, order = 3)
         public Object rpt_msg_getimg_url_req;

         @Protobuf(fieldType = FieldType.OBJECT, order = 4)
         public Object rpt_msg_del_img_req;
         */

        @Protobuf(fieldType = FieldType.INT32, order = 10)
        public int net_type = 3;

        public byte[] toByteArray(){
            try {
                return ProtobufProxy.create(Req.class).encode(this);
            } catch (IOException e) {
                System.out.println(e);
            }
            return null;
        }

    }

    public static class Resp {
        @Protobuf(fieldType = FieldType.INT32, order = 1)
        public int subcmd;

        @Protobuf(fieldType = FieldType.OBJECT, order = 2)
        public TryUpImgRsp msg_tryup_img_rsp;

        //@Protobuf(fieldType = FieldType.OBJECT, order = 3)
        //public Object rpt_msg_getimg_url_rsp;

        @Protobuf(fieldType = FieldType.BOOL, order = 4)
        public boolean new_bigchan;

        //@Protobuf(fieldType = FieldType.OBJECT, order = 5)
        //public Object rpt_msg_del_img_rsp;

        @Protobuf(fieldType = FieldType.BYTES, order = 10)
        public byte[] fail_msg;

        public static Resp decode(byte[] src){
            try {
                return ProtobufProxy.create(Resp.class).decode(src);
            } catch (IOException e) {}
            return null;
        }
    }

    public static class TryUpImgRsp {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public long file_id;

        @Protobuf(fieldType = FieldType.INT32, order = 2)
        public int client_ip;

        @Protobuf(fieldType = FieldType.INT32, order = 3)
        public int result;

        @Protobuf(fieldType = FieldType.BYTES, order = 4)
        public byte[] fail_msg;

        @Protobuf(fieldType = FieldType.BOOL, order = 5)
        public boolean file_exit;

        @Protobuf(fieldType = FieldType.OBJECT, order = 6)
        public ImgInfo msg_img_info;

        @Protobuf(fieldType = FieldType.INT32, order = 7)
        public Integer up_ip;

        @Protobuf(fieldType = FieldType.INT32, order = 8)
        public Integer up_port = 8080;

        @Protobuf(fieldType = FieldType.BYTES, order = 9)
        public byte[] up_ukey;

        @Protobuf(fieldType = FieldType.BYTES, order = 10)
        public byte[] up_resid;

        @Protobuf(fieldType = FieldType.BYTES, order = 11)
        public byte[] up_uuid;

        @Protobuf(fieldType = FieldType.INT64, order = 12)
        public long up_offset;

        @Protobuf(fieldType = FieldType.INT64, order = 13)
        public long block_size;

        @Protobuf(fieldType = FieldType.BYTES, order = 14)
        public byte[] encrypt_dstip;

        @Protobuf(fieldType = FieldType.INT32, order = 15)
        public int roamdays;

        @Protobuf(fieldType = FieldType.OBJECT, order = 26)
        public IPv6Info msg_up_ip6;

        @Protobuf(fieldType = FieldType.BYTES, order = 27)
        public byte[] client_ip6;

        @Protobuf(fieldType = FieldType.BYTES, order = 60)
        public byte[] thumb_down_para;

        @Protobuf(fieldType = FieldType.BYTES, order = 61)
        public byte[] original_down_para;

        @Protobuf(fieldType = FieldType.BYTES, order = 62)
        public byte[] down_domain;

        @Protobuf(fieldType = FieldType.BYTES, order = 64)
        public byte[] big_down_para;

        @Protobuf(fieldType = FieldType.BYTES, order = 65)
        public byte[] big_thumb_down_para;

        @Protobuf(fieldType = FieldType.INT32, order = 66)
        public int https_url_flag;

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


    public static class TryUpImgReq {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public long src_uin;

        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public long dst_uin;

        @Protobuf(fieldType = FieldType.INT64, order = 3)
        public long file_id = 0;

        @Protobuf(fieldType = FieldType.BYTES, order = 4)
        public byte[] file_md5;

        @Protobuf(fieldType = FieldType.INT64, order = 5)
        public long file_size;

        @Protobuf(fieldType = FieldType.BYTES, order = 6)
        public byte[] file_name;

        @Protobuf(fieldType = FieldType.INT32, order = 7)
        public int src_term = 5;

        @Protobuf(fieldType = FieldType.INT32, order = 8)
        public int platform_type = 9;

        @Protobuf(fieldType = FieldType.INT32, order = 9)
        public int inner_ip;

        @Protobuf(fieldType = FieldType.BOOL, order = 10)
        public boolean address_book = false;

        @Protobuf(fieldType = FieldType.INT32, order = 11)
        public int retry;

        @Protobuf(fieldType = FieldType.INT32, order = 12)
        public int bu_type = 1;

        @Protobuf(fieldType = FieldType.BOOL, order = 13)
        public boolean pic_original = false;

        @Protobuf(fieldType = FieldType.INT32, order = 14)
        public int pic_width = 100;

        @Protobuf(fieldType = FieldType.INT32, order = 15)
        public int pic_height = 100;

        @Protobuf(fieldType = FieldType.INT32, order = 16)
        public int pic_type = 1000;

        @Protobuf(fieldType = FieldType.BYTES, order = 17)
        public byte[] build_ver;

        @Protobuf(fieldType = FieldType.BYTES, order = 18)
        public byte[] file_index;

        @Protobuf(fieldType = FieldType.INT32, order = 19)
        public int store_days;

        @Protobuf(fieldType = FieldType.INT32, order = 20)
        public int tryup_stepflag;

        @Protobuf(fieldType = FieldType.BOOL, order = 21)
        public boolean reject_tryfast = false;

        @Protobuf(fieldType = FieldType.INT32, order = 22)
        public int srv_upload = 0;

        @Protobuf(fieldType = FieldType.BYTES, order = 23)
        public byte[] transfer_url;
    }
}
