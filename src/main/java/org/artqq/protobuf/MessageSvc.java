package org.artqq.protobuf;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.artqq.MD5;
import org.artqq.utils.BytesUtil;
import org.artqq.utils.HexUtil;
import org.artqq.utils.bytes.ByteObject;
import org.artqq.utils.zLibUtils;
import org.helper.Desc;

public final class MessageSvc {

    public static class PbSendMsg {
        @Protobuf(fieldType = FieldType.OBJECT, order = 1)
        public RoutingHead routing_head;

        @Protobuf(fieldType = FieldType.OBJECT, order = 2)
        public ContentHead content_head;

        @Protobuf(fieldType = FieldType.OBJECT, order = 3)
        public MsgBody msg_body;

        @Protobuf(fieldType = FieldType.INT64, order = 4)
        public long msg_seq;

        @Protobuf(fieldType = FieldType.INT64, order = 5)
        public long msg_rand;

        @Protobuf(fieldType = FieldType.BYTES, order = 6)
        public byte[] sync_cookie;

        @Protobuf(fieldType = FieldType.OBJECT, order = 7)
        public AppShareInfo app_share;

        @Protobuf(fieldType = FieldType.INT64, order = 8)
        public long msg_via;

        public byte[] toByteArray(){
            try {
                return ProtobufProxy.create(MessageSvc.PbSendMsg.class).encode(this);
            } catch (IOException e) {
                System.out.println(e);
            }
            return null;
        }
    }
    
    public static class PbSendMsgResp {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public long result;

        @Protobuf(fieldType = FieldType.STRING, order = 2)
        public String errmsg;

        @Protobuf(fieldType = FieldType.INT64, order = 3)
        public long send_time;

        @Protobuf(fieldType = FieldType.INT64, order = 4)
        public long svrbusy_wait_time;
        
        @Protobuf(fieldType = FieldType.OBJECT, order = 5)
        public MsgSendInfo msg_send_info;
        
        @Protobuf(fieldType = FieldType.INT32, order = 6)
        public int errtype;
        
        @Protobuf(fieldType = FieldType.OBJECT, order = 7)
        public TransSvrInfo trans_svr_info;
        
        @Protobuf(fieldType = FieldType.OBJECT, order = 8)
        public ReceiptResp receipt_resp;
        
        @Protobuf(fieldType = FieldType.INT32, order = 9)
        public int text_analysis_result;
        
        @Protobuf(fieldType = FieldType.INT32, order = 10)
        public int msg_info_flag;
        
        public static PbSendMsgResp decode(byte[] src){
            try {
                return ProtobufProxy.create(PbSendMsgResp.class).decode(src);
            } catch (IOException e) {
                System.out.println(e);
            }
            return null;
        }
        
        
        public static class MsgSendInfo {
            @Protobuf(fieldType = FieldType.INT32, order = 1)
            public int receiver;
        }
        
        public static class TransSvrInfo {
            @Protobuf(fieldType = FieldType.INT32, order = 1)
            public int sub_type;
            
            @Protobuf(fieldType = FieldType.INT32, order = 2)
            public int ret_code;
            
            @Protobuf(fieldType = FieldType.STRING, order = 3)
            public String err_msg;
            
            @Protobuf(fieldType = FieldType.STRING, order = 4)
            public String trans_info;
        }
        
        public static class ReceiptResp {
            
        }
    }

    public static class AppShareInfo {

    }

    public static class MsgBody {
        @Protobuf(fieldType = FieldType.OBJECT, order = 1)
        public RichText rich_text;

        @Protobuf(fieldType = FieldType.BYTES, order = 2)
        public byte[] msg_content;

        @Protobuf(fieldType = FieldType.BYTES, order = 3)
        public byte[] msg_encrypt_content;

