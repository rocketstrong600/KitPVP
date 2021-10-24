package dev.alcor.rocketstrong.fireworks.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

public class FireworksCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> Options = new ArrayList<>();
        List<String> Output = new ArrayList<>();
        if (args.length == 1) {
            Options.add("red");
            Options.add("green");
            Options.add("blue");
            Options.add("random");
            Options.add("custom");
            StringUtil.copyPartialMatches(args[0], Options, Output);
        }
        if (args[0].equalsIgnoreCase("custom") && args.length <= 4) {

            Output.add("0");
        }

        return Output;
    }
    
}
