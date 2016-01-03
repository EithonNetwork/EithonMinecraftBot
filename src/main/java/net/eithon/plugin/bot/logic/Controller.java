package net.eithon.plugin.bot.logic;

import java.util.UUID;

import net.eithon.library.extensions.EithonPlugin;
import net.eithon.library.json.PlayerCollection;
import net.eithon.library.plugin.Logger.DebugPrintLevel;
import net.eithon.plugin.bot.Config;
import net.minecraft.server.v1_8_R2.EntityPlayer;
import net.minecraft.server.v1_8_R2.PlayerInteractManager;
import net.minecraft.server.v1_8_R2.PlayerList;
import net.minecraft.server.v1_8_R2.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftServer;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.entity.Player;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;

public class Controller {
	private EithonPlugin _eithonPlugin;

	public Controller(EithonPlugin eithonPlugin) {
		this._eithonPlugin = eithonPlugin;
	}

	public void createBot(Location location) {
		WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
		PlayerList playerList = ((CraftServer) Bukkit.getServer()).getHandle();
		UUID uuid = UUID.fromString(Config.V.botUUID);
		GameProfile gameProfile = new GameProfile(uuid, Config.V.botName);

		EntityPlayer entityplayer = new EntityPlayer(playerList.getServer(), world, gameProfile, new PlayerInteractManager(world));
		new DummyPlayerConnection(playerList.getServer(), new DummyNetworkManager(), entityplayer);

		entityplayer.spawnIn(world);
		entityplayer.playerInteractManager.a((WorldServer) entityplayer.world);
		entityplayer.playerInteractManager.b(world.getWorldData().getGameType());

		entityplayer.setPosition(location.getX(), location.getY(), location.getZ());

		playerList.players.add(entityplayer);
		world.addEntity(entityplayer);
		playerList.a(entityplayer);
	}
}
