package com.jayhill.lifebinding.effects;

import com.jayhill.lifebinding.capability.binding.BindingCapabilities;
import com.jayhill.lifebinding.capability.binding.IBoundCapability;
import com.jayhill.lifebinding.potions.LifeBindingPotion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;

@SuppressWarnings("all")
public class BindingEffect extends Effect {

    public BindingEffect() {
        super(EffectType.HARMFUL, 13315412);
    }

    /** Life Binding Effect.
     ** This effect will bind both players UUID's together. */
    public void performEffect(LivingEntity entity, int amplifier) {
        PlayerEntity player1 = (PlayerEntity)entity;
        Effect bindingEffect = LifeBindingPotion.BINDING_EFFECT.get();

        if (player1.isPotionActive(bindingEffect)) {
            for (PlayerEntity player2 : player1.world.getEntitiesWithinAABB(PlayerEntity.class, player1.getBoundingBox().grow(2.0D))) {

                if (player2 != player1) {
                    if (player1.isPotionActive(bindingEffect) && player2.isPotionActive(bindingEffect)) {
                        IBoundCapability bound = entity.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).orElse(null);

                        /** Checks if the player is already life bound. */
                        if (!bound.isBound()) {
                            /** Get both player's Name & UUID.
                             ** This is what binds the players. */
                            player1.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).ifPresent((capability) -> {
                                capability.setBound(true);
                                capability.setUUID(player2.getUniqueID());
                                capability.setName(player2.getScoreboardName());
                            });

                            player2.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).ifPresent((capability) -> {
                                capability.setBound(true);
                                capability.setUUID(player1.getUniqueID());
                                capability.setName(player1.getScoreboardName());
                            });

                            if (!entity.world.isRemote()) {
                                entity.world.playSound(player1, player1.getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 0.5F, 1.0F);
                                ((ServerWorld)player1.world).spawnParticle(ParticleTypes.EXPLOSION, player1.getPosX(), player1.getPosY(), player1.getPosZ(), 6, 2.0D, 2.0D, 2.0D, 1.0D);
                                ((ServerWorld)player2.world).spawnParticle(ParticleTypes.EXPLOSION, player2.getPosX(), player2.getPosY(), player2.getPosZ(), 6, 2.0D, 2.0D, 2.0D, 1.0D);
                            }

                            player1.removePotionEffect(LifeBindingPotion.BINDING_EFFECT.get());
                            player2.removePotionEffect(LifeBindingPotion.BINDING_EFFECT.get());
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

}