package dev.alcor.rocketstrong.kitpvp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import dev.alcor.rocketstrong.kitpvp.Khandler.KCommands;
import dev.alcor.rocketstrong.kitpvp.commands.ChangeSkin;
import dev.alcor.rocketstrong.kitpvp.commands.Fireworks;
import dev.alcor.rocketstrong.kitpvp.commands.GiveGun;
import dev.alcor.rocketstrong.kitpvp.commands.GiveGunCompleter;
import dev.alcor.rocketstrong.kitpvp.commands.Reload;

public class App extends JavaPlugin {
    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        //Config
        config.addDefault("youAreAwesome", true);
        config.options().copyDefaults(true);
        saveConfig();
        
        //SQLite


        //Base Command
        KCommands commandHandler = new KCommands();
        commandHandler.RegisterCommand(new Fireworks(this));
        commandHandler.RegisterCommand(new Reload(this));
        commandHandler.RegisterCommand(new ChangeSkin(this));

        this.getCommand("kitpvp").setExecutor(commandHandler);
        this.getCommand("kitpvp").setTabCompleter(commandHandler);

        //Give Gun
        this.getCommand("givegun").setExecutor(new GiveGun());
        this.getCommand("givegun").setTabCompleter(new GiveGunCompleter());

        getServer().getPluginManager().registerEvents(new EventListener(), this);

        getLogger().info("KitPVP has loaded");
    }

    @Override
    public void onDisable() {
        getLogger().info("KitPVP has unloaded");
    }
}