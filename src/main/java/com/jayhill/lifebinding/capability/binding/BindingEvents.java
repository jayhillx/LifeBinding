package com.jayhill.lifebinding.capability.binding;

import com.jayhill.lifebinding.capability.BoundPlayersList;
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
import net.minecraftforge.event.entity.player.PlayerEvent;
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
                IBoundCapability bound = player.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).orElse(null);

                /** Checks if the source is from our damaging effect. */
                if (event.getSource() == ModDamageSource.causeBoundPlayer(player)) {
                    BoundPlayersList.removeUsername(bound.getUUID());
                    serverPlayerEntity.removePotionEffect(LifeBindingPotion.LIFE_DAMAGING_EFFECT.get());
                }

                if (player.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).orElse(null).isBound()) {

                    /** Checks if the bound player is offline.
                     * If so, adds their name to the death list. */
                    if (player.getServer().getPlayerList().getPlayerByUUID(bound.getUUID()) == null) {
                        BoundPlayersList.setUsername(bound.getUUID(), bound.getName());
                    } else if (!player.world.isRemote) {
                        /** If the player is online, remove their name and give them the effect. */
                        player.getServer().getPlayerList().getPlayerByUUID(bound.getUUID()).addPotionEffect(new EffectInstance(LifeBindingPotion.LIFE_DAMAGING_EFFECT.get(), 200));
                        BoundPlayersList.removeUsername(bound.getUUID());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();

        if (player.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).orElse(null).isBound()) {

            /** Checks if the players uuid who logged in, matches a uuid the death list. */
            if (BoundPlayersList.containsUUID(player.getUniqueID())) {
                /** If so, applies effect to the player. */
                player.addPotionEffect(new EffectInstance(LifeBindingPotion.LIFE_DAMAGING_EFFECT.get(), 150));
                BoundPlayersList.removeUsername(player.getUniqueID());
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