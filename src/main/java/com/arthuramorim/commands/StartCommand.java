package com.arthuramorim.commands;

import com.arthuramorim.TresheTanker;
import com.arthuramorim.listener.EventStatus;
import com.arthuramorim.utils.player.PlayerUtil;
import com.arthuramorim.utils.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private TresheTanker plugin;

    public StartCommand(TresheTanker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        FileConfiguration config = plugin.getEventConfig().getConfigFile();

        if (command.getName().equalsIgnoreCase("tanker")) {
            if (commandSender instanceof Player) {

                Player player = (Player) commandSender;
                if (args.length == 0) {
                    player.sendMessage(TextUtil.color("&bEvento Tanker\n\n]" +
                            "&eDesenvolvido por Arthur Amorim\n" +
                            "&eVersao: 1.0\n" +
                            "&eDiscord: Arthur Amorim#9378 \n"));
                    return false;
                }
                if (args.length == 1) {

                    if (args[0].equalsIgnoreCase("sair")) {
                        plugin.getEventManager().getParticipants().remove(player);
                        PlayerUtil.addBalance(player, plugin.getEventConfig().getConfigFile().getInt("priceAcess"));
                        player.sendMessage(TextUtil.color(config.getString("messages.leave_event")));
                        return false;
                    }

                    if (args[0].equalsIgnoreCase("help")) {
                        if (player.hasPermission("tanker.staff")) {
                            player.sendMessage("\n&6Tanker" +
                                    "\n/tanker iniciar - inicia o evento" +
                                    "\n/tanker cancelar - cancela o evento" +
                                    "\n/tanker entrar - entrar no evento" +
                                    "\n/tanker sair - sair do evento" +
                                    "\n/tanker reload - recarregar configuracao");
                        } else {
                            player.sendMessage("");
                        }
                    }

                    if (args[0].equalsIgnoreCase("iniciar")) {
                        if (player.hasPermission("tanker.iniciar")) {
                            if (plugin.getEventManager().getEventStatus() != EventStatus.FINISHED) {
                                player.sendMessage(TextUtil.color(config.getString("messages.is_started")));
                            } else {
                                plugin.getEventManager().startEvent();
                            }
                            return false;
                        } else {
                            player.sendMessage(TextUtil.color(config.getString("messages.not_have_permission")));
                            return false;
                        }

                    }
                    if (args[0].equalsIgnoreCase("cancelar")) {
                        if (player.hasPermission("tanker.cancelar")) {
                            plugin.getEventManager().setEventStatus(EventStatus.FINISHED);
                        } else {
                            player.sendMessage(TextUtil.color(config.getString("messages.not_have_permission")));
                        }
                        return false;

                    }

                    if (args[0].equalsIgnoreCase("reload")) {
                        if (player.hasPermission("tanker.reload")) {
                            plugin.getEventConfig().reload();
                            player.sendMessage(TextUtil.color("&aConfiguração do Evento Tanker foi recarregada."));

                        } else {
                            player.sendMessage(TextUtil.color(config.getString("messages.not_have_permission")));
                        }
                        return false;

                    }

                    if (args[0].equalsIgnoreCase("entrar")) {
                        if (plugin.getEventManager().getEventStatus() != EventStatus.FINISHED) {
                            if (plugin.getEventManager().getParticipants().contains(player)) {
                                player.sendMessage(TextUtil.color(config.getString("messages.is_participant")));
                                return false;
                            } else {
                                if (plugin.getEventManager().getEventStatus() == EventStatus.CLOSED) {
                                    player.sendMessage(TextUtil.color(config.getString("messages.close")));

                                } else {
                                    if (plugin.getEventManager().getParticipants().size() == 0 ||
                                            plugin.getEventManager().getParticipants().size() < plugin.getEventConfig().getConfigFile().getInt("maxPlayer")) {
                                        if (PlayerUtil.getBalance(player) >= plugin.getEventConfig().getConfigFile().getDouble("priceAcess")) {
                                            if (PlayerUtil.emptySlots(player) == player.getInventory().getSize()) {
                                                PlayerUtil.removeBalance(player, plugin.getEventConfig().getConfigFile().getDouble("priceAcess"));
                                                plugin.getEventManager().getParticipants().add(player);
                                                player.sendMessage(TextUtil.color(config.getString("messages.join_event")));
                                            } else {
                                                player.sendMessage(TextUtil.color(config.getString("messages.items_in_inv")));
                                            }
                                        } else {
                                            player.sendMessage(TextUtil.color(config.getString("messages.not_have_money")));
                                        }
                                    } else {
                                        player.sendMessage(TextUtil.color(config.getString("messages.max_participants")));
                                    }
                                }
                                return false;
                            }
                        } else {
                            player.sendMessage(TextUtil.color(config.getString("messages.not_started")));
                        }
                        return false;
                    }

                }
                if (args.length > 1) {
                    player.sendMessage(TextUtil.color("&cSub-comando invalido"));
                    player.sendMessage(TextUtil.color("&a---- &6Evento Tanker &a----\n\n &einiciar evento - /tanker iniciar\n &ecancelar evento - /tanker cancelar\n &eajuda - /tanker\n"));
                    return false;
                }
            } else {

                if (args.length == 0) {
                    Bukkit.getConsoleSender().sendMessage(TextUtil.color("&a---- &6Evento Tanker &a----\n\n &einiciar evento - /tanker iniciar\n &eajuda - /tanker\n"));
                    return false;
                }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("iniciar")) {
                        if (plugin.getEventManager().getEventStatus() != EventStatus.FINISHED) {
                            Bukkit.getConsoleSender().sendMessage(TextUtil.color(config.getString("messages.is_started")));
                        } else {
                            plugin.getEventManager().startEvent();
                            return false;
                        }
                    }
                    if (args[0].equalsIgnoreCase("cancelar")) {
                        if (plugin.getEventManager().getEventStatus() != EventStatus.FINISHED) {
                            plugin.getEventManager().setEventStatus(EventStatus.FINISHED);
                            return false;
                        } else {
                            Bukkit.getConsoleSender().sendMessage(TextUtil.color(config.getString("messages.not_started")));
                        }
                    }
                }
                if (args.length > 1) {
                    Bukkit.getConsoleSender().sendMessage(TextUtil.color("&cSub-comando invalido"));


                }

            }
        }
        return false;
    }
}

