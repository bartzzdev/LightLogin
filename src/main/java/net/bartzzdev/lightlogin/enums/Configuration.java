package net.bartzzdev.lightlogin.enums;

import net.bartzzdev.lightlogin.LightLogin;
import net.bartzzdev.lightlogin.api.yaml.LightConfiguration;

public enum Configuration {

    PREMIUM$AUTOLOGIN,

    STORAGE$MYSQL,
    STORAGE$MYSQL$HOST,
    STORAGE$MYSQL$PORT,
    STORAGE$MYSQL$USER,
    STORAGE$MYSQL$PASSWORD,
    STORAGE$MYSQL$DATABASE,
    STORAGE$FLAT;

    private LightConfiguration configuration = LightLogin.getInstance().getConfiguration();

    public String getString() {
        if (!this.configuration.contains(this)) {
            return "";
        }

        return (String) this.configuration.getFieldsMap().get(this);
    }

    public int getInteger() {
        if (!this.configuration.contains(this)) {
            return 0;
        }

        return (int) this.configuration.getFieldsMap().get(this);
    }

    public boolean getBoolean() {
        if (!this.configuration.contains(this)) {
            return false;
        }

        return (boolean) this.configuration.getFieldsMap().get(this);
    }
}
