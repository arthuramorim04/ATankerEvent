package com.arthuramorim.listener;

import com.arthuramorim.TresheTanker;
import com.arthuramorim.manager.EventManager;
import com.arthuramorim.utils.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    private TresheTanker plugin;

    public PlayerDeath(TresheTanker plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        FileConfiguration config = plugin.getEventConfig().getConfigFile();

        if (plugin.getEventManager().getEventStatus().equals(EventStatus.IN_PROGRESS)) {
            EventManager eventManager = plugin.getEventManager();

            Player player = event.getEntity().getPlayer();
            Player killer = event.getEntity().getKiller();

            if (eventManager.getParticipants().contains(event.getEntity().getPlayer())) {
                eventManager.getParticipants().remove(event.getEntity().getPlayer());
                player.sendMessage(TextUtil.color(config.getString("messages.elimined").replace("%killer%", killer.getName())));
                event.getDrops().clear();

                if (eventManager.hasClan(player)) {
                    if (eventManager.lastClanMember(player)) {
                        eventManager.chageFriendFire(player, false);
                    }
                }

                if (eventManager.getParticipants().size() == 1) {
                    Bukkit.broadcastMessage(TextUtil.color(config.getString("messages.winner")).replace("%player%", killer.getName()));
                    eventManager.clearInventoryPlayer(killer);
                    eventManager.setReward(killer);
                    eventManager.teleportWinner(killer);
                    if (eventManager.hasClan(player)) {
                        if (eventManager.lastClanMember(player)) {
                            eventManager.chageFriendFire(player, false);
                        }

                    }
                    eventManager.setNewWinnerConfig(killer);
                    eventManager.closeEvent();
                }
            }


        }
        return;
    }
}
