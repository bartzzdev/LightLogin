package net.bartzzdev.lightlogin.tasks;

import net.bartzzdev.lightlogin.LightLogin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.Callable;

public class ConnectionCallable implements Callable<Boolean> {

    private String url, user, password;
    private Connection connection;
    private boolean toClose;

    public ConnectionCallable(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public ConnectionCallable(boolean close) {
        this.toClose = close;
    }

    @Override
    public Boolean call() throws Exception {
        if (this.toClose) {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
                return true;
            }
        } else {
            this.connection = DriverManager.getConnection(this.url, this.user, this.password);
            LightLogin.getInstance().getLoginDatabase().setConnection(this.connection);
            return true;
        }
        return false;
    }
}
