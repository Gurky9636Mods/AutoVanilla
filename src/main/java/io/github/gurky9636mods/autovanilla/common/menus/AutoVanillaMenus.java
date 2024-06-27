package io.github.gurky9636mods.autovanilla.common.menus;

import io.github.gurky9636mods.autovanilla.AutoVanillaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AutoVanillaMenus {
    public static DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, AutoVanillaMod.MOD_ID);

    public static DeferredHolder<MenuType<?>, MenuType<AutoSmithingTableMenu>> AUTO_SMITHING_TABLE = MENU_TYPES.register(
            "auto_smithing_table",
            () -> new MenuType<>(AutoSmithingTableMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static void register(IEventBus modEventBus) {
        MENU_TYPES.register(modEventBus);
    }
}
