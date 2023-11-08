package net.aegistudio.mpp.common;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import net.aegistudio.mpp.MapPainting;
import net.aegistudio.mpp.Module;
import java.awt.Color;


public class Utils implements Module {

	// Common references
	public MapPainting plugin;
	public ConfigurationSection config;
	
	// Formatting strings
	public static final String redFormat 						= "\u00A74";
	
	// Common strings
    public static final String PERMISSION_NODE_FAIL_TOKEN 		= "@mpp.common.permission.fail";
    public static final String PERMISSION_NODE_FAIL_INVARIANT 	= redFormat + "You don't have permission to execute this command."; 
    public String noPermission 									= PERMISSION_NODE_FAIL_INVARIANT;
    
    public static final String PLAYER_OFFLINE_TOKEN 			= "@mpp.common.player.offline";
    public static final String PLAYER_OFFLINE_INVARIANT 		= redFormat + " is offline."; 
    public String playerOffline 								= PLAYER_OFFLINE_INVARIANT;
    
    public static final String PLAYER_ONLY_CMD_TOKEN 			= "@mpp.common.player.only.command";
    public static final String PLAYER_ONLY_CMD_INVARIANT 		= redFormat + "Only players may execute this command."; 
    public String playerOnlyCommand 							= PLAYER_ONLY_CMD_INVARIANT;
    
    public static final String INVENTORY_FULL_TOKEN 			= "@mpp.common.player.inventory.full";
    public static final String INVENTORY_FULL_INVARIANT 		= redFormat + " has no free inventory space."; 
    public String noInventory 									= INVENTORY_FULL_INVARIANT;
    
    public static final String RGB_INVALID_TOKEN 				= "@mpp.common.rgb.invalid";
    public static final String RGB_INVALID_INVARIANT 			= redFormat + "The RGB values provided were invalid."; 
    public String rgbInvalid 									= RGB_INVALID_INVARIANT;
    
    
	public Utils(MapPainting mapPainting) {
		plugin = mapPainting;
	}


	@Override
	public void load(MapPainting plugin, ConfigurationSection config) {
		
		// Update references
		this.config = config;
		
		// Localize common strings
		plugin.getLocale(PERMISSION_NODE_FAIL_TOKEN, PERMISSION_NODE_FAIL_INVARIANT, config);
		plugin.getLocale(PLAYER_OFFLINE_TOKEN, PLAYER_OFFLINE_INVARIANT, config);
		plugin.getLocale(INVENTORY_FULL_TOKEN, INVENTORY_FULL_INVARIANT, config);
		plugin.getLocale(RGB_INVALID_TOKEN, RGB_INVALID_INVARIANT, config);
		plugin.getLocale(PLAYER_ONLY_CMD_TOKEN, PLAYER_ONLY_CMD_INVARIANT, config);
		
	}


	@Override
	public void save(MapPainting plugin, ConfigurationSection config) throws Exception {
		// TODO Auto-generated method stub
	}
    
    
    /**
     * Checks if a player is offline. Performs no validity checks for if playerName is an actual player.
     * @param playerName - check by name if the player is online.
     * @param sender - the commandSender to notify if failed
     * @return boolean - if the player is online
     */
    public boolean playerIsOffline(String playerName, CommandSender sender) {
    	
        Player player = sender.getServer().getPlayer(playerName);
        if (player == null) {
        	sender.sendMessage(playerName + playerOffline);
            return true;
        }
    	return false;    	
    }

    
    /**
     * Checks if a commandSender has a permission node.
     * @param permissionNode - the node to check
     * @param sender - the commandSender to check and notify if failed
     * @param failureMessage - Failure message override (must already be localized by caller)
     * @return boolean - true if the check fails, false if the player has the permission
     */
	public boolean permissionCheckFails(String permissionNode, CommandSender sender, String failureMessage) {
		
        if (!sender.hasPermission(permissionNode)) {
        	sender.sendMessage(failureMessage);
        	return true;
        }
		return false;
	}
	
	
	/**
     * Checks if a commandSender has a permission node.
     * @param permissionNode - the node to check
     * @param sender - the commandSender to check and notify if failed
     * @return boolean - true if the check fails, false if the player has the permission
     */
	public boolean permissionCheckFails(String permissionNode, CommandSender sender) {
		return this.permissionCheckFails(permissionNode, sender, noPermission);
	}


	/**
     * Checks if a command has the right number of parameters (matches expected)
     * @param expectedCount - the number of args expected
     * @param args - the command args
     * @param usageGuide - the feedback to give the sender (ie how to use the command)
     * @param sender - the commandSender to check and notify if failed
     * @return boolean - true if the check fails, false if the number of args is correct
     */
	public boolean commandHasIncorrectNumberOfArgs(int expectedCount, String[] args, String usageGuide, CommandSender sender) {
		
        if (args.length != expectedCount) {
            sender.sendMessage(usageGuide);
            return true;
        }
		return false;
	}


	/**
     * Checks if playerName has a free inventory slot.
     * @param playerName - the player to search for a free inventory slot
     * @param sender - the commandSender to check and notify if failed
     * @return boolean - true if the check fails, false if the number of args is correct
     */
	public boolean playerHasNoInventorySpace(String playerName, CommandSender sender) {
		
        if (plugin.getServer().getPlayer(playerName).getInventory().firstEmpty() == -1){
        	sender.sendMessage(playerName + noInventory);
        	return true;
        }
		
		return false;
	}


    /**
     * Checks if a number is inside an expected range 
     * @param num - int to check
     * @param min - the lower boundary for success (inclusive)
     * @param max - the upper boundary for success (inclusive)
     * @return boolean - if the check value is between the min and max
     */
    public final static boolean isBetween(int num, int min, int max) {
        return num >= min && num <= max;
    }
	
	
	/**
     * Parses strings to a color if possible, and notifies the sender if it could not be parsed.
     * @param redString - the red channel component to parse. Expect int in range 0-255.
     * @param greenString - the green channel component to parse. Expect int in range 0-255.
     * @param blueString - the blue channel component to parse. Expect int in range 0-255.
     * @param sender - the commandSender to check and notify if failed
     * @return If successful returns the range checked, parsed Color. If unsuccessful, returns null and notifies the sender.
     */
	public Color parseStringsToColor(String redString, String greenString, String blueString, CommandSender sender) {
		
        // Guard against argument casts not being valid (ie it's a string rather than a number)
        int red;
        int green;
        int blue;
        try {
            red = Integer.parseInt(redString);
            green = Integer.parseInt(greenString);
            blue = Integer.parseInt(blueString);
        }
        catch (Exception e) {
            sender.sendMessage(rgbInvalid);
            return null;
        }
        
        // Guard against bad color range values
        if (!(isBetween(red, 0, 255) && isBetween(green, 0, 255) && isBetween(blue, 0, 255))) {
            sender.sendMessage(rgbInvalid);
            return null;
        }
        
		return new Color(red, green, blue);
	}

	/**
     * Checks if a command sender is not a player and notifies them if the command is for players only.
     * @param sender - the commandSender to check and notify if failed
     * @return If sender is a player returns false. Otherwise returns true.
     */
	public boolean senderIsNonPlayer(CommandSender sender) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(playerOnlyCommand);
			return true;
		}
		return false;
	}
	
	
	

    
    
	
}