package com.lothrazar.plaingrinder.grind;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerGrinder extends Container {

  public static final int PLAYERSIZE = 4 * 9;
  protected int startInv = 0;
  protected int endInv = 2;
  private TileGrinder tile;
  protected EntityPlayer playerEntity;
  protected InventoryPlayer playerInventory;

  public ContainerGrinder(InventoryPlayer inv, TileGrinder tile) {
    this.playerEntity = inv.player;
    this.playerInventory = inv;
    this.tile = tile;
    addSlotToContainer(new SlotItemHandler(tile.inputSlots, 0, 55, 35));
    addSlotToContainer(new SlotItemHandler(tile.outputSlots, 0, 109, 35));
    layoutPlayerInventorySlots(8, 84);
  }

  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {
    return true;
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    try {
      int playerStart = endInv;
      int playerEnd = endInv + PLAYERSIZE;
      ItemStack itemstack = ItemStack.EMPTY;
      Slot slot = this.inventorySlots.get(index);
      if (slot != null && slot.getHasStack()) {
        ItemStack stack = slot.getStack();
        itemstack = stack.copy();
        if (index < this.endInv) {
          if (!this.mergeItemStack(stack, playerStart, playerEnd, false)) {
            return ItemStack.EMPTY;
          }
        }
        else if (index <= playerEnd && !this.mergeItemStack(stack, startInv, endInv, false)) {
          return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
          slot.putStack(ItemStack.EMPTY);
        }
        else {
          slot.onSlotChanged();
        }
        if (stack.getCount() == itemstack.getCount()) {
          return ItemStack.EMPTY;
        }
        slot.onTake(playerIn, stack);
      }
      return itemstack;
    }
    catch (Exception e) {
      return ItemStack.EMPTY;
    }
  }

  private int addSlotRange(InventoryPlayer handler, int index, int x, int y, int amount, int dx) {
    for (int i = 0; i < amount; i++) {
      addSlotToContainer(new Slot(handler, index, x, y));
      x += dx;
      index++;
    }
    return index;
  }

  private int addSlotBox(InventoryPlayer handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
    for (int j = 0; j < verAmount; j++) {
      index = addSlotRange(handler, index, x, y, horAmount, dx);
      y += dy;
    }
    return index;
  }

  protected void layoutPlayerInventorySlots(int leftCol, int topRow) {
    // Player inventory
    addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);
    // Hotbar
    topRow += 58;
    addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
  }
}
