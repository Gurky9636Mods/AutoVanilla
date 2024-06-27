package io.github.gurky9636mods.autovanilla.common.blocks;

import com.mojang.serialization.MapCodec;
import io.github.gurky9636mods.autovanilla.common.blockentitys.AutoSmithingTableBlockEntity;
import io.github.gurky9636mods.autovanilla.common.blockentitys.AutoVanillaBlockEntities;
import io.github.gurky9636mods.autovanilla.common.menus.AutoSmithingTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class AutoSmithingTableBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public AutoSmithingTableBlock(Properties properties) {
        super(properties);

        registerDefaultState(getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    public InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pPlayer instanceof ServerPlayer player) {
            openMenu(player);
        }
        return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        var result = super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
        if (result.consumesAction()) return result; // Otherwise, we open the screen
        if (pPlayer instanceof ServerPlayer player) {
            openMenu(player);
        }
        return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    private void openMenu(ServerPlayer player)
    {
        player.openMenu(new SimpleMenuProvider(
                (pContainerId, pPlayerInventory, pPlayer) -> new AutoSmithingTableMenu(pContainerId, pPlayerInventory),
                Component.translatable("menu.title.autovanilla.auto_smithing_table")
        ));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new AutoSmithingTableBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pBlockEntityType != AutoVanillaBlockEntities.AUTO_SMITHING_TABLE.get())
            return null;
        return (level, pos, state, blockEntity) -> {
            // Sanity check
            if (blockEntity instanceof AutoSmithingTableBlockEntity entity) {
                entity.tick();
            }
        };
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return AutoVanillaBlocks.AUTO_SMITHING_TABLE_CODEC.get();
    }
}
