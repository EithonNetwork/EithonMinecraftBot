package net.eithon.plugin.bot;

import net.eithon.library.extensions.EithonPlugin;
import net.eithon.library.plugin.Logger.DebugPrintLevel;
import net.eithon.plugin.bot.logic.Controller;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EventListener implements Listener {
	private EithonPlugin _eithonPlugin = null;
	private Controller _controller;

	public EventListener(EithonPlugin eithonPlugin, Controller controller) {
		this._controller = controller;
		this._eithonPlugin = eithonPlugin;
	}

	// Avoid becoming a target
	@EventHandler
	public void onEntityTargetLivingEntityEvent(EntityTargetLivingEntityEvent event) {
		if (event.isCancelled()) return;

		Entity target = event.getTarget();
		if (!(target instanceof Player)) return;
		Player player = (Player) target;

		if (!(event.getEntity() instanceof Monster)) return;

		if (!this._controller.canUseFreebuilderProtection(player)) return;

		event.setCancelled(true);
	}

	// Don't attack monsters
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) return;

		Entity damager = event.getDamager();
		Player player = null;
		if (damager instanceof Player) player = (Player) damager;
		else if (damager instanceof Projectile) {
			Projectile projectile = (Projectile) damager;
			ProjectileSource source = projectile.getShooter();
			if (source instanceof Player) player = (Player) source; 
		}

		if (player == null) return;
		
		if (!(event.getEntity() instanceof Monster)) return;
		Monster monster = (Monster) event.getEntity();

		if (!this._controller.canUseFreebuilderProtection(player)) return;

		// You can attack monsters that targets you
		if (monster.getTarget() == player) return;

		event.setCancelled(true);
	}

	// No damage on self
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (event.isCancelled()) return;

		Entity entity = event.getEntity();
		if (!(entity instanceof Player)) return;
		Player player = (Player) entity;

		if (!this._controller.canUseFreebuilderProtection(player)) return;

		event.setCancelled(true);
	}

	// No potion effects on free build players
	@EventHandler
	public void onPotionSplashEvent(PotionSplashEvent event) {
		if (event.isCancelled()) return;
		Player shooter = null;
		boolean shooterIsBoter = false;
		if(event.getPotion().getShooter() instanceof Player) {
			shooter = (Player) event.getPotion().getShooter();
			shooterIsBoter = this._controller.canUseFreebuilderProtection(shooter);
		}
		if (shooter == null) return;
		for (LivingEntity livingEntity : event.getAffectedEntities()) {
			if (!(livingEntity instanceof Player)) {
				// Boters can't affect non-players
				if (shooterIsBoter) event.setIntensity(livingEntity, 0.0);
				continue;
			}
			Player affectedPlayer = (Player) livingEntity;
			boolean shooterAndAffectedAreTheSamePlayer = affectedPlayer.getUniqueId() == shooter.getUniqueId();
			boolean affectedIsFreebuilder = this._controller.canUseFreebuilderProtection(affectedPlayer);
			// Freebuilders can affect themselves
			if (shooterIsBoter && shooterAndAffectedAreTheSamePlayer) continue;
			// Non boters can affect each other
			if (!shooterIsBoter && !affectedIsFreebuilder) continue;
			event.setIntensity(livingEntity, 0.0);
		}
	}
	
	// Survival players can't fly
	@EventHandler
	public void onPlayerToggleFlightEvent(PlayerToggleFlightEvent event) {
		debug("onPlayerToggleFlightEvent", "Enter");
		if (event.isCancelled()) {
			debug("onPlayerToggleFlightEvent", "Event has already been cancelled. Return.");
			return;
		}
		if (!event.isFlying()) {
			debug("onPlayerToggleFlightEvent", "Not flying, rather landing. Return.");
			return;
		}
		
		Player player = event.getPlayer();
		if (!this._controller.inFreebuildWorld(player, false)) {
			debug("onPlayerToggleFlightEvent", "Not in a boter world. Return.");
			return;
		}
		
		// Allow players with permission bot.canfly to fly
		if (player.hasPermission("bot.canfly")) {
			debug("onPlayerToggleFlightEvent", "User has bot.canfly permission. Return.");
			return;
		}
		
		if (this._controller.isBoter(player)) {
			debug("onPlayerToggleFlightEvent", "The player is a boter. Set fly speed and return.");
			Config.C.setSpeed.execute(Config.V.flySpeed, player.getName());
			return;
		}
		debug("onPlayerToggleFlightEvent", "The player is not allowed to fly. Cancel the event and return.");
		player.sendMessage("You are currently not allowed to fly.");
		event.setCancelled(true);
		Config.C.stopFly.executeAs(event.getPlayer());
	}

	private void debug(String method, String message) {
		this._eithonPlugin.getEithonLogger().debug(DebugPrintLevel.VERBOSE, "%s: %s", method, message);
	}
}
