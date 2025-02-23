package com.denfop.gui;


import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.IUpgradeItem;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.api.upgrades.UpgradeRegistry;
import com.denfop.componets.ComponentButton;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerMultiMatter;
import com.denfop.tiles.base.TileMultiMatter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
        this.progressLabel = Localization.translate("Matter.gui.info.progress");
        this.amplifierLabel = Localization.translate("Matter.gui.info.amplifier");

        this.addComponent(new GuiComponent(this, 4, 15, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));

        this.componentList.add(new GuiComponent(this, 117, 41, EnumTypeComponent.FLUID_PART,
                new Component<>(new ComponentEmpty())
        ));
        this.componentList.add(new GuiComponent(this, 45, 60, EnumTypeComponent.WORK_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return ((TileMultiMatter) this.getEntityBlock()).work ? Localization.translate("turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((TileMultiMatter) this.getEntityBlock()).work;
                    }
                })
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

    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {

        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate(Constants.ABBREVIATION + ".generic.text.upgrade"));

            for (final ItemStack stack : getCompatibleUpgrades(this.container.base)) {
                text.add(stack.getDisplayName());
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.handleUpgradeTooltip(par1, par2);
        TankGauge.createNormal(this, 96, 22, container.base.fluidTank).drawForeground(par1, par2);
        this.fontRenderer.drawString(this.progressLabel, 8, 29, 4210752);
        this.fontRenderer.drawString(this.container.base.getProgressAsString(), 18, 39, 4210752);
        if ((this.container.base).scrap > 0) {
            this.fontRenderer.drawString(this.amplifierLabel, 8, 49, 4210752);
            this.fontRenderer.drawString("" + (this.container.base).scrap, 8, 59, 4210752);
        }
        this.drawForeground(par1, par2);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        TankGauge.createNormal(this, 96, 22, container.base.fluidTank).drawBackground(this.guiLeft, guiTop);
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(this.container.base.getName());
        int textWidth = this.fontRenderer.getStringWidth(name);
        float scale = 1.0f;


        if (textWidth > 120) {
            scale = 120f / textWidth;
        }


        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0f);


        int centerX = this.guiLeft + this.xSize / 2;
        int textX = (int) ((centerX / scale) - (textWidth / 2.0f));
        int textY = (int) ((this.guiTop + 6)/scale);


        this.fontRenderer.drawString(name, textX, textY, 4210752);


        GlStateManager.popMatrix();
    }

    public ResourceLocation getTexture() {

        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");


    }

}
