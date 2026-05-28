package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.colonies.DataItem;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.colonies.api.building.IColonyMiningFactory;
import com.denfop.api.space.colonies.building.FluidFactory;
import com.denfop.api.space.colonies.building.ItemFactory;
import com.denfop.api.space.colonies.enums.EnumMiningFactory;
import com.denfop.api.space.colonies.enums.EnumProblems;
import com.denfop.api.space.fakebody.Data;
import com.denfop.api.widget.*;
import com.denfop.componets.Fluids;
import com.denfop.network.packet.*;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

import static com.denfop.api.widget.ScreenWidget.bindBlockTexture;
import static com.denfop.api.widget.TankWidget.getSafeFluidSprite;
import static com.denfop.screen.ScreenIndustrialUpgrade.bindTexture;

public class ScreenColony extends ScreenDefaultResearchTable {

    boolean hoverDelete;
    boolean hoverCreate;
    double sliderPosition;
    double sliderVertex;
    double sliderLength;
    double sliderStoragePosition;
    double sliderStorageVertex;
    double sliderStorageLength;

    List<ItemStack> listBuildings = List.of(
            new ItemStack(IUItem.colonial_building.getStack(4), 3),
            new ItemStack(IUItem.colonial_building.getStack(19), 1),
            new ItemStack(IUItem.colonial_building.getStack(0), 1),
            new ItemStack(IUItem.colonial_building.getStack(16), 1),
            new ItemStack(IUItem.colonial_building.getStack(13), 2)
    );

    List<DataItem<?>> resourceList = null;

    public ScreenColony(ScreenResearchTableSpace tileEntityResearchTableSpace) {
        super(tileEntityResearchTableSpace, 25, 25, 150, 150);
    }

    private static <T> List<T> snapshot(List<T> source) {
        return source == null || source.isEmpty() ? List.of() : new ArrayList<>(source);
    }

    private static int getPageCount(int total, int pageSize) {
        return Math.max(1, (int) Math.ceil(total / (double) pageSize));
    }

