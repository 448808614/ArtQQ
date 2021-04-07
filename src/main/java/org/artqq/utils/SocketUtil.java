package org.artqq.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class SocketUtil {
    /**
     * 判断某服务能否连通
     *
     * @param host host
     * @param port port
     * @return boolean
     */
    public static boolean isRunning(String host, int port) {
        Socket sClient = null;
        try {
            SocketAddress saAdd = new InetSocketAddress(host.trim(), port);
            sClient = new Socket();
            sClient.connect(saAdd, 3000);
        } catch (UnknownHostException e) {
            return false;
        } catch (SocketTimeoutException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (sClient != null) {
                    sClient.close();
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
    
}
