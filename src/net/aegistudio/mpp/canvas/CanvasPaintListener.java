package net.aegistudio.mpp.canvas;

import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import net.aegistudio.mpp.MapPainting;

public class CanvasPaintListener implements Listener {
	private final MapPainting painting;
	public CanvasPaintListener(MapPainting painting) {
		this.painting = painting;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent interact) {
		if(interact.getClickedBlock() == null) return;
		BlockFace blockFace = interact.getBlockFace();
		if(blockFace == BlockFace.UP || blockFace == BlockFace.DOWN) return;
		
		Collection<Entity> entities = interact.getClickedBlock().getWorld()
			.getNearbyEntities(interact.getClickedBlock()
					.getLocation().add(0.5, 0.5, 0.5)
						.add(blockFace.getModX(), 0, blockFace.getModZ()), 0.5, 0.5, 0.5);
		
		for(Entity entity : entities) 
				if(entity instanceof ItemFrame){
			ItemFrame itemFrame = (ItemFrame) entity;
			if(itemFrame.getItem().getType() != Material.MAP) return;
			short mapId = itemFrame.getItem().getDurability();
			MapCanvasRegistry registry = painting.canvas.idCanvasMap.get(mapId);
			if(registry == null) break;
			
			if(manipulate(itemFrame.getLocation(), registry, interact.getPlayer())) 
				interact.setCancelled(true);
			break;
		}
	}

	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent interact) {
		if(interact.getRightClicked() instanceof ItemFrame) {
			ItemFrame itemFrame = (ItemFrame) interact.getRightClicked();
			if(itemFrame.getItem().getType() != Material.MAP) return;
			short mapId = itemFrame.getItem().getDurability();
			MapCanvasRegistry registry = painting.canvas.idCanvasMap.get(mapId);
			if(registry == null) return;
			
			if(manipulate(itemFrame.getLocation(), registry, interact.getPlayer()))
				interact.setCancelled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	public boolean manipulate(Location itemFrame, MapCanvasRegistry registry, Player player) {
		// Calculate looking direction.
		Location playerEyePos = player.getLocation().add(0, player.getEyeHeight(), 0);
		
		double yaw = (playerEyePos.getYaw() + 90) * Math.PI / 180;
		double pitch = -playerEyePos.getPitch() * Math.PI / 180;
		
		double a = Math.cos(yaw) * Math.cos(pitch);
		double b = Math.sin(pitch);
		double c = Math.sin(yaw) * Math.cos(pitch);

		// Calculate canvas direction.
		double dir = (itemFrame.getYaw() + 90) * Math.PI / 180;
		double A = Math.round(Math.cos(dir));
		double C = Math.round(Math.sin(dir));
		

		// Calculate bias vector.
		double x0 = playerEyePos.getX() - itemFrame.getX();
		double y0 = playerEyePos.getY() - itemFrame.getY();
		double z0 = playerEyePos.getZ() - itemFrame.getZ();
		
		// Do intersection.
		double v1 = A * x0 + C * z0;
		double v0 = A * a + C * c;
		double miu = (-v1 / v0) * 0.92;
		
		double xLook = x0 + miu * a;
		double yLook = y0 + miu * b;
		double zLook = z0 + miu * c;
		
		// Calculate uv coordination.
		double u, v = 0;
		v = yLook + 0.5;
		
		if(Math.abs(A) > Math.abs(C)) {
			if(A > 0) 
				u = -zLook;
			else u = zLook;
		}
		else {
			if(C > 0)
				u = xLook;
			else u = -xLook;
		}
		
		u += 0.5;
		int x = (int)Math.round(u * registry.canvas.size());
		int y = (int)Math.round(v * registry.canvas.size());
		
		// Paint on canvas.
		if(registry.painter.contains(player.getName()))
			if(painting.tool.paint(player.getItemInHand(), registry, 
					x, y)) return true;
		return registry.canvas.interact(x, y, player);
	}
}
