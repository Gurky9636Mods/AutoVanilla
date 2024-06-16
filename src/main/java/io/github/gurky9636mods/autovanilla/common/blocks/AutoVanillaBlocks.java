package io.github.gurky9636mods.autovanilla.common.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.gurky9636mods.autovanilla.AutoVanillaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AutoVanillaBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AutoVanillaMod.MODID);

    public static final DeferredBlock<AutoSmithingTableBlock> AUTO_SMITHING_TABLE = BLOCKS.registerBlock(
            "auto_smithing_table",
            AutoSmithingTableBlock::new,
            BlockBehaviour.Properties.of());

    public static final DeferredRegister<MapCodec<? extends Block>> BLOCK_TYPES = DeferredRegister.create(Registries.BLOCK_TYPE, AutoVanillaMod.MODID);

    public static final DeferredHolder<MapCodec<? extends Block>, MapCodec<AutoSmithingTableBlock>> AUTO_SMITHING_TABLE_CODEC = BLOCK_TYPES.register(
            "auto_smithing_table",
            () -> BlockBehaviour.simpleCodec(AutoSmithingTableBlock::new));

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        BLOCK_TYPES.register(modEventBus);
    }
}
