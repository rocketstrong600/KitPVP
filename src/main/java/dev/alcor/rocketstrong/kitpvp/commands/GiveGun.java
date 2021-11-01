package dev.alcor.rocketstrong.kitpvp.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class GiveGun implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            sender.sendMessage("Too Few Arguments");
            return false;
        }

        if (sender instanceof Player) {
            Player commandPlayer = (Player) sender;
            PlayerInventory commanderInventory = commandPlayer.getInventory();
            ItemStack Gun = new ItemStack(Material.CROSSBOW,1);

            ItemMeta GunMeta = Gun.getItemMeta();

            NBTItem nbti;

            final TextComponent GunName;
            final TextComponent GunLorePrimary;
            final TextComponent GunLoreSecondary;
            List<Component> GunLore = new ArrayList<>();

            switch(args[0].toLowerCase()) {
                case "rifle":

                    GunName = Component.text("Rifle!")
                    .color(TextColor.color(0x443344));

                    GunLorePrimary = Component.text("Best 'Back to school' supply!!!")
                    .color(TextColor.color(NamedTextColor.DARK_PURPLE));

                    GunLoreSecondary = Component.text("To fire, press: ")
                    .color(TextColor.color(NamedTextColor.GRAY))
                    .append(Component.keybind("key.use").color(NamedTextColor.GRAY).decoration(TextDecoration.BOLD, true));

                    GunLore.add(GunLorePrimary);
                    GunLore.add(GunLoreSecondary);
                    
                    GunMeta.displayName(GunName);
                    GunMeta.lore(GunLore);
                    
                    Gun.setItemMeta(GunMeta);
                    
                    nbti = new NBTItem(Gun);
                    nbti.setString("Gun", "Rifle");
                    nbti.setInteger("CustomModelData", 1);
                    nbti.setBoolean("Charged", true);

                    break;

                case "rocketlancher":

                    GunName = Component.text("RocketLauncher!")
                    .color(TextColor.color(0x443344));

                    GunLorePrimary = Component.text("Fire unstopable rockets at your enemys!!")
                    .color(TextColor.color(NamedTextColor.DARK_PURPLE));
                    
                    GunLoreSecondary = Component.text("To fire, press: ")
                    .color(TextColor.color(NamedTextColor.GRAY))
                    .append(Component.keybind("key.use").color(NamedTextColor.GRAY).decoration(TextDecoration.BOLD, true));

                    GunLore.add(GunLorePrimary);
                    GunLore.add(GunLoreSecondary);
                    
                    GunMeta.displayName(GunName);
                    GunMeta.lore(GunLore);
                    
                    Gun.setItemMeta(GunMeta);

                    nbti = new NBTItem(Gun);
                    nbti.setString("Gun", "RocketLauncher");
                    nbti.setInteger("CustomModelData", 4);
                    nbti.setBoolean("Charged", true);
                    break;
                default:
                    sender.sendMessage("That's Not A Gun");
                    return false;
            }

            commanderInventory.addItem(nbti.getItem());
        } else {
            sender.sendMessage("Must Be executed by a player");
        }
        return true;
    }
    
}
