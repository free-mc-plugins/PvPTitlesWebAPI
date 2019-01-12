package com.ericlam.mc.api;

import com.alternacraft.pvptitles.Misc.Rank;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.util.MojangNameLookup;
import org.json.simple.JSONObject;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.UUID;

class APIDatas {
    private String uuid;
    private String playername;
    private int fame;
    private String rank;
    private int kills;
    private int deaths;
    private double kdr;
    private long seconds;
    private Duration duration;

    APIDatas(UUID uuid, OfflinePlayer player, int fame, Rank rank, int kills, int deaths, long seconds){
        this.uuid = uuid.toString();
        this.playername = player.getName()!=null ? player.getName() : MojangNameLookup.lookupName(uuid);
        this.fame = fame;
        this.rank = rank.getDisplay();
        this.kills = kills;
        this.deaths = deaths;
        this.kdr = (double) this.kills / this.deaths;
        this.seconds = seconds;
        this.duration = Duration.ofSeconds(seconds);
    }

    JSONObject toJSONObject(){
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        map.put("uuid",uuid);
        map.put("player_name",playername);
        map.put("fame",fame);
        map.put("rank",getRank());
        map.put("kills",kills);
        map.put("deaths",deaths);
        map.put("kdr",getKdr());
        map.put("duration",getDuration());
        map.put("seconds", seconds);
        return new JSONObject(map);
    }

    private String getDuration(){
        long day = duration.toDays();
        long hour = duration.toHours();
        long mins = duration.toMinutes();
        if (day != 0) return day+" 天";
        else if (hour != 0) return hour+" 小時";
        else if (mins != 0) return mins+" 分鐘";
        else return "剛剛";
    }

    private String getRank(){
        String rank = this.rank.split(" ")[0];
        return rank.replaceAll("§+[a-zA-Z0-9]","");
    }

    private double getKdr(){
        try {
            String kdr = new DecimalFormat("#.##").format(this.kdr);
            return Double.parseDouble(kdr!=null ? kdr : "0.0");
        }catch (NumberFormatException e){
            return Math.rint(kdr);
        }

    }
}
