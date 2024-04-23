package fr.zwartkat.randomchestspawn.Command;

import fr.zwartkat.randomchestspawn.Configuration;
import fr.zwartkat.randomchestspawn.RandomChestSpawn;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class ReloadCommand implements CommandExecutor, TabExecutor {

    private final JavaPlugin plugin;
    private Configuration config = RandomChestSpawn.config;
    private String prefix;
    private String argumentsError;
    private String reloadMessage;
    private SpawnChest spawn;

    public ReloadCommand(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        prefix = config.getString("prefix");
        argumentsError = config.getString("invalid_arguments");
        reloadMessage = config.getString("reload");

        if(sender instanceof Player){
            Player player = (Player) sender;
        }

        if(args.length < 1){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + argumentsError));
        }
        else if(args[0].equalsIgnoreCase("reload")){
            config.loadConfiguration();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + config.getString("reload")));
            return true;
        }
        else if(args[0].equalsIgnoreCase("event")){
            if(args[1].equalsIgnoreCase("start")){
                sender.sendMessage("Ok");
                spawn = new SpawnChest();

            }
            else if(args[1].equalsIgnoreCase("stop")){

            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1){
            return Arrays.asList("reload","event");
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("event")){
            return Arrays.asList("start","stop");
        }
        return null;
    }


}
