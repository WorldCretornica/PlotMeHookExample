package com.worldcretornica.plotmehookexample;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.worldcretornica.plotme.Plot;
import com.worldcretornica.plotme.PlotManager;

public class PlotMeHookExample extends JavaPlugin {

	@Override
	public void onDisable() 
	{
		
	}

	@Override
	public void onEnable() {

	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {
		if(label.equalsIgnoreCase("plotmehook")) {
		    if(sender instanceof Player) {
		        Player p = (Player) sender;
		        
		        if(PlotManager.getMap(p) == null) {
		            p.sendMessage("This isn't a plot world");
		        } else {
		            p.sendMessage("There are " + PlotManager.getMap(p).plots.size() + " plots in this world");
		            
		            //The plotmanager class contains static methods. Note that this is not the case for PlotMe-Core
		            String id = PlotManager.getPlotId(p);
		            
		            if(id.equals("")) {
		                p.sendMessage("You are not standing in a plot.");
		            } else {	                
    		            Plot plot = PlotManager.getPlotById(p);  //this function supports many arguments; world, location, id, etc..
    		            
    		            if(plot != null) {
    		                p.sendMessage("You are standing in plot " + plot.id + ", owned by " + plot.owner);
    		                
    		                Location bottom = PlotManager.getPlotBottomLoc(p.getWorld(), plot.id);
    		                Location top = PlotManager.getPlotTopLoc(p.getWorld(), plot.id);
    		                
    		                p.sendMessage("The plot coords are " +  bottom.toString() + " to " + top.toString());
    		                
    		                Location home = PlotManager.getPlotHome(p.getWorld(), plot);
    		                
    		                p.sendMessage("The plot home is located at " + home.toString());
    		            } else {
    		                p.sendMessage("You are standing in an unclaimed plot.");
    		            }
		            }
		        }
		    }
		}
		return false;
	}
}
