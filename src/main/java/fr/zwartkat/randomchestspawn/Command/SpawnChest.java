package fr.zwartkat.randomchestspawn.Command;

import fr.zwartkat.randomchestspawn.Configuration;
import fr.zwartkat.randomchestspawn.RandomChestSpawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SpawnChest {

    private final FileConfiguration config = RandomChestSpawn.plugin.getConfig();
    private final Integer chestNumber = config.getInt("number");
    private List<Location> chestsLocation;
    private Map<Location,Block> chests;

    public SpawnChest(){
        System.out.println("test");
        Random random = new Random();
        System.out.println(config.getString("chest_already_open"));
        ConfigurationSection chestsList = config.getConfigurationSection("chests");
        System.out.println(chestsList);

        for( String chestKey : chestsList.getKeys(false)){
            ConfigurationSection chestData = chestsList.getConfigurationSection(chestKey);
            Location location = new Location(Bukkit.getWorld(chestData.getString("world")),chestData.getInt("x"),chestData.getInt("y"),chestData.getInt("z"));
            chestsLocation.add(location);
        }
        for (int i = 0; i < chestNumber;i++){
            int randomChest = random.nextInt(chestsLocation.size());
            Location chestLocation = chestsLocation.get(randomChest);
            Block block = Bukkit.getWorld(chestLocation.getWorld().getName()).getBlockAt(chestLocation);
            if(!chests.containsKey(chestLocation)){
                if(block.getType().isAir()){
                    block.setType(Material.CHEST);
                    chests.put(chestLocation,block);
                }
                else {
                    System.out.println("T");
                }
            }


        }
        //new BukkitRunnable() {
        //    @Override
        //    public void run() {
        //        System.out.println("T");
        //    }
        //}.runTaskTimer(RandomChestSpawn.plugin,30*60,1);
    }
}
