package dev.alcor.rocketstrong.kitpvp.Khandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class KCommands implements CommandExecutor, TabCompleter {
    private ArrayList<KCommand> commands = new ArrayList<>();

    public KCommands() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
            return this.ExecuteCommand(args[0], sender, command, label, newArgs);
        } else {
            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Command Not Found");
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> CommandWords = new ArrayList<>();
        List<String> Output = new ArrayList<>();

        for (KCommand kCommand: commands) {
            CommandWords.add(kCommand.GetCommandWord());
        }

        if (args.length == 1) {
            Output =  CommandWords;
        } else if (args.length == 2) {
            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
            Output = this.ComleteCommand(args[0], sender, command, alias, newArgs);
        }
        return Output;
    }

    public void RegisterCommand(KCommand kcommand) {
        commands.add(kcommand);
    }

    
    public boolean ExecuteCommand(String com_Word, CommandSender sender, Command command, String label, String[] args) {
        boolean Executeresult = false;
        for (KCommand kCommand: commands) {
            if (kCommand.GetCommandWord().equals(com_Word)) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if(!p.hasPermission(kCommand.GetCommandPerm())) {
                        sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "I’m sorry, Dave. I’m afraid I can’t do that.");
                        sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You Need: " + kCommand.GetCommandPerm());
                        return true;
                    }
                }
                Executeresult = kCommand.CommandExecuter(sender, command, label, args);
            }
        }
        return Executeresult;
    }

    public List<String> ComleteCommand(String com_Word, CommandSender sender, Command command, String label, String[] args) {
        List<String> empty = new ArrayList<>();
        for (KCommand kCommand: commands) {
            if (kCommand.GetCommandWord().equals(com_Word)) {
                return (List<String>) kCommand.CommandCompleter(sender, command, label, args);
            }
        }
        return empty;
    }
}
