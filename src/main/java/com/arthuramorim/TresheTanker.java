package com.arthuramorim;

import com.arthuramorim.commands.StartCommand;
import com.arthuramorim.config.ConfigsAPI;
import com.arthuramorim.listener.EventStatus;
import com.arthuramorim.listener.PlayerDeath;
import com.arthuramorim.manager.EventManager;
import com.arthuramorim.tag.AddNewTagLC;
import com.arthuramorim.utils.utils.TextUtil;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TresheTanker extends APlugin{

    private ConfigsAPI eventConfig;
    private EventManager eventManager;
    private SimpleClans simpleClans;

    public TresheTanker() {
        super("TresherTanker", "1.0", "Arthur Amorim");
    }



    @Override
    public void start() {
        eventConfig = new ConfigsAPI(this,"eventConfig");
        eventManager = new EventManager(this);
        eventManager.setEventStatus(EventStatus.FINISHED);
        new PlayerDeath(this);
        new AddNewTagLC(this);
        commandRegister();

        if(!hookSimpleClans()){
            Bukkit.getConsoleSender().sendMessage(TextUtil.color("&cNao foi encontrado o plugin SimpleClans"));
            this.disablePlugin();
        }
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

    public net.sacredlabyrinth.phaed.simpleclans.SimpleClans getSimpleClans() {
        return simpleClans;
    }

    public boolean hookSimpleClans() {
        try {
            for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
                if (plugin instanceof SimpleClans) {
                    this.simpleClans = (SimpleClans) plugin;
                    return true;
                }
            }
        } catch (NoClassDefFoundError e) {
            return false;
        }

        return false;
    }

    public ClanPlayer getClanPlayerManager(Player player) {
        return this.simpleClans.getClanManager().getClanPlayer(player);
    }


}
