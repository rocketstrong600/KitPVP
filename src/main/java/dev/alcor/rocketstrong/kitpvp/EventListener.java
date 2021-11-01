package dev.alcor.rocketstrong.kitpvp;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.ticxo.modelengine.api.util.math.Function.Sin;
import com.comphenix.protocol.ProtocolManager;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import de.tr7zw.nbtapi.NBTItem;
import dev.alcor.rocketstrong.kitpvp.PlayerTools.PlayerManipulator;
import it.unimi.dsi.fastutil.ints.IntArrayList;

public class EventListener implements Listener{
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    
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
                    s.setSilent(true);
                    s.setPierceLevel(1);

                    //Player getworld play sound at player location
                    p.getWorld().playSound(p.getLocation(), Sound.ITEM_SHIELD_BREAK, 0.65F, 0.52F);

                    //Player particle effects
                    Location barrel = p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(1.5)).add(0, -0.2, 0);
                    //If count 0 direction used else offset
                    //Particle, Location, count, Direction/offest x, y, z

                    //Vector Direction = p.getEyeLocation().getDirection().normalize();
                    //p.getWorld().spawnParticle(Particle.FLAME, barrel, 0, 0, 0, 0);
                    //p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, barrel, 4, 0, 0, 0, 0.050);
                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(100, 100, 100), 1);
                    p.getWorld().spawnParticle(Particle.REDSTONE, barrel, 1, 0, 0, 0, 0.5, dust, true);

                    s.getPersistentDataContainer().set(NamespacedKey.fromString("bullet"), PersistentDataType.STRING, "RifleBullet");
                    //s.setGravity(false);
                    s.setKnockbackStrength(0);
                    s.setDamage(1);

                    Entity Arrow = (Entity) s;
                    //Destroy Arrow Entity Renderer on Client shooters side
                    final PacketContainer destroyEntity = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
                    destroyEntity.getModifier().write(0, new IntArrayList(new int[]{Arrow.getEntityId()}));

                    try {
                        protocolManager.broadcastServerPacket(destroyEntity, s, false);
                    } catch (FieldAccessException e) {
                        throw new RuntimeException(
                            "Cannot send packet " + destroyEntity, e);
                    }
                    Vector ArrowVelocity = s.getVelocity();
                    ArrowVelocity.multiply(2.5);
                    s.setVelocity(ArrowVelocity);
                    event.setCancelled(true);
                    break;
                case "RocketLauncher":
                    nbti.setBoolean("Charged", true);
                    p.getInventory().setItemInMainHand(nbti.getItem());

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
        Block block = event.getHitBlock();
        BlockFace blockface = event.getHitBlockFace();
        Entity entity = event.getHitEntity();
        NamespacedKey Bullet = NamespacedKey.fromString("bullet");
        PersistentDataContainer ProjectileData = Projectile.getPersistentDataContainer();

        if (ProjectileData.has(Bullet, PersistentDataType.STRING)) {
            if(block != null) {
                Projectile.getWorld().spawnParticle(Particle.BLOCK_DUST, block.getLocation().add(0.5, 0.5, 0.5).add(blockface.getDirection()), 5, 0.15, 0.15, 0.15, 0.5, block.getType().createBlockData(), true);
                Projectile.getWorld().playSound(Projectile.getLocation(), Sound.BLOCK_COPPER_HIT, 1.5F, 0.52F);
            } else if (entity instanceof Player) {
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1);
                Projectile.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation().add(0, 1, 0), 8, 0.20, 0.20, 0.20, 0.01, dust, true);
            }
            Projectile.remove();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        //String texture = "ewogICJ0aW1lc3RhbXAiIDogMTYxODQ5NDczNzcyMSwKICAicHJvZmlsZUlkIiA6ICJmYzUwMjkzYTVkMGI0NzViYWYwNDJhNzIwMWJhMzBkMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDVUNGTDE3IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzI5NjIzZWUyYmExY2Q0YmNmZTBkOWJhYTM4ZDEyMzljMjRmYWY3MzFlMzE0N2Q1OGQxNTFiYTUyOGE2M2FlMjQiCiAgICB9CiAgfQp9";
        //String signature = "yYlo/K67dFNfyMJcpHWwKefw3AXEo4oNB4x7Dt/IaacAFfHp8XMXYbnwPM07EvFMe8TLvihs2AA7M9bu/qEQ4Rr07EEvVmwPHPBqZJBzzozJ9ztXm3Yn7Q7olnZ70XteOxt/lOCtzDjm7ohmq/+3K+bcQ00qLKP93F1CcA4C7Bw2t1HBhGkvs51WnHy5LKxgw6y1FHyB/2LxFD+nk4DJ5vYvs1V02ZKp74jXqhCKtHrXsMLEZMtN3kc61Fmbw6n7KoxaeJmCApRqdSLvUOFTw50BkYqW0pWfgUQrKUUys4XevgQSt0F5CaIn5g8HvNZ4+Qh3ni2z8DsyvH+3lE5fTCgh8KtxI7uuduyQYbaIs6vh8Hq5/yjOiK9jwV8R9JWWacXSPg687LeNKWLbJd8tIVTp8bX9buBPw2eOj8ioI5C+Y9+TdRuXVU/EKPZqjng3HSGnSaY2qidMBbxpjO6ebfQ6zHJG9V4+TBzs/wqCHrOA1+Yp3SDWNa4PuqmaJuQePgGOFraBr7tqxKM/V6jeEJqp0zmKXqsSrbpOcPtW3L+o8R/DMfhd1CW4W1fUD7Odn+ShasNl39qXVDJSJi1UHOh1hM32wcjYXOhFRSR4yzaY+yyDxzGNMWv4jPhRRJkUwQj7k0+t/yegyhwr55AYo/4V+LNxLDBbFMFs9yRnN3Q=";
        //PlayerManipulator.ChangeSkin(event.getPlayer(), texture, signature);
    }

}
