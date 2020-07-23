package com.arthuramorim.commands;

import com.arthuramorim.TresheTanker;
import com.arthuramorim.listener.EventStatus;
import com.arthuramorim.utils.hooks.VaultAPI;
import com.arthuramorim.utils.player.PlayerUtil;
import com.arthuramorim.utils.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private TresheTanker plugin;

    public StartCommand(TresheTanker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("tanker")) {
            if (commandSender instanceof Player) {

                Player player = (Player) commandSender;
                if (args.length == 0) {
                    player.sendMessage(TextUtil.color("&a---- &6Evento Tanker &a----\n\n &einiciar evento - /tanker iniciar\n &eajuda - /tanker\n"));
                    return false;
                }
                if (args.length == 1) {

                    if(args[0].equalsIgnoreCase("sair")){
                        plugin.getEventManager().getParticipants().remove(player);
                        PlayerUtil.addBalance(player,plugin.getEventConfig().getConfigFile().getInt("priceAcess"));
                        return false;
                    }

                    if (args[0].equalsIgnoreCase("iniciar")) {
                        if (player.hasPermission("tanker.iniciar")) {
                            if (plugin.getEventManager().getEventStatus() != EventStatus.FINISHED) {
                                player.sendMessage(TextUtil.color("&cO evento ja foi iniciado"));
                            } else {
                                plugin.getEventManager().startEvent();
                            }
                            return false;
                        } else {
                            player.sendMessage(TextUtil.color("&cVocê não tem permissão para iniciar esse evento."));
                            return false;
                        }

                    }
                    if (args[0].equalsIgnoreCase("cancelar")) {
                        if (player.hasPermission("tanker.cancelar")) {
                            plugin.getEventManager().setEventStatus(EventStatus.FINISHED);
                        } else {
                            player.sendMessage(TextUtil.color("&cVocê não tem permissão para iniciar esse evento."));
                        }
                        return false;

                    }

                    if (args[0].equalsIgnoreCase("reload")) {
                        if (player.hasPermission("tanker.reload")) {
                            plugin.getEventConfig().reload();
                            player.sendMessage(TextUtil.color("&aConfiguração do Evento Tanker foi recarregada."));

                        } else {
                            player.sendMessage(TextUtil.color("&cVocê não tem permissão para isso."));
                        }
                        return false;

                    }

                    if (args[0].equalsIgnoreCase("entrar")) {
                        if (plugin.getEventManager().getEventStatus() != EventStatus.FINISHED) {
                            if (plugin.getEventManager().getParticipants().contains(player)) {
                                player.sendMessage(TextUtil.color("&aVoce ja esta participando do evento!"));
                                return false;
                            } else {
                                if (plugin.getEventManager().getEventStatus() == EventStatus.CLOSED) {
                                    player.sendMessage(TextUtil.color("&eO evento esta fechado! Aguarde o proximo para entrar."));

                                } else {
                                    if (plugin.getEventManager().getParticipants().size() == 0 ||
                                            plugin.getEventManager().getParticipants().size() < plugin.getEventConfig().getConfigFile().getInt("maxPlayer")) {
                                       if(PlayerUtil.getBalance(player) >= plugin.getEventConfig().getConfigFile().getDouble("priceAcess")){
                                           PlayerUtil.removeBalance(player,plugin.getEventConfig().getConfigFile().getDouble("priceAcess"));
                                           plugin.getEventManager().getParticipants().add(player);
                                           player.sendMessage(TextUtil.color("&aVoce agora esta participando do evento! Aguarde o teleporte!"));
                                        }else{
                                           player.sendMessage(TextUtil.color("&bSaldo insuficiente para participar do evento!"));
                                       }
                                    } else {
                                        player.sendMessage(TextUtil.color("&cO evento ja atingiu a capacidade maxima de participantes."));
                                    }
                                }
                                return false;
                            }
                        } else {
                            player.sendMessage(TextUtil.color("&cOps! Parece que esse evento nao esta ativo."));
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
                            Bukkit.getConsoleSender().sendMessage(TextUtil.color("&cEvento Tanker ja foi iniciado."));
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
                            Bukkit.getConsoleSender().sendMessage(TextUtil.color("&cEvento nao iniciado."));
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

