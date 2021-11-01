package dev.alcor.rocketstrong.kitpvp.commands;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import dev.alcor.rocketstrong.kitpvp.App;
import dev.alcor.rocketstrong.kitpvp.Khandler.KCommand;

public class Reload implements KCommand {
    // Executed when someone does /firework
    private final App plugin;

    public Reload(App plugin) {
        this.plugin = plugin;
    }

    @Override
    public String GetCommandWord() {
        return "reload";
    }

    @Override
    public String GetCommandPerm() {
        return "kitpvp.reload";
    }

    @Override
    public boolean CommandExecuter(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("Reloading Config For KitPVP");
        plugin.reloadConfig();
        sender.sendMessage("Reloaded!");
        return true;
    }

    @Override
    public List<String> CommandCompleter(CommandSender sender, Command command, String alias, String[] args) {
        List<String> empty = new ArrayList<>();
        return empty;
    }

}