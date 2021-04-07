package org.artqq.protobuf;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import java.io.IOException;

public class msg_comm {
    public static class Msg {
        @Protobuf(fieldType = FieldType.OBJECT, order = 1)
        public MsgHead msg_head;

        @Protobuf(fieldType = FieldType.OBJECT, order = 2)
        public MessageSvc.ContentHead content_head;

        @Protobuf(fieldType = FieldType.OBJECT, order = 3)
        public MessageSvc.MsgBody msg_body;

        @Protobuf(fieldType = FieldType.OBJECT, order = 4)
        public AppShareInfo appshare_info;
        
        public byte[] toByteArray(){
            try {
                return ProtobufProxy.create(Msg.class).encode(this);
            } catch (IOException e) {
                return null;
            }
        }
    }
    
    public static class AppShareInfo {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public Long appshare_id;
        
        @Protobuf(fieldType = FieldType.BYTES, order = 2)
        public Byte[] appshare_cookie;
        
        @Protobuf(fieldType = FieldType.OBJECT, order = 3)
        public PluginInfo appshare_resource;
        
        public static class PluginInfo {
            @Protobuf(fieldType = FieldType.INT64, order = 1)
            public Long res_id;
            
            @Protobuf(fieldType = FieldType.STRING, order = 2)
            public String pkg_name;
            
            @Protobuf(fieldType = FieldType.INT64, order = 3)
            public Long new_ver;
            
            @Protobuf(fieldType = FieldType.INT64, order = 4)
            public Long res_type;
            
            @Protobuf(fieldType = FieldType.INT64, order = 5)
            public Long lan_type;
            
            @Protobuf(fieldType = FieldType.INT64, order = 6)
            public Long priority;
            
            @Protobuf(fieldType = FieldType.STRING, order = 7)
            public String res_name;
            
            @Protobuf(fieldType = FieldType.STRING, order = 8)
            public String res_desc;
            
            @Protobuf(fieldType = FieldType.STRING, order = 9)
            public String res_url_big;
            
            @Protobuf(fieldType = FieldType.STRING, order = 10)
            public String res_url_small;
            
            @Protobuf(fieldType = FieldType.STRING, order = 11)
            public String res_conf;
        }
    }

    public static class MsgHead {
        /*
         21 _ msg_inst_ctrl
         23 _ wseq_in_c2c_msghead
         25 _ ext_group_key_info
         */
        @Protobuf(fieldType = FieldType.UINT64, order = 1)
        public Long from_uin;

        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public Long to_uin;

        @Protobuf(fieldType = FieldType.UINT32, order = 3)
        public int msg_type;

        @Protobuf(fieldType = FieldType.INT64, order = 4)
        public Long c2c_cmd;

        @Protobuf(fieldType = FieldType.INT32, order = 5)
        public int msg_seq;
        // msgbar

        @Protobuf(fieldType = FieldType.UINT32, order = 6)
        public int msg_time;

        @Protobuf(fieldType = FieldType.INT64, order = 7)
        public Long msg_uid;

        @Protobuf(fieldType = FieldType.OBJECT, order = 8)
        public C2CTmpMsgHead c2c_tmp_msg_head;

        @Protobuf(fieldType = FieldType.OBJECT, order = 9)
        public GroupInfo group_info = null;

        @Protobuf(fieldType = FieldType.INT64, order = 10)
        public Long from_appid;

        @Protobuf(fieldType = FieldType.INT64, order = 11)
        public Long from_instid;

        @Protobuf(fieldType = FieldType.INT64, order = 12)
        public Long user_active;

        @Protobuf(fieldType = FieldType.OBJECT, order = 13)
        public DiscussInfo discuss_info = null;

        @Protobuf(fieldType = FieldType.STRING, order = 14)
        public String from_nick;

        @Protobuf(fieldType = FieldType.INT64, order = 15)
        public Long auth_uin;

        @Protobuf(fieldType = FieldType.STRING, order = 16)
        public String auth_nick;

        @Protobuf(fieldType = FieldType.INT64, order = 17)
        public Long msg_flag;

        @Protobuf(fieldType = FieldType.STRING, order = 18)
        public String auth_remark;

        @Protobuf(fieldType = FieldType.STRING, order = 19)
        public String group_name;

        @Protobuf(fieldType = FieldType.OBJECT, order = 20)
        public MutilTransHead mutiltrans_head;

        @Protobuf(fieldType = FieldType.INT64, order = 22)
        public Long public_account_group_send_flag;

        @Protobuf(fieldType = FieldType.INT64, order = 23)
        public Long cpid;

        @Protobuf(fieldType = FieldType.STRING, order = 26)
        public String multi_compatible_text;

        @Protobuf(fieldType = FieldType.INT64, order = 27)
        public Long auth_sex;

        @Protobuf(fieldType = FieldType.BOOL, order = 28)
        public boolean is_src_msg = false;

        @Override
        public String toString() {
            return "MsgHead{" +
                "from_uin=" + from_uin +
                ", to_uin=" + to_uin +
                ", msg_type=" + msg_type +
                ", c2c_cmd=" + c2c_cmd +
                ", msg_seq=" + msg_seq +
                ", msg_time=" + msg_time +
                ", msg_uid=" + msg_uid +
                '}';
        }
    }

    public static class MutilTransHead {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public Long status;

        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public Long msgId;
    }

    public static class C2CTmpMsgHead {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public Long c2cType;

        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public Long sType;

        @Protobuf(fieldType = FieldType.INT64, order = 3)
        public Long groupUin;

        @Protobuf(fieldType = FieldType.INT64, order = 4)
        public Long groupCode;

        @Protobuf(fieldType = FieldType.STRING, order = 5)
        public String sig;
    }

    public static class DiscussInfo {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public Long discuss_uin;

        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public Long discuss_type;

        @Protobuf(fieldType = FieldType.INT64, order = 3)
        public Long discuss_info_seq;

        @Protobuf(fieldType = FieldType.STRING, order = 4)
        public String discuss_remark;

        @Protobuf(fieldType = FieldType.STRING, order = 5)
        public String discuss_name;

    }

    public static class GroupInfo {
        @Protobuf(fieldType = FieldType.UINT64, order = 1)
        public Long group_code;

        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public Long group_type;

        @Protobuf(fieldType = FieldType.INT64, order = 3)
        public Long group_info_seq;

        @Protobuf(fieldType = FieldType.STRING, order = 4)
        public String group_card;

        @Protobuf(fieldType = FieldType.STRING, order = 5)
        public String group_rank;

        @Protobuf(fieldType = FieldType.INT64, order = 6)
        public Long group_level;

        @Protobuf(fieldType = FieldType.INT64, order = 7)
        public Long group_card_type;

        @Protobuf(fieldType = FieldType.STRING, order = 8)
        public String group_name;

    }
}

