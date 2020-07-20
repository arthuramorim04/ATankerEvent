package com.arthuramorim.commands;

import com.arthuramorim.TresheTanker;
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
                    if (player.hasPermission("tanker.iniciar")) {
                        if (args[0].equalsIgnoreCase("iniciar")) {
                            if(plugin.getEventManager().isStarted()){
                                player.sendMessage(TextUtil.color("&cO evento ja foi iniciado"));
                            }else{
                                plugin.getStartEvent().startEvent();
                            }
                            return false;
                        }
                        if (args[0].equalsIgnoreCase("cancelar")) {
                            plugin.getEventManager().setCanceled(true);
                            return false;
                        }

                    } else {
                        player.sendMessage(TextUtil.color("&cVocê não tem permissão para iniciar esse evento."));
                    }

                    if (args[0].equalsIgnoreCase("entrar")) {
                        if(plugin.getEventManager().isStarted()){
                            if(plugin.getStartEvent().getEvent().getParticipants().contains(player)){
                                player.sendMessage(TextUtil.color("&aVoce ja esta participando do evento!"));
                                return false;
                            }else{
                                if(plugin.getStartEvent().getEvent().getParticipants().size() == 0 ||
                                        plugin.getStartEvent().getEvent().getParticipants().size() < plugin.getEventConfig().getConfigFile().getInt("maxPlayer")){
                                    plugin.getStartEvent().getEvent().getParticipants().add(player);
                                }else{
                                    player.sendMessage(TextUtil.color("&cO evento ja atingiu a capacidade maxima de participantes."));
                                }
                                return false;
                            }
                        }else{
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
                        plugin.getStartEvent().startEvent();
                        return false;
                    }
                    if (args[0].equalsIgnoreCase("cancelar")) {
                        plugin.getEventManager().setCanceled(true);
                        return false;
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

