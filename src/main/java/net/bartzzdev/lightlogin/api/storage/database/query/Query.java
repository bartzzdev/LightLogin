package net.bartzzdev.lightlogin.api.storage.database.query;

import java.sql.ResultSet;

public interface Query {

    int executeUpdate();

    ResultSet executeQuery();
}
