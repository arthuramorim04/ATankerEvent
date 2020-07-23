package com.arthuramorim.listener;

import com.arthuramorim.TresheTanker;
import com.arthuramorim.manager.EventManager;
import com.arthuramorim.utils.utils.TextUtil;
import org.bukkit.Bukkit;
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

        if (plugin.getEventManager().getEventStatus().equals(EventStatus.IN_PROGRESS)) {
            EventManager eventManager = plugin.getEventManager();

            Player player = event.getEntity().getPlayer();
            Player killer = event.getEntity().getKiller();

            if (eventManager.getParticipants().contains(event.getEntity().getPlayer())) {
                eventManager.getParticipants().remove(event.getEntity().getPlayer());
                player.sendMessage(TextUtil.color("&cVocê foi eliminado do evento por ") + killer.getName());
                event.getDrops().clear();
                if (eventManager.getParticipants().size() == 1) {
                    Bukkit.broadcastMessage(TextUtil.color("&aParabens! O vencedor do evento tanker foi ") + killer.getName());
                    killer.getInventory().clear();
                    killer.getInventory().setHelmet(null);
                    killer.getInventory().setChestplate(null);
                    killer.getInventory().setLeggings(null);
                    killer.getInventory().setBoots(null);
                    eventManager.setReward(killer);
                    eventManager.teleportWinner(killer);
                    eventManager.closeEvent();
                }
            }


        }
        return;
    }
}
