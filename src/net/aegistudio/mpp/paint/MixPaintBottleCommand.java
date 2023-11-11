package net.aegistudio.mpp.paint;

import net.aegistudio.mpp.MapPainting;
import net.aegistudio.mpp.CommandHandle;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.awt.Color;


/**
 * Handler responsible for letting players mix dyes by command to yield a specific color paint bottle. 
 * Uses consumables to produce the resultant item.
 */
public class MixPaintBottleCommand implements CommandHandle {
	
    public static final String PERMISSION_NODE					= "mpp.command.mix.paint.bottle";

	public static final String GAVE_PIGMENT = "gavePigment $name";
	public String gavePigment;
	
	public String description;

	
	public MixPaintBottleCommand() {
		this.description = "@pigment.description";
		this.gavePigment = "@pigment.gavePigment $name";
	}

	
	/**
     * Run when initially loaded, checks localization for all strings involved.
     */
	@Override
	public void load(MapPainting plugin, ConfigurationSection section) throws Exception {
		
	}

	
	@Override
	public boolean handle(MapPainting plugin, String prefix, CommandSender sender, String[] args) {
		
		if (plugin.utils.senderIsNonPlayer(sender)) { 
			return true;
		}
		
		if (plugin.utils.permissionCheckFails(PERMISSION_NODE, sender)) {
			return true;
		}
		
		// make sure the command has arguments
		if (args.length == 0) {
			sender.sendMessage("no args");
			// TODO - USAGE EXAMPLE
			return true;
		}
		
		Color mixResult = null;
		
		// TODO - Historic names are all single words, need to fix this gate
		// Attempt name based handling "ie /paint mix Absolute Black"
		// currently names all declared in config settings resolving from pseudocolor. Kinda ugly solution.
		if (args.length == 1) {
			
			// TODO - this handling is bad, 
			// Implement a reverse lookup table for paint colors that gets the mixResult Color if it exists
			
			// check default color maps (ie "red", "green")
			mixResult = plugin.m_colorManager.parseColor(args[0]).color;
			if(mixResult == null) {
				// sender.sendMessage(invalidFormat);
				// TODO - "Invalid color name" perhaps - review how this gate works overall with rest of function
				return true;
			}
		}
		
		// check if full RGB value provided
		if ((args.length == 3) && (mixResult == null)) {
			mixResult = plugin.utils.parseStringsToColor(args[0], args[1], args[2], sender);
		}
		
		// TODO - USAGE MESSAGE - can't parse the color
		if (mixResult == null) {
			return true;
		}
		
		// make sure player can receive the item
		Player player = (Player) sender;
		if (plugin.utils.playerHasNoInventorySpace(player.getName(), sender)) {
			return true;
		}

        // Actually generate the item and give it to the player
		ItemStack item = plugin.m_paintManager.getPaintBottle(mixResult);
		player.getInventory().addItem(item);
		
		sender.sendMessage(this.gavePigment);
		
		return true;
	}

	@Override
	public void save(MapPainting plugin, ConfigurationSection config) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String description() {
		return description;
	}
}
