package io.github.gurky9636mods.autovanilla.common.blockentity;

import io.github.gurky9636mods.autovanilla.AutoVanillaMod;
import io.github.gurky9636mods.autovanilla.common.blocks.AutoVanillaBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AutoVanillaBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AutoVanillaMod.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AutoSmithingTableBlockEntity>> AUTO_SMITHING_TABLE = BLOCK_ENTITIES.register(
            "auto_smithing_table",
            () -> BlockEntityType.Builder.of(
                    AutoSmithingTableBlockEntity::new,
                    AutoVanillaBlocks.AUTO_SMITHING_TABLE.get()
                )
                .build(null));

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITIES.register(modEventBus);
    }
}
