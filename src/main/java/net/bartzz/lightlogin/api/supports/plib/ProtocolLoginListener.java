package net.bartzz.lightlogin.api.supports.plib;

import com.comphenix.protocol.ProtocolManager;
import net.bartzz.lightlogin.LightLogin;
import net.bartzz.lightlogin.api.data.DataInitializer;

/*
 * @author bartzz
 * @date 17.01.2017
 */
public class ProtocolLoginListener implements DataInitializer<ProtocolLoginListener>
{
    private ProtocolManager protocolManager = LightLogin.getInstance().getProtocolManager();

    @Override
    public ProtocolLoginListener register()
    {
        return this;
    }
}
