package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.colonies.DataItem;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.colonies.enums.EnumProblems;
import com.denfop.api.space.fakebody.Data;
import com.denfop.audio.EnumSound;
import com.denfop.componets.Fluids;
import com.denfop.network.packet.PacketAddBuildingToColony;
import com.denfop.network.packet.PacketCreateAutoSends;
import com.denfop.network.packet.PacketCreateColony;
import com.denfop.network.packet.PacketSendResourceToEarth;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

import static com.denfop.api.gui.GuiElement.bindBlockTexture;
import static com.denfop.api.gui.GuiElement.getBlockTextureMap;
import static com.denfop.gui.GuiCore.bindTexture;

public class GuiColony extends GuiDefaultResearchTable {

    boolean hoverDelete;
    boolean hoverCreate;
    double sliderPosition;
    double sliderVertex;
    double sliderLength;
    double sliderStoragePosition;
    double sliderStorageVertex;
    double sliderStorageLength;
    List<ItemStack> listBuildings = List.of(new ItemStack(IUItem.colonial_building.getStack(4), 3), new ItemStack(IUItem.colonial_building.getStack(19), 1), new ItemStack(IUItem.colonial_building.getStack(0), 1), new ItemStack(IUItem.colonial_building.getStack(16), 1), new ItemStack(IUItem.colonial_building.getStack(13), 1));
    List<DataItem<?>> resourceList = null;

    public GuiColony(GuiResearchTableSpace tileEntityResearchTableSpace) {
        super(tileEntityResearchTableSpace, 25, 25, 150, 150);
    }

