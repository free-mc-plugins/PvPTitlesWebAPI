package com.ericlam.mc.api;

import com.alternacraft.pvptitles.Exceptions.DBException;
import com.alternacraft.pvptitles.Exceptions.RanksException;
import com.alternacraft.pvptitles.Main.Manager;
import com.alternacraft.pvptitles.Main.PvpTitles;
import com.alternacraft.pvptitles.Managers.RankManager;
import com.alternacraft.pvptitles.Misc.PlayerFame;
import com.alternacraft.pvptitles.Misc.Rank;
import com.ericlam.mc.libraries.Static;
import com.ericlam.mc.main.PvPTitlesWebAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class APIDataHandler {
    private ArrayList<PlayerFame> fames;

    public APIDataHandler() throws DBException {
        Manager manager = Manager.getInstance();
        PvPTitlesWebAPI.plugin.getLogger().info("Getting PvPTitles Data.....");
        fames = manager.getDBH().getDM().getTopPlayers((short) 90, PvpTitles.getInstance().getServer().getServerName());
    }

    public ArrayList<JSONObject> getAllDatas() throws RanksException {
        ArrayList<JSONObject> datas = new ArrayList<>();
        for (PlayerFame fame : fames) {
            int fameInt = fame.getFame();
            OfflinePlayer player = fame.getPlayer();
            UUID uuid = UUID.fromString(fame.getUUID());
            long seconds = fame.getSeconds();
            Rank rank = RankManager.getRank(fameInt,seconds,player);
            int kills = PvPTitlesWebAPI.papiEnabled ? Integer.parseInt(PlaceholderAPI.setPlaceholders(player,"%statistic_player_kills%")) : (int)Static.getOfflinePlayerStatistic(player,Statistic.PLAYER_KILLS);
            int deaths = PvPTitlesWebAPI.papiEnabled ? Integer.parseInt(PlaceholderAPI.setPlaceholders(player,"%statistic_deaths%")) : (int)Static.getOfflinePlayerStatistic(player,Statistic.DEATHS);
            datas.add(new APIDatas(uuid,player,fameInt,rank,kills,deaths,seconds).toJSONObject());
        }
        return datas;
    }

}
