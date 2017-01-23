package net.bartzz.lightlogin.api.players.messaging;

import net.bartzz.lightlogin.api.files.enums.Messages;
import net.bartzz.lightlogin.api.players.LightPlayer;

import java.util.function.Predicate;

/*
 * @author bartzz
 * @date 22.01.2017
 */
public interface Messaging
{
    String PREFIX = "[LightLogin]";

    void prefixedMessage(Messages message);
    boolean prefixedMessageIf(Messages message, Predicate<LightPlayer> predicate);

    void coloredMessage(Messages message);
    boolean coloredMessageIf(Messages message, Predicate<LightPlayer> predicate);

    void coloredPrefixedMessage(Messages message);
    boolean coloredPrefixedMessageIf(Messages message, Predicate<LightPlayer> predicate);
}
