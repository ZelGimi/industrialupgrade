package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.Recipes;
import com.denfop.api.item.upgrade.UpgradeSystem;
import com.denfop.api.widget.ImageInterfaceWidget;
import com.denfop.api.widget.ItemStackWidget;
import com.denfop.containermenu.ContainerMenuHeldUpgradeItem;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ScreenUpgradeItem<T extends ContainerMenuHeldUpgradeItem> extends ScreenMain<ContainerMenuHeldUpgradeItem> {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/guiblacklist.png");
    final List<ItemStack> list;
    private final String name;


    public ScreenUpgradeItem(ContainerMenuHeldUpgradeItem container, final ItemStack itemStack1) {
        super(container);

        this.name = itemStack1.getDisplayName().getString();
        this.imageHeight = 125;
        this.list = ModUtils.get_blacklist_block();
        final List<String> list2 = UpgradeSystem.system.getBlackList(itemStack1);
        for (String name : list2) {
            list.add(Recipes.inputFactory.getInput(name).getInputs().get(0));
        }
        this.componentList.clear();
        this.addWidget(new ImageInterfaceWidget(this, 0, 0, imageWidth, imageHeight));
        for (int i = 0; i < this.list.size(); i++) {
            int y = i / 9;
            int x = i % 9;
            ItemStack stack = this.list.get(i);
            this.addWidget(new ItemStackWidget(this, 8 + x * 18, 40 + y * 18, () -> stack));
        }
    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        draw(poseStack, this.name, (this.imageWidth - this.getStringWidth(this.name)) / 2, 11, 0);
        draw(poseStack,
                Localization.translate("iu.blacklist_description"),
                (this.imageWidth - this.getStringWidth(Localization.translate("iu.blacklist_description"))) / 2,
                21,
                0
        );

    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);


    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
