package org.artqq.utils;

// 伪造虚拟的安卓设备信息
public class VirtualAndroidUtil {
    /*
     * @param imei
     * @return boolean
     * @author 伏秋洛
     * @creed: 判断是否是真实IMEI
     * @date 2020/5/31 2:33
     */
    public static Boolean isCorrectImei(String imei) {
        final int imeiLength = 15;
        if (imei.length() == imeiLength) {
            int check = Integer.valueOf(imei.substring(14));
            imei = imei.substring(0, 14);
            char[] imeiChar = imei.toCharArray();
            int resultInt = 0;
            for (int i = 0; i < imeiChar.length; i++) {
                int a = Integer.parseInt(String.valueOf(imeiChar[i]));
                i++;
                final int temp = Integer.parseInt(String.valueOf(imeiChar[i])) * 2;
                final int b = temp < 10 ? temp : temp - 9;
                resultInt += a + b;
            }
            resultInt %= 10;
            resultInt = resultInt == 0 ? 0 : 10 - resultInt;
            if (resultInt == check) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCorrectImei(long imei){
        return isCorrectImei(String.valueOf(imei));
    }

    public static String getMacAddr() {
        return "02:00:00:00:00:00";
    }

    /*
     *
     * @param
     * @return java.lang.String
     * @author 伏秋洛
     * @creed: 合成一个IMEI
     * @date 2020/5/31 2:37
     */
    public static String getIMEI() {
        int r1 = 1000000 + new java.util.Random().nextInt(9000000);
        int r2 = 1000000 + new java.util.Random().nextInt(9000000);
        String input = r1 + "" + r2;
        char[] ch = input.toCharArray();
        int a = 0, b = 0;
        for (int i = 0; i < ch.length; i++) {
            int tt = Integer.parseInt(ch[i] + "");
            if (i % 2 == 0) {
                a = a + tt;
            } else {
                int temp = tt * 2;
                b = b + temp / 10 + temp % 10;
            }
        }
        int last = (a + b) % 10;
        if (last == 0) {
            last = 0;
        } else {
            last = 10 - last;
        }
        String imei = input + last;
        if(! isCorrectImei(imei))
            return getIMEI();
        return imei;
    }

    /*
     * @param
     * @return java.lang.String
     * @author 伏秋洛
     * @creed: 合成一个虚假的IMSI
     * @date 2020/5/31 3:03
     */
    public static String getImsi() {
        String title = "4600";
        int second = 0;
        do {
            second = new java.util.Random().nextInt(8);
        } while (second == 4);
        int r1 = 10000 + new java.util.Random().nextInt(90000);
        int r2 = 10000 + new java.util.Random().nextInt(90000);
        return title + "" + second + "" + r1 + "" + r2;
    }

    /*
     * 一瞬冻千秋
     * @return byte[]
     * @author 伏秋洛
     * @creed: Guid合成
     * @date 2020/6/5 23:21
     */
    public static byte[] getGuid(byte[] imei){
        // imei (md5)
        if(imei == null || imei.length <= 0){
            return null;
        }
        byte[] guid = new byte[imei.length];
        System.arraycopy(imei, 0, guid, 0, imei.length);
        return guid;
    }



}

