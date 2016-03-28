package net.aegistudio.mpp.tool;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.aegistudio.mpp.Interaction;
import net.aegistudio.mpp.canvas.MapCanvasRegistry;

public class Razor extends Pencil {
	{
		tapMessage = "Razored pixel [$x, $y].";
		lineMessage = "Razored a line from [$x1, $y1] to [$x2, $y2].";
	}
	
	@Override
	public boolean paint(ItemStack itemStack, MapCanvasRegistry canvas, Interaction interact) {
		if(itemStack.getType() == Material.SHEARS) {
			super.pencilPaint(interact, canvas, null);
			return true;
		}
		return false;
	}
}
