package com.lothrazar.plaingrinder.grind;

import com.lothrazar.plaingrinder.ConfigPlainGrinder;
import com.lothrazar.plaingrinder.RegistryGrinder;
import com.lothrazar.plaingrinder.data.ItemStackHandlerWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class BlockEntityGrinder extends BlockEntity implements MenuProvider, Container {

  private static final int MULT_OF_MAX_STAGE_BREAKSTUFF = 4;
  public static final String NBTINV = "inv";
  ItemStackHandler inputSlots = new ItemStackHandler(1);
  ItemStackHandler outputSlots = new ItemStackHandler(1);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private int stage = 0;
  private int timer = 0;
  private int emptyHits = 0;

  public BlockEntityGrinder(BlockPos pos, BlockState state) {
    super(RegistryGrinder.TE_GRINDER.get(), pos, state);
  }

  public IItemHandler getInventory() {
    return inventory;
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
    return stage == getMaxStage();
  }

  public Integer getMaxStage() {
    return ConfigPlainGrinder.MAX_STAGE.get();
  }

  public int getStage() {
    return stage;
  }

  private void doProcess() {
    stage = 0;
    ItemStack inputItem = this.inputSlots.getStackInSlot(0);
    if (inputItem.isEmpty()) {
      return;
    }
    GrindRecipe currentRecipe = this.findMatchingRecipe();
    SingleRecipeInput recipeInput = new SingleRecipeInput(inputItem);
    if (currentRecipe != null && this.tryProcessRecipe(currentRecipe, recipeInput)) {
      //we did it
      //pay all costs, RF etc
      if (level.isClientSide() == false) {
        //server so process
        this.inputSlots.getStackInSlot(0).shrink(1);
        //and then insert it for real
        this.outputSlots.insertItem(0, currentRecipe.assemble(recipeInput), false);
        //and sound on the trigger
        level.levelEvent((Player) null, 1042, worldPosition, 0);
        // update comparator outputs
        level.updateNeighbourForOutputSignal(worldPosition, getBlock());
      }
    }
  }

  Block getBlock() {
    return RegistryGrinder.GRINDER.get();
  }

  private boolean tryProcessRecipe(GrindRecipe currentRecipe, SingleRecipeInput recipeInput) {
    // ok so do the thing
    ItemStack result = currentRecipe.assemble(recipeInput);
    //does it match? does it fit into the output slot
    //insert in simulate mode. does it fit?
    if (this.outputSlots.insertItem(0, result, true).isEmpty()) {
      return true;
    }
    return false;
  }

  private GrindRecipe findMatchingRecipe() {
    SingleRecipeInput recipeInput = new SingleRecipeInput(this.inputSlots.getStackInSlot(0));
    return ((ServerLevel) level).recipeAccess()
        .getRecipeFor(RegistryGrinder.GRINDER_RECIPE_TYPE.get(), recipeInput, level)
        .map(RecipeHolder::value)
        .orElse(null);
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    inventory.deserialize(input.childOrEmpty(NBTINV));
    stage = input.getIntOr("grindstage", 0);
    timer = input.getIntOr("timer", 0);
    emptyHits = input.getIntOr("emptyHits", 0);
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    inventory.serialize(output.child(NBTINV));
    output.putInt("grindstage", stage);
    output.putInt("timer", timer);
    output.putInt("emptyHits", emptyHits);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("block.plaingrinder.grinder");
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerGrinder(i, playerInventory, this);
  }

  public void incrementGrind() {
    timer = ConfigPlainGrinder.TIMER_COOLDOWN.get(); //restart to allow another rotation
    stage++;
    if (stage > getMaxStage()) {
      stage = getMaxStage();
    }
    if (this.inputIsEmpty()) {
      //only track empty if its breakable
      this.emptyHits++;
      if (ConfigPlainGrinder.BREAKABLE_HANDLE.get() &&
          this.emptyHits > getMaxStage() * MULT_OF_MAX_STAGE_BREAKSTUFF) {
        this.breakHandleAboveMe();
      }
    }
    else {
      this.emptyHits = 0;
    }
  }

  private void breakHandleAboveMe() {
    BlockState state = level.getBlockState(worldPosition.above());
    if (state.getBlock() == RegistryGrinder.handle.get()) {
      level.destroyBlock(worldPosition.above(), true);
      this.emptyHits = 0;
    }
  }

  public boolean inputIsEmpty() {
    return this.inputSlots.getStackInSlot(0).isEmpty();
  }

  public boolean outputIsEmpty() {
    return this.outputSlots.getStackInSlot(0).isEmpty();
  }

  public boolean canGrind() {
    return timer == 0;
  }

  @Override
  public ItemStack removeItem(int arg0, int arg1) {
    return inventory.extractItem(arg0, arg1, false);
  }

  @Override
  public int getContainerSize() {
    return this.inputSlots.getSlots() + this.outputSlots.getSlots();
  }

  @Override
  public ItemStack getItem(int arg0) {
    return inventory.getStackInSlot(arg0);
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public boolean stillValid(Player player) {
    return player.isAlive();
  }

  @Override
  public ItemStack removeItemNoUpdate(int arg0) {
    return ItemStack.EMPTY;
  }

  @Override
  public void setItem(int arg0, ItemStack arg1) {}

  public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntityGrinder tileGrinder) {}

  public static <E extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntityGrinder tile) {
    tile.tick();
  }

  @Override
  public void clearContent() {
    // TODO Auto-generated method stub
  }

  // used on clientside to set value when server syncs in
  public void setStage(int value) {
    this.stage = value;
  }
}
