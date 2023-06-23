package com.lifebinding.effect;

import com.lifebinding.capability.binding.BindingCapability;
import com.lifebinding.capability.data.BoundPlayerData;
import com.lifebinding.util.BindingDamageSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.world.dimension.DimensionType;

public class DeathEffect extends Effect {

    public DeathEffect() {
        super(EffectType.HARMFUL, 13315412);
    }

    public void performEffect(LivingEntity entity, int amplifier) {
        if (entity.getServer() != null) {
            entity.getServer().getWorld(DimensionType.OVERWORLD).getCapability(BindingCapability.BOUND).ifPresent((bound) -> {
                BoundPlayerData data = bound.getInformationById(entity.getUniqueID()).getBoundPlayer();

                entity.attackEntityFrom(BindingDamageSource.fromBoundPlayer(data.getName()).setDamageBypassesArmor().setDamageIsAbsolute().setDamageAllowedInCreativeMode(), 1.0F);
            });
        }
    }

    public boolean isReady(int duration, int amplifier) {
        int i = 40 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }

}