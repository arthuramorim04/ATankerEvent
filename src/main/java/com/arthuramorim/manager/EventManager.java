package com.arthuramorim.manager;


import com.arthuramorim.TresheTanker;
import com.arthuramorim.listener.EventStatus;
import com.arthuramorim.listener.StartEvent;
import com.arthuramorim.utils.player.PlayerUtil;
import com.arthuramorim.utils.utils.MakeItem;
import com.arthuramorim.utils.utils.TextUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class EventManager {

    private EventStatus eventStatus;
    private Set<Player> participants;

    private TresheTanker plugin;

    public EventManager(TresheTanker plugin) {
        this.plugin = plugin;
    }

    public void startEvent() {
        StartEvent eventStarted = new StartEvent(this, plugin);
        participants = new HashSet<>();
        eventStatus = EventStatus.STARTED;
        eventStarted.startEvent();
    }

    public void closeEvent() {
        eventStatus = EventStatus.FINISHED;
        participants.clear();
    }

    public void setReward(Player winner) {
        FileConfiguration configFile = plugin.getEventConfig().getConfigFile();

        PlayerUtil.addBalance(winner, configFile.getInt("reward.coins"));
        if (configFile.getBoolean("reward.commandEnable")) {
            List<String> stringList = configFile.getStringList("reward.cmds");
            this.dispachCommands(stringList, winner);
        }
        if (configFile.getBoolean("reward.itemEnable")) {

            configFile.getConfigurationSection("reward.items").getKeys(false).forEach(item -> {
                MakeItem makeItem = new MakeItem(configFile.getInt("reward.items." + item + ".id"), (byte) configFile.getInt("reward.items." + item + ".data"));
                try {
                    makeItem.setAmount(configFile.getInt("reward.items." + item + ".quantity"));
                } catch (Exception e) {
                    e.printStackTrace();

                }
                makeItem.setName(configFile.getString("reward.items." + item + ".name"));
                makeItem.setLore((ArrayList<String>) TextUtil.color(configFile.getStringList("reward.items." + item + ".lore")));

                configFile.getStringList("reward.items." + item + ".enchantList").forEach(enchant -> {
                    try {
                        String[] aux = null;
                        String encanto = null;
                        aux = enchant.trim().split(",");
                        encanto = aux[0];
                        Integer level = Integer.valueOf(aux[1]);

                        makeItem.addEnchantment(Enchantment.getByName(encanto), level);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Erro ao gerar encantamento " + enchant + " do item " + makeItem);

                    }
                });

                winner.getInventory().addItem(makeItem.build());
            });
        }

    }

    public void setItems(Player player) {

        FileConfiguration configFile = plugin.getEventConfig().getConfigFile();
        for (Player participant : this.participants) {
            participant.getInventory().setHelmet(new MakeItem(configFile.getInt("set.helmet.id")).build());
            participant.getInventory().setChestplate(new MakeItem(configFile.getInt("set.armor.id")).build());
            participant.getInventory().setLeggings(new MakeItem(configFile.getInt("set.legs.id")).build());
            participant.getInventory().setBoots(new MakeItem(configFile.getInt("set.boots.id")).build());

            configFile.getConfigurationSection("items").getKeys(false).forEach(PostItem -> {
                ItemStack item = new MakeItem(configFile.getInt("items." + PostItem + ".id"), (byte) configFile.getInt("items." + PostItem + ".data")).build();
                item.setAmount(configFile.getInt("items." + PostItem + ".quantity"));
                player.getInventory().addItem(item);
            });
        }

    }

    public void teleportToCabin(Set<Player> players) {
        World world = Bukkit.getServer().getWorld(plugin.getEventConfig().getConfigFile().getString("cabin.world"));
        Integer x = plugin.getEventConfig().getConfigFile().getInt("cabin.x");
        Integer y = plugin.getEventConfig().getConfigFile().getInt("cabin.y");
        Integer z = plugin.getEventConfig().getConfigFile().getInt("cabin.z");
        Location location = new Location(world, x, y, z);
        for (Player player : players) {
            player.teleport(location);
        }
    }

    public void teleportToArena(Set<Player> players) {
        World world = Bukkit.getServer().getWorld(plugin.getEventConfig().getConfigFile().getString("cabin.world"));
        Integer x = plugin.getEventConfig().getConfigFile().getInt("arena.x");
        Integer y = plugin.getEventConfig().getConfigFile().getInt("arena.y");
        Integer z = plugin.getEventConfig().getConfigFile().getInt("arena.z");
        Location location = new Location(world, x, y, z);
        for (Player player : players) {
            player.teleport(location);
            setItems(player);
        }
    }

    public void teleportWinner(Player winner) {
        World world = Bukkit.getServer().getWorld(plugin.getEventConfig().getConfigFile().getString("cabin.world"));
        Integer x = plugin.getEventConfig().getConfigFile().getInt("cabin.x");
        Integer y = plugin.getEventConfig().getConfigFile().getInt("cabin.y");
        Integer z = plugin.getEventConfig().getConfigFile().getInt("cabin.z");
        Location location = new Location(world, x, y, z);

        winner.teleport(location);

    }


    private void dispachCommands(List<String> listCommands, Player winner) {
        for (String command : listCommands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", winner.getName()));
        }
    }

//    protected void enableDamagerAll(Player player) {
//        plugin.getSimpleClans().getClanManager().getClanPlayer(player).setFriendlyFire(true);
//    }


//    public boolean lastClanMember(Player deathPlayer) {
//        Clan clan = plugin.getSimpleClans().getClanManager().getClanPlayer(deathPlayer).getClan();
//
//        for (Player participant : participants) {
//            if (plugin.getSimpleClans().getClanManager().getClanPlayer(participant).getClan().equals(clan)) {
//                return false;
//            }
//        }
//
//        return true;
//    }

}
