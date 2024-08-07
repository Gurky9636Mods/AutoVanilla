package io.github.gurky9636mods.autovanilla.common.utils;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class SingleSlotItemStackHandler implements IItemHandler {
    protected final IItemHandler handler;
    protected final int slot;
    public SingleSlotItemStackHandler(IItemHandler handler, int slot) {
        this.handler = handler;
        this.slot = slot;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        assert slot == 0;
        return handler.getStackInSlot(this.slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        assert slot == 0;
        return handler.insertItem(this.slot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        assert slot == 0;
        return handler.extractItem(this.slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        assert slot == 0;
        return handler.getSlotLimit(this.slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        assert slot == 0;
        return handler.isItemValid(this.slot, stack);
    }
}
