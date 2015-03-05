package com.worldcretornica.plotmehookexample;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PlotMeHookExample extends JavaPlugin {

    private PlotMeCoreManager plotAPI;
    private PlotMe_CorePlugin plotmeBukkitHook;

    @Override
    public void onDisable() {
        plotAPI = null;
    }

    @Override
    public void onEnable() {
        // put code that detects if plotme plugin is present
        PluginManager pm = Bukkit.getPluginManager();
        if (pm.isPluginEnabled("PlotMe")) {
            plotmeBukkitHook = (PlotMe_CorePlugin) pm.getPlugin("PlotMe");
            plotAPI = PlotMeCoreManager.getInstance();
            getLogger().info("Hooked to PlotMe!");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("plotmehook")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                //You can get an IPlayer and then cast it to a BukkitPlayer like this:
                BukkitPlayer player = (BukkitPlayer) plotmeBukkitHook.wrapPlayer(p);
                //BukkitPlayer can also be downcasted like this:
                //IPlayer player2 = (IPlayer) player;
                // You cannot cast a player found in the bukkit libraries to an IPlayer or BukkitPlayer
                // The same goes for all the other PlotMe "I" classes.
                PlotMapInfo pmi = plotAPI.getMap(player);

                if (pmi == null) {
                    p.sendMessage("This isn't a plot world");
                } else {
                    p.sendMessage("There are " + pmi.getNbPlots() + " plots in this world");

                    // PlotMe-Core does not contain any static methods
                    PlotId id = plotAPI.getPlotId(player);

                    if (id == null) {
                        p.sendMessage("You are not standing in a plot.");
                    } else {
                        Plot plot = plotAPI.getPlotById(id, player); // this function supports many arguments;

                        if (plot != null) {
                            p.sendMessage("You are standing in plot " + plot.getId() + ", owned by " + plot.getOwner());

                            Location bottom = ((BukkitLocation) plotAPI.getPlotBottomLoc(player.getWorld(), plot.getId())).getLocation();
                            Location top = ((BukkitLocation) plotAPI.getPlotTopLoc(player.getWorld(), plot.getId())).getLocation();

                            p.sendMessage("The plot coords are " + bottom + " to " + top);

                            Location home = ((BukkitLocation) plotAPI.getPlotHome(player.getWorld(), plot.getId())).getLocation();

                            p.sendMessage("The plot home is located at " + home);
                            
                            // Each plugin can set his own properties
                            // The properties will be deleted if the plot is reset
                            // The properties will be moved if the plot is moved
                            
                            // Set property
                            // with plot.setPlotProperty() or PlotMeCoreManager.getInstance().setPlotProperty()  
                            String propertyname = "What happens if you eat all the potatoes";
                            plot.setPlotProperty(this.getName(), propertyname, "they're all gone");

                            // Get the value of the property later
                            // with plot.getPlotProperty() or PlotMeCoreManager.getInstance().getPlotProperty()
                            String value = plot.getPlotProperty(this.getName(), propertyname);
                            p.sendMessage("The property of what happens if you eat all the potatoes is : " + value);
                        } else {
                            p.sendMessage("You are standing in an unclaimed plot.");
                        }
                    }
                }

                // I haven't tested the code below here yet but it should be like this
                p.sendMessage("You have " + plotAPI.getOwnedPlotCount(player.getUniqueId(), player.getWorld().getName()) + " plots in this world.");
            }
        }
        return false;
    }
}
