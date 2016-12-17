package net.bartzzdev.lightlogin.enums;

import net.bartzzdev.lightlogin.LightLogin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public enum PreparedStatements {

    INSERT_PLAYER("insert into `lightlogin_players` (playerId binary(16) not null, playerName varchar(16) not null, premium boolean not null, lastLogged long not null) values (?, ?, ?, ?)"),
    UPDATE_PLAYER("update `lightlogin_players` set playerName=?, premium=?, lastLogged=? where playerId=?"),
    DELETE_PLAYER("delete from `lightlogin_players` where playerId=?"),
    SELECT_PLAYER("select * from `lightlogin_players` where playerId=?");

    private String s;
    private Connection connection = LightLogin.getInstance().getLoginDatabase().getConnection();

    PreparedStatements(String s) {
        this.s = s;
    }

    public PreparedStatement build() {
        try {
            return this.connection.prepareStatement(this.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
