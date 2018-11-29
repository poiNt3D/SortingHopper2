/* 
 * https://gist.github.com/JustRayz/cacdb2995df4cd54531f
 */

package point3d.sortinghopper2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class Serialization {

    public static String toBase64(Inventory inventory) {
        return toBase64(inventory.getContents());
    }

    public static String toBase64(ItemStack itemstack){
        ItemStack[] arr = new ItemStack[1];
        arr[0] = itemstack;
        return toBase64(arr);
    }

    public static String toBase64(ItemStack[] contents) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(contents.length);

            for (ItemStack stack : contents) {
                dataOutput.writeObject(stack);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static Inventory inventoryFromBase64(String data) throws IOException {
        ItemStack[] stacks = stacksFromBase64(data);
        //Inventory inventory = Bukkit.createInventory(null, (int) Math.ceil(stacks.length / 9D) * 9);
        Inventory inventory = Sorter.createInv();
        for (int i = 0; i < stacks.length; i++) {
      	if(i>=inventory.getSize()){
      		SortingHopper.mclog.warning("[SortingHopper] Warning: Saved inventory is larger than configured inv size");
      		break;
      	}
            inventory.setItem(i, stacks[i]);
            
        }

        return inventory;
    }

    public static ItemStack[] stacksFromBase64(String data) throws IOException {
        try {
            if(data == null || Base64Coder.decodeLines(data) == null) return new ItemStack[]{};
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] stacks = new ItemStack[dataInput.readInt()];

            for (int i = 0; i < stacks.length; i++) {
                stacks[i] = (ItemStack) dataInput.readObject();
            }
            dataInput.close();
            return stacks;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
    public static String locationToString(Location loc) {
	        String string = loc.getWorld().getName() + "//" + (int)loc.getX() + "//" + (int)loc.getY() + "//" + (int)loc.getZ();
	        return string;
    }
    public static Location stringToLocation(String string) {
	    String[] s = string.split("//");
	    Location loc = new Location(Bukkit.getWorld(s[0]), Double.parseDouble(s[1]), Double.parseDouble(s[2]), Double.parseDouble(s[3]));
	    return loc;
    }
}