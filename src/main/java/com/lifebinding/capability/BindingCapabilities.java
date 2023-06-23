package com.lifebinding.capability;

import com.lifebinding.LifeBinding;
import com.lifebinding.capability.binding.BindingCapability;
import com.lifebinding.capability.binding.BindingCapabilityProvider;
import com.lifebinding.capability.binding.IBoundPlayers;
import com.lifebinding.capability.binding.StoredBoundPlayers;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BindingCapabilities {

    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(IBoundPlayers.class, new BindingCapability.Storage(), StoredBoundPlayers::new);
    }

    @SubscribeEvent
    public void attachWorldCapability(AttachCapabilitiesEvent<World> event) {
        event.addCapability(LifeBinding.location("data"), new BindingCapabilityProvider());
    }

}