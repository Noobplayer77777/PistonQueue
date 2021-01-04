package me.alexprogrammerde.xerabungeequeueplaceholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PAPIExpansion extends PlaceholderExpansion {
    XeraBungeeQueuePlaceholder plugin;

    public PAPIExpansion(XeraBungeeQueuePlaceholder plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public @NotNull String getAuthor(){
        return "AlexProgrammerDE";
    }

    @Override
    public @NotNull String getIdentifier(){
        return "xerabungeequeue";
    }

    @Override
    public @NotNull String getVersion(){
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier){
        if (identifier.equals("regular")){
            return String.valueOf(plugin.regular);
        }

        if (identifier.equals("priority")){
            return String.valueOf(plugin.priority);


        }if (identifier.equals("veteran")){
            return String.valueOf(plugin.veteran);
        }

        return null;
    }
}
