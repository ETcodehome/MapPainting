
package net.aegistudio.mpp.paint;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import net.aegistudio.mpp.CommandHandle;
import net.aegistudio.mpp.MapPainting;
import java.awt.Color;


/**
 * Handler responsible for giving a specific player a specific paint color. 
 * Does not consume anything and is a direct "give".
 */
public class GivePaintBottleCommand implements CommandHandle {
	
    public static final String PERMISSION_NODE					= "mpp.command.give.player.paint.bottle";
    
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
     * @return if the command was successfully applied.
     */
	@Override
	public boolean handle(MapPainting plugin, String prefix, CommandSender sender, String[] args) {
    	
        if (plugin.utils.permissionCheckFails(PERMISSION_NODE, sender)) { 
        	return false;
        }
        
        int expectedArguments = 4;
        if (plugin.utils.commandHasIncorrectNumberOfArgs(expectedArguments, args, usageGuide, sender)) { 
        	return false; 
        };
        
        String playerName 	= args[0];
        String red 			= args[1];
        String green		= args[2];
        String blue			= args[3];
        
        if (plugin.utils.playerIsOffline(playerName, sender)) {
        	return false;
        }
        
        if (plugin.utils.playerHasNoInventorySpace(playerName, sender)) {
        	return false;
        }
        
        Color color = plugin.utils.parseStringsToColor(red, green, blue, sender);
        if (color == null) {
        	return false;
        }
        
        PaintManager PM = plugin.m_paintManager;
        plugin.getServer().getPlayer(playerName).getInventory().addItem(PM.getPaintBottle(color));
        sender.sendMessage(playerName + success + PM.getColorName(color));
        
        return true;

	}
	
}

