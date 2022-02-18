package com.lothrazar.plaingrinder.grind;

import com.lothrazar.plaingrinder.ConfigManager;
import com.lothrazar.plaingrinder.ModRegistry;
import com.lothrazar.plaingrinder.data.ItemStackHandlerWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileGrinder extends BlockEntity implements MenuProvider, Container {

  private static final int MULT_OF_MAX_STAGE_BREAKSTUFF = 4;
  public static final String NBTINV = "inv";
  ItemStackHandler inputSlots = new ItemStackHandler(1);
  ItemStackHandler outputSlots = new ItemStackHandler(1);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private int stage = 0;
  private int timer = 0;
  private int emptyHits = 0;

  public TileGrinder(BlockPos pos, BlockState state) {
    super(ModRegistry.T_GRINDER, pos, state);
  }

  private void tick() {
    timer--;
    if (timer < 0) {
      timer = 0;
    }
    //do we process
    if (canProcessOre()) {
      this.doProcess();
    }
  }

  public boolean canProcessOre() {
    return stage == ConfigManager.MAX_STAGE.get();
  }

  private void doProcess() {
    stage = 0;
    ItemStack input = this.inputSlots.getStackInSlot(0);
    if (input.isEmpty()) {
      return;
    }
    GrindRecipe currentRecipe = this.findMatchingRecipe();
    if (currentRecipe != null && this.tryProcessRecipe(currentRecipe)) {
      //we did it
      //pay all costs, RF etc
      if (level.isClientSide == false) {
        //server so process
        this.inputSlots.getStackInSlot(0).shrink(1);
        //and then insert it for real 
        this.outputSlots.insertItem(0, currentRecipe.assemble(this), false);
        //and sound on the trigger
        level.levelEvent((Player) null, 1042, worldPosition, 0);
      }
    }
  }

  private boolean tryProcessRecipe(GrindRecipe currentRecipe) {
    // ok so do the thing
    ItemStack result = currentRecipe.assemble(this);
    //does it match? does it fit into the output slot 
    //insert in simulate mode. does it fit?
    if (this.outputSlots.insertItem(0, result, true).isEmpty()) {
      return true;
    }
    return false;
  }

  private GrindRecipe findMatchingRecipe() {
    for (GrindRecipe rec : GrindRecipe.RECIPES) {
      if (rec.matches(this, level)) {
        return rec;
      }
    }
    return null;
  }

  @Override
  public void load(CompoundTag tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    stage = tag.getInt("grindstage");
    timer = tag.getInt("timer");
    emptyHits = tag.getInt("emptyHits");
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    tag.putInt("grindstage", stage);
    tag.putInt("timer", timer);
    tag.putInt("emptyHits", emptyHits);
    super.saveAdditional(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
        && ConfigManager.AUTOMATION_ALLOWED.get()) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerGrinder(i, level, worldPosition, playerInventory, playerEntity);
  }

  public void incrementGrind() {
    timer = ConfigManager.TIMER_COOLDOWN.get(); //restart to allow another rotation
    stage++;
    if (stage > ConfigManager.MAX_STAGE.get()) {
      stage = ConfigManager.MAX_STAGE.get();
    }
    if (this.inputIsEmpty()) {
      //only track empty if its breakable
      this.emptyHits++;
      if (ConfigManager.BREAKABLE_HANDLE.get() &&
          this.emptyHits > ConfigManager.MAX_STAGE.get() * MULT_OF_MAX_STAGE_BREAKSTUFF) {
        this.breakHandleAboveMe();
      }
    }
    else {
      this.emptyHits = 0;
    }
  }

  private void breakHandleAboveMe() {
    BlockState state = level.getBlockState(worldPosition.above());
    if (state.getBlock() == ModRegistry.B_HANDLE) {
      level.destroyBlock(worldPosition.above(), true);
      this.emptyHits = 0;
    }
  }

  private boolean inputIsEmpty() {
    return this.inputSlots.getStackInSlot(0).isEmpty();
  }

  public boolean canGrind() {
    return timer == 0;
  }

  /******** Fakeout stuff for IRecipe *********************/
  @Override
  public void clearContent() {
    // TODO Auto-generated method stub
  }

  @Override
  public ItemStack removeItem(int arg0, int arg1) {
    return ItemStack.EMPTY;
  }

  @Override
  public int getContainerSize() {
    return 0;
  }

  @Override
  public ItemStack getItem(int arg0) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public boolean stillValid(Player arg0) {
    return true;
  }

  @Override
  public ItemStack removeItemNoUpdate(int arg0) {
    return ItemStack.EMPTY;
  }

  @Override
  public void setItem(int arg0, ItemStack arg1) {
  }

  public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileGrinder tileGrinder) {
  }

  public static <E extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileGrinder tile) {
    tile.tick();
  }
}
