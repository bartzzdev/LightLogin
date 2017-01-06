package net.bartzz.lightlogin.api.files;

import net.bartzz.lightlogin.api.files.enums.Configuration;

public interface LightConfiguration
{
    void load();

    String getString(Configuration c, String def);

    Integer getInt(Configuration c, int def);

    Boolean getBoolean(Configuration c, boolean def);

    Long getLong(Configuration c, long def);
}
