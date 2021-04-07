package org.artqq.protobuf;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import java.util.ArrayList;
import java.util.List;

public class wording_storage {
    public static class KeyInfo {
        @Protobuf(fieldType = FieldType.INT32, order = 1)
        public int type;
        
        @Protobuf(fieldType = FieldType.INT64, order = 2)
        public long key;
    }
    
    public static class WordingCfg {
        @Protobuf(fieldType = FieldType.INT32, order = 1)
        public int auto_reply_flag;
        
        @Protobuf(fieldType = FieldType.INT32, order = 2)
        public int select_id;
        
        @Protobuf(fieldType = FieldType.OBJECT, order = 3)
        public List<MessageSvc.MsgBody.RichText> wording_list = new ArrayList<MessageSvc.MsgBody.RichText>();
        
        public void addReplyMessage(String str){
            MessageSvc.MsgBody.RichText rich_text = new MessageSvc.MsgBody.RichText();
            MessageSvc.MsgBody.RichText.Elem elem = new MessageSvc.MsgBody.RichText.Elem();
            MessageSvc.MsgBody.RichText.Elem.Text text = new MessageSvc.MsgBody.RichText.Elem.Text();
            text.str = str;
            elem.text = text;
            rich_text.addMsg(elem);
            wording_list.add(rich_text);
        }
    }
}