        @Override
        public MsgBody clone() {
            try {
                return (MsgBody)super.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }

        public static class RichText{
            @Protobuf(fieldType = FieldType.OBJECT, order = 1)
            public Attr attr = new Attr();

            @Protobuf(fieldType = FieldType.OBJECT, order = 2)
            public List<Elem> elems = new ArrayList<>();

            @Protobuf(fieldType = FieldType.OBJECT, order = 3)
            public NotOnlineFile not_online_file;

            @Protobuf(fieldType = FieldType.OBJECT, order = 4)
            public Ptt ptt;

            public void cleanMsg(){
                elems.clear();
            }
            
            public void addMsg(Elem elem){
                elems.add(elem);
            }

            public static class Ptt {
                @Protobuf(fieldType = FieldType.INT64, order = 1)
                public long file_type;

                @Protobuf(fieldType = FieldType.UINT64, order = 2)
                public long src_uin;

                @Protobuf(fieldType = FieldType.BYTES, order = 3)
                public byte[] file_uuid;

                @Protobuf(fieldType = FieldType.BYTES, order = 4)
                public byte[] file_md5;

                @Protobuf(fieldType = FieldType.STRING, order = 5)
                public String file_name;

                @Protobuf(fieldType = FieldType.INT64, order = 6)
                public long file_size;

                @Protobuf(fieldType = FieldType.BYTES, order = 7)
                public byte[] reserve;

                @Protobuf(fieldType = FieldType.INT32, order = 8)
                public int file_id = new Random().nextInt();

                @Protobuf(fieldType = FieldType.UINT32, order = 9)
                public int server_ip;

                @Protobuf(fieldType = FieldType.UINT32, order = 10)
                public int server_port;

                @Protobuf(fieldType = FieldType.BOOL, order = 11)
                public boolean valid = true;

                @Protobuf(fieldType = FieldType.BYTES, order = 12)
                public byte[] signature;

                @Protobuf(fieldType = FieldType.BYTES, order = 13)
                public byte[] shortcut;

                @Protobuf(fieldType = FieldType.BYTES, order = 14)
                public byte[] file_key;

                @Protobuf(fieldType = FieldType.INT32, order = 15)
                public Integer magic_ptt_index;

                @Protobuf(fieldType = FieldType.INT64, order = 16)
                public long voice_switch;

                @Protobuf(fieldType = FieldType.STRING, order = 17)
                public String ptt_url;

                @Protobuf(fieldType = FieldType.STRING, order = 18)
                public String group_file_key;

                @Protobuf(fieldType = FieldType.INT64, order = 19)
                public long time = 3;

                @Protobuf(fieldType = FieldType.BYTES, order = 20)
                public byte[] down_para = "".getBytes();

                @Protobuf(fieldType = FieldType.UINT32, order = 29)
                public int format = 1;

                @Protobuf(fieldType = FieldType.OBJECT, order = 30)
                public Reserve pb_reserve = new Reserve();

                // 31另外重新解析

                @Protobuf(fieldType = FieldType.UINT32, order = 32)
                public int download_flag;

                public static class Reserve {
                    @Protobuf(fieldType = FieldType.INT32, order = 1)
                    public int change_voice = 0;

                }
            }

            public static class NotOnlineFile {
                @Protobuf(fieldType = FieldType.BYTES, order = 4)
                public byte[] file_md5;

            }

            public static class Attr {
                @Protobuf(fieldType = FieldType.INT32, order = 1)
                public int code_page = 0;

                @Protobuf(fieldType = FieldType.INT64, order = 2)
                public long time = System.currentTimeMillis() / 1000;

                @Protobuf(fieldType = FieldType.INT64, order = 3)
                public long random = new Random().nextInt();

                @Protobuf(fieldType = FieldType.INT64, order = 4)
                public long color = 0;

                @Protobuf(fieldType = FieldType.INT64, order = 5)
                public long size = 10;

                @Protobuf(fieldType = FieldType.INT64, order = 6)
                public long effect = 0;

                @Protobuf(fieldType = FieldType.INT64, order = 7)
                public long char_set = 78;

                @Protobuf(fieldType = FieldType.INT64, order = 8)
                public long pitch_and_family = 90;

                @Protobuf(fieldType = FieldType.STRING, order = 9)
                public String font_name = "Times New Roman";

                @Protobuf(fieldType = FieldType.BYTES, order = 10)
                public byte[] reserve_data;
            }


            public static class Elem {
                //消息MSG
                @Protobuf(fieldType = FieldType.OBJECT, order = 1)
                public Text text;

                //表情包
                @Protobuf(fieldType = FieldType.OBJECT, order = 2)
                public Face face;

                @Protobuf(fieldType = FieldType.OBJECT, order = 3)
                public OnlineImage online_image;

                @Protobuf(fieldType = FieldType.OBJECT, order = 4)
                public NotOnlineImage not_online_image;
                
                //新闻卡片xml
                @Protobuf(fieldType = FieldType.OBJECT, order = 5)
                public TransElem trans_elem_info;

                // 魔法表情(骰子)
                @Protobuf(fieldType = FieldType.OBJECT, order = 6)
                public MarketFace market_face;
                
                //图片
                @Protobuf(fieldType = FieldType.OBJECT, order = 8)
                public CustomFace custom_face;

                //气泡
                @Protobuf(fieldType = FieldType.OBJECT, order = 9)
                public ElemFlags2 elem_flags2;

                @Protobuf(fieldType = FieldType.OBJECT, order = 10)
                public FunFace fun_face;

                //卡片xml或长消息
                @Protobuf(fieldType = FieldType.OBJECT, order = 12)
                public RichMsg rich_msg;

                //自定义头衔昵称
                @Protobuf(fieldType = FieldType.OBJECT, order = 16)
                public ExtraInfo extra_info;

                @Protobuf(fieldType = FieldType.OBJECT, order = 17)
                public ShakeWindow shake_window;

                @Protobuf(fieldType = FieldType.OBJECT, order = 21)
                public AnonymousGroupMsg anon_group_msg;

                @Protobuf(fieldType = FieldType.OBJECT, order = 37)
                public GeneralFlags general_flags = null;
                // 消息全局设置[是否是长消息]

                @Protobuf(fieldType = FieldType.OBJECT, order = 45)
                public SourceMsg src_msg;
                // 消息源设置[回复目标]

                // json
                @Protobuf(fieldType = FieldType.OBJECT, order = 51)
                public LightAppElem light_app;

                // 特殊卡片框架[闪图, 群小视频, 厘米秀游戏]
                @Protobuf(fieldType = FieldType.OBJECT, order = 53)
                public CommonElem common_elem;

                public static class AnonymousGroupMsg {
                    @Protobuf(order = 1, fieldType = FieldType.UINT32)
                    public int flags;

                    @Protobuf(order = 2, fieldType = FieldType.STRING)
                    public String anon_id;

                    @Protobuf(order = 3, fieldType = FieldType.STRING)
                    public String anon_nick;

                    @Protobuf(order = 4, fieldType = FieldType.UINT32)
                    @Desc(desc = "匿名头像ID")
                    public int head_portrait;

                    @Protobuf(order = 5, fieldType = FieldType.UINT32)
                    public int expire_time;

                    @Protobuf(order = 6, fieldType = FieldType.UINT32)
                    public int bubble_id;

                    @Protobuf(order = 7, fieldType = FieldType.STRING)
                    public String rank_color;
                }

                public static class NotOnlineImage {
                    @Protobuf(fieldType = FieldType.STRING, order = 1)
                    public String file_path;

                    @Protobuf(fieldType = FieldType.INT64, order = 2)
                    public long file_len;

                    @Protobuf(fieldType = FieldType.STRING, order = 3)
                    public String download_path;

                    @Protobuf(fieldType = FieldType.BYTES, order = 4)
                    public byte[] old_ver_send_file;

                    @Protobuf(fieldType = FieldType.INT32, order = 5)
                    public int img_type;

                    @Protobuf(fieldType = FieldType.BYTES, order = 6)
                    public byte[] previews_image;

                    @Protobuf(fieldType = FieldType.BYTES, order = 7)
                    public byte[] pic_md5;

                    @Protobuf(fieldType = FieldType.INT32, order = 8)
                    public int pic_height;

                    @Protobuf(fieldType = FieldType.INT32, order = 9)
                    public int pic_width;

                    @Protobuf(fieldType = FieldType.STRING, order = 10)
                    public String res_id;

                    @Protobuf(fieldType = FieldType.OBJECT, order = 11)
                    public Object flag;

                    @Protobuf(fieldType = FieldType.STRING, order = 12)
                    public String thumb_url;

                    @Protobuf(fieldType = FieldType.INT32, order = 13)
                    public int original = 0;

                    @Protobuf(fieldType = FieldType.STRING, order = 14)
                    public String big_url;

                    @Protobuf(fieldType = FieldType.STRING, order = 15)
                    public String orig_url;

                    @Protobuf(fieldType = FieldType.INT32, order = 16)
                    public int biz_type = 0;

                    @Protobuf(fieldType = FieldType.INT32, order = 17)
                    public int result;

                    @Protobuf(fieldType = FieldType.INT32, order = 18)
                    public int index;

                    @Protobuf(fieldType = FieldType.BYTES, order = 19)
                    public byte[] op_face_buf;

                    @Protobuf(fieldType = FieldType.BOOL, order = 20)
                    public boolean old_pic_md5;

                    @Protobuf(fieldType = FieldType.INT32, order = 21)
                    public int thumb_width;

                    @Protobuf(fieldType = FieldType.INT32, order = 22)
                    public int thumb_height;

                    @Protobuf(fieldType = FieldType.INT32, order = 23)
                    public int file_id;

                    @Protobuf(fieldType = FieldType.INT32, order = 24)
                    public int show_len = 0;

                    @Protobuf(fieldType = FieldType.INT32, order = 25)
                    public int download_len = 0;

                    @Protobuf(fieldType = FieldType.STRING, order = 26)
                    public String str_400_url;

                    @Protobuf(fieldType = FieldType.UINT32, order = 27)
                    public int int_400_width;

                    @Protobuf(fieldType = FieldType.INT32, order = 28)
                    public int int_400_height;

                    @Protobuf(fieldType = FieldType.BYTES, order = 29)
                    public byte[] pb_reserve;
                }
                
                public static class MarketFace {
                    @Protobuf(fieldType = FieldType.STRING, order = 1)
                    public String face_name;

                    @Protobuf(fieldType = FieldType.INT32, order = 2)
                    public int item_type;

                    @Protobuf(fieldType = FieldType.INT32, order = 3)
                    public int face_info;

                    @Protobuf(fieldType = FieldType.BYTES, order = 4)
                    public byte[] face_id;

                    @Protobuf(fieldType = FieldType.INT32, order = 5)
                    public int tab_id;

                    @Protobuf(fieldType = FieldType.INT32, order = 6)
                    public int sub_type;

                    @Protobuf(fieldType = FieldType.BYTES, order = 7)
                    public byte[] key;

                    @Protobuf(fieldType = FieldType.BYTES, order = 8)
                    public byte[] param;

                    @Protobuf(fieldType = FieldType.INT32, order = 9)
                    public int media_type;

                    @Protobuf(fieldType = FieldType.INT32, order = 10)
                    public int image_width;

                    @Protobuf(fieldType = FieldType.INT32, order = 11)
                    public int image_height;

                    @Protobuf(fieldType = FieldType.BYTES, order = 12)
                    public byte[] mobileparam;

                    @Protobuf(fieldType = FieldType.OBJECT, order = 13)
                    public ResvAttr pb_reserve = new ResvAttr();

                    public static class ResvAttr {
                        @Protobuf(fieldType = FieldType.OBJECT, order = 1)
                        public SupportSize support_size;

                        @Protobuf(fieldType = FieldType.INT32, order = 2)
                        public int source_type;

                        @Protobuf(fieldType = FieldType.STRING, order = 3)
                        public String source_name;

                        @Protobuf(fieldType = FieldType.STRING, order = 4)
                        public String source_jump_url;

                        @Protobuf(fieldType = FieldType.STRING, order = 5)
                        public String source_type_name;

                        @Protobuf(fieldType = FieldType.INT32, order = 6)
                        public int start_time;

                        @Protobuf(fieldType = FieldType.INT32, order = 7)
                        public int end_time;

                        @Protobuf(fieldType = FieldType.INT32, order = 8)
                        public int emoji_type;

                        @Protobuf(fieldType = FieldType.OBJECT, order = 9)
                        public SupportSize apng_support_size;

                        @Protobuf(fieldType = FieldType.INT32, order = 10)
                        public int has_ip_product;

                        @Protobuf(fieldType = FieldType.INT32, order = 11)
                        public int voice_item_height_arr;

                        @Protobuf(fieldType = FieldType.STRING, order = 12)
                        public String back_color;

                        @Protobuf(fieldType = FieldType.STRING, order = 13)
                        public String volume_color;

                        public static class SupportSize {
                            @Protobuf(fieldType = FieldType.INT32, order = 1)
                            public int width;

                            @Protobuf(fieldType = FieldType.INT32, order = 2)
                            public int height;
                        }
                    }
                }
                
                public static final class FunFace {
                    @Protobuf(fieldType = FieldType.OBJECT, order = 1)
                    public byte[] tmp;
                    // 待补全
                }

                public static class OnlineImage {
                    @Protobuf(fieldType = FieldType.OBJECT, order = 1)
                    public byte[] guid;

                    @Protobuf(fieldType = FieldType.OBJECT, order = 2)
                    public byte[] path;

                    @Protobuf(fieldType = FieldType.OBJECT, order = 3)
                    public byte[] old_ver_send_file;
                }

                public static class ShakeWindow {
                    @Protobuf(fieldType = FieldType.UINT32, order = 1)
                    public int type;

                    @Protobuf(fieldType = FieldType.UINT32, order = 2)
                    public int reserve;

                    @Protobuf(fieldType = FieldType.UINT64, order = 3)
                    public long uin;
                }

                public static class CommonElem {
                    @Protobuf(fieldType = FieldType.UINT32, order = 1)
                    public int service_type;

                    @Protobuf(fieldType = FieldType.BYTES, order = 2)
                    public byte[] pb_elem;
                    // hummer_commelem.java 里面的都可以丢这里

                    @Protobuf(fieldType = FieldType.UINT32, order = 3)
                    public int business_type;
                }


                public static class LightAppElem {
                    @Protobuf(fieldType = FieldType.BYTES, order = 1)
                    public byte[] data;

                    @Protobuf(fieldType = FieldType.BYTES, order = 2)
                    public byte[] msg_resid;
                    
                    public String getText(){
                        ByteObject bo = new ByteObject(data);
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
                        return json;
                    }
                    
                    public void setJsonSrc(String json){
                        byte[] jData = zLibUtils.compress(json.getBytes());
                        msg_resid = MD5.toMD5Byte(jData);
                        jData = BytesUtil.byteMerger(new byte[]{0x01}, jData);
                        data = jData;
                    }
                }

                public static class SourceMsg {
                    @Protobuf(fieldType = FieldType.UINT32, order = 1)
                    public int orig_seqs;
                    // 回复id

                    @Protobuf(fieldType = FieldType.UINT64, order = 2)
                    public long sender_uin;

                    @Protobuf(fieldType = FieldType.INT64, order = 3)
                    public long time;

                    @Protobuf(fieldType = FieldType.UINT32, order = 4)
                    public int flag;

                    @Protobuf(fieldType = FieldType.OBJECT, order = 5)
                    public MessageField elems = new MessageField();

                    @Protobuf(fieldType = FieldType.UINT32, order = 6)
                    public int type;

                    @Protobuf(fieldType = FieldType.BYTES, order = 7)
                    public byte[] richMsg;

                    @Protobuf(fieldType = FieldType.BYTES, order = 8)
                    public byte[] pb_reserve;

                    @Protobuf(fieldType = FieldType.BYTES, order = 9)
                    public byte[] src_msg;

                    @Protobuf(fieldType = FieldType.UINT64, order = 10)
                    public long to_uin;

                    @Protobuf(fieldType = FieldType.BYTES, order = 11)
                    public byte[] troop_name;

                    public static class MessageField {
                        @Protobuf(fieldType = FieldType.OBJECT, order = 1)
                        public ClassData data = new ClassData();

                        public static class ClassData {
                            @Protobuf(fieldType = FieldType.STRING, order = 1)
                            public String name;
                            // 回复现实的标题
                        }
                    }

                }

                public static class GeneralFlags {
                    @Protobuf(fieldType = FieldType.UINT32, order = 1)
                    public int bubble_diy_text_id;

                    @Protobuf(fieldType = FieldType.UINT32, order = 2)
                    public int group_flag_new;

                    @Protobuf(fieldType = FieldType.UINT64, order = 3)
                    public long uin;

                    @Protobuf(fieldType = FieldType.BYTES, order = 4)
                    public byte[] rp_id;

                    @Protobuf(fieldType = FieldType.UINT32, order = 5)
                    public int prp_fold;

                    @Protobuf(fieldType = FieldType.INT64, order = 6)
                    public long long_text_flag =  0;

                    @Protobuf(fieldType = FieldType.STRING, order = 7)
                    public String long_text_resid;

                    @Protobuf(fieldType = FieldType.UINT32, order = 8)
                    public int group_type;

                    @Protobuf(fieldType = FieldType.UINT32, order = 9)
                    public int to_uin_flag;

                    @Protobuf(fieldType = FieldType.UINT32, order = 10)
                    public int glamour_level;

                    @Protobuf(fieldType = FieldType.UINT32, order = 11)
                    public int member_level;

                    @Protobuf(fieldType = FieldType.UINT64, order = 12)
                    public long group_rank_seq;

                    @Protobuf(fieldType = FieldType.UINT32, order = 13)
                    public int olympic_torch;

                    @Protobuf(fieldType = FieldType.STRING, order = 14)
                    public String babyq_guide_msg_cookie;

                    @Protobuf(fieldType = FieldType.UINT32, order = 15)
                    public int expert_flag;

                    @Protobuf(fieldType = FieldType.UINT32, order = 16)
                    public int bubble_sub_id;

                    @Protobuf(fieldType = FieldType.UINT64, order = 17)
                    public long pendant_id;

                    @Protobuf(fieldType = FieldType.BYTES, order = 18)
                    public byte[] rp_index;

                    @Protobuf(fieldType = FieldType.OBJECT, order = 19)
                    public generalflags.ResvAttr pb_reserve = new generalflags.ResvAttr();
                    // class at generalflags.ResvAttr   
                }

                public static class ExtraInfo {
                    @Protobuf(fieldType = FieldType.STRING, order = 1)
                    public String nick;

                    @Protobuf(fieldType = FieldType.STRING, order = 2)
                    public String group_card;

                    @Protobuf(fieldType = FieldType.UINT32, order = 3)
                    public int level;

                    @Protobuf(fieldType = FieldType.UINT32, order = 4)
                    public int flags;

                    @Protobuf(fieldType = FieldType.UINT32, order = 5)
                    public int group_mask;

                    @Protobuf(fieldType = FieldType.UINT32, order = 6)
                    public int msg_tail_id;

                    @Protobuf(fieldType = FieldType.STRING, order = 7)
                    public String sender_title;

                    @Protobuf(fieldType = FieldType.STRING, order = 8)
                    public String apns_tips;

                    @Protobuf(fieldType = FieldType.INT64, order = 9)
                    public long uin;

                    @Protobuf(fieldType = FieldType.INT64, order = 10)
                    public long msg_state_flag;

                    @Protobuf(fieldType = FieldType.INT64, order = 11)
                    public long apns_sound_type;

                    @Protobuf(fieldType = FieldType.INT64, order = 12)
                    public long new_group_flag;
                }

                public static class RichMsg {
                    @Protobuf(fieldType = FieldType.BYTES, order = 1)
                    public byte[] template_1;

                    @Protobuf(fieldType = FieldType.INT64, order = 2)
                    public long service_id;

                    @Protobuf(fieldType = FieldType.BYTES, order = 3)
                    public byte[] msg_resid;
                    //聊天记录resid

                    @Protobuf(fieldType = FieldType.INT64, order = 4)
                    public long rand;
                    // 卡片rand随机id

                    @Protobuf(fieldType = FieldType.INT64, order = 5)
                    public long seq;

                    @Protobuf(fieldType = FieldType.INT64, order = 6)
                    public long flags;

                    public String getText(){
                        if(template_1 == null){
                            return "";
                        }
                        ByteObject bo = new ByteObject(template_1);
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
                        String xml = new String(source);
                        return xml;
                    }
                    
                    public void setXmlSrc(byte[] xml){
                        byte[] xmlData = zLibUtils.compress(xml);
                        xmlData = BytesUtil.byteMerger(new byte[]{0x01}, xmlData);
                        template_1 = xmlData;
                    }
                }

                public static class ElemFlags2 {
                    @Protobuf(fieldType = FieldType.INT64, order = 1)
                    public long color_text_id;

                    @Protobuf(fieldType = FieldType.INT64, order = 2)
                    public long msg_id;

                    @Protobuf(fieldType = FieldType.INT64, order = 3)
                    public long whisper_session_id;

                    @Protobuf(fieldType = FieldType.INT64, order = 4)
                    public long ptt_change_bit;

                    @Protobuf(fieldType = FieldType.INT64, order = 5)
                    public long vip_status;

                    @Protobuf(fieldType = FieldType.INT64, order = 6)
                    public long compatible_id;

                    // 7是个list

                    @Protobuf(fieldType = FieldType.INT64, order = 8)
                    public long msg_rpt_cnt;
                }

                public static class CustomFace {
                    @Protobuf(fieldType = FieldType.BYTES, order = 1)
                    public byte[] guid;

                    @Protobuf(fieldType = FieldType.STRING, order = 2)
                    public String str_file_path;

                    @Protobuf(fieldType = FieldType.STRING, order = 3)
                    public String str_shortcut;

                    @Protobuf(fieldType = FieldType.BYTES, order = 4)
                    public byte[] buffer;

                    @Protobuf(fieldType = FieldType.BYTES, order = 5)
                    public byte[] flag;

                    @Protobuf(fieldType = FieldType.BYTES, order = 6)
                    public byte[] old_data;

                    @Protobuf(fieldType = FieldType.INT64, order = 7)
                    public long file_id;

                    @Protobuf(fieldType = FieldType.INT64, order = 8)
                    public long server_ip;

                    @Protobuf(fieldType = FieldType.INT64, order = 9)
                    public long server_port;

                    @Protobuf(fieldType = FieldType.INT64, order = 10)
                    public long file_type;

                    @Protobuf(fieldType = FieldType.BYTES, order = 11)
                    public byte[] signature;

                    @Protobuf(fieldType = FieldType.INT64, order = 12)
                    public long useful;

                    @Protobuf(fieldType = FieldType.BYTES, order = 13)
                    public byte[] md5;

                    @Protobuf(fieldType = FieldType.STRING, order = 14)
                    public String str_thumb_url;

                    @Protobuf(fieldType = FieldType.STRING, order = 15)
                    public String str_big_url;

                    @Protobuf(fieldType = FieldType.STRING, order = 16)
                    public String str_orig_url;

                    @Protobuf(fieldType = FieldType.INT32, order = 17)
                    public int biz_type;

                    @Protobuf(fieldType = FieldType.INT64, order = 18)
                    public long repeat_index;

                    @Protobuf(fieldType = FieldType.INT64, order = 19)
                    public long repeat_image;

                    @Protobuf(fieldType = FieldType.INT64, order = 20)
                    public long image_type;

                    @Protobuf(fieldType = FieldType.INT64, order = 21)
                    public long index;

                    @Protobuf(fieldType = FieldType.INT64, order = 22)
                    public long width;

                    @Protobuf(fieldType = FieldType.INT64, order = 23)
                    public long height;

                    @Protobuf(fieldType = FieldType.INT64, order = 24)
                    public long source;

                    @Protobuf(fieldType = FieldType.INT64, order = 25)
                    public long size;

                    @Protobuf(fieldType = FieldType.INT64, order = 26)
                    public long origin;

                    @Protobuf(fieldType = FieldType.INT64, order = 27)
                    public long thumb_width;

                    @Protobuf(fieldType = FieldType.INT64, order = 28)
                    public long thumb_height;

                    @Protobuf(fieldType = FieldType.INT64, order = 29)
                    public long show_len;

                    @Protobuf(fieldType = FieldType.INT64, order = 30)
                    public long download_len;

                    @Protobuf(fieldType = FieldType.STRING, order = 31)
                    public String str_400_url;

                    @Protobuf(fieldType = FieldType.INT64, order = 32)
                    public long _400_width = 64;

                    @Protobuf(fieldType = FieldType.INT64, order = 33)
                    public long _400_height = 64;

                    @Protobuf(fieldType = FieldType.OBJECT, order = 34)
                    public ResvAttr pb_reserve;
                    // ResvAttr

                    public static class ResvAttr {
                        @Protobuf(fieldType = FieldType.INT64, order = 1)
                        public long image_biz_type;

                        @Protobuf(fieldType = FieldType.INT64, order = 2)
                        public long customface_type;

                        @Protobuf(fieldType = FieldType.INT64, order = 3)
                        public long emoji_packageid;

                        @Protobuf(fieldType = FieldType.INT64, order = 4)
                        public long emoji_id;

                        @Protobuf(fieldType = FieldType.STRING, order = 5)
                        public String string_text;

                        @Protobuf(fieldType = FieldType.STRING, order = 6)
                        public String string_doutu_suppliers;

                        @Protobuf(fieldType = FieldType.OBJECT, order = 7)
                        public AnimationImageShow msg_image_show;

                        @Protobuf(fieldType = FieldType.BYTES, order = 9)
                        public byte[] text_summary;

                        @Protobuf(fieldType = FieldType.INT64, order = 10)
                        public long emoji_from;

                        @Protobuf(fieldType = FieldType.STRING, order = 11)
                        public String string_emoji_source;

                        @Protobuf(fieldType = FieldType.STRING, order = 12)
                        public String string_emoji_webUrl;

                        @Protobuf(fieldType = FieldType.STRING, order = 13)
                        public String string_emoji_iconUrl;

                        @Protobuf(fieldType = FieldType.STRING, order = 14)
                        public String string_emoji_marketFaceName;

                        @Protobuf(fieldType = FieldType.INT64, order = 15)
                        public long source;

                        @Protobuf(fieldType = FieldType.STRING, order = 16)
                        public String string_camera_capture_templateinfo;

                        @Protobuf(fieldType = FieldType.STRING, order = 17)
                        public String string_camera_capture_materialname;

                        public static class AnimationImageShow {
                            @Protobuf(fieldType = FieldType.INT32, order = 1)
                            public int int32_effect_id;

                            @Protobuf(fieldType = FieldType.BYTES, order = 2)
                            public byte[] animation_param = "{}".getBytes();

                            @Protobuf(fieldType = FieldType.INT64, order = 3)
                            public long int32_effect_flag = 1;
                        }
                    }
                }

                public static class TransElem {
                    @Protobuf(fieldType = FieldType.INT32, order = 1)
                    public int elem_type;

                    @Protobuf(fieldType = FieldType.BYTES, order = 2)
                    public byte[] elem_value;
                }

                public static class Face {
                    @Protobuf(fieldType = FieldType.INT32, order = 1)
                    public int index;

                    @Protobuf(fieldType = FieldType.BYTES, order = 2)
                    public byte[] old;

                    @Protobuf(fieldType = FieldType.BYTES, order = 3)
                    public byte[] buf;
                }

                public static class Text {
                    @Protobuf(fieldType = FieldType.STRING, order = 1)
                    public String str;

                    @Protobuf(fieldType = FieldType.STRING, order = 2)
                    public String link;

                    @Protobuf(fieldType = FieldType.BYTES, order = 3)
                    public byte[] attr_6_buf = null;
                    // at buf 包

                    @Protobuf(fieldType = FieldType.BYTES, order = 4)
                    public byte[] attr_7_buf;

                    @Protobuf(fieldType = FieldType.BYTES, order = 5)
                    public byte[] buf;

                    @Protobuf(fieldType = FieldType.BYTES, order = 6)
                    public byte[] pb_reserve;
                }

                public static class VideoFile {
                    @Protobuf(fieldType = FieldType.BYTES, order = 1)
                    public byte[] file_uuid;

                    @Protobuf(fieldType = FieldType.STRING, order = 2)
                    public String file_md5;

                    @Protobuf(fieldType = FieldType.BYTES, order = 3)
                    public byte[] file_name;

                    @Protobuf(fieldType = FieldType.INT64, order = 4)
                    public long file_format;

                    @Protobuf(fieldType = FieldType.INT64, order = 5)
                    public long file_time;

                    @Protobuf(fieldType = FieldType.INT64, order = 6)
                    public long file_size;

                    @Protobuf(fieldType = FieldType.INT64, order = 7)
                    public long thumb_width;

                    @Protobuf(fieldType = FieldType.INT64, order = 8)
                    public long thumb_height;

                    @Protobuf(fieldType = FieldType.BYTES, order = 9)
                    public byte[] thumb_file_md5;

                    @Protobuf(fieldType = FieldType.BYTES, order = 10)
                    public byte[] source;

                    @Protobuf(fieldType = FieldType.INT64, order = 11)
                    public long thumb_file_size;

                    @Protobuf(fieldType = FieldType.INT64, order = 12)
                    public long busi_type;

                    @Protobuf(fieldType = FieldType.INT64, order = 13)
                    public long from_chat_type;

                    @Protobuf(fieldType = FieldType.INT64, order = 14)
                    public long to_chat_type;

                    @Protobuf(fieldType = FieldType.BOOL, order = 15)
                    public boolean bool_support_progressive;

                    // 待补全
                }

            }

        }

    }


