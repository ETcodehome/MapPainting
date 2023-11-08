
package net.aegistudio.mpp.export;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;

public interface PlaceSensitive {
    public void place(Location var1, BlockFace var2);

    public void unplace(Item var1);
}

