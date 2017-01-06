package net.bartzz.lightlogin.api.files;

import net.bartzz.lightlogin.api.files.enums.Messages;

public interface LightMessages
{
    void load();

    String get(Messages key);
}
