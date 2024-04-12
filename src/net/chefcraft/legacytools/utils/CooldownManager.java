package net.chefcraft.legacytools.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface CooldownManager {
	
	public static final CooldownManager INSTANCE = new CooldownManagerLegacy();

	boolean hasCooldown(Player player, Material material);
	
	int getCooldown(Player player, Material material);
	
	void setCooldown(Player player, Material material, int ticks);
	
	
}
