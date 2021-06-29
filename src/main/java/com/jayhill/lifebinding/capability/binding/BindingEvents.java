package com.jayhill.lifebinding.capability.binding;

import com.jayhill.lifebinding.effects.DoomedEffect;
import com.jayhill.lifebinding.init.BoundDamageSource;
import com.jayhill.lifebinding.potions.LifeBindingPotion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.PotionEvent.*;
import net.minecraftforge.event.entity.player.PlayerEvent.*;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("all")
public class BindingEvents {

    Set<UUID> players = new HashSet<>();

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

            IBoundCapability bound = player.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).orElse(null);

            if (bound.isBound()) {
                /** Prevents player from dying if the event was caused by doomed effect. */
                if (event.getSource() instanceof BoundDamageSource) {
                    if (!(player.getServer().getPlayerList().getPlayerByUUID(bound.getUUID()) == null)) {

                        player.getServer().getPlayerList().getPlayerByUUID(bound.getUUID()).removePotionEffect(LifeBindingPotion.DOOMED_EFFECT.get());
                    }
                    /** Removes players name if they died. */
                    players.remove(player.getUniqueID());
                } else {
                    if (player.getServer().getPlayerList().getPlayerByUUID(bound.getUUID()) == null) {
                        players.add(bound.getUUID());

                    } else {
                        player.getServer().getPlayerList().getPlayerByUUID(bound.getUUID()).addPotionEffect(new EffectInstance(LifeBindingPotion.DOOMED_EFFECT.get(), 300));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPotionExpire(PotionExpiryEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity;

            if (event.getPotionEffect().getPotion() == LifeBindingPotion.DOOMED_EFFECT.get()) {
                /** Removes the rest of the players health when the potion expires. */
                player.attackEntityFrom(BoundDamageSource.causeBoundPlayer(player).setDamageBypassesArmor().setDamageIsAbsolute().setDamageAllowedInCreativeMode(), player.getHealth());
            }
        }
    }

    @SubscribeEvent
    public void onPotionRemoved(PotionRemoveEvent event) {
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();

        /** Prevents the player from removing the effect. */
        if (event.getPotion() instanceof DoomedEffect) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();

        if (player.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).orElse(null).isBound()) {
            /** Checks if the players uuid who logged in, matches a uuid the hashset. */
            if (players.contains(player.getUniqueID())) {
                player.addPotionEffect(new EffectInstance(LifeBindingPotion.DOOMED_EFFECT.get(), 400));
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