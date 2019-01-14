package com.ericlam.mc.libraries;

import org.bukkit.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;

public class Static {
    public static long getOfflinePlayerStatistic(OfflinePlayer player, Statistic statistic) {
        if (player.isOnline()) return player.getPlayer().getStatistic(statistic);
        //Default server world always be the first of the list
        File worldFolder = new File(Bukkit.getServer().getWorlds().get(0).getWorldFolder(), "stats");
        File playerStatistics = new File(worldFolder, player.getUniqueId().toString() + ".json");
        if(playerStatistics.exists()){
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) parser.parse(new FileReader(playerStatistics));
            } catch (IOException | ParseException e) {
                Bukkit.getLogger().log(Level.WARNING, "Failed to read player statistics file " + player.getName(), e);
            }
            StringBuilder statisticNmsName = new StringBuilder("stat.");
            for(char character : statistic.name().toCharArray()) {
                if(statisticNmsName.charAt(statisticNmsName.length() - 1) == '_') {
                    statisticNmsName.setCharAt(statisticNmsName.length() - 1, Character.toUpperCase(character));
                }else {
                    statisticNmsName.append(Character.toLowerCase(character));
                }
            }
            if(jsonObject!=null&&jsonObject.containsKey(statisticNmsName.toString())) {
                return (long) jsonObject.get(statisticNmsName.toString());
            }else {
                //This statistic has not yet been saved to file, so it is 0
                return 0;
            }
        }
        //Any statistic can be -1?
        return -1;
    }

    public static ArrayList<JSONObject> getBanObjects(BanList.Type type) {
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        for (BanEntry entry : Bukkit.getBanList(type).getBanEntries()) {
            LinkedHashMap<String, Object> entries = new LinkedHashMap<>();
            String operator = entry.getSource().replaceAll("(?=\\[).*(?=])(])", "").replaceAll("§+[a-zA-Z0-9]", "");
            entries.put("create", new Date(entry.getCreated().getTime()).toLocalDate().toString());
            entries.put("created_timestamp", entry.getCreated().getTime());
            entries.put("using_name", type == BanList.Type.NAME);
            entries.put("expire", entry.getExpiration() == null ? "永久封禁" : new Date(entry.getExpiration().getTime()).toLocalDate().toString());
            entries.put("expire_timestamp", entry.getExpiration() == null ? Date.from(Instant.now()).getTime() * 2 : entry.getExpiration().getTime());
            entries.put("source", operator);
            entries.put("target", entry.getTarget());
            entries.put("reason", entry.getReason().replaceAll("§+[a-zA-Z0-9]", ""));
            entries.put("active", entry.getExpiration() == null || entry.getExpiration().before(Date.from(Instant.now())));
            jsonObjects.add(new JSONObject(entries));
        }
        return jsonObjects;
    }
}
