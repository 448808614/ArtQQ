package org.artqq.crypt;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.KeyAgreement;

import org.artqq.Code;
import org.artqq.MD5;
import org.artqq.android.AndroidInfo;

import java.security.spec.InvalidKeySpecException;

public class EcdhCrypt{
    public static final String DEFAULT_PUB_KEY = "04edb8906046f5bfbe9abbc5a88b37d70a6006bfbabc1f0cd49dfb33505e63efc5d78ee4e0a4595033b93d02096dcd3190279211f7b4f6785079e19004aa0e03bc";
    public static final String DEFAULT_SHARE_KEY = "c129edba736f4909ecc4ab8e010f46a3";
    static String SvrPubKey = "04EBCA94D733E399B2DB96EACDD3F69A8BB0F74224E2B44E3357812211D2E62EFBC91BB553098E25E33A799ADC7F76FEB208DA7C6522CDB0719A305180CC54A82E";
    static final String X509Prefix = "3059301306072a8648ce3d020106082a8648ce3d030107034200";
    public static byte[] _c_pri_key = new byte[0];
    public static byte[] _c_pub_key = new byte[0];
    private static byte[] _g_share_key = new byte[0];
    private static boolean initFlg = false;
    public static PrivateKey pkcs8PrivateKey;
    private static int sKeyVersion = 1;
    private static boolean userOpenSSLLib = true;
    public static PublicKey x509PublicKey;

    public native int GenECDHKeyEx(String str, String str2, String str3);

    public EcdhCrypt() {
        setPubKey("0440eaf325b9c66225143aa7f3961c953c3d5a8048c2b73293cdc7dcbab7f35c4c66aa8917a8fd511f9d969d02c8501bcaa3e3b11746f00567e3aea303ac5f2d25", 2);
        initShareKey();
    }

    public int GenereateKey() {
        int GenECDHKeyEx;
        try {
            synchronized (EcdhCrypt.class) {
                GenECDHKeyEx = GenECDHKeyEx(SvrPubKey, "", "");
            }
            return GenECDHKeyEx;
        } catch (UnsatisfiedLinkError e) {
            util.LOGI("GenereateKey failed " + e.getMessage(), "");
            return -1;
        } catch (RuntimeException e2) {
            return -2;
        } catch (Exception e3) {
            util.LOGI("GenereateKey failed " + e3.getMessage(), "");
            return -3;
        } catch (Error e4) {
            util.LOGI("GenereateKey failed " + e4.getMessage(), "");
            return -4;
        }
    }

    public byte[] get_c_pub_key() {
        return (byte[]) _c_pub_key.clone();
    }

    public void set_c_pub_key(byte[] bArr) {
        if (bArr != null) {
            _c_pub_key = (byte[]) bArr.clone();
        } else {
            _c_pub_key = new byte[0];
        }
    }

    public void set_c_pri_key(byte[] bArr) {
        if (bArr != null) {
            _c_pri_key = (byte[]) bArr.clone();
        } else {
            _c_pri_key = new byte[0];
        }
    }

    public byte[] get_g_share_key() {
        return (byte[]) _g_share_key.clone();
    }

    public void set_g_share_key(byte[] bArr) {
        if (bArr != null) {
            _g_share_key = (byte[]) bArr.clone();
        } else {
            _g_share_key = new byte[0];
        }
    }

    public byte[] calShareKeyMd5ByPeerPublicKey(byte[] bArr) {
        if (userOpenSSLLib) {
            return calShareKeyByOpenSSL(util.buf_to_string(_c_pri_key), util.buf_to_string(_c_pub_key), util.buf_to_string(bArr));
        }
        return calShareKeyByBouncycastle(bArr);
    }

    private byte[] calShareKeyByOpenSSL(String str, String str2, String str3) {
        util.LOGI("calShareKeyByOpenSSL publickey " + str2, "");
        if (GenECDHKeyEx(str3, str2, str) == 0) {
            return _g_share_key;
        }
        return null;
    }

