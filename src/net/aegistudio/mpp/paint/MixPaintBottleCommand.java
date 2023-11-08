package net.aegistudio.mpp.paint;

import net.aegistudio.mpp.MapPainting;
import net.aegistudio.mpp.color.PseudoColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
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
	
	public static final String ONLY_PLAYER = "onlyPlayer";
	public String onlyPlayer;
	public static final String NO_PIGMENT_PERMISSION = "noPermission";
	public String noPigmentPermission;
	public static final String INVALID_FORMAT = "invalidFormat";
	public String invalidFormat;
	public static final String CHARGED = "charged";
	public String charged;
	public static final String CANT_AFFORD = "cantAfford";
	public String cantAfford;
	public static final String NEED_INV = "needInv";
	public String needInv;
	public static final String GAVE_PIGMENT = "gavePigment $name";
	public String gavePigment;
	
	public String description;

	
	public MixPaintBottleCommand() {
		this.description = "@pigment.description";
		this.onlyPlayer = "@pigment.onlyPlayer";
		this.noPigmentPermission = "@pigment.noPigmentPermission";
		this.invalidFormat = "@pigment.invalidFormat";
		this.charged = "@pigment.charged $cost";
		this.cantAfford = "@pigment.charged $cost";
		this.needInv = "@pigment.needInv";
		this.gavePigment = "@pigment.gavePigment $name";
	}

	
	/**
     * Run when initially loaded, checks localization for all strings involved.
     */
	@Override
	public void load(MapPainting plugin, ConfigurationSection section) throws Exception {
		this.onlyPlayer = plugin.getLocale(ONLY_PLAYER, this.onlyPlayer, section);
		this.noPigmentPermission = plugin.getLocale(NO_PIGMENT_PERMISSION, this.noPigmentPermission, section);
		this.invalidFormat = plugin.getLocale(INVALID_FORMAT, this.invalidFormat, section);
		this.charged = plugin.getLocale(CHARGED, this.charged, section);
	}

	
	@Override
	public boolean handle(MapPainting plugin, String prefix, CommandSender sender, String[] args) {
		
		if (plugin.utils.senderIsNonPlayer(sender)) { 
			return false;
		}
		
		if (plugin.utils.permissionCheckFails(PERMISSION_NODE, sender)) {
			return false;
		}
		
		// make sure the command has arguments
		if (args.length == 0) {
			sender.sendMessage("no args");
			// TODO - USAGE EXAMPLE
			return false;
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
				return false;
			}
		}
		
		// check if full RGB value provided
		if ((args.length == 3) && (mixResult == null)) {
			mixResult = plugin.utils.parseStringsToColor(args[0], args[1], args[2], sender);
		}
		
		// TODO - USAGE MESSAGE - can't parse the color
		if (mixResult == null) {
			return false;
		}
		
		// make sure player can recieve the item
		Player player = (Player) sender;
		if (plugin.utils.playerHasNoInventorySpace(player.getName(), sender)) {
			return false;
		}
        
        // check player can afford if economy is active
    	Economy econ = MapPainting.getEconomy();    	
    	int cost = 0;
    	double balance = 0;
    	
		cost = plugin.costPaintRGB;

    	// check balance
    	if (econ != null){	
    		balance = econ.getBalance(player);
    		if (balance < cost) {
    			sender.sendMessage(this.cantAfford.replace("$cost", String.valueOf(cost)));
    			return true;
    		}
    	}
    	
    	// charge the player appropriately
        if (econ != null){
        	EconomyResponse transaction = econ.withdrawPlayer(player,  cost);
        	if (transaction.transactionSuccess()) {
        		sender.sendMessage(this.charged.replace("$cost", String.valueOf(cost)));
        	} else {
        		sender.sendMessage(this.cantAfford.replace("$cost", String.valueOf(cost)));
        		return true;
        	}
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
