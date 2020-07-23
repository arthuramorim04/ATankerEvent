package com.arthuramorim;

import com.arthuramorim.commands.StartCommand;
import com.arthuramorim.config.ConfigsAPI;
import com.arthuramorim.listener.EventStatus;
import com.arthuramorim.listener.PlayerDeath;
import com.arthuramorim.manager.EventManager;

public class TresheTanker extends APlugin{

    private ConfigsAPI eventConfig;
    private EventManager eventManager;
//    private net.sacredlabyrinth.phaed.simpleclans.SimpleClans simpleClans;

    public TresheTanker() {
        super("TresherTanker", "1.0", "Arthur Amorim");
    }



    @Override
    public void start() {
        eventConfig = new ConfigsAPI(this,"eventConfig");
        eventManager = new EventManager(this);
        eventManager.setEventStatus(EventStatus.FINISHED);
        new PlayerDeath(this);
        commandRegister();
//        SimpleClans.hook();
//        if(SimpleClans.use){
//            simpleClans = new net.sacredlabyrinth.phaed.simpleclans.SimpleClans();
//        }else{
//            System.out.println("Faltando dependencia: SimpleClans");
//        }
    }

    @Override
    public void load() {

    }

    @Override
    public void stop() {

    }

    public void commandRegister(){
        getCommand("tanker").setExecutor(new StartCommand(this));
    }

    public ConfigsAPI getEventConfig() {
        return eventConfig;
    }


    public EventManager getEventManager() {
        return eventManager;
    }

//    public net.sacredlabyrinth.phaed.simpleclans.SimpleClans getSimpleClans() {
//        return simpleClans;
//    }
}
