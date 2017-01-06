package net.bartzz.lightlogin.nms;

import net.minecraft.server.v1_11_R1.*;

import java.net.InetAddress;
import java.util.HashMap;

public class LightHandshakeListener extends HandshakeListener
{
    private static final HashMap<InetAddress, Long> throttleTracker = new HashMap();
    private static int throttleCounter = 0;
    private final MinecraftServer a;
    private final NetworkManager b;

    public LightHandshakeListener(MinecraftServer minecraftserver, NetworkManager networkmanager)
    {
        super(minecraftserver, networkmanager);
        this.a = minecraftserver;
        this.b = networkmanager;
    }

    public void a(PacketHandshakingInSetProtocol packethandshakinginsetprotocol)
    {
        super.a(packethandshakinginsetprotocol);
        if (packethandshakinginsetprotocol.a() == EnumProtocol.LOGIN)
        {
            this.b.setPacketListener(new LightLoginListener(this.a, this.b));
        }
    }
}

