package com.denfop.ssp.tiles.neutronfabricator;

import ic2.core.GuiIC2;
import ic2.core.gui.TankGauge;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMatter extends GuiIC2<ContainerMatter> {
  public final String progressLabel;
  
  public final String amplifierLabel;
  
  public GuiMatter(ContainerMatter container) {
    super(container);
    addElement(TankGauge.createNormal(this, 96, 22, container.base.fluidTank));
    this.progressLabel = Localization.translate("ic2.Matter.gui.info.progress");
    this.amplifierLabel = Localization.translate("ic2.Matter.gui.info.amplifier");
  }
  
  protected void drawForegroundLayer(int mouseX, int mouseY) {
    super.drawForegroundLayer(mouseX, mouseY);
    this.fontRenderer.drawString(this.progressLabel, 8, 22, 4210752);
    this.fontRenderer.drawString(this.container.base.getProgressAsString(), 18, 31, 4210752);
    if (this.container.base.scrap > 0) {
      this.fontRenderer.drawString(this.amplifierLabel, 8, 46, 4210752);
      this.fontRenderer.drawString("" + this.container.base.scrap, 8, 58, 4210752);
    } 
  }
  
  public ResourceLocation getTexture() {
    return new ResourceLocation("ic2", "textures/gui/GUIMatter.png");
  }
}
