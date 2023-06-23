package com.lifebinding.init;

import com.lifebinding.LifeBinding;
import com.lifebinding.effect.BindingEffect;
import com.lifebinding.effect.DeathEffect;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BindingEffects {
    public static final DeferredRegister<Effect> EFFECTS = new DeferredRegister<>(ForgeRegistries.POTIONS, LifeBinding.id);

    public static final RegistryObject<Effect> BINDING_EFFECT = EFFECTS.register("binding", BindingEffect::new);
    public static final RegistryObject<Effect> DEATH_EFFECT = EFFECTS.register("death", DeathEffect::new);

}