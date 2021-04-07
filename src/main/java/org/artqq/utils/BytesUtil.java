package org.artqq.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.Random;
import java.util.zip.CRC32;

public class BytesUtil {
    public static byte[] int_to_buf(long in){
        return BytesUtil.subByte(
            BytesUtil.LongToBytes(in)
            , 4, 4
        );
    }
    public static byte[] short_to_buf(int in){
        return BytesUtil.subByte(
            BytesUtil.LongToBytes(in)
            , 2, 2
        );
    }

    public static short bytesToshort(byte[] input){
        byte high = input[0];
        byte low = input[1];
        short z = (short)(((high & 0x00FF) << 8) | (0x00FF & low));
        return z;
    }

    public static String GetIpStringFromBytes(byte[] input){
        String u1 = String.valueOf((int)input[0] & 0xff);
        String u2 = String.valueOf((int)input[1] & 0xff);
        String u3 = String.valueOf((int)input[2] & 0xff);
        String u4 = String.valueOf((int)input[3] & 0xff);
        return u1 + "." + u2 + "." + u3 + "." + u4;
    }

    public static long bytesToLong(byte[] input, int offset, boolean littleEndian){
        // 将byte[] 封装为 ByteBuffer
        ByteBuffer buffer = ByteBuffer.wrap(byteMerger(new byte[8 - input.length], input));
        if (littleEndian){
            // ByteBuffer.order(ByteOrder) 方法指定字节序,即大小端模式(BIG_ENDIAN/LITTLE_ENDIAN)
            // ByteBuffer 默认为大端(BIG_ENDIAN)模式
            buffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        return buffer.getLong();
    }

    public static int GetInt(byte[] data, int offset, int length){
        byte[] test = new byte[]{0x00,0x00,data[offset],data[offset + 1],0x00,0x00,0x00,0x00};
        ByteBuffer u = ByteBuffer.wrap(test);
        return u.getInt();
    }

    /*
     * 一瞬冻千秋
     * @return int
     * @author 伏秋洛
     * @creed: bytes转int
     * @date 2020/5/31 14:21
     */
    public static int GetInt(byte[] data){
        byte[] test = new byte[]{0x00, 0x00, data[0], data[1], 0x00, 0x00, 0x00, 0x00};
        ByteBuffer u = ByteBuffer.wrap(test);
        return u.getInt();
    }

    public static long GetLong(byte[] data){
        byte[] test = new byte[]{0x00,0x00,0x00,0x00,data[0],data[1],data[2],data[3]};
        ByteBuffer u = ByteBuffer.wrap(test);
        return u.getLong();
    }

    public static short GetShort(byte[] data){
        byte[] test = new byte[]{data[0],data[1],0x00,0x00,0x00,0x00,0x00,0x00};
        ByteBuffer u = ByteBuffer.wrap(test);
        return u.getShort();
    }

    public static Date GetDateTimeFromMillis(long timeMillis){
        Date date = new Date(timeMillis);
        return date;
    }

    public static byte[] GetBytes(String string){
        return string.getBytes();
    }

    //随机密钥
    public static byte[] RandomKey(){
        byte[] key = new byte[16];
        new Random().nextBytes(key);
        return key;
    }

    public static int randomInt(int min, int max){
        return new Random().nextInt(max - min + 1) + min;
    }

    public static byte[] RandomKey(int size){
        byte[] key = new byte[size];
        new Random().nextBytes(key);
        return key;
    }

    public static void int8_to_buf(byte[] bArr, int i, int i2) {
        bArr[i + 0] = (byte) (i2 >> 0);
    }

    public static void int16_to_buf(byte[] bArr, int i, int i2) {
        bArr[i + 1] = (byte) (i2 >> 0);
        bArr[i + 0] = (byte) (i2 >> 8);
    }

    public static void int32_to_buf(byte[] bArr, int i, int i2) {
        bArr[i + 3] = (byte) (i2 >> 0);
        bArr[i + 2] = (byte) (i2 >> 8);
        bArr[i + 1] = (byte) (i2 >> 16);
        bArr[i + 0] = (byte) (i2 >> 24);
    }

    public static void int64_to_buf(byte[] bArr, int i, long j) {
        bArr[i + 7] = (byte) ((int) (j >> 0));
        bArr[i + 6] = (byte) ((int) (j >> 8));
        bArr[i + 5] = (byte) ((int) (j >> 16));
        bArr[i + 4] = (byte) ((int) (j >> 24));
        bArr[i + 3] = (byte) ((int) (j >> 32));
        bArr[i + 2] = (byte) ((int) (j >> 40));
        bArr[i + 1] = (byte) ((int) (j >> 48));
        bArr[i + 0] = (byte) ((int) (j >> 56));
    }

    public static void int64_to_buf32(byte[] bArr, int i, long j) {
        bArr[i + 3] = (byte) ((int) (j >> 0));
        bArr[i + 2] = (byte) ((int) (j >> 8));
        bArr[i + 1] = (byte) ((int) (j >> 16));
        bArr[i + 0] = (byte) ((int) (j >> 24));
    }

    public static int buf_to_int8(byte[] bArr, int i) {
        return bArr[i] & 255;
    }

    public static int buf_to_int16(byte[] bArr, int i) {
        return ((bArr[i] << 8) & 65280) + ((bArr[i + 1] << 0) & 255);
    }

    public static int buf_to_int32(byte[] bArr, int i) {
        return ((bArr[i] << 24) & -16777216) + ((bArr[i + 1] << 16) & 16711680) + ((bArr[i + 2] << 8) & 65280) + ((bArr[i + 3] << 0) & 255);
    }

    public static long buf_to_int64(byte[] bArr, int i) {
        return 0 + ((((long) bArr[i]) << 56) & -72057594037927936L) + ((((long) bArr[i + 1]) << 48) & 71776119061217280L) + ((((long) bArr[i + 2]) << 40) & 280375465082880L) + ((((long) bArr[i + 3]) << 32) & 1095216660480L) + ((((long) bArr[i + 4]) << 24) & 4278190080L) + ((((long) bArr[i + 5]) << 16) & 16711680) + ((((long) bArr[i + 6]) << 8) & 65280) + ((((long) bArr[i + 7]) << 0) & 255);
    }

    public static int get_rand_32() {
        return (int) (Math.random() * 2.147483647E9d);
    }

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public static byte[] LongToBytes(long x){
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(x);
        return  buffer.array();
    }

    public static byte[] IntegerToByte(int x){
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putInt(x);
        return  buffer.array();
    }

    public static byte[] ShortToByte(short x){
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putShort(x);
        return  buffer.array();
    }

    public static byte[] subByte(byte[] b, int off, int length){
        if (b != null && b.length > 0){
            byte[] b1 = new byte[length];
            System.arraycopy(b, off, b1, 0, length);
            return b1;
        }
        return null;
    }

    public static byte[] IPStringToByteArray(String ip){
        byte[] array = new byte[4];
        String[] array2 = ip.split("[.]");
        if (array2.length == 4){
            for (int i = 0; i < 4; i++){
                array[i] = (byte) Integer.parseInt(array2[i]);
            }
        }
        return array;
    }

    /**
     * ip地址转成long型数字
     * 将IP地址转化成整数的方法如下：
     * 1、通过String的split方法按.分隔得到4个长度的数组
     * 2、通过左移位操作（<<）给每一段的数字加权，第一段的权为2的24次方，第二段的权为2的16次方，第三段的权为2的8次方，最后一段的权为1
     * @param strIp
     * @return
     */
    public static long ipToLong(String strIp) {
        String[]ip = strIp.split("\\.");
        return (Long.parseLong(ip[0]) << 24) + (Long.parseLong(ip[1]) << 16) + (Long.parseLong(ip[2]) << 8) + Long.parseLong(ip[3]);
    }

    public static String LongToIp(long ip, String def){
        try{
            return (ip & 0x000000FF)+"."+((ip & 0x0000FFFF) >> 8)+"."+((ip & 0x00FFFFFF) >> 16)+"."+(ip >> 24);
        }catch(Exception e){
            return def;
        }
    }

    public static byte[] random_byte(int size) {
        byte[] b = new byte[size];
        Random random = new Random();
        random.nextBytes(b);
        return b;
    }


    public static byte[] GetCrc32(byte[] data){
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        return reverse_byte(HexUtil.str2byte(Long.toHexString(crc32.getValue())));
    }


    public static byte[] reverse_byte(byte[] data){
        byte[] Fuck = new byte[data.length];
        for (int time = 0;time < data.length;time++){
            Fuck[time] = data[data.length - time - 1];
        }
        return Fuck;
    }
}
