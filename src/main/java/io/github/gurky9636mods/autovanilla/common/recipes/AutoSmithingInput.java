package io.github.gurky9636mods.autovanilla.common.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.SmithingRecipeInput;

public record AutoSmithingInput(ItemStack input, ItemStack addition, ItemStack template) implements RecipeInput {
    @Override
    public ItemStack getItem(int slot) {
        return switch (slot) {
            case 0 -> input;
            case 1 -> addition;
            case 2 -> template;
            default -> ItemStack.EMPTY;
        };
    }

    public SmithingRecipeInput toVanilla() {
        return new SmithingRecipeInput(this.template, this.input, this.addition);
    }

    @Override
    public int size() {
        return 3;
    }
}
