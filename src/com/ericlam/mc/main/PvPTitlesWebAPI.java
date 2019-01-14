package com.ericlam.mc.main;

import com.ericlam.mc.webhandler.ResponseHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.eclipse.jetty.server.Server;

public class PvPTitlesWebAPI extends JavaPlugin {
    public static Plugin plugin;
    public static boolean papiEnabled;
    @Override
    public void onEnable() {
        plugin = this;
        papiEnabled = this.getServer().getPluginManager().isPluginEnabled("PlaceHolderAPI");
        this.getLogger().info("PVPTitles - Web API 插件已啟用");
        this.getLogger().info("將開啟port 9090 作為 HTTP API 伺服器");
        new BukkitRunnable() {
            @Override
            public void run() {
                Server server = new Server(9090);
                server.setHandler(new ResponseHandler());
                try {
                    server.start();
                    server.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(this);

    }
}
