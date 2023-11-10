
package net.aegistudio.mpp.factory;

import net.aegistudio.mpp.MapPainting;
import net.aegistudio.mpp.canvas.Canvas;
import net.aegistudio.mpp.canvas.WrapCanvas;
import org.bukkit.command.CommandSender;

public class WrapCreateCommand extends BaseCreateCommand {
    public WrapCreateCommand() {
        this.description = "@create.wrap.description";
        this.paramList = "";
    }

    @Override
    protected Canvas create(MapPainting painting, CommandSender sender, String[] arguments) {
        WrapCanvas canvas = new WrapCanvas(painting);
        return canvas;
    }

}

