package com.lothrazar.plaingrinder.grind;

import java.util.List;

import com.lothrazar.library.block.EntityBlockFlib;
import com.lothrazar.plaingrinder.RegistryGrinder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlockGrinder extends EntityBlockFlib {

  public BlockGrinder(Properties properties) {
    super(properties);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new BlockEntityGrinder(pos, state);
  }

  /**
   * used by comparator
   */
  @Override
  public boolean hasAnalogOutputSignal(BlockState bs) {
    return true;
  }

  /**
   * used by comparator
   */
  @Override
  public int getAnalogOutputSignal(BlockState bs, Level level, BlockPos pos, Direction direction) {
    return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
  }

  @Override
  public RenderShape getRenderShape(BlockState bs) {
    return RenderShape.MODEL;
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, RegistryGrinder.TE_GRINDER.get(), world.isClientSide() ? BlockEntityGrinder::clientTick : BlockEntityGrinder::serverTick);
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
    if (!world.isClientSide()) {
      BlockEntity tileEntity = world.getBlockEntity(pos);
      if (tileEntity instanceof MenuProvider) {
        ((ServerPlayer) player).openMenu((MenuProvider) tileEntity, buf -> buf.writeBlockPos(tileEntity.getBlockPos()));
      }
      else {
        throw new IllegalStateException("Our named container provider is missing!");
      }
    }
    return InteractionResult.SUCCESS;
  }

  // dropping inputSlots/outputSlots contents is now automatic: BlockEntity#preRemoveSideEffects
  // drops everything in a BlockEntity that implements Container by default, and BlockEntityGrinder
  // already does. Only the comparator update still needs a manual hook, via this replacement for onRemove.
  @Override
  protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
    level.updateNeighbourForOutputSignal(pos, this);
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
    tooltip.add(Component.translatable(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
  }
}
