package com.lothrazar.plaingrinder.grind;

import com.lothrazar.library.gui.TexturedProgress;
import com.lothrazar.plaingrinder.ModPlainGrinder;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class ScreenGrinder extends AbstractContainerScreen<ContainerGrinder> {

  public static final Identifier INVENTORY = Identifier.fromNamespaceAndPath(ModPlainGrinder.MODID, "textures/gui/inventory.png");
  public static final Identifier SLOT = Identifier.fromNamespaceAndPath(ModPlainGrinder.MODID, "textures/gui/slot.png");
  public static final Identifier SAW = Identifier.fromNamespaceAndPath(ModPlainGrinder.MODID, "textures/gui/saw.png");
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
  public void extractBackground(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    super.extractBackground(ms, mouseX, mouseY, partialTicks);
    this.drawBackground(ms, INVENTORY);
  }

  protected void drawBackground(GuiGraphicsExtractor ms, Identifier gui) {
    int relX = (this.width - this.imageWidth) / 2;
    int relY = (this.height - this.imageHeight) / 2;
    ms.blit(RenderPipelines.GUI_TEXTURED, gui, relX, relY, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    this.drawSlot(ms, 54, 34);
    this.drawSlot(ms, 108, 34);
  }

  protected void drawSlot(GuiGraphicsExtractor ms, int x, int y) {
    ms.blit(RenderPipelines.GUI_TEXTURED, SLOT, leftPos + x, topPos + y, 0.0F, 0.0F, size, size, size, size);
    //
    final int max = menu.tile.getMaxStage();
    progress.max = max;
    progress.draw(ms, max - menu.tile.getStage());
  }
}