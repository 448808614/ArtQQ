package org.artqq.Wtlogin;
import org.artqq.Code;
import org.artqq.utils.HexUtil;
import org.artqq.utils.QQAgreementUtils;
import org.artqq.ArtBot;
import org.artqq.utils.RandomUtil;
import org.artqq.utils.TCPSocket;

public class HeartbeatAlive extends Thread {
    public HeartbeatAlive(ArtBot bot){
        this.bot = bot;
    }
    
    private final ArtBot bot;
    static int sleepTime = 60 * 1000;
    
    @Override
    public void run() {
        while(true) {
            try {
                if(bot.data.containsKey("client") && bot.bot_type != 0) {
                    TCPSocket client = (TCPSocket) bot.data.get("client");
                    byte[] src = QQAgreementUtils.encodeRequest(bot, bot.recorder.nextSsoSeq(), "Heartbeat.Alive", null);
                    // System.out.println(HexUtil.bytesToHexString(src));
                    client.send(src);
                    assert bot.getControl() != null;
                    bot.getControl().getMultiTroopInfo(RandomUtil.randomInt(40000, 1000000000));
                    System.out.println("心跳发送成功！间隔：" + sleepTime);
                }else if(bot.bot_type == -1)
                    return;
                sleep(sleepTime);
            } catch(Exception e) {
                if(Code.isDebug) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}
