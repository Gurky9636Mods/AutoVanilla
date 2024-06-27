package io.github.gurky9636mods.autovanilla.common.menus;

import io.github.gurky9636mods.autovanilla.common.blockentitys.AutoSmithingTableBlockEntity;
import io.github.gurky9636mods.autovanilla.common.blocks.AutoSmithingTableBlock;
import io.github.gurky9636mods.autovanilla.common.blocks.AutoVanillaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class AutoSmithingTableMenu extends AbstractContainerMenu {

    public static final int INVENTORY_SLOTS_START = 0;
    public static final int INVENTORY_SLOTS_END = 26;

    public static final int HOTBAR_SLOTS_START = 27;
    public static final int HOTBAR_SLOTS_END = 35;

    public static final int BASE_SLOTS_START = 36;
    public static final int BASE_SLOTS_END = 36;

    public static final int ADDITION_SLOTS_START = 37;
    public static final int ADDITION_SLOTS_END = 37;

    private ContainerLevelAccess levelAccess;

    // Client constructor
    public AutoSmithingTableMenu(int pContainerId, Inventory playerInventory) {
        this(pContainerId, playerInventory, ContainerLevelAccess.NULL, new AutoSmithingTableSlots());
    }

    public AutoSmithingTableMenu(int pContainerId, Inventory playerInventory, ContainerLevelAccess pLevelAccess, AutoSmithingTableSlots slots)
    {
        super(AutoVanillaMenus.AUTO_SMITHING_TABLE.get(), pContainerId);
        this.levelAccess = pLevelAccess;

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.addSlot(new SlotItemHandler(slots.slots, 0, 82, 10));
        this.addSlot(new SlotItemHandler(slots.slots, 1, 59, 32));
        this.addSlot(new SlotItemHandler(slots.slots,  2, 82, 55));
        this.addSlot(new SlotItemHandler(slots.slots,  3, 132, 32) {
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                return false;
            }
        });
    }

    public static MenuConstructor getServerConstructor(AutoSmithingTableBlockEntity blockEntity, BlockPos pos)
    {
        return (id, playerInv, player) -> new AutoSmithingTableMenu(id, playerInv, ContainerLevelAccess.create(player.level(), pos), new AutoSmithingTableSlots((IItemHandler) blockEntity));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack returnStack = ItemStack.EMPTY;

        final Slot slot = getSlot(index);
        if (slot.hasItem()) {
            final ItemStack item = slot.getItem();
            returnStack = item.copy();
            if (index <= 35) {
                // Check for slots in Container first
                //moveItemToContainer(item);

                // Inventory slots
                if (index <= INVENTORY_SLOTS_END) {
                    if (!moveItemStackTo(item, HOTBAR_SLOTS_START, HOTBAR_SLOTS_END + 1, true))
                        return ItemStack.EMPTY;
                }
                else {
                    if (!moveItemStackTo(item, INVENTORY_SLOTS_START, INVENTORY_SLOTS_END + 1, false))
                        return ItemStack.EMPTY;
                }
            }
            else { // From Container
                if (!moveItemStackTo(item, INVENTORY_SLOTS_START, HOTBAR_SLOTS_END + 1, true))
                    return ItemStack.EMPTY;
            }

            if (item.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return returnStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return AbstractContainerMenu.stillValid(this.levelAccess, pPlayer, AutoVanillaBlocks.AUTO_SMITHING_TABLE.get());
    }
}
