package net.chefcraft.legacytools.tools;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import net.chefcraft.legacytools.LegacyTools;
import net.chefcraft.legacytools.utils.CustomExplosions;

public class EnchantmentWindBurst implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player) || event.getCause() == DamageCause.ENTITY_SWEEP_ATTACK) return;
		Player damager = (Player) event.getDamager();
		ItemStack item = damager.getInventory().getItemInMainHand();
		if (item == null || item.getType() == Material.AIR) return;
		if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return;
		float enchPower = (float) checkLore(item.getItemMeta().getLore());
		if (enchPower == 0.0F) return;
		Location loc = damager.getLocation();
		CustomExplosions.windExplode(null, loc, enchPower, (float) LegacyTools.getInstance().getConfig().getDouble("windBurstEnchantment.knockbackSize"), true);
		this.spawnParticles(loc);
		this.playSound(loc);
	}
	
	private double checkLore(final List<String> lore) {
		FileConfiguration config = LegacyTools.getInstance().getConfig();
		for (int i = 0; i < lore.size(); i ++) {
			String line = lore.get(i);
			if (line.contains(config.getString("windBurstEnchantment.windburst_3.name"))) {
				return config.getDouble("windBurstEnchantment.windburst_3.power");
			} else if (line.contains(config.getString("windBurstEnchantment.windburst_2.name"))) {
				return config.getDouble("windBurstEnchantment.windburst_2.power");
			} else if (line.contains(config.getString("windBurstEnchantment.windburst_1.name"))) {
				return config.getDouble("windBurstEnchantment.windburst_1.power");
			}
		}
		return 0.0F;
	}
	
	public void spawnParticles(Location location) {
		World world = location.getWorld();
		for (float f = 1.0F; f <= 4.5F; f += 0.5F) {
			double xr = Math.random() * f;
			double xz = Math.random() * f;
			world.spawnParticle(Particle.EXPLOSION_LARGE,
					location.getX() + CustomExplosions.randomPositiveOrNegative(xr),
					location.getY(),
					location.getZ() + CustomExplosions.randomPositiveOrNegative(xz), 1);
		}
	}
	
	public void playSound(Location location) {
		World world = location.getWorld();
		world.playSound(location, Sound.ENTITY_BLAZE_SHOOT, 1F, 1.4F + (float) (Math.random() * 2.0D * 0.2D));
    	world.playSound(location, Sound.ENTITY_WITHER_SHOOT, 1F, 1.4F + (float) (Math.random() * 2.0D * 0.2D));
    	world.playSound(location, Sound.BLOCK_GRASS_BREAK, 1F, 0.9F);
	}
}
