package com.Denfop.ssp;

import com.Denfop.ssp.gui.BackgroundlessDynamicGUI;

import ic2.core.ContainerBase;
import ic2.core.gui.GuiElement;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.DynamicGui;
import ic2.core.gui.dynamic.GuiParser;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class TransparentDynamicGUI<T extends ContainerBase<? extends IInventory>> extends BackgroundlessDynamicGUI<T> {
  public static <T extends IInventory> DynamicGui<ContainerBase<T>> create(T base, EntityPlayer player, GuiParser.GuiNode guiNode) {
    return new TransparentDynamicGUI(player, DynamicContainer.create((IInventory)base, player, guiNode), guiNode);
  }
  
  protected TransparentDynamicGUI(EntityPlayer player, T container, GuiParser.GuiNode guiNode) {
    super(player, container, guiNode);
  }
  
  protected void drawElement(GuiElement<?> element, int mouseX, int mouseY) {
    boolean image;
    if (image = element instanceof ic2.core.gui.Image) {
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    } 
    super.drawElement(element, mouseX, mouseY);
    if (image)
      GlStateManager.disableBlend(); 
  }
}
