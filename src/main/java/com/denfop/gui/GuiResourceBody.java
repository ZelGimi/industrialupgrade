package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.ScrollDirection;
import com.denfop.api.gui.TankGauge;
import com.denfop.api.space.IBaseResource;
import com.denfop.api.space.IBody;
import com.denfop.api.space.fakebody.Data;
import com.denfop.audio.EnumSound;
import com.denfop.container.ContainerResearchTableSpace;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import static com.denfop.api.gui.GuiElement.bindBlockTexture;
import static com.denfop.api.gui.GuiElement.getBlockTextureMap;
import static com.denfop.gui.GuiCore.bindTexture;
import static net.minecraft.world.item.ItemDisplayContext.GUI;

public class GuiResourceBody extends GuiDefaultResearchTable {

    boolean hoverDelete;
    public GuiResourceBody(GuiResearchTableSpace tileEntityResearchTableSpace) {
        super(tileEntityResearchTableSpace, 25, 25, 150, 150);
    }

    @Override
    public void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        IBody planet = this.getTile().focusedPlanet;
        Data data = getTile().getContainer().base.dataMap.get(planet);
        for (int i = 0; i < planet.getResources().size(); i++) {
            IBaseResource resource = planet.getResources().get(i);
            if (resource.getItemStack() != null) {
                new Area(getTile(), this.x+offsetX1+((15 + (i % 7) * 18)),this.y+offsetY1 + (int) ((27  + 18 * (i / 7))), 18, 18).withTooltip(
                        (!(resource.getPercentPanel() > data.getPercent()) ?    resource
                                .getItemStack()
                                .getDisplayName().getString()  : "???")+ "\n" + Localization.translate("iu.space_chance") + " " + ModUtils.getString(
                                resource.getChance() * 100D / resource.getMaxChance()) + "%" +
                                "\n" + Localization.translate("iu.space_rover") + " " + Localization.translate("iu" +
                                ".space_rover_" + resource.getTypeRovers().name().toLowerCase())
                ).drawForeground(poseStack, par1, par2);
            }
            if (resource.getFluidStack() != null && !(resource.getPercentPanel() > data.getPercent())) {
                new Area(getTile(),this.x+offsetX1+ ((15 + (i % 7) * 18)), (int) this.y+offsetY1 +((27  + 18 * (i / 7))), 16, 16).withTooltip(
                        (!(resource.getPercentPanel() > data.getPercent()) ?    resource
                                .getFluidStack()
                                .getDisplayName().getString() : "???") + "\n" + (!(resource.getPercentPanel() > data.getPercent()) ? Localization.translate("iu.space_amount") :  "??? mb")+ " " + resource.getFluidStack().getAmount() + "mb" + "\n" +
                                Localization.translate("iu.space_chance") + " " + ModUtils.getString(resource.getChance() * 100D / resource.getMaxChance()) + "%" +
                                "\n" + Localization.translate("iu.space_rover") + " " + Localization.translate("iu.space_rover_" + resource
                                .getTypeRovers()
                                .name()
                                .toLowerCase())
                ).drawForeground(poseStack, par1, par2);
            }
        }
        hoverDelete = false;
        if (par1 >= this.x+offsetX1 +  width -  20 && par2 <=  this.x+offsetX1 +  width -  20 + 15 && par2 >=this.y+offsetY1 + 5 && par2 <=this.y+offsetY1 + 20 ){
            hoverDelete = true;
        }
    }

    @Override
    public void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        this.getTile().drawString(poseStack,Localization.translate("iu.space.planet.resource"),getTile().guiLeft+ this.x+offsetX1+width/2-tile.getStringWidth(Localization.translate("iu.space.planet.resource"))/2,getTile().guiTop + this.y+offsetY1 + 5, ModUtils.convertRGBAcolorToInt(255,255,255));
        IBody planet = this.getTile().focusedPlanet;
        PoseStack pose = poseStack.pose();
        int guiLeft = getTile().guiLeft + this.x + offsetX1;
        int guiTop = getTile().guiTop + this.y + offsetY1;
        pose.pushPose();
        pose.translate(guiLeft + width -  20, guiTop + 5, 20);
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"));
        pose.scale(0.5f, 0.5f, 1);
        if (!hoverDelete)
            getTile().drawTexturedModalRect(poseStack, 0, 0, 139, 32, 30, 30);
        else
            getTile().drawTexturedModalRect(poseStack, 0, 0, 170, 32, 30, 30);
        pose.popPose();
        Data data = getTile().getContainer().base.dataMap.get(planet);
        for (int i = 0; i < planet.getResources().size(); i++) {
            IBaseResource baseResource = planet.getResources().get(i);
            if (baseResource.getPercentPanel() > data.getPercent()) {

            }
            if (baseResource.getItemStack() != null || baseResource.getFluidStack() != null) {
                if (baseResource.getPercentPanel() <= data.getPercent() && baseResource.getItemStack() != null) {
                    RenderSystem.enableBlend();
                    pose.translate(0,0, -100);
                    poseStack.renderItem(baseResource.getItemStack(), ((15 + (i % 7) * 18)) + guiLeft, (int) ((27  + 18 * (i / 7))) + guiTop);
                    poseStack.renderItemDecorations(Minecraft.getInstance().font, baseResource.getItemStack(), ((15 + (i % 7) * 18)) + guiLeft, (int) ((27  + 18 * (i / 7))) + guiTop);
                    pose.translate(0,0, 100);
                    RenderSystem.disableBlend();
                 } else if (baseResource.getPercentPanel() <= data.getPercent() && baseResource.getFluidStack() != null) {
                    int fluidX = ((15 + (i % 7) * 18))  + 1;
                    int fluidY =((27  + 18 * (i / 7))) + 1;
                    int fluidWidth = 16;
                    int fluidHeight = 16;
                    Fluid fluid =  baseResource.getFluidStack() .getFluid();
                    IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                    TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture( baseResource.getFluidStack() ));
                    int color = extensions.getTintColor();
                    bindBlockTexture();
                    this.getTile().drawSprite(poseStack,
                            guiLeft+ fluidX,
                            guiTop+     fluidY,
                            fluidWidth,
                            fluidHeight,
                            sprite,
                            color,
                            1.0,
                            false,
                            false
                    );
                } else {
                    ItemStack stack = new ItemStack(Items.BARRIER);

                    RenderSystem.enableBlend();
                    poseStack.renderItem(stack, ((15 + (i % 7) * 18)) + guiLeft, (int) ((27  + 18 * (i / 7))) + guiTop);
                    poseStack.renderItemDecorations(Minecraft.getInstance().font, stack, ((15 + (i % 7) * 18)) + guiLeft, (int) ((27  + 18 * (i / 7))) + guiTop);
                    RenderSystem.disableBlend();

                }
            }



        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, ScrollDirection direction) {
        return super.mouseScrolled(mouseX, mouseY, direction);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {
        if (hoverDelete){
            this.getTile().defaultResearchGuis.remove(this);
            getTile().getContainer().player.playSound(EnumSound.button.getSoundEvent(), 0.5F, 1);
            return true;

        }
        return super.mouseClicked(mouseX, mouseY);

    }
}
