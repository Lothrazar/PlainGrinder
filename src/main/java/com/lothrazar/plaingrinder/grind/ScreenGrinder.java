package com.lothrazar.plaingrinder.grind;

import com.lothrazar.plaingrinder.ModPlainGrinder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ScreenGrinder extends AbstractContainerScreen<ContainerGrinder> {

  public static final ResourceLocation INVENTORY = new ResourceLocation(ModPlainGrinder.MODID, "textures/gui/inventory.png");
  public static final ResourceLocation SLOT = new ResourceLocation(ModPlainGrinder.MODID, "textures/gui/slot.png");
  final int size = 18;

  public ScreenGrinder(ContainerGrinder screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
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
    //    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    //    RenderSystem.setShaderTexture(0, gui);
    //    this.minecraft.getTextureManager().bindForSetup(gui);
    int relX = (this.width - this.imageWidth) / 2;
    int relY = (this.height - this.imageHeight) / 2;
    ms.blit(gui, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    this.drawSlot(ms, 54, 34);
    this.drawSlot(ms, 108, 34);
  }

  protected void drawSlot(GuiGraphics ms, int x, int y) {
    //    this.minecraft.getTextureManager().bindForSetup(SLOT);
    //    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    //    RenderSystem.setShaderTexture(0, SLOT);
    ms.blit(SLOT, leftPos + x, topPos + y, 0, 0, size, size, size, size);
  }
}