package net.aegistudio.mpp;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class ConfirmCommand extends ActualHandle {
	{	 description = "Confirm before using a hazardous command."; 		}
	public HashMap<CommandSender, Object> status = new HashMap<CommandSender, Object>();
	public HashMap<CommandSender, HazardCommand> command = new HashMap<CommandSender, HazardCommand>();
	
	public static final String NOTHING_TO_CONFIRM = "nothingToConfirm";
	public String nothingToConfirm = ChatColor.RED + "You have nothing to confirm! Only use when you're executing a hazardous command.";
	
	public static final String PLEASE_CONFIRM = "pleaseConfirm";
	public String pleaseConfirm = "You are executing a hazardous command! Please issue " + ChatColor.YELLOW + 
			"/mpp confirm" + ChatColor.RESET + " if you want to continue.";
	
	@Override
	public boolean handle(MapPainting painting, String prefix, CommandSender sender, String[] arguments) {
		HazardCommand command = this.command.remove(sender);
		Object status = this.status.remove(sender);
		
		if(command == null) sender.sendMessage(nothingToConfirm);
		else command.handle(painting, sender, status);
		return true;
	}
	
	public void remove(CommandSender sender) {
		this.status.remove(sender);
		this.command.remove(sender);
	}
	
	public void hazard(CommandSender sender, HazardCommand command, Object status) {
		sender.sendMessage(pleaseConfirm);
		this.status.put(sender, status);
		this.command.put(sender, command);
	}
	
	public void load(MapPainting painting, ConfigurationSection section) throws Exception {
		super.load(painting, section);
		nothingToConfirm = painting.getLocale(NOTHING_TO_CONFIRM, nothingToConfirm, section);
		pleaseConfirm = painting.getLocale(PLEASE_CONFIRM, pleaseConfirm, section);
	}
}
