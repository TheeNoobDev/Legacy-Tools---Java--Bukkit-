package net.chefcraft.legacytools.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import net.chefcraft.legacytools.LegacyTools;
import net.chefcraft.legacytools.LegacyToolsPerms;
import net.chefcraft.legacytools.player.LegacyPlayer;
import net.chefcraft.legacytools.utils.CustomExplosions;

public class MaceItem implements Listener {
	
	private static final Random RANDOM = new Random();
	private static Material material = Material.DIAMOND_SWORD;

	@EventHandler(priority = EventPriority.LOWEST)
	public void onDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player)) return;
		Player damager = (Player) event.getDamager();
		ItemStack item = damager.getInventory().getItemInMainHand();
		if (item == null || item.getType() == Material.AIR) return;
		if (item.getType() != Material.DIAMOND_SWORD || !damager.hasPermission(LegacyToolsPerms.MACE)) return;
		LegacyPlayer lp = LegacyPlayer.getPlayer(damager);
		if (lp == null || damager.isOnGround() || damager.getFallDistance() < 2.0F) return;
		lp.setMaceHit(true);
		List<Block> blocks = this.getNearbyBlocks(damager, 2);
		LegacyTools lg = LegacyTools.getInstance();
		FileConfiguration config = lg.getConfig();
		event.setDamage((event.getDamage() + damager.getFallDistance()) * config.getDouble("mace.damageMultiplier"));
		Bukkit.getScheduler().runTaskLater(lg, ()-> {
			CustomExplosions.windExplode(damager, damager.getLocation(), (float) config.getDouble("mace.knockback"), (float) config.getDouble("mace.knockbackSize"), false);
			this.playSound(damager.getLocation(), !blocks.isEmpty());
			if (!blocks.isEmpty()) {
				this.spawnParticles(damager.getLocation(), blocks);
			}
		}, 1L);
		Bukkit.getScheduler().runTaskLater(lg, ()-> {
			lp.setMaceHit(false);
		}, 20L);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onWindChargeDamage(EntityDamageEvent event) {
		if (event.getCause() != DamageCause.FALL) return;
		Entity entity = event.getEntity();
		if (!(entity instanceof Player)) return; 
		LegacyPlayer lp = LegacyPlayer.getPlayer(entity.getUniqueId());
		if (lp == null || !lp.hasMaceHit()) return;
		event.setCancelled(true);
		lp.setMaceHit(false);
	}
	
	public void playSound(Location location, boolean ground) {
		World world = location.getWorld();
		world.playSound(location, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 1.6F + (RANDOM.nextInt(3) * 0.1F));
		world.playSound(location, Sound.BLOCK_GRASS_STEP, 1.0F, 0.7F + (RANDOM.nextInt(3) * 0.1F));
		world.playSound(location, Sound.ENTITY_BLAZE_HURT, 1.0F, 0.75F + (RANDOM.nextInt(3) * 0.1F));
		if (ground) {
			world.playSound(location, Sound.ENTITY_WITHER_BREAK_BLOCK, 1.0F, 0.88F + (RANDOM.nextInt(3) * 0.1F));
		}
	}
	
	public void spawnParticles(Location location, List<Block> blocks) {
		World world = location.getWorld();
		
		for (int i = 1; i <= 36; i++) {
			double x = (2.4D * Math.cos(i * 10.D)) + location.getX();
			double z = (2.4D * Math.sin(i * 10.D)) + location.getZ();
			Block block = blocks.get(RANDOM.nextInt(blocks.size()));
			@SuppressWarnings("deprecation")
			MaterialData data = new MaterialData(block.getType(), block.getData());
			world.spawnParticle(Particle.BLOCK_CRACK, x, location.getY(), z, 4, data);
			world.spawnParticle(Particle.BLOCK_CRACK, x, location.getY(), z, 8, 0, 3, 0, data);
			world.spawnParticle(Particle.BLOCK_CRACK,
					location.getX() + CustomExplosions.randomPositiveOrNegative(RANDOM.nextInt(5) * 0.1D),
					location.getY() - 0.2D,
					location.getZ() + CustomExplosions.randomPositiveOrNegative(RANDOM.nextInt(5) * 0.1D), 6, data);
		}
	}
	
	
	public List<Block> getNearbyBlocks(Entity entity, int size) {
		List<Block> blocks = new ArrayList<>();
		Location loc = entity.getLocation();
		
		int startX = loc.getBlockX() - size;
		int startY = loc.getBlockY() - size - 1;
		int startZ = loc.getBlockZ() - size;
		
		int endX = loc.getBlockX() + size;
		int endY = loc.getBlockY() + size + 1;
		int endZ = loc.getBlockZ() + size;
		
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				for (int z = startZ; z <= endZ; z++) {
					Block block = loc.getWorld().getBlockAt(x, y, z);
					if (block.isEmpty()) continue;
					blocks.add(block);
				}
			}
		}
		return blocks;
	}

	public static Material getMaterial() {
		return material;
	}

	public static void setMaterial(Material material) {
		MaceItem.material = material;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