    public static class ContentHead {
        @Protobuf(fieldType = FieldType.INT64, order = 1)
        public long pkg_num;

        @Protobuf(fieldType = FieldType.INT32, order = 2)
        public int pkg_index;

        @Protobuf(fieldType = FieldType.UINT32, order = 3)
        public int div_seq;

        @Protobuf(fieldType = FieldType.INT32, order = 4)
        public int auto_reply;

        @Override
        public String toString() {
            return "ContentHead{" +
                "pkg_num=" + pkg_num +
                ", pkg_index=" + pkg_index +
                ", div_seq=" + div_seq +
                '}';
        }
    }

    public static class RoutingHead {
        public long to_uin;
        
        @Protobuf(fieldType = FieldType.OBJECT, order = 1)
        public C2C c2C;
        // 好友聊天

        @Protobuf(fieldType = FieldType.OBJECT, order = 2)
        public Grp grp;

        @Protobuf(fieldType = FieldType.OBJECT, order = 3)
        public Grp_tmp grp_tmp;

        // dis 讨论组
        // grp 群聊
        @Protobuf(fieldType = FieldType.OBJECT, order = 4)
        public Dis dis;

        @Protobuf(fieldType = FieldType.OBJECT, order = 5)
        public Dis_tmp dis_tmp;