    @Override
    public void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        IBody planet = this.getTile().focusedPlanet;
        IColony colony = getTile().getContainer().base.colony;
        hoverDelete = false;
        hoverCreate = false;
        if (par1 >= this.x + offsetX1 + width - 20 && par2 <= this.x + offsetX1 + width - 20 + 15 && par2 >= this.y + offsetY1 + 5 && par2 <= this.y + offsetY1 + 20) {
            hoverDelete = true;
        }
        if (colony == null && par1 >= this.x + offsetX1 + 37 && par2 <= this.x + offsetX1 + 37 + 103 * 0.75 && par2 >= this.y + offsetY1 + 25 && par2 <= this.y + offsetY1 + 25 + 20 * 0.75) {
            hoverCreate = true;
        }
        if (colony == null)
            for (int i = 0; i < listBuildings.size(); i++) {
                ItemStack stack = listBuildings.get(i);
                new ItemStackImage(tile, this.x + offsetX1 + 33 + i * 18, this.y + offsetY1 + 40, () -> stack).drawForeground(poseStack, par1, par2);
            }
        if (colony != null) {
            int guiLeft = this.x + offsetX1;
            int guiTop = this.y + offsetY1;
            new Area(tile, guiLeft + 33, guiTop + 40 + 15 + 15 + 15, (int) (76 * 0.75), (int) (12 * 0.75)).withTooltip(Localization.translate("iu.space_colony_energy") + ModUtils.getString(colony.getEnergy()) + "/" + ModUtils.getString(
                    colony.getMaxEnergy()) + " EF"
                    + "\n" + Localization.translate("iu.space_colony_using_energy") + ModUtils.getString(colony.getUsingEnergy()) + " EF/t"
                    + "\n" + Localization.translate("iu.space_colony_generation_energy") + ModUtils.getString(colony.getGenerationEnergy()) + " EF/t").drawForeground(poseStack, par1, par2);
            new Area(tile, guiLeft + 33, guiTop + 40 + 15 + 15 + 15 + 15, (int) (76 * 0.75), (int) (12 * 0.75)).withTooltip(Localization.translate("iu.space_colony_food") + ModUtils.getString(colony.getFood()) +
                    "/" + ModUtils.getString((colony.getFreeWorkers() + colony.getWorkers()) * 15)
                    + "\n" + Localization.translate("iu.space_colony_using_food") + ModUtils.getString(colony.getUsingFood()) + "t"
                    + "\n" + Localization.translate("iu.space_colony_generation_food") + ModUtils.getString(colony.getGenerationFood()) + "t").drawForeground(poseStack, par1, par2);
            new Area(tile, guiLeft + 33, guiTop + 40 + 15 + 15 + 15 + 15 + 15, (int) (76 * 0.75), (int) (12 * 0.75)).withTooltip(Localization.translate("iu.space_colony_oxygen") + ModUtils.getString(colony.getOxygen()) + "/" + ModUtils.getString(
                    colony.getMaxOxygen())
                    + "\n" + Localization.translate("iu.space_colony_generation_oxygen") + ModUtils.getString(colony.getGenerationOxygen()) + "t"
                    + "\n" + Localization.translate("iu.space_colony_using_oxygen") + ModUtils.getString(colony.getUsingOxygen()) + "t").drawForeground(poseStack, par1, par2);
            new Area(tile, guiLeft + 33, guiTop + 40 + 15 + 15 + 15 + 15 + 15 + 15, (int) (76 * 0.75), (int) (12 * 0.75)).withTooltip(Localization.translate("iu.space_colony_entertainment") + ModUtils.getString(colony.getPercentEntertainment() * 100) + "%").drawForeground(poseStack, par1, par2);
            new Area(tile, guiLeft + 33, guiTop + 40 + 15 + 15 + 15 + 15 + 15 + 15 + 30, (int) (76 * 0.75), (int) (12 * 0.75)).withTooltip(Localization.translate("iu.space_colony_protection") + colony.getProtection()
                    + "\n" + Localization.translate("iu.space_colony_need_protection") + colony
                    .getBuildingList()
                    .size() * 2).drawForeground(poseStack, par1, par2);
            int j = 0;
            for (int i = (int) (8 * sliderPosition); i < Math.min(resourceList.size(), (sliderPosition * 8) + 8); i++) {
                DataItem<?> baseResource = resourceList.get(i);
                if (baseResource.getElement() instanceof ItemStack) {
                    ItemStack stack = (ItemStack) baseResource.getElement();
                    RenderSystem.enableBlend();
                    new ItemStackImageText(tile, ((25 + 32 + 53 + (j % 4) * 18)) + guiLeft, (int) ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + guiTop, () -> stack,  Localization.translate("iu.space.colony_level")+" " + baseResource.getLevel()).drawForeground(poseStack, par1, par2);
                    RenderSystem.disableBlend();
                } else if (baseResource.getElement() instanceof FluidStack) {

                    int fluidX = guiLeft + ((25 + 32 + 53 + (j % 4) * 18)) + 1;
                    int fluidY = guiTop + ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + 1;
                    FluidStack stack = ((FluidStack) baseResource.getElement());
                    new FluidItem(tile, fluidX, fluidY, stack).withTooltip(() -> Localization.translate("iu.space.colony_level")+" " + baseResource.getLevel()).drawForeground(poseStack, par1, par2);

                }
                j++;
            }
            j = 0;
            for (int i = (int) (8 * sliderStoragePosition); i < Math.min(colony.getStacksFromStorage().size() + colony.getFluidsFromStorage().size(), (sliderStoragePosition * 8) + 8); i++) {
                Object baseResource;
                if (i < colony.getStacksFromStorage().size()) {
                    baseResource = colony.getStacksFromStorage().get(i);
                } else {
                    baseResource = colony.getFluidsFromStorage().get(i - colony.getStacksFromStorage().size());
                }
                if (baseResource instanceof ItemStack) {
                    ItemStack stack = (ItemStack) baseResource;
                    new ItemStackImage(tile, ((25 + 32 + 53 + (j % 4) * 18)) + guiLeft, (int) ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + guiTop + 50, () -> stack).drawForeground(poseStack, par1, par2);
                } else if (baseResource instanceof FluidStack) {
                    FluidStack fluidStack = (FluidStack) baseResource;
                    if (fluidStack.isEmpty() || fluidStack.getFluid() == Fluids.EMPTY)
                        continue;
                    int fluidX = guiLeft + ((25 + 32 + 53 + (j % 4) * 18)) + 1;
                    int fluidY = guiTop + ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + 50 + 1;
                    new FluidItem(tile, fluidX, fluidY, ((FluidStack) baseResource)).drawForeground(poseStack, par1, par2);

                }
                j++;
            }

            if (getTile().container.player.containerMenu.getCarried().isEmpty())
                new Area(getTile(), guiLeft + 108, guiTop + 21, 76, 14).withTooltip(Localization.translate("iu.space.colony_info_add")).drawForeground(poseStack, par1, par2);


            if (getTile().container.base.getSends() != null) {
                List<Timer> timers = getTile().container.base.getSends().getTimers();
                if (timers != null && !timers.isEmpty()) {

                    new Area(getTile(), guiLeft + 108, guiTop + 55 - 17, (int) (103 * 0.75), (int) (20 * 0.75)).withTooltip(timers.get(0).getDisplay()).drawForeground(poseStack, par1, par2);
                }
            }
        }
    }

    @Override
    public void drawGuiContainerBackgroundLayer(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        IColony colony = getTile().getContainer().base.colony;
        if (colony != null) {
            this.width = 200;
            this.height = 200;
        }

        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        this.getTile().drawString(poseStack, Localization.translate("iu.space.planet.colony"), getTile().guiLeft + this.x + offsetX1 + width / 2 - tile.getStringWidth(Localization.translate("iu.space.planet.colony")) / 2, getTile().guiTop + this.y + offsetY1 + 5, ModUtils.convertRGBAcolorToInt(255, 255, 255));
        IBody planet = this.getTile().focusedPlanet;
        PoseStack pose = poseStack;
        int guiLeft = getTile().guiLeft + this.x + offsetX1;
        int guiTop = getTile().guiTop + this.y + offsetY1;
        pose.pushPose();
        pose.translate(guiLeft + width - 20, guiTop + 5, 20);
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"));
        pose.scale(0.5f, 0.5f, 1);
        if (!hoverDelete)
            getTile().drawTexturedModalRect(poseStack, 0, 0, 139, 32, 30, 30);
        else
            getTile().drawTexturedModalRect(poseStack, 0, 0, 170, 32, 30, 30);
        pose.popPose();
        if (colony == null) {

            Data data = getTile().getContainer().base.dataMap.get(planet);

            if (colony == null) {
                if (SpaceNet.instance.getColonieNet().canAddColony(planet, getTile().container.player))
                    getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 30 + 7, guiTop + 25, 0, 0.75f, 0.75f, 0, 125, 103, 20);
                else {
                    getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 30 + 7, guiTop + 25, 0, 0.75f, 0.75f, 0, 145, 103, 20);
                }

                getTile().drawCenteredText(poseStack, Localization.translate("iu.space.createcolony"), (float) (guiLeft + width / 2), guiTop + 25 + 4, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

            }
            if (colony == null)
                for (int i = 0; i < listBuildings.size(); i++) {
                    ItemStack stack = listBuildings.get(i);
                    if (!ModUtils.isEmpty(stack)) {
                        RenderSystem.enableBlend();
                        getTile().getItemRenderer().renderGuiItem(stack, 33 + i * 18 + guiLeft, 40 + guiTop);
                        getTile().getItemRenderer().renderGuiItemDecorations(Minecraft.getInstance().font, stack, 33 + i * 18 + guiLeft, 40 + guiTop);
                        RenderSystem.disableBlend();
                    }

                }
        } else {
            if (resourceList == null) {
                resourceList = new ArrayList<>();
                resourceList.addAll(SpaceNet.instance.getColonieNet().getItemsFromBody(colony.getBody()));
                resourceList.addAll(SpaceNet.instance.getColonieNet().getFluidsFromBody(colony.getBody()));
            }
            sliderStorageLength = colony.getStacksFromStorage().size() + colony.getFluidsFromStorage().size();
            sliderStorageLength /= 8D;
            sliderStorageLength = Math.floor(sliderStorageLength);
            sliderStorageLength += 1;
            sliderStorageVertex = 120D / sliderStorageLength;
            sliderStoragePosition = Math.min(sliderStoragePosition, sliderStorageLength - 1);
            sliderLength = resourceList.size();
            sliderLength /= 8D;
            sliderLength = Math.floor(sliderLength);
            sliderLength += 1;
            sliderVertex = 90D / sliderLength;
            sliderPosition = Math.min(sliderPosition, sliderLength - 1);
            int xPos = 36;
            int levelColony = colony.getLevel();
            if (levelColony >= 100) {
                xPos = 146;
            } else if (levelColony >= 75) {
                xPos = 124;
            } else if (levelColony >= 50) {
                xPos = 102;
            } else if (levelColony >= 25) {
                xPos = 102 - 22;
            } else if (levelColony >= 10) {
                xPos = 102 - 44;
            }
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 15, guiTop + 15, 0, 0.75f, 0.75f, xPos, 22, 22, 22);
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 18, 0, 0.75f, 0.75f, 1, 44, 76 - 0, 55 - 43);
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 18, 0, 0.75f, 0.75f, 78, 44, (int) (76 * (colony.getExperience() * 1D / colony.getMaxExperience())), 55 - 43);
            getTile().drawCenteredText(poseStack, String.valueOf(colony.getLevel()), (float) (guiLeft + 33) + 65, guiTop + 20, 20, 0.75f, ModUtils.convertRGBAcolorToInt(255, 255, 255));
            getTile().drawCenteredText(poseStack, String.valueOf(colony.getExperience() + "/" + colony.getMaxExperience()), (float) (guiLeft + 33) + 76 * 0.75f / 2, guiTop + 30, 20, 0.75f, ModUtils.convertRGBAcolorToInt(255, 255, 255));


            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 19, guiTop + 40, 0, 0.5f, 0.5f, 0, 57, 20, 20);
            getTile().drawCenteredText(poseStack, Localization.translate("iu.space_colony_people") + (colony.getFreeWorkers() + colony.getWorkers()), (float) (guiLeft + 19) + 42, guiTop + 42, 20, 0.75f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 19, guiTop + 40 + 15, 0, 0.6f, 0.6f, 21, 57, 22, 22);
            getTile().drawCenteredText(poseStack, Localization.translate("iu.space_colony_need") + colony.getNeedWorkers(), (float) (guiLeft + 19) + 40, guiTop + 44 + 15, 20, 0.75f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 21, guiTop + 40 + 15 + 17, 0, 0.5f, 0.5f, 160, 59, 20, 20);
            getTile().drawCenteredText(poseStack, Localization.translate("iu.space_colony_free") + colony.getFreeWorkers(), (float) (guiLeft + 19) + 44, guiTop + 44 + 15 + 15, 20, 0.75f, ModUtils.convertRGBAcolorToInt(255, 255, 255));


            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 21, guiTop + 40 + 15 + 15 + 15, 0, 0.6f, 0.6f, 62, 83, 17, 18);
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 40 + 15 + 15 + 15, 0, 0.75f, 0.75f, 1, 44, 76 - 0, 55 - 43);
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 40 + 15 + 15 + 15, 0, 0.75f, 0.75f, 78, 44, (int) (76 * (colony.getEnergy() * 1D / colony.getMaxEnergy())), 55 - 43);

            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 21, guiTop + 40 + 15 + 15 + 15 + 15, 0, 0.6f, 0.6f, 118, 86, 18, 18);
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 40 + 15 + 15 + 15 + 15, 0, 0.75f, 0.75f, 1, 44, 76 - 0, 55 - 43);
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 40 + 15 + 15 + 15 + 15, 0, 0.75f, 0.75f, 78, 44, (int) (76 * (colony.getFood() * 1D / colony.getFood())), 55 - 43);

            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 21, guiTop + 40 + 15 + 15 + 15 + 15 + 12, 0, 0.6f, 0.6f, 95, 81, 20, 22);
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 40 + 15 + 15 + 15 + 15 + 15, 0, 0.75f, 0.75f, 1, 44, 76 - 0, 55 - 43);
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 40 + 15 + 15 + 15 + 15 + 15, 0, 0.75f, 0.75f, 78, 44, (int) (76 * (colony.getOxygen() * 1D / colony.getMaxoxygen())), 55 - 43);

            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 21, guiTop + 40 + 15 + 15 + 15 + 15 + 15 + 15, 0, 0.5f, 0.5f, 33, 81, 24, 24);
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 40 + 15 + 15 + 15 + 15 + 15 + 15, 0, 0.75f, 0.75f, 1, 44, 76 - 0, 55 - 43);
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 40 + 15 + 15 + 15 + 15 + 15 + 15, 0, 0.75f, 0.75f, 78, 44, (int) (76 * (colony.getPercentEntertainment() - 0.8) / 0.7), 55 - 43);

            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 21, guiTop + 40 + 15 + 15 + 15 + 15 + 15 + 15 + 15 + 15, 0, 0.6f, 0.6f, 140, 84, 159 - 143, 79 - 61);
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 40 + 15 + 15 + 15 + 15 + 15 + 15 + 15 + 15, 0, 0.75f, 0.75f, 1, 44, 76 - 0, 55 - 43);
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 40 + 15 + 15 + 15 + 15 + 15 + 15 + 15 + 15, 0, 0.75f, 0.75f, 78, 44, (int) (76 * Math.min(1, (colony.getProtection() * 1D / colony
                    .getBuildingList()
                    .size() * 2))), 55 - 43);

            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 21, guiTop + 40 + 15 + 15 + 15 + 15 + 15 + 15 + 15, 0, 0.6f, 0.6f, 45, 61, 20, 18);
            getTile().drawCenteredText(poseStack, Localization.translate("iu.space_colony_available_building") + colony.getAvailableBuilding(), (float) (guiLeft + 19) + 48, guiTop + 44 + 15 + 15 + 15 + 15 + 15 + 15 + 15, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

            RenderSystem.enableBlend();
            new ImageSpaceInterface2(getTile(), 21, 40 + 15 + 15 + 15 + 15 + 15 + 15 + 15 + 15 + 15, 80, 17).drawBackground(poseStack, guiLeft, guiTop);
            RenderSystem.disableBlend();

            List<EnumProblems> problems = colony.getProblems();
            for (int i = 0; i < problems.size(); i++) {
                EnumProblems problem = problems.get(i);
                getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 25 + 12 * i, guiTop + 40 + 15 + 15 + 15 + 15 + 15 + 15 + 15 + 15 + 15 + 3, 0, 0.6f, 0.6f, problem.getX(), problem.getY(), problem.getWidth(), problem.getHeight());

            }
            RenderSystem.enableBlend();
            new ImageSpaceInterface1(tile, 105, 40 + 15 + 15 + 15 + 15 + 15 + 15 + 10, 32 + 51, 52).drawBackground(poseStack, guiLeft, guiTop);
            RenderSystem.disableBlend();

            RenderSystem.enableBlend();
            new ImageSpaceInterface1(tile, 105, 40 + 15 + 15 + 15 + 8, 32 + 51, 40).drawBackground(poseStack, guiLeft, guiTop);
            RenderSystem.disableBlend();

            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_slider.png"), guiLeft + 105 + 32 + 52, guiTop + 40 + 15 + 15 + 15 + 15 + 15 + 15 + 11, 0, 0.4f, 0.4f, 0, 0, 14, 124);
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_slider.png"), guiLeft + 105 + 32 + 52, guiTop + 40 + 15 + 15 + 15 + 15 + 15 + 15 + 15 + 45, 0, 0.4f, 0.4f, 0, 252, 14, 4);


            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_slider.png"), guiLeft + 105 + 32 + 52, guiTop + 41 + 15 + 15 + 15 + 8, 0, 0.4f, 0.4f, 0, 0, 14, 94);
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_slider.png"), guiLeft + 105 + 32 + 52, guiTop + 41 + 15 + 15 + 15 + 8 + 33 + 4, 0, 0.4f, 0.4f, 0, 252, 14, 4);
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_slider.png"), (float) (guiLeft + 105 + 32 + 52 + 1.25f), guiTop + 41 + 15 + 15 + 15 + 8 + 1.25f + (float) (sliderPosition * sliderVertex * 0.4f), 0, 0.37f, 0.4f, 15, 3, 8, (int) ((1) * sliderVertex));
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_slider.png"), (float) (guiLeft + 105 + 32 + 52 + 1.25f), guiTop + 40 + 15 + 15 + 15 + 15 + 15 + 15 + 11 + 1.25f + (float) (sliderStoragePosition * sliderStorageVertex * 0.4f), 0, 0.37f, 0.4f, 15, 3, 8, (int) ((1) * sliderStorageVertex));
            RenderSystem.enableBlend();
            new ImageSpaceInterface1(tile, 108, 21, 76, 14).drawBackground(poseStack, guiLeft, guiTop);
            RenderSystem.disableBlend();
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), (float) (guiLeft + 108) + 76 / 2 - 4, guiTop + 16 + 8, 0, 0.5f, 0.5f, 6, 83, 17, 17);
            int j = 0;
            for (int i = (int) (8 * sliderPosition); i < Math.min(resourceList.size(), (sliderPosition * 8) + 8); i++) {
                DataItem<?> baseResource = resourceList.get(i);
                if (baseResource.getElement() instanceof ItemStack) {
                    ItemStack stack = (ItemStack) baseResource.getElement();
                    RenderSystem.enableBlend();
                    pose.translate(0, 0, -100);
                    getTile().getItemRenderer().renderGuiItem(stack, ((25 + 32 + 53 + (j % 4) * 18)) + guiLeft, (int) ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + guiTop);
                    getTile().getItemRenderer().renderGuiItemDecorations(Minecraft.getInstance().font, stack, ((25 + 32 + 53 + (j % 4) * 18)) + guiLeft, (int) ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + guiTop);
                    RenderSystem.disableBlend();
                    pose.translate(0, 0, 100);
                } else {

                    int fluidX = ((25 + 32 + 53 + (j % 4) * 18)) + 1;
                    int fluidY = ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + 1;
                    int fluidWidth = 16;
                    int fluidHeight = 16;
                    Fluid fluid = ((FluidStack) baseResource.getElement()).getFluid();
                    IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                    TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture((FluidStack) baseResource.getElement()));
                    int color = extensions.getTintColor();
                    bindBlockTexture();
                    this.getTile().drawSprite(poseStack,
                            guiLeft + fluidX,
                            guiTop + fluidY,
                            fluidWidth,
                            fluidHeight,
                            sprite,
                            color,
                            1.0,
                            false,
                            false
                    );
                }
                j++;
            }
            j = 0;
            for (int i = (int) (8 * sliderStoragePosition); i < Math.min(colony.getStacksFromStorage().size() + colony.getFluidsFromStorage().size(), (sliderStoragePosition * 8) + 8); i++) {
                Object baseResource;
                if (i < colony.getStacksFromStorage().size()) {
                    baseResource = colony.getStacksFromStorage().get(i);
                } else {
                    baseResource = colony.getFluidsFromStorage().get(i - colony.getStacksFromStorage().size());
                }
                if (baseResource instanceof ItemStack) {
                    ItemStack stack = (ItemStack) baseResource;
                    RenderSystem.enableBlend();
                    pose.translate(0, 0, -100);
                    getTile().getItemRenderer().renderGuiItem(stack, ((25 + 32 + 53 + (j % 4) * 18)) + guiLeft, (int) ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + guiTop + 50);
                    getTile().getItemRenderer().renderGuiItemDecorations(Minecraft.getInstance().font, stack, ((25 + 32 + 53 + (j % 4) * 18)) + guiLeft, (int) ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + 50 + guiTop);
                    pose.translate(0, 0, 100);
                    RenderSystem.disableBlend();
                } else if (baseResource instanceof FluidStack) {
                    FluidStack fluidStack = (FluidStack) baseResource;
                    if (fluidStack.isEmpty() || fluidStack.getFluid() == Fluids.EMPTY)
                        continue;
                    int fluidX = ((25 + 32 + 53 + (j % 4) * 18)) + 1;
                    int fluidY = ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + 50 + 1;
                    int fluidWidth = 16;
                    int fluidHeight = 16;
                    Fluid fluid = ((FluidStack) baseResource).getFluid();
                    IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                    TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture((FluidStack) baseResource));
                    int color = extensions.getTintColor();
                    bindBlockTexture();
                    this.getTile().drawSprite(poseStack,
                            guiLeft + fluidX,
                            guiTop + fluidY,
                            fluidWidth,
                            fluidHeight,
                            sprite,
                            color,
                            1.0,
                            false,
                            false
                    );
                }
                j++;
            }
            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 101 + 7, guiTop + 55 - 17, 0, 0.75f, 0.75f, 152, 121, 103, 20);
            if (getTile().container.base.getSends() != null) {
                List<Timer> timers = getTile().container.base.getSends().getTimers();
                if (timers != null && !timers.isEmpty()) {
                    getTile().drawCenteredText(poseStack, Localization.translate("iu.space.operation_active"), guiLeft + 100 + 7 + (103 / 2f) * 0.75f, guiTop + 59 - 17, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

                } else {
                    getTile().drawCenteredText(poseStack, Localization.translate("iu.space.operation_inactive"), guiLeft + 100 + 7 + (103 / 2f) * 0.75f, guiTop + 59 - 17, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

                }
            } else {
                getTile().drawCenteredText(poseStack, Localization.translate("iu.space.operation_inactive"), guiLeft + 100 + 7 + (103 / 2f) * 0.75f, guiTop + 59 - 17, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

            }

            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 100 + 7, guiTop + 55, 0, 0.75f, 0.75f, 0, 105, 103, 20);
            getTile().drawCenteredText(poseStack, Localization.translate("iu.space.send_item"), guiLeft + 100 + 7 + (103 / 2f) * 0.75f, guiTop + 59, 20, 0.63f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

            getTile().drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 100 + 7, guiTop + 55 + 20, 0, 0.75f, 0.75f, 0, 105, 103, 20);
            getTile().drawCenteredText(poseStack, Localization.translate("iu.space.auto_" + getTile().container.base.colony.isAuto()), guiLeft + 100 + 7 + (103 / 2f) * 0.75f, guiTop + 59 + 21, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

        }
    }


    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, ScrollDirection direction) {
        int guiLeft = this.x + offsetX1;
        int guiTop = this.y + offsetY1;
        int pos1 = guiLeft + 105 + 32 + 52;
        int pos2 = guiTop + 41 + 15 + 15 + 15 + 8 + 33 + 4;
        int pos3 = guiTop + 41 + 15 + 15 + 15 + 8;
        int x1 = 6;
        int y1 = 50;
        int y2 = 36;
        if (direction != ScrollDirection.stopped) {
            if (mouseX >= pos1 && mouseX <= pos1 + x1 && mouseY >= pos2 && mouseY <= pos2 + y1) {
                if (direction == ScrollDirection.up) {
                    sliderStoragePosition--;
                    if (sliderStoragePosition < 0)
                        sliderStoragePosition = 0;
                } else {
                    sliderStoragePosition++;
                    sliderStoragePosition = Math.min(sliderStoragePosition, sliderStorageLength - 1);
                }
            }
            if (mouseX >= pos1 && mouseX <= pos1 + x1 && mouseY >= pos3 && mouseY <= pos3 + y2) {
                if (direction == ScrollDirection.up) {
                    sliderPosition--;
                    if (sliderPosition < 0)
                        sliderPosition = 0;
                } else {
                    sliderPosition++;
                    sliderPosition = Math.min(sliderPosition, sliderLength - 1);
                }
            }
        }
        return super.mouseScrolled(mouseX, mouseY, direction);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {
        if (hoverDelete) {
            this.getTile().defaultResearchGuis.remove(this);
            getTile().getContainer().player.playSound(EnumSound.button.getSoundEvent(), 0.5F, 1);
            return true;

        }

        if (mouseX >= x + offsetX1 + 108 && mouseX <= x + offsetX1 + 108 + 76 && mouseY >= y + offsetY1 + 21 && mouseY <= y + offsetY1 + 21 + 14) {
            new PacketAddBuildingToColony(this.getTile().container.base.colony);
        }


        if (mouseX >= x + offsetX1 + 100 + 7 && mouseX <= x + offsetX1 + 100 + 7 + 103 * 0.75 && mouseY >= y + offsetY1 + 55 && mouseY <= y + offsetY1 + 55 + 20 * 0.75) {
            if (getTile().container.player.getUUID().equals(getTile().container.base.getPlayer())) {
                new PacketSendResourceToEarth(
                        getTile().container.player,
                        getTile().container.base.body
                );
            }
        }
        if (mouseX >= x + offsetX1 + 100 + 7 && mouseX <= x + offsetX1 + 100 + 7 + 103 * 0.75 && mouseY >= y + offsetY1 + 55 + 20 && mouseY <= y + offsetY1 + 55 + 20 + 20 * 0.75) {
            if (getTile().container.player.getUUID().equals(getTile().container.base.getPlayer())) {
                new PacketCreateAutoSends(
                        getTile().container.player,
                        getTile().container.base.body
                );
            }
        }
        if (hoverCreate) {
            if (getTile().getContainer().player.getUUID().equals(getTile().getContainer().base.getPlayer())) {
                new PacketCreateColony(
                        getTile().getContainer().player,
                        getTile().getContainer().base.body
                );
            }
            getTile().getContainer().player.playSound(EnumSound.button.getSoundEvent(), 0.5F, 1);
            return true;

        }
        return super.mouseClicked(mouseX, mouseY);

    }
}
