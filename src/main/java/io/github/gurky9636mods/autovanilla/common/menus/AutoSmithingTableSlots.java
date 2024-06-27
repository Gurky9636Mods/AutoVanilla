package io.github.gurky9636mods.autovanilla.common.menus;

import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class AutoSmithingTableSlots {
    public final IItemHandler slots;

    public AutoSmithingTableSlots() {
        this(new ItemStackHandler(4));
    }

    public AutoSmithingTableSlots(IItemHandler slots) {
        assert slots.getSlots() == 4 : "AutoSmithingTableSlots must have 4 slots";
        this.slots = slots;
    }
}
