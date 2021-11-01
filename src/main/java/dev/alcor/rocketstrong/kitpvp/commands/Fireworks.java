package dev.alcor.rocketstrong.kitpvp.commands;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.command.CommandSender;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;

import dev.alcor.rocketstrong.kitpvp.App;
import dev.alcor.rocketstrong.kitpvp.Khandler.KCommand;

import org.bukkit.entity.EntityType;
import org.bukkit.World;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;


public class Fireworks implements KCommand {
    // Executed when someone does /firework
    private final App plugin;

    public Fireworks(App plugin) {
        this.plugin = plugin;
    }

    @Override
    public String GetCommandWord() {
        return "fireworkshow";
    }

    @Override
    public String GetCommandPerm() {
        return "kitpvp.fireworks";
    }
    @Override
    public boolean CommandExecuter(CommandSender sender, Command command, String label, String[] args) {
        //Check if Player
        if (sender instanceof Player || sender instanceof BlockCommandSender) {
            sender.sendMessage("Enjoy the show!");
            World world = Bukkit.getServer().getWorlds().get(0);
            Location PlayerLocation = new Location(world, 0, 0, 0);
            //Get Player and INFO

            //VARIABLES TO DETERMINE COLOUR
            boolean RandomColour = false;
            Color FwColour = Color.fromRGB(0,0,0);
            
            //Test For args 
            if (args.length == 0){
                RandomColour = true;
            } else {
                RandomColour = false;
                //Determine what colour the args are... RGB
                switch (args[0].toLowerCase()){ 
                    case "blue":
                        FwColour = Color.fromRGB(0,0,255);
                        break;
                    case "red":
                        FwColour = Color.fromRGB(255,0,0);
                        break;
                    case "green":
                        FwColour = Color.fromRGB(0,255,0);
                        break;
                    case "custom":
                    //Player can set theyre own RGB
                        if(args.length == 4){
                        FwColour = Color.fromRGB(Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]));
                        }else {
                        //If incorect amount of args it will defualt to random
                        sender.sendMessage("Error: Not Correct usage of this command");
                        RandomColour = true;
                        }
                        break;
                    case "random":
                        RandomColour = true;
                        break;
                        default:
                    //if unrecognised arg, it will default to random
                        sender.sendMessage("Error: Unknown Colour.");
                        RandomColour = true;
                        break;    
                }
                

            }

            if (sender instanceof BlockCommandSender) {
                BlockCommandSender commandBlock = (BlockCommandSender) sender;
                PlayerLocation = commandBlock.getBlock().getLocation();
            } else {
                Player commandPlayer = (Player) sender;
                PlayerLocation = commandPlayer.getLocation();
                float distance = 50;
                PlayerLocation.setPitch(0);
                PlayerLocation.add(PlayerLocation.getDirection().multiply(distance));

            }
            final Location PlayerLocationFinal = PlayerLocation;
            final boolean RandomColourFinal = RandomColour; 
            final Color FwColourFinal = FwColour;
            //Firework
            Random ran = new Random();
            for (int f = 0; f < 100; f++) {
                int time = ran.nextInt(25);
                //time += 2*f;
                
                new BukkitRunnable() {
            
                    @Override
                    public void run() {
                        Random ran = new Random();
                        Color FwColourFinalNew = FwColourFinal;

                        //Will only select random colours if Random was selected
                        // Or No colour was selected
                        if (RandomColourFinal){
                            int red = ran.nextInt(255);
                            int green = ran.nextInt(255);
                            int blue = ran.nextInt(255);
                            FwColourFinalNew = Color.fromRGB(red, green, blue);
                        }
                        int type = ran.nextInt(4);
                        Location FireworkSpawn = new Location(PlayerLocationFinal.getWorld(), PlayerLocationFinal.getX(), PlayerLocationFinal.getY(), PlayerLocationFinal.getZ());
                        int sizeX = 50;
                        int sizeZ = 50;
                        double x = ((double)ran.nextInt(sizeX))-(sizeX/2);
                        double z = ((double)ran.nextInt(sizeZ))-(sizeZ/2);

                        double y = 0;
                        FireworkSpawn.add(FireworkSpawn, x, y, z);
                        for (int i = (int)FireworkSpawn.getY(); i < 256; i++) {
                            FireworkSpawn.setY(i+1);
                            if(FireworkSpawn.getBlock().getType() == Material.AIR) {
                                break;
                            }
                        }
    
                        Firework fw = (Firework) FireworkSpawn.getWorld().spawnEntity(FireworkSpawn, EntityType.FIREWORK);
                        FireworkMeta fwm = fw.getFireworkMeta();

                        FireworkEffect.Type[] FeffectsType = {
                            FireworkEffect.Type.BALL,
                            FireworkEffect.Type.BALL_LARGE,
                            FireworkEffect.Type.CREEPER,
                            FireworkEffect.Type.STAR,
                            FireworkEffect.Type.BURST};
                            
                        fwm.setPower(2);
                        fwm.addEffect(FireworkEffect.builder().with(FeffectsType[type]).withColor(FwColourFinalNew).flicker(true).build());
                        fw.setFireworkMeta(fwm);
                        // What you want to schedule goes here1
                    }
                    
                }.runTaskLater(plugin , time*20);
            }

        } else {
            sender.sendMessage("You Are Not A Player");
        }
        return true;
    }

    @Override
    public List<String> CommandCompleter(CommandSender sender, Command command, String alias, String[] args) {
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
