package org.artbot.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.artbot.Messenger;
import org.artqq.ArtBot;
import org.artqq.Code;

/**
 * @author luoluo
 */
public class ArtUtil {
    public static String messagerToArtCode(Messenger msg){
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < msg.size(); i++){
            int msgType = 0;
            Map<String, ArrayList<String>> msgPackage = msg.message_list.get(i);
            ArrayList<String> indexs = msgPackage.get("index");
            StringBuilder elem = new StringBuilder();
            elem.append("<AQ-").append(indexs.get(0)).append(":");
            String oldMain = msgPackage.get(indexs.get(0)).get(0) == null ? "" : msgPackage.get(indexs.get(0)).get(0);
            String msgMain = encode(oldMain);
            switch(indexs.get(0)){
                case "msg" :{
                    elem = new StringBuilder(oldMain
                        .replace("&", "&#60")
                        .replace("<", "&#55")
                        .replace(">", "&#56")
                    );
                    msgType = 1;
                    break;
                }
                case "img" :{
                    msgType = 2;
                    boolean flash = msg.getAddContent(indexs.get(0), "flash", i, false);
                    int animation = msg.getAddContent(indexs.get(0), "animation", i, 0);
                    elem.append(msgMain).append(",");
                    if(animation != 0) {
                        elem.append("animation:").append(msgMain).append(",");
                    }
                    elem.append("flash:").append(flash).append(">");
                    break;
                }
                case "shake" :{
                    msgType = 3;
                    elem.append("0>");
                    break;
                }
                case "poke" :{
                    try {
                        int id = msg.getAddContent(indexs.get(0), "id", i, new Random().nextInt());
                        int size = msg.getAddContent(indexs.get(0), "size", i, 0);
                        elem.append(Integer.valueOf(msgMain)).append(",");
                        if(size != 0){
                            elem.append("size:").append(size).append(",");
                        }
                        elem.append("id:").append(id).append(">");
                        msgType = 4;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "animation" :{
                    try {
                        int type = Integer.parseInt( msgPackage.get(indexs.get(0)).get(0) );
                        int size = msg.getAddContent(indexs.get(0), "size", i, 0);
                        String name = encode(msg.getAddContent(indexs.get(0), "name", i, "动画消息"));
                        elem.append(type).append(",");
                        if(size != 0) {
                            elem.append("size:").append(size).append(",");
                        }
                        if(!"动画消息".equals(name)) {
                            elem.append("name:").append(name);
                        }
                        elem.append(">");
                        msgType = 5;
                    }catch(Exception e){
                        e.printStackTrace(System.out);
                    }
                    break;
                }
                case "at" :{
                    try{
                        boolean space = msg.getAddContent(indexs.get(0), "space", i, false);
                        long uin = Long.parseLong( msgPackage.get(indexs.get(0)).get(0).replace(" ", "").replace("\n", "").replace("\r", "") );
                        msgType = 6;
                        elem.append(uin).append(",");
                        elem.append("space:").append(space).append(">");
                    }catch(Exception e){
                        if(Code.isDebug) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                case "flashMsg": {
                    int id = msg.getAddContent(indexs.get(0), "id", i, 2000);
                    elem.append(msgMain).append(",");
                    elem.append("id:").append(id).append(">");
                    msgType = 8;
                    break;
                }
                case "json" :{
                    elem.append(msgMain).append(">");
                    msgType = 9;
                    break;
                }
                case "xml" :{
                    int serviceId = msg.getAddContent(indexs.get(0), "serviceID", i, 0);
                    elem.append(msgMain).append(",");
                    elem.append("serviceID:").append(serviceId).append(">");
                    msgType = 10;
                    break;
                }
                case "dice" :{
                    elem.append(msgMain).append(">");
                    msgType = 10;
                    break;
                }
                case "mface" :{
                    elem.append(Integer.valueOf(msgMain.trim())).append(">");
                    msgType = 11;
                    break;
                }
                case "face" :{
                    try{
                        int faceId = Integer.parseInt( msgPackage.get(indexs.get(0)).get(0) );
                        elem.append(faceId).append(">");
                        msgType = 7;
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case "bface" :{
                    elem.append(Integer.valueOf(msgMain));
                    String name =  encode(msg.getAddContent(indexs.get(0), "name", i, ""));
                    if(!"".equals(name)) {
                        elem.append(",name:").append(name).append(">");
                    } else {
                        elem.append(">");
                    }
                    msgType = 12;
                    break;
                }
                case "ptt" :{
                    int time = msg.getAddContent(indexs.get(0), "time", i, -1);
                    boolean magic = msg.getAddContent(indexs.get(0), "magic", i, false);
                    elem.append(msgMain).append(",");
                    if(time != -1){
                        elem.append("time:").append(time).append(",");
                    }
                    elem.append("magic:").append(magic).append(">");
                    msgType = 13;
                    break;
                }
                default:
            }
            if(msgType != 0) {
                ret.append(elem);
            }
        }
        return ret.toString();
    }

    public static Messenger parseArtCode(ArtBot bot, String code){
        Messenger messenger = bot.getMessager();
        assert messenger != null;
        boolean isReading = false;
        ArrayList<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        char[] cs = code.toCharArray();
        for(char c : cs) {
            if(c == '<') {
                if(isReading) {
                    throw new RuntimeException("Repeat the first grammatical error.");
                }
                isReading = true;
                if(sb.length() > 0) {
                    list.add(decodeValue(sb.toString()));
                    sb = new StringBuilder();
                }
                sb.append(c);
            } else if(c == '>') {
                if(!isReading) {
                    throw new RuntimeException("A grammatical error that ends without beginning.");
                }
                isReading = false;
                sb.append(c);
                String aq = sb.toString();
                list.add(aq);
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
        }
        if(sb.length() > 0) {
            list.add(decodeValue(sb.toString()));
        }
        if(Code.isDebug) {
            System.out.println(code + ">>>" + list.toString().replaceAll("[\r_\n]", "\\\\n"));
        }
        for(String m : list){
            String tmp = m;
            if(m.startsWith("<AQ-") && m.endsWith(">")){
                m = m.replace(" ", "").replace("\n", "").replace("\r", "");
                m = m.substring(4, m.length() - 1);
                try{
                    Map<String, String> map = parseValue(m);
                    if(m.startsWith("at")){
                        messenger.addMsg(
                                "at",
                                String.valueOf(get(map, "at", 0L)),
                                "space",
                                String.valueOf(get(map, "space", false))
                        );
                    }else if(m.startsWith("img")){
                        String src = get(map, "img", null);
                        messenger.addMsg(
                                    "img",
                                    src,
                                    "flash",
                                    String.valueOf(get(map, "flash", false)),
                                    "animation",
                                    String.valueOf(get(map, "animation", 0))
                        );
                    }else if(m.startsWith("face")){
                        int face = get(map, "face", 1);
                        messenger.addMsg("face", String.valueOf(face));
                    }else if(m.startsWith("shake")){
                        messenger.addMsg("shake", "0");
                    }else if(m.startsWith("poke")){
                        int poke = get(map, "poke", 1);
                        int id = get(map, "id", 90909);
                        int size = get(map, "size", 0);
                        if(id != 90909){
                            messenger.addMsg("poke", String.valueOf(poke), "id", String.valueOf(id), "size", String.valueOf(size));
                        }else{
                            messenger.addMsg("poke", String.valueOf(poke), "size", String.valueOf(size));
                        }
                    }else if(m.startsWith("animation")){
                        int id = get(map, "animation", 1);
                        String name = get(map, "name", "");
                        int size = get(map, "size", 0);
                        messenger.addMsg("animation", String.valueOf(id), "size", String.valueOf(size), "name", name);
                    }else if(m.startsWith("flashMsg")){
                        String msg = get(map, "flashMsg", null);
                        int id = get(map, "id", 1);
                        if(msg != null) {
                            messenger.addMsg("flashMsg", msg, "id", String.valueOf(id));
                        }
                    }else if(m.startsWith("bface")){
                        int index = get(map, "bface", 789272);
                        if(index != 789272){
                            messenger.addMsg("bface", String.valueOf(index));
                        }
                    }else if(m.startsWith("dice")){
                        int index = get(map, "dice", 456972);
                        if(index != 456972) {
                            // if(index > 6 || index < 1) index = 6;
                            // 筛子合法判断
                            messenger.addMsg("dice", String.valueOf(index));
                        }
                    }else if(m.startsWith("json")){
                        String card = get(map, "json", null);
                        if(card != null) {
                            messenger.addMsg("json", card);
                        }
                    }else if(m.startsWith("xml")){
                        int serviceId = get(map, "serviceID", 76);
                        String card = get(map, "xml", null);
                        if(card != null) {
                            messenger.addMsg("xml", card, "serviceID", String.valueOf(serviceId));
                        }
                    }else if(m.startsWith("ptt")){
                        boolean magic = get(map, "magic", false);
                        int time = get(map, "time", 3);
                        messenger.addMsg("ptt", get(map, "ptt", ""), "magic", String.valueOf(magic), "time", String.valueOf(time));
                    }else{
                        messenger.addMsg("msg", decode(tmp));
                    }
                }catch (Throwable throwable){
                    if(Code.isDebug) {
                        throwable.printStackTrace();
                    }
                }
            }else{
                messenger.addMsg("msg", decode(m));
            }
        }
        return messenger;
    }

    private static Map<String, String> parseValue(String parm){
        HashMap<String, String> map = new HashMap<>(0);
        String[] ps = parm.split("[,]");
        for(String p : ps){
            String[] vs = p.split("[:]");
            if(vs.length < 2) {
                continue;
            }
            map.put( decode(vs[0]), decode(vs[1]));
        }
        return map;
    }

    private static <T> T get(Map<String, String> map, String name, T defaultV){
        if(map.containsKey(name)){
            try{
                if(defaultV instanceof String) {
                    return (T) map.get(name);
                } else if(defaultV instanceof Integer){
                    return (T) Integer.valueOf(map.get(name));
                }else if(defaultV instanceof Long){
                    return (T) Long.valueOf(map.get(name));
                }else if(defaultV instanceof Boolean){
                    return (T) Boolean.valueOf(map.get(name));
                }else{
                    return (T) map.get(name);
                }
            }catch (Exception e){
                return defaultV;
            }
        }else{
            return defaultV;
        }
    }

    public static String encode(String input){
        return input
                .replace("&", "&#60")
                .replace("<", "&#55")
                .replace(">", "&#56")
                .replace(":", "&#57")
                .replace(",", "&#58")
                .replace(" ", "&#59")
                .replace("\n", "&#62")
                .replace("\r", "&#61")
            ;
    }

    public static String decodeValue(String input) {
        return decode(input).replace("<", "&#55").replace(">", "&#56");
    }

    public static String decode(String input){
        return input
            .replace("&#62", "\n")
            .replace("&#61", "\r")
            .replace("&#59", " ")
            .replace("&#55", "<")
            .replace("&#56", ">")
            .replace("&#57", ":")
            .replace("&#58", ",")
            .replace("&#60", "&");
    }
}
