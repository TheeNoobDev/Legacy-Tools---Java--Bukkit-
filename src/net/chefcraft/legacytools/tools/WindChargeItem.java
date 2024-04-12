package net.chefcraft.legacytools.tools;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;

import net.chefcraft.legacytools.LegacyTools;
import net.chefcraft.legacytools.LegacyToolsPerms;
import net.chefcraft.legacytools.player.LegacyPlayer;
import net.chefcraft.legacytools.utils.CustomExplosions;

public class WindChargeItem implements Listener {

	@EventHandler
	public void onWindChargeThrow(ProjectileLaunchEvent event) {
		Projectile projectile = event.getEntity();
		if (!(projectile instanceof Snowball) || !(projectile.getShooter() instanceof Player)) return;
		Player player = (Player) projectile.getShooter();
		if (!player.hasPermission(LegacyToolsPerms.WIND_CHARGE)) return;
		if (player.hasCooldown(LegacyTools.SNOWBALL)) {
			event.setCancelled(true);
		} else {
			projectile.setMetadata("wind_charge", new FixedMetadataValue(LegacyTools.getInstance(), null));
			projectile.setGravity(false);
			player.setCooldown(LegacyTools.SNOWBALL, 10);
			LegacyPlayer lp = LegacyPlayer.getPlayer(player);
			if (lp == null) return;
			if (lp.getWindChargedLocation() == null) {
				lp.setWindChargedLocation(null);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onWindChargeHit(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		if (!(projectile instanceof Snowball) || !(projectile.getShooter() instanceof Player) || !projectile.hasMetadata("wind_charge")) return;
		Location loc = projectile.getLocation();
		FileConfiguration config = LegacyTools.getInstance().getConfig();
		CustomExplosions.windExplode((Player) projectile.getShooter(), loc, (float) config.getDouble("windCharge.power"), (float) config.getDouble("windCharge.knockbackSize"), true);
		this.spawnParticles(loc);
		this.playSound(loc);
		/*Block block = event.getHitBlock();
		if (block != null && !block.isEmpty()) {
			try {
				if (block.getState() instanceof NoteBlock) {
					NoteBlock note = ((NoteBlock) block.getState());
					note.play(note.getRawData(), note.getRawNote());
				} else {
					String name = block.getType().name().toLowerCase();
					if (name.contains("button")
						|| name.contains("door")
						|| name.contains("sensor")
						|| name.contains("target")
						|| name.contains("plate")
						|| name.contains("lever")) {
					}
				}
			} catch (Exception x) {
				x.printStackTrace();
			}
		}*/
	}
	
	@EventHandler
	public void onWindChargeDamage(EntityDamageEvent event) {
		if (event.getCause() != DamageCause.FALL) return;
		Entity entity = event.getEntity();
		if (!(entity instanceof Player)) return; 
		LegacyPlayer lp = LegacyPlayer.getPlayer(entity.getUniqueId());
		if (lp == null) return;
		if (lp.hasWindChargeEffect()) {
			event.setCancelled(true);
		}
	}
	
	public void spawnParticles(Location location) {
		World world = location.getWorld();
		for (int i = 0; i < 3; i++) {
			world.spawnParticle(Particle.EXPLOSION_LARGE, location.getX() + Math.random(), location.getY() + Math.random(), location.getZ() + Math.random(), 1);
		}
	}
	
	public void playSound(Location location) {
		World world = location.getWorld();
		world.playSound(location, Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.3F + (float) (Math.random() * 2.0D * 0.2D));
    	world.playSound(location, Sound.ENTITY_WITHER_SHOOT, 1.0F, 1.3F + (float) (Math.random() * 2.0D * 0.2D));
    	world.playSound(location, Sound.BLOCK_GRASS_BREAK, 1.0F, 0.9F);
	}
}
