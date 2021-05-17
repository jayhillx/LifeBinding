package com.jayhill.lifebinding.capability.network.client;

import com.jayhill.lifebinding.capability.binding.BindingCapabilities;
import com.jayhill.lifebinding.capability.network.messages.SendMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;

public class ClientWork {

    @SuppressWarnings("all")
    public static void handleUpdatePlayerBound(SendMessage message) {
        ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;

        if (clientPlayer != null) {
            clientPlayer.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).orElse(BindingCapabilities.LIFE_BOUND_CAPABILITY.getDefaultInstance()).setBound(message.bound);
        }
    }

}