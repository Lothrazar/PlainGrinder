package com.lothrazar.plaingrinder.grind;

import com.lothrazar.library.gui.TexturedProgress;
import com.lothrazar.plaingrinder.ModPlainGrinder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ScreenGrinder extends AbstractContainerScreen<ContainerGrinder> {

  public static final ResourceLocation INVENTORY = new ResourceLocation(ModPlainGrinder.MODID, "textures/gui/inventory.png");
  public static final ResourceLocation SLOT = new ResourceLocation(ModPlainGrinder.MODID, "textures/gui/slot.png");
  public static final ResourceLocation SAW = new ResourceLocation(ModPlainGrinder.MODID, "textures/gui/saw.png");
  private TexturedProgress progress;
  final int size = 18;

  public ScreenGrinder(ContainerGrinder screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    this.progress = new TexturedProgress(this.font, 83, 36, SAW);
    progress.guiLeft = leftPos;
    progress.guiTop = topPos;
  }

  @Override
  protected void renderBg(GuiGraphics ms, float partialTicks, int x, int y) {
    //    super.drawGuiContainerForegroundLayer(ms, x, y);
    this.drawBackground(ms, INVENTORY);
  }

  @Override
  public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
  }

  protected void drawBackground(GuiGraphics ms, ResourceLocation gui) {
    int relX = (this.width - this.imageWidth) / 2;
    int relY = (this.height - this.imageHeight) / 2;
    ms.blit(gui, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    this.drawSlot(ms, 54, 34);
    this.drawSlot(ms, 108, 34);
  }

  protected void drawSlot(GuiGraphics ms, int x, int y) {
    ms.blit(SLOT, leftPos + x, topPos + y, 0, 0, size, size, size, size);
    //
    final int max = menu.tile.getMaxStage();
    progress.max = max;
    progress.draw(ms, max - menu.tile.getStage());
  }
}