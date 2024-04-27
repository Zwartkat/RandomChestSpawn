package fr.zwartkat.randomchestspawn.command.subcommand;

import fr.zwartkat.randomchestspawn.command.SubCommand;
import org.bukkit.entity.Player;

public class EventChestCommand implements SubCommand {
    @Override
    public String getName() {
        return "event";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public void perform(Player player, String[] args) {

    }
}
