package fr.zwartkat.randomchestspawn;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class Configuration {

    private String filePath;
    private File file;
    private YamlConfiguration config;

    public Configuration(String filePath){
        this.filePath = filePath;
    }

    public void loadConfiguration(){
        this.file = new File(RandomChestSpawn.plugin.getDataFolder(),filePath);

        if(!file.exists()){
            RandomChestSpawn.plugin.saveResource(filePath,false);
        }

        config = new YamlConfiguration();
        config.options().parseComments(true);

        try {
            config.load(file);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void save(){
        try{
            config.save(file);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getString(String key){
        return config.getString(key);
    }

    public Integer getInteger(String key){
        return config.getInt(key);
    }

    public List getList(String key){
        return config.getList(key);
    }

    public void set(String key, Object value){
        config.set(key, value);
        save();
    }

}
