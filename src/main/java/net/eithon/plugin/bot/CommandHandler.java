package net.eithon.plugin.bot;

import net.eithon.library.extensions.EithonPlayer;
import net.eithon.library.extensions.EithonPlugin;
import net.eithon.library.plugin.CommandParser;
import net.eithon.library.plugin.ICommandHandler;
import net.eithon.library.time.CoolDown;
import net.eithon.plugin.bot.logic.Controller;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements ICommandHandler {
	private static final String ON_COMMAND = "/bot on";
	private static final String OFF_COMMAND = "/bot off";
	private static final String RELEASE_COMMAND = "/bot release [<player>]";
	private CoolDown _coolDown;
	private Controller _controller;

	public CommandHandler(EithonPlugin eithonPlugin, Controller controller) {
		this._controller = controller;
		this._coolDown = new CoolDown("bot", Config.V.coolDownTimeInSeconds);
	}

	public boolean onCommand(CommandParser commandParser) {
		EithonPlayer eithonPlayer = commandParser.getEithonPlayerOrInformSender();
		if (eithonPlayer == null) return true;
		
		String command = commandParser.getArgumentCommand();
		if (command == null) return false;
		
		if (command.equals("on")) {
			botOnCommand(commandParser);
		} else if (command.equals("off")) {
			botOffCommand(commandParser);
		} else if (command.equals("release")) {
			releaseCommand(commandParser);
		} else {
			commandParser.showCommandSyntax();
		}
		return true;
	}

	void botOnCommand(CommandParser commandParser)
	{
		if (!commandParser.hasPermissionOrInformSender("bot.on")) return;
		if (!commandParser.hasCorrectNumberOfArgumentsOrShowSyntax(1, 1)) return;
		Player player = commandParser.getPlayer();
		if (this._controller.isBoter(player))
		{
			Config.M.alreadyOn.sendMessage(player);
			return;
		}

		if (!this._controller.inFreebuildWorld(player, true)) {
			return;
		}

		if (!verifyCoolDown(player)) return;
		this._controller.addToBoters(player);
		
		Config.M.activated.sendMessage(player);
		this._coolDown.addIncident(player);
	}

	void botOffCommand(CommandParser commandParser)
	{
		if (!commandParser.hasPermissionOrInformSender("bot.off")) return;
		if (!commandParser.hasCorrectNumberOfArgumentsOrShowSyntax(1, 1)) return;
		
		Player player = commandParser.getPlayer();

		if (!this._controller.isBoter(player))
		{
			Config.M.alreadyOff.sendMessage(player);
			return;
		}

		if (!verifyCoolDown(player)) return;

		this._controller.removeFromBoters(player);
		Config.M.deactivated.sendMessage(player);
		this._coolDown.addIncident(player);	
	}

	void releaseCommand(CommandParser commandParser)
	{
		if (!commandParser.hasPermissionOrInformSender("bot.release")) return;
		if (!commandParser.hasCorrectNumberOfArgumentsOrShowSyntax(1, 2)) return;
		
		Player player = commandParser.getArgumentPlayer(commandParser.getPlayer());
		if (player == null) {
			commandParser.showCommandSyntax();
			return;
		}

		if (verifyCoolDown(player)) {
			Config.M.notInCoolDown.sendMessage(commandParser.getSender(), player.getName());
			return;
		}
		
		releaseFromCoolDown(player);
		Config.M.releasedFromCoolDown.sendMessage(commandParser.getSender(), player.getName());
	}

	private void releaseFromCoolDown(Player player) {
		this._coolDown.removePlayer(player);		
	}

	private boolean verifyCoolDown(Player player) {
		if (player.hasPermission("bot.nocooldown")) return true;

		long secondsLeft = this._coolDown.secondsLeft(player);
		if (secondsLeft == 0) return true;

		long minutes = secondsLeft/60;
		long seconds = secondsLeft - 60 * minutes;
		Config.M.waitForCoolDown.sendMessage(player, minutes, seconds);
		return false;
	}

	@Override
	public void showCommandSyntax(CommandSender sender, String command) {
		if (command.equals("on")) {
			sender.sendMessage(ON_COMMAND);
		} else if (command.equals("off")) {
			sender.sendMessage(OFF_COMMAND);
		} else if (command.equals("release")) {
			sender.sendMessage(RELEASE_COMMAND);
		} else {
			sender.sendMessage(String.format("Unknown command: %s.", command));
		}
	}
}
