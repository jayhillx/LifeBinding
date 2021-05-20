package com.jayhill.lifebinding.effects;

import com.jayhill.lifebinding.capability.binding.BindingCapabilities;
import com.jayhill.lifebinding.potions.LifeBindingPotion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

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
            /** Checks for nearby players & entities. */
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

                                player1.getEntityWorld().playSound(player1, (player1).getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 0.5F, 1.0F);

                                String cause1 = null;
                                String cause2 = null;
                                if (player1.getScoreboardName().equals(player1.getGameProfile().getName())) {
                                    cause1 = (player2).getGameProfile().getName();
                                } if (player2.getScoreboardName().equals(player2.getGameProfile().getName())) {
                                    cause2 = (player1).getGameProfile().getName();
                                }

                                String finalCause1 = cause1;
                                player1.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).ifPresent((statsHandler) -> {
                                    String[] causeArray = statsHandler.getBoundPlayer();
                                    causeArray[temp] = finalCause1;
                                    statsHandler.setBoundPlayer(causeArray);
                                });
                                String finalCause2 = cause2;
                                player2.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).ifPresent((statsHandler) -> {
                                    String[] causeArray = statsHandler.getBoundPlayer();
                                    causeArray[temp] = finalCause2;
                                    statsHandler.setBoundPlayer(causeArray);
                                });
                            }
                        }
                    }
                }
            }
        }
    }

    private int temp;

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

}