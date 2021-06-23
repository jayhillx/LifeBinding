package com.jayhill.lifebinding.effects;

import com.jayhill.lifebinding.capability.binding.BindingCapabilities;
import com.jayhill.lifebinding.potions.LifeBindingPotion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

@SuppressWarnings("all")
public class BindingEffect extends Effect {

    public BindingEffect() {
        super(EffectType.HARMFUL, 10690366);
    }

    /**
     * Binding Effect
     * This effect will bind both players UUID's together.
     * */
    public void performEffect(LivingEntity entity, int amplifier) {
        Effect bindingEffect = LifeBindingPotion.LIFE_BINDING_EFFECT.get();

        if (entity.isPotionActive(bindingEffect)) {
            /** Checks for nearby players. */
            for (LivingEntity livingEntity : entity.world.getEntitiesWithinAABB(LivingEntity.class, entity.getBoundingBox().grow(2.0D))) {

                if (livingEntity.isAlive() && livingEntity != entity) {
                    if (!(livingEntity instanceof PlayerEntity && ((PlayerEntity) livingEntity).isCreative())) {
                        if (livingEntity instanceof PlayerEntity && entity instanceof PlayerEntity) {
                            PlayerEntity player1 = (PlayerEntity)livingEntity;
                            PlayerEntity player2 = (PlayerEntity)entity;

                            /** Checks if the potion on both players is active. */
                            if (player1.isPotionActive(bindingEffect) && player2.isPotionActive(bindingEffect)) {

                                player1.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).orElse(null).setBound(true);
                                player2.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).orElse(null).setBound(true);

                                player1.removePotionEffect(LifeBindingPotion.LIFE_BINDING_EFFECT.get());
                                player2.removePotionEffect(LifeBindingPotion.LIFE_BINDING_EFFECT.get());

                                if (!player1.world.isRemote()) {
                                    ((ServerWorld)player1.world).spawnParticle(ParticleTypes.EXPLOSION, player1.getPosX(), player1.getPosYHeight(1.0D), player1.getPosZ(), 6, 2.0D, 2.0D, 2.0D, 1.0D);
                                } if (!player2.world.isRemote()) {
                                    ((ServerWorld)player2.world).spawnParticle(ParticleTypes.EXPLOSION, player2.getPosX(), player2.getPosYHeight(1.0D), player2.getPosZ(), 6, 2.0D, 2.0D, 2.0D, 1.0D);
                                }

                                player1.getEntityWorld().playSound(player1, (player1).getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 0.5F, 1.0F);
                                player2.getEntityWorld().playSound(player2, (player2).getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 0.5F, 1.0F);

                                /** Get both player's Name & UUID. */
                                UUID player1UUID = null;
                                String player1Name = null;
                                player1UUID = (player2).getGameProfile().getId();
                                player1Name = (player2).getGameProfile().getName();

                                UUID player2UUID = null;
                                String player2Name = null;
                                player2UUID = (player1).getGameProfile().getId();
                                player2Name = (player1).getGameProfile().getName();

                                // Player 1
                                UUID getPlayer1UUID = player1UUID;
                                String getPlayer1Name = player1Name;
                                player1.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).ifPresent((capability) -> {
                                    capability.setUUID(getPlayer1UUID);
                                    capability.setName(getPlayer1Name);
                                });

                                // Player 2
                                UUID getPlayer2UUID = player2UUID;
                                String getPlayer2Name = player2Name;
                                player2.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).ifPresent((capability) -> {
                                    capability.setUUID(getPlayer2UUID);
                                    capability.setName(getPlayer2Name);
                                });
                            }
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