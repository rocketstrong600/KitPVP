package dev.alcor.rocketstrong.kitpvp;

import org.bukkit.plugin.java.JavaPlugin;

import dev.alcor.rocketstrong.kitpvp.commands.Fireworks;
import dev.alcor.rocketstrong.kitpvp.commands.FireworksCompleter;
import dev.alcor.rocketstrong.kitpvp.commands.GiveGun;
import dev.alcor.rocketstrong.kitpvp.commands.GiveGunCompleter;

public class App extends JavaPlugin {

    @Override
    public void onEnable() {
        //Fireworks
        this.getCommand("fshow").setExecutor(new Fireworks(this));
        this.getCommand("fshow").setTabCompleter(new FireworksCompleter());
        //Give Gun
        this.getCommand("givegun").setExecutor(new GiveGun());
        this.getCommand("givegun").setTabCompleter(new GiveGunCompleter());

        getServer().getPluginManager().registerEvents(new EventListener(), this);

        getLogger().info("Fireworks has loaded");
    }

    @Override
    public void onDisable() {
        getLogger().info("Fireworks has unloaded");
    }
}