package com.lothrazar.plaingrinder.grind;

import com.lothrazar.plaingrinder.ModMain;
import com.lothrazar.plaingrinder.ModRegistry;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiGrinder extends GuiContainer {

  public static final ResourceLocation INVENTORY = new ResourceLocation(ModMain.MODID, "textures/gui/inventory.png");
  public static final ResourceLocation SLOT = new ResourceLocation(ModMain.MODID, "textures/gui/slot.png");
  private static final int SLOT_SIZE = 18;

  public GuiGrinder(ContainerGrinder container, InventoryPlayer inv) {
    super(container);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(INVENTORY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    String title = I18n.format(ModRegistry.B_GRINDER.getTranslationKey() + ".name");
    this.fontRenderer.drawString(title, this.xSize / 2 - this.fontRenderer.getStringWidth(title) / 2, 6, 4210752);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  protected void drawBackground(ResourceLocation gui) {
    this.mc.getTextureManager().bindTexture(gui);
    int relX = (this.width - this.xSize) / 2;
    int relY = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(relX, relY, 0, 0, this.xSize, this.ySize);
    this.drawSlot(54, 34);
    this.drawSlot(108, 34);
  }

  protected void drawSlot(int x, int y) {
    this.mc.getTextureManager().bindTexture(SLOT);
    Gui.drawModalRectWithCustomSizedTexture(guiLeft + x, guiTop + y, 0, 0, SLOT_SIZE, SLOT_SIZE, SLOT_SIZE, SLOT_SIZE);
  }
}
