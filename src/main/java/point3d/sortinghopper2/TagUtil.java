package point3d.sortinghopper2;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.configuration.file.YamlConfiguration;
public class TagUtil {
	private final static File groupsymlfile = new File("plugins" + File.separator + "SortingHopper2" + File.separator + "itemgroups.yml");
	
	/**
	 * Import currently loaded Minecraft item tags, should work with resourcepacks too
	 * See https://minecraft.gamepedia.com/Tag for details
	 */
	public static void importMinecraftTags(){
		SortingHopper.mclog.info("[SortingHopper2] Creating new itemgroups.yml...");
		SortingTag.reset();
		
		Field[] declaredFields = Tag.class.getDeclaredFields();
		for (Field field : declaredFields) {
			if (field.getGenericType().toString().equals("org.bukkit.Tag<org.bukkit.Material>")) {
	
				
				
				try {
					@SuppressWarnings("unchecked")
					Tag<Material> tag = (Tag<Material>) field.get(null);
					new SortingTag(field.getName(), tag.getValues());
					
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}      	 
		      	
			      	  
			} 

		}
		saveSortingTags();
	}
	
	/*
	 * Save currently loaded SortingTag objects
	 * from SortingTag.alltags HashMap to groupsymlfile
	 */
	
	public static void saveSortingTags(){

		if(groupsymlfile.exists()){
			groupsymlfile.delete();
		}
		
		YamlConfiguration groupsyml =  new YamlConfiguration();// YamlConfiguration.loadConfiguration(groupsymlfile);
		
		for(Map.Entry<String, SortingTag> entry : SortingTag.getAllTags().entrySet()) {			
				groupsyml.set(entry.getKey(), getItemsString(entry.getValue()));
		}
		
		try {
			groupsyml.save(groupsymlfile);
		} 
		catch(IOException e) {
			  e.printStackTrace();
		}
	}
	
	/*
	 * Load item groups from groupsymlfile
	 * and create new SortingTag objects
	 */
	@SuppressWarnings("unchecked")
	public static void loadSortingTags(){
		SortingTag.reset();
		
		if(!groupsymlfile.exists()){
			importMinecraftTags();
		}
		YamlConfiguration groupsyml =  YamlConfiguration.loadConfiguration(groupsymlfile);

		for(String listname : groupsyml.getKeys(false)){
			if(groupsyml.getList(listname) instanceof List<?>){
				Set<Material> materials =  new HashSet<>();
				for(String itemname : (List<String>)groupsyml.getList(listname)){
					if(Material.getMaterial(itemname) == null){
						SortingHopper.mclog.warning("[SortingHopper2] Invalid material name: " + itemname + " in itemgroups.yml");
						SortingHopper.mclog.warning("[SortingHopper2] Check https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html for reference!");
					}
					else {
						materials.add(Material.getMaterial(itemname));
					}
				}
				
				new SortingTag(listname, materials);
				SortingHopper.DebugLog("Added " + listname);
			}
			else {
				SortingHopper.mclog.warning("[SortingHopper2] Invalid array: " + listname + " in itemgroups.yml");
			}
		}	
	}
	/*
	 * Returns List<String> of items which given SortingTag object contains
	 */
	private static List<String> getItemsString(SortingTag tag){
		List<String> names = new ArrayList<String>();
		for(Material material: tag.getValues()){
			names.add(material.name());
		}
		return names;
	}

}
