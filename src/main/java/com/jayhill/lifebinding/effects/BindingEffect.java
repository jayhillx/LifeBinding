package com.jayhill.lifebinding.effects;

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
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity)entity;

        if (entity.isPotionActive(LifeBindingPotion.LIFE_BINDING_EFFECT.get())) {
            for (LivingEntity livingEntity : entity.world.getEntitiesWithinAABB(LivingEntity.class, entity.getBoundingBox().grow(3.0D))) {

                if (livingEntity.isAlive() && livingEntity != entity) {
                    if (!(livingEntity instanceof PlayerEntity)) {

                        if (livingEntity.isPotionActive(LifeBindingPotion.LIFE_BINDING_EFFECT.get())) {

                            serverPlayer.removePotionEffect(LifeBindingPotion.LIFE_BINDING_EFFECT.get());
                            serverPlayer.getEntityWorld().playSound(null, (livingEntity).getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 0.5F, 1.0F);
                            ((ServerWorld)serverPlayer.world).spawnParticle(ParticleTypes.EXPLOSION, serverPlayer.getPosX(), serverPlayer.getPosYHeight(1.0D), serverPlayer.getPosZ(), 8, 2.0D, 2.0D, 2.0D, 1.0D);

                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        if (this == LifeBindingPotion.LIFE_BINDING_EFFECT.get()) {
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