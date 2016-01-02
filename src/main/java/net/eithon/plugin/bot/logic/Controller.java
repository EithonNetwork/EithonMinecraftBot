package net.eithon.plugin.bot.logic;

import net.eithon.library.extensions.EithonPlugin;
import net.eithon.library.json.PlayerCollection;
import net.eithon.library.plugin.Logger.DebugPrintLevel;
import net.eithon.plugin.bot.Config;

import org.bukkit.entity.Player;

public class Controller {
	private PlayerCollection<BoterInfo> _boters = new PlayerCollection<BoterInfo>(new BoterInfo());
	private EithonPlugin _eithonPlugin;

	public Controller(EithonPlugin eithonPlugin) {
		this._eithonPlugin = eithonPlugin;
		this._boters = new PlayerCollection<BoterInfo>(new BoterInfo());
		delayedLoad(eithonPlugin);
	}

	public void addToBoters(Player player) {
		this._boters.put(player, new BoterInfo(player));
		delayedSave();
	}

	public void removeFromBoters(Player player) {
		this._boters.remove(player);
		delayedSave();
	}

	public boolean isBoter(Player player)
	{
		return this._boters.hasInformation(player);
	}

	public boolean inFreebuildWorld(Player player, boolean mustBeInBotWord) {
		if (Config.V.applicableWorlds != null) {
			String currentWorldName = player.getWorld().getName();
			for (String worldName : Config.V.applicableWorlds) {
				if (currentWorldName.equalsIgnoreCase(worldName)) return true;
			}
		}
		if (mustBeInBotWord) Config.M.mustBeInFreebuildWord.sendMessage(player);
		return false;
	}

	public boolean canUseFreebuilderProtection(Player player)
	{
		return canUseFreebuilderProtection(player, false);
	}

	public boolean canUseFreebuilderProtection(Player player, boolean warnIfNotInFreebuildWorld)
	{
		this._eithonPlugin.getEithonLogger().debug(DebugPrintLevel.VERBOSE, 
				"canUseFreebuilderProtection: Return false if the current player is not a boter.");
		if (!isBoter(player)) return false;
		this._eithonPlugin.getEithonLogger().debug(DebugPrintLevel.VERBOSE,
				"canUseFreebuilderProtection: Return false if the player's current world is not a boter world.");
		if (!inFreebuildWorld(player, warnIfNotInFreebuildWorld)) return false;
		this._eithonPlugin.getEithonLogger().debug(DebugPrintLevel.VERBOSE,
				"canUseFreebuilderProtection: Returns true.");
		return true;
	}

	private void delayedSave() {
		this._boters.delayedSave(this._eithonPlugin, "boters.json", "Boters", 0);
	}

	private void delayedLoad(EithonPlugin eithonPlugin) {
		this._boters.delayedLoad(eithonPlugin, "boters.json", 0);
	}
}
