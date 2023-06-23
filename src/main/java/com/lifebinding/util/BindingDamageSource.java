package com.lifebinding.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

public class BindingDamageSource extends DamageSource {
    private final String name;

    public BindingDamageSource(String name) {
        super("deathCurse");
        this.name = name;
    }

    public static DamageSource fromBoundPlayer(String name) {
        return new BindingDamageSource(name);
    }

    @Nonnull
    public ITextComponent getDeathMessage(LivingEntity entity) {
        String s = "death.attack." + this.damageType;
        String s1 = s + ".player";

        return new TranslationTextComponent(s1, entity.getDisplayName(), this.name);
    }

}