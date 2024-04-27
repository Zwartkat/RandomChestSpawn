package fr.zwartkat.randomchestspawn.command.subcommand;

import fr.zwartkat.randomchestspawn.command.SubCommand;
import org.bukkit.entity.Player;

public class ReloadCommand implements SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload the configuration";
    }

    @Override
    public String getSyntax() {
        return "/rcs reload";
    }

    @Override
    public void perform(Player player, String[] args) {

    }
}
