package com.jayhill.lifebinding.events;

import com.jayhill.lifebinding.LifeBinding;
import com.jayhill.lifebinding.capability.binding.BindingCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AttachCapabilityEvent {

    @SubscribeEvent
    public void attachPlayerCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(LifeBinding.resourceLoc("bound"), new BindingCapabilityProvider());
        }
    }

}