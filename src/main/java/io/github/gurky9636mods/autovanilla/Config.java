package io.github.gurky9636mods.autovanilla;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = AutoVanillaMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to include default vanilla recipes")
            .define("vanillaRecipes", true);

    private static final ModConfigSpec.IntValue MAX_AUTO_SMITHING_TABLE_ENERGY = BUILDER
            .comment("The max amount of energy the AutoSmithingTable can store")
            .defineInRange("maxAutoSmithingTableEnergy", 10000, 0, 32767);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean logDirtBlock;
    public static int maxAutoSmithingTableEnergy;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        logDirtBlock = LOG_DIRT_BLOCK.get();
        maxAutoSmithingTableEnergy = MAX_AUTO_SMITHING_TABLE_ENERGY.get();
    }
}
