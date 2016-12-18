package net.bartzzdev.lightlogin.api.yaml;

import net.bartzzdev.lightlogin.LightLogin;
import net.bartzzdev.lightlogin.enums.Messages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LightMessages {

    private LightLogin lightLogin = LightLogin.getInstance();
    private File messagesFile = new File(this.lightLogin.getDataFolder(), "messages.yml");
    private YamlConfiguration yaml = YamlConfiguration.loadConfiguration(this.messagesFile);
    private Map<Messages, String> stringMap = new HashMap<>();

    public void load() {
        if (!this.messagesFile.exists()) {
            this.lightLogin.saveResource("messages.yml", true);
        }

        for (String key : this.yaml.getKeys(false)) {
            String string = this.yaml.getString(key);
            if (string.contains("{$s}")) string = StringUtils.replace(string, "{$s}", "\n");
            this.stringMap.put(Messages.valueOf(StringUtils.replace(key, ".", "_")), string);
        }
    }

    public YamlConfiguration getFile() {
        return this.yaml;
    }

    public Map<Messages, String> getStringMap() {
        return this.stringMap;
    }

    public boolean contains(Messages message) {
        return this.stringMap.containsKey(message);
    }
}
