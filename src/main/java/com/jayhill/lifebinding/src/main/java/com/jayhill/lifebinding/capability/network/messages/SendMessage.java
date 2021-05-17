package com.jayhill.lifebinding.capability.network.messages;

import com.jayhill.lifebinding.capability.network.client.ClientWork;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SendMessage {

    public final boolean bound;

    public SendMessage(boolean bound) {
        this.bound = bound;
    }

    public static void handle(SendMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> ClientWork.handleUpdatePlayerBound(message));
        }
        context.setPacketHandled(true);
    }

    public static SendMessage decode(PacketBuffer buffer) {
        return new SendMessage(buffer.readBoolean());
    }

    public static void encode(SendMessage message, PacketBuffer buffer) {
        buffer.writeBoolean(message.bound);
    }

}