package io.github.gurky9636mods.autovanilla.common.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.gurky9636mods.autovanilla.Config;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.level.Level;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class AutoSmithingTransformRecipe implements AutoSmithingRecipe {

    public final Ingredient input;
    public final Ingredient addition;
    public final Ingredient template;
    public final ItemStack result;
    public final int ticks;
    /**
     * Energy per tick
     */
    public final int energy;

    public AutoSmithingTransformRecipe(Ingredient input, Ingredient addition, Ingredient template, ItemStack result, int ticks, int energy) {
        this.input = input;
        this.addition = addition;
        this.template = template;
        this.result = result;
        this.ticks = ticks;
        this.energy = energy;
    }

    public static AutoSmithingTransformRecipe fromVanilla(SmithingTransformRecipe recipe, HolderLookup.Provider provider) {
        Ingredient base = ObfuscationReflectionHelper.getPrivateValue(SmithingTransformRecipe.class, recipe, "base");
        Ingredient addition = ObfuscationReflectionHelper.getPrivateValue(SmithingTransformRecipe.class, recipe, "addition");
        Ingredient template = ObfuscationReflectionHelper.getPrivateValue(SmithingTransformRecipe.class, recipe, "template");
        return new AutoSmithingTransformRecipe(base, addition, template, recipe.getResultItem(provider), Config.defaultSmithingTicks, Config.defaultSmithingEnergy);
    }

    @Override
    public boolean matches(@NotNull AutoSmithingInput input, @NotNull Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(AutoSmithingInput input, HolderLookup.Provider provider) {
        if (this.isInputValid(input.input()) && this.isAdditionValid(input.addition()) && this.isTemplateValid(input.template())) {
            return this.getResultItem(provider);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return this.result;
    }

    @Override
    public RecipeSerializer<AutoSmithingTransformRecipe> getSerializer() {
        return SERIALIZER;
    }

    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public RecipeType<?> getType() {
        return AutoVanillaRecipes.AUTO_SMITHING.get();
    }

    boolean isInputValid(ItemStack input) {
        return this.input.test(input);
    }

    boolean isAdditionValid(ItemStack addition) {
        return this.addition.test(addition);
    }

    boolean isTemplateValid(ItemStack template) {
        return this.template.test(template);
    }

    @Override
    public boolean isIncomplete() {
        return Stream.of(this.input, this.addition, this.template).anyMatch(Ingredient::hasNoItems);
    }

    @Override
    public int getEnergy() {
        return energy;
    }

    @Override
    public int getTicks() {
        return ticks;
    }

    public static class Serializer implements RecipeSerializer<AutoSmithingTransformRecipe> {

        private static final MapCodec<AutoSmithingTransformRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                        Ingredient.CODEC.fieldOf("input").forGetter(recipe -> recipe.input),
                        Ingredient.CODEC.fieldOf("addition").forGetter(recipe -> recipe.addition),
                        Ingredient.CODEC.fieldOf("template").forGetter(recipe -> recipe.template),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.INT.fieldOf("ticks").forGetter(recipe -> recipe.ticks),
                        Codec.INT.fieldOf("energy").forGetter(recipe -> recipe.energy)
                ).apply(instance, AutoSmithingTransformRecipe::new)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, AutoSmithingTransformRecipe> STREAM_CODEC = StreamCodec.of(
                AutoSmithingTransformRecipe.Serializer::toNetwork,
                AutoSmithingTransformRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<AutoSmithingTransformRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AutoSmithingTransformRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static AutoSmithingTransformRecipe fromNetwork(RegistryFriendlyByteBuf pBuf) {
            Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuf);
            Ingredient addition = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuf);
            Ingredient template = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuf);
            ItemStack result = ItemStack.STREAM_CODEC.decode(pBuf);
            int ticks = pBuf.readInt();
            int energy = pBuf.readInt();
            return new AutoSmithingTransformRecipe(input, addition, template, result, ticks, energy);
        }

        public static void toNetwork(RegistryFriendlyByteBuf pBuf, AutoSmithingTransformRecipe pRecipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuf, pRecipe.input);
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuf, pRecipe.addition);
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuf, pRecipe.template);
            ItemStack.STREAM_CODEC.encode(pBuf, pRecipe.result);
            pBuf.writeInt(pRecipe.ticks);
            pBuf.writeInt(pRecipe.energy);
        }
    }
}
