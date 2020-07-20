package com.arthuramorim.listener;

import com.arthuramorim.TresheTanker;
import com.arthuramorim.entity.Event;
import com.arthuramorim.manager.EventManager;
import com.arthuramorim.utils.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

public class StartEvent {

    private TresheTanker plugin;

    private Event event;

    public StartEvent(TresheTanker plugin) {
        this.plugin = plugin;
    }

    public void startEvent() {

        FileConfiguration eventConfig = plugin.getEventConfig().getConfigFile();
        if(plugin.getEventManager().isCanceled() == false) return;

        plugin.getEventManager().setCanceled(false);

        event = new Event(
                eventConfig.getInt("maxPlayer")
                , eventConfig.getInt("minStart")
                , eventConfig.getInt("priceAcess")
                , eventConfig.getInt("announcer")
                , eventConfig.getInt("timerAnnouncer"));

        plugin.getEventManager().setStarted(true);
        new BukkitRunnable() {
            int announcer = 0;

            @Override
            public void run() {
                announcer++;
                if (plugin.getEventManager().isCanceled()) {
                    this.cancel();
                    Bukkit.broadcastMessage(TextUtil.color("&cO evento foi cancelado por um staff."));
                    plugin.getEventManager().setStarted(false);
                } else {


                    if (announcer <= event.getAnnouncers()) {
                        eventMessage(announcer, event.getAnnouncers(), event.getPriceAcess());
                    }
                    if (announcer == 5) {
                        Bukkit.broadcastMessage(TextUtil.color("&aEvento iniciado!"));
                        this.cancel();
                        plugin.getEventManager().teleportPlayersToEvent(event.getParticipants());
                        plugin.getEventManager().setStarted(false);
                    }


                }
            }
        }.runTaskTimer(plugin, 0L, event.getTimerAnnouncer() * 20);
    }


    protected void eventMessage(Integer currentAnnouncement, Integer maxEventAnnouncer, Integer priceAcess) {
        Bukkit.getServer().broadcastMessage(TextUtil.color("\n\n  &eEvento Tanker\n\n" +
                "     &eParticipantes: " + event.getParticipants().size()+
                "\n     &eAnuncio: &f" + currentAnnouncement + "/" + maxEventAnnouncer +
                "\n     &eValor para participar: &f" + priceAcess + "coins \n" +
                "\n    &ePara participar digite &b/tanker entrar\n\n"));
    }

    public Event getEvent() {
        return event;
    }
}
