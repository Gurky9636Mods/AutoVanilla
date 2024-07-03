package io.github.gurky9636mods.autovanilla.common.recipes;

import io.github.gurky9636mods.autovanilla.AutoVanillaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AutoVanillaRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, AutoVanillaMod.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<AutoSmithingTransformRecipe>> AUTO_SMITHING = RECIPE_TYPES.register("auto_smithing", () -> new RecipeType<>() {});

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, AutoVanillaMod.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, AutoSmithingTransformRecipe.Serializer> AUTO_SMITHING_TRANSFORM_SERIALIZER = RECIPE_SERIALIZERS.register("auto_smithing_transform", () -> AutoSmithingTransformRecipe.SERIALIZER);

    public static void register(IEventBus modBus) {
        RECIPE_TYPES.register(modBus);
        RECIPE_SERIALIZERS.register(modBus);
    }
}
