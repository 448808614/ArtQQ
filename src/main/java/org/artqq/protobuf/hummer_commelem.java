package org.artqq.protobuf;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import java.io.IOException;
import org.artqq.utils.Tools;
import com.google.gson.JsonObject;
import org.artqq.utils.zLibUtils;
import org.artqq.utils.bytes.ByteObject;

public class hummer_commelem {
    // 群视频
    public static class MsgElemInfo_servtype27 {
        @Protobuf(fieldType = FieldType.OBJECT, order = 1)
        public MessageSvc.MsgBody.RichText.Elem.VideoFile video_file;
    }

    // 闪图
    public static class MsgElemInfo_servtype3 {
        @Protobuf(fieldType = FieldType.OBJECT, order = 1)
        public MessageSvc.MsgBody.RichText.Elem.CustomFace flash_troop_pic;

        @Protobuf(fieldType = FieldType.OBJECT, order = 2)
        public MessageSvc.MsgBody.RichText.Elem.NotOnlineImage flash_c2c_pic;
        
        public byte[] toByteArray(){
            try {
                return ProtobufProxy.create(MsgElemInfo_servtype3.class).encode(this);
            } catch (IOException e) {
                System.out.println(e);
            }
            return null;
        }

        public static MsgElemInfo_servtype3 decode(byte[] data){
            try {
                return ProtobufProxy.create(MsgElemInfo_servtype3.class).decode(data);
            } catch (IOException e) {
                System.out.println(e);
            }
            return null;
        }
    }

    // 戳一戳等
    public static class MsgElemInfo_servtype2 {
        @Protobuf(fieldType = FieldType.UINT32, order = 1)
        public int poke_type;

        @Protobuf(fieldType = FieldType.BYTES, order = 2)
        public byte[] poke_summary;

        @Protobuf(fieldType = FieldType.UINT32, order = 3)
        public int double_hit;

        @Protobuf(fieldType = FieldType.UINT32, order = 4)
        public int vaspoke_id;

        @Protobuf(fieldType = FieldType.BYTES, order = 5)
        public byte[] vaspoke_name;

        @Protobuf(fieldType = FieldType.BYTES, order = 6)
        public byte[] vaspoke_minver;

        @Protobuf(fieldType = FieldType.UINT32, order = 7)
        public int poke_strength;

        @Protobuf(fieldType = FieldType.UINT32, order = 8)
        public int msg_type;

        @Protobuf(fieldType = FieldType.UINT32, order = 9)
        public int face_bubble_count;

        @Protobuf(fieldType = FieldType.UINT32, order = 10)
        public int poke_flag;

        public static MsgElemInfo_servtype2 decode(byte[] data){
            try {
                return ProtobufProxy.create(MsgElemInfo_servtype2.class).decode(data);
            } catch (IOException e) {
                System.out.println(e);
            }
            return null;
        }
    }

    // 连续炸弹*xxx
    public static class MsgElemInfo_servtype23 {
        @Protobuf(fieldType = FieldType.UINT32, order = 1)
        public int face_type;

        @Protobuf(fieldType = FieldType.UINT32, order = 2)
        public int face_bubble_count;

        @Protobuf(fieldType = FieldType.BYTES, order = 3)
        public byte[] face_summary;

        @Protobuf(fieldType = FieldType.UINT32, order = 4)
        public int flag;

        @Protobuf(fieldType = FieldType.BYTES, order = 5)
        public byte[] others;
        
        public static MsgElemInfo_servtype23 decode(byte[] data){
            try {
                return ProtobufProxy.create(MsgElemInfo_servtype23.class).decode(data);
            } catch (IOException e) {
                System.out.println(e);
            }
            return null;
        }
    }

    // 闪字
    public static class MsgElemInfo_servtype14 {
        @Protobuf(fieldType = FieldType.INT32, order = 1)
        public int id;

        @Protobuf(fieldType = FieldType.BYTES, order = 2)
        public byte[] reserve_Info;

        public static MsgElemInfo_servtype14 decode(byte[] data){
            try {
                return ProtobufProxy.create(MsgElemInfo_servtype14.class).decode(data);
            } catch (IOException e) {
                System.out.println(e);
            }
            return null;
        }
        
        public String getText(){
            ByteObject bo = new ByteObject(reserve_Info);
            byte type = bo.readByte();
            byte[] source = null;
            switch (type){
                case 1 :
                    source = zLibUtils.uncompress( bo.readRestBytes() );
                    break;
                case 0:
                    source = bo.readRestBytes();
                    break;
            }
            String json = new String(source);
            JsonObject main = Tools.parseJsonObject(json);
            return main.get("prompt").getAsString();
        }
        
        public void setInfo(String text){
            JsonObject main = new JsonObject();
            main.addProperty("a", "com.tencent.randomwords");
            main.addProperty("desc", "随机字");
            main.addProperty("resid", id);
            main.addProperty("m", "main");
            main.addProperty("v", "1.0.0.16");
            main.addProperty("prompt", text);
            byte[] zip = zLibUtils.compress(main.toString().getBytes());
            ByteObject o = new ByteObject();
            o.WriteByte(1);
            o.WriteBytes(zip);
            reserve_Info = o.toByteArray();
        }
    }

    // QQ新版表情
    public static class MsgElemInfo_servtype33 {
        @Protobuf(fieldType = FieldType.INT32, order = 1)
        public int index;

        @Protobuf(fieldType = FieldType.BYTES, order = 2)
        public byte[] text;

        @Protobuf(fieldType = FieldType.BYTES, order = 3)
        public byte[] compat;

        @Protobuf(fieldType = FieldType.BYTES, order = 4)
        public byte[] buf;

        public byte[] toByteArray(){
            try {
                return ProtobufProxy.create(MsgElemInfo_servtype33.class).encode(this);
            } catch (IOException e) {
                System.out.println(e);
            }
            return null;
        }

        public static MsgElemInfo_servtype33 decode(byte[] data){
            try {
                return ProtobufProxy.create(MsgElemInfo_servtype33.class).decode(data);
            } catch (IOException e) {
                System.out.println(e);
            }
            return null;
        }
    }

}

