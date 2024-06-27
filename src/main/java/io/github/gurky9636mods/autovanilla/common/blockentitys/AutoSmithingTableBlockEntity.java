package io.github.gurky9636mods.autovanilla.common.blockentitys;

import com.mojang.logging.LogUtils;
import io.github.gurky9636mods.autovanilla.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class AutoSmithingTableBlockEntity extends BlockEntity implements Container, ContainerData {

    public static final Logger LOGGER = LogUtils.getLogger();

    // Input, Template, Addition, Output
    private NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
    private int progress;
    private int maxProgress;
    IEnergyStorage energyCap;

    public AutoSmithingTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(AutoVanillaBlockEntities.AUTO_SMITHING_TABLE.get(), pPos, pBlockState);
    }



    @Override
    public void onLoad() {
        super.onLoad();

        assert level != null;
        this.energyCap = level.getCapability(Capabilities.EnergyStorage.BLOCK, worldPosition, null);
        if (this.energyCap == null) {
            LOGGER.error("Energy capability is null for AutoSmithingTableBlockEntity at {}", worldPosition);
        }
    }

    public void tick() {
    }

    @Override
    public void invalidateCapabilities() {
        super.invalidateCapabilities();

        assert level != null;
        level.invalidateCapabilities(worldPosition);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = super.getUpdateTag(pRegistries);

        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        ContainerHelper.saveAllItems(pTag, this.items, pRegistries);
        pTag.putInt("progress", progress);
        // Max progress can be generated from the items
        //pTag.put("energy", this.energyCap.serializeNBT(null));
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        this.items = NonNullList.withSize(3, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, this.items, pRegistries);
        this.progress = pTag.getInt("progress");
        //this.energyCap.deserializeNBT(null, pTag.getCompound("energy"));
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return items.get(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        if (pSlot != 2) return ItemStack.EMPTY;
        return ContainerHelper.removeItem(items, pSlot, pAmount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return ContainerHelper.takeItem(items, pSlot);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        items.set(pSlot, pStack);
        pStack.limitSize(this.getMaxStackSize(pStack));
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public int get(int pIndex) {
        return switch (pIndex) {
            case 0 -> this.progress;
            case 1 -> this.maxProgress;
            case 2 -> this.energyCap.getEnergyStored();
            case 3 -> this.energyCap.getMaxEnergyStored();
            default -> throw new IllegalArgumentException("Unknown index: " + pIndex);
        };
    }

    @Override
    public void set(int pIndex, int pValue) {
        switch (pIndex) {
            case 0 -> this.progress = pValue;
            case 1 -> this.maxProgress = pValue;
            case 2 -> {
                if (this.energyCap.getEnergyStored() > pValue) {
                    this.energyCap.extractEnergy(this.energyCap.getEnergyStored() - pValue, false);
                } else {
                    this.energyCap.receiveEnergy(this.energyCap.getEnergyStored() - pValue, false);
                }
            }
            case 3 -> throw new IllegalArgumentException("Cannot set max energy values");
            default -> throw new IllegalArgumentException("Unknown index: " + pIndex);
        }
    }

    @Override
    public int getCount() {
        // 0: Current progress
        // 1: Max progress
        // 2: Energy stored
        // 3: Max energy
        return 4;
    }
}
