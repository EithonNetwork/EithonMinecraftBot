package net.eithon.plugin.bot;

import net.eithon.library.extensions.EithonPlugin;
import net.eithon.plugin.bot.logic.Controller;

import org.bukkit.event.Listener;

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
		this._controller.createBot(Config.V.botLocation);
	}

	@Override
	public void onDisable() {
		super.onDisable();
		this._controller = null;
	}
}
