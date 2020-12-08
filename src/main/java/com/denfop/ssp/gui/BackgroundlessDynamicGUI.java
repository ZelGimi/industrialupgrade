package com.denfop.ssp.gui;

import ic2.core.ContainerBase;
import ic2.core.gui.GuiElement;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.DynamicGui;
import ic2.core.gui.dynamic.GuiParser;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class BackgroundlessDynamicGUI<T extends ContainerBase<? extends IInventory>> extends DynamicGui<T> {
  public static <T extends IInventory> DynamicGui<ContainerBase<T>> create(T base, EntityPlayer player, GuiParser.GuiNode guiNode) {
    return new BackgroundlessDynamicGUI(player, DynamicContainer.create((IInventory)base, player, guiNode), guiNode);
  }
  
  protected BackgroundlessDynamicGUI(EntityPlayer player, T container, GuiParser.GuiNode guiNode) {
    super(player, container, guiNode);
  }
  
  protected final void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    mouseX -= this.guiLeft;
    mouseY -= this.guiTop;
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    for (GuiElement<?> element : this.elements) {
      if (element.isEnabled())
        drawElement(element, mouseX, mouseY); 
    } 
  }
  
  protected void drawElement(GuiElement<?> element, int mouseX, int mouseY) {
    element.drawBackground(mouseX, mouseY);
  }
  
  public int getLeft() {
    return this.guiLeft;
  }
  
  public int getTop() {
    return this.guiTop;
  }
}