    private PublicKey constructX509PublicKey(String str) {
        try {
            return KeyFactory.getInstance("EC", "BC").generatePublic(new X509EncodedKeySpec(util.string_to_buf(str)));
        } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            if(Code.isDebug)
                e.printStackTrace();
        }
        return null;
    }

    private byte[] calShareKeyByBouncycastle(byte[] bArr) {
        try {
            PublicKey constructX509PublicKey = constructX509PublicKey(X509Prefix + util.buf_to_string(bArr));
            KeyAgreement instance = KeyAgreement.getInstance("ECDH", "BC");
            instance.init(pkcs8PrivateKey);
            instance.doPhase(constructX509PublicKey, true);
            byte[] generateSecret = instance.generateSecret();
            byte[] bArr2 = new byte[16];
            System.arraycopy(generateSecret, 0, bArr2, 0, 16);
            return MD5.toMD5Byte(bArr2);
        } catch (ExceptionInInitializerError e) {
            return null;
        } catch (Throwable th) {
            util.LOGI("calShareKeyByBouncycastle failed " + pkcs8PrivateKey.toString() + " peer public key " + util.buf_to_string(bArr), "");
            util.printThrowable(th, "calShareKeyByBouncycastle");
            return null;
        }
    }

    private int initShareKeyByOpenSSL() {
        if (AndroidInfo.machine_version_number >= 23 || GenereateKey() != 0) {
            return -1;
        }
        if (_c_pub_key == null || _c_pub_key.length == 0 || _c_pri_key == null || _c_pri_key.length == 0 || _g_share_key == null || _g_share_key.length == 0) {
            util.LOGI("_c_pub_key " + util.buf_to_string(_c_pub_key), "");
            util.LOGI("_c_pri_key " + util.buf_to_string(_c_pri_key), "");
            util.LOGI("_g_share_key " + util.buf_to_string(_g_share_key), "");
            util.LOGI("initShareKeyByOpenSSL generate null key", "");
            return -2;
        }
        util.LOGI("initShareKeyByOpenSSL OK", "");
        return 0;
    }

    private int initShareKeyByBouncycastle() {
        try {
            KeyPairGenerator instance = KeyPairGenerator.getInstance("EC", "BC");
            instance.initialize(new ECGenParameterSpec("prime256v1"));
            KeyPair genKeyPair = instance.genKeyPair();
            PublicKey publicKey = genKeyPair.getPublic();
            byte[] encoded = publicKey.getEncoded();
            PrivateKey privateKey = genKeyPair.getPrivate();
            privateKey.getEncoded();
            PublicKey constructX509PublicKey = constructX509PublicKey(X509Prefix + util.buf_to_string(util.string_to_buf(SvrPubKey)));
            KeyAgreement instance2 = KeyAgreement.getInstance("ECDH", "BC");
            instance2.init(privateKey);
            instance2.doPhase(constructX509PublicKey, true);
            byte[] generateSecret = instance2.generateSecret();
            byte[] bArr = new byte[16];
            System.arraycopy(generateSecret, 0, bArr, 0, 16);
            _g_share_key = MD5.toMD5Byte(bArr);
            _c_pub_key = new byte[65];
            System.arraycopy(encoded, 26, _c_pub_key, 0, 65);
            x509PublicKey = publicKey;
            pkcs8PrivateKey = privateKey;
            util.LOGI("initShareKeyByBouncycastle OK", "");
            return 0;
        } catch (ExceptionInInitializerError e) {
            return -1;
        } catch (Throwable th) {
            util.LOGI("initShareKeyByBouncycastle failed, ", "");
            util.printThrowable(th, "initShareKeyByBouncycastle");
            return -2;
        }
    }

    public int initShareKeyByDefault() {
        _c_pub_key = util.string_to_buf(DEFAULT_PUB_KEY);
        _g_share_key = util.string_to_buf(DEFAULT_SHARE_KEY);
        util.LOGI("initShareKeyByDefault OK", "");
        return 0;
    }

    public int initShareKey() {
        if (initFlg) {
            return 0;
        }
        initFlg = true;
        if (initShareKeyByOpenSSL() == 0) {
            System.out.println("initShareKeyByOpenSSL");
            userOpenSSLLib = true;
            return 0;
        } else if (initShareKeyByBouncycastle() != 0) {
            System.out.println("initShareKeyByDefault");
            return initShareKeyByDefault();
        } else {
            System.out.println("initShareKeyByBouncycastle");
            userOpenSSLLib = false;
            return 0;
        }
    }

    public void setPubKey(String str, int i) {
        try {
            util.LOGI("setPubKey " + str + " ver:" + i, "");
            if (str != null && !str.equals("") && i > 0) {
                SvrPubKey = str;
                sKeyVersion = i;
            }
        } catch (Throwable th) {
            util.printThrowable(th, "setPubKey");
        }
    }

    public int get_pub_key_ver() {
        return sKeyVersion;
    }
    
    public static class util {
        public static String buf_to_string(byte[] bArr) {
            if (bArr == null) {
                return "";
            }
            StringBuilder str = new StringBuilder();
            for(int i = bArr.length - 1; i >= 0; i--) {
                byte b = bArr[i];
                str = new StringBuilder((str + Integer.toHexString((b >> 4) & 15)) + Integer.toHexString(b & 15));
            }
            return str.toString();
        }

        public static String buf_to_string(byte[] bArr, int i) {
            if (bArr == null) {
                return "";
            }
            if (i > bArr.length) {
                i = bArr.length;
            }
            String str = "";
            int i2 = 0;
            while (i2 < i) {
                String str2 = (str + Integer.toHexString((bArr[i2] >> 4) & 15)) + Integer.toHexString(bArr[i2] & 15);
                i2++;
                str = str2;
            }
            return str;
        }

        public static long buf_len(byte[] bArr) {
            if (bArr == null) {
                return 0;
            }
            return (long) bArr.length;
        }

        public static byte get_char(byte b) {
            if (b >= 48 && b <= 57) {
                return (byte) (b - 48);
            }
            if (b >= 97 && b <= 102) {
                return (byte) ((b - 97) + 10);
            }
            if (b < 65 || b > 70) {
                return 0;
            }
            return (byte) ((b - 65) + 10);
        }

        public static byte[] string_to_buf(String str) {
            if (str == null) {
                return new byte[0];
            }
            byte[] bArr = new byte[(str.length() / 2)];
            for (int i = 0; i < str.length() / 2; i++) {
                bArr[i] = (byte) ((get_char((byte) str.charAt(i * 2)) << 4) + get_char((byte) str.charAt((i * 2) + 1)));
            }
            return bArr;
        }
        
        public static void LOGI(String i, String p1) {
            if(Code.isDebug)
                System.out.println(i);
        }
        
        public static void printThrowable(Throwable th, String str) {
            if(Code.isDebug)
                th.printStackTrace();
        }
        
        public static void int8_to_buf(byte[] bArr, int i, int i2) {
            bArr[i] = (byte) (i2);
        }

        public static void int16_to_buf(byte[] bArr, int i, int i2) {
            bArr[i + 1] = (byte) (i2);
            bArr[i] = (byte) (i2 >> 8);
        }

        public static void int32_to_buf(byte[] bArr, int i, int i2) {
            bArr[i + 3] = (byte) (i2);
            bArr[i + 2] = (byte) (i2 >> 8);
            bArr[i + 1] = (byte) (i2 >> 16);
            bArr[i] = (byte) (i2 >> 24);
        }

        public static void int64_to_buf(byte[] bArr, int i, long j) {
            bArr[i + 7] = (byte) ((int) (j));
            bArr[i + 6] = (byte) ((int) (j >> 8));
            bArr[i + 5] = (byte) ((int) (j >> 16));
            bArr[i + 4] = (byte) ((int) (j >> 24));
            bArr[i + 3] = (byte) ((int) (j >> 32));
            bArr[i + 2] = (byte) ((int) (j >> 40));
            bArr[i + 1] = (byte) ((int) (j >> 48));
            bArr[i] = (byte) ((int) (j >> 56));
        }

        public static void int64_to_buf32(byte[] bArr, int i, long j) {
            bArr[i + 3] = (byte) ((int) (j));
            bArr[i + 2] = (byte) ((int) (j >> 8));
            bArr[i + 1] = (byte) ((int) (j >> 16));
            bArr[i] = (byte) ((int) (j >> 24));
        }

        public static int buf_to_int8(byte[] bArr, int i) {
            return bArr[i] & 255;
        }

        public static int buf_to_int16(byte[] bArr, int i) {
            return ((bArr[i] << 8) & 65280) + ((bArr[i + 1]) & 255);
        }

        public static int buf_to_int32(byte[] bArr, int i) {
            return ((bArr[i] << 24) & -16777216) + ((bArr[i + 1] << 16) & 16711680) + ((bArr[i + 2] << 8) & 65280) + ((bArr[i + 3]) & 255);
        }

        public static long buf_to_int64(byte[] bArr, int i) {
            return ((((long) bArr[i]) << 56) & -72057594037927936L) + ((((long) bArr[i + 1]) << 48) & 71776119061217280L) + ((((long) bArr[i + 2]) << 40) & 280375465082880L) + ((((long) bArr[i + 3]) << 32) & 1095216660480L) + ((((long) bArr[i + 4]) << 24) & 4278190080L) + ((((long) bArr[i + 5]) << 16) & 16711680) + ((((long) bArr[i + 6]) << 8) & 65280) + ((((long) bArr[i + 7]) << 0) & 255);
        }

        public static int get_rand_32() {
            return (int) (Math.random() * 2.147483647E9d);
        }

        public static byte[] get_rand_16byte(byte[] bArr) {
            try {
                byte[] seed = SecureRandom.getSeed(16);
                byte[] bArr2 = new byte[(seed.length + bArr.length)];
                System.arraycopy(seed, 0, bArr2, 0, seed.length);
                System.arraycopy(bArr, 0, bArr2, seed.length, bArr.length);
                return MD5.toMD5Byte(bArr2);
            } catch (Throwable th) {
                return get_prand_16byte();
            }
        }

        public static byte[] get_rand_16byte(SecureRandom secureRandom) {
            try {
                byte[] bArr = new byte[16];
                secureRandom.nextBytes(bArr);
                return bArr;
            } catch (Throwable th) {
                return get_prand_16byte();
            }
        }

        public static byte[] get_prand_16byte() {
            try {
                byte[] bArr = new byte[16];
                int32_to_buf(bArr, 0, (int) (Math.random() * 2.147483647E9d));
                int32_to_buf(bArr, 4, (int) (Math.random() * 2.147483647E9d));
                int32_to_buf(bArr, 8, (int) (Math.random() * 2.147483647E9d));
                int32_to_buf(bArr, 12, (int) (Math.random() * 2.147483647E9d));
                return MD5.toMD5Byte(bArr);
            } catch (Throwable th) {
                return new byte[16];
            }
        }
    }
}

