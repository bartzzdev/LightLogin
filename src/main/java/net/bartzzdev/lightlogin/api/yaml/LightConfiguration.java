package net.bartzzdev.lightlogin.api.yaml;

import net.bartzzdev.lightlogin.LightLogin;
import net.bartzzdev.lightlogin.enums.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class LightConfiguration {

    private final LightLogin lightLogin = LightLogin.getInstance();
    private FileConfiguration file = this.lightLogin.getConfig();
    private Map<Configuration, Object> fieldsMap = new HashMap<>();

    public void load() {
        for (String string : file.getKeys(false)) {
            this.fieldsMap.put(Configuration.valueOf(string.replace('-', '$')), file.get(string));
        }
    }

    public FileConfiguration getFile() {
        return this.file;
    }

    public Map<Configuration, Object> getFieldsMap() {
        return this.fieldsMap;
    }

    public boolean contains(Configuration configuration) {
        return this.fieldsMap.containsKey(configuration);
    }

    public void put(Configuration configuration, Object value) {
        this.fieldsMap.put(configuration, value);
    }
}
