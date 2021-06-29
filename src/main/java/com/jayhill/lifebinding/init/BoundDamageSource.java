package com.jayhill.lifebinding.init;

import com.jayhill.lifebinding.capability.binding.BindingCapabilities;
import com.jayhill.lifebinding.capability.binding.IBoundCapability;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

public class BoundDamageSource extends DamageSource {
    protected final LivingEntity damageSourceIn;

    public BoundDamageSource(String damageTypeIn, LivingEntity damageSourceIn) {
        super(damageTypeIn);
        this.damageSourceIn = damageSourceIn;
    }

    /** Sets cause from the bound player. */
    public static DamageSource causeBoundPlayer(LivingEntity entity) {
        return new BoundDamageSource("lifeBinding", entity);
    }

    @Nonnull
    public ITextComponent getDeathMessage(LivingEntity entity) {
        String s = "death.attack." + this.damageType;
        String s1 = s + ".boundPlayer";

        IBoundCapability bound = entity.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).orElse(null);

        return new TranslationTextComponent(s1, entity.getDisplayName(), bound.getName());
    }

}