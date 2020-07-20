package com.arthuramorim.entity;


import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Event {

    private Integer maxPlayer;
    private Integer minStart;
    private Integer priceAcess;
    private Integer announcers;
    private Integer timerAnnouncer;
    private Set<Player> participants;


    public Event(Integer maxPlayer, Integer minStart, Integer priceAcess, Integer announcers, Integer timerAnnouncer) {
        participants = new HashSet<>();
        this.maxPlayer = maxPlayer;
        this.minStart = minStart;
        this.priceAcess = priceAcess;
        this.announcers = announcers;
        this.timerAnnouncer = timerAnnouncer;
    }

}
