package org.artqq.jce.group;

import java.util.ArrayList;
import com.qq.taf.jce.JceOutputStream;
import com.qq.taf.jce.JceStruct;
import com.qq.taf.jce.JceInputStream;

public class ModifyGroupCard extends JceStruct{
    public static class Req extends JceStruct {
        public long groupCode;
        public long newSeq;
        public long zero;
        public ArrayList<UinInfo> uinInfo;
        
        @Override
        public void writeTo(JceOutputStream jceOutputStream) {
            jceOutputStream.write(this.zero, 0);
            jceOutputStream.write(this.groupCode, 1);
            jceOutputStream.write(this.newSeq, 2);
            jceOutputStream.write(this.uinInfo, 3);
        }
    }
    
    public static class Resp extends JceStruct {
        static ArrayList<Long> cache_vecUin = new ArrayList<>();
        public String ErrorString = "";
        public long groupCode;
        public long groupUin;
        public int result;
        public ArrayList<Long> uins;
        
        static {
            cache_vecUin.add(0L);
        }

        @Override
        public void readFrom(JceInputStream jceInputStream) {
            this.result = jceInputStream.read(this.result, 0, true);
            this.groupUin = jceInputStream.read(this.groupUin, 1, true);
            this.groupCode = jceInputStream.read(this.groupCode, 2, true);
            this.uins = (ArrayList) jceInputStream.readV2(cache_vecUin, 3, true);
            this.ErrorString = jceInputStream.readString(4, false);
        }
    }
    
}
