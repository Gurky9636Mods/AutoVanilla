package io.github.gurky9636mods.autovanilla.datagen;

import io.github.gurky9636mods.autovanilla.AutoVanillaMod;
import io.github.gurky9636mods.autovanilla.common.blocks.AutoVanillaBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class AutoVanillaBlockStateProvider extends BlockStateProvider {

    public AutoVanillaBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, AutoVanillaMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ResourceLocation autoSmithingTableSide = modLoc("block/auto_smithing_table_side");
        ResourceLocation autoSmithingTableFront = modLoc("block/auto_smithing_table_front");
        ResourceLocation autoSmithingTableTop = modLoc("block/auto_smithing_table_top");
        ResourceLocation autoSmithingTableBottom = modLoc("block/auto_smithing_table_bottom");

        horizontalBlock(AutoVanillaBlocks.AUTO_SMITHING_TABLE.get(),
                models()
                        .cube(AutoVanillaBlocks.AUTO_SMITHING_TABLE.getId().getPath(),
                                autoSmithingTableBottom, autoSmithingTableTop, autoSmithingTableFront,
                                autoSmithingTableSide, autoSmithingTableSide, autoSmithingTableSide)
                        .texture("particle", autoSmithingTableFront)
        );
    }
}
