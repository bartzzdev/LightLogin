package net.bartzz.lightlogin.api.storage.database;

import java.sql.ResultSet;

public interface DatabaseQuery
{
    int executeUpdate();

    ResultSet executeQuery();
}
