package net.bartzzdev.lightlogin.api.data;

import java.util.UUID;

public interface ObjectHolder<T> {

    UUID getUuid();

    T save();

    T load();
}
