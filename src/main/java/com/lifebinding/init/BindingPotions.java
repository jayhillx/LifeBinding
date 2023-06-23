package com.lifebinding.init;

import com.lifebinding.LifeBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class BindingPotions {
    public static final DeferredRegister<Potion> POTIONS = new DeferredRegister<>(ForgeRegistries.POTION_TYPES, LifeBinding.id);

    public static final RegistryObject<Potion> LIFE_BINDING = POTIONS.register("life_binding", () -> new Potion(new EffectInstance(BindingEffects.BINDING_EFFECT.get(), 400)));

    /**
     * Brewing recipe for potion of life binding.
     */
    public static void registerRecipes() {
        BrewingRecipeRegistry.addRecipe(new Brewing(Potions.HARMING, Items.BONE, LIFE_BINDING.get()));
    }

    private static class Brewing implements IBrewingRecipe {
        private final Potion bottleInput;
        private final Item itemInput;
        private final ItemStack output;

        public Brewing(Potion bottleInput, Item itemInput, Potion output) {
            this.bottleInput = bottleInput;
            this.itemInput = itemInput;
            this.output = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), output);
        }

        public boolean isInput(@Nonnull ItemStack input) {
            return PotionUtils.getPotionFromItem(input).equals(this.bottleInput);
        }

        public boolean isIngredient(ItemStack ingredient) {
            return ingredient.getItem().equals(this.itemInput);
        }

        @Nonnull
        public ItemStack getOutput(@Nonnull ItemStack input, @Nonnull ItemStack ingredient) {
            if (isInput(input) && isIngredient(ingredient)) {
                return this.output.copy();
            } else {
                return ItemStack.EMPTY;
            }
        }
    }

}