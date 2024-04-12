package net.chefcraft.legacytools.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

import net.chefcraft.legacytools.player.LegacyPlayer;

public class CustomExplosions {
	
	public static final Random RANDOM = new Random();

	public static void windExplode(@Nullable Player source, Location location, float power, float size, boolean sourceBounce) {
    	List<Entity> entities = new ArrayList<>(location.getWorld().getNearbyEntities(location, size, size, size));
    	
    	for (int i = 0; i < entities.size(); i++) {
    		Entity entity = entities.get(i);
    		if (entity instanceof Snowball) continue;
    		if (entity instanceof Player && ((Player) entity).getGameMode() == GameMode.SPECTATOR) continue;
    		
    		Location entityLoc = entity.getLocation();
    		boolean flag = source != null && source.getUniqueId().equals(entity.getUniqueId());
    		
    		if (flag && !sourceBounce) continue; 
    		
    		double distance = entity.getLocation().distanceSquared(entityLoc) / power;
    		double eyeHeight = entity instanceof LivingEntity ? ((LivingEntity) entity).getEyeHeight() : 1.53D;
			double dx = entityLoc.getX() - location.getX();
			double dy = entityLoc.getY() + eyeHeight - location.getY();
			double dz = entityLoc.getZ() - location.getZ();
			double dq = Math.sqrt(dx * dx + dy * dy + dz * dz);
			if (dq != 0.0D) {
				dx /= dq;
				dy /= dq;
				dz /= dq;
				double face = (1.0D - distance) * power;
				Vector vec = new Vector(dx * face, dy * face, dz * face);
				
				if (flag) {
					LegacyPlayer lp = LegacyPlayer.getPlayer(source);
					if (lp == null) return;
					lp.setWindChargedLocation(entity.getLocation());
				}
				
				entity.setVelocity(entity.getVelocity().add(vec).multiply(power));
			}
    	}
    }
	
	public static void windExplodeSelf(Location location, Entity entity, float power) {
		if (entity instanceof Snowball) return;
		if (entity instanceof Player && ((Player) entity).getGameMode() == GameMode.SPECTATOR) return;
		
		Location entityLoc = entity.getLocation();
		
		double distance = entity.getLocation().distanceSquared(entityLoc) / power;
		double eyeHeight = entity instanceof LivingEntity ? ((LivingEntity) entity).getEyeHeight() : 1.53D;
		double dx = entityLoc.getX() - location.getX();
		double dy = entityLoc.getY() + eyeHeight - location.getY();
		double dz = entityLoc.getZ() - location.getZ();
		double dq = Math.sqrt(dx * dx + dy * dy + dz * dz);
		if (dq != 0.0D) {
			dx /= dq;
			dy /= dq;
			dz /= dq;
			double face = (1.0D - distance) * power;
			Vector vec = new Vector(dx * face, dy * face, dz * face);
			entity.setVelocity(entity.getVelocity().add(vec).multiply(power));
		}
    }
	
	public static double randomPositiveOrNegative(double number) {
		return RANDOM.nextBoolean() ? number : (-number);
	}
}
