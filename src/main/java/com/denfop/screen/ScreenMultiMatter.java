package com.denfop.screen;


import com.denfop.Constants;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.BlockEntityUpgradeManager;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.api.upgrades.UpgradeItem;
import com.denfop.api.widget.*;
import com.denfop.blockentity.base.BlockEntityMultiMatter;
import com.denfop.componets.ComponentButton;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuMultiMatter;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class ScreenMultiMatter<T extends ContainerMenuMultiMatter> extends ScreenMain<ContainerMenuMultiMatter> {

    public final ContainerMenuMultiMatter container;
    public final String progressLabel;
    public final String amplifierLabel;

    public ScreenMultiMatter(ContainerMenuMultiMatter container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.progressLabel = Localization.translate("Matter.gui.info.progress");
        this.amplifierLabel = Localization.translate("Matter.gui.info.amplifier");

        this.addComponent(new ScreenWidget(this, 4, 15, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));

        this.componentList.add(new ScreenWidget(this, 117, 41, EnumTypeComponent.FLUID_PART,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.componentList.add(new ScreenWidget(this, 45, 60, EnumTypeComponent.WORK_BUTTON,
                new WidgetDefault<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return ((BlockEntityMultiMatter) this.getEntityBlock()).work ? Localization.translate("turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((BlockEntityMultiMatter) this.getEntityBlock()).work;
                    }
                })
        ));
    }

    private static List<ItemStack> getCompatibleUpgrades(BlockEntityUpgrade block) {
        List<ItemStack> ret = new ArrayList<>();
        Set<EnumBlockEntityUpgrade> properties = block.getUpgradableProperties();

        for (final ItemStack stack : BlockEntityUpgradeManager.getUpgrades()) {
            UpgradeItem item = (UpgradeItem) stack.getItem();
            if (item.isSuitableFor(stack, properties)) {
                ret.add(stack);
            }
        }

        return ret;
    }

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.getXSize()) / 2;
        int yMin = (this.height - this.getYSize()) / 2;
        int x = i - xMin;
        int y = j - yMin;

    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {

        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate(Constants.ABBREVIATION + ".generic.text.upgrade"));

            for (final ItemStack stack : getCompatibleUpgrades(this.container.base)) {
                text.add(stack.getDisplayName().getString());
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        this.handleUpgradeTooltip(par1, par2);
        TankWidget.createNormal(this, 96, 22, container.base.fluidTank).drawForeground(poseStack, par1, par2);
        this.font.draw(poseStack, this.progressLabel, 8, 29, 4210752);
        this.font.draw(poseStack, this.container.base.getProgressAsString(), 18, 39, 4210752);
        if ((this.container.base).scrap > 0) {
            this.font.draw(poseStack, this.amplifierLabel, 8, 49, 4210752);
            this.font.draw(poseStack, "" + (this.container.base).scrap, 8, 59, 4210752);
        }
        this.drawForeground(poseStack, par1, par2);

    }

    @Override
    protected void renderBg(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.renderBg(poseStack, partialTicks, mouseX, mouseY);
        TankWidget.createNormal(this, 96, 22, container.base.fluidTank).drawBackground(poseStack, this.guiLeft(), guiTop());
    }

    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft(), this.guiTop(), 0, 0, this.getXSize(), this.getYSize());
        String name = Localization.translate(this.container.base.getName());
        int textWidth = this.getStringWidth(name);
        float scale = 1.0f;


        if (textWidth > 120) {
            scale = 120f / textWidth;
        }


        poseStack.pushPose();
        poseStack.scale(scale, scale, 1.0f);


        int centerX = this.guiLeft() + this.getXSize() / 2;
        int textX = (int) ((centerX / scale) - (textWidth / 2.0f));
        int textY = (int) ((this.guiTop() + 6) / scale);


        this.font.draw(poseStack, name, textX, textY, 4210752);


        poseStack.popPose();
    }

    public ResourceLocation getTexture() {

        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");


    }

}
