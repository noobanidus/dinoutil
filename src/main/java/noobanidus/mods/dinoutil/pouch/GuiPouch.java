/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package noobanidus.mods.dinoutil.pouch;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import noobanidus.mods.dinoutil.DinoUtil;

import javax.annotation.Nonnull;

public class GuiPouch extends GuiContainer {

  private ContainerPouch containerPouch;

  public GuiPouch(@Nonnull ContainerPouch containerPouch) {
    super(containerPouch);
    this.containerPouch = containerPouch;
    xSize = 176;
    ySize = 168;
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    ResourceLocation rl = new ResourceLocation(DinoUtil.MODID, "textures/gui/pouch_gui.png");
    this.mc.getTextureManager().bindTexture(rl);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(i - 20, j - 20, 0, 0, 256, 256);
  }
}
