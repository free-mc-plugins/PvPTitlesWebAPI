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

        new BukkitRunnable() {
            @Override
            public void run() {
                Server server = new Server(8080);
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
