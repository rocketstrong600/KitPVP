package dev.alcor.rocketstrong.kitpvp;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import de.tr7zw.nbtapi.NBTItem;
import it.unimi.dsi.fastutil.ints.IntArrayList;

public class EventListener implements Listener{
    
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event){
        Player p = event.getPlayer();
        if (p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType() == Material.AIR) return;
        ItemStack PlayerItem = p.getInventory().getItemInMainHand();
        NBTItem nbti = new NBTItem(PlayerItem);

        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && nbti.hasKey("Gun")) {
            switch (nbti.getString("Gun")) {
                case "Rifle":
                    nbti.setBoolean("Charged", true);
                    p.getInventory().setItemInMainHand(nbti.getItem());

                    Arrow s = p.launchProjectile(Arrow.class);

                    s.getPersistentDataContainer().set(NamespacedKey.fromString("bullet"), PersistentDataType.STRING, "RifleBullet");
                    //s.setGravity(false);
                    s.setKnockbackStrength(0);
                    s.setDamage(1);

                    //Destroy Arrow Entity Renderer on Client shooters side
                    final PacketContainer destroyEntity = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
                    destroyEntity.getModifier().write(0, new IntArrayList(new int[]{s.getEntityId()}));

                    List<Player> Observers = ProtocolLibrary.getProtocolManager().getEntityTrackers(p);

                    for (Player observer : Observers) {
                        try {
                            ProtocolLibrary.getProtocolManager().sendServerPacket(observer, destroyEntity);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException("Cannot send packet.", e);
                        }
                    }

                    Vector ArrowVelocity = s.getVelocity();
                    ArrowVelocity.multiply(2.5);
                    s.setVelocity(ArrowVelocity);

                    event.setCancelled(true);
                    break;
                case "RocketLauncher":
                    Fireball Rocket = p.launchProjectile(Fireball.class);
                    Vector RocketVelocity = Rocket.getVelocity();
                    RocketVelocity.multiply(50);
                    Rocket.setVelocity(RocketVelocity);
                    Rocket.setYield(3.0f);
                    event.setCancelled(true);
                    break;
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR) return;
        ItemStack PlayerItem = player.getInventory().getItemInMainHand();
        NBTItem nbti = new NBTItem(PlayerItem);
        if(!nbti.hasKey("Gun")) return; 
        switch (nbti.getString("Gun")) {
            case "Rifle":
                if(player.isSneaking()) {
                    nbti.setInteger("CustomModelData", 1);
                    player.getInventory().setItemInMainHand(nbti.getItem());
                    player.removePotionEffect(PotionEffectType.SLOW);
                } else {
                    nbti.setInteger("CustomModelData", 2);
                    player.getInventory().setItemInMainHand(nbti.getItem());
                    PotionEffect Slowness = new PotionEffect(PotionEffectType.SLOW, 9999, 20);
                    player.addPotionEffect(Slowness);
                }
                break;
            case "RocketLauncher":
                break;
        }
    }


    @EventHandler(priority=EventPriority.HIGH)
    public void onProjectileHit (ProjectileHitEvent event){
        Entity Projectile = event.getEntity();
        NamespacedKey Bullet = NamespacedKey.fromString("bullet");
        PersistentDataContainer ProjectileData = Projectile.getPersistentDataContainer();

        if (ProjectileData.has(Bullet, PersistentDataType.STRING)) {
            Projectile.remove();
        }
    }
}
