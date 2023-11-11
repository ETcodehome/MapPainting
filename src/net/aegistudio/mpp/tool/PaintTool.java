
package net.aegistudio.mpp.tool;

import net.aegistudio.mpp.Interaction;
import net.aegistudio.mpp.Module;
import net.aegistudio.mpp.canvas.MapCanvasRegistry;
import org.bukkit.inventory.ItemStack;

public interface PaintTool
extends Module {
    public boolean paint(ItemStack var1, MapCanvasRegistry var2, Interaction var3);
}

