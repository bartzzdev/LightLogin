package net.bartzz.lightlogin.api.files.enums;

import net.bartzz.lightlogin.LightLogin;
import net.bartzz.lightlogin.api.files.LightConfiguration;

public enum Configuration
{
    SUPPORTS_PROTOCOLLIB,
    SUPPORTS_SKRIPT,

    STORAGE_MYSQL,
    STORAGE_FILE,

    MYSQL_HOST,
    MYSQL_PORT,
    MYSQL_USER,
    MYSQL_PASSWORD,
    MYSQL_DATABASE;

    private LightLogin lightLogin = LightLogin.getInstance();
    private LightConfiguration config = this.lightLogin.getConfiguration();

    public String getString(String def)
    {
        return this.config.getString(this, def);
    }

    public Integer getInt(int def)
    {
        return this.config.getInt(this, def);
    }

    public Boolean getBoolean(boolean def)
    {
        return this.config.getBoolean(this, def);
    }

    public Long getLong(long def)
    {
        return this.config.getLong(this, def);
    }
}
