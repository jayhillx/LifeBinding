package com.jayhill.lifebinding.effects;

import com.jayhill.lifebinding.init.BoundDamageSource;
import com.jayhill.lifebinding.potions.LifeBindingPotion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import javax.annotation.Nonnull;

public class DoomedEffect extends Effect {

    public DoomedEffect() {
        super(EffectType.HARMFUL, 13315412);
    }

    /** Life Doomed Effect.
     ** This effect will kill the player. */
    public void performEffect(@Nonnull LivingEntity entity, int amplifier) {

        if (this == LifeBindingPotion.DOOMED_EFFECT.get()) {
            entity.attackEntityFrom(BoundDamageSource.causeBoundPlayer(entity).setDamageBypassesArmor().setDamageIsAbsolute().setDamageAllowedInCreativeMode(), 1.0F);
        }
    }

    public boolean isReady(int duration, int amplifier) {
        if (this == LifeBindingPotion.DOOMED_EFFECT.get()) {
            int i = 40 >> amplifier;
            if (i > 0) {
                return duration % i == 0;
            } else {
                return true;
            }
        } return true;
    }

}