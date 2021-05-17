package com.jayhill.lifebinding.effects;

import com.jayhill.lifebinding.capability.CapabilityHelper;
import com.jayhill.lifebinding.potions.LifeBindingPotion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;

public class BindingEffect extends Effect {
    public BindingEffect() {
        super(EffectType.HARMFUL, 10690366);
    }

    /**
     * Binding Effect
     * This effect will bind both players UUID's together.
     * */
    public void performEffect(LivingEntity entity, int amplifier) {
        if (entity.isPotionActive(LifeBindingPotion.LIFE_BINDING_EFFECT.get())) {

            for (LivingEntity livingEntity : entity.world.getEntitiesWithinAABB(LivingEntity.class, entity.getBoundingBox().grow(2.0D))) {
                if (livingEntity.isAlive() && livingEntity != entity) {
                    if (!(livingEntity instanceof PlayerEntity && ((PlayerEntity) livingEntity).isCreative())) {

                        if (livingEntity.isPotionActive(LifeBindingPotion.LIFE_BINDING_EFFECT.get())) {

                            livingEntity.removePotionEffect(LifeBindingPotion.LIFE_BINDING_EFFECT.get());
                            livingEntity.getEntityWorld().playSound(null, (livingEntity).getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 0.5F, 1.0F);
                            ((ServerWorld)livingEntity.world).spawnParticle(ParticleTypes.EXPLOSION, livingEntity.getPosX(), livingEntity.getPosYHeight(1.0D), livingEntity.getPosZ(), 8, 2.0D, 2.0D, 2.0D, 1.0D);

                            entity.removePotionEffect(LifeBindingPotion.LIFE_BINDING_EFFECT.get());
                            entity.getEntityWorld().playSound(null, (entity).getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 0.5F, 1.0F);
                            ((ServerWorld)entity.world).spawnParticle(ParticleTypes.EXPLOSION, entity.getPosX(), entity.getPosYHeight(1.0D), entity.getPosZ(), 8, 2.0D, 2.0D, 2.0D, 1.0D);

                            assert livingEntity instanceof ServerPlayerEntity;
                            CapabilityHelper.setPlayerBound((ServerPlayerEntity) livingEntity, true);
                            CapabilityHelper.setPlayerBound((ServerPlayerEntity) entity, true);

                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        if (this == LifeBindingPotion.LIFE_DAMAGING_EFFECT.get()) {
            int i = 40 >> amplifier;
            if (i > 0) {
                return duration % i == 0;
            } else {
                return true;
            }
        }
        return true;
    }

}