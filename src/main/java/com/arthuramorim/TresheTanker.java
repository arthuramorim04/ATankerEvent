package com.arthuramorim;

import com.arthuramorim.commands.StartCommand;
import com.arthuramorim.config.ConfigsAPI;
import com.arthuramorim.listener.StartEvent;
import com.arthuramorim.manager.EventManager;

public class TresheTanker extends APlugin{

    private ConfigsAPI eventConfig;
    private StartEvent startEvent;
    private EventManager eventManager;

    public TresheTanker() {
        super("TresherTanker", "1.0", "Arthur Amorim");
    }



    @Override
    public void start() {
        eventConfig = new ConfigsAPI(this,"eventConfig");
        startEvent = new StartEvent(this);
        eventManager = new EventManager(this);
        eventManager.setCanceled(true);

        commandRegister();
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

    public StartEvent getStartEvent() {
        return startEvent;
    }

    public EventManager getEventManager() {
        return eventManager;
    }
}
