package io.github.gurky9636mods.autovanilla.common.menus;

import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class AutoSmithingTableData {

    public final IItemHandler slots;

    public AutoSmithingTableData() {
        this(new ItemStackHandler(4));
    }

    public AutoSmithingTableData(IItemHandler slots) {
        assert slots.getSlots() == 4 : "AutoSmithingTableSlots must have 4 slots";
        this.slots = slots;
    }
}
