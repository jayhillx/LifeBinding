package com.jayhill.lifebinding.capability.network;

import com.jayhill.lifebinding.capability.network.messages.SendMessage;
import net.minecraft.entity.player.ServerPlayerEntity;

import javax.annotation.Nonnull;

public class NetworkHelper {

    public static void updatePlayerBound(@Nonnull ServerPlayerEntity player, boolean bound) {
        PacketHandler.sendToClient(new SendMessage(bound), player);
    }

}