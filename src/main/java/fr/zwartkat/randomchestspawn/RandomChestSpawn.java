package fr.zwartkat.randomchestspawn;

import fr.zwartkat.randomchestspawn.command.RcsCommand;
import fr.zwartkat.randomchestspawn.placeholderapi.PlaceHolderAPIExtensions;
import fr.zwartkat.randomchestspawn.listeners.EntityListener;
import fr.zwartkat.randomchestspawn.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class RandomChestSpawn extends JavaPlugin implements Listener {

    public static RandomChestSpawn plugin;
    public static Configuration config = new Configuration("config.yml");
    private RcsCommand rcs;
    private FileConfiguration fileConfiguration;
    @Override
    public void onEnable() {

        rcs = new RcsCommand(this);

        plugin = this;
        this.fileConfiguration = this.getConfig();
        config.loadConfiguration();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceHolderAPIExtensions().register();
        }

        getLogger().info("Starting RandomChestSpawn");
        getServer().getPluginManager().registerEvents(new EntityListener(rcs),this);
        getServer().getPluginManager().registerEvents(new PlayerListener(rcs),this);
        getCommand("rcs").setExecutor(rcs);

        this.saveDefaultConfig();

    }

    @Override
    public void onDisable() {
        rcs.despawnChest();
        getLogger().info("Stopping RandomChestSpawn");
    }

}
