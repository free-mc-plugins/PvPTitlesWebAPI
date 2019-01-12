package com.ericlam.mc.webhandler;

import com.alternacraft.pvptitles.Exceptions.DBException;
import com.alternacraft.pvptitles.Exceptions.RanksException;
import com.ericlam.mc.api.APIDataHandler;
import com.ericlam.mc.main.PvPTitlesWebAPI;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;

class RefreshScheduler {

    private static RefreshScheduler refreshScheduler;
    private String datalist = null;
    private boolean processing;

    static RefreshScheduler getInstance() {
        if (refreshScheduler == null) refreshScheduler = new RefreshScheduler();
        return refreshScheduler;
    }

    private RefreshScheduler(){
        synchronized (this){
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (processing) return;
                    try {
                        processing = true;
                        APIDataHandler dataHandler = new APIDataHandler();
                        datalist = JSONArray.toJSONString(dataHandler.getAllDatas());
                    } catch (DBException | RanksException e) {
                        e.printStackTrace();
                    }
                    processing = false;
                }
            }.runTaskTimerAsynchronously(PvPTitlesWebAPI.plugin, 0,3600 * 20);
        }
    }

    String getDatalist() {
        if (datalist == null) refreshData();
        return datalist;
    }

    private boolean cooldown = false;

    synchronized boolean refreshData(){
        if (processing) return false;
        if (cooldown) return true;
        try {
            processing = true;
            APIDataHandler dataHandler = new APIDataHandler();
            datalist = JSONArray.toJSONString(dataHandler.getAllDatas());
            cooldown();
        } catch (DBException | RanksException e) {
            processing = false;
            e.printStackTrace();
            return false;
        }
        processing = false;
        return true;
    }

    private synchronized void cooldown() {
        cooldown = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                cooldown = false;
            }
        }.runTaskLater(PvPTitlesWebAPI.plugin, 120 * 20);
    }
}
