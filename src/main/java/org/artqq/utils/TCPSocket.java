package org.artqq.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.artqq.ThreadManager;
import org.artqq.protobuf.highway.HwData;

public class TCPSocket {
    public Socket client;
    DataOutputStream writeChannel;
    DataInputStream readChannel;
    public int TimeOut = 1000;
    boolean debug = false;
    public int code = 0;
    public int send_count = 0;
    public int read_count = 0;

    public TCPSocket(String host, int port) throws IOException {
        client = new Socket();
        try {
            client.setSoTimeout(TimeOut);
            client.setKeepAlive(true);
            client.connect(new InetSocketAddress(host, port), 3000);
            init();
        } catch (IOException e) {
            code = -999999;
            throw e;
        }
    }

    public DataInputStream getDataInputStream(){
        return readChannel;
    }
    
    public void send(byte[] data){
        ThreadManager.newInstance().addExecuteTask(new Outsourcing(data));
    }
    
    public byte[] read(int timeout) throws Throwable {
        try {
            client.setSoTimeout(timeout);
            int len = readChannel.readInt() - 4;
            byte[] ret = new byte[len];
            readChannel.readFully(ret);
            client.setSoTimeout(TimeOut);
            read_count++;
            return BytesUtil.byteMerger(
                BytesUtil.int_to_buf( len + 4),
                ret);
        } catch (Throwable e) {
            throw e;
        }
    }
    
    public HwData readHwData(int timeout){
        HwData hwData = new HwData();
        try {
            client.setSoTimeout(timeout);
            byte h1 = readChannel.readByte();
            if(h1 == 0x28){
                int headLen = readChannel.readInt();
                int dataLen = readChannel.readInt();
                hwData.head = new byte[headLen];
                hwData.data = new byte[dataLen];
                readChannel.readFully(hwData.head);
                readChannel.readFully(hwData.data);
                // System.out.println(HexUtil.bytesToHexString(hwData.head));
                // System.out.println("\n\n" + HexUtil.bytesToHexString(hwData.data));
                if(readChannel.readByte() == 0x29){
                    System.out.println("[DataUp]获取资源上传结束！");
                }
            }
        } catch (Exception e) {}
        return hwData;
    }
    
    public boolean close(){
        try {
            client.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private void init(){
        try {
            writeChannel = new DataOutputStream(client.getOutputStream());
            readChannel = new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            code = -1372362033;
        }
    }
    
    public class Outsourcing extends Thread {
        public Outsourcing(byte[] data){
            this.data = data;
        }
        
        public byte[] data = null;
        
        @Override
        public void run() {
            try{
                if(data == null || data.length <= 0){
                    return;
                }
                writeChannel.write(data, 0, data.length);
                writeChannel.flush();
                send_count++;
            }catch(Throwable th){}
        }     
    }
}
