package org.artqq.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.artqq.utils.crypts.Crypter;
import java.io.FileInputStream;

public class Tools {

    public static byte[] readFileBytes(String p0) throws IOException {
        return readFileBytes(new FileInputStream(p0));
    }
    
    public static byte[] TeaDecrypt(byte[] in, byte[] key){
        return new Crypter().decrypt(in, key);
    }

    public static JsonObject parseJsonObject(String c){
        return new JsonParser().parse(c).getAsJsonObject();
    }
    
    public static byte[] readFileBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            int read = inputStream.read();
            if (read == -1) {
                inputStream.close();
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.close();
                return byteArray;
            }
            byteArrayOutputStream.write(read);
        }
    }

    public static void saveFile(String str, String str2) throws IOException {
        saveFile(str, str2.getBytes());
    }

    public static void saveFile(String str, byte[] bArr) throws IOException {
        File file = new File(str);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bArr);
        fileOutputStream.close();
    }
    
    private static void createParentDir(File f){
        File pf = f.getParentFile();
        if(!pf.exists()){
            File ptf = pf.getParentFile();
            if(!ptf.exists()){
                createParentDir(pf);
            }else
                pf.mkdir();
        }
    }
    
}
