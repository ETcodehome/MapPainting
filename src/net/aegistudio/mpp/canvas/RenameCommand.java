
package net.aegistudio.mpp.canvas;

import net.aegistudio.mpp.ActualHandle;
import net.aegistudio.mpp.MapPainting;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class RenameCommand extends ActualHandle {
	public static final String ONLY_PLAYER = "onlyPlayer";
	public String onlyPlayer;
	public static final String CANVAS_NOT_EXISTS = "canvasNotExists";
	public String canvasNotExists;
	public static final String CANVAS_ALREADY_EXIST = "canvasAlreadyExist";
	public String canvasAlreadyExist;
	public static final String NOT_HOLDING = "notHolding";
	public String notHolding;
	public static final String NO_RENAME_PERMISSION = "noRenamePermission";
	public String noRenamePermission;
	public static final String SUCCESSFULLY_RENAME = "successfullyRename";
	public String successfullyRename;

	public RenameCommand() {
		this.description = "@rename.description";
		this.onlyPlayer = "@rename.onlyPlayer";
		this.canvasNotExists = "@rename.canvasNotExists";
		this.canvasAlreadyExist = "@rename.canvasAlreadyExist";
		this.notHolding = "@rename.notHolding";
		this.noRenamePermission = "@rename.noRenamePermission";
		this.successfullyRename = "@rename.successfullyRename";
	}

	@Override
	public boolean handle(MapPainting plugin, String prefix, CommandSender sender, String[] arguments) {
		
		// if no arguments are supplied, advise player the right command
		if (arguments.length == 0) {
			sender.sendMessage(prefix + " [<oldname>] <newname>");
			return true;
		}
		
		MapCanvasRegistry oldcanvas;
		String newname;
			
		// only rename by players
		if (!(sender instanceof Player)) {
			sender.sendMessage(this.onlyPlayer);
			return true;
		}
		Player player = (Player) sender;
		
		// if only one name is supplied, assume renaming the held canvas
		if (arguments.length == 1) {
				
				oldcanvas = plugin.m_canvasManager.holding(player);
				if (oldcanvas == null) {
					sender.sendMessage(this.notHolding);
					return true;
				}
				newname = arguments[0];
			
		}
		
		else {
				
			// handled when both oldname and newname are supplied
				oldcanvas = plugin.m_canvasManager.nameCanvasMap.get(arguments[0]);
				if (null == oldcanvas || oldcanvas.removed()) {
					sender.sendMessage(this.canvasNotExists.replace("$canvasName", arguments[0]));
					return true;
				}
				newname = arguments[1];
		}
		
		// sanitise newname
		newname = newname.replace('.', '_');
		newname = newname.replace('/', '_');
		newname = newname.replace('\\', '_');
		
		// confirm sender has rename permissions	
		if (!oldcanvas.hasPermission(sender, "rename")) {
			sender.sendMessage(this.noRenamePermission);
			return true;
		}
		
		// confirm we arent duplicating a painting name
		if (plugin.m_canvasManager.nameCanvasMap.containsKey(newname)) {
			sender.sendMessage(this.canvasAlreadyExist.replace("$canvasName", newname));
			return true;
		}
		
		String oldname = oldcanvas.name;
		plugin.m_canvasManager.nameCanvasMap.put(newname, oldcanvas);
		oldcanvas.name = newname;
		plugin.m_canvasManager.nameCanvasMap.remove(oldname);
		
		sender.sendMessage(this.successfullyRename.replace("$oldname", oldname).replace("$newname", newname));
		
		// actually replace the item in hand with renamed item
		// TODO - does this handle stacks properly? inv space issues?
		if (sender instanceof Player) {
			plugin.m_canvasManager.scopeListener.make(player.getInventory().getItemInMainHand(), oldcanvas);
		}
		
		return true;
	}

	@Override
	public void load(MapPainting painting, ConfigurationSection section) throws Exception {
		super.load(painting, section);
		this.onlyPlayer = painting.getLocale(ONLY_PLAYER, this.onlyPlayer, section);
		this.canvasNotExists = painting.getLocale(CANVAS_NOT_EXISTS, this.canvasNotExists, section);
		this.canvasAlreadyExist = painting.getLocale(CANVAS_ALREADY_EXIST, this.canvasAlreadyExist, section);
		this.notHolding = painting.getLocale(NOT_HOLDING, this.notHolding, section);
		this.noRenamePermission = painting.getLocale(NO_RENAME_PERMISSION, this.noRenamePermission, section);
		this.successfullyRename = painting.getLocale(SUCCESSFULLY_RENAME, this.successfullyRename, section);
	}
}
