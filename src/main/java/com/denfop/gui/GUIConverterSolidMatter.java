package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerConverterSolidMatter;
import com.denfop.utils.ModUtils;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.api.upgrade.UpgradableProperty;
import ic2.api.upgrade.UpgradeRegistry;
import ic2.core.GuiIC2;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class GUIConverterSolidMatter extends GuiIC2<ContainerConverterSolidMatter> {

    private static final ResourceLocation background = new ResourceLocation(
            Constants.TEXTURES,
            "textures/gui/GUIConverterSolidMatter.png"
    );
    final TextFormatting[] name = {TextFormatting.DARK_PURPLE, TextFormatting.YELLOW, TextFormatting.BLUE,
            TextFormatting.RED, TextFormatting.GRAY, TextFormatting.GREEN, TextFormatting.DARK_AQUA, TextFormatting.AQUA};
    private final ContainerConverterSolidMatter container;

    public GUIConverterSolidMatter(ContainerConverterSolidMatter container1) {
        super(container1);
        this.ySize = 240;
        this.container = container1;
    }

    private static List<ItemStack> getCompatibleUpgrades(IUpgradableBlock block) {
        List<ItemStack> ret = new ArrayList();
        Set<UpgradableProperty> properties = block.getUpgradableProperties();
        Iterator var3 = UpgradeRegistry.getUpgrades().iterator();

        while (var3.hasNext()) {
            ItemStack stack = (ItemStack) var3.next();
            IUpgradeItem item = (IUpgradeItem) stack.getItem();
            if (item.isSuitableFor(stack, properties)) {
                ret.add(stack);
            }
        }

        return ret;
    }

    protected void drawForegroundLayer(int par1, int par2) {
        this.fontRenderer.drawString(
                this.getName(),
                (this.xSize - this.fontRenderer.getStringWidth(this.getName())) / 2,
                6,
                4210752
        );

        new AdvArea(this, 119, 114, 157, 126)
                .withTooltip("EU: " + ModUtils.getString((this.container.base).energy.getEnergy()) + "/" + ModUtils.getString((this.container.base).energy.getCapacity()))
                .drawForeground(par1, par2);


        for (int i = 0; i < container.base.quantitysolid.length; i++) {
            String tooltip1 = container.base.quantitysolid[i] + "/" + 5000;

            new AdvArea(this, 23, 20 + 15 * i, 40, 28 + 15 * i).withTooltip(name[i] + tooltip1).drawForeground(par1, par2);

        }
        new AdvArea(this, 78,
                50, 111, 67
        ).withTooltip(ModUtils.getString(this.container.base.getProgress() * 100) + "%").drawForeground(par1, par2);
        new AdvArea(this, 138, 50, 171, 67)
                .withTooltip(ModUtils.getString(this.container.base.getProgress() * 100) + "%")
                .drawForeground(par1, par2);
        new AdvArea(this, 116, 16, 133, 49)
                .withTooltip(ModUtils.getString(this.container.base.getProgress() * 100) + "%")
                .drawForeground(par1, par2);
        new AdvArea(this, 116, 68, 133, 101)
                .withTooltip(ModUtils.getString(this.container.base.getProgress() * 100) + "%")
                .drawForeground(par1, par2);
        if (this.container.base instanceof IUpgradableBlock) {
            this.handleUpgradeTooltip1(par1, par2);
        }


    }

    private void handleUpgradeTooltip1(int mouseX, int mouseY) {

        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList();
            text.add(Localization.translate("ic2.generic.text.upgrade"));
            Iterator var5 = getCompatibleUpgrades(this.container.base).iterator();

            while (var5.hasNext()) {
                ItemStack stack = (ItemStack) var5.next();
                text.add(stack.getDisplayName());
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

    private String getName() {
        return Localization.translate("blockConverterSolidMatter.name");
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(background);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);

        for (int i = 0; i < container.base.quantitysolid.length; i++) {
            double p = ((container.base.quantitysolid[i] / 5000) * 11);
            int l = i - 6;
            if (l != 1) {
                l = 0;
            }

            drawTexturedModalRect((int) (j + 26 + p), k + 25 + 15 * i - l, 182, 12, 1, 3);


        }
        double temp = container.base.getProgress();
        if (temp > 0) {
            temp *= 31;
            drawTexturedModalRect(j + 79, k + 51, 176, 24, (int) temp, 15);
            drawTexturedModalRect((int) ((j + 171) - temp), k + 51, (int) (208 - temp), 24, (int) temp, 15);

            drawTexturedModalRect(j + 116, k + 16 + 1, 176, 42, 17, (int) temp);

            drawTexturedModalRect(j + 116, (int) (k + 101 - temp), 176, (int) (74 - temp), 17, (int) temp);

        }
        double energy = this.container.base.energy.getFillRatio() * 38;
        if (energy > 0) {
            drawTexturedModalRect(j + 119, k + 115, 176,
                    81, (int) energy, 11
            );
        }
        if (this.container.base instanceof IUpgradableBlock) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("ic2", "textures/gui/infobutton.png"));
            this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }
    }

    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
    }

}
