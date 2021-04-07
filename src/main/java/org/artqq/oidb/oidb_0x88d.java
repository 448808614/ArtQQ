package org.artqq.oidb;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import java.io.IOException;

public class oidb_0x88d {
    public static class Req {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public Long appid;
        
        @Protobuf(fieldType = FieldType.OBJECT, order = 2)
        public ReqGroupInfo zreqgroupinfo;
        
        public byte[] toByteArray(){
            try {
                return ProtobufProxy.create(Req.class).encode(this);
            } catch (IOException e) {
                return new byte[0];
            }
        }
    }
    
    public static class ReqGroupInfo {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public Long group_code;
        
        @Protobuf(fieldType = FieldType.OBJECT, order = 2)
        public GroupInfo groupinfo;
        
        @Protobuf(fieldType = FieldType.INT32, order = 3)
        public Integer last_get_group_name_time;
    }
    
    
    public static class GroupInfo {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public Long group_owner;

        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public Long group_create_time;

        @Protobuf(fieldType = FieldType.INT32, order = 3)
        public Integer group_flag;

        @Protobuf(fieldType = FieldType.INT32, order = 4)
        public Integer group_flag_ext;

        @Protobuf(fieldType = FieldType.INT32, order = 5)
        public Integer group_member_max_num;

        @Protobuf(fieldType = FieldType.INT32, order = 6)
        public Integer group_member_num;

        @Protobuf(fieldType = FieldType.INT32, order = 7)
        public Integer group_option;

        @Protobuf(fieldType = FieldType.INT32, order = 8)
        public Integer group_class_ext;

        @Protobuf(fieldType = FieldType.INT32, order = 9)
        public Integer group_special_class;

        @Protobuf(fieldType = FieldType.INT32, order = 10)
        public Integer group_level;

        @Protobuf(fieldType = FieldType.INT32, order = 11)
        public Integer group_face;

        @Protobuf(fieldType = FieldType.INT32, order = 12)
        public Integer group_default_page;

        @Protobuf(fieldType = FieldType.INT32, order = 13)
        public Integer group_info_seq;

        @Protobuf(fieldType = FieldType.INT32, order = 14)
        public Integer group_roaming_time;

        @Protobuf(fieldType = FieldType.STRING, order = 15)
        public String group_name;

        @Protobuf(fieldType = FieldType.STRING, order = 16)
        public String group_memo;

        @Protobuf(fieldType = FieldType.STRING, order = 17)
        public String group_finger_memo;

        @Protobuf(fieldType = FieldType.STRING, order = 18)
        public String group_class_text;

        @Protobuf(fieldType = FieldType.INT32, order = 19)
        public Integer group_alliance_code;

        @Protobuf(fieldType = FieldType.INT32, order = 20)
        public Integer group_extra_adm_num;

        @Protobuf(fieldType = FieldType.INT64, order = 21)
        public Long group_uin;

        @Protobuf(fieldType = FieldType.INT32, order = 22)
        public Integer group_cur_msg_seq;

        @Protobuf(fieldType = FieldType.INT32, order = 23)
        public Integer group_last_msg_time;

        @Protobuf(fieldType = FieldType.STRING, order = 24)
        public String group_question;

        @Protobuf(fieldType = FieldType.STRING, order = 25)
        public String group_answer;

        @Protobuf(fieldType = FieldType.INT32, order = 26)
        public Integer group_visitor_max_num;

        @Protobuf(fieldType = FieldType.INT32, order = 27)
        public Integer group_visitor_cur_num;

        @Protobuf(fieldType = FieldType.INT32, order = 28)
        public Integer level_name_seq;

        @Protobuf(fieldType = FieldType.INT32, order = 29)
        public Integer group_admin_max_num;

        @Protobuf(fieldType = FieldType.INT32, order = 30)
        public Integer group_aio_skin_timestamp;

        @Protobuf(fieldType = FieldType.INT32, order = 31)
        public Integer group_board_skin_timestamp;

        @Protobuf(fieldType = FieldType.STRING, order = 32)
        public String group_aio_skin_url;

        @Protobuf(fieldType = FieldType.STRING, order = 33)
        public String group_board_skin_url;

        @Protobuf(fieldType = FieldType.INT32, order = 34)
        public Integer group_cover_skin_timestamp;

        @Protobuf(fieldType = FieldType.STRING, order = 35)
        public String group_cover_skin_url;

        @Protobuf(fieldType = FieldType.INT32, order = 36)
        public Integer group_grade;

        @Protobuf(fieldType = FieldType.INT32, order = 37)
        public Integer active_member_num;

        @Protobuf(fieldType = FieldType.INT32, order = 38)
        public Integer certification_type;

        @Protobuf(fieldType = FieldType.STRING, order = 39)
        public String certification_text;

        @Protobuf(fieldType = FieldType.STRING, order = 40)
        public String group_rich_finger_memo;

        @Protobuf(fieldType = FieldType.OBJECT, order = 41)
        public TagRecord tag_record;
        
