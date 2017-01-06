package net.bartzz.lightlogin.api.storage.database;

import net.bartzz.lightlogin.LightLogin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public enum PreparedStatements
{
    INSERT("insert into lightlogin_players (playerId, playerName, account) values (?, ?, ?)"),
    UPDATE("update lightlogin_players set playerName=?, account=? where playerId=?"),
    DELETE("delete * from lightlogin_players where playerId=?"),
    SELECT("select * from lightlogin_players");

    private Connection connection = LightLogin.getInstance().getLightDatabase().getConnection();
    private String query;

    PreparedStatements(String query)
    {
        this.query = query;
    }

    public PreparedStatement build()
    {
        try
        {
            return this.connection.prepareStatement(this.query);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
