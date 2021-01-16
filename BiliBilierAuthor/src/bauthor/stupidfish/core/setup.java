package bauthor.stupidfish.core;

import bauthor.stupidfish.utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class setup extends JavaPlugin {
    public static boolean debug = false;
    public static setup instance;
    public static String helptext =
            "&a自助主播验证 &6 - &bBiliBiliAuthor\n" +
                    "&e/bauthor &6uid\n" +
                    "         &creload(administrators only)";

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getLogger().info("已加载");
//        Bukkit.getPluginManager().registerEvents(new noDamage(), (Plugin)this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {//判断是不是玩家
            if (cmd.getName().equalsIgnoreCase("bauthor")) {
                if (args.length < 1) {
                    sender.sendMessage(utils.StringReplace("&c命令缺少参数"));
                    utils.SendMessageRe((Player) sender, helptext);
                } else if (args.length > 1) {
                    sender.sendMessage(utils.StringReplace("&c命令输入错误"));
                    utils.SendMessageRe((Player) sender, helptext);
                } else {//参数筛选器
                    if (args[0].equalsIgnoreCase("reload")) {
                        if (sender.hasPermission("bauthor.reload")) {
                            Bukkit.getPluginManager().disablePlugin(this);
                            Bukkit.getPluginManager().enablePlugin(this);//try
                            sender.sendMessage(utils.StringReplace("&a重载插件成功"));
                        } else {
                            sender.sendMessage(utils.StringReplace("&c您没有此权限"));
                        }
                    } else if (utils.isNumeric(args[0])) {//判断uid是否有效
                        PlayerAuthCore.authstart(args[0], (Player) sender);
                    } else {
                        utils.SendMessageRe((Player) sender, "&c请输入有效的uid");
                    }
                    return true;
                }

            }
        }
        return false;
    }

    @Override
    public void onDisable() {
        Bukkit.getPluginManager().disablePlugin((Plugin) this);
        Bukkit.getLogger().warning("BiliBilierAuthor By _stupidfish已卸载");
    }
}
