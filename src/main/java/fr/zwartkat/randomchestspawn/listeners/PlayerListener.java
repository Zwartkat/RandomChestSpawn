package fr.zwartkat.randomchestspawn.listeners;

import fr.zwartkat.randomchestspawn.block.SpawnedChest;
import fr.zwartkat.randomchestspawn.command.RcsCommand;
import fr.zwartkat.randomchestspawn.RandomChestSpawn;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class PlayerListener implements Listener {

    private final RandomChestSpawn plugin = RandomChestSpawn.plugin;
    private RcsCommand rcsCommand;
    private HashMap<Player,Chest> openChest = new HashMap<Player, Chest>();

    public PlayerListener(RcsCommand rcs){
        this.rcsCommand = rcs;
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event){
        if(rcsCommand.blockIsSpawnedChest(event.getBlock())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChestInventoryOpen(InventoryOpenEvent event){
        if(event.getInventory().getHolder() instanceof Chest){
            Chest chest = (Chest) event.getInventory().getHolder();
            if(rcsCommand.blockIsSpawnedChest(chest.getBlock())){
                openChest.put((Player) event.getPlayer(),chest);
            }
        }
    }

    @EventHandler
    public  void onChestInventoryClose(InventoryCloseEvent event){
        if(event.getInventory().getHolder() instanceof Chest){
            Chest chest = (Chest) event.getInventory().getHolder();
            if(rcsCommand.blockIsSpawnedChest(chest.getBlock())){
                openChest.remove((Player) event.getPlayer(),chest);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Inventory clickedInventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();

        if(clickedInventory != null && player != null){

            ItemStack clickedItem = event.getCurrentItem();
            Inventory chestInventory = openChest.get(player).getInventory();

            if(clickedInventory.equals(chestInventory)) {
                SpawnedChest chest = rcsCommand.getSpawnedChest(((Chest) clickedInventory.getHolder()).getBlock());
                event.setCancelled(true);

                if (clickedItem != null) {
                    clickedInventory.remove(clickedItem);
                    player.getInventory().addItem(clickedItem);

                    if (chest.getUsedSlots() == 0) {
                        rcsCommand.deleteChest(chest);
                    }
                }
            }
            else if(clickedInventory.equals(player.getInventory()) && chestInventory.equals(player.getOpenInventory().getTopInventory())){
                event.setCancelled(true);
            }
        }
    }
}
