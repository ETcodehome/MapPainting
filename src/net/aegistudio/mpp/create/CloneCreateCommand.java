
package net.aegistudio.mpp.create;

import net.aegistudio.mpp.MapPainting;
import net.aegistudio.mpp.canvas.Canvas;
import net.aegistudio.mpp.canvas.MapCanvasRegistry;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class CloneCreateCommand extends BaseCreateCommand {
	
    public static final String CLONED_UNSPECIFIED = "clonedUnspecified";
    public String clonedUnspecified;
    public static final String CANVAS_NOT_EXISTS = "canvasNotExists";
    public String canvasNotExists;
    

    public CloneCreateCommand() {
        this.description = "@create.clone.description";
        this.paramList = "<cloned>";
        this.clonedUnspecified = "@create.clone.clonedUnspecified";
        this.canvasNotExists = "@create.clone.canvasNotExists";
    }

    @Override
    protected Canvas create(MapPainting plugin, CommandSender sender, String[] arguments) {
        
    	// check sender has sufficient perms
    	if (plugin.utils.permissionCheckFails("mpp.create.clone", sender)) {
            return null;
        }
    	
    	// check sender is a player
    	if (plugin.utils.senderIsNonPlayer(sender)) {
    		return null;
    	}
    	
    	if (arguments.length == 0) {
            sender.sendMessage(this.clonedUnspecified);
            return null;
        }
    	
    	String name = arguments[0];
        MapCanvasRegistry mapData = plugin.getCanvas(name, sender);
        
        if (mapData == null) {
            sender.sendMessage(this.canvasNotExists.replace("$canvasName", name));
            return null;
        }
        
     // check player has sufficient rights to copy the canvas
        if (!mapData.hasPermission(sender, "give")) {
            //sender.sendMessage(this.noPermission.replace("$canvasName", name));
        	sender.sendMessage("You have insufficient rights to copy canvas {canvasName}");
            return null;
        }
        
        return mapData.canvas.clone();
    }

    @Override
    public void load(MapPainting painting, ConfigurationSection section) throws Exception {
        super.load(painting, section);
        this.clonedUnspecified = painting.getLocale(CLONED_UNSPECIFIED, this.clonedUnspecified, section);
        this.canvasNotExists = painting.getLocale(CANVAS_NOT_EXISTS, this.canvasNotExists, section);
    }

}

