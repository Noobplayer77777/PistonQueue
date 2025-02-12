/*
 * #%L
 * PistonQueue
 * %%
 * Copyright (C) 2021 AlexProgrammerDE
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package net.pistonmaster.pistonqueue.bungee.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.pistonmaster.pistonqueue.bungee.PistonQueue;
import net.pistonmaster.pistonqueue.bungee.QueueAPI;
import net.pistonmaster.pistonqueue.bungee.utils.Config;
import net.pistonmaster.pistonqueue.bungee.utils.StorageTool;

import java.util.*;

public final class MainCommand extends Command implements TabExecutor {
    private static final String[] commands = {"help", "version", "stats"};
    private static final String[] adminCommands = {"reload", "shadowban", "unshadowban"};
    private final PistonQueue plugin;

    public MainCommand(PistonQueue plugin) {
        super("pistonqueue", null, "pq");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0)
            help(sender);

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "version":
                    sendLine(sender);
                    sender.sendMessage(new ComponentBuilder("PistonQueue").color(ChatColor.GOLD).create());
                    sender.sendMessage(new ComponentBuilder("Version " + plugin.getDescription().getVersion() + " by").color(ChatColor.GOLD).create());
                    sender.sendMessage(new ComponentBuilder(plugin.getDescription().getAuthor()).color(ChatColor.GOLD).create());
                    sendLine(sender);
                    break;
                case "stats":
                    sendLine(sender);
                    sender.sendMessage(new ComponentBuilder("Queue stats").color(ChatColor.GOLD).create());
                    sender.sendMessage(new ComponentBuilder("Regular: ").color(ChatColor.GOLD).append(String.valueOf(QueueAPI.getRegularSize())).color(ChatColor.GOLD).bold(true).create());
                    sender.sendMessage(new ComponentBuilder("Priority: ").color(ChatColor.GOLD).append(String.valueOf(QueueAPI.getPrioritySize())).color(ChatColor.GOLD).bold(true).create());
                    sender.sendMessage(new ComponentBuilder("Veteran: ").color(ChatColor.GOLD).append(String.valueOf(QueueAPI.getVeteranSize())).color(ChatColor.GOLD).bold(true).create());
                    sendLine(sender);
                    break;
                case "reload":
                    if (sender.hasPermission(Config.ADMINPERMISSION)) {
                        plugin.processConfig();

                        sendLine(sender);
                        sender.sendMessage(new ComponentBuilder("PistonQueue").color(ChatColor.GOLD).create());
                        sender.sendMessage(new ComponentBuilder("Config reloaded").color(ChatColor.GREEN).create());
                        sendLine(sender);
                    } else {
                        noPermission(sender);
                    }
                    break;
                case "shadowban":
                    if (sender.hasPermission(Config.ADMINPERMISSION)) {
                        if (args.length > 1) {
                            if (plugin.getProxy().getPlayer(args[1]) != null) {
                                ProxiedPlayer player = plugin.getProxy().getPlayer(args[1]);

                                if (args.length > 2) {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(new Date());

                                    if (args[2].toLowerCase().endsWith("d")) {
                                        int d = Integer.parseInt(args[2].toLowerCase().replace("d", ""));

                                        calendar.add(Calendar.DAY_OF_WEEK, d);
                                    } else if (args[2].toLowerCase().endsWith("h")) {
                                        int h = Integer.parseInt(args[2].toLowerCase().replace("h", ""));

                                        calendar.add(Calendar.HOUR_OF_DAY, h);
                                    } else if (args[2].toLowerCase().endsWith("m")) {
                                        int m = Integer.parseInt(args[2].toLowerCase().replace("m", ""));

                                        calendar.add(Calendar.MINUTE, m);
                                    } else if (args[2].toLowerCase().endsWith("s")) {
                                        int s = Integer.parseInt(args[2].toLowerCase().replace("s", ""));

                                        calendar.add(Calendar.SECOND, s);
                                    } else {
                                        sendBanHelp(sender);
                                        break;
                                    }

                                    if (StorageTool.shadowBanPlayer(player, calendar.getTime())) {
                                        sendLine(sender);
                                        sender.sendMessage(new ComponentBuilder("PistonQueue").color(ChatColor.GOLD).create());
                                        sender.sendMessage(new ComponentBuilder("Successfully shadowbanned " + player.getName() + "!").color(ChatColor.GREEN).create());
                                        sendLine(sender);
                                    } else {
                                        sendLine(sender);
                                        sender.sendMessage(new ComponentBuilder("PistonQueue").color(ChatColor.GOLD).create());
                                        sender.sendMessage(new ComponentBuilder(player.getName() + " is already shadowbanned!").color(ChatColor.RED).create());
                                        sendLine(sender);
                                    }
                                } else {
                                    sendBanHelp(sender);
                                }
                            } else {
                                sendLine(sender);
                                sender.sendMessage(new ComponentBuilder("PistonQueue").color(ChatColor.GOLD).create());
                                sender.sendMessage(new ComponentBuilder("The player " + args[1] + " was not found!").color(ChatColor.GOLD).create());
                                sendLine(sender);
                            }
                        } else {
                            sendBanHelp(sender);
                        }
                    } else {
                        noPermission(sender);
                    }
                    break;
                case "unshadowban":
                    if (sender.hasPermission(Config.ADMINPERMISSION)) {
                        if (args.length > 1) {
                            if (plugin.getProxy().getPlayer(args[1]) != null) {
                                ProxiedPlayer player = plugin.getProxy().getPlayer(args[1]);

                                if (StorageTool.unShadowBanPlayer(player)) {
                                    sendLine(sender);
                                    sender.sendMessage(new ComponentBuilder("PistonQueue").color(ChatColor.GOLD).create());
                                    sender.sendMessage(new ComponentBuilder("Successfully unshadowbanned " + player.getName() + "!").color(ChatColor.GREEN).create());
                                    sendLine(sender);
                                } else {
                                    sendLine(sender);
                                    sender.sendMessage(new ComponentBuilder("PistonQueue").color(ChatColor.GOLD).create());
                                    sender.sendMessage(new ComponentBuilder(player.getName() + " is already shadowbanned!").color(ChatColor.RED).create());
                                    sendLine(sender);
                                }
                            } else {
                                sendLine(sender);
                                sender.sendMessage(new ComponentBuilder("PistonQueue").color(ChatColor.GOLD).create());
                                sender.sendMessage(new ComponentBuilder("The player " + args[1] + " was not found!").color(ChatColor.GOLD).create());
                                sendLine(sender);
                            }
                        } else {
                            sendUnBanHelp(sender);
                        }
                    } else {
                        noPermission(sender);
                    }
                    break;
                default:
                    help(sender);
                    break;
            }
        }
    }

    private void noPermission(CommandSender sender) {
        sendLine(sender);
        sender.sendMessage(new ComponentBuilder("PistonQueue").color(ChatColor.GOLD).create());
        sender.sendMessage(new ComponentBuilder("You do not").color(ChatColor.RED).create());
        sender.sendMessage(new ComponentBuilder("have permission").color(ChatColor.RED).create());
        sendLine(sender);
    }

    private void help(CommandSender sender) {
        sendLine(sender);
        sender.sendMessage(new ComponentBuilder("PistonQueue").color(ChatColor.GOLD).create());
        sender.sendMessage(new ComponentBuilder("/pq help").color(ChatColor.GOLD).create());
        sender.sendMessage(new ComponentBuilder("/pq version").color(ChatColor.GOLD).create());
        sender.sendMessage(new ComponentBuilder("/pq stats").color(ChatColor.GOLD).create());

        if (sender.hasPermission(Config.ADMINPERMISSION)) {
            sender.sendMessage(new ComponentBuilder("/pq reload").color(ChatColor.GOLD).create());
            sender.sendMessage(new ComponentBuilder("/pq shadowban").color(ChatColor.GOLD).create());
            sender.sendMessage(new ComponentBuilder("/pq unshadowban").color(ChatColor.GOLD).create());
        }

        sendLine(sender);
    }

    private void sendBanHelp(CommandSender sender) {
        sendLine(sender);
        sender.sendMessage(new ComponentBuilder("PistonQueue").color(ChatColor.GOLD).create());
        sender.sendMessage(new ComponentBuilder("/pq shadowban player <d|h|m|s>").color(ChatColor.GOLD).create());
        sender.sendMessage(new ComponentBuilder("Example:").color(ChatColor.GOLD).create());
        sender.sendMessage(new ComponentBuilder("/pq shadowban Pistonmaster 2d").color(ChatColor.GOLD).create());
        sendLine(sender);
    }

    private void sendUnBanHelp(CommandSender sender) {
        sendLine(sender);
        sender.sendMessage(new ComponentBuilder("PistonQueue").color(ChatColor.GOLD).create());
        sender.sendMessage(new ComponentBuilder("/pq unshadowban player").color(ChatColor.GOLD).create());
        sender.sendMessage(new ComponentBuilder("Example:").color(ChatColor.GOLD).create());
        sender.sendMessage(new ComponentBuilder("/pq unshadowban Pistonmaster").color(ChatColor.GOLD).create());
        sendLine(sender);
    }

    private void sendLine(CommandSender sender) {
        sender.sendMessage(new ComponentBuilder("----------------").color(ChatColor.DARK_BLUE).create());
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (Config.REGISTERTAB) {
            final List<String> completions = new ArrayList<>();

            if (args.length == 1) {
                for (String string : commands) {
                    if (string.toLowerCase().startsWith(args[0].toLowerCase()))
                        completions.add(string);
                }

                if (sender.hasPermission(Config.ADMINPERMISSION)) {
                    for (String string : adminCommands) {
                        if (string.toLowerCase().startsWith(args[0].toLowerCase()))
                            completions.add(string);
                    }
                }
            } else if (sender.hasPermission(Config.ADMINPERMISSION)
                    && args.length == 2
                    && (args[0].equalsIgnoreCase("shadowban") || args[0].equalsIgnoreCase("unshadowban"))) {
                addPlayers(completions, args);
            }

            Collections.sort(completions);

            return completions;
        } else {
            return null;
        }
    }

    private void addPlayers(List<String> completions, String[] args) {
        for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
            if (player.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                completions.add(player.getName());
        }
    }
}
