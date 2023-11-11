
package net.aegistudio.mpp.create;

import java.awt.Color;
import net.aegistudio.mpp.MapPainting;
import net.aegistudio.mpp.canvas.BufferedCanvas;
import net.aegistudio.mpp.canvas.Canvas;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class NormalCreateCommand extends BaseCreateCommand {
	
    public static final String INVALID_FORMAT = "invalidFormat";
    public String invalidFormat;
    public static final String OUT_OF_RANGE = "outOfRange";
    public String outOfRange;
    public static final String NEED_INV = "needInv";
    public String needInv;
    

    public NormalCreateCommand() {
        this.description = "@create.normal.description";
        this.paramList = "[<1~128>] [<background>]";
        this.invalidFormat = "@create.normal.invalidFormat";
        this.outOfRange = "@create.normal.outOfRange";
        this.needInv = "@create.normal.needInv";
    }

    @Override
    protected Canvas create(MapPainting plugin, CommandSender sender, String[] arguments) {
    	
    	// check sender has permission to create new paintings
    	if (!sender.hasPermission("mpp.create.normal")) {
            sender.sendMessage("No permission");
            return null;
        }
    	
    	// check sender is a player
    	if (!(sender instanceof Player)) {
    		sender.sendMessage("not a player");
    		return null;
    	}
    	
    	// check if size argument is supplied
        int size = 32;
        if (arguments.length > 0) {
            try {
                size = Integer.parseInt(arguments[0]);
            }
            catch (Throwable e) {
                sender.sendMessage(this.invalidFormat);
                return null;
            }
        }
        
        // confirm size argument is valid if supplied
        if (size < 1 || size > 128) {
            sender.sendMessage(this.outOfRange);
            return null;
        }
        
        // confirm color argument is valid if supplied
        Color color = Color.WHITE;
        if (arguments.length > 1) {
            try {
                color = plugin.m_colorManager.parseColor((String)arguments[1]).color;
            }
            catch (Throwable e) {
                sender.sendMessage(this.invalidFormat);
                return null;
            }
        }
        
        // actually return the canvas to the player        
        byte canvasColor = (byte)plugin.m_canvasManager.color.getIndex(color);
        BufferedCanvas canvas = new BufferedCanvas(plugin);
        canvas.size = size;
        canvas.pixel = new byte[canvas.size][canvas.size];
        for (int i = 0; i < canvas.size; ++i) {
            for (int j = 0; j < canvas.size; ++j) {
                canvas.pixel[i][j] = canvasColor;
            }
        }
        return canvas;
    }

    @Override
    public void load(MapPainting plugin, ConfigurationSection section) throws Exception {
        super.load(plugin, section);
        this.invalidFormat = plugin.getLocale(INVALID_FORMAT, this.invalidFormat, section);
        this.outOfRange = plugin.getLocale(OUT_OF_RANGE, this.outOfRange, section);
        this.needInv = plugin.getLocale(NEED_INV, this.needInv, section);
    }

}

