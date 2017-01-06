package net.bartzz.lightlogin.impl.files;

import net.bartzz.lightlogin.LightLogin;
import net.bartzz.lightlogin.api.data.DataInitializer;
import net.bartzz.lightlogin.api.files.LightMessages;
import net.bartzz.lightlogin.api.files.enums.Messages;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LightMessagesImpl implements LightMessages, DataInitializer<LightMessages>
{
    private LightLogin lightLogin = LightLogin.getInstance();
    private File dataFolder = this.lightLogin.getDataFolder();
    private YamlConfiguration yaml;

    private final Map<Messages, String> valuesMap = new HashMap<>();

    @Override
    public void load()
    {
        if (this.yaml == null)
        {
            return;
        }

        for (String key : this.yaml.getKeys(false))
        {
            String value = this.yaml.getString(key);
            if (this.valuesMap.containsKey(value))
            {
                continue;
            }

            value = StringUtils.replace(value, "<%n>", "\n");
            this.valuesMap.put(Messages.valueOf(key.replace('.', '_').toUpperCase()), ChatColor.translateAlternateColorCodes('&', value));
        }
    }

    @Override
    public String get(Messages key)
    {
        return this.valuesMap.get(key);
    }

    @Override
    public LightMessages register()
    {
        File file = new File(this.dataFolder, "messages.yml");
        if (!file.exists())
        {
            this.lightLogin.saveResource("messages.yml", true);
        }

        this.yaml = YamlConfiguration.loadConfiguration(file);
        return this;
    }
}
