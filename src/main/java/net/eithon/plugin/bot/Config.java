package net.eithon.plugin.bot;

import java.util.List;

import net.eithon.library.extensions.EithonPlugin;
import net.eithon.library.plugin.ConfigurableCommand;
import net.eithon.library.plugin.ConfigurableMessage;
import net.eithon.library.plugin.Configuration;

public class Config {
	public static void load(EithonPlugin plugin)
	{
		Configuration config = plugin.getConfiguration();
		V.load(config);
		C.load(config);
		M.load(config);

	}
	public static class V {
		public static long coolDownTimeInSeconds;
		public static  List<String> applicableWorlds;
		public static float flySpeed;

		static void load(Configuration config) {
			coolDownTimeInSeconds = config.getSeconds("CoolDownTime", "30m");
			applicableWorlds = config.getStringList("FreebuildWorldNames");
			flySpeed = (float) config.getDouble("FlySpeed", 0.1);
		}

	}
	public static class C {
		public static ConfigurableCommand setSpeed;
		public static ConfigurableCommand stopFly;

		static void load(Configuration config) {
			setSpeed = config.getConfigurableCommand("commands.SetSpeed", 2,
					"speed fly %.2f %s");
			stopFly = config.getConfigurableCommand("commands.StopFly", 0,
					"fly");
		}

	}
	public static class M {
		public static ConfigurableMessage waitForCoolDown;
		public static ConfigurableMessage mustBeInFreebuildWord;
		public static ConfigurableMessage alreadyOn;
		public static ConfigurableMessage alreadyOff;
		public static ConfigurableMessage activated;
		public static ConfigurableMessage deactivated;
		public static ConfigurableMessage notInCoolDown;
		public static ConfigurableMessage releasedFromCoolDown;

		static void load(Configuration config) {
			waitForCoolDown = config.getConfigurableMessage(
					"messages.WaitForCoolDown", 2, 
					"The remaining cool down period for switching Freebuild mode is %d minutes and %d seconds.");
			mustBeInFreebuildWord = config.getConfigurableMessage(
					"messages.MustBeInFreebuildWord", 0, 
					"You can only switch between survival and bot in the SurvivalFreebuild world.");
			alreadyOn = config.getConfigurableMessage(
					"messages.AlreadyOn_0", 0, 
					"Freebuild mode is already active.");
			alreadyOff = config.getConfigurableMessage(
					"messages.AlreadyOff_0", 0, 
					"Survival mode is already active (bot is OFF).");
			activated = config.getConfigurableMessage(
					"messages.Activated_0", 0, 
					"Freebuild mode is now active.");
			deactivated = config.getConfigurableMessage(
					"messages.Deactivated_0", 0, 
					"Survival mode is now active (bot is OFF).");
			notInCoolDown = config.getConfigurableMessage(
					"messages.NotInCoolDown_1", 1, 
					"The player %s was not in cool down.");
			releasedFromCoolDown = config.getConfigurableMessage(
					"messages.ReleasedFromCoolDown_1", 1, 
					"The player %s was released from cool down.");
		}		
	}

}
