
package net.aegistudio.mpp.factory;

import net.aegistudio.mpp.ActualHandle;
import net.aegistudio.mpp.MapPainting;
import net.aegistudio.mpp.canvas.Canvas;
import net.aegistudio.mpp.canvas.CanvasManager;
import net.aegistudio.mpp.canvas.MapCanvasRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;


public abstract class BaseCreateCommand extends ActualHandle {
	
	protected String paramList = "[<parameter>]";
  
	
	public boolean handle(MapPainting plugin, String prefix, CommandSender sender, String[] args) {
		
		String usageGuide = "Create command failed, no painting name provided";
		if (plugin.utils.commandHasTooFewArgs(1, args, usageGuide, sender)){
	    	sender.sendMessage(prefix + " <name> " + this.paramList);
	    	return true;
	    }
	    
		// Despite the fact we check this in the sub-commands, we guard against the sender not 
		// being a player as much of the implementation below relies on it.
		if (plugin.utils.senderIsNonPlayer(sender)) {
			return true;
		}
	    
	    Player player = (Player)sender;
	    String name = args[0];
	    
	    // Sanitize new name
	    name = name.replace('.', '_');
	    name = name.replace('/', '_');
	    name = name.replace('\\', '_');
	    
	    // Guard against new canvas name being an existing canvas name
	    if (plugin.m_canvasManager.nameCanvasMap.containsKey(name)) {
	    	// TODO - better localization
	        sender.sendMessage(plugin.m_commandCreateHandler.canvasAlreadyExisted.replace("$canvasName", name));
	        return true;
	    }
	    
	    // Guard against the player having insufficient inventory space
	    if (plugin.utils.playerHasNoInventorySpace(player.getName(), sender)) {
	    	return true;
	    }
	    
	    // Creates the canvas instance for the new painting
	    String[] subArguments = new String[args.length - 1];
	    System.arraycopy(args, 1, subArguments, 0, args.length - 1);
	    Canvas canvasInstance = create(plugin, (CommandSender)player, subArguments);
	    
	    // Guard against the canvas not being able to be created
	    if (canvasInstance == null) {
	    	return true;
	    }
	    
	    // Create the painting data record and link the relevant objects
	    MapCanvasRegistry paintingData = new MapCanvasRegistry(name);
        paintingData.canvas = canvasInstance;
        paintingData.owner = player.getName();
        paintingData.painter.add(player.getName());
        
        // Actually generate the painting and give it to the player
        generateNewPainting(plugin, sender, paintingData);
        return true;
	    
	}

  	
  	protected abstract Canvas create(MapPainting plugin, CommandSender paramCommandSender, String[] paramArrayOfString);
  	// Sub-commands need to implement this method to define how the canvas instance should be generated.
  	
	
	@SuppressWarnings("deprecation")
	public void generateNewPainting(MapPainting plugin, CommandSender sender, MapCanvasRegistry paintingData) {
	
		Server server = Bukkit.getServer();
		MapView newMapView = null;
		CanvasManager CM = plugin.m_canvasManager;
		  
		// The pool is a collection of maps that have been previously used for paintings
		// But now the slots are available again for reuse (ie painting deleted / purged)
		  
		// if pool is empty
		if (CM.pool.isEmpty()) {
			  
			// Just grab a new map
			// TODO - make this customizable for what world is "linked" to paintings
			// Get world index 0.... not ideal.
			World world0 = server.getWorlds().get(0);
			newMapView = server.createMap(world0);
			  
	    } else {
	    	  
	    	// Reuse a slot from the map pool.
	    	int i = CM.pool.pollFirst();
	    	newMapView = server.getMap(i);
	    
	    }
		
		// TODO - Error response - should not be reached.
		// If we resolve here then the map was unable to be created
		if (newMapView == null) {
			return;
		}
	
		// If map count wraps into negatives, exit because map limit has been exceeded.
		// TODO - Error response
		if (newMapView.getId() < 0) {
			return;
		}
	    
		// Update the data tracking the painting details to point to the desired map
		paintingData.binding = (int) newMapView.getId();
		paintingData.view = newMapView;

		// Make the canvas manager track the new painting
	    plugin.m_canvasManager.add(paintingData);
	    
	    // Actually give the player the new canvas
	    if (sender instanceof Player) {
	    	Player player = (Player)sender;
	    	plugin.m_canvasManager.give(player, paintingData, 1);
	    }
	    
	    // TODO - Feedback Message
	    sender.sendMessage(plugin.m_commandCreateHandler.bound.replace("$canvasName", paintingData.name));
	    
	    // Log that the player created the painting
	    plugin.ackHistory(paintingData, sender);
		  
	  }
}

