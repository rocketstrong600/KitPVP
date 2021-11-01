package dev.alcor.rocketstrong.kitpvp.PlayerTools;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.network.PlayerConnection;

public class PlayerManipulator {

    public static void ChangeSkin(Player player, String TextureData , String Signature, Plugin plugin) {
        Entity vehicle = player.getVehicle();
        if (vehicle != null) {
            vehicle.removePassenger(player);
        }
        if (!player.isEmpty()) {
            for (Entity passenger : player.getPassengers()) {
                player.removePassenger(passenger);
            }
        }

        //get GameProfile
        GameProfile profile = ((CraftPlayer)player).getHandle().getProfile();

        //((CraftPlayer)player).getHandle().b is ((CraftPlayer)player).getHandle().playerConnection
        //Get Player Connection
        PlayerConnection connection = ((CraftPlayer)player).getHandle().b;
        //PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a is PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER
        //send packet to remove player
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, ((CraftPlayer)player).getHandle()));

        //change stored player texture
        Property skin = new Property("textures", TextureData, Signature);
        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", skin);

        //add back player
        //PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a is PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, ((CraftPlayer)player).getHandle()));
        
        //reload player for everyone
        for (Player ps : Bukkit.getOnlinePlayers()) {
            ps.hidePlayer(plugin, player);
            ps.showPlayer(plugin, player);
        }

        //attempt to invoke unaccesable method refresh player on player object
        try {
            Method m;
            Class<?> CraftPlayerClass = Class.forName("org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer");
            m = CraftPlayerClass.getDeclaredMethod("refreshPlayer");
            m.setAccessible(true);
            m.invoke(player);
        } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e){

        }

    }
}
