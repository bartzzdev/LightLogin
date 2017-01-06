package net.bartzz.lightlogin.api.files.enums;

import net.bartzz.lightlogin.LightLogin;
import net.bartzz.lightlogin.api.files.LightMessages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

public enum Messages
{
    GLOBAL_NOTPLAYER,
    GLOBAL_NOPERMISSION,
    GLOBAL_PARAMS,

    ACCOUNTCOMMAND_PLAYERNOTFOUND,
    ACCOUNTCOMMAND_TRIGGER;

    private final LightLogin lightLogin = LightLogin.getInstance();
    private final LightMessages messages = this.lightLogin.getMessages();

    public String getMessage()
    {
        return this.messages.get(this);
    }

    public String getMessageReplaced(String[] toReplace, String[] newText)
    {
        if (toReplace.length != newText.length)
        {
            return "";
        }

        String message = this.messages.get(this);
        for (int i = 0; i < toReplace.length; i++)
        {
            message = StringUtils.replace(message, toReplace[i], newText[i]);
        }

        return message;
    }

    public String getRawMessage()
    {
        return ChatColor.stripColor(this.messages.get(this));
    }
}
