package me.sothatsit.usefulsnippets;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GlowHelperUtil {

	public static final String TAG = "GlowHelperUtil";

	public static void addGlow(ItemStack item) {
		L.d(TAG, "addGlow: " + item);
		ItemMeta im = item.getItemMeta();

		if (im == null) {
			return;
		}

		im.setEnchantmentGlintOverride(true);
		item.setItemMeta(im);
	}

	public static void removeGlow(ItemStack item) {
		L.d(TAG, "removeGlow: " + item);
		ItemMeta im = item.getItemMeta();
		if (im == null) {
			return;
		}
		im.setEnchantmentGlintOverride(false);
		item.setItemMeta(im);
	}
}
