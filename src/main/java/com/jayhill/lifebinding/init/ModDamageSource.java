package com.jayhill.lifebinding.init;

import net.minecraft.util.DamageSource;

public class ModDamageSource {

    public static final DamageSource LIFE_BINDING = (new DamageSource("lifeBinding")).setDamageBypassesArmor().setMagicDamage().setDamageAllowedInCreativeMode();

}