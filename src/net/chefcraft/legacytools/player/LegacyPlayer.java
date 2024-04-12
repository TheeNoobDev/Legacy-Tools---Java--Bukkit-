package net.chefcraft.legacytools.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.chefcraft.legacytools.LegacyTools;

public class LegacyPlayer {
	
	private static Map<UUID, LegacyPlayer> players = new HashMap<>();

	private final Player player;
	private Location windChargedLocation = null;
	private boolean hasHit = false;
	private BukkitTask task = null;
	
	public LegacyPlayer(Player player) {
		this.player = player;
		players.put(player.getUniqueId(), this);
	}

	public Player getPlayer() {
		return player;
	}
	
	public static LegacyPlayer getPlayer(UUID uuid) {
		return players.get(uuid);
	}
	
	public static LegacyPlayer getPlayer(Player player) {
		return players.get(player.getUniqueId());
	}

	public Location getWindChargedLocation() {
		return windChargedLocation;
	}

	public void setWindChargedLocation(Location windChargedLocation) {
		this.windChargedLocation = windChargedLocation;
		if (task != null && !task.isCancelled()) {
			task.cancel();
		}
		task = new BukkitRunnable() {
			public void run() {
				setWindChargedLocation();
			}
		}.runTaskLater(LegacyTools.getInstance(), 60L);
	}
	
	private void setWindChargedLocation() {
		this.windChargedLocation = null;
	}
	
	public boolean hasWindChargeEffect() {
		if (this.windChargedLocation != null && this.windChargedLocation.getY() - 1.5D <= player.getLocation().getY()) {
			return true;
		}
		return false;
	}

	public boolean hasMaceHit() {
		return hasHit;
	}

	public void setMaceHit(boolean hasHit) {
		this.hasHit = hasHit;
	}
}
