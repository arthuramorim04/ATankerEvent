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
                    Bukkit.broadcastMessage(TextUtil.color("&cO evento foi cancelado por um staff."));
                } else {


                    if (announcer <= event.getAnnouncers()) {
                        eventMessage(announcer, event.getAnnouncers(), event.getPriceAcess());
                    }

                    if (announcer == (event.getAnnouncers() + 1)) {
                        if(manager.getParticipants().size() >= event.getMinStart()){
                            Bukkit.broadcastMessage(TextUtil.color("&eEvento fechado! Participantes se preparem!"));
                            manager.teleportToCabin(manager.getParticipants());
                            manager.setEventStatus(EventStatus.CLOSED);
                        }else{
                            Bukkit.broadcastMessage(TextUtil.color("&cEvento cancelado por nao atingir o minimo de jogadores"));
                            manager.closeEvent();
                            manager.setEventStatus(EventStatus.FINISHED);
                        }
                    }

                    if (announcer == (event.getAnnouncers() + 2)) {
                        Bukkit.broadcastMessage(TextUtil.color("&aEvento iniciado! Que o melhor sobreviva!"));
                        manager.setEventStatus(EventStatus.IN_PROGRESS);
                        this.cancel();
                        manager.teleportToArena(manager.getParticipants());

                        //esse set vai ser substituido pela task que monitora os jogadores vivos
                    }


                }
            }
        }.runTaskTimer(plugin, 0L, event.getTimerAnnouncer() * 20);
    }


    protected void eventMessage(Integer currentAnnouncement, Integer maxEventAnnouncer, Integer priceAcess) {
        Bukkit.getServer().broadcastMessage(TextUtil.color("\n&a" +""+
                "&eEvento Tanker\n" +
                "&eParticipantes: " + manager.getParticipants().size() +
                "\n&eAnuncio: &f" + currentAnnouncement + "/" + maxEventAnnouncer +
                "\n&eValor para participar: &f" + priceAcess + "coins" +
                "\n&a"+"" +
                "\n&ePara participar digite &b/tanker entrar" +
                "\n&a"+""));
    }

    public Event getEvent() {
        return event;
    }
}
