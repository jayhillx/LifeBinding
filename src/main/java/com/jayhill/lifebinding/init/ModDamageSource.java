package com.jayhill.lifebinding.init;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;

public class ModDamageSource extends DamageSource {

    public ModDamageSource(String damageTypeIn) {
        super(damageTypeIn);
    }

    /** Cause is from the bound player. */
    public static DamageSource causeBoundPlayer(PlayerEntity player) {
        return new BoundDamageSource("lifeBinding", player);
    }

}