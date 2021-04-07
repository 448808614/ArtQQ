package org.artqq.jce.group;
import com.qq.taf.jce.JceOutputStream;
import com.qq.taf.jce.JceStruct;
import java.util.ArrayList;
import com.qq.taf.jce.JceInputStream;

public class GetTroopList {
    public static class SimplifyReq extends JceStruct {
        public byte getLongGroupName;
        public byte getMSFMsgFlag;
        public byte groupFlagExt;
        public long companyId;
        public int shVersion;
        public long uin;
        public byte[] cookies;
        public ArrayList<TroopNumSimplify> groupInfo;
        public long versionNum;
        
        @Override
        public void writeTo(JceOutputStream jceOutputStream) {
            jceOutputStream.write(this.uin, 0);
            jceOutputStream.write(this.getMSFMsgFlag, 1);
            if (this.cookies != null) {
                jceOutputStream.write(this.cookies, 2);
            }
            if (this.groupInfo != null) {
                jceOutputStream.write(this.groupInfo, 3);
            }
            jceOutputStream.write(this.groupFlagExt, 4);
            jceOutputStream.write(this.shVersion, 5);
            jceOutputStream.write(this.companyId, 6);
            jceOutputStream.write(this.versionNum, 7);
            jceOutputStream.write(this.getLongGroupName, 8);
        }
    }
    
    public static class Resp extends JceStruct {
        static byte[] cache_vecCookies = new byte[1];
        static ArrayList<TroopNum> cache_troopNum_list = new ArrayList<>();
        static ArrayList<FavoriteGroup> cache_vecFavGroup = new ArrayList<>();
        static ArrayList<GroupRankInfo> cache_vecTroopRank = new ArrayList<>();
        public short errorCode;
        public int result;
        public short troopcount;
        public long uin;
        public byte[] vecCookies;
        public ArrayList<FavoriteGroup> vecFavGroup;
        public ArrayList<TroopNum> vecTroopList;
        public ArrayList<TroopNum> vecTroopListDel;
        public ArrayList<TroopNum> vecTroopListExt;
        public ArrayList<GroupRankInfo> vecTroopRank;
        
        static {
            cache_troopNum_list.add(new TroopNum());
            cache_vecTroopRank.add(new GroupRankInfo());
            cache_vecFavGroup.add(new FavoriteGroup());
        }
        
        @Override
        public void readFrom(JceInputStream jceInputStream) {
            this.uin = jceInputStream.read(this.uin, 0, true);
            this.troopcount = jceInputStream.read(this.troopcount, 1, true);
            this.result = jceInputStream.read(this.result, 2, true);
            this.errorCode = jceInputStream.read(this.errorCode, 3, false);
            this.vecCookies = jceInputStream.read(cache_vecCookies, 4, false);
            this.vecTroopList = (ArrayList) jceInputStream.readV2(cache_troopNum_list, 5, false);
            this.vecTroopListDel = (ArrayList) jceInputStream.readV2(cache_troopNum_list, 6, false);
            this.vecTroopRank = (ArrayList) jceInputStream.readV2(cache_vecTroopRank, 7, false);
            this.vecFavGroup = (ArrayList) jceInputStream.readV2(cache_vecFavGroup, 8, false);
            this.vecTroopListExt = (ArrayList) jceInputStream.readV2(cache_troopNum_list, 9, false);
        }
    }
    
}
