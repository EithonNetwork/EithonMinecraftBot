package net.eithon.plugin.bot.logic;

import java.util.UUID;

import net.eithon.library.core.IUuidAndName;
import net.eithon.library.extensions.EithonPlayer;
import net.eithon.library.json.JsonObject;

import org.bukkit.entity.Player;

class BoterInfo extends JsonObject<BoterInfo> implements IUuidAndName {
	private EithonPlayer _player;
	
	BoterInfo(Player player)
	{
		this._player = new EithonPlayer(player);
	}
	
	BoterInfo() {
	}
	
	Player getPlayer()
	{
		return this._player.getPlayer();
	}
	
	public String getName()
	{
		return this._player.getName();
	}

	public String toString() {
		return String.format("%s", this.getName());
	}

	@Override
	public BoterInfo factory() {
		return new BoterInfo();
	}

	@Override
	public UUID getUniqueId() {
		return this._player.getUniqueId();
	}

	@Override
	public Object toJson() {
		return this._player.toJson();
	}

	@Override
	public BoterInfo fromJson(Object json) {
		this._player = EithonPlayer.getFromJson(json);
		return this;
	}
}
