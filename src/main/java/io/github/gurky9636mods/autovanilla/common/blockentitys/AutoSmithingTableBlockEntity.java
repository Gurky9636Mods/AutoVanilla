package io.github.gurky9636mods.autovanilla.common.blockentitys;

import com.mojang.logging.LogUtils;
import io.github.gurky9636mods.autovanilla.Config;
import io.github.gurky9636mods.autovanilla.common.blockentitys.capability.AutoVanillaEnergyStorage;
import io.github.gurky9636mods.autovanilla.common.recipes.AutoSmithingInput;
import io.github.gurky9636mods.autovanilla.common.recipes.AutoSmithingTransformRecipe;
import io.github.gurky9636mods.autovanilla.common.recipes.AutoVanillaRecipes;
import io.github.gurky9636mods.autovanilla.common.utils.SingleSlotItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Optional;

public class AutoSmithingTableBlockEntity extends BlockEntity implements ContainerData {

    public static final Logger LOGGER = LogUtils.getLogger();

    // Input, Template, Addition, Output
    private int progress = 0;
    private int maxProgress = -1;
    public final AutoVanillaEnergyStorage energyCap;
    public final ItemStackHandler itemHandler;
    private AutoSmithingTransformRecipe currentRecipe;

    public AutoSmithingTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(AutoVanillaBlockEntities.AUTO_SMITHING_TABLE.get(), pPos, pBlockState);

        this.itemHandler = new ItemStackHandler(4);
        this.energyCap = new AutoVanillaEnergyStorage(Config.maxAutoSmithingTableEnergy);
        this.currentRecipe = null;
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    private Optional<AutoSmithingTransformRecipe> getRecipe() {
        assert this.level != null;
        var input = new AutoSmithingInput(
                this.itemHandler.getStackInSlot(0),
                this.itemHandler.getStackInSlot(1),
                this.itemHandler.getStackInSlot(2)
        );
        var autoRecipe = this.level.getRecipeManager().getRecipeFor(AutoVanillaRecipes.AUTO_SMITHING.get(), input, this.level);
        if (autoRecipe.isPresent())
            return autoRecipe.map(RecipeHolder::value);
        if (!Config.convertVanillaRecipes) return Optional.empty();

        var vanillaInput = input.toVanilla();
        var vanillaRecipe = this.level.getRecipeManager().getRecipeFor(RecipeType.SMITHING, vanillaInput, this.level);
        if (vanillaRecipe.isEmpty()) return Optional.empty();
        if (vanillaRecipe.get().value() instanceof SmithingTransformRecipe recipe)
            return Optional.of(AutoSmithingTransformRecipe.fromVanilla(recipe, this.level.registryAccess()));
        return Optional.empty();
    }

    public void tick() {
        if (this.currentRecipe == null) {
            var recipe = this.getRecipe();
            if (recipe.isEmpty()) {
                this.maxProgress = -1;
                return;
            }
            this.currentRecipe = recipe.get();
            this.maxProgress = 20; // TODO: Config and Custom Smithing Recipe
            this.progress = 0;
            this.update();
        }

        if (this.progress >= this.maxProgress) {
            assert this.maxProgress != -1;
            var output = this.currentRecipe.getResultItem(this.level.registryAccess()).copy();
            if (this.itemHandler.insertItem(3, output, true).isEmpty()) {
                this.itemHandler.insertItem(3, output, false);
                this.itemHandler.extractItem(0, 1, false);
                this.itemHandler.extractItem(1, 1, false);
                this.itemHandler.extractItem(2, 1, false);
                this.currentRecipe = null;
                this.progress = 0;
                this.maxProgress = -1;
                this.update();
            }
            // Otherwise keep trying to put the item
        } else {
            this.progress++;
        }
    }

    public IItemHandler getCapabilityForSide(Direction dir)
    {
        switch (dir)
        {
            case null:
                return this.itemHandler;
            case UP:
                return new SingleSlotItemStackHandler(this.itemHandler, 0);
            case DOWN:
                return new SingleSlotItemStackHandler(this.itemHandler, 3);
            default:
                break;
        }
        var currentDir = this.getBlockState().getValue(HorizontalDirectionalBlock.FACING);

        // Left: Addition
        if (dir == currentDir.getClockWise())
        {
            return new SingleSlotItemStackHandler(this.itemHandler, 1);
        }

        // Right: Template
        if (dir == currentDir.getCounterClockWise())
        {
            return new SingleSlotItemStackHandler(this.itemHandler, 2);
        }

        return null;
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
    public void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put("autovanilla.Items", this.itemHandler.serializeNBT(pRegistries));
        pTag.putInt("autovanilla.Progress", progress);
        // Max progress can be generated from the items
        pTag.put("autovanilla.Energy", this.energyCap.serializeNBT(pRegistries));
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        this.itemHandler.deserializeNBT(pRegistries, pTag.getCompound("autovanilla.Items"));
        this.progress = pTag.getInt("autovanilla.Progress");
        this.energyCap.deserializeNBT(pRegistries, pTag.getCompound("autovanilla.Energy"));
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
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

    public static final int DATA_COUNT = 4;

    @Override
    public int getCount() {
        // 0: Current progress
        // 1: Max progress
        // 2: Energy stored
        // 3: Max energy
        return DATA_COUNT;
    }

    public void update()
    {
        this.setChanged();
        this.requestModelDataUpdate();
    }
}
