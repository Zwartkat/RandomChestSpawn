package fr.zwartkat.randomchestspawn.command;

import org.bukkit.entity.Player;

public interface SubCommand {

    public String getName();

    public String getDescription();

    public String getSyntax();

    public void perform(Player player, String[] args);
}