        @Protobuf(fieldType = FieldType.OBJECT, order = 42)
        public GroupGeoInfo group_geo_info;
        
        @Protobuf(fieldType = FieldType.INT32, order = 43)
        public Integer head_portrait_seq;
        
        @Protobuf(fieldType = FieldType.OBJECT, order = 44)
        public GroupHeadPortrait msg_head_portrait;
        
        @Protobuf(fieldType = FieldType.INT32, order = 45)
        public Integer shutup_timestamp;
        
        @Protobuf(fieldType = FieldType.INT32, order = 46)
        public Integer shutup_timestamp_me;
        
        @Protobuf(fieldType = FieldType.INT32, order = 47)
        public Integer create_source_flag;
        
        @Protobuf(fieldType = FieldType.INT32, order = 48)
        public Integer cmduin_msg_seq;
        
        @Protobuf(fieldType = FieldType.INT32, order = 49)
        public Integer cmduin_join_time;
        
        @Protobuf(fieldType = FieldType.INT32, order = 50)
        public Integer cmduin_uin_flag;
        
        @Protobuf(fieldType = FieldType.INT32, order = 51)
        public Integer cmduin_flag_ex;
        
        @Protobuf(fieldType = FieldType.INT32, order = 52)
        public Integer cmduin_new_mobile_flag;
        
        @Protobuf(fieldType = FieldType.INT32, order = 53)
        public Integer cmduin_read_msg_seq;
        
        @Protobuf(fieldType = FieldType.INT32, order = 54)
        public Integer cmduin_last_msg_time;
        
        @Protobuf(fieldType = FieldType.INT32, order = 55)
        public Integer group_type_flag;
        
        @Protobuf(fieldType = FieldType.INT32, order = 56)
        public Integer app_privilege_flag;
        
        @Protobuf(fieldType = FieldType.OBJECT, order = 57)
        public GroupExInfoOnly group_ex_info;
        
        @Protobuf(fieldType = FieldType.INT32, order = 58)
        public Integer group_sec_level;
        
        @Protobuf(fieldType = FieldType.INT32, order = 59)
        public Integer group_sec_level_info;
        
        @Protobuf(fieldType = FieldType.INT32, order = 60)
        public Integer cmduin_privilege;
       
        @Protobuf(fieldType = FieldType.STRING, order = 61)
        public String poid_info;
        
        @Protobuf(fieldType = FieldType.INT32, order = 62)
        public Integer cmduin_flag_ex2;
        
        @Protobuf(fieldType = FieldType.INT64, order = 63)
        public Long conf_uin;
        
        @Protobuf(fieldType = FieldType.INT32, order = 64)
        public Integer conf_max_msg_seq;
        
        @Protobuf(fieldType = FieldType.INT32, order = 65)
        public Integer conf_to_group_time;
        
        @Protobuf(fieldType = FieldType.INT32, order = 66)
        public Integer password_redbag_time;
        
        @Protobuf(fieldType = FieldType.INT64, order = 67)
        public Long subscription_uin;
        
        @Protobuf(fieldType = FieldType.INT32, order = 68)
        public Integer member_list_change_seq;
        
        @Protobuf(fieldType = FieldType.INT32, order = 69)
        public Integer membercard_seq;
        
        @Protobuf(fieldType = FieldType.INT64, order = 70)
        public Long root_id;
        
        @Protobuf(fieldType = FieldType.INT64, order = 71)
        public Long parent_id;
        
        @Protobuf(fieldType = FieldType.INT32, order = 72)
        public Integer team_seq;
        
        @Protobuf(fieldType = FieldType.INT64, order = 73)
        public Long history_msg_begin_time;
        
        @Protobuf(fieldType = FieldType.INT64, order = 74)
        public Long invite_no_auth_num_limit;
        
        @Protobuf(fieldType = FieldType.INT32, order = 75)
        public Integer cmduin_history_msg_seq;
        
        @Protobuf(fieldType = FieldType.INT32, order = 76)
        public Integer cmduin_join_msg_seq;
        
        @Protobuf(fieldType = FieldType.INT32, order = 77)
        public Integer group_flagext3;
        
        @Protobuf(fieldType = FieldType.INT32, order = 78)
        public Integer group_open_appid;
        
        @Protobuf(fieldType = FieldType.INT32, order = 79)
        public Integer is_conf_group;
        
        @Protobuf(fieldType = FieldType.INT32, order = 80)
        public Integer is_modify_conf_group_face;
        
        @Protobuf(fieldType = FieldType.INT32, order = 81)
        public Integer is_modify_conf_group_name;
        
        @Protobuf(fieldType = FieldType.INT32, order = 82)
        public Integer no_finger_open_flag;
        
        @Protobuf(fieldType = FieldType.INT32, order = 83)
        public Integer no_code_finger_open_flag;
        
