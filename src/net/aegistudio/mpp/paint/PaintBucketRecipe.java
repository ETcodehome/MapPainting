
package net.aegistudio.mpp.paint;

import java.awt.Color;
import net.aegistudio.mpp.MapPainting;
import net.aegistudio.mpp.Module;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;


/**
 * Responsible for definition, registration and configuration of the paint bucket crafting recipe.
 */
public class PaintBucketRecipe implements Module {
    
    // Quick reference to the main plugin for ease of accessing other classes
    public MapPainting plugin;
    
    
    /**
     * Checks if an item is a milk bucket or not.
     * @param item - An ItemStack that should be a milk bucket
     * @return boolean - If the item is a milk bucket or not.
     */
    public boolean isItemMilkBucket(ItemStack item) {
    	return item.getType() == Material.MILK_BUCKET;
    }

    
    /**
     * Gets the color of the milk bucket item supplied. Returns null if not a milk bucket.
     * @param item - An ItemStack that should be a milk bucket
     * @return Color or null - The color of the paint bucket, null if item is incorrectly typed
     */
    public Color getColor(ItemStack item) {
    	if (!isItemMilkBucket(item)) return null;
    	Color resultColor = plugin.m_paintManager.getItemColor(item);
    	return resultColor;
    }

    
    /**
     * Sets a passed milk bucket ItemStack to the paint color supplied. No op if not a milk bucket.
     * @param item - An ItemStack that should be a milk bucket
     * @param color - The new color for the resulting paint bucket
     */
    public void setColor(ItemStack item, Color color) {
    	if (!isItemMilkBucket(item)) return;
        plugin.m_paintManager.setColor(item, color);
    }

    
    /**
     * Adds a paint bucket listener that handles interactions related to paint buckets
     */
    public void RegisterPaintBucketListener() {
    	PaintBucketListener listener = new PaintBucketListener(plugin);
        plugin.getServer().getPluginManager().registerEvents((Listener)listener, (Plugin)plugin);
    }
    
    
    /**
     * Defines and registers the recipe for paint buckets
     */
	public void AddPaintBucketRecipe() {
    	
    	// Define the resulting item
    	ItemStack result = new ItemStack(Material.MILK_BUCKET);
        
        // Define the unique recipe key
        NamespacedKey recipeName = new NamespacedKey(plugin, "paintbuckets");
        
        // Define the recipe
        ShapelessRecipe recipe = new ShapelessRecipe(recipeName, result);
        recipe.addIngredient(Material.POTION);
        recipe.addIngredient(Material.MILK_BUCKET);
        
        // Register the recipe
        plugin.getServer().addRecipe((Recipe)recipe);
    }
    
    
    /**
     * Mandatory override declaration for modules.
     * Runs when the main plugin is loaded
     * @param plugin - Reference to the main plugin loaded
     * @param config - Reference to the main configuration file settings loaded
     */
    @Override
    public void load(MapPainting plugin, ConfigurationSection config) throws Exception {
        
    	// Update the uninstantiated plugin reference
    	this.plugin = plugin;
        
        AddPaintBucketRecipe();
        RegisterPaintBucketListener();
    }

    
    /**
     * Mandatory override declaration for modules
     * @param plugin - Reference to the main plugin loaded
     * @param config - Reference to the main configuration file settings loaded
     */
    @Override
    public void save(MapPainting plugin, ConfigurationSection config) throws Exception {
    }
    
    
}

