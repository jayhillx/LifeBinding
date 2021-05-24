package com.jayhill.lifebinding.events;

import com.jayhill.lifebinding.capability.binding.BindingCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AttachCapabilityEvent {

    @SubscribeEvent
    public void attachPlayerCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            BindingCapabilityProvider provider = new BindingCapabilityProvider();
            event.addCapability(new ResourceLocation("lifebinding", "bound"), provider);
            event.addListener(provider::invalidate);
        }
    }

}