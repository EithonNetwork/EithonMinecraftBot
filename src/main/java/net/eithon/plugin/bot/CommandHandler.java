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
	private static final String CREATE_COMMAND = "/bot create";
	private Controller _controller;

	public CommandHandler(EithonPlugin eithonPlugin, Controller controller) {
		this._controller = controller;
	}

	public boolean onCommand(CommandParser commandParser) {
		EithonPlayer eithonPlayer = commandParser.getEithonPlayerOrInformSender();
		if (eithonPlayer == null) return true;
		
		String command = commandParser.getArgumentCommand();
		if (command == null) return false;
		
		if (command.equals("create")) {
			createCommand(commandParser);
		} else {
			commandParser.showCommandSyntax();
		}
		return true;
	}

	void createCommand(CommandParser commandParser)
	{
		if (!commandParser.hasPermissionOrInformSender("bot.create")) return;
		if (!commandParser.hasCorrectNumberOfArgumentsOrShowSyntax(1, 1)) return;
		Player player = commandParser.getPlayer();
				
		this._controller.createBot(player.getLocation());
	}

	@Override
	public void showCommandSyntax(CommandSender sender, String command) {
		if (command.equals("create")) {
			sender.sendMessage(CREATE_COMMAND);
		} else {
			sender.sendMessage(String.format("Unknown command: %s.", command));
		}
	}
}
