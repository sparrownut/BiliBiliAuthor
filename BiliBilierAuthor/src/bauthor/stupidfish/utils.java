package bauthor.stupidfish;


import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class utils {
    public static void SendMessageRe(Player player, String s) {
        player.sendMessage(StringReplace(s));
    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String StringReplace(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append("§b§lTwigmc §a>> §r");
        sb.append(s.replace('&', '§'));
        return sb.toString();//replace
    }

    public static String get(String url) {
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setReadTimeout(8000);
            conn.setConnectTimeout(8000);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0");
            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream is = conn.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = in.readLine()) != null) {
                    buffer.append(line);
                }
                String result = buffer.toString();
                //subscriber是观察者，在本代码中可以理解成发送数据给activity
                return result;
            }
        } catch (Exception e) {
            return "ERROR";
        }
        return "ERROR";
    }
}
