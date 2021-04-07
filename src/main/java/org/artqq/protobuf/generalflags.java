package org.artqq.protobuf;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class generalflags {
    public static class ResvAttr {
        @Protobuf(fieldType = FieldType.UINT32, order = 1)
        public Integer global_group_level;

        @Protobuf(fieldType = FieldType.UINT32, order = 2)
        public Integer nearby_charm_level;

        @Protobuf(fieldType = FieldType.UINT64, order = 3)
        public Long redbag_msg_sender_uin;

        @Protobuf(fieldType = FieldType.UINT32, order = 4)
        public Integer title_id;

        @Protobuf(fieldType = FieldType.UINT32, order = 5)
        public Integer robot_msg_flag;

        @Protobuf(fieldType = FieldType.UINT64, order = 6)
        public Long want_gift_sender_uin;

        @Protobuf(fieldType = FieldType.FLOAT, order = 7)
        public Float sticker_x;

        @Protobuf(fieldType = FieldType.FLOAT, order = 8)
        public Float sticker_y;

        @Protobuf(fieldType = FieldType.FLOAT, order = 9)
        public Float sticker_width;

        @Protobuf(fieldType = FieldType.FLOAT, order = 10)
        public Float sticker_height;

        @Protobuf(fieldType = FieldType.UINT32, order = 11)
        public Integer sticker_rotate;

        @Protobuf(fieldType = FieldType.UINT64, order = 12)
        public Long sticker_host_msgseq;

        @Protobuf(fieldType = FieldType.UINT64, order = 13)
        public Long sticker_host_msguid;

        @Protobuf(fieldType = FieldType.UINT64, order = 14)
        public Long sticker_host_time;

        @Protobuf(fieldType = FieldType.UINT32, order = 15)
        public Integer mobile_custom_font = 65536;

        @Protobuf(fieldType = FieldType.UINT32, order = 16)
        public Integer tail_key;

        @Protobuf(fieldType = FieldType.UINT32, order = 17)
        public Integer show_tail_flag;

        @Protobuf(fieldType = FieldType.UINT32, order = 18)
        public Integer doutu_msg_type;

        @Protobuf(fieldType = FieldType.UINT32, order = 19)
        public Integer doutu_combo;

        @Protobuf(fieldType = FieldType.UINT32, order = 20)
        public Integer custom_featureid;

        @Protobuf(fieldType = FieldType.UINT32, order = 21)
        public Integer golden_msg_type;

        @Protobuf(fieldType = FieldType.BYTES, order = 22)
        public byte[] golden_msg_info;

        @Protobuf(fieldType = FieldType.UINT32, order = 23)
        public Integer bot_message_class_id;

        @Protobuf(fieldType = FieldType.BYTES, order = 24)
        public byte[] subscription_url;

        @Protobuf(fieldType = FieldType.UINT32, order = 25)
        public Integer pendant_diy_id;

        @Protobuf(fieldType = FieldType.UINT32, order = 26)
        public Integer timed_message;

        @Protobuf(fieldType = FieldType.UINT32, order = 27)
        public Integer holiday_flag;

        @Protobuf(fieldType = FieldType.BYTES, order = 29)
        public byte[] kpl_info;

        @Protobuf(fieldType = FieldType.UINT32, order = 30)
        public Integer face_id;

        @Protobuf(fieldType = FieldType.UINT32, order = 31)
        public Integer diy_font_timestamp;

        @Protobuf(fieldType = FieldType.UINT32, order = 32)
        public Integer red_envelope_type;

        @Protobuf(fieldType = FieldType.BYTES, order = 33)
        public byte[] shortVideoId;

        @Protobuf(fieldType = FieldType.UINT32, order = 34)
        public int req_font_effect_id;

        @Protobuf(fieldType = FieldType.UINT32, order = 35)
        public int love_language_flag;

        @Protobuf(fieldType = FieldType.UINT32, order = 36)
        public int aio_sync_to_story_flag;

        @Protobuf(fieldType = FieldType.UINT32, order = 37)
        public int upload_image_to_qzone_flag;

        @Protobuf(fieldType = FieldType.BYTES, order = 39)
        public byte[] upload_image_to_qzone_param;

        @Protobuf(fieldType = FieldType.BYTES, order = 40)
        public byte[] group_confess_sig;

        @Protobuf(fieldType = FieldType.UINT64, order = 41)
        public Long subfont_id;

        @Protobuf(fieldType = FieldType.UINT32, order = 42)
        public int msg_flag_type;

        @Protobuf(fieldType = FieldType.INT32, order = 43)
        public Integer rpt_custom_featureid;

        @Protobuf(fieldType = FieldType.UINT32, order = 44)
        public int rich_card_name_ver;

        @Protobuf(fieldType = FieldType.UINT32, order = 47)
        public int msg_info_flag;

        @Protobuf(fieldType = FieldType.UINT32, order = 48)
        public int service_msg_type;

        @Protobuf(fieldType = FieldType.UINT32, order = 49)
        public int service_msg_remind_type;

        @Protobuf(fieldType = FieldType.BYTES, order = 50)
        public byte[] service_msg_name;

        @Protobuf(fieldType = FieldType.UINT32, order = 51)
        public int vip_type = 272;

        @Protobuf(fieldType = FieldType.UINT32, order = 52)
        public int vip_level = 8;

        @Protobuf(fieldType = FieldType.OBJECT, order = 53)
        public PttWaveForm ptt_waveform;

        @Protobuf(fieldType = FieldType.UINT32, order = 54)
        public int user_bigclub_level;

        @Protobuf(fieldType = FieldType.UINT32, order = 55)
        public int user_bigclub_flag;

        @Protobuf(fieldType = FieldType.UINT32, order = 56)
        public int nameplate;

        @Protobuf(fieldType = FieldType.UINT32, order = 57)
        public int auto_reply;

        @Protobuf(fieldType = FieldType.UINT32, order = 58)
        public int req_is_bigclub_hidden;

        @Protobuf(fieldType = FieldType.UINT32, order = 59)
        public int show_in_msg_list;

        @Protobuf(fieldType = FieldType.BYTES, order = 60)
        public byte[] oac_msg_extend;

        @Protobuf(fieldType = FieldType.UINT32, order = 61)
        public int group_member_flag_ex2;

        @Protobuf(fieldType = FieldType.UINT32, order = 62)
        public int group_ringtone_id;

        @Protobuf(fieldType = FieldType.BYTES, order = 63)
        public byte[] robot_general_trans;

        @Protobuf(fieldType = FieldType.UINT32, order = 64)
        public int troop_pobing_template;

        @Protobuf(fieldType = FieldType.OBJECT, order = 65)
        public Honor hudong_mark;

        @Protobuf(fieldType = FieldType.UINT32, order = 66)
        public int group_info_flag_ex3;

        @Protobuf(fieldType = FieldType.UINT32, order = 67)
        public int comment_flag;

        @Protobuf(fieldType = FieldType.UINT64, order = 68)
        public long comment_location;

        @Protobuf(fieldType = FieldType.BYTES, order = 69)
        public byte[] pass_through;

        @Protobuf(fieldType = FieldType.UINT32, order = 70)
        public int group_savedb_flag;

        @Protobuf(fieldType = FieldType.UINT32, order = 71)
        public int nameplate_vip_type = 2;

        @Protobuf(fieldType = FieldType.UINT32, order = 72)
        public int gray_name_plate;

        @Protobuf(fieldType = FieldType.BYTES, order = 73)
        public byte[] user_vip_info;

        public static class PttWaveForm {
            @Protobuf(fieldType = FieldType.UINT32, order = 1)
            public int size;

            @Protobuf(fieldType = FieldType.BYTES, order = 2)
            public byte[] amplitudes;
        }

        public static class Honor {
            @Protobuf(fieldType = FieldType.INT32, order = 1)
            public List<Integer> id = new ArrayList<Integer>();

            @Protobuf(fieldType = FieldType.INT32, order = 2)
            public int level;

            public static Honor decode(byte[] src){
                try {
                    return ProtobufProxy.create(Honor.class).decode(src);
                } catch(IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
    }
}

