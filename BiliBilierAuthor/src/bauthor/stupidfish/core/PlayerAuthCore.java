package bauthor.stupidfish.core;

import bauthor.stupidfish.utils;
import net.sf.json.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class PlayerAuthCore implements Listener {
    public static void authstart(String uid, Player player) {

        //https://space.bilibili.com/151442236

        Bukkit.getScheduler().scheduleSyncDelayedTask(setup.instance, new Runnable() {
                    @Override
                    public void run() {//run
                        int fans = getFans(uid, player);
                        if (fans <= 1000) {//粉丝要求
                            utils.SendMessageRe(player, "&a您因&c填写uid错误&a或者&c粉丝不足1k &6无法申请主播&r");
                            utils.SendMessageRe(player, "&a您的粉丝数只有&6" + fans);
                            return;
                        } else {
                            String Key = getRandomString(5);
                            //开始验证
                            utils.SendMessageRe(player, "&6您有180秒的时间把bilibili的个性签名改为: &4" + Key);//修改个性签名
                            new BukkitRunnable() {//新开一个进程

                                int i = 180;//180 Seconds

                                @Override
                                public void run() {
                                    i--;
                                    if (i <= 0) {//结束计时
                                        utils.SendMessageRe(player, "&4 180秒已经过 主播验证失败");
                                        this.cancel();
                                        return;
                                    }
                                    //do
                                    String Introduction = getIntroduction(uid);
                                    player.sendMessage(utils.StringReplace("&a当前您的个性签名为:&e") + Introduction);
                                    if (Introduction.equalsIgnoreCase(Key)) {//如果uid == Key
                                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent set zhubo");
                                        utils.SendMessageRe(player, "&6恭喜你！ &6您已满足&c所有&6申请主播的要求 &6已经自动把您设置为主播");
                                        this.cancel();//提前结束进程
                                        return;//return
                                    }
                                }
                            }.runTaskTimer(setup.instance, 20L, 20L);
                        }
                    }
                }//run
        );


    }

    public static String getIntroduction(String uid) {
        //http://api.bilibili.com/x/space/acc/info?mid=151442236&jsonp=jsonp
        String html = null;
        //http://api.bilibili.com/x/relation/stat?vmid=151442236
        html = utils.get("http://api.bilibili.com/x/space/acc/info?mid=" + uid + "&jsonp=jsonp");
        if (html.equalsIgnoreCase("ERROR")) {
            return "ERROR";
        }
        String api_data = html;
        JSONObject json = JSONObject.fromObject(api_data);
        return json.getJSONObject("data").getString("sign");//返回sign
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static int getFans(String uid, Player player) {
        String SendHelp =
                "\n&a----------------------------------------------------\n" +
                        "&a检验中 &6请打开个性签名页面 准备为下一步修改做准备    \n" +
                        "&a----------------------------------------------------\n";
        utils.SendMessageRe(player, SendHelp);
        utils.SendMessageRe(player, "&6&l后台正在查看您的粉丝数");
        String html = null;
        //http://api.bilibili.com/x/relation/stat?vmid=151442236
        html = utils.get("http://api.bilibili.com/x/relation/stat?vmid=" + uid);
        if (html.equalsIgnoreCase("ERROR")) {
            utils.SendMessageRe(player, "&c网页访问错误");
            return -1;
        }
        utils.SendMessageRe(player, "&e网页有返回");
        if (setup.debug)
            utils.SendMessageRe(player, "&4[DEBUG]&e" + html);

        String api_data = html;
        //{"code":0,"message":"0","ttl":1,"data":{"mid":151442236,"following":538,"whisper":0,"black":457,"follower":2428}}
        JSONObject json = JSONObject.fromObject(api_data);
        return json.getJSONObject("data").getInt("follower");


    }
}
