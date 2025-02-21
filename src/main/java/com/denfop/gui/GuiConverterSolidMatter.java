package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class GuiConverterSolidMatter extends GuiIU<ContainerConverterSolidMatter> {

    private static final ResourceLocation background = new ResourceLocation(
            Constants.TEXTURES,
            "textures/gui/guiconvertersolidmatter1.png"
    );
    final TextFormatting[] name = {TextFormatting.DARK_PURPLE, TextFormatting.YELLOW, TextFormatting.BLUE,
            TextFormatting.RED, TextFormatting.GRAY, TextFormatting.GREEN, TextFormatting.DARK_AQUA, TextFormatting.AQUA};
    private final ContainerConverterSolidMatter container;

    public GuiConverterSolidMatter(ContainerConverterSolidMatter container1) {
        super(container1);
        this.xSize = 246;
        this.ySize = 192;
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 117, 93, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.energy)
        ));
        for (int i = 0; i < container.base.quantitysolid.length; i++) {

            final int finalI = i;
            this.addElement(new AdvArea(this, 202, 6 + 18 * finalI, 245, 19 + 18 * finalI) {
                @Override
                protected List<String> getToolTip() {
                    return Collections.singletonList(name[finalI] + "" + container.base.quantitysolid[finalI] + "/" + 100000);
                }
            });

        }
        this.addElement(new AdvArea(this, 79,
                11, 96, 44
        ) {
            @Override
            protected List<String> getToolTip() {
                return Collections.singletonList(ModUtils.getString(container.base.getProgress() * 100) + "%");
            }
        });
        this.addElement(new AdvArea(this, 43, 47, 76, 64
        ) {
            @Override
            protected List<String> getToolTip() {
                return Collections.singletonList(ModUtils.getString(container.base.getProgress() * 100) + "%");
            }
        });
        this.addElement(new AdvArea(this, 99, 47, 132, 64
        ) {
            @Override
            protected List<String> getToolTip() {
                return Collections.singletonList(ModUtils.getString(container.base.getProgress() * 100) + "%");
            }
        });
        this.addElement(new AdvArea(this, 79, 67, 96, 100
        ) {
            @Override
            protected List<String> getToolTip() {
                return Collections.singletonList(ModUtils.getString(container.base.getProgress() * 100) + "%");
            }
        });

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
        super.drawForegroundLayer(par1, par2);
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
        super.drawGuiContainerBackgroundLayer(f, x, y);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(background);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        for (int i = 0; i < container.base.quantitysolid.length; i++) {
            double p = ((container.base.quantitysolid[i] / 100000) * 23);


            drawTexturedModalRect(j + 217, k + 11 + i * 18, 99, 194, (int) p, 4);


        }
        double temp = container.base.getProgress();
        if (temp > 0) {
            temp *= 32;
            drawTexturedModalRect(j + 44, k + 48, 144, 194, (int) temp, 16);
            drawTexturedModalRect((int) ((j + 133) - temp), k + 48, (int) (177 - temp), 194, (int) temp, 16);

            drawTexturedModalRect(j + 80, k + 12, 125, 194, 16, (int) temp);

            drawTexturedModalRect(j + 80, (int) (k + 101 - temp), 125, (int) (227 - temp), 16, (int) temp);

        }
        drawBackground();
        this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);

    }


}
