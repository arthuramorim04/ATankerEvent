package com.arthuramorim.listener;

import com.arthuramorim.TresheTanker;
import com.arthuramorim.entity.Event;
import com.arthuramorim.manager.EventManager;
import com.arthuramorim.utils.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

public class StartEvent {

    private EventManager manager;
    private TresheTanker plugin;

    private Event event;

    public StartEvent(EventManager eventManager, TresheTanker plugin) {
        this.plugin = plugin;
        this.manager = eventManager;
    }

    public void startEvent() {

        FileConfiguration eventConfig = plugin.getEventConfig().getConfigFile();
        event = new Event(
                eventConfig.getInt("maxPlayer")
                , eventConfig.getInt("minStart")
                , eventConfig.getInt("priceAcess")
                , eventConfig.getInt("announcer")
                , eventConfig.getInt("timerAnnouncer"));
        new BukkitRunnable() {
            int announcer = 0;

            @Override
            public void run() {
                announcer++;
                if (manager.getEventStatus() == EventStatus.FINISHED) {
                    this.cancel();
                    Bukkit.broadcastMessage(TextUtil.color(eventConfig.getString("messages.canceled")));
                } else {


                    if (announcer <= event.getAnnouncers()) {
                        eventMessage(announcer, event.getAnnouncers(), event.getPriceAcess());
                    }

                    if (announcer == (event.getAnnouncers() + 1)) {

                        manager.removePlayerForItemInInventory();

                        if (manager.getParticipants().size() >= event.getMinStart()) {
                            Bukkit.broadcastMessage(TextUtil.color(eventConfig.getString("messages.close")));
                            manager.teleportToCabin(manager.getParticipants());
                            manager.setEventStatus(EventStatus.CLOSED);
                        } else {
                            Bukkit.broadcastMessage(TextUtil.color(eventConfig.getString("messages.not_have_min")));
                            manager.closeEvent();
                            manager.setEventStatus(EventStatus.FINISHED);
                            this.cancel();
                        }
                    }

                    if (announcer == (event.getAnnouncers() + 2)) {
                        Bukkit.broadcastMessage(TextUtil.color(eventConfig.getString("messages.start")));
                        manager.setEventStatus(EventStatus.IN_PROGRESS);
                        this.cancel();
                        manager.teleportToArena(manager.getParticipants());
                    }


                }
            }
        }.runTaskTimer(plugin, 0L, event.getTimerAnnouncer() * 20);
    }


    protected void eventMessage(Integer currentAnnouncement, Integer maxEventAnnouncer, Integer priceAcess) {
        Bukkit.getServer().broadcastMessage(TextUtil.color("\n&eEvento Tanker"));
        Bukkit.getServer().broadcastMessage(TextUtil.color(
                "\n&eParticipantes: &f" + manager.getParticipants().size() +
                        "\n&eAnúncio: &f" + currentAnnouncement + "/" + maxEventAnnouncer +
                        "\n&eValor para participar: &f" + priceAcess + " coins"));
        Bukkit.getServer().broadcastMessage(TextUtil.color("\n&ePara participar digite &b/tanker entrar"));

    }

}
