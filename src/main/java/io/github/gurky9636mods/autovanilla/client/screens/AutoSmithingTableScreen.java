package io.github.gurky9636mods.autovanilla.client.screens;

import io.github.gurky9636mods.autovanilla.AutoVanillaMod;
import io.github.gurky9636mods.autovanilla.common.menus.AutoSmithingTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AutoSmithingTableScreen extends AbstractContainerScreen<AutoSmithingTableMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(AutoVanillaMod.MOD_ID, "textures/gui/auto_smithing_table.png");

    private static final int ELEMENT_PROGRESS_BAR_X = 102;
    private static final int ELEMENT_PROGRESS_BAR_Y = 37;
    private static final int ELEMENT_PROGRESS_BAR_WIDTH = 128 - ELEMENT_PROGRESS_BAR_X;
    private static final int ELEMENT_PROGRESS_BAR_HEIGHT = 57 - ELEMENT_PROGRESS_BAR_Y;
    private static final int OFFSCREEN_PROGRESS_BAR_FAILED_X = 176;
    private static final int OFFSCREEN_PROGRESS_BAR_FAILED_Y = 0;
    private static final int OFFSCREEN_PROGRESS_BAR_SUCCESS_X = 176;
    private static final int OFFSCREEN_PROGRESS_BAR_SUCCESS_Y = 94;

    private static final int ELEMENT_ENERGY_BAR_X = 157;
    private static final int ELEMENT_ENERGY_BAR_Y = 6;
    private static final int ELEMENT_ENERGY_BAR_WIDTH = 166 - ELEMENT_ENERGY_BAR_X;
    private static final int ELEMENT_ENERGY_BAR_HEIGHT = 78 - ELEMENT_ENERGY_BAR_Y;
    private static final int OFFSCREEN_ENERGY_BAR_X = 176;
    private static final int OFFSCREEN_ENERGY_BAR_Y = 22;

    public AutoSmithingTableScreen(AutoSmithingTableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        int progress = this.menu.data.get(0);
        int maxProgress = this.menu.data.get(1);
        if (maxProgress == -1) {
            pGuiGraphics.blit(TEXTURE, this.leftPos + ELEMENT_PROGRESS_BAR_X, this.topPos + ELEMENT_PROGRESS_BAR_Y, OFFSCREEN_PROGRESS_BAR_FAILED_X, OFFSCREEN_PROGRESS_BAR_FAILED_Y, ELEMENT_PROGRESS_BAR_WIDTH, ELEMENT_PROGRESS_BAR_HEIGHT);
        }
        else {
            // TODO: Calculate width to blit
            pGuiGraphics.blit(TEXTURE, this.leftPos + ELEMENT_PROGRESS_BAR_X, this.topPos + ELEMENT_PROGRESS_BAR_Y, OFFSCREEN_PROGRESS_BAR_SUCCESS_X, OFFSCREEN_PROGRESS_BAR_SUCCESS_Y, ELEMENT_PROGRESS_BAR_WIDTH, ELEMENT_PROGRESS_BAR_HEIGHT);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        super.renderLabels(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);

        if (pX > this.leftPos + ELEMENT_PROGRESS_BAR_X && pX < this.leftPos + ELEMENT_PROGRESS_BAR_X + ELEMENT_PROGRESS_BAR_WIDTH
                && pY > this.topPos + ELEMENT_PROGRESS_BAR_Y && pY < this.topPos + ELEMENT_PROGRESS_BAR_Y + ELEMENT_PROGRESS_BAR_HEIGHT) {

            if (this.menu.data.get(1) == -1)
                pGuiGraphics.renderTooltip(this.font, Component.translatable("tooltip.autovanilla.auto_smithing_table.invalid"), pX, pY);
            else
                pGuiGraphics.renderTooltip(this.font, Component.translatable("tooltip.autovanilla.auto_smithing_table.progress", 0, 0), pX, pY);
        }

        if (pX > this.leftPos + ELEMENT_ENERGY_BAR_X && pX < this.leftPos + ELEMENT_ENERGY_BAR_X + ELEMENT_ENERGY_BAR_WIDTH
                && pY > this.topPos + ELEMENT_ENERGY_BAR_Y && pY < this.topPos + ELEMENT_ENERGY_BAR_Y + ELEMENT_ENERGY_BAR_HEIGHT) {
            pGuiGraphics.renderTooltip(this.font, Component.translatable("tooltip.autovanilla.auto_smithing_table.energy", 0, 0), pX, pY);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
    }
}
