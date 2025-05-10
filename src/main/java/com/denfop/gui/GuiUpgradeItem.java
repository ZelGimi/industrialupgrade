package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.ItemStackImage;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.container.ContainerHeldUpgradeItem;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiUpgradeItem<T extends ContainerHeldUpgradeItem> extends GuiIU<ContainerHeldUpgradeItem> {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/guiblacklist.png");
    final List<ItemStack> list;
    private final String name;


    public GuiUpgradeItem(ContainerHeldUpgradeItem container, final ItemStack itemStack1) {
        super(container);

        this.name = itemStack1.getDisplayName().getString();
        this.imageHeight = 125;
        this.list = ModUtils.get_blacklist_block();
        final List<String> list2 = UpgradeSystem.system.getBlackList(itemStack1);
        for (String name : list2) {
            list.add(Recipes.inputFactory.getInput(name).getInputs().get(0));
        }
        this.componentList.clear();
        this.addElement(new ImageInterface(this, 0, 0, imageWidth, imageHeight));
        for (int i = 0; i < this.list.size(); i++) {
            int y = i / 9;
            int x = i % 9;
            ItemStack stack = this.list.get(i);
            this.addElement(new ItemStackImage(this, 8 + x * 18, 40 + y * 18, () -> stack));
        }
    }

    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        this.font.draw(poseStack, this.name, (this.imageWidth - this.getStringWidth(this.name)) / 2, 11, 0);
        this.font.draw(poseStack,
                Localization.translate("iu.blacklist_description"),
                (this.imageWidth - this.getStringWidth(Localization.translate("iu.blacklist_description"))) / 2,
                21,
                0
        );

    }

    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);


    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
