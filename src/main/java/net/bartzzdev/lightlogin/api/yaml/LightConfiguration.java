package net.bartzzdev.lightlogin.api.yaml;

import net.bartzzdev.lightlogin.LightLogin;
import net.bartzzdev.lightlogin.enums.Configuration;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class LightConfiguration {

    private final LightLogin lightLogin = LightLogin.getInstance();
    private FileConfiguration file = this.lightLogin.getConfig();
    private Map<Configuration, Object> fieldsMap = new HashMap<>();

    public void load() {
        for (String key : file.getKeys(false)) {
            this.fieldsMap.put(Configuration.valueOf(StringUtils.replace(key, ".", "_")), file.get(key));
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
}
