package fr.zwartkat.randomchestspawn.block;

import fr.zwartkat.randomchestspawn.Configuration;
import fr.zwartkat.randomchestspawn.RandomChestSpawn;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getLogger;

public class SpawnedChest extends BukkitRunnable {

    private final Configuration config = RandomChestSpawn.config;
    private Block blockChest;
    private Chest chest;
    private Location chestLocation,itemLocation;
    private Item displayChestItem;
    private Inventory chestInventory;

    public SpawnedChest(String chestName, ConfigurationSection lootsList){

        // Summon chest
        ConfigurationSection chestConfig = config.getConfigurationSection("chests." + chestName);
        World world = Bukkit.getWorld(chestConfig.getString("world"));
        int x = chestConfig.getInt("x");
        int y = chestConfig.getInt("y");
        int z = chestConfig.getInt("z");
        chestLocation = new Location(world,x,y,z);
        blockChest = Bukkit.getWorld(world.getName()).getBlockAt(chestLocation);
        blockChest.setType(Material.CHEST);
        chest = (Chest) blockChest.getState();

        // Set loots of the chest
        chestInventory = chest.getInventory();

        while(this.getUsedSlots() == 0){
            for (String lootName : lootsList.getKeys(false)){
                Random random = new Random();
                ConfigurationSection lootData = lootsList.getConfigurationSection(lootName);
                Material material = Material.getMaterial(lootData.getString("material").toUpperCase());

                if(material !=null){

                    double percent = lootData.getDouble("percent")%101;
                    double randomPercent = random.nextDouble(100);

                    if(randomPercent <= percent){

                        int randomIndex = random.nextInt(27);
                        String name = lootData.getString("name");
                        List<String> lore = lootData.getStringList("lore");
                        Integer modelData = lootData.getInt("model_data");
                        int quantity = lootData.getInt("quantity");
                        ItemStack item = new ItemStack(material,quantity);
                        ItemMeta itemMeta = item.getItemMeta();

                        if(modelData != null){
                            itemMeta.setCustomModelData(modelData);
                        }

                        if(name != null){
                            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',name));
                        }

                        if(lore != null){
                            List<String> coloredLore = lore.stream()
                                    .map(line -> ChatColor.translateAlternateColorCodes('&',line))
                                    .collect(Collectors.toList());

                            itemMeta.setLore(coloredLore);
                        }

                        item.setItemMeta(itemMeta);
                        chestInventory.addItem(item);
                    }

                }
                else {
                    getLogger().info("The loot " + lootName + " have an unknown material");
                }
            }
        }


        // Above item
        String itemName = config.getString("item_display");
        Material item = Material.getMaterial(config.getString("item_display").toUpperCase());
        if(item != null){
            itemLocation = new Location(chestLocation.getWorld(), chestLocation.getX() + 0.5 , chestLocation.getY() + 2.0, chestLocation.getZ() + 0.5);
            displayChestItem = (Item) blockChest.getLocation().getWorld().spawnEntity(itemLocation, EntityType.DROPPED_ITEM);
            displayChestItem.setItemStack(new ItemStack(item));
            displayChestItem.setPickupDelay(Integer.MAX_VALUE);
            displayChestItem.setGravity(false);
            displayChestItem.setGlowing(true);
            displayChestItem.setInvulnerable(true);
            displayChestItem.setPersistent(true);
            displayChestItem.setVelocity(new Vector(0,0,0));
            this.start();
        }
    }

    public void deleteChest(){
        blockChest.setType(Material.AIR);
        if(displayChestItem != null){
            displayChestItem.remove();
            this.cancel();
        }
    }

    public int getUsedSlots(){
        int usedSlots = 0;
        for (ItemStack slots : this.chestInventory.getContents()){
            if(slots != null){
                usedSlots++;
            }
        }
        return usedSlots;
    }

    public boolean equals(Object object){
        if(object instanceof Block){
            Block otherBlock = (Block) object;

            if(otherBlock.getType() == this.blockChest.getType() && this.chestLocation.equals(otherBlock.getLocation())){
                return true;
            }
            else {
                return false;
            }
        }
        else if(object instanceof SpawnedChest){
            SpawnedChest chest = (SpawnedChest) object;
            if(this.blockChest.equals(chest.blockChest) && this.chestLocation.equals(chest.chestLocation)){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }

    }

    @Override
    public void run() {
        if(displayChestItem != null){
            Location currentLocation = displayChestItem.getLocation();
            displayChestItem.setTicksLived(1);
            if (!currentLocation.equals(itemLocation)) {
                displayChestItem.teleport(itemLocation);
                displayChestItem.setVelocity(new Vector(0,0,0));
            }
        }

    }

    public void start() {
        runTaskTimer(RandomChestSpawn.plugin, 0, 300);
    }
}
