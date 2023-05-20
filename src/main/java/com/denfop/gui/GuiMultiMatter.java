package com.denfop.gui;


import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.gui.*;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerMultiMatter;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.api.upgrade.UpgradableProperty;
import ic2.api.upgrade.UpgradeRegistry;
import ic2.core.init.Localization;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class GuiMultiMatter extends GuiIU<ContainerMultiMatter> {

    public final ContainerMultiMatter container;
    public final String progressLabel;
    public final String amplifierLabel;

    public GuiMultiMatter(ContainerMultiMatter container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.progressLabel = Localization.translate("ic2.Matter.gui.info.progress");
        this.amplifierLabel = Localization.translate("ic2.Matter.gui.info.amplifier");
        addElement(TankGauge.createNormal(this, 96, 22, container.base.fluidTank));
        this.xSize = 200;
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
    }

    private static List<ItemStack> getCompatibleUpgrades(IUpgradableBlock block) {
        List<ItemStack> ret = new ArrayList<>();
        Set<UpgradableProperty> properties = block.getUpgradableProperties();

        for (final ItemStack stack : UpgradeRegistry.getUpgrades()) {
            IUpgradeItem item = (IUpgradeItem) stack.getItem();
            if (item.isSuitableFor(stack, properties)) {
                ret.add(stack);
            }
        }

        return ret;
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 182 && x <= 190 && y >= 6 && y <= 14) {
            IUCore.network.get(false).initiateClientTileEntityEvent(this.container.base, 0);
        }
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {

        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("ic2.generic.text.upgrade"));

            for (final ItemStack stack : getCompatibleUpgrades(this.container.base)) {
                text.add(stack.getDisplayName());
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void drawForegroundLayer(int par1, int par2) {

        this.handleUpgradeTooltip(par1, par2);

        this.fontRenderer.drawString(this.progressLabel, 8, 27, 4210752);
        this.fontRenderer.drawString(this.container.base.getProgressAsString(), 18, 36, 4210752);
        if ((this.container.base).scrap > 0) {
            this.fontRenderer.drawString(this.amplifierLabel, 8, 46, 4210752);
            this.fontRenderer.drawString("" + (this.container.base).scrap, 8, 58, 4210752);
        }
        FluidStack fluidstack = (this.container.base).fluidTank.getFluid();
        if (fluidstack != null) {
            String tooltip = Localization.translate("ic2.uumatter") + ": " + fluidstack.amount
                    + Localization.translate("ic2.generic.text.mb");
            new Area(this, 99, 25, 112 - 99, 73 - 25).withTooltip(tooltip).drawForeground(par1, par2);

        }
        new AdvArea(this, 182, 6, 190, 14).withTooltip(this.container.base.work ? Localization.translate("turn_off") :
                Localization.translate("turn_on")).drawForeground(par1, par2);
        this.mc.getTextureManager().bindTexture(this.getTexture());
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.container.base.work) {
            this.drawTexturedModalRect(+181, +5, 203, 5, 11, 11);

        }
        this.drawForeground(par1, par2);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(this.xSize / 2 - 15, 6, name, 4210752, false);

    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(this.xSize / 2 - 15, 6, name, 4210752, false);
    }

    public ResourceLocation getTexture() {

        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIMatter.png");


    }

}
