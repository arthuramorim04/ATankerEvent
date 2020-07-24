package com.arthuramorim.manager;


import com.arthuramorim.TresheTanker;
import com.arthuramorim.listener.EventStatus;
import com.arthuramorim.listener.StartEvent;
import com.arthuramorim.utils.player.PlayerUtil;
import com.arthuramorim.utils.utils.MakeItem;
import com.arthuramorim.utils.utils.TextUtil;
import lombok.Getter;
import lombok.Setter;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
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
        MakeItem helmetMake = new MakeItem(configFile.getInt("set.helmet.id"), (byte) configFile.getInt("set.helmet.data"));
        ItemStack helmet = enchantSetAndWeapon(helmetMake, "helmet").build();
        player.getInventory().setHelmet(helmet);

        MakeItem armorMake = new MakeItem(configFile.getInt("set.armor.id"), (byte) configFile.getInt("set.armor.data"));
        ItemStack armor = enchantSetAndWeapon(armorMake, "armor").build();
        player.getInventory().setChestplate(armor);

        MakeItem legsMake = new MakeItem(configFile.getInt("set.legs.id"), (byte) configFile.getInt("set.legs.data"));
        ItemStack legs = enchantSetAndWeapon(legsMake, "legs").build();
        player.getInventory().setLeggings(legs);

        MakeItem bootsMake = new MakeItem(configFile.getInt("set.boots.id"), (byte) configFile.getInt("set.boots.data"));
        ItemStack boots = enchantSetAndWeapon(bootsMake, "boots").build();
        player.getInventory().setBoots(boots);

        MakeItem weaponMake = new MakeItem(configFile.getInt("set.weapon.id"), (byte) configFile.getInt("set.weapon.data"));
        ItemStack weapon = enchantSetAndWeapon(weaponMake, "weapon").build();
        player.getInventory().setItem(0,weapon);

        configFile.getConfigurationSection("items").getKeys(false).forEach(PostItem -> {
            ItemStack item = new MakeItem(configFile.getInt("items." + PostItem + ".id"), (byte) configFile.getInt("items." + PostItem + ".data")).build();
            item.setAmount(configFile.getInt("items." + PostItem + ".quantity"));
            player.getInventory().addItem(item);
        });


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
            clearInventoryPlayer(player);
            player.teleport(location);
            setItems(player);
            if (hasClan(player)) {
                chageFriendFire(player, true);
            }
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


    public boolean hasClan(Player player) {
        ClanPlayer clanPlayer = plugin.getSimpleClans().getClanManager().getClanPlayer(player);

        if (clanPlayer == null) {
            return false;
        } else {
            return true;
        }
    }

    public void chageFriendFire(Player player, Boolean status) {
        plugin.getClanPlayerManager(player).setFriendlyFire(status);
    }


    public boolean lastClanMember(Player deathPlayer) {
        ClanPlayer cp = plugin.getSimpleClans().getClanManager().getClanPlayer(deathPlayer);
        String tag = cp.getTag();

        for (ClanPlayer member : plugin.getSimpleClans().getClanManager().getClan(tag).getMembers()) {
            if (participants.contains(member)) {
                return false;
            }
        }
        return true;
    }

    public void clearInventoryPlayer(Player player) {

        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);

    }

    public MakeItem enchantSetAndWeapon(MakeItem item, String nameItem) {
        FileConfiguration configFile = plugin.getEventConfig().getConfigFile();
        configFile.getStringList("set." + nameItem + ".enchantList").forEach(enchant -> {
            try {
                String[] aux = null;
                String encanto = null;
                aux = enchant.trim().split(",");
                encanto = aux[0];
                Integer level = Integer.valueOf(aux[1]);

                item.addEnchantment(Enchantment.getByName(encanto), level);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao gerar encantamento " + enchant + " do item " + item);

            }
        });
        return item;
    }
    public void removePlayerForItemInInventory(){
        for (Player player : participants) {
            if (PlayerUtil.emptySlots(player) < player.getInventory().getSize()) {
                participants.remove(player);
                PlayerUtil.addBalance(player, plugin.getEventConfig().getConfigFile().getInt("priceAcess"));
                player.sendMessage(TextUtil.color(plugin.getEventConfig().getConfigFile().getString("messages.items_in_inv")));
            }
        }
    }
    public void setNewWinnerConfig(Player winner){
        try{
            String name = winner.getName();
            plugin.getEventConfig().getConfigFile().set("lastWinner", name);
            plugin.getEventConfig().getConfigFile().save(plugin.getEventConfig().getConfig());
            plugin.getEventConfig().reload();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
