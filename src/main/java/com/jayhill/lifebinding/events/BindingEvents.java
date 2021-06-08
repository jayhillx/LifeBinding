package com.jayhill.lifebinding.events;

import com.jayhill.lifebinding.capability.CapabilityHelper;
import com.jayhill.lifebinding.capability.binding.BindingCapabilities;
import com.jayhill.lifebinding.capability.binding.BindingCapabilityProvider;
import com.jayhill.lifebinding.capability.binding.IBoundCapability;
import com.jayhill.lifebinding.init.ModDamageSource;
import com.jayhill.lifebinding.potions.LifeBindingPotion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@SuppressWarnings("all")
public class BindingEvents {

    @SubscribeEvent
    public void attachPlayerCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            BindingCapabilityProvider provider = new BindingCapabilityProvider();
            event.addCapability(new ResourceLocation("lifebinding", "bound"), provider);
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity;

            for (ServerPlayerEntity serverPlayerEntity : player.getServer().getPlayerList().getPlayers()) {

                serverPlayerEntity.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).ifPresent((h) -> {
                    if (CapabilityHelper.getPlayerBound(serverPlayerEntity)) {

                        /** Stops a loop of bound players dying. */
                        if (event.getSource() == ModDamageSource.LIFE_BINDING) {
                            serverPlayerEntity.removePotionEffect(LifeBindingPotion.LIFE_DAMAGING_EFFECT.get());

                        } else {
                            serverPlayerEntity.getServer().getPlayerList().getPlayerByUUID(h.getUUID()); {

                                /** Adds potion effect to bound player. */
                                serverPlayerEntity.addPotionEffect(new EffectInstance(LifeBindingPotion.LIFE_DAMAGING_EFFECT.get(), 200));
                                /** Removes potion from player who had died/triggered the event. */
                                player.removePotionEffect(LifeBindingPotion.LIFE_DAMAGING_EFFECT.get());
                            }
                        }
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public void onDeath(Clone event) {
        LazyOptional<IBoundCapability> capability = event.getOriginal().getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY);

        capability.ifPresent((oldStore) -> {
            event.getPlayer().getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).ifPresent((newStore) -> {
                newStore.copyForRespawn(oldStore);
            });
        });
    }

}