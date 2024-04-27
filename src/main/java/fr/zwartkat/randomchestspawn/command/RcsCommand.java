package fr.zwartkat.randomchestspawn.command;

import fr.zwartkat.randomchestspawn.block.SpawnedChest;
import fr.zwartkat.randomchestspawn.Configuration;
import fr.zwartkat.randomchestspawn.RandomChestSpawn;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

import static org.bukkit.Bukkit.getLogger;

public class RcsCommand implements CommandExecutor, TabExecutor {

    private final JavaPlugin plugin;
    private final Configuration config = RandomChestSpawn.config;
    private boolean eventStarted = false;
    private int eventTimer;
    private String prefix,argumentsError,reloadMessage,configurationError,eventFinishedMessage;
    private Player sender;
    private BukkitTask eventRunnable;
    private ArrayList<SpawnedChest> chests;
    private RcsCommand  command;

    public RcsCommand(JavaPlugin plugin){
        this.plugin = plugin;
        this.command = this;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        prefix = config.getString("prefix");
        argumentsError = config.getString("invalid_arguments");
        reloadMessage = config.getString("reload");
        configurationError = config.getString("configuration_error");
        eventFinishedMessage = config.getString("event_finished");

        if(sender instanceof Player){
            this.sender = (Player) sender;
        }

        if(args.length < 1){
            this.sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + argumentsError));
        }
        else if(args[0].equalsIgnoreCase("reload")){
            config.loadConfiguration();
            this.deleteChests();
            this.sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + reloadMessage));
            return true;
        }
        else if(args[0].equalsIgnoreCase("event")){
            if(args[1].equalsIgnoreCase("start")){
                this.spawnChest();
            }
            else if(args[1].equalsIgnoreCase("stop")){
                this.despawnChest();
            }
            else if(args[1].equalsIgnoreCase("chestremove")){
                if(sender instanceof Player){
                    Block targetBlock = this.sender.getTargetBlockExact(5);
                    if(this.blockIsSpawnedChest(targetBlock)){
                        this.deleteChest(this.getSpawnedChest(targetBlock));
                    }
                }
                else{
                    getLogger().info("This command must be execute by a player");
                }
            }
        }
        else if(args[0].equalsIgnoreCase("chest")){
            if(args[1].equalsIgnoreCase("create")){
                if(args.length > 2){
                    this.chestCreate(args[2]);
                }
                else{
                    this.sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + argumentsError));
                }
            }
        }
        return false;
    }

    private void spawnChest(){

        Integer chestNumber = config.getInteger("number");
        ConfigurationSection chestsList = config.getConfigurationSection("chests");
        ConfigurationSection lootsList = config.getConfigurationSection("loots");

        if(this.eventStarted){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + config.getString("event_still_started")));
            return;
        }

        ArrayList<String> chestsNameList = new ArrayList<String>();
        Random random = new Random();
        this.chests = new ArrayList<SpawnedChest>();
        this.eventStarted = true;

        chestsNameList.addAll(chestsList.getKeys(false));

        for (int i = 0; i < chestNumber; i++) {
            for (int j = chestsNameList.size(); j > 0; j--) {
                int randomChest = random.nextInt(chestsNameList.size());
                String chestName = chestsNameList.get(randomChest);
                Block block = Bukkit.getWorld(chestsList.getString(chestName + ".world")).getBlockAt(chestsList.getInt(chestName + ".x"), chestsList.getInt(chestName + ".y"), chestsList.getInt(chestName + ".z"));
                chestsNameList.remove(chestName);
                if (block.getType().isAir()) {
                    this.chests.add(new SpawnedChest(chestName, lootsList));
                    break;
                }
            }
        }

        if(this.chests.isEmpty()){
            this.eventStarted = false;
        }
        else {
            eventTimer = config.getInteger("duration");

            eventRunnable = new BukkitRunnable(){
                @Override
                public void run(){
                    if((eventTimer%30 == 0 && eventTimer>=30) || (eventTimer <= 10 && eventTimer > 0)){
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',prefix + config.getString("event_message").replace("%time%",""+eventTimer/3600+"h"+(eventTimer%3600)/60+"m"+eventTimer%60+"s")));
                    }
                    else if(eventTimer == 0){
                        command.despawnChest();
                    }
                    eventTimer--;
                }
            }.runTaskTimer(plugin,0,20);
        }



    }

    public void despawnChest(){
        if(this.eventStarted){
            if(!this.chests.isEmpty()){
                this.deleteChests();
            }
            else {
                this.sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + config.getString("no_chest")));
            }
            this.eventStarted = false;
        }
        else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + config.getString("event_not_started")));
        }
    }

    public SpawnedChest getSpawnedChest(Block block){
        for(SpawnedChest chest : this.chests){
            if(chest.equals(block)){
                return chest;
            }
        }
        return null;
    }
    public ArrayList<SpawnedChest> getSpawnedChests(){
        return this.chests;
    }

    public void deleteChest(SpawnedChest chest){
        boolean test = this.chests.remove(chest);
        chest.deleteChest();
        if(this.chests.isEmpty()){
            if(!eventRunnable.isCancelled()){
                eventRunnable.cancel();
            }
            this.eventStarted = false;
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',prefix + eventFinishedMessage));
        }
    }
    private void deleteChests(){
        if(!eventRunnable.isCancelled()){
            eventRunnable.cancel();
        }
        if(!this.chests.isEmpty()){
            for(int i = 0; i < chests.size(); i++){
                this.chests.get(i).deleteChest();
            }
        }
        this.eventStarted = false;
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',prefix + eventFinishedMessage));


    }
    public boolean blockIsSpawnedChest(Block block){
        if(this.chests != null && !this.chests.isEmpty()){
            for(SpawnedChest chest : this.chests){
                if(chest.equals(block)){
                    return true;
                }
            }
        }
        return false;
    }

    private void chestCreate(String name){
        Location playerLocation = sender.getLocation();
        ConfigurationSection chestsConfiguration = config.getConfigurationSection("chests");

        if(chestsConfiguration == null){
            chestsConfiguration = config.createSection("chests");
        }

        ConfigurationSection newChest = chestsConfiguration.createSection(name);
        newChest.set("world",playerLocation.getWorld().getName());
        newChest.set("x",Math.round(playerLocation.getX()));
        newChest.set("y",Math.round(playerLocation.getY()));
        newChest.set("z",Math.round(playerLocation.getZ()));
        config.set("chests",chestsConfiguration);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1){
            return Arrays.asList("reload","event","chest","loot");
        }
        else if(args.length == 2){
            if(args[0].equalsIgnoreCase("event")){
                return Arrays.asList("start","stop","chestremove");
            }
            else if(args[0].equalsIgnoreCase("chest")){
                return Arrays.asList("create","remove");
            }
        }
        return null;
    }


}
