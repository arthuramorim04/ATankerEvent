package com.arthuramorim.tag;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import com.arthuramorim.TresheTanker;
import com.arthuramorim.utils.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AddNewTagLC implements Listener {

    private TresheTanker plugin;

    public AddNewTagLC(TresheTanker plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void alterTag(ChatMessageEvent event){
        String lastWinner = plugin.getEventConfig().getConfigFile().getString("lastWinner");
        System.out.println(lastWinner);
        if(event.getSender().getName().equals(lastWinner)){
            event.setTagValue("tanker", TextUtil.color(plugin.getEventConfig().getConfigFile().getString("tag")));
        }else{
            event.setTagValue("tanker", "");
        }
    }

}
