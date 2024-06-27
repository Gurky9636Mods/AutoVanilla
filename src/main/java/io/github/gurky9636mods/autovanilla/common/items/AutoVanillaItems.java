package io.github.gurky9636mods.autovanilla.common.items;

import io.github.gurky9636mods.autovanilla.AutoVanillaMod;
import io.github.gurky9636mods.autovanilla.common.blocks.AutoVanillaBlocks;
import net.minecraft.world.item.BlockItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AutoVanillaItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AutoVanillaMod.MOD_ID);

    public static final DeferredItem<BlockItem> AUTO_SMITHING_TABLE = ITEMS.registerSimpleBlockItem("auto_smithing_table", AutoVanillaBlocks.AUTO_SMITHING_TABLE);

    public static void register(IEventBus modEventBus) {
        // Register the items
        ITEMS.register(modEventBus);
    }
}
