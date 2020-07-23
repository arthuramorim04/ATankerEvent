package com.arthuramorim.hooks;

import com.arthuramorim.ACore;
import com.arthuramorim.TresheTanker;

public class SimpleClans {

    private TresheTanker plugin;

    public static boolean use = false;


    public static void hook() {
        if (ACore.getACore().getServer().getPluginManager().isPluginEnabled("SimpleClans")) {
            use = true;
            ACore.getHooks().add(ACore.getACore().getServer().getPluginManager().getPlugin("SimpleClans"));
        }
    }






}
