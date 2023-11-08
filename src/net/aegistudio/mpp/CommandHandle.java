
package net.aegistudio.mpp;

import org.bukkit.command.CommandSender;

// handles commands?
public interface CommandHandle
extends Module {
    public String description();

    public boolean handle(MapPainting var1, String var2, CommandSender var3, String[] var4);
}