        @Protobuf(fieldType = FieldType.OBJECT, order = 6)
        public Wpa_tmp wpa_tmp;

        /*
         @Protobuf(fieldType = FieldType.OBJECT, order = 7)
         public SecretFileHead secret_file = new SecretFileHead();
         文件上传 不需要 不继续扩展 后续继续添加
         */

        public static class C2C {
            @Protobuf(fieldType = FieldType.INT64, order = 1)
            public long to_uin = 0;
        }

        public static class Grp {
            @Protobuf(fieldType = FieldType.INT64, order = 1)
            public long group_code = 0;
        }

        public static class Grp_tmp {
            @Protobuf(fieldType = FieldType.INT64, order = 1)
            public long group_uin = 0;

            @Protobuf(fieldType = FieldType.INT64, order = 2)
            public long to_uin = 0;
        }

        public static class Dis {
            @Protobuf(fieldType = FieldType.INT64, order = 1)
            public long dis_uin = 0;
        }

        public static class Dis_tmp {
            @Protobuf(fieldType = FieldType.INT64, order = 1)
            public long group_uin = 0;

            @Protobuf(fieldType = FieldType.INT64, order = 2)
            public long to_uin = 0;
        }

        public static class Wpa_tmp {
            @Protobuf(fieldType = FieldType.BYTES, order = 1)
            public byte[] sig = {};

            @Protobuf(fieldType = FieldType.INT64, order = 2)
            public long to_uin = 0;
        }


        public static class SecretFileHead {
            @Protobuf(fieldType = FieldType.OBJECT, order = 1)
            public Object secret_file_msg;

            @Protobuf(fieldType = FieldType.OBJECT, order = 2)
            public Object grp;
        }

    }

}

