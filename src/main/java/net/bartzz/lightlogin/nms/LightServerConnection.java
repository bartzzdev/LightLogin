package net.bartzz.lightlogin.nms;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.server.v1_11_R1.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class LightServerConnection extends ServerConnection
{
    private static final Logger b = LogManager.getLogger();
    private static final NioEventLoopGroup c = new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty IO #%d").setDaemon(true).build());
    private final MinecraftServer d;
    public volatile boolean a;
    private static final List e = Collections.synchronizedList(new ArrayList());
    private static final List f = Collections.synchronizedList(new ArrayList());

    public LightServerConnection(MinecraftServer minecraftserver)
    {
        super(minecraftserver);
        this.d = minecraftserver;
        this.a = true;
    }

    public void a(InetAddress inetaddress, int i)
    {
        List list = this.e;

        synchronized (this.e)
        {
            this.e.add((((new ServerBootstrap()).channel(NioServerSocketChannel.class)).childHandler(new LightServerConnectionChannel(this)).group(c).localAddress(inetaddress, i)).bind().syncUninterruptibly());
        }
    }

    public void b()
    {
        this.a = false;
        Iterator iterator = this.e.iterator();

        while (iterator.hasNext())
        {
            ChannelFuture channelfuture = (ChannelFuture) iterator.next();

            channelfuture.channel().close().syncUninterruptibly();
        }
    }

    public void c()
    {
        List list = this.f;

        synchronized (this.f)
        {
            Iterator iterator = this.f.iterator();

            while (iterator.hasNext())
            {
                NetworkManager networkmanager = (NetworkManager) iterator.next();

                if (!networkmanager.isConnected())
                {
                    iterator.remove();
                    if (networkmanager.f != null)
                    {
                        networkmanager.i().a(networkmanager.j());
                    } else if (networkmanager.i() != null)
                    {
                        networkmanager.i().a(new ChatComponentText("Disconnected"));
                    }
                } else
                    {
                    try
                    {
                        networkmanager.a();
                    } catch (Exception exception)
                    {
                        if (networkmanager.isLocal())
                        {
                            CrashReport crashreport = CrashReport.a(exception, "Ticking memory connection");
                            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Ticking connection");

                            crashreportsystemdetails.a("Connection", new CrashReportCallable[]{});
                            throw new ReportedException(crashreport);
                        }

                        b.warn("Failed to handle packet for " + networkmanager.getSocketAddress(), exception);
                        ChatComponentText chatcomponenttext = new ChatComponentText("Internal server error");

                        networkmanager.sendPacket(new PacketPlayOutKickDisconnect(chatcomponenttext), new GenericFutureListener<Future<? super Void>>()
                        {
                            @Override
                            public void operationComplete(Future<? super Void> future) throws Exception
                            {
                                networkmanager.close(chatcomponenttext);
                            }
                        });
                        networkmanager.stopReading();
                    }
                }
            }
        }
    }

    public MinecraftServer d()
    {
        return this.d;
    }

    public static List a()
    {
        return f;
    }
}
