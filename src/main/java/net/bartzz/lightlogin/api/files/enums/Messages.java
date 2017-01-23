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
    private final StringBuilder builder = new StringBuilder();

    Messages()
    {
        builder.append(this.messages.get(this));
    }

    public String getMessage()
    {
        return this.builder.toString();
    }

    public Messages replace(String[] toReplace, String[] newText)
    {
        this.builder.delete(0, this.builder.length());
        boolean success = false;
        if (toReplace.length == newText.length)
        {
            success = true;
        }

        String message = this.messages.get(this);
        if (success)
        {
            for (int i = 0; i < toReplace.length; i++)
            {
                message = StringUtils.replace(message, toReplace[i], newText[i]);
            }
        }

        this.builder.append(message);
        return this;
    }

    public String getRawMessage()
    {
        return ChatColor.stripColor(this.builder.toString());
    }
}
