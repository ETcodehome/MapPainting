
package net.aegistudio.mpp.paint;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import net.aegistudio.mpp.CommandHandle;
import net.aegistudio.mpp.MapPainting;
import java.awt.Color;


/**
 * Handler responsible for giving a specific player a specific paint color. 
 * Does not consume anything and is a direct "give".
 */
public class GivePaintBottleCommand implements CommandHandle {
	
	private static final String redFormat 						= "\u00A74";
	
    public static final String PERMISSION_NODE					= "mpp.command.give.player.paint.bottle";
    public static final String PERMISSION_NODE_FAIL_TOKEN 		= "@mpp.common.permission.fail";
    public static final String PERMISSION_NODE_FAIL_INVARIANT 	= redFormat + "You don't have permission to execute this command."; 
    public String noPermission 									= PERMISSION_NODE_FAIL_INVARIANT;
    
    public static final String RGB_INVALID_TOKEN 				= "@mpp.common.rgb.invalid";
    public static final String RGB_INVALID_INVARIANT 			= redFormat + "The RGB values provided were invalid."; 
    public String rgbInvalid 									= RGB_INVALID_INVARIANT;
    
    public static final String PLAYER_OFFLINE_TOKEN 			= "@mpp.common.player.offline";
    public static final String PLAYER_OFFLINE_INVARIANT 		= redFormat + " is offline."; 
    public String playerOffline 								= PLAYER_OFFLINE_INVARIANT;
    
    public static final String INVENTORY_FULL_TOKEN 			= "@mpp.common.player.inventory.full";
    public static final String INVENTORY_FULL_INVARIANT 		= redFormat + " has no free inventory space."; 
    public String noInventory 									= INVENTORY_FULL_INVARIANT;
    
    public static final String USAGE_GUIDE_TOKEN 				= "@mpp.command.give.player.paint.bottle.usage";
    public static final String USAGE_GUIDE_INVARIANT 			= "Usage: /paint give <player> <red> <green> <blue>"; 
    public String usageGuide 									= USAGE_GUIDE_INVARIANT;
    
    public static final String DESCRIPTION_TOKEN 				= "@mpp.command.give.player.paint.bottle.description";
    public static final String DESCRIPTION_INVARIANT 			= "Gives a player a specific paint bottle color."; 
    public String description 									= DESCRIPTION_INVARIANT;
    
    public static final String SUCCESS_TOKEN 					= "@mpp.command.give.player.paint.bottle.success";
    public static final String SUCCESS_INVARIANT 				= " recieved paint bottle color "; 
    public String success										= SUCCESS_INVARIANT;


    /**
     * Run when initially loaded, checks localization for all strings involved.
     */
	@Override
	public void load(MapPainting plugin, ConfigurationSection config) throws Exception {
		plugin.getLocale(PERMISSION_NODE_FAIL_TOKEN, PERMISSION_NODE_FAIL_INVARIANT, config);
		plugin.getLocale(RGB_INVALID_TOKEN, RGB_INVALID_INVARIANT, config);
		plugin.getLocale(PLAYER_OFFLINE_TOKEN, PLAYER_OFFLINE_INVARIANT, config);
		plugin.getLocale(INVENTORY_FULL_TOKEN, INVENTORY_FULL_INVARIANT, config);
		plugin.getLocale(USAGE_GUIDE_TOKEN, USAGE_GUIDE_INVARIANT, config);
		plugin.getLocale(DESCRIPTION_TOKEN, DESCRIPTION_INVARIANT, config);
		plugin.getLocale(SUCCESS_TOKEN, SUCCESS_INVARIANT, config);
	}

	
	/**
     * Mandatory override
     */
	@Override
	public void save(MapPainting plugin, ConfigurationSection config) throws Exception {
	}

	
	/**
     * Description for this command when detailing it in command lists.
     */
	@Override
	public String description() {
		return description;
	}

	
	/**
     * Handler for the /paint give command.
     * Gives a specific player a bottle of paint of a particular color.
     * @param plugin - Reference to the invoking plugin.
     * @param prefix - The command prefix /paint give.
     * @param sender - The command invoker, can be the console window.
     * @param args[] - Array of space separated arguments passed with the command
     * @return true.
     */
	@Override
	public boolean handle(MapPainting plugin, String prefix, CommandSender sender, String[] args) {
    	
    	// Guard against insufficient permissions to forcibly give a paint bottle
        if (!sender.hasPermission(PERMISSION_NODE)) {
            sender.sendMessage(noPermission);
            return true;
        }
        
        // Guard against too few arguments to generate a bottle
        if (args.length != 4) {
            sender.sendMessage(usageGuide);
            return true;
        }
        
        // Guard against giving items to offline players
        String playerName = args[0];
        Player player = plugin.getServer().getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(args[0] + playerOffline);
            return true;
        }
        
        // Guard against no inventory space to receive the paint bottle
        if (player.getInventory().firstEmpty() == -1){
        	sender.sendMessage(playerName + noInventory);
        	return true;
        }
        
        // Guard against argument casts not being valid (ie it's a string rather than a number)
        int red;
        int green;
        int blue;
        try {
            red = Integer.parseInt(args[1]);
            green = Integer.parseInt(args[2]);
            blue = Integer.parseInt(args[3]);
        }
        catch (Exception e) {
            sender.sendMessage(rgbInvalid);
            return true;
        }
        
        // Guard against bad color range values
        if (!(isBetween(red, 0, 255) && isBetween(green, 0, 255) && isBetween(blue, 0, 255))) {
            sender.sendMessage(rgbInvalid);
            return true;
        }
        
        // Actually give the item
        PaintManager PM = plugin.m_paintManager;
        Color color = new Color(red, green, blue);
        player.getInventory().addItem(PM.getPaintBottle(color));
        sender.sendMessage(playerName + success + PM.getColorName(color));
        
        return true;

	}
	
	
    /**
     * Checks if a number is inside an expected range 
     * @param num - int to check
     * @param min - the lower boundary for success (inclusive)
     * @param max - the upper boundary for success (inclusive)
     * @return boolean - if the check value is between the min and max
     */
    private boolean isBetween(int num, int min, int max) {
        return num >= min && num <= max;
    }
	
	
}

