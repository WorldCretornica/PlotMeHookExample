package com.worldcretornica.plotmehookexample;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;

public class PlotMeHookExample extends JavaPlugin {

    private PlotMeCoreManager plotAPI;

    @Override
    public void onDisable() {
        plotAPI = null;
    }

    @Override
    public void onEnable() {
        // put code that detects if plotme plugin is present
        PluginManager pm = Bukkit.getPluginManager();
        if (pm.isPluginEnabled("PlotMe")) {
            plotAPI = PlotMeCoreManager.getInstance();
            getLogger().info("Hooked to PlotMe!");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("plotmehook")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;

                BukkitPlayer player = new BukkitPlayer(p);
                PlotMapInfo pmi = plotAPI.getMap(player);

                if (pmi == null) {
                    p.sendMessage("This isn't a plot world");
                } else {
                    p.sendMessage("There are " + pmi.getNbPlots() + " plots in this world");

                    // The plotmanager class contains static methods. Note that
                    // this is not the case for PlotMe-Core
                    String id = plotAPI.getPlotId(player);

                    if (id.equals("")) {
                        p.sendMessage("You are not standing in a plot.");
                    } else {
                        Plot plot = plotAPI.getPlotById(player); // this function supports many arguments;
                                                                 // BukkitWorld, BukkitLocation, Id, BukkitEntity, etc..

                        if (plot != null) {
                            p.sendMessage("You are standing in plot " + plot.getId() + ", owned by " + plot.getOwner());

                            Location bottom = ((BukkitLocation) plotAPI.getPlotBottomLoc(player.getWorld(), plot.getId())).getLocation();
                            Location top = ((BukkitLocation) plotAPI.getPlotTopLoc(player.getWorld(), plot.getId())).getLocation();

                            p.sendMessage("The plot coords are " + bottom.toString() + " to " + top.toString());

                            Location home = ((BukkitLocation) plotAPI.getPlotHome(player.getWorld(), plot.getId())).getLocation();

                            p.sendMessage("The plot home is located at " + home.toString());
                        } else {
                            p.sendMessage("You are standing in an unclaimed plot.");
                        }
                    }
                }

                // I haven't tested the code below here yet but it should be like this
                p.sendMessage("You have " + plotAPI.getNbOwnedPlot(player.getUniqueId(), player.getWorld().getName()) + " plots in this world.");
            }
        }
        return false;
    }
}