        @Protobuf(fieldType = FieldType.INT32, order = 84)
        public Integer auto_agree_join_group_user_num_for_normal_group;
        
        @Protobuf(fieldType = FieldType.INT32, order = 85)
        public Integer auto_agree_join_group_user_num_for_conf_group;
        
        @Protobuf(fieldType = FieldType.INT32, order = 86)
        public Integer is_allow_conf_group_member_nick;
        
        @Protobuf(fieldType = FieldType.INT32, order = 87)
        public Integer is_allow_conf_group_member_at_all;
        
        @Protobuf(fieldType = FieldType.INT32, order = 88)
        public Integer is_allow_conf_group_member_modify_group_name;
        
        @Protobuf(fieldType = FieldType.STRING, order = 89)
        public String Long_group_name;
        
        @Protobuf(fieldType = FieldType.INT32, order = 90)
        public Integer cmduin_join_real_msg_seq;
        
        @Protobuf(fieldType = FieldType.INT32, order = 91)
        public Integer is_group_freeze;
        
        @Protobuf(fieldType = FieldType.INT32, order = 92)
        public Integer msg_limit_frequency;
        
        @Protobuf(fieldType = FieldType.BYTES, order = 93)
        public byte[] join_group_auth;
        
        @Protobuf(fieldType = FieldType.INT32, order = 94)
        public Integer hl_guild_appid;
        
        @Protobuf(fieldType = FieldType.INT32, order = 95)
        public Integer hl_guild_sub_type;
        
        @Protobuf(fieldType = FieldType.INT32, order = 96)
        public Integer hl_guild_orgid;
        
        @Protobuf(fieldType = FieldType.INT32, order = 97)
        public Integer is_allow_hl_guild_binary;
        
        @Protobuf(fieldType = FieldType.INT32, order = 98)
        public Integer cmduin_ringtone_id;
        
        @Protobuf(fieldType = FieldType.INT32, order = 99)
        public Integer group_flagext4;
        
        @Protobuf(fieldType = FieldType.INT32, order = 100)
        public Integer group_freeze_reason;
        
        @Protobuf(fieldType = FieldType.INT32, order = 101)
        public Integer is_allow_recall_msg;
    }
    
    public static class TagRecord {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public Long from_uin;
       
        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public Long group_code;
        
        @Protobuf(fieldType = FieldType.BYTES, order = 3)
        public byte[] tag_id;
        
        @Protobuf(fieldType = FieldType.INT64, order = 4)
        public Long set_time;
        
        @Protobuf(fieldType = FieldType.INT32, order = 5)
        public Integer good_num;
        
        @Protobuf(fieldType = FieldType.INT32, order = 6)
        public Integer bad_num;
        
        @Protobuf(fieldType = FieldType.INT32, order = 7)
        public Integer tag_len;
        
        @Protobuf(fieldType = FieldType.BYTES, order = 3)
        public byte[] tag_value;
    }
    
    public static class GroupGeoInfo {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public Long owneruin;
        
        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public Long set_time;
        
        @Protobuf(fieldType = FieldType.INT32, order = 3)
        public Integer cityId;
        
        @Protobuf(fieldType = FieldType.INT64, order = 4)
        public Long Longitude;
        
        @Protobuf(fieldType = FieldType.INT64, order = 5)
        public Long latitude;
        
        @Protobuf(fieldType = FieldType.BYTES, order = 6)
        public byte[] geocontent;
        
        @Protobuf(fieldType = FieldType.INT64, order = 7)
        public Long poi_id;
    }
    
    public static class GroupHeadPortrait {
        @Protobuf(fieldType = FieldType.INT32, order = 1)
        public Integer pic_cnt;
        
        @Protobuf(fieldType = FieldType.OBJECT, order = 2)
        public GroupHeadPortraitInfo msg_info;
        
        @Protobuf(fieldType = FieldType.INT32, order = 3)
        public Integer default_id;
        
        @Protobuf(fieldType = FieldType.INT32, order = 4)
        public Integer verifying_pic_cnt;
        
        @Protobuf(fieldType = FieldType.OBJECT, order = 5)
        public GroupHeadPortraitInfo msg_verifyingpic_info;
    }
    
    public static class GroupHeadPortraitInfo {
        @Protobuf(fieldType = FieldType.INT32, order = 1)
        public Integer pic_id;
        
        @Protobuf(fieldType = FieldType.INT32, order = 2)
        public Integer left_x;
        
        @Protobuf(fieldType = FieldType.INT32, order = 3)
        public Integer left_y;
        
        @Protobuf(fieldType = FieldType.INT32, order = 4)
        public Integer right_y;
        
        @Protobuf(fieldType = FieldType.INT32, order = 5)
        public Integer right_x;
    }
    
    public static class GroupExInfoOnly {
        @Protobuf(fieldType = FieldType.INT32, order = 1)
        public Integer tribe_id;
        
        @Protobuf(fieldType = FieldType.INT32, order = 2)
        public Integer money_for_add_group;
    }
}



