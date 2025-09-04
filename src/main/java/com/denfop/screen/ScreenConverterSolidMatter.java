package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.IUpgradeItem;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.api.upgrades.UpgradeRegistry;
import com.denfop.api.widget.AdvancedTooltipWidget;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuConverterSolidMatter;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class ScreenConverterSolidMatter<T extends ContainerMenuConverterSolidMatter> extends ScreenMain<ContainerMenuConverterSolidMatter> {

    private static final ResourceLocation background = new ResourceLocation(
            Constants.TEXTURES,
            "textures/gui/guiconvertersolidmatter1.png"
    );
    final ChatFormatting[] name = {ChatFormatting.DARK_PURPLE, ChatFormatting.YELLOW, ChatFormatting.BLUE,
            ChatFormatting.RED, ChatFormatting.GRAY, ChatFormatting.GREEN, ChatFormatting.DARK_AQUA, ChatFormatting.AQUA};
    private final ContainerMenuConverterSolidMatter container;

    public ScreenConverterSolidMatter(ContainerMenuConverterSolidMatter container1) {
        super(container1);
        this.imageWidth = 246;
        this.imageHeight = 192;
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new ScreenWidget(this, 117, 93, EnumTypeComponent.ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));
        for (int i = 0; i < container.base.quantitysolid.length; i++) {

            final int finalI = i;
            this.addWidget(new AdvancedTooltipWidget(this, 202, 6 + 18 * finalI, 245, 19 + 18 * finalI) {
                @Override
                protected List<String> getToolTip() {
                    return Collections.singletonList(name[finalI] + "" + container.base.quantitysolid[finalI] + "/" + 100000);
                }
            });

        }
        this.addWidget(new AdvancedTooltipWidget(this, 79,
                11, 96, 44
        ) {
            @Override
            protected List<String> getToolTip() {
                return Collections.singletonList(ModUtils.getString(container.base.getProgress() * 100) + "%");
            }
        });
        this.addWidget(new AdvancedTooltipWidget(this, 43, 47, 76, 64
        ) {
            @Override
            protected List<String> getToolTip() {
                return Collections.singletonList(ModUtils.getString(container.base.getProgress() * 100) + "%");
            }
        });
        this.addWidget(new AdvancedTooltipWidget(this, 99, 47, 132, 64
        ) {
            @Override
            protected List<String> getToolTip() {
                return Collections.singletonList(ModUtils.getString(container.base.getProgress() * 100) + "%");
            }
        });
        this.addWidget(new AdvancedTooltipWidget(this, 79, 67, 96, 100
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

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);


    }

    private void handleUpgradeTooltip1(int mouseX, int mouseY) {

        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate(Constants.ABBREVIATION + ".generic.text.upgrade"));

            for (final ItemStack stack : getCompatibleUpgrades(this.container.base)) {
                text.add(stack.getDisplayName().getString());
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

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(background);
        int j = (this.width - this.imageWidth) / 2;
        int k = (this.height - this.imageHeight) / 2;
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        for (int i = 0; i < container.base.quantitysolid.length; i++) {
            double p = ((container.base.quantitysolid[i] / 100000) * 23);


            drawTexturedModalRect(poseStack, j + 217, k + 11 + i * 18, 99, 194, (int) p, 4);


        }
        double temp = container.base.getProgress();
        if (temp > 0) {
            temp *= 32;
            drawTexturedModalRect(poseStack, j + 44, k + 48, 144, 194, (int) temp, 16);
            drawTexturedModalRect(poseStack, (int) ((j + 133) - temp), k + 48, (int) (177 - temp), 194, (int) temp, 16);

            drawTexturedModalRect(poseStack, j + 80, k + 12, 125, 194, 16, (int) temp);

            drawTexturedModalRect(poseStack, j + 80, (int) (k + 101 - temp), 125, (int) (227 - temp), 16, (int) temp);

        }
        drawBackground(poseStack);
        bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);

    }


}
