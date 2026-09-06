package com.lothrazar.plaingrinder.data;

import com.lothrazar.plaingrinder.RegistryGrinder;
import com.lothrazar.plaingrinder.grind.BlockEntityGrinder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class GrindEvents {

  @SubscribeEvent
  public void onHit(PlayerInteractEvent.RightClickBlock event) {
    if (event.getHand() == InteractionHand.OFF_HAND) {

      return;
    }
    BlockPos pos = event.getPos();
    Player player = event.getEntity();
    Level world = player.level();
    BlockState state = world.getBlockState(pos);
    if (state.getBlock() == RegistryGrinder.handle.get()) {
      BlockState below = world.getBlockState(pos.below());
      if (below.getBlock() == RegistryGrinder.GRINDER.get()) {
        //do the thing
        BlockEntityGrinder tile = (BlockEntityGrinder) world.getBlockEntity(pos.below());
        if (tile != null && tile.canGrind()) {
          // and state
          if (world.isClientSide() == false) {
            Direction old = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.HORIZONTAL_FACING, old.getCounterClockWise()));
            tile.incrementGrind();
          }
          player.swing(event.getHand());
          // the spin is the interaction - stop vanilla from also placing/using the held item
          event.setCancellationResult(InteractionResult.CONSUME);
          event.setCanceled(true);
        }
      }
    }
  }
}