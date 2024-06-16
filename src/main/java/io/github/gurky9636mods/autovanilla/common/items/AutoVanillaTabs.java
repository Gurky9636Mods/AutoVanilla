package io.github.gurky9636mods.autovanilla.common.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AutoVanillaTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "autovanilla");

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.autovanilla")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> AutoVanillaItems.AUTO_SMITHING_TABLE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(AutoVanillaItems.AUTO_SMITHING_TABLE.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    public static void register(IEventBus modEventBus) {
        // Register the tabs
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
