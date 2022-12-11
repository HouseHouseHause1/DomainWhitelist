package com.qbasty.domainwhitelist;

import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Set;
import java.util.logging.Level;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

public class Main extends Plugin implements Listener {
    private static final ConfigurationProvider configProvider = ConfigurationProvider.getProvider(YamlConfiguration.class);
    private Set<String> validHostNames;
    private String msg;
    private boolean ignoreCase;
    private boolean blockLegacy;

    public Main() {
    }

    public void onEnable() {
        this.reloadConfig();
        PluginManager pm = this.getProxy().getPluginManager();
        pm.registerCommand(this, new BungeeCommand(this));
        pm.registerListener(this, this);
    }

    public void reloadConfig() {
        try {
            File file = new File(this.getDataFolder(), "config.yml");
            if (!file.exists()) {
                this.getDataFolder().mkdirs();
                file.createNewFile();
                InputStream in = this.getResourceAsStream("config.yml");

                try {
                    FileOutputStream out = new FileOutputStream(file);

                    try {
                        ByteStreams.copy(in, out);
                    } catch (Throwable var8) {
                        try {
                            out.close();
                        } catch (Throwable var7) {
                            var8.addSuppressed(var7);
                        }

                        throw var8;
                    }

                    out.close();
                } catch (Throwable var9) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Throwable var6) {
                            var9.addSuppressed(var6);
                        }
                    }

                    throw var9;
                }

                if (in != null) {
                    in.close();
                }
            }

            Configuration config = configProvider.load(file);
            this.msg = ChatColor.translateAlternateColorCodes('&', config.getString("msg"));
            this.ignoreCase = config.getBoolean("ignore-case", true);
            this.validHostNames = Util.getHostNames(config.getStringList("allowed-connect"), this.ignoreCase);
            this.blockLegacy = config.getBoolean("block-legacy", true);
        } catch (IOException var10) {
            this.getLogger().log(Level.SEVERE, "Error loading configuration", var10);
        }

    }

    private boolean isBlocked(PendingConnection conn) {
        InetSocketAddress address = conn.getVirtualHost();
        if (!conn.isLegacy() && address != null) {
            String hostname = this.ignoreCase ? address.getHostName().toLowerCase() : address.getHostName();
            return !this.validHostNames.contains(hostname);
        } else {
            return this.blockLegacy;
        }
    }

    @EventHandler
    public void onPreLogin(PreLoginEvent event) {
        if (this.isBlocked(event.getConnection())) {
            event.setCancelled(true);
            event.setCancelReason(this.msg);
        }

    }

    public void onDisable() {
    }
}
