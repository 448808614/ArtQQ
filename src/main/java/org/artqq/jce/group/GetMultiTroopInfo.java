package org.artqq.jce.group;

import com.qq.taf.jce.*;
import java.util.ArrayList;

public class GetMultiTroopInfo {
    public static  class Req extends JceStruct {
        public byte richInfo = 0;
        public long uin = 0;
        public ArrayList<Long> groupCode = new ArrayList<Long>();
        
        @Override
        public void writeTo(JceOutputStream jceOutputStream) {
            jceOutputStream.write(this.uin, 0);
            jceOutputStream.write(this.groupCode, 1);
            jceOutputStream.write(this.richInfo, 2);
        }
    }
    
    public static class Resp extends JceStruct {
        static ArrayList<TroopInfoV2> cache_vecTroopInfo;
        public short errorCode;
        public int result;
        public String sGroupClassXMLPath = "";
        public long uin;
        public ArrayList<TroopInfoV2> vecTroopInfo;
        
        @Override
        public void readFrom(JceInputStream jceInputStream) {
            this.uin = jceInputStream.read(this.uin, 0, true);
            this.result = jceInputStream.read(this.result, 1, true);
            this.errorCode = jceInputStream.read(this.errorCode, 2, true);
            if (cache_vecTroopInfo == null) {
                cache_vecTroopInfo = new ArrayList<>();
                cache_vecTroopInfo.add(new TroopInfoV2());
            }
            this.vecTroopInfo = (ArrayList) jceInputStream.readV2(cache_vecTroopInfo, 3, true);
            this.sGroupClassXMLPath = jceInputStream.readString(4, false);
        }
    }
}
