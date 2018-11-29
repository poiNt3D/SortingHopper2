package point3d.sortinghopper2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;

public class SortingTag{
	
	private static Map<Material, List<String>> materialtags = new  HashMap<>();
	private static Map<String, SortingTag> alltags = new HashMap<>();

	private String name;
	private Set<Material> items;
	
	public SortingTag(String name, Set<Material> items){
		
		this.name = name;
		this.items = items;
		alltags.put(name, this);
		
		for(Material item : items){
			addMaterailTag(item, this);
		}
	}
	
	public String getName(){
		return this.name;
	}
	public Set<Material> getValues(){
		return this.items;
	}
	
	
	public List<String> getValuesString(){
		List<String> names = new ArrayList<String>();
		for(Material item: items){
			names.add(item.name());
		}
		return names;
	}
	
	public boolean isTagged(Material item){
		return this.items.contains(item);
	}
	
	public static void reset(){
		
		materialtags = new  HashMap<>();
		alltags = new HashMap<>();
		
	}
	
	
	public static SortingTag getSortingTag(String name){
		return alltags.get(name);
	}
	
	public static List<String> getMaterialTags(Material material){	
		return materialtags.get(material);
	}
	
	public static  Map<String, SortingTag> getAllTags(){
		return alltags;
	}
	
	private static void addMaterailTag(Material material, SortingTag sortingtag){
		if(!materialtags.containsKey(material)){
			materialtags.put(material, new ArrayList<String>());
		}
		materialtags.get(material).add(sortingtag.name);
	}

	
}
