package com.lothrazar.plaingrinder.data;

import com.lothrazar.plaingrinder.ModRegistry;
import com.lothrazar.plaingrinder.grind.TileGrinder;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GrindEvents {

  @SubscribeEvent
  public void onHit(PlayerInteractEvent.RightClickBlock event) {
    EntityPlayer player = event.getEntityPlayer();
    ItemStack held = player.getHeldItem(event.getHand());
    World world = player.getEntityWorld();
    if (!held.isEmpty() || event.getHand() == EnumHand.OFF_HAND) {
      return;
    }
    BlockPos pos = event.getPos();
    IBlockState state = world.getBlockState(pos);
    if (state.getBlock() == ModRegistry.B_HANDLE) {
      IBlockState below = world.getBlockState(pos.down());
      if (below.getBlock() == ModRegistry.B_GRINDER) {
        TileGrinder tile = (TileGrinder) world.getTileEntity(pos.down());
        if (tile.canGrind()) {
          if (world.isRemote == false) {
            EnumFacing old = state.getValue(BlockHorizontal.FACING);
            world.setBlockState(pos, state.withProperty(BlockHorizontal.FACING, old.rotateYCCW()));
            tile.incrementGrind();
          }
          player.swingArm(event.getHand());
        }
      }
    }
  }
}
