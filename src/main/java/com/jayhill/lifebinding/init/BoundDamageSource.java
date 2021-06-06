package com.jayhill.lifebinding.init;

import com.jayhill.lifebinding.capability.binding.BindingCapabilities;
import com.jayhill.lifebinding.capability.binding.IBoundCapability;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

@SuppressWarnings("all")
public class BoundDamageSource extends DamageSource {
    protected final PlayerEntity damageSourcePlayerIn;

    public BoundDamageSource(String damageTypeIn, PlayerEntity damageSourcePlayerIn) {
        super(damageTypeIn);
        this.damageSourcePlayerIn = damageSourcePlayerIn;
    }

    public ITextComponent getDeathMessage(LivingEntity playerEntityIn) {
        PlayerEntity playerEntity = (PlayerEntity) playerEntityIn;
        String s = "death.attack." + this.damageType;
        String s1 = s + ".boundPlayer";

        IBoundCapability boundPlayer = playerEntity.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).orElse(null);

        return playerEntity != null ? new TranslationTextComponent(s1, playerEntityIn.getDisplayName(), playerEntity.getServer().getPlayerList().getPlayerByUUID(boundPlayer.getUUID()).getDisplayName()) : new TranslationTextComponent(s, playerEntityIn.getDisplayName());
    }

}