package me.sothatsit.usefulsnippets;

import static org.bukkit.Registry.*;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.logging.Logger;

public class EnchantGlow extends Enchantment {

	private EnchantGlow() {
		super();
	}

	@Override
	public boolean canEnchantItem(ItemStack item) {
	    return false;
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
	public boolean isTreasure() {
		return false;
	}

	@Override
	public boolean isCursed() {
		return false;
	}

	@Override
	public int getMaxLevel() {
	    return 0;
	}

	@Override
	public String getName() {
		return "Glow";
	}

	@Override
	public int getStartLevel() {
		return 0;
	}

	public static void addGlow(ItemStack item) {
		ItemMeta im = item.getItemMeta();
		if (im == null) {
			return;
		}
		im.setEnchantmentGlintOverride(true);
		item.setItemMeta(im);
	}

	public static void removeGlow(ItemStack item) {
		ItemMeta im = item.getItemMeta();
		if (im == null) {
			return;
		}
		im.setEnchantmentGlintOverride(false);
		item.setItemMeta(im);
	}

	@Override
	public NamespacedKey getKey() {
		return null;
	}

	@Override
	public String getTranslationKey() {
		return "";
	}
}
