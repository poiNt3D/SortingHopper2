package me.sothatsit.usefulsnippets;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

public class EnchantGlow extends EnchantmentWrapper {

	private static Enchantment glow;

	private EnchantGlow(String name) {
	    super(name);
	}

	@Override
	public boolean canEnchantItem(ItemStack item) {
	    return true;
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
	    return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
	    return null;
	}

	@Override
	public int getMaxLevel() {
	    return 10;
	}

	@Override
	public String getName() {
		return "Glow";
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	public static Enchantment getGlow() {
		if (glow != null) {
			return glow;
		}

		try {
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
		} catch (Exception e) {
			// e.printStackTrace();
		}

		glow = new EnchantGlow("glow");
		Enchantment.registerEnchantment(glow);
		return glow;
	}

	public static void addGlow(ItemStack item) {
		Enchantment glow = getGlow();
		item.addEnchantment(glow, 1);
	}
	public static void removeGlow(ItemStack item) {
		Enchantment glow = getGlow();
		item.removeEnchantment(glow);
	}
	public static boolean hasGlow(ItemStack item) {
		if (item == null || getGlow() == null || item.getEnchantments() == null) {
			return false;
		}
		return item.getEnchantments().containsKey(getGlow());
	}
}
