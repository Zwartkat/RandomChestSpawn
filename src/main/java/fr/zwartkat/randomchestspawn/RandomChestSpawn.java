package fr.zwartkat.randomchestspawn;

import fr.zwartkat.randomchestspawn.Command.ReloadCommand;
import fr.zwartkat.randomchestspawn.listeners.EntityListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class RandomChestSpawn extends JavaPlugin {

    public static RandomChestSpawn plugin;
    public static Configuration config = new Configuration("config.yml");
    private FileConfiguration fileConfiguration;
    @Override
    public void onEnable() {

        plugin = this;

        this.fileConfiguration = this.getConfig();
        config.loadConfiguration();

        getLogger().info("Starting RandomChestSpawn");
        getServer().getPluginManager().registerEvents(new EntityListener(),this);
        getCommand("rcs").setExecutor(new ReloadCommand(this));

        this.saveDefaultConfig();

    }

    @Override
    public void onDisable() {
        getLogger().info("Stopping RandomChestSpawn");

    }


}
