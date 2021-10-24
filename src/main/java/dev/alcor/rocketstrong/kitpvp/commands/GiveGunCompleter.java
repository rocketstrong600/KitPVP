package dev.alcor.rocketstrong.kitpvp.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

public class GiveGunCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> Options = new ArrayList<>();
        List<String> Output = new ArrayList<>();
        if (args.length == 1) {
            Options.add("rifle");
            Options.add("rocketlancher");
            StringUtil.copyPartialMatches(args[0], Options, Output);
        }

        return Output;
    }
    
}