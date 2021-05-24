package com.jayhill.lifebinding.effects;

import com.jayhill.lifebinding.capability.CapabilityHelper;
import com.jayhill.lifebinding.capability.binding.BindingCapabilities;
import com.jayhill.lifebinding.potions.LifeBindingPotion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

@SuppressWarnings("all")
public class BindingEffect extends Effect {

    public BindingEffect() {
        super(EffectType.HARMFUL, 10690366);
    }

    public World world;

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

                                player1.removePotionEffect(LifeBindingPotion.LIFE_BINDING_EFFECT.get());
                                player2.removePotionEffect(LifeBindingPotion.LIFE_BINDING_EFFECT.get());

                                CapabilityHelper.setPlayerBound((ServerPlayerEntity) player1, true);
                                CapabilityHelper.setPlayerBound((ServerPlayerEntity) player2, true);

                                player1.getEntityWorld().playSound(player1, (player1).getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 0.5F, 1.0F);

                                /** Get both player's UUID. */
                                UUID boundPlayer1 = null;
                                boundPlayer1 = (player2).getGameProfile().getId();

                                UUID boundPlayer2 = null;
                                boundPlayer2 = (player1).getGameProfile().getId();

                                // Player 1
                                UUID getBoundPlayer1 = boundPlayer1;
                                player1.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).ifPresent((capability) -> {
                                    capability.setUUID(getBoundPlayer1);
                                });

                                // Player 2
                                UUID getBoundPlayer2 = boundPlayer2;
                                player2.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).ifPresent((capability) -> {
                                    capability.setUUID(getBoundPlayer2);
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