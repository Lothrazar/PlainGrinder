package com.lothrazar.plaingrinder.grind;

import com.lothrazar.plaingrinder.ConfigManager;
import com.lothrazar.plaingrinder.ModRegistry;
import com.lothrazar.plaingrinder.data.ItemStackHandlerWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileGrinder extends TileEntity implements ITickable, IInventory {

  private static final int MULT_OF_MAX_STAGE_BREAKSTUFF = 4;
  public static final String NBTINV = "inv";
  public ItemStackHandler inputSlots = new ItemStackHandler(1);
  public ItemStackHandler outputSlots = new ItemStackHandler(1);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private int stage = 0;
  private int timer = 0;
  private int emptyHits = 0;

  @Override
  public void update() {
    timer--;
    if (timer < 0) {
      timer = 0;
    }
    if (canProcessOre()) {
      this.doProcess();
    }
  }

  public boolean canProcessOre() {
    return stage == ConfigManager.MAX_STAGE;
  }

  private void doProcess() {
    stage = 0;
    ItemStack input = this.inputSlots.getStackInSlot(0);
    if (input.isEmpty()) {
      return;
    }
    GrindRecipe currentRecipe = this.findMatchingRecipe(input);
    if (currentRecipe != null && this.tryProcessRecipe(currentRecipe)) {
      if (world.isRemote == false) {
        this.inputSlots.getStackInSlot(0).shrink(1);
        this.outputSlots.insertItem(0, currentRecipe.getRecipeOutput(), false);
        world.playEvent((EntityPlayer) null, 1042, pos, 0);
      }
    }
  }

  private boolean tryProcessRecipe(GrindRecipe currentRecipe) {
    ItemStack result = currentRecipe.getRecipeOutput();
    if (this.outputSlots.insertItem(0, result, true).isEmpty()) {
      return true;
    }
    return false;
  }

  private GrindRecipe findMatchingRecipe(ItemStack input) {
    for (GrindRecipe rec : GrindRecipe.RECIPES) {
      if (rec.matches(input)) {
        return rec;
      }
    }
    return null;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    inventory.deserializeNBT(tag.getCompoundTag(NBTINV));
    stage = tag.getInteger("grindstage");
    timer = tag.getInteger("timer");
    emptyHits = tag.getInteger("emptyHits");
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    tag.setTag(NBTINV, inventory.serializeNBT());
    tag.setInteger("grindstage", stage);
    tag.setInteger("timer", timer);
    tag.setInteger("emptyHits", emptyHits);
    return tag;
  }

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
        && ConfigManager.AUTOMATION_ALLOWED) {
      return true;
    }
    return super.hasCapability(capability, facing);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
        && ConfigManager.AUTOMATION_ALLOWED) {
      return (T) inventory;
    }
    return super.getCapability(capability, facing);
  }

  public IItemHandler getInventoryHandler() {
    return inventory;
  }

  public void incrementGrind() {
    timer = ConfigManager.TIMER_COOLDOWN; //restart to allow another rotation
    stage++;
    if (stage > ConfigManager.MAX_STAGE) {
      stage = ConfigManager.MAX_STAGE;
    }
    if (this.inputIsEmpty()) {
      this.emptyHits++;
      if (ConfigManager.BREAKABLE_HANDLE &&
          this.emptyHits > ConfigManager.MAX_STAGE * MULT_OF_MAX_STAGE_BREAKSTUFF) {
        this.breakHandleAboveMe();
      }
    }
    else {
      this.emptyHits = 0;
    }
  }

  private void breakHandleAboveMe() {
    IBlockState state = world.getBlockState(pos.up());
    if (state.getBlock() == ModRegistry.B_HANDLE) {
      world.destroyBlock(pos.up(), true);
      this.emptyHits = 0;
    }
  }

  private boolean inputIsEmpty() {
    return this.inputSlots.getStackInSlot(0).isEmpty();
  }

  public boolean canGrind() {
    return timer == 0;
  }

  /******** Fakeout stuff for IInventory *********************/
  @Override
  public int getSizeInventory() {
    return 0;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public ItemStack getStackInSlot(int index) {
    return ItemStack.EMPTY;
  }

  @Override
  public ItemStack decrStackSize(int index, int count) {
    return ItemStack.EMPTY;
  }

  @Override
  public ItemStack removeStackFromSlot(int index) {
    return ItemStack.EMPTY;
  }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {}

  @Override
  public int getInventoryStackLimit() {
    return 64;
  }

  @Override
  public boolean isUsableByPlayer(EntityPlayer player) {
    return true;
  }

  @Override
  public void openInventory(EntityPlayer player) {}

  @Override
  public void closeInventory(EntityPlayer player) {}

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return false;
  }

  @Override
  public int getField(int id) {
    return 0;
  }

  @Override
  public void setField(int id, int value) {}

  @Override
  public int getFieldCount() {
    return 0;
  }

  @Override
  public void clear() {}

  @Override
  public String getName() {
    return getDisplayName().getFormattedText();
  }

  @Override
  public boolean hasCustomName() {
    return false;
  }

  public ITextComponent getDisplayName() {
    return new TextComponentString("grinder");
  }
}
