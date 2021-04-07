package org.artqq.utils.bytes;

/*
 * 一瞬冻千秋
 * @author 伏秋洛
 * @creed: 字节工场比字节构建器更加强大, 边写边读, 最高开放性
 * @date 2020/6/6 2:26
 */
import org.artqq.utils.BytesUtil;
import org.artqq.utils.crypts.Crypter;

/**
 * @author pc
 */
public class ByteFactory extends ByteBuilder {
    public int position = 0;

    public ByteFactory(byte[] _data){
        data = _data;
    }

    public ByteFactory(){
    }

    public byte readByte() {
        return readBytes(1)[0];
    }

    public byte[] readBytes(long len) {
        if(len > 0xffff){
            return null;
        }
        return readBytes((int)len);
    }

    /**
     * 一瞬冻千秋
     * @return byte[]
     * @author 伏秋洛
     * @creed: 读取剩下所有Bytes
     * @date 2020/6/6 2:13
     */
    public byte[] readRestBytes(){
        int length = this.data.length - this.position;
        byte[] test = subByte(this.data,this.position,length);
        this.position += length;
        return test;
    }

    public String readStringByShortLen(){
        int length = BytesUtil.GetInt(subByte(this.data,this.position,2));
        this.position += 2;
        byte[] test = subByte(this.data,position,length);
        this.position += length;
        return new String(test);
    }

    public String readStringByIntLen(){
        long length = BytesUtil.GetLong(subByte(this.data,this.position,4));
        this.position += 4;
        byte[] test = subByte(this.data, position, (int)length);
        this.position += length;
        return new String(test);
    }

    public String readString(int length) {
        byte[] test = subByte(this.data,position,length);
        this.position += length;
        return new String(test);
    }

    public String readString(long length) {
        if(length > 0xffff || length < 0)
            return null;
        byte[] test = readBytes(length);
        return new String(test);
    }

    public boolean readBoolean(){
        return readByte()==1?true:false;
    }

    public byte[] readBytesByShortLen(){
        int length = BytesUtil.GetInt(subByte(this.data,this.position,2));
        this.position +=2;
        byte[] test = subByte(this.data,position,length);
        this.position +=length;
        return test;
    }

    public byte[] readBytesByIntLen(){
        long l = readInt();
        return readBytes(l);
    }

    public byte[] readBytes(int length) {
        if (length < 0 || length > 0xffff) {
            return null;
        }
        byte[] test = subByte(this.data, this.position, length);
        this.position += length;
        return test;
    }

    public int readShort(){
        int test = BytesUtil.GetInt(subByte(this.data, this.position, 2));
        this.position += 2;
        return test;
    }

    public byte[] decrypt(byte[] key){
        return new Crypter().decrypt(this.data, key);
    }

    public byte[] decryptRestData(byte[] key){
        int old = getPosition();
        byte[] rest = readRestBytes();
        // String hex = HexUtil.bytesToHexString(rest);
        byte[] ret = new Crypter().decrypt(rest, key);
        setPosition(old);
        return ret;
    }

    public long readInt(){
        long test = BytesUtil.GetLong(subByte(this.data,this.position,4));
        this.position +=4;
        return test;
    }

    @Override
    public byte[] getData() {
        return super.getData();
    }

    public boolean setData(byte[] data){
        if (data != null)
            super.data = data;
        else {
            return false;
        }
        return true;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void addPosition(int a){
        position += a;
    }
}

