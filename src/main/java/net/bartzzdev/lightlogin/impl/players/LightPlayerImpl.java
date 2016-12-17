package net.bartzzdev.lightlogin.impl.players;

import net.bartzzdev.lightlogin.LightLogin;
import net.bartzzdev.lightlogin.api.players.LightPlayer;
import net.bartzzdev.lightlogin.api.thread.Executor;
import net.bartzzdev.lightlogin.api.thread.ExecutorInitializer;
import net.bartzzdev.lightlogin.enums.Account;
import net.bartzzdev.lightlogin.enums.PreparedStatements;
import net.bartzzdev.lightlogin.enums.Storage;
import net.bartzzdev.lightlogin.tasks.QueryCallable;
import net.bartzzdev.lightlogin.tasks.UpdateCallable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LightPlayerImpl implements LightPlayer {

    private final UUID uuid;
    private String playerName;
    private Account account;
    private long lastLogged;
    private boolean changed;

    public LightPlayerImpl(UUID uuid, String playerName) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.account = Account.NOT_AUTHORIZED;
    }

    @Override
    public String getPlayerName() {
        return this.playerName;
    }

    @Override
    public Account getAccountState() {
        return this.account;
    }

    @Override
    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public long lastLogged() {
        return this.lastLogged;
    }

    @Override
    public void setLastLogged(long lastLogged) {
        this.lastLogged = lastLogged;
    }

    @Override
    public boolean changed() {
        return this.changed;
    }

    @Override
    public void change() {
        this.changed = true;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public LightPlayer saveFirst() {
        if (LightLogin.getInstance().getStorageType().equals(Storage.MYSQL)) {
            PreparedStatement preparedStatement = PreparedStatements.INSERT_PLAYER.build();
            try {
                byte[] uuidBytes = new byte[16];
                ByteBuffer.wrap(uuidBytes).order(ByteOrder.BIG_ENDIAN).putLong(this.uuid.getMostSignificantBits()).putLong(this.uuid.getLeastSignificantBits());
                preparedStatement.setBytes(0, uuidBytes);
                preparedStatement.setString(1, this.playerName);
                preparedStatement.setBoolean(2, this.account.equals(Account.PREMIUM));
                preparedStatement.setLong(3, this.lastLogged);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Executor<Integer> insertExecutor = new ExecutorInitializer(new UpdateCallable(preparedStatement));
            insertExecutor.call();
        }
        return this;
    }

    @Override
    public LightPlayer save() {
        if (LightLogin.getInstance().getStorageType().equals(Storage.MYSQL)) {
            PreparedStatement preparedStatement = PreparedStatements.UPDATE_PLAYER.build();
            try {
                byte[] uuidBytes = new byte[16];
                ByteBuffer.wrap(uuidBytes).order(ByteOrder.BIG_ENDIAN).putLong(this.uuid.getMostSignificantBits()).putLong(this.uuid.getLeastSignificantBits());
                preparedStatement.setString(0, this.playerName);
                preparedStatement.setBoolean(1, this.account.equals(Account.PREMIUM));
                preparedStatement.setLong(2, this.lastLogged);
                preparedStatement.setBytes(3, uuidBytes);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Executor<Integer> updateExecutor = new ExecutorInitializer(new UpdateCallable(preparedStatement));
            updateExecutor.call();
        }
        return this;
    }

    @Override
    public LightPlayer load() {
        LightPlayer lightPlayer = null;
        if (LightLogin.getInstance().getStorageType().equals(Storage.MYSQL)) {
            try {
                byte[] uuidBytes = new byte[16];
                ByteBuffer.wrap(uuidBytes).order(ByteOrder.BIG_ENDIAN).putLong(this.uuid.getMostSignificantBits()).putLong(this.uuid.getLeastSignificantBits());
                PreparedStatement preparedStatement = PreparedStatements.SELECT_PLAYER.build();
                preparedStatement.setBytes(0, uuidBytes);
                Executor<ResultSet> selectExecutor = new ExecutorInitializer(new QueryCallable(preparedStatement));
                ResultSet resultSet = selectExecutor.call();
                while (resultSet.next()) {
                    UUID playerId = UUID.nameUUIDFromBytes(resultSet.getBytes("playerId"));
                    String playerName = resultSet.getString("playerName");
                    boolean premium = resultSet.getBoolean("premium");
                    long lastLogged = resultSet.getLong("lastLogged");
                    lightPlayer = LightLogin.getInstance().getPlayerManager().create(playerId, playerName);
                    if (premium) lightPlayer.setAccount(Account.PREMIUM);
                    lightPlayer.setLastLogged(lastLogged);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lightPlayer;
    }
}
