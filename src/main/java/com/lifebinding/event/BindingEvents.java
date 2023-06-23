package com.lifebinding.event;

import com.lifebinding.capability.binding.BindingCapability;
import com.lifebinding.capability.data.BoundPlayerData;
import com.lifebinding.capability.data.PlayerData;
import com.lifebinding.effect.DeathEffect;
import com.lifebinding.init.BindingEffects;
import com.lifebinding.util.BindingDamageSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BindingEvents {

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();

        if (entity.world.getServer() != null) {
            entity.world.getServer().getWorld(DimensionType.OVERWORLD).getCapability(BindingCapability.BOUND).ifPresent((bound) -> {

                if (bound.getPlayers().containsKey(entity.getUniqueID())) {
                    PlayerData data = bound.getInformationById(entity.getUniqueID());

                    PlayerEntity boundPlayer = entity.world.getServer().getPlayerList().getPlayerByUUID(data.getBoundPlayer().getId());

                    if (event.getSource() instanceof BindingDamageSource) {
                        bound.getDeathList().remove(entity.getUniqueID());
                    } else {
                        if (boundPlayer != null) {
                            boundPlayer.addPotionEffect(new EffectInstance(BindingEffects.DEATH_EFFECT.orElse(null), 300));
                        } else {
                            if (!bound.getDeathList().contains(data.getBoundPlayer().getId())) {
                                bound.getDeathList().add(data.getBoundPlayer().getId());
                            }
                        }
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        if (event.getEntityLiving().isPotionActive(BindingEffects.DEATH_EFFECT.orElse(null))) {
            if (!(event.getSource() instanceof BindingDamageSource)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();

        if (player.getServer() != null) {
            player.getServer().getWorld(DimensionType.OVERWORLD).getCapability(BindingCapability.BOUND).ifPresent((bound) -> {

                if (bound.getDeathList().contains(player.getUniqueID())) {
                    player.addPotionEffect(new EffectInstance(BindingEffects.DEATH_EFFECT.orElse(null), 400));
                }
            });
        }
    }

    @SubscribeEvent
    public void onPotionRemoved(PotionEvent.PotionRemoveEvent event) {
        if (event.getPotion() instanceof DeathEffect) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPotionExpire(PotionEvent.PotionExpiryEvent event) {
        LivingEntity entity = event.getEntityLiving();

        if (event.getPotionEffect() != null && event.getPotionEffect().getPotion() == BindingEffects.DEATH_EFFECT.get()) {

            if (entity.getServer() != null) {
                entity.getServer().getWorld(DimensionType.OVERWORLD).getCapability(BindingCapability.BOUND).ifPresent((bound) -> {
                    BoundPlayerData data = bound.getInformationById(entity.getUniqueID()).getBoundPlayer();

                    entity.attackEntityFrom(BindingDamageSource.fromBoundPlayer(data.getName()).setDamageBypassesArmor().setDamageIsAbsolute().setDamageAllowedInCreativeMode(), entity.getHealth());
                });
            }
        }
    }

}