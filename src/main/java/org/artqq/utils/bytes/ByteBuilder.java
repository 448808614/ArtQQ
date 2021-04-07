package org.artqq.utils.bytes;

import java.io.UnsupportedEncodingException;
import org.artqq.utils.BytesUtil;
import org.artqq.utils.HexUtil;
import org.artqq.utils.crypts.Crypter;

/**
 * @author luoluo
 */
public class ByteBuilder {
    public byte[] data = new byte[]{};
    public static String charsetName = "UTF-8";

    public ByteBuilder(){ }

    /**
     * 一瞬冻千秋
     * @return com.artqq.utils.bytes.ByteBuilder
     * @author 伏秋洛
     * @creed: 在开头加上包的4位大小的包字节组长度， add表示偏移量
     * @date 2020/6/1 23:45
     */
    public ByteBuilder putStartSize(int add){
        byte[] test = subByte(BytesUtil.LongToBytes(data.length+add ),4,4);
        this.data = byteMerger(test,this.data);
        return this;
    }

    public ByteBuilder WriteByte(Integer i){
        WriteByte(i.byteValue());
        return this;
    }

    /**
     * 一瞬冻千秋
     * @return com.artqq.utils.bytes.ByteBuilder
     * @author 伏秋洛
     * @creed: 在开头加上包的2位大小的包字节组长度， add表示偏移量
     * @date 2020/6/1 23:45
     */
    public ByteBuilder putStartSmallSize(int add){
        byte[] test = subByte(BytesUtil.IntegerToByte(data.length+add),2,2);
        this.data = byteMerger(test,this.data);
        return this;
    }

    /**
     * 一瞬冻千秋
     * @return com.artqq.utils.bytes.ByteBuilder
     * @author 伏秋洛
     * @creed: 向构建器写入一个字符串
     * @date 2020/6/1 23:12
     */
    public ByteBuilder WriteString(String to_write){
        try {
            this.data = byteMerger(this.data, to_write.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) { }
        return this;
    }

    /**
     * 一瞬冻千秋
     * @return com.artqq.utils.bytes.ByteBuilder
     * @author 伏秋洛
     * @creed: 向构建器写入一个字节组
     * @date 2020/6/1 23:17
     */
    public ByteBuilder WriteBytes(byte[] to_write){
        this.data = byteMerger(this.data,to_write);
        return this;
    }

    public ByteBuilder WriteBytes(byte[]... to_write){
        if(to_write.length <= 0) return this;
        for(byte[] bs : to_write){
            if(bs == null) continue;
            WriteBytes(bs);
        }
        return this;
    }

    public ByteBuilder WriteByte(byte to_write){
        this.data = byteMerger(this.data,new byte[]{to_write});
        return this;
    }

    public ByteBuilder WriteShort(int... to_write){
        if(to_write.length <= 0) return this;
        for(int i : to_write){
            WriteShort(i);
        }
        return this;
    }

    public ByteBuilder WriteShort(int to_write){
        byte[] test = subByte(BytesUtil.IntegerToByte(to_write),2,2);
        this.data = byteMerger(this.data,test);
        return this;
    }

    public ByteBuilder WriteHexStr(String s){
        WriteBytes(HexUtil.hexStringToByte(s.replace("[分割]","").replace("|", "").replace(" ", "")));
        return this;
    }

    public ByteBuilder WriteBoolean(boolean b){
        WriteByte( b?(byte)0x01:(byte)0x00);
        return this;
    }

    public ByteBuilder ReWriteBytes(byte[] to_write){
        this.data = byteMerger(to_write, this.data);
        return this;
    }
    
    public ByteBuilder ReWriteByte(Integer to_write){
        this.data = byteMerger(new byte[]{to_write.byteValue()}, this.data);
        return this;
    }

    public ByteBuilder ReWriteHexStr(String to_write){
        this.data = byteMerger(HexUtil.hexStringToByte(to_write), this.data);
        return this;
    }

    public ByteBuilder WriteInt(long to_write){
        byte[] test = subByte(BytesUtil.LongToBytes(to_write), 4, 4);
        this.data = byteMerger(this.data,test);
        return this;
    }

    public byte[] getData(){
        return this.data.clone();
    }

    public byte[] toByteArray(){
        return getData();
    }

    public int length(){
        if(data == null)
            return 0;
        return data.length;
    }

    /**
     * 一瞬冻千秋
     * @return com.artqq.utils.bytes.ByteBuilder
     * @author 伏秋洛
     * @creed: 清空构建器
     * @date 2020/6/1 23:11
     */
    public ByteBuilder clean(){
        this.data = new byte[]{};
        return this;
    }

    public byte[] byteMerger(byte[] a, byte[] b){
        return BytesUtil.byteMerger(a, b);
    }

    public byte[] subByte(byte[] a, int b, int c){
        return BytesUtil.subByte(a,b,c);
    }

    public byte[] encypt(byte[] key){
        Crypter crypter = new Crypter();
        return crypter.encrypt(this.data, key);
    }
}

