package net.bartzz.lightlogin.nms;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import net.bartzz.lightlogin.LightLogin;
import net.bartzz.lightlogin.api.players.LightPlayer;
import net.bartzz.lightlogin.api.storage.database.PreparedStatements;
import net.bartzz.lightlogin.api.threads.Executor;
import net.bartzz.lightlogin.api.threads.ExecutorInitializer;
import net.bartzz.lightlogin.callables.PostCallable;
import net.bartzz.lightlogin.events.AuthorizationPlayerEvent;
import net.minecraft.server.v1_11_R1.*;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.v1_11_R1.CraftServer;
import org.bukkit.craftbukkit.v1_11_R1.util.Waitable;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.PrivateKey;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class LightLoginListener extends LoginListener
{
    private static final AtomicInteger b = new AtomicInteger(0);
    private static final Logger c = LogManager.getLogger();
    private static final Random random = new Random();
    private final byte[] e = new byte[4];
    private final MinecraftServer server;
    public final NetworkManager networkManager;
    private EnumProtocolState g;
    private int h;
    private GameProfile i;
    private final String j;
    private SecretKey loginKey;
    private EntityPlayer l;
    public String hostname = "";

    public LightLoginListener(MinecraftServer minecraftserver, NetworkManager networkmanager)
    {
        super(minecraftserver, networkmanager);
        this.g = EnumProtocolState.HELLO;
        this.j = "";
        this.server = minecraftserver;
        this.networkManager = networkmanager;
        random.nextBytes(this.e);
    }

    public void F_()
    {
        if (this.g == EnumProtocolState.READY_TO_ACCEPT)
        {
            this.b();
        }
        else if (this.g == EnumProtocolState.DELAY_ACCEPT)
        {
            EntityPlayer entityplayer = this.server.getPlayerList().a(this.i.getId());
            if (entityplayer == null)
            {
                this.g = EnumProtocolState.READY_TO_ACCEPT;
                this.server.getPlayerList().a(this.networkManager, this.l);
                this.l = null;
            }
        }

        if (this.h++ == 600)
        {
            this.disconnect("Took too long to log in");
        }

    }

    public void disconnect(String s)
    {
        try
        {
            c.info("Disconnecting {}: {}", new Object[]{ this.d(), s });
            ChatComponentText exception = new ChatComponentText(s);
            this.networkManager.sendPacket(new PacketLoginOutDisconnect(exception));
            this.networkManager.close(exception);
        } catch (Exception var3)
        {
            c.error("Error whilst disconnecting player", var3);
        }

    }

    public void b()
    {
        if (!this.i.isComplete())
        {
            this.i = this.a(this.i);
        }

        EntityPlayer s = this.server.getPlayerList().attemptLogin(this, this.i, this.hostname);
        if (s != null)
        {
            this.g = EnumProtocolState.ACCEPTED;
            if (this.server.aG() >= 0 && !this.networkManager.isLocal())
            {
                this.networkManager.sendPacket(new PacketLoginOutSetCompression(this.server.aG()), new ChannelFutureListener()
                {
                    public void a(ChannelFuture channelfuture) throws Exception
                    {
                        networkManager.setCompressionLevel(server.aG());
                    }

                    public void operationComplete(ChannelFuture future) throws Exception
                    {
                        this.a(future);
                    }
                }, new GenericFutureListener[0]);
            }

            this.networkManager.sendPacket(new PacketLoginOutSuccess(this.i));
            EntityPlayer entityplayer = this.server.getPlayerList().a(this.i.getId());
            if (entityplayer != null)
            {
                this.g = EnumProtocolState.DELAY_ACCEPT;
                this.l = this.server.getPlayerList().processLogin(this.i, s);
            }
            else
            {
                this.server.getPlayerList().a(this.networkManager, this.server.getPlayerList().processLogin(this.i, s));
            }
        }

    }

    public void a(IChatBaseComponent ichatbasecomponent)
    {
        c.info("{} lost connection: {}", new Object[]{ this.d(), ichatbasecomponent.toPlainText() });
    }

    public String d()
    {
        return this.i != null ? this.i + " (" + this.networkManager.getSocketAddress() + ")" : String.valueOf(this.networkManager.getSocketAddress());
    }

    public void a(PacketLoginInStart packetlogininstart)
    {
        Validate.validState(this.g == EnumProtocolState.HELLO, "Unexpected hello packet", new Object[0]);
        this.i = packetlogininstart.a();
        if (this.server.getOnlineMode() && !this.networkManager.isLocal())
        {
            this.g = EnumProtocolState.KEY;
            this.networkManager.sendPacket(new PacketLoginOutEncryptionBegin("", this.server.O().getPublic(), this.e));
        }
        else
        {
            this.g = EnumProtocolState.READY_TO_ACCEPT;
        }

    }

    public void a(PacketLoginInEncryptionBegin packetlogininencryptionbegin)
    {
        this.server.setOnlineMode(true);
        Validate.validState(this.g == EnumProtocolState.KEY, "Unexpected key packet", new Object[0]);
        PrivateKey privatekey = this.server.O().getPrivate();
        if (!Arrays.equals(this.e, packetlogininencryptionbegin.b(privatekey)))
        {
            throw new IllegalStateException("Invalid nonce!");
        }
        else
        {
            this.loginKey = packetlogininencryptionbegin.a(privatekey);
            this.g = EnumProtocolState.AUTHENTICATING;
            this.networkManager.a(this.loginKey);
            (new Thread("User Authenticator #" + b.incrementAndGet())
            {
                public void run()
                {
                    c.info("Hej");
                    GameProfile gameprofile = i;

                    try
                    {
                        String exception = (new BigInteger(MinecraftEncryption.a("", server.O().getPublic(), loginKey))).toString(16);
                        i = server.az().hasJoinedServer(new GameProfile(null, gameprofile.getName()), exception, this.a());
                        if (i != null)
                        {
                            if (!networkManager.isConnected())
                            {
                                return;
                            }

                            String playerName = i.getName();
                            InetAddress address = ((InetSocketAddress) networkManager.getSocketAddress()).getAddress();
                            UUID uniqueId = i.getId();
                            final CraftServer server = LightLoginListener.this.server.server;
                            LightLogin lightLogin = LightLogin.getInstance();
                            lightLogin.getPlayerManager().getNamesAwaiting().add(playerName);
                            LightPlayer lightPlayer = lightLogin.getPlayerManager().get(uniqueId);
                            if (lightPlayer == null)
                            {
                                lightPlayer = lightLogin.getPlayerManager().create(uniqueId, playerName);
                                PreparedStatement preparedStatement = PreparedStatements.INSERT.build();
                                preparedStatement.setString(0, lightPlayer.getPlayerId().toString());
                                preparedStatement.setString(1, lightPlayer.getPlayerName());
                                preparedStatement.setString(2, lightPlayer.getAccountType().name());
                            }

                            AuthorizationPlayerEvent authEvent = new AuthorizationPlayerEvent(lightPlayer);
                            String response;
                            server.getPluginManager().callEvent(authEvent);
                            {
                                Executor<String> executor = new ExecutorInitializer<String>().newExecutor(new PostCallable());
                                response = executor.execute();
                            }

                            AsyncPlayerPreLoginEvent asyncEvent = new AsyncPlayerPreLoginEvent(playerName, address, uniqueId);
                            server.getPluginManager().callEvent(asyncEvent);
                            if (PlayerPreLoginEvent.getHandlerList().getRegisteredListeners().length != 0)
                            {
                                final PlayerPreLoginEvent event = new PlayerPreLoginEvent(playerName, address, uniqueId);
                                if (asyncEvent.getResult() != PlayerPreLoginEvent.Result.ALLOWED)
                                {
                                    event.allow();
                                }

                                Waitable waitable = new Waitable()
                                {
                                    protected PlayerPreLoginEvent.Result evaluate()
                                    {
                                        server.getPluginManager().callEvent(event);
                                        return event.getResult();
                                    }
                                };

                                LightLoginListener.this.server.processQueue.add(waitable);
                                if (waitable.get() != PlayerPreLoginEvent.Result.ALLOWED)
                                {
                                    disconnect(event.getKickMessage());
                                    return;
                                }
                            }
                            else if (asyncEvent.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
                            {
                                disconnect(asyncEvent.getKickMessage());
                                return;
                            }

                            c.info("hejUUID of player {} is {}", new Object[]{ i.getName(), i.getId() });
                            g = EnumProtocolState.READY_TO_ACCEPT;
                        }
                        else if (server.R())
                        {
                            c.warn("Failed to verify username but will let them in anyway!");
                            i = LightLoginListener.this.a(gameprofile);
                            g = EnumProtocolState.READY_TO_ACCEPT;
                        }
                        else
                        {
                            disconnect("Failed to verify username!");
                            c.error("Username \'{}\' tried to join with an invalid session", new Object[]{ gameprofile.getName() });
                        }
                    } catch (AuthenticationUnavailableException var10)
                    {
                        if (server.R())
                        {
                            LightLoginListener.this.c.warn("Authentication servers are down but will let them in anyway!");
                            i = LightLoginListener.this.a(gameprofile);
                            g = EnumProtocolState.READY_TO_ACCEPT;
                        }
                        else
                        {
                            disconnect("Authentication servers are down. Please try again later, sorry!");
                            c.error("Couldn\'t verify username because servers are unavailable");
                        }
                    } catch (Exception var11)
                    {
                        disconnect("Failed to verify username!");
                        server.server.getLogger().log(Level.WARNING, "Exception verifying " + gameprofile.getName(), var11);
                    }

                }

                @Nullable
                private InetAddress a()
                {
                    SocketAddress socketaddress = networkManager.getSocketAddress();
                    return server.ac() && socketaddress instanceof InetSocketAddress ? ((InetSocketAddress) socketaddress).getAddress() : null;
                }
            }).start();
        }
    }

    protected GameProfile a(GameProfile gameprofile)
    {
        UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + gameprofile.getName()).getBytes(Charsets.UTF_8));
        return new GameProfile(uuid, gameprofile.getName());
    }

    static enum EnumProtocolState
    {
        HELLO,
        KEY,
        AUTHENTICATING,
        READY_TO_ACCEPT,
        DELAY_ACCEPT,
        ACCEPTED;
    }
}