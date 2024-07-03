package io.github.gurky9636mods.autovanilla.common.menus;

import io.github.gurky9636mods.autovanilla.common.blockentitys.AutoSmithingTableBlockEntity;
import io.github.gurky9636mods.autovanilla.common.blocks.AutoVanillaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class AutoSmithingTableMenu extends AbstractContainerMenu {

    public static final int INVENTORY_SLOTS_START = 0;
    public static final int INVENTORY_SLOTS_END = 26;

    public static final int HOTBAR_SLOTS_START = 27;
    public static final int HOTBAR_SLOTS_END = 35;

    private final ContainerLevelAccess levelAccess;
    public final ContainerData data;

    // Client constructor
    public AutoSmithingTableMenu(int pContainerId, Inventory playerInventory) {
        this(pContainerId, playerInventory, ContainerLevelAccess.NULL, new ItemStackHandler(4), new SimpleContainerData(AutoSmithingTableBlockEntity.DATA_COUNT));
    }

    public AutoSmithingTableMenu(int pContainerId, Inventory playerInventory, ContainerLevelAccess pLevelAccess, IItemHandler itemHandler, ContainerData containerData)
    {
        super(AutoVanillaMenus.AUTO_SMITHING_TABLE.get(), pContainerId);
        this.levelAccess = pLevelAccess;
        this.data = containerData;

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.addSlot(new SlotItemHandler(itemHandler, 0, 29, 40));
        this.addSlot(new SlotItemHandler(itemHandler, 1, 64, 40));
        this.addSlot(new SlotItemHandler(itemHandler,  2, 84, 40));
        this.addSlot(new SlotItemHandler(itemHandler,  3, 132, 40) {
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                return false;
            }
        });

        this.addDataSlots(containerData);
    }

    public static AutoSmithingTableMenu constructOnServer(int containerId, Inventory playerInv, Player player, BlockPos pos, IItemHandler itemHandler, ContainerData containerData)
    {
        return new AutoSmithingTableMenu(
                containerId,
                playerInv,
                ContainerLevelAccess.create(player.level(), pos),
                itemHandler,
                containerData
                );
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
