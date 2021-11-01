package dev.alcor.rocketstrong.kitpvp.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.alcor.rocketstrong.kitpvp.App;
import dev.alcor.rocketstrong.kitpvp.Khandler.KCommand;
import dev.alcor.rocketstrong.kitpvp.PlayerTools.PlayerManipulator;

public class ChangeSkin implements KCommand{

    private final App plugin;

    public ChangeSkin(App plugin) {
        this.plugin = plugin;
    }


    @Override
    public String GetCommandWord() {
        return "skin";
    }

    @Override
    public String GetCommandPerm() {
        return "kitpvp.skin";
    }

    @Override
    public boolean CommandExecuter(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            switch (args[0]) {
                case "russian":
                    String texture = "ewogICJ0aW1lc3RhbXAiIDogMTYxODQ5NDczNzcyMSwKICAicHJvZmlsZUlkIiA6ICJmYzUwMjkzYTVkMGI0NzViYWYwNDJhNzIwMWJhMzBkMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDVUNGTDE3IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzI5NjIzZWUyYmExY2Q0YmNmZTBkOWJhYTM4ZDEyMzljMjRmYWY3MzFlMzE0N2Q1OGQxNTFiYTUyOGE2M2FlMjQiCiAgICB9CiAgfQp9";
                    String signature = "yYlo/K67dFNfyMJcpHWwKefw3AXEo4oNB4x7Dt/IaacAFfHp8XMXYbnwPM07EvFMe8TLvihs2AA7M9bu/qEQ4Rr07EEvVmwPHPBqZJBzzozJ9ztXm3Yn7Q7olnZ70XteOxt/lOCtzDjm7ohmq/+3K+bcQ00qLKP93F1CcA4C7Bw2t1HBhGkvs51WnHy5LKxgw6y1FHyB/2LxFD+nk4DJ5vYvs1V02ZKp74jXqhCKtHrXsMLEZMtN3kc61Fmbw6n7KoxaeJmCApRqdSLvUOFTw50BkYqW0pWfgUQrKUUys4XevgQSt0F5CaIn5g8HvNZ4+Qh3ni2z8DsyvH+3lE5fTCgh8KtxI7uuduyQYbaIs6vh8Hq5/yjOiK9jwV8R9JWWacXSPg687LeNKWLbJd8tIVTp8bX9buBPw2eOj8ioI5C+Y9+TdRuXVU/EKPZqjng3HSGnSaY2qidMBbxpjO6ebfQ6zHJG9V4+TBzs/wqCHrOA1+Yp3SDWNa4PuqmaJuQePgGOFraBr7tqxKM/V6jeEJqp0zmKXqsSrbpOcPtW3L+o8R/DMfhd1CW4W1fUD7Odn+ShasNl39qXVDJSJi1UHOh1hM32wcjYXOhFRSR4yzaY+yyDxzGNMWv4jPhRRJkUwQj7k0+t/yegyhwr55AYo/4V+LNxLDBbFMFs9yRnN3Q=";
                    PlayerManipulator.ChangeSkin((Player)sender, texture, signature, plugin);
                    sender.sendMessage("Skin Changed to Russian");
                    break;
                default:
                sender.sendMessage("What Skin?");
            }
        }
        return true;
    }

    @Override
    public List<String> CommandCompleter(CommandSender sender, Command command, String alias, String[] args) {
        List<String> Options = new ArrayList<>();
        if (args.length == 1) {
            Options.add("russian");
        }
        return Options;
    }
    
}
