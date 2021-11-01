package dev.alcor.rocketstrong.kitpvp.Khandler;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface KCommand {
    public String GetCommandWord();
    public String GetCommandPerm();
    public boolean CommandExecuter(CommandSender sender, Command command, String label, String[] args);
    public List<String> CommandCompleter(CommandSender sender, Command command, String alias, String[] args);
}
