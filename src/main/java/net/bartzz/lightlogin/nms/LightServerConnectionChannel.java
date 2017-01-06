package net.bartzz.lightlogin.nms;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.minecraft.server.v1_11_R1.*;

public class LightServerConnectionChannel extends ChannelInitializer
{
    private LightServerConnection a;

    public LightServerConnectionChannel(LightServerConnection serverconnection)
    {
        this.a = serverconnection;
    }

    protected void initChannel(Channel channel)
    {
        try
        {
            channel.config().setOption(ChannelOption.IP_TOS, Integer.valueOf(24));
        } catch (ChannelException channelexception)
        {
            channelexception.printStackTrace();
        }

        try
        {
            channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(false));
        } catch (ChannelException channelexception1) {
            channelexception1.printStackTrace();
        }

        channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("legacy_query", new LegacyPingHandler(this.a)).addLast("splitter", new PacketSplitter()).addLast("decoder", new PacketDecoder(EnumProtocolDirection.SERVERBOUND)).addLast("prepender", new PacketPrepender()).addLast("encoder", new PacketEncoder(EnumProtocolDirection.CLIENTBOUND));
        NetworkManager networkmanager = new NetworkManager(EnumProtocolDirection.SERVERBOUND);

        this.a.a().add(networkmanager);
        channel.pipeline().addLast("packet_handler", networkmanager);
        networkmanager.setPacketListener(new LightHandshakeListener(this.a.d(), networkmanager));
    }
}