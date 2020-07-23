package com.arthuramorim.entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event {

    private Integer maxPlayer;
    private Integer minStart;
    private Integer priceAcess;
    private Integer announcers;
    private Integer timerAnnouncer;


    public Event(Integer maxPlayer, Integer minStart, Integer priceAcess, Integer announcers, Integer timerAnnouncer) {
        this.maxPlayer = maxPlayer;
        this.minStart = minStart;
        this.priceAcess = priceAcess;
        this.announcers = announcers;
        this.timerAnnouncer = timerAnnouncer;
    }

}
