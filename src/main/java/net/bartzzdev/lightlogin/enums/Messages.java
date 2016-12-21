package net.bartzzdev.lightlogin.enums;

import net.bartzzdev.lightlogin.LightLogin;
import net.bartzzdev.lightlogin.api.yaml.LightMessages;

public enum Messages {

    GLOBAL_INVALIDARGS,
    GLOBAL_NOPERMISSION;

    private LightMessages messages = LightLogin.getInstance().getMessages();

    public String getMessage() {
        if (!this.messages.contains(this)) {
            return "";
        }

        return this.messages.getStringMap().get(this.toString().toLowerCase());
    }
}
