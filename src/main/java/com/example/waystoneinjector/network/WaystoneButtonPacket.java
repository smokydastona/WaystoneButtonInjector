package com.example.waystoneinjector.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class WaystoneButtonPacket {
    private final int buttonId;

    public WaystoneButtonPacket(int buttonId) {
        this.buttonId = buttonId;
    }

    public static void encode(WaystoneButtonPacket pkt, FriendlyByteBuf buf) {
        buf.writeInt(pkt.buttonId);
    }

    public static WaystoneButtonPacket decode(FriendlyByteBuf buf) {
        return new WaystoneButtonPacket(buf.readInt());
    }

    public static void handle(WaystoneButtonPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            WaystoneButtonHandler.handle(ctx.get().getSender(), pkt);
        });
        ctx.get().setPacketHandled(true);
    }

    public int getButtonId() { return buttonId; }
}
