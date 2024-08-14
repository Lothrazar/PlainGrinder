package com.lothrazar.plaingrinder.handle;

import javax.annotation.Nullable;
import com.lothrazar.plaingrinder.RegistryGrinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockHandleAuto extends BlockHandle implements EntityBlock {

  public BlockHandleAuto(Properties properties) {
    super(properties);
  }

  @Override
  public boolean triggerEvent(BlockState p_49226_, Level p_49227_, BlockPos p_49228_, int p_49229_, int p_49230_) {
    super.triggerEvent(p_49226_, p_49227_, p_49228_, p_49229_, p_49230_);
    BlockEntity blockentity = p_49227_.getBlockEntity(p_49228_);
    return blockentity == null ? false : blockentity.triggerEvent(p_49229_, p_49230_);
  }

  // cross impl from BaseEntityBlock
  @Nullable
  protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> p_152133_, BlockEntityType<E> p_152134_, BlockEntityTicker<? super E> p_152135_) {
    return p_152134_ == p_152133_ ? (BlockEntityTicker<A>) p_152135_ : null;
  }

  //cross impl from BaseEntityBlock
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, RegistryGrinder.TE_HANDLE.get(), world.isClientSide ? BlockEntityHandleAuto::clientTick : BlockEntityHandleAuto::serverTick);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new BlockEntityHandleAuto(pos, state);
  }
}
