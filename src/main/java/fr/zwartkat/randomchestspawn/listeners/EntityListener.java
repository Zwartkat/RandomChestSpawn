package fr.zwartkat.randomchestspawn.listeners;

import fr.zwartkat.randomchestspawn.block.SpawnedChest;
import fr.zwartkat.randomchestspawn.command.RcsCommand;
import fr.zwartkat.randomchestspawn.RandomChestSpawn;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class EntityListener implements Listener {

    private final RandomChestSpawn plugin = RandomChestSpawn.plugin;
    private RcsCommand rcsCommand;
    private Player player;
    private Block clickedBlock;
    private ArrayList<SpawnedChest> chests;

    public EntityListener(RcsCommand rcs){
        this.rcsCommand = rcs;
    }

    @EventHandler
    public void onEntityRightCLick(PlayerInteractEvent event){

        player = event.getPlayer();
        clickedBlock = event.getClickedBlock();
        chests = new ArrayList<>(rcsCommand.getSpawnedChests());

        if(clickedBlock != null && !chests.isEmpty() && event.getAction().toString().contains("RIGHT") && rcsCommand.blockIsSpawnedChest(clickedBlock)){
            Chest chest = (Chest) clickedBlock.getState();
            if(chest.getBlockInventory().getViewers().size() > 0){
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',"" + plugin.getConfig().getString("chest_already_open")));
            }
        }
    }
}
