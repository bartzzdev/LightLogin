package net.bartzz.lightlogin.api.supports;

import net.bartzz.lightlogin.api.files.enums.Configuration;

public enum Supports
{
    PROTOCOLLIB,
    SKRIPT;

    public boolean isSupported()
    {
        if (this.equals(Supports.PROTOCOLLIB))
        {
            return Configuration.SUPPORTS_PROTOCOLLIB.getBoolean(false);
        } else if (this.equals(Supports.SKRIPT))
        {
            return Configuration.SUPPORTS_SKRIPT.getBoolean(false);
        }
        return false;
    }
}
