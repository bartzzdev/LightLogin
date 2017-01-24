package net.bartzz.lightlogin.impl.storage;

import net.bartzz.lightlogin.LightLogin;
import net.bartzz.lightlogin.api.players.Account;
import net.bartzz.lightlogin.api.players.LightPlayer;
import net.bartzz.lightlogin.api.storage.Storage;
import net.bartzz.lightlogin.api.storage.database.PreparedStatements;
import net.bartzz.lightlogin.impl.storage.database.DatabaseQueryImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MysqlStorage implements Storage
{
    private LightLogin lightLogin = LightLogin.getInstance();

    @Override
    public void loadAll()
    {
        PreparedStatement preparedStatement = PreparedStatements.SELECT.build();
        ResultSet resultSet = new DatabaseQueryImpl(preparedStatement).executeQuery();
        try
        {
            while (resultSet.next())
            {
                UUID playerId = UUID.fromString(resultSet.getString("playerId"));
                String playerName = resultSet.getString("playerName");
                Account account = Account.valueOf(resultSet.getString("account"));
                this.lightLogin.getPlayerManager().create(playerId, playerName).setAccountType(account);
                preparedStatement.close();
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void saveAll()
    {
        for (LightPlayer lightPlayer : this.lightLogin.getPlayerManager().getPlayersMap().values())
        {
            try
            {
                PreparedStatement preparedStatement = PreparedStatements.UPDATE.build();
                preparedStatement.setString(0, lightPlayer.getPlayerName());
                preparedStatement.setString(1, lightPlayer.getAccountType().name());
                preparedStatement.setString(3, lightPlayer.getPlayerId().toString());
                new DatabaseQueryImpl(preparedStatement).executeUpdate();
                preparedStatement.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Storage getStorage()
    {
        return this;
    }
}
