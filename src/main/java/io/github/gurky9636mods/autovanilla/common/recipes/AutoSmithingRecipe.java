package io.github.gurky9636mods.autovanilla.common.recipes;

import net.minecraft.world.item.crafting.Recipe;

public interface AutoSmithingRecipe extends Recipe<AutoSmithingInput> {
    int getTicks();
    int getEnergy();
}
