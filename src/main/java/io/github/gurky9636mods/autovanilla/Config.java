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

    private static final ModConfigSpec.IntValue MAX_AUTO_SMITHING_TABLE_ENERGY = BUILDER
            .comment("The max amount of energy the AutoSmithingTable can store")
            .defineInRange("maxAutoSmithingTableEnergy", 10000, 0, 32767);

    private static final ModConfigSpec.BooleanValue CONVERT_VANILLA_TO_AUTO = BUILDER
            .comment("Convert vanilla recipes, such as Smithing Recipes to Auto Smithing recipes")
            .define("convertVanillaRecipes", true);

    private static final ModConfigSpec.IntValue DEFAULT_SMITHING_TICKS = BUILDER
            .comment("Default smithing ticks, used when converting vanilla recipes")
            .defineInRange("defaultSmithingTicks", 100, 0, 32767);

    private static final ModConfigSpec.IntValue DEFAULT_SMITHING_ENERGY = BUILDER
            .comment("Default smithing energy, used when converting vanilla recipes")
            .defineInRange("defaultSmithingEnergy", 100, 0, 32767);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int maxAutoSmithingTableEnergy;
    public static boolean convertVanillaRecipes;
    public static int defaultSmithingTicks;
    public static int defaultSmithingEnergy;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        maxAutoSmithingTableEnergy = MAX_AUTO_SMITHING_TABLE_ENERGY.get();
        convertVanillaRecipes = CONVERT_VANILLA_TO_AUTO.get();
        defaultSmithingTicks = DEFAULT_SMITHING_TICKS.get();
        defaultSmithingEnergy = DEFAULT_SMITHING_ENERGY.get();
    }
}
