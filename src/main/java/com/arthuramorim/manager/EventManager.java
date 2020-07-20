package com.arthuramorim.manager;


import com.arthuramorim.TresheTanker;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Set;

@Getter
@Setter
public class EventManager {

    private boolean canceled;
    private boolean started;

    private TresheTanker plugin;

    public EventManager(TresheTanker plugin) {
        this.plugin = plugin;
    }

    public void teleportPlayersToEvent(Set<Player> players){
        World world = Bukkit.getServer().getWorld(plugin.getEventConfig().getConfigFile().getString("position.world"));
        Integer x = plugin.getEventConfig().getConfigFile().getInt("position.x");
        Integer y = plugin.getEventConfig().getConfigFile().getInt("position.y");
        Integer z = plugin.getEventConfig().getConfigFile().getInt("position.z");
        Location location = new Location(world, x, y, z);
        for (Player player : players) {
            player.teleport(location);
        }
    }

}
