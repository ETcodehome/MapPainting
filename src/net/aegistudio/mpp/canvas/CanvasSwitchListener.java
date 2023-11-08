
package net.aegistudio.mpp.canvas;

import net.aegistudio.mpp.MapPainting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class CanvasSwitchListener
implements Listener {
    private final MapPainting painting;

    public CanvasSwitchListener(MapPainting painting) {
        this.painting = painting;
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onSwitchItem(PlayerItemHeldEvent event) {
        if (event.isCancelled()) {
            return;
        }
        ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        int mapId = this.painting.m_canvasManager.scopeListener.parse(item);
        if (mapId < 0) {
            return;
        }
    }
}

