package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.IUpgradeItem;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.api.upgrades.UpgradeRegistry;
import com.denfop.container.ContainerConverterSolidMatter;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class GuiConverterSolidMatter extends GuiCore<ContainerConverterSolidMatter> {

    private static final ResourceLocation background = new ResourceLocation(
            Constants.TEXTURES,
            "textures/gui/GuiConverterSolidMatter.png"
    );
    final TextFormatting[] name = {TextFormatting.DARK_PURPLE, TextFormatting.YELLOW, TextFormatting.BLUE,
            TextFormatting.RED, TextFormatting.GRAY, TextFormatting.GREEN, TextFormatting.DARK_AQUA, TextFormatting.AQUA};
    private final ContainerConverterSolidMatter container;

    public GuiConverterSolidMatter(ContainerConverterSolidMatter container1) {
        super(container1);
        this.ySize = 240;
        this.container = container1;
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

    protected void drawForegroundLayer(int par1, int par2) {


        new AdvArea(this, 119, 114, 157, 126)
                .withTooltip("EF: " + ModUtils.getString((this.container.base).energy.getEnergy()) + "/" + ModUtils.getString((this.container.base).energy.getCapacity()))
                .drawForeground(par1, par2);


        for (int i = 0; i < container.base.quantitysolid.length; i++) {
            String tooltip1 = container.base.quantitysolid[i] + "/" + 100000;

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
        this.handleUpgradeTooltip1(par1, par2);


    }

    private void handleUpgradeTooltip1(int mouseX, int mouseY) {

        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate(Constants.ABBREVIATION + ".generic.text.upgrade"));

            for (final ItemStack stack : getCompatibleUpgrades(this.container.base)) {
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
        return Localization.translate(this.container.base.getName());
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(background);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);

        for (int i = 0; i < container.base.quantitysolid.length; i++) {
            double p = ((container.base.quantitysolid[i] / 100000) * 11);
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
        this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
    }


}
