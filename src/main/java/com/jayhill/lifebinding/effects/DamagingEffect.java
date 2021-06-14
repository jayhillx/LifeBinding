package com.jayhill.lifebinding.effects;

import com.jayhill.lifebinding.init.ModDamageSource;
import com.jayhill.lifebinding.potions.LifeBindingPotion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

@SuppressWarnings("all")
public class DamagingEffect extends Effect {
    public DamagingEffect() {
        super(EffectType.HARMFUL, 10690366);
    }

    /**
     * Damaging Effect
     * This effect will kill the player.
     */
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        PlayerEntity player = (PlayerEntity)entityLivingBaseIn;
        if (this == LifeBindingPotion.LIFE_DAMAGING_EFFECT.get()) {

            entityLivingBaseIn.attackEntityFrom(ModDamageSource.causeBoundPlayer(player).setDamageBypassesArmor().setDamageIsAbsolute(), 1.0F);
        }
    }

    public boolean isReady(int duration, int amplifier) {
        if (this == LifeBindingPotion.LIFE_DAMAGING_EFFECT.get()) {
            int i = 40 >> amplifier;
            if (i > 0) {
                return duration % i == 0;
            } else {
                return true;
            }
        } return true;
    }

}