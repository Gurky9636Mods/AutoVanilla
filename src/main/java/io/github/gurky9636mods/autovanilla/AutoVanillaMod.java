package io.github.gurky9636mods.autovanilla;

import io.github.gurky9636mods.autovanilla.client.screens.AutoSmithingTableScreen;
import io.github.gurky9636mods.autovanilla.common.blockentitys.AutoVanillaBlockEntities;
import io.github.gurky9636mods.autovanilla.common.blocks.AutoVanillaBlocks;
import io.github.gurky9636mods.autovanilla.common.items.AutoVanillaItems;
import io.github.gurky9636mods.autovanilla.common.items.AutoVanillaTabs;
import io.github.gurky9636mods.autovanilla.common.menus.AutoVanillaMenus;
import io.github.gurky9636mods.autovanilla.datagen.AutoVanillaDatagen;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(AutoVanillaMod.MOD_ID)
public class AutoVanillaMod
{
    public static final String MOD_ID = "autovanilla";
    private static final Logger LOGGER = LogUtils.getLogger();

    public AutoVanillaMod(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup);

        AutoVanillaBlocks.register(modEventBus);
        AutoVanillaItems.register(modEventBus);
        AutoVanillaTabs.register(modEventBus);
        AutoVanillaBlockEntities.register(modEventBus);
        AutoVanillaMenus.register(modEventBus);
        AutoVanillaDatagen.register(modEventBus);
        this.register();

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void register() {
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }

        @SubscribeEvent
        public static void onScreensRegister(RegisterMenuScreensEvent event)
        {
            event.register(AutoVanillaMenus.AUTO_SMITHING_TABLE.get(), AutoSmithingTableScreen::new);
        }
    }
}
