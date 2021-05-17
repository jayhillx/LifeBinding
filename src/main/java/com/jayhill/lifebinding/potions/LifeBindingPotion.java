package com.jayhill.lifebinding.potions;

import com.jayhill.lifebinding.LifeBinding;
import com.jayhill.lifebinding.effects.BindingEffect;
import com.jayhill.lifebinding.effects.DamagingEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.*;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class LifeBindingPotion {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, LifeBinding.MOD_ID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, LifeBinding.MOD_ID);

    // Effects
    public static final RegistryObject<Effect> LIFE_BINDING_EFFECT = EFFECTS.register("life_binding", BindingEffect::new);
    public static final RegistryObject<Effect> LIFE_DAMAGING_EFFECT = EFFECTS.register("life_damaging", DamagingEffect::new);

    /**
     * This is for the Life Binding potion Item.
     */
    // Potion Items
    public static final RegistryObject<Potion> LIFE_BINDING = POTIONS.register("life_binding", () -> new Potion(new EffectInstance(LIFE_BINDING_EFFECT.get(), 400)));

    /**
     * This adds a recipe for the Life Binding potion.
     */
    public static void addPotionRecipes() {
        BrewingRecipeRegistry.addRecipe(new LifeBindingBrewing(Potions.HARMING, Items.BONE, LIFE_BINDING.get()));
    }

    private static class LifeBindingBrewing implements IBrewingRecipe {
        private final Potion bottleInput;
        private final Item itemInput;
        private final ItemStack output;

        public LifeBindingBrewing(Potion bottleInput, Item itemInput, Potion output) {
            this.bottleInput = bottleInput;
            this.itemInput = itemInput;
            this.output = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), output);
        }

        @Override
        public boolean isInput(ItemStack input) {
            return PotionUtils.getPotionFromItem(input).equals(this.bottleInput);
        }

        @Override
        public boolean isIngredient(ItemStack ingredient) {
            return ingredient.getItem().equals(this.itemInput);
        }

        @Override
        public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
            if (isInput(input) && isIngredient(ingredient)) {
                return this.output.copy();
            } else {
                return ItemStack.EMPTY;
            }
        }
    }

}