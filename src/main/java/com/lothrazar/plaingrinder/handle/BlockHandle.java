package com.lothrazar.plaingrinder.handle;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHandle extends Block {

  private static final double BOUNDS = 6;
  private static final AxisAlignedBB AABB = new AxisAlignedBB(
      BOUNDS / 16, 0, BOUNDS / 16,
      1 - BOUNDS / 16, 12D / 16, 1 - BOUNDS / 16);

  public BlockHandle(Material material) {
    super(material);
    this.setHardness(0.4F);
  }

  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    return AABB;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    ITextComponent t = new TextComponentTranslation(getTranslationKey() + ".tooltip");
    tooltip.add(TextFormatting.GRAY + t.getFormattedText());
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
    if (entity != null) {
      world.setBlockState(pos, state.withProperty(BlockHorizontal.FACING, getFacingFromEntity(pos, entity)), 2);
    }
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, BlockHorizontal.FACING);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.byHorizontalIndex(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(BlockHorizontal.FACING).getHorizontalIndex();
  }

  public static EnumFacing getFacingFromEntity(BlockPos clickedBlock, EntityLivingBase entity) {
    EnumFacing d = EnumFacing.getFacingFromVector(
        (float) (entity.posX - clickedBlock.getX()),
        (float) (entity.posY - clickedBlock.getY()),
        (float) (entity.posZ - clickedBlock.getZ()));
    if (d == EnumFacing.UP || d == EnumFacing.DOWN) {
      return entity.getHorizontalFacing().getOpposite();
    }
    return d;
  }
}
