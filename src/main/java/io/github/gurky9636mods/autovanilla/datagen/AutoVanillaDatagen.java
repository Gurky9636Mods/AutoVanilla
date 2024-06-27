package io.github.gurky9636mods.autovanilla.datagen;

import net.minecraft.data.DataProvider;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class AutoVanillaDatagen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<DataProvider>) pOutput -> new AutoVanillaBlockStateProvider(pOutput, event.getExistingFileHelper())
        );

        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<DataProvider>) pOutput -> new AutoVanillaItemModelProvider(pOutput, event.getExistingFileHelper())
        );
    }

    public static void register(IEventBus eventBus)
    {
        eventBus.register(AutoVanillaDatagen.class);
    }
}
