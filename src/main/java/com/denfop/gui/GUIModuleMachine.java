package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerModuleMachine;
import ic2.core.GuiIC2;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@SideOnly(Side.CLIENT)
public class GuiModuleMachine extends GuiIC2<ContainerModuleMachine> {

    public final ContainerModuleMachine container;

    public GuiModuleMachine(ContainerModuleMachine container1) {
        super(container1);
        this.container = container1;
    }

    private static List<String> getInformation() {
        List<String> ret = new ArrayList<>();
        ret.add(Localization.translate("iu.moduleinformation1"));
        ret.add(Localization.translate("iu.moduleinformation2"));
        ret.add(Localization.translate("iu.moduleinformation3"));


        return ret;
    }

    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(0, (this.width - this.xSize) / 2 + 103, (this.height - this.ySize) / 2 + 21,
                68, 9, Localization.translate("button.write")
        ));
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;

        this.mc.getTextureManager().bindTexture(getTexture());
        this.mc.getTextureManager().bindTexture(new ResourceLocation(IC2.RESOURCE_DOMAIN, "textures/gui/infobutton.png"));
        this.drawTexturedModalRect(xoffset + 3, yoffset + 3, 0, 0, 10, 10);
        this.mc.getTextureManager().bindTexture(this.getTexture());

    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);

        handleUpgradeTooltip(par1, par2);
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.moduleinformation"));
            List<String> compatibleUpgrades = getInformation();
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    public String getName() {
        return this.container.base.getInventoryName();
    }

    protected void actionPerformed(GuiButton guibutton) {

        if (guibutton.id == 0) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 0);

        }
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiModuleMachine.png");
    }

}
