package org.artqq.utils;

import java.io.ByteArrayOutputStream;

public class HexUtil {

    public static byte[] HexString2Bytes(String pbkh) {
        return hexStringToByte(pbkh);
    }
    public static byte[] hexStringToByte(String str) {
        str = str.replaceAll(" ", "").replace("\n", "").replace("\r", "").replace("[分割]", "");
        String trim = str.replaceAll("\\s*", "").trim();
        int length = trim.length() / 2;
        byte[] bArr = new byte[length];
        char[] charArray = trim.toCharArray();
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (toByte(charArray[i2 + 1]) | (toByte(charArray[i2]) << 4));
        }
        return bArr;
    }

    /*
     * 一瞬冻千秋
     * @return byte
     * @author 伏秋洛
     * @creed: Hex对照表
     * @date 2020/5/31 4:25
     */
    private static byte toByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String bytesToHexString(byte[] bArr) {
        if(bArr == null) return null;
        StringBuffer stringBuffer = new StringBuffer(bArr.length);
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() < 2) {
                stringBuffer.append(0);
            }
            stringBuffer.append(hexString.toUpperCase());
        }
        return stringBuffer.toString();
    }

    public static byte[] str2byte(String str){
        String replaceAll = str.replaceAll(" ", "");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(replaceAll.length() >> 1);
        for (int i = 0; i <= replaceAll.length() - 2; i += 2){
            byteArrayOutputStream.write(Integer.parseInt(replaceAll.substring(i, i + 2), 16) & 255);
        }
        return byteArrayOutputStream.toByteArray();
    }
}

