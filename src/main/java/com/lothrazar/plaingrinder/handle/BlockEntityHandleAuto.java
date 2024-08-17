package com.lothrazar.plaingrinder.handle;

import com.lothrazar.plaingrinder.RegistryGrinder;
import com.lothrazar.plaingrinder.grind.BlockEntityGrinder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BlockEntityHandleAuto extends BlockEntity {

  private int timer;

  public BlockEntityHandleAuto(BlockPos pos, BlockState state) {
    super(RegistryGrinder.TE_HANDLE.get(), pos, state);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    timer = tag.getInt("timer");
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putInt("timer", timer);
  }

  public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntityHandleAuto tileGrinder) {}

  public static <E extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntityHandleAuto tile) {
    tile.tick();
  }

  //the main gametick method for this block
  private void tick() {
    tickDownTimer();
    if (canProcessOre()) {
      processRotation();
      resetTimer();
    }
  }

  private boolean canProcessOre() {
    return timer == 0;
  }

  private void tickDownTimer() {
    this.timer--;
    if (timer < 0) {
      this.resetTimer();
    }
  }

  private void processRotation() {
    var world = this.getLevel();
    var belowPos = this.worldPosition.below();
    BlockState below = world.getBlockState(belowPos);
    if (below.getBlock() == RegistryGrinder.GRINDER.get()) {
      BlockEntityGrinder tile = (BlockEntityGrinder) world.getBlockEntity(belowPos);
      if (tile.canGrind() && !tile.getItem(0).isEmpty()) {
        //rotate and then grind
        Direction old = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        world.setBlockAndUpdate(this.worldPosition, this.getBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, old.getCounterClockWise()));
        tile.incrementGrind();
      }
    }
  }

  private void resetTimer() {
    this.timer = 20;
  }
}