    @Override
    public void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);

        IBody planet = this.getTile().focusedPlanet;
        IColony colony = getTile().getContainer().base.colony;

        hoverDelete = false;
        hoverCreate = false;

        if (par1 >= this.x + offsetX1 + width - 20
                && par2 <= this.x + offsetX1 + width - 20 + 15
                && par2 >= this.y + offsetY1 + 5
                && par2 <= this.y + offsetY1 + 20) {
            hoverDelete = true;
        }

        if (colony == null
                && par1 >= this.x + offsetX1 + 37
                && par2 <= this.x + offsetX1 + 37 + 103 * 0.75
                && par2 >= this.y + offsetY1 + 25
                && par2 <= this.y + offsetY1 + 25 + 20 * 0.75) {
            hoverCreate = true;
        }

        if (colony == null) {
            for (int i = 0; i < listBuildings.size(); i++) {
                ItemStack stack = listBuildings.get(i);
                new ItemStackWidget(tile, this.x + offsetX1 + 33 + i * 18, this.y + offsetY1 + 40, () -> stack)
                        .drawForeground(poseStack, par1, par2);
            }
        }

        if (colony != null) {
            int guiLeft = this.x + offsetX1;
            int guiTop = this.y + offsetY1;

            new TooltipWidget(tile, guiLeft + 33, guiTop + 40 + 15 + 15 + 15, (int) (76 * 0.75), (int) (12 * 0.75))
                    .withTooltip(
                            Localization.translate("iu.space_colony_energy") + ModUtils.getString(colony.getEnergy()) + "/" + ModUtils.getString(colony.getMaxEnergy()) + " EF"
                                    + "\n" + Localization.translate("iu.space_colony_using_energy") + ModUtils.getString(colony.getUsingEnergy()) + " EF/t"
                                    + "\n" + Localization.translate("iu.space_colony_generation_energy") + ModUtils.getString(colony.getGenerationEnergy()) + " EF/t"
                    )
                    .drawForeground(poseStack, par1, par2);

            new TooltipWidget(tile, guiLeft + 33, guiTop + 40 + 15 + 15 + 15 + 15, (int) (76 * 0.75), (int) (12 * 0.75))
                    .withTooltip(
                            Localization.translate("iu.space_colony_food") + ModUtils.getString(colony.getFood()) + "/" + ModUtils.getString((colony.getFreeWorkers() + colony.getWorkers()) * 15)
                                    + "\n" + Localization.translate("iu.space_colony_using_food") + ModUtils.getString(colony.getUsingFood()) + "t"
                                    + "\n" + Localization.translate("iu.space_colony_generation_food") + ModUtils.getString(colony.getGenerationFood()) + "t"
                    )
                    .drawForeground(poseStack, par1, par2);

            new TooltipWidget(tile, guiLeft + 33, guiTop + 40 + 15 + 15 + 15 + 15 + 15, (int) (76 * 0.75), (int) (12 * 0.75))
                    .withTooltip(
                            Localization.translate("iu.space_colony_oxygen") + ModUtils.getString(colony.getOxygen()) + "/" + ModUtils.getString(colony.getMaxOxygen())
                                    + "\n" + Localization.translate("iu.space_colony_generation_oxygen") + ModUtils.getString(colony.getGenerationOxygen()) + "t"
                                    + "\n" + Localization.translate("iu.space_colony_using_oxygen") + ModUtils.getString(colony.getUsingOxygen()) + "t"
                    )
                    .drawForeground(poseStack, par1, par2);

            new TooltipWidget(tile, guiLeft + 33, guiTop + 40 + 15 + 15 + 15 + 15 + 15 + 15, (int) (76 * 0.75), (int) (12 * 0.75))
                    .withTooltip(Localization.translate("iu.space_colony_entertainment") + ModUtils.getString(colony.getPercentEntertainment() * 100) + "%")
                    .drawForeground(poseStack, par1, par2);

            new TooltipWidget(tile, guiLeft + 33, guiTop + 40 + 15 + 15 + 15 + 15 + 15 + 15 + 30, (int) (76 * 0.75), (int) (12 * 0.75))
                    .withTooltip(
                            Localization.translate("iu.space_colony_protection") + colony.getProtection()
                                    + "\n" + Localization.translate("iu.space_colony_need_protection") + colony.getBuildingList().size() * 2
                    )
                    .drawForeground(poseStack, par1, par2);

            int j = 0;

            if (this.resourceList != null) {
                int amountResourceByLevelItem = 0;
                int amountResourceByLevelFluid = 0;
                int[] amountFactoryByType = new int[]{0, 0, 0, 0, 0, 0};

                for (DataItem<?> baseResource : resourceList) {
                    if (baseResource.getElement() instanceof ItemStack && colony.getLevel() >= baseResource.getLevel()) {
                        amountResourceByLevelItem++;
                    }
                    if (baseResource.getElement() instanceof FluidStack && colony.getLevel() >= baseResource.getLevel()) {
                        amountResourceByLevelFluid++;
                    }
                }

                for (IColonyMiningFactory colonyBuilding : colony.getBuildingMiningList()) {
                    if (colonyBuilding instanceof ItemFactory factory) {
                        amountFactoryByType[factory.getFactory().ordinal()]++;
                    }
                    if (colonyBuilding instanceof FluidFactory factory) {
                        amountFactoryByType[factory.getFactory().ordinal() + EnumMiningFactory.values().length]++;
                    }
                }

                double checksPerMinute = 60.0 / 5.0;
                double k = colony.getPercentEntertainment();
                EnumMiningFactory[] types = EnumMiningFactory.values();

                double totalItemPerMinute = 0.0;
                double totalFluidPerMinute = 0.0;

                for (int t = 0; t < types.length; t++) {
                    EnumMiningFactory type = types[t];
                    double p = type.getChance() / 100.0;
                    double eItem = (type.getMaxItemValue() + 1.0) / 2.0;
                    double eFluid = (type.getMaxValue() + 1.0) / 4.0 + (type.getMaxValue() + 1.0) / 2.0;

                    int itemFactories = amountFactoryByType[t];
                    int fluidFactories = amountFactoryByType[t + types.length];

                    totalItemPerMinute += itemFactories * checksPerMinute * p * eItem * k;
                    totalFluidPerMinute += fluidFactories * checksPerMinute * p * eFluid * k;
                }

                double avgOneItemPerMinute =
                        (amountResourceByLevelItem == 0) ? 0 : Math.min(totalItemPerMinute, colony.getMaxAvailableItem()) / amountResourceByLevelItem;

                double avgOneFluidPerMinute =
                        (amountResourceByLevelFluid == 0) ? 0 : Math.min(totalFluidPerMinute, colony.getMaxAvailableFluid()) / amountResourceByLevelFluid;

                int resourceStart = Math.max(0, (int) (sliderPosition * 8));
                int resourceEnd = Math.min(resourceList.size(), resourceStart + 8);

                for (int i = resourceStart; i < resourceEnd; i++) {
                    DataItem<?> baseResource = resourceList.get(i);

                    if (baseResource.getElement() instanceof ItemStack stack) {
                        RenderSystem.enableBlend();
                        new ItemStackTooltipWidget(
                                tile,
                                ((25 + 32 + 53 + (j % 4) * 18)) + guiLeft,
                                (int) ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + guiTop,
                                () -> stack,
                                Localization.translate("iu.space.colony_level") + " " + baseResource.getLevel()
                                        + (colony.getLevel() >= baseResource.getLevel()
                                        ? ("\n" + Localization.translate("iu.tooltip.mode.mining") + " " + ModUtils.getString(avgOneItemPerMinute) + Localization.translate("iu.colony.mining.per_minute"))
                                        : "")
                        ).drawForeground(poseStack, par1, par2);
                        RenderSystem.disableBlend();

                    } else if (baseResource.getElement() instanceof FluidStack stack) {
                        int fluidX = guiLeft + ((25 + 32 + 53 + (j % 4) * 18)) + 1;
                        int fluidY = guiTop + ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + 1;

                        new FluidDefaultWidget(tile, fluidX, fluidY, stack)
                                .withTooltip(() -> Localization.translate("iu.space.colony_level") + " " + baseResource.getLevel()
                                        + (colony.getLevel() >= baseResource.getLevel()
                                        ? ("\n" + Localization.translate("iu.tooltip.mode.mining") + " " + ModUtils.getString(avgOneFluidPerMinute) + "mB" + Localization.translate("iu.colony.mining.per_minute"))
                                        : ""))
                                .drawForeground(poseStack, par1, par2);
                    }

                    j++;
                }
            }

            j = 0;

            List<ItemStack> storageStacks = snapshot(colony.getStacksFromStorage());
            List<FluidStack> storageFluids = snapshot(colony.getFluidsFromStorage());

            int storageStacksSize = storageStacks.size();
            int storageFluidsSize = storageFluids.size();
            int storageTotalSize = storageStacksSize + storageFluidsSize;

            int storageStart = Math.max(0, (int) (sliderStoragePosition * 8));
            int storageEnd = Math.min(storageTotalSize, storageStart + 8);

            for (int i = storageStart; i < storageEnd; i++) {
                Object baseResource = i < storageStacksSize
                        ? storageStacks.get(i)
                        : storageFluids.get(i - storageStacksSize);

                if (baseResource instanceof ItemStack stack) {
                    new ItemStackWidget(
                            tile,
                            ((25 + 32 + 53 + (j % 4) * 18)) + guiLeft,
                            (int) ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + guiTop + 50,
                            () -> stack
                    ).drawForeground(poseStack, par1, par2);

                } else if (baseResource instanceof FluidStack fluidStack) {
                    if (fluidStack.isEmpty() || fluidStack.getFluid() == Fluids.EMPTY) {
                        continue;
                    }

                    int fluidX = guiLeft + ((25 + 32 + 53 + (j % 4) * 18)) + 1;
                    int fluidY = guiTop + ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + 50 + 1;

                    new FluidDefaultWidget(tile, fluidX, fluidY, fluidStack).drawForeground(poseStack, par1, par2);
                }

                j++;
            }

            if (getTile().container.player.containerMenu.getCarried().isEmpty()) {
                new TooltipWidget(getTile(), guiLeft + 108, guiTop + 21, 76, 14)
                        .withTooltip(Localization.translate("iu.space.colony_info_add"))
                        .drawForeground(poseStack, par1, par2);
            }

            if (getTile().container.base.getSends() != null) {
                List<Timer> timers = getTile().container.base.getSends().getTimers();
                if (timers != null && !timers.isEmpty()) {
                    new TooltipWidget(getTile(), guiLeft + 108, guiTop + 55 - 17, (int) (103 * 0.75), (int) (20 * 0.75))
                            .withTooltip(timers.get(0).getDisplay())
                            .drawForeground(poseStack, par1, par2);
                }
            }
        }
    }

    @Override
    public void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        IColony colony = getTile().getContainer().base.colony;
        if (colony != null) {
            this.width = 200;
            this.height = 200;
        }

        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);

        this.getTile().drawString(
                poseStack,
                Localization.translate("iu.space.planet.colony"),
                getTile().guiLeft + this.x + offsetX1 + width / 2 - tile.getStringWidth(Localization.translate("iu.space.planet.colony")) / 2,
                getTile().guiTop + this.y + offsetY1 + 5,
                ModUtils.convertRGBAcolorToInt(255, 255, 255)
        );

        IBody planet = this.getTile().focusedPlanet;
        PoseStack pose = poseStack.pose();

        int guiLeft = getTile().guiLeft + this.x + offsetX1;
        int guiTop = getTile().guiTop + this.y + offsetY1;

        pose.pushPose();
        pose.translate(guiLeft + width - 20, guiTop + 5, 20);
        bindTexture(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_other.png"));
        pose.scale(0.5f, 0.5f, 1);

        if (!hoverDelete) {
            getTile().drawTexturedModalRect(poseStack, 0, 0, 139, 32, 30, 30);
        } else {
            getTile().drawTexturedModalRect(poseStack, 0, 0, 170, 32, 30, 30);
        }

        pose.popPose();

        if (colony == null) {
            Data data = getTile().getContainer().base.dataMap.get(planet);

            if (SpaceNet.instance.getColonieNet().canAddColony(planet, getTile().container.player)) {
                getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 30 + 7, guiTop + 25, 0, 0.75f, 0.75f, 0, 125, 103, 20);
            } else {
                getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 30 + 7, guiTop + 25, 0, 0.75f, 0.75f, 0, 145, 103, 20);
            }

            getTile().drawCenteredText(poseStack, Localization.translate("iu.space.createcolony"), (float) (guiLeft + width / 2), guiTop + 25 + 4, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

            for (int i = 0; i < listBuildings.size(); i++) {
                ItemStack stack = listBuildings.get(i);
                if (!ModUtils.isEmpty(stack)) {
                    RenderSystem.enableBlend();
                    poseStack.renderItem(stack, 33 + i * 18 + guiLeft, 40 + guiTop);
                    poseStack.renderItemDecorations(Minecraft.getInstance().font, stack, 33 + i * 18 + guiLeft, 40 + guiTop);
                    RenderSystem.disableBlend();
                }
            }

        } else {
            if (resourceList == null) {
                resourceList = new ArrayList<>();
                resourceList.addAll(SpaceNet.instance.getColonieNet().getItemsFromBody(colony.getBody()));
                resourceList.addAll(SpaceNet.instance.getColonieNet().getFluidsFromBody(colony.getBody()));
            }

            List<ItemStack> storageStacks = snapshot(colony.getStacksFromStorage());
            List<FluidStack> storageFluids = snapshot(colony.getFluidsFromStorage());
            int storageTotalSize = storageStacks.size() + storageFluids.size();

            sliderStorageLength = getPageCount(storageTotalSize, 8);
            sliderStorageVertex = 120D / sliderStorageLength;
            sliderStoragePosition = Math.min(sliderStoragePosition, sliderStorageLength - 1);

            sliderLength = getPageCount(resourceList.size(), 8);
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
                xPos = 80;
            } else if (levelColony >= 10) {
                xPos = 58;
            }

            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 15, guiTop + 15, 0, 0.75f, 0.75f, xPos, 22, 22, 22);
            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 18, 0, 0.75f, 0.75f, 1, 44, 76, 12);
            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 18, 0, 0.75f, 0.75f, 78, 44, (int) (76 * (colony.getExperience() * 1D / colony.getMaxExperience())), 12);
            getTile().drawCenteredText(poseStack, String.valueOf(colony.getLevel()), (float) (guiLeft + 33) + 65, guiTop + 20, 20, 0.75f, ModUtils.convertRGBAcolorToInt(255, 255, 255));
            getTile().drawCenteredText(poseStack, colony.getExperience() + "/" + colony.getMaxExperience(), (float) (guiLeft + 33) + 76 * 0.75f / 2, guiTop + 30, 20, 0.75f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 19, guiTop + 40, 0, 0.5f, 0.5f, 0, 57, 20, 20);
            getTile().drawCenteredText(poseStack, Localization.translate("iu.space_colony_people") + (colony.getFreeWorkers() + colony.getWorkers()), (float) (guiLeft + 19) + 42, guiTop + 42, 20, 0.75f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 19, guiTop + 55, 0, 0.6f, 0.6f, 21, 57, 22, 22);
            getTile().drawCenteredText(poseStack, Localization.translate("iu.space_colony_need") + colony.getNeedWorkers(), (float) (guiLeft + 19) + 40, guiTop + 59, 20, 0.75f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 21, guiTop + 40 + 15 + 17, 0, 0.5f, 0.5f, 160, 59, 20, 20);
            getTile().drawCenteredText(poseStack, Localization.translate("iu.space_colony_free") + colony.getFreeWorkers(), (float) (guiLeft + 19) + 44, guiTop + 74, 20, 0.75f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 21, guiTop + 85, 0, 0.6f, 0.6f, 62, 83, 17, 18);
            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 85, 0, 0.75f, 0.75f, 1, 44, 76, 12);
            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 85, 0, 0.75f, 0.75f, 78, 44, (int) (76 * (colony.getEnergy() * 1D / Math.max(1, colony.getMaxEnergy()))), 12);

            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 21, guiTop + 100, 0, 0.6f, 0.6f, 118, 86, 18, 18);
            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 100, 0, 0.75f, 0.75f, 1, 44, 76, 12);

            int maxFood = Math.max(1, (colony.getFreeWorkers() + colony.getWorkers()) * 15);
            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 100, 0, 0.75f, 0.75f, 78, 44, (int) (76 * (colony.getFood() * 1D / maxFood)), 12);

            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 21, guiTop + 117, 0, 0.6f, 0.6f, 95, 81, 20, 22);
            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 115, 0, 0.75f, 0.75f, 1, 44, 76, 12);
            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 115, 0, 0.75f, 0.75f, 78, 44, (int) (76 * (colony.getOxygen() * 1D / Math.max(1, colony.getMaxOxygen()))), 12);

            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 21, guiTop + 130, 0, 0.5f, 0.5f, 33, 81, 24, 24);
            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 130, 0, 0.75f, 0.75f, 1, 44, 76, 12);
            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 130, 0, 0.75f, 0.75f, 78, 44, (int) (76 * Math.max(0, (colony.getPercentEntertainment() - 0.8) / 0.7)), 12);

            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 21, guiTop + 160, 0, 0.6f, 0.6f, 140, 84, 16, 18);
            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 33, guiTop + 160, 0, 0.75f, 0.75f, 1, 44, 76, 12);
            getTile().drawRect(
                    poseStack,
                    ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"),
                    guiLeft + 33,
                    guiTop + 160,
                    0,
                    0.75f,
                    0.75f,
                    78,
                    44,
                    (int) (76 * Math.min(1, colony.getProtection() * 1D / Math.max(1, colony.getBuildingList().size() * 2))),
                    12
            );

            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 21, guiTop + 145, 0, 0.6f, 0.6f, 45, 61, 20, 18);
            getTile().drawCenteredText(poseStack, Localization.translate("iu.space_colony_available_building") + colony.getAvailableBuilding(), (float) (guiLeft + 19) + 48, guiTop + 149, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

            RenderSystem.enableBlend();
            new SpaceMainRedInterfaceWidget(getTile(), 21, 175, 80, 17).drawBackground(poseStack, guiLeft, guiTop);
            RenderSystem.disableBlend();

            List<EnumProblems> problems = colony.getProblems();
            for (int i = 0; i < problems.size(); i++) {
                EnumProblems problem = problems.get(i);
                getTile().drawRect(
                        poseStack,
                        ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"),
                        guiLeft + 25 + 12 * i,
                        guiTop + 178,
                        0,
                        0.6f,
                        0.6f,
                        problem.getX(),
                        problem.getY(),
                        problem.getWidth(),
                        problem.getHeight()
                );
            }

            RenderSystem.enableBlend();
            new SpaceMainBlueInterfaceWidget(tile, 105, 130, 83, 52).drawBackground(poseStack, guiLeft, guiTop);
            RenderSystem.disableBlend();

            RenderSystem.enableBlend();
            new SpaceMainBlueInterfaceWidget(tile, 105, 93, 83, 40).drawBackground(poseStack, guiLeft, guiTop);
            RenderSystem.disableBlend();

            RenderSystem.enableBlend();
            new SpaceMainBlueInterfaceWidget(tile, (width / 2) - 40, 198, 80, 15).drawBackground(poseStack, guiLeft, guiTop);
            RenderSystem.disableBlend();

            getTile().drawCenteredText(poseStack, Localization.translate("iu.space.deletecolony"), (float) (guiLeft + width / 2), guiTop + 203.5f, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_slider.png"), guiLeft + 189, guiTop + 131, 0, 0.4f, 0.4f, 0, 0, 14, 124);
            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_slider.png"), guiLeft + 189, guiTop + 175, 0, 0.4f, 0.4f, 0, 252, 14, 4);

            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_slider.png"), guiLeft + 189, guiTop + 94, 0, 0.4f, 0.4f, 0, 0, 14, 94);
            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_slider.png"), guiLeft + 189, guiTop + 127, 0, 0.4f, 0.4f, 0, 252, 14, 4);

            getTile().drawRect(
                    poseStack,
                    ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_slider.png"),
                    (float) (guiLeft + 190.25f),
                    guiTop + 95.25f + (float) (sliderPosition * sliderVertex * 0.4f),
                    0,
                    0.37f,
                    0.4f,
                    15,
                    3,
                    8,
                    (int) (1 * sliderVertex)
            );

            getTile().drawRect(
                    poseStack,
                    ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_slider.png"),
                    (float) (guiLeft + 190.25f),
                    guiTop + 132.25f + (float) (sliderStoragePosition * sliderStorageVertex * 0.4f),
                    0,
                    0.37f,
                    0.4f,
                    15,
                    3,
                    8,
                    (int) (1 * sliderStorageVertex)
            );

            RenderSystem.enableBlend();
            new SpaceMainBlueInterfaceWidget(tile, 108, 21, 76, 14).drawBackground(poseStack, guiLeft, guiTop);
            RenderSystem.disableBlend();

            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), (float) (guiLeft + 108) + 76 / 2 - 4, guiTop + 24, 0, 0.5f, 0.5f, 6, 83, 17, 17);

            int j = 0;
            int resourceStart = Math.max(0, (int) (sliderPosition * 8));
            int resourceEnd = Math.min(resourceList.size(), resourceStart + 8);

            for (int i = resourceStart; i < resourceEnd; i++) {
                DataItem<?> baseResource = resourceList.get(i);

                if (baseResource.getElement() instanceof ItemStack stack) {
                    RenderSystem.enableBlend();
                    pose.translate(0, 0, -100);
                    poseStack.renderItem(stack, ((25 + 32 + 53 + (j % 4) * 18)) + guiLeft, (int) ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + guiTop);
                    poseStack.renderItemDecorations(Minecraft.getInstance().font, stack, ((25 + 32 + 53 + (j % 4) * 18)) + guiLeft, (int) ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + guiTop);
                    RenderSystem.disableBlend();
                    pose.translate(0, 0, 100);

                } else if (baseResource.getElement() instanceof FluidStack fluidStack) {
                    int fluidX = ((25 + 32 + 53 + (j % 4) * 18)) + 1;
                    int fluidY = ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + 1;
                    int fluidWidth = 16;
                    int fluidHeight = 16;

                    Fluid fluid = fluidStack.getFluid();
                    IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                    TextureAtlasSprite sprite = getSafeFluidSprite(fluidStack);
                    int color = extensions.getTintColor();

                    bindBlockTexture();
                    this.getTile().drawSprite(
                            poseStack,
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
            int storageStacksSize = storageStacks.size();
            int storageStart = Math.max(0, (int) (sliderStoragePosition * 8));
            int storageEnd = Math.min(storageTotalSize, storageStart + 8);

            for (int i = storageStart; i < storageEnd; i++) {
                Object baseResource = i < storageStacksSize
                        ? storageStacks.get(i)
                        : storageFluids.get(i - storageStacksSize);

                if (baseResource instanceof ItemStack stack) {
                    RenderSystem.enableBlend();
                    pose.translate(0, 0, -100);
                    poseStack.renderItem(stack, ((25 + 32 + 53 + (j % 4) * 18)) + guiLeft, (int) ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + guiTop + 50);
                    poseStack.renderItemDecorations(Minecraft.getInstance().font, stack, ((25 + 32 + 53 + (j % 4) * 18)) + guiLeft, (int) ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + guiTop + 50);
                    pose.translate(0, 0, 100);
                    RenderSystem.disableBlend();

                } else if (baseResource instanceof FluidStack fluidStack) {
                    if (fluidStack.isEmpty() || fluidStack.getFluid() == Fluids.EMPTY) {
                        continue;
                    }

                    int fluidX = ((25 + 32 + 53 + (j % 4) * 18)) + 1;
                    int fluidY = ((41 + 15 + 15 + 15 + 11 + 18 * (j / 4))) + 51;
                    int fluidWidth = 16;
                    int fluidHeight = 16;

                    Fluid fluid = fluidStack.getFluid();
                    IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                    TextureAtlasSprite sprite = getSafeFluidSprite(fluidStack);
                    int color = extensions.getTintColor();

                    bindBlockTexture();
                    this.getTile().drawSprite(
                            poseStack,
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

            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 108, guiTop + 38, 0, 0.75f, 0.75f, 152, 121, 103, 20);

            if (getTile().container.base.getSends() != null) {
                List<Timer> timers = getTile().container.base.getSends().getTimers();
                if (timers != null && !timers.isEmpty()) {
                    getTile().drawCenteredText(poseStack, Localization.translate("iu.space.operation_active"), guiLeft + 108 + (103 / 2f) * 0.75f, guiTop + 42, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));
                } else {
                    getTile().drawCenteredText(poseStack, Localization.translate("iu.space.operation_inactive"), guiLeft + 108 + (103 / 2f) * 0.75f, guiTop + 42, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));
                }
            } else {
                getTile().drawCenteredText(poseStack, Localization.translate("iu.space.operation_inactive"), guiLeft + 108 + (103 / 2f) * 0.75f, guiTop + 42, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));
            }

            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 107, guiTop + 55, 0, 0.75f, 0.75f, 0, 105, 103, 20);
            getTile().drawCenteredText(poseStack, Localization.translate("iu.space.send_item"), guiLeft + 107 + (103 / 2f) * 0.75f, guiTop + 59, 20, 0.63f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 107, guiTop + 75, 0, 0.75f, 0.75f, 0, 105, 103, 20);
            getTile().drawCenteredText(poseStack, Localization.translate("iu.space.auto_" + getTile().container.base.colony.isAuto()), guiLeft + 107 + (103 / 2f) * 0.75f, guiTop + 80, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));
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
                    if (sliderStoragePosition < 0) {
                        sliderStoragePosition = 0;
                    }
                } else {
                    sliderStoragePosition++;
                    sliderStoragePosition = Math.min(sliderStoragePosition, sliderStorageLength - 1);
                }
            }

            if (mouseX >= pos1 && mouseX <= pos1 + x1 && mouseY >= pos3 && mouseY <= pos3 + y2) {
                if (direction == ScrollDirection.up) {
                    sliderPosition--;
                    if (sliderPosition < 0) {
                        sliderPosition = 0;
                    }
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
            new PacketAddBuildingToColony(this.getTile().container.base.colony, this.getTile().container.player);
        }

        if (mouseX >= x + offsetX1 + (width / 2) - 40 && mouseX <= x + offsetX1 + (width / 2) + 80 && mouseY >= y + offsetY1 + 198 && mouseY <= y + offsetY1 + 213) {
            if (getTile().container.player.getUUID().equals(getTile().container.base.getPlayer())) {
                new PacketDeleteColony(
                        getTile().container.player,
                        getTile().container.base.body
                );
                this.getTile().defaultResearchGuis.remove(this);
                getTile().getContainer().player.playSound(EnumSound.button.getSoundEvent(), 0.5F, 1);
                return true;
            }
        }

        if (mouseX >= x + offsetX1 + 107 && mouseX <= x + offsetX1 + 107 + 103 * 0.75 && mouseY >= y + offsetY1 + 55 && mouseY <= y + offsetY1 + 55 + 20 * 0.75) {
            if (getTile().container.player.getUUID().equals(getTile().container.base.getPlayer())) {
                new PacketSendResourceToEarth(
                        getTile().container.player,
                        getTile().container.base.body
                );
            }
        }

        if (mouseX >= x + offsetX1 + 107 && mouseX <= x + offsetX1 + 107 + 103 * 0.75 && mouseY >= y + offsetY1 + 75 && mouseY <= y + offsetY1 + 75 + 20 * 0.75) {
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