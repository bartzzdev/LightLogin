package net.bartzz.lightlogin.impl.files;

import net.bartzz.lightlogin.LightLogin;
import net.bartzz.lightlogin.api.files.LightConfiguration;
import net.bartzz.lightlogin.api.files.enums.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class LightConfigurationImpl implements LightConfiguration
{
    private LightLogin lightLogin = LightLogin.getInstance();
    private FileConfiguration config = this.lightLogin.getConfig();

    private final Map<Configuration, Object> valuesMap = new HashMap<>();

    @Override
    public void load()
    {
        this.lightLogin.saveDefaultConfig();

        for (String key : this.config.getKeys(false))
        {
            this.valuesMap.put(Configuration.valueOf(key.replace('.', '_').toUpperCase()), this.config.get(key));
        }
    }

    @Override
    public String getString(Configuration c, String def)
    {
        Object o = this.valuesMap.get(c);
        if (o == null || !(o instanceof String))
        {
            return def;
        }

        return (String) o;
    }

    @Override
    public Integer getInt(Configuration c, int def)
    {
        Object o = this.valuesMap.get(c);
        if (o == null || !(o instanceof String))
        {
            return def;
        }

        return (Integer) o;
    }

    @Override
    public Boolean getBoolean(Configuration c, boolean def)
    {
        Object o = this.valuesMap.get(c);
        if (o == null || !(o instanceof String))
        {
            return def;
        }

        return (Boolean) o;
    }

    @Override
    public Long getLong(Configuration c, long def)
    {
        Object o = this.valuesMap.get(c);
        if (o == null || !(o instanceof String))
        {
            return def;
        }

        return (Long) o;
    }
}
