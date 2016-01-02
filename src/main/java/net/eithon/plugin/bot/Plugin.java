package net.eithon.plugin.bot;

import java.util.UUID;

import net.eithon.library.extensions.EithonPlugin;
import net.eithon.plugin.bot.logic.Controller;
import net.minecraft.server.v1_8_R2.EntityPlayer;
import net.minecraft.server.v1_8_R2.PlayerInteractManager;
import net.minecraft.server.v1_8_R2.PlayerList;
import net.minecraft.server.v1_8_R2.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.craftbukkit.v1_8_R2.CraftServer;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;

public final class Plugin extends EithonPlugin {
	private Controller _controller;

	@Override
	public void onEnable() {
		super.onEnable();
		Config.load(this);
		this._controller = new Controller(this);
		CommandHandler commandHandler = new CommandHandler(this, this._controller);
		Listener eventListener = new EventListener(this, this._controller);
		super.activate(commandHandler, eventListener);
	}

	@Override
	public void onDisable() {
		super.onDisable();
		this._controller = null;
	}
}
