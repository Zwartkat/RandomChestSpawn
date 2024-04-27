package fr.zwartkat.randomchestspawn.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;

public class PlaceHolderAPIExtensions extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "rcs";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Zwartkat";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }
}
