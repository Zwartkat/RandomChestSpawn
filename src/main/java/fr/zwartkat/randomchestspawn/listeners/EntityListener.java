package fr.zwartkat.randomchestspawn.listeners;

import fr.zwartkat.randomchestspawn.RandomChestSpawn;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class EntityListener implements Listener {

    private final RandomChestSpawn plugin = RandomChestSpawn.plugin;

    @EventHandler
    public void onEntityRightCLick(PlayerInteractEvent event){

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if(block != null && block.getType() == Material.CHEST && event.getAction().toString().contains("RIGHT")){
            Chest chest = (Chest) block.getState();
            if(chest.getBlockInventory().getViewers().size() > 0){
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',"" + plugin.getConfig().getString("chest_already_open")));
            }
            System.out.println(chest.getBlockInventory().getViewers().size() );
        }
    }
}
