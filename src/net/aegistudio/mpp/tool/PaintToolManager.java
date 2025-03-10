
package net.aegistudio.mpp.tool;

import java.util.Map;
import java.util.TreeMap;

import net.aegistudio.mpp.Interaction;
import net.aegistudio.mpp.MapPainting;
import net.aegistudio.mpp.canvas.MapCanvasRegistry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class PaintToolManager
implements PaintTool {
    public final TreeMap<String, PaintTool> toolMap = new TreeMap<String, PaintTool>();

    @Override
    public void load(MapPainting painting, ConfigurationSection section) throws Exception {
        for (Map.Entry<String, PaintTool> toolEntry : this.toolMap.entrySet()) {
            if (!section.contains(toolEntry.getKey())) {
                section.createSection(toolEntry.getKey());
            }
            ConfigurationSection toolConfig = section.getConfigurationSection(toolEntry.getKey());
            toolEntry.getValue().load(painting, toolConfig);
        }
    }

    @Override
    public void save(MapPainting painting, ConfigurationSection section) throws Exception {
        for (Map.Entry<String, PaintTool> toolEntry : this.toolMap.entrySet()) {
            if (!section.contains(toolEntry.getKey())) {
                section.createSection(toolEntry.getKey());
            }
            ConfigurationSection toolConfig = section.getConfigurationSection(toolEntry.getKey());
            toolEntry.getValue().save(painting, toolConfig);
        }
    }

    @Override
    public boolean paint(ItemStack itemStack, MapCanvasRegistry canvas, Interaction interact) {
        for (PaintTool tool : this.toolMap.values()) {
            if (!tool.paint(itemStack, canvas, interact)) continue;
            return true;
        }
        return false;
    }
}

