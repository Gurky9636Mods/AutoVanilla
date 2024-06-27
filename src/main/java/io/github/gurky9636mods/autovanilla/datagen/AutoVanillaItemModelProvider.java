package io.github.gurky9636mods.autovanilla.datagen;

import io.github.gurky9636mods.autovanilla.AutoVanillaMod;
import io.github.gurky9636mods.autovanilla.common.items.AutoVanillaItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class AutoVanillaItemModelProvider extends ItemModelProvider {
    public AutoVanillaItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AutoVanillaMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(AutoVanillaItems.AUTO_SMITHING_TABLE.getId().getPath(),
                modLoc("block/auto_smithing_table"));
    }
}
