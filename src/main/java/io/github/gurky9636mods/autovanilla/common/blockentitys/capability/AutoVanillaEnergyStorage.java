package io.github.gurky9636mods.autovanilla.common.blockentitys.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public class AutoVanillaEnergyStorage implements IEnergyStorage, INBTSerializable<CompoundTag> {

    protected int currentEnergy;
    protected final int maxEnergy;

    public AutoVanillaEnergyStorage(int maxStorage) {
        this.currentEnergy = 0;
        this.maxEnergy = maxStorage;
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        int accepted = Math.min(toReceive, this.maxEnergy - this.currentEnergy);
        if (!simulate) {
            this.currentEnergy += accepted;
            onChange();
        }
        return accepted;
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        int extracted = Math.min(toExtract, this.currentEnergy);
        if (!simulate) {
            this.currentEnergy -= extracted;
            onChange();
        }
        return extracted;
    }

    @Override
    public int getEnergyStored() {
        return this.currentEnergy;
    }

    @Override
    public int getMaxEnergyStored() {
        return this.maxEnergy;
    }

    @Override
    public boolean canExtract() {
        return this.currentEnergy > 0;
    }

    @Override
    public boolean canReceive() {
        return this.currentEnergy < this.maxEnergy;
    }

    @Override
    public @NotNull CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("energy", this.currentEnergy);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.currentEnergy = nbt.getInt("energy");
    }

    protected void onChange() {}
}
