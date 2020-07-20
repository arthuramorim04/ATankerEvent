package com.arthuramorim.config;

import com.arthuramorim.TresheTanker;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigsAPI {

    private File config;
    private FileConfiguration configFile;

    public ConfigsAPI(TresheTanker plugin, String nmConfig) {
        config = new File(plugin.getDataFolder(), nmConfig+".yml");
        if (!config.exists()) {
            config.getParentFile().mkdirs();
            plugin.saveResource(nmConfig+".yml", false);
        }

        configFile = new YamlConfiguration();
        try {
            configFile.load(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload(){
        configFile = YamlConfiguration.loadConfiguration(config);
    }

    public FileConfiguration getConfigFile() {
        return configFile;
    }
}