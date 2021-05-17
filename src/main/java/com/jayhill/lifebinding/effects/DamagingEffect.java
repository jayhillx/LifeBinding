package com.jayhill.lifebinding.effects;

import com.jayhill.lifebinding.init.ModDamageSource;
import com.jayhill.lifebinding.potions.LifeBindingPotion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class DamagingEffect extends Effect {
    public DamagingEffect() {
        super(EffectType.HARMFUL, 10690366);
    }

    /**
     * Damaging Effect
     * This effect will kill the player.
     */
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        if (this == LifeBindingPotion.LIFE_DAMAGING_EFFECT.get()) {
            entityLivingBaseIn.attackEntityFrom(ModDamageSource.LIFE_BINDING, 1.5F);
        }
    }

    public boolean isReady(int duration, int amplifier) {
        if (this == LifeBindingPotion.LIFE_DAMAGING_EFFECT.get()) {
            int i = 60 >> amplifier;
            if (i > 0) {
                return duration % i == 0;
            } else {
                return true;
            }
        }
        return true;
    }

}