package org.artqq.protobuf;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import java.io.IOException;

public class group_member_info {
    public static class Req {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public long group_code;

        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public long uin;

        @Protobuf(fieldType = FieldType.BOOL, order = 3)
        public boolean new_client;

        @Protobuf(fieldType = FieldType.INT32, order = 4)
        public int client_type;

        @Protobuf(fieldType = FieldType.INT32, order = 5)
        public int rich_card_name_ver;

        public byte[] toByteArray(){
            try {
                return ProtobufProxy.create(Req.class).encode(this);
            } catch (IOException e) {
                return new byte[0];
            }
        }
        
    }

    public static class Resp {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public long group_code;

        @Protobuf(fieldType = FieldType.OBJECT, order = 3)
        public MemberInfo msg_meminfo;

        @Protobuf(fieldType = FieldType.BOOL, order = 4)
        public boolean self_location_shared;

        @Protobuf(fieldType = FieldType.INT32, order = 2)
        public int group_type;
        
        public static Resp decode(byte[] src){
            try {
                return ProtobufProxy.create(Resp.class).decode(src);
            } catch (IOException e) {
                return null;
            }
        }
    }


    public static class MemberInfo {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public long uin;

        @Protobuf(fieldType = FieldType.INT32, order = 2)
        public Integer result = null;

        @Protobuf(fieldType = FieldType.STRING, order = 3)
        public String errmsg;

        @Protobuf(fieldType = FieldType.BOOL, order = 4)
        public boolean is_friend;

        @Protobuf(fieldType = FieldType.STRING, order = 5)
        public String remark;

        @Protobuf(fieldType = FieldType.BOOL, order = 6)
        public boolean is_concerned = false;

        @Protobuf(fieldType = FieldType.INT32, order = 7)
        public int credit = 0;

        @Protobuf(fieldType = FieldType.STRING, order = 8)
        public String card = "";

        @Protobuf(fieldType = FieldType.INT32, order = 9)
        public int sex = 0;

        @Protobuf(fieldType = FieldType.STRING, order = 10)
        public String location = "";

        @Protobuf(fieldType = FieldType.STRING, order = 11)
        public String nick = null;

        @Protobuf(fieldType = FieldType.INT32, order = 12)
        public int age = 0;

        @Protobuf(fieldType = FieldType.STRING, order = 13)
        public String lev = "";

        @Protobuf(fieldType = FieldType.INT64, order = 14)
        public long join = 0;

        @Protobuf(fieldType = FieldType.INT64, order = 15)
        public long last_speak = 0;

        @Protobuf(fieldType = FieldType.OBJECT, order = 16)
        public CustomEntry msg_custom_enties;

        @Protobuf(fieldType = FieldType.OBJECT, order = 17)
        public GBarInfo msg_gbar_concerned;

        @Protobuf(fieldType = FieldType.STRING, order = 18)
        public String gbar_title;

        @Protobuf(fieldType = FieldType.STRING, order = 19)
        public String gbar_url;

        @Protobuf(fieldType = FieldType.INT32, order = 20)
        public int gbar_cnt;

        @Protobuf(fieldType = FieldType.BOOL, order = 21)
        public boolean is_allow_mod_card;

        @Protobuf(fieldType = FieldType.BOOL, order = 22)
        public boolean is_vip;

        @Protobuf(fieldType = FieldType.BOOL, order = 23)
        public boolean is_year_vip;

        @Protobuf(fieldType = FieldType.BOOL, order = 24)
        public boolean is_super_vip;

        @Protobuf(fieldType = FieldType.BOOL, order = 25)
        public boolean is_super_qq;

        @Protobuf(fieldType = FieldType.INT32, order = 26)
        public int vip_lev;

        @Protobuf(fieldType = FieldType.INT32, order = 27)
        public int role;

        @Protobuf(fieldType = FieldType.BOOL, order = 28)
        public boolean location_shared;

        @Protobuf(fieldType = FieldType.INT64, order = 29)
        public long distance;

        @Protobuf(fieldType = FieldType.INT32, order = 30)
        public int concern_type = -1;

        @Protobuf(fieldType = FieldType.BYTES, order = 31)
        public byte[] special_title = new byte[0];

        @Protobuf(fieldType = FieldType.INT32, order = 32)
        public int special_title_expire_time;

        @Protobuf(fieldType = FieldType.OBJECT, order = 33)
        public FlowersEntry msg_flower_entry;

        @Protobuf(fieldType = FieldType.OBJECT, order = 34)
        public TeamEntry msg_team_entry;

        @Protobuf(fieldType = FieldType.BYTES, order = 35)
        public byte[] phone_num = new byte[0];

        @Protobuf(fieldType = FieldType.BYTES, order = 36)
        public byte[] job = new byte[0];

        @Protobuf(fieldType = FieldType.INT32, order = 37)
        public int medal_id;

        @Protobuf(fieldType = FieldType.OBJECT, order = 38)
        public RspGroupCardGetStory qqstory_infocard;

        @Protobuf(fieldType = FieldType.INT32, order = 39)
        public int level = 0;

        @Protobuf(fieldType = FieldType.OBJECT, order = 40)
        public MemberGameInfo msg_game_info;

        @Protobuf(fieldType = FieldType.BYTES, order = 41)
        public byte[] group_honor;
    }

    public static class CustomEntry {
        @Protobuf(fieldType = FieldType.STRING, order = 1)
        public String name;

        @Protobuf(fieldType = FieldType.STRING, order = 2)
        public String value;

        @Protobuf(fieldType = FieldType.BOOL, order = 3)
        public boolean clicked;

        @Protobuf(fieldType = FieldType.STRING, order = 4)
        public String url;

        @Protobuf(fieldType = FieldType.INT64, order = 5)
        public long report_id;
    }

    public static class GBarInfo {
        @Protobuf(fieldType = FieldType.INT32, order = 1)
        public int gbar_id;

        @Protobuf(fieldType = FieldType.INT32, order = 2)
        public int uin_lev;

        @Protobuf(fieldType = FieldType.STRING, order = 3)
        public String head_portrait;

        @Protobuf(fieldType = FieldType.BYTES, order = 4)
        public byte[] gbar_name;

    }

    public static class FlowersEntry {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public long flower_count = 0;
    }

    public static class TeamEntry {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public Long depid;

        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public Long self_depid;

    }

    public static class RspGroupCardGetStory{
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public long result;

        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public long flag;

        @Protobuf(fieldType = FieldType.OBJECT, order = 3)
        public InfoCardVideoInfo info;

    }

    public static class InfoCardVideoInfo {
        @Protobuf(fieldType = FieldType.INT32, order = 1)
        public int cover;

        @Protobuf(fieldType = FieldType.INT32, order = 2)
        public int feed_id;

        @Protobuf(fieldType = FieldType.INT32, order = 3)
        public int vid;

    }

    public static class MemberGameInfo {
        @Protobuf(fieldType = FieldType.STRING, order = 1)
        public String game_name;

        @Protobuf(fieldType = FieldType.STRING, order = 2)
        public String level_name;

        @Protobuf(fieldType = FieldType.STRING, order = 3)
        public String level_icon;

        @Protobuf(fieldType = FieldType.STRING, order = 4)
        public String game_font_color;

        @Protobuf(fieldType = FieldType.STRING, order = 5)
        public String game_background_color;

        @Protobuf(fieldType = FieldType.STRING, order = 6)
        public String game_url;

        @Protobuf(fieldType = FieldType.STRING, order = 7)
        public String desc_info;
    }
}
