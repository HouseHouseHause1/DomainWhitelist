//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.qbasty.domainwhitelist;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class BungeeCommand extends Command {
    private final Main plugin;

    public BungeeCommand(Main plugin) {
        super("domainwhitelist", "domainwhitelist.admin", new String[]{"dw"});
        this.plugin = plugin;
    }

    public void execute(CommandSender cs, String[] args) {
        if (args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
            this.plugin.reloadConfig();
            cs.sendMessage(new TextComponent("Configuration reloaded"));
        }

    }
}
