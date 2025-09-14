package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.tesseract.Channel;
import com.denfop.api.tesseract.TypeChannel;
import com.denfop.api.tesseract.TypeMode;
import com.denfop.api.widget.*;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuTesseract;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
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
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.denfop.recipes.BaseRecipes.getBlockStack;

public class ScreenTesseract<T extends ContainerMenuTesseract> extends ScreenMain<ContainerMenuTesseract> {

    private final ItemStack stack;
    private final TankWidget fluid;
    boolean hoverShowPublicChannels;
    boolean hoverShowInventory;
    boolean hoverShowOwnChannels;
    boolean hoverShowCreateChannels;
    double scrollPositionChannels;
    double posScrollPositionChannels;
    boolean hoverUpChannel;
    boolean hoverDownChannel;
    boolean hoverEnergyChannel;
    boolean hoverFluidChannel;
    boolean hoverItemChannel;
    boolean hoverExtractChannel;
    boolean hoverReceiveChannel;
    boolean hoverPublicChannel;
    boolean hoverDeleteChannel;
    boolean hoverChangeModeChannel;
    boolean hoverBack;
    int indexTesseract = 0;
    int indexMaxTesseract = 1;
    int indexPublicTesseract = 0;
    int indexPublicMaxTesseract = 1;
    int prevId = -1;
    private int guiScreenID = 0;
    private int index = 24;
    private int index1 = 24;

    public ScreenTesseract(ContainerMenuTesseract guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.stack = getBlockStack(BlockBaseMachine3Entity.tesseract);
        this.fluid = new TankWidget(
                this,
                147,
                10,
                16,
                53,
                container.base.getTank()
        ) {

            protected List<String> getToolTip() {
                List<String> ret = new ArrayList<>();
                FluidStack fs = container.base.getTank().getFluid();
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    if (this.tank instanceof Fluids.InternalFluidTank) {
                        Fluids.InternalFluidTank tank1 = (Fluids.InternalFluidTank) this.tank;
                        ret.add(Localization.translate("iu.tank.fluids"));
                        ret.addAll(tank1.getFluidList());
                    }
                } else if (!fs.isEmpty() && fs.getAmount() > 0) {
                    Fluid fluid = fs.getFluid();
                    if (fluid != null) {
                        ret.add(fluid.getFluidType().getDescription().getString() + ": " + fs.getAmount() + " " + Localization.translate("iu.generic.text.mb"));
                    } else {
                        ret.add("invalid fluid stack");
                    }
                } else {
                    ret.add(Localization.translate("iu.generic.text.empty"));
                }

                return ret;
            }

            @Override
            public void drawBackground(PoseStack poseStack, final int mouseX, final int mouseY) {

                FluidStack fs = container.base.getTank().getFluid();
                if (!fs.isEmpty() && fs.getAmount() > 0) {
                    int fluidX = this.x;
                    int fluidY = this.y;
                    int fluidWidth;
                    int fluidHeight;
                    fluidX += 4;
                    fluidY += 4;
                    fluidWidth = 9;
                    fluidHeight = 46;

                    Fluid fluid = fs.getFluid();
                    IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                    TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fs));
                    int color = extensions.getTintColor();
                    double renderHeight = (double) fluidHeight * ModUtils.limit(
                            (double) fs.getAmount() / (double) this.tank.getCapacity(),
                            0.0D,
                            1.0D
                    );
                    bindBlockTexture();
                    this.gui.drawSprite(poseStack, mouseX +
                                    fluidX,
                            mouseY + (double) (fluidY + fluidHeight) - renderHeight,
                            fluidWidth,
                            renderHeight,
                            sprite,
                            color,
                            1.0D,
                            false,
                            true
                    );
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    this.gui.bindTexture();
                    this.gui.drawTexturedModalRect(poseStack, this.gui.guiLeft + 97, this.gui.guiTop + 14, 191, 5, 7, 46);

                }


            }
        };
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 13 && mouseY >= 3 && mouseY <= 13) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.tesseract.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 8; i++) {
                compatibleUpgrades.add(Localization.translate("iu.tesseract.info" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX - 60, mouseY, text);
        }
    }

    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        blit(poseStack, this.getGuiLeft(), this.getGuiTop(), 0, 0, this.getXSize(), this.getYSize());
        String name = this.container.base.getDisplayName().getString();
        int textWidth = this.getStringWidth(name);
        float scale = 1.0f;


        if (textWidth > 120) {
            scale = 120f / textWidth;
        }

        PoseStack pose = poseStack;
        pose.pushPose();
        pose.scale(scale, scale, 1.0f);


        int centerX = this.guiLeft + this.imageWidth / 2;
        int textX = (int) ((centerX / scale) - (textWidth / 2.0f));
        int textY = (int) ((this.guiTop + 6) / scale);

        if (guiScreenID == 0)
            drawString(poseStack, Minecraft.getInstance().font, name, textX, textY, 4210752);
        pose.scale(1 / scale, 1 / scale, 1);

        pose.popPose();
    }

    @Override
    public boolean mouseScrolled(double d, double d2, double d3) {
        ScrollDirection scrollDirection = d3 != 0.0 ? (d3 < 0.0 ? ScrollDirection.down : ScrollDirection.up) : ScrollDirection.stopped;
        int mouseX = (int) (d - this.guiLeft);
        int mouseY = (int) (d2 - this.guiTop);
        if (guiScreenID == 2) {
            if (mouseX >= 20 && mouseY >= 17 && mouseX <= 155 && mouseY <= 69) {
                if (scrollDirection == ScrollDirection.down) {
                    indexTesseract++;

                    indexMaxTesseract = indexTesseract * 7 + 21;
                    indexMaxTesseract = Math.min(indexMaxTesseract, this.container.base.getChannels().size());
                    double temp = this.container.base.getChannels().size() / 7D;
                    temp -= 2;
                    if (indexTesseract >= temp)
                        indexTesseract--;
                    if (indexTesseract < 0)
                        indexTesseract = 0;
                }
                if (scrollDirection == ScrollDirection.up) {
                    indexTesseract--;
                    if (indexTesseract < 0)
                        indexTesseract = 0;
                    indexMaxTesseract = indexTesseract * 7 + 21;
                    indexMaxTesseract = Math.min(indexMaxTesseract, this.container.base.getChannels().size());

                }
            }
        }
        if (guiScreenID == 4) {
            if (mouseX >= 20 && mouseY >= 17 && mouseX <= 155 && mouseY <= 69) {
                if (scrollDirection == ScrollDirection.down) {
                    indexPublicTesseract++;

                    indexPublicMaxTesseract = indexPublicTesseract * 7 + 21;
                    indexPublicMaxTesseract = Math.min(indexPublicMaxTesseract, this.container.base.getPublicChannel().size());
                    double temp = this.container.base.getPublicChannel().size() / 7D;
                    temp -= 2;
                    if (indexPublicTesseract >= temp)
                        indexPublicTesseract--;
                    if (indexPublicTesseract < 0)
                        indexPublicTesseract = 0;

                }
                if (scrollDirection == ScrollDirection.up) {
                    indexPublicTesseract--;
                    if (indexPublicTesseract < 0)
                        indexPublicTesseract = 0;
                    indexPublicMaxTesseract = indexPublicTesseract * 7 + 21;
                    indexPublicMaxTesseract = Math.min(indexPublicMaxTesseract, this.container.base.getPublicChannel().size());

                }
            }
        }
        return super.mouseScrolled(d, d2, d3);
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        if (guiScreenID == 0)
            handleUpgradeTooltip(par1, par2);
        switch (guiScreenID) {
            case 0:
                hoverShowPublicChannels = false;
                hoverShowInventory = false;
                hoverShowOwnChannels = false;
                hoverShowCreateChannels = false;
                if (par1 >= 31 && par1 <= 54 && par2 >= 31 && par2 <= 54)
                    hoverShowCreateChannels = true;
                if (par1 >= 61 && par1 <= 84 && par2 >= 31 && par2 <= 54)
                    hoverShowOwnChannels = true;
                if (par1 >= 91 && par1 <= 114 && par2 >= 31 && par2 <= 54)
                    hoverShowPublicChannels = true;
                if (par1 >= 121 && par1 <= 164 && par2 >= 31 && par2 <= 54)
                    hoverShowInventory = true;
                if (hoverShowCreateChannels) {
                    new AdvancedTooltipWidget(this, 31, 31, 54, 54).withTooltip(Localization.translate("tesseract.create_channel")).drawForeground(poseStack, par1, par2);
                }
                if (hoverShowPublicChannels) {
                    new AdvancedTooltipWidget(this, 91, 31, 124, 54).withTooltip(Localization.translate("tesseract.public_channel")).drawForeground(poseStack, par1, par2);
                }
                if (hoverShowInventory) {
                    new AdvancedTooltipWidget(this, 121, 31, 164, 54).withTooltip(Localization.translate("tesseract.inventory")).drawForeground(poseStack, par1, par2);
                }
                if (hoverShowOwnChannels) {
                    new AdvancedTooltipWidget(this, 61, 31, 84, 54).withTooltip(Localization.translate("tesseract.lists")).drawForeground(poseStack, par1, par2);
                }


                break;
            case 1:
                if (this.container.base.channel == null) {
                    return;
                }
                hoverUpChannel = false;
                hoverDownChannel = false;
                hoverEnergyChannel = false;
                hoverFluidChannel = false;
                hoverItemChannel = false;
                hoverExtractChannel = false;
                hoverReceiveChannel = false;
                hoverPublicChannel = false;
                hoverDeleteChannel = false;
                hoverChangeModeChannel = false;
                hoverBack = false;
                if (par1 >= 56 && par2 >= 7 && par1 <= 56 + 19 && par2 <= 7 + 20) {
                    hoverEnergyChannel = true;
                    new TooltipWidget(this, 56, 7, 19, 20).withTooltip(Localization.translate("tesseract.energy")).drawForeground(
                            poseStack, par1,
                            par2
                    );
                }
                if (par1 >= 78 && par2 >= 7 && par1 <= 78 + 19 && par2 <= 7 + 20) {
                    hoverFluidChannel = true;
                    new TooltipWidget(this, 78, 7, 19, 20).withTooltip(Localization.translate("tesseract.fluid")).drawForeground(
                            poseStack, par1,
                            par2
                    );
                }
                if (par1 >= 100 && par2 >= 7 && par1 <= 100 + 19 && par2 <= 7 + 20) {
                    hoverItemChannel = true;
                    new TooltipWidget(this, 100, 7, 19, 20).withTooltip(Localization.translate("tesseract.item")).drawForeground(
                            poseStack, par1,
                            par2
                    );
                }
                if (par1 >= 7 && par2 >= 55 && par1 <= 7 + 14 && par2 <= 55 + 10) {
                    hoverUpChannel = true;
                    new TooltipWidget(this, 7, 55, 14, 10).withTooltip(Localization.translate("tesseract.plus")).drawForeground(
                            poseStack, par1,
                            par2
                    );
                }

                if (par1 >= 7 && par2 >= 67 && par1 <= 7 + 14 && par2 <= 67 + 10) {
                    hoverDownChannel = true;
                    new TooltipWidget(this, 7, 67, 14, 10).withTooltip(Localization.translate("tesseract.minus")).drawForeground(
                            poseStack, par1,
                            par2
                    );
                }
                if (par1 >= 67 && par2 >= 57 && par1 <= 67 + 19 && par2 <= 57 + 20) {
                    hoverReceiveChannel = true;
                    new TooltipWidget(this, 67, 57, 19, 20).withTooltip(Localization.translate("tesseract.sink")).drawForeground(
                            poseStack, par1,
                            par2
                    );
                }

                if (par1 >= 89 && par2 >= 57 && par1 <= 89 + 19 && par2 <= 57 + 20) {
                    hoverExtractChannel = true;
                    new TooltipWidget(this, 89, 57, 19, 20).withTooltip(Localization.translate("tesseract.source")).drawForeground(
                            poseStack, par1,
                            par2
                    );
                }

                if (par1 >= 149 && par2 >= 34 && par1 <= 149 + 19 && par2 <= 34 + 20) {
                    hoverPublicChannel = true;
                    new TooltipWidget(this, 149, 34, 19, 20)
                            .withTooltip(Localization.translate("tesseract.player_channel") + "- " + this.container.base.channel.getTesseract().getPlayer() + "\n" + Localization.translate("tesseract.private") + (container.base.channel.isPrivate() ? Localization.translate("iu.yes") : Localization.translate("iu.no")))
                            .drawForeground(
                                    poseStack, par1,
                                    par2
                            );
                }

                if (par1 >= 65 && par2 >= 35 && par1 <= 110 && par2 <= 50) {
                    hoverChangeModeChannel = true;
                    new AdvancedTooltipWidget(this, 65, 35, 110, 50).withTooltip(Localization.translate("tesseract.activate")).drawForeground(
                            poseStack, par1,
                            par2
                    );
                }
                if (par1 >= 149 && par2 >= 57 && par1 <= 149 + 19 && par2 <= 57 + 20) {
                    hoverDeleteChannel = true;
                    new TooltipWidget(this, 149, 57, 19, 20).withTooltip(Localization.translate("tesseract.delete")).drawForeground(
                            poseStack, par1,
                            par2
                    );
                }
                if (par1 >= 5 && par2 >= 5 && par1 <= 13 && par2 <= 16) {
                    hoverBack = true;

                }
                break;
            case 2:
                hoverBack = false;
                indexMaxTesseract = indexTesseract * 7 + 21;
                indexMaxTesseract = Math.min(indexMaxTesseract, this.container.base.getChannels().size());
                if (indexTesseract > Math.floor((double) this.container.base.getChannels().size() / 7) - 2)
                    indexTesseract--;
                if (indexTesseract < 0)
                    indexTesseract = 0;
                int j = 0;
                for (int i = indexTesseract * 7; i < Math.min(this.container.base.getChannels().size(), indexMaxTesseract); i++, j++) {
                    final Channel channel = this.container.base.getChannel(i);
                    new ItemStackTooltipWidget(this, 22 + ((j) % 7) * 17, 19 + ((j) / 7) * 17, () -> this.stack,
                            Localization.translate("tesseract.channel") + channel.getChannel() + "\n" +
                                    Localization.translate("tesseract.private") + (channel.isPrivate() ? Localization.translate("iu.yes") : Localization.translate("iu.no")) + "\n" +
                                    Localization.translate("tesseract.player") + channel.getTesseract().getPlayer() + "\n" +
                                    Localization.translate("tesseract.type_channel") + channel.getTypeChannel().name() + "\n" +
                                    Localization.translate("tesseract.mode") + channel.getMode().name() + "\n" +
                                    Localization.translate("tesseract.private") + (channel.isActive() ? Localization.translate("iu.yes") : Localization.translate("iu.no")) + "\n"

                    ).drawForeground(poseStack, par1, par2);
                }
                if (par1 >= 5 && par2 >= 5 && par1 <= 13 && par2 <= 16) {
                    hoverBack = true;

                }
                break;
            case 3:
                hoverBack = false;
                this.fluid.drawForeground(poseStack, par1, par2);
                List<ItemStack> itemStackList =
                        this.container.base
                                .getSlotItem()
                                .stream()
                                .filter(itemStack -> !itemStack.isEmpty())
                                .toList();
                for (int i = 0; i < itemStackList.size(); i++) {
                    final int finalI = i;
                    new TooltipWidget(this, 35 + (i % 6) * 20, 11 + (i / 6) * 22, 18, 18).withTooltip(() -> itemStackList
                            .get(finalI)
                            .getDisplayName().getString()).drawForeground(poseStack, par1, par2);
                }
                if (par1 >= 5 && par2 >= 5 && par1 <= 13 && par2 <= 16) {
                    hoverBack = true;

                }

                new AdvancedTooltipWidget(this, 7, 68, 168, 79).withTooltip(ModUtils.getString(Math.min(
                        this.container.base.getEnergy().getEnergy(),
                        this.container.base.getEnergy().getCapacity()
                )) + "/" + ModUtils.getString(this.container.base.getEnergy().getCapacity()) + " " +
                        "EF").drawForeground(poseStack, par1, par2);
                break;
            case 4:
                hoverBack = false;
                indexPublicMaxTesseract = indexPublicTesseract * 7 + 21;
                indexPublicMaxTesseract = Math.min(indexPublicMaxTesseract, this.container.base.getPublicChannel().size());
                if (indexPublicTesseract > Math.floor((double) this.container.base.getPublicChannel().size() / 7) - 2)
                    indexPublicTesseract--;
                if (indexPublicTesseract < 0)
                    indexPublicTesseract = 0;
                j = 0;
                for (int i = indexPublicTesseract * 7; i < Math.min(this.container.base.getPublicChannel().size(), indexPublicMaxTesseract); i++, j++) {
                    final Channel channel = this.container.base.getPublicChannel().get(i);
                    new ItemStackTooltipWidget(this, 22 + ((j) % 7) * 17, 19 + ((j) / 7) * 17, () -> this.stack,
                            Localization.translate("tesseract.channel") + channel.getChannel() + "\n" +
                                    Localization.translate("tesseract.private") + (channel.isPrivate() ? Localization.translate("iu.yes") : Localization.translate("iu.no")) + "\n" +
                                    Localization.translate("tesseract.player") + channel.getTesseract().getPlayer() + "\n" +
                                    Localization.translate("tesseract.type_channel") + channel.getTypeChannel().name() + "\n" +
                                    Localization.translate("tesseract.mode") + channel.getMode().name() + "\n" +
                                    Localization.translate("tesseract.private") + (channel.isActive() ? Localization.translate("iu.yes") : Localization.translate("iu.no")) + "\n"

                    ).drawForeground(poseStack, par1, par2);
                }
                if (par1 >= 5 && par2 >= 5 && par1 <= 13 && par2 <= 16) {
                    hoverBack = true;

                }

                break;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        switch (guiScreenID) {
            case 0:
                if (hoverShowCreateChannels)
                    drawTexturedModalRect(poseStack, this.guiLeft + 31, this.guiTop + 31,
                            232,
                            0, 24, 25
                    );
                if (hoverShowInventory)
                    drawTexturedModalRect(poseStack, this.guiLeft + 121, this.guiTop + 31,
                            232,
                            0, 24, 25
                    );
                if (hoverShowPublicChannels)
                    drawTexturedModalRect(poseStack, this.guiLeft + 91, this.guiTop + 31,
                            232,
                            0, 24, 25
                    );
                if (hoverShowOwnChannels)
                    drawTexturedModalRect(poseStack, this.guiLeft + 61, this.guiTop + 31,
                            232,
                            0, 24, 25
                    );
                break;
            case 1:
                if (this.container.base.channel == null) {
                    return;
                }
                this.drawXCenteredString(poseStack, 38, 64, String.valueOf(this.container.base.channel.getChannel()),
                        ModUtils.convertRGBcolorToInt(13,

                                229, 34
                        ), false
                );


                this.bindTexture();
                if (hoverReceiveChannel) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 67, this.guiTop + 57,
                            236,
                            81, 20, 21
                    );
                }
                if (hoverExtractChannel) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 89, this.guiTop + 57,
                            236,
                            81, 20, 21
                    );
                }
                if (hoverEnergyChannel) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 56, this.guiTop + 7,
                            236,
                            81, 20, 21
                    );
                }
                if (hoverFluidChannel) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 78, this.guiTop + 7,
                            236,
                            81, 20, 21
                    );
                }
                if (hoverItemChannel) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 100, this.guiTop + 7,
                            236,
                            81, 20, 21
                    );
                }

                if (hoverDeleteChannel) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 149, this.guiTop + 57,
                            236,
                            81, 20, 21
                    );
                }
                if (hoverUpChannel) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 7, this.guiTop + 55,
                            241,
                            103, 15, 12
                    );
                }
                if (hoverDownChannel) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 7, this.guiTop + 67,
                            241,
                            103, 15, 12
                    );
                }
                if (this.container.base.channel.getMode() == TypeMode.INOUT || this.container.base.channel.getMode() == TypeMode.INPUT) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 67, this.guiTop + 57,
                            215,
                            22, 19, 20
                    );
                }
                if (this.container.base.channel.getMode() == TypeMode.INOUT || this.container.base.channel.getMode() == TypeMode.OUTPUT) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 89, this.guiTop + 57,
                            236,
                            22, 19, 20
                    );
                }
                if (this.container.base.channel.getTypeChannel() == TypeChannel.ENERGY) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 56, this.guiTop + 7,
                            194,
                            0, 19, 20
                    );
                }
                if (this.container.base.channel.getTypeChannel() == TypeChannel.FLUID) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 78, this.guiTop + 7,
                            215,
                            0, 19, 20
                    );
                }
                if (this.container.base.channel.getTypeChannel() == TypeChannel.ITEM) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 100, this.guiTop + 7,
                            236,
                            0, 19, 20
                    );
                }
                if (hoverBack) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 6, this.guiTop + 6,
                            249,
                            130, 7, 10
                    );
                }
                if (this.container.base.channel.isPrivate()) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 149, this.guiTop + 33,
                            236,
                            58, 19, 20
                    );
                }
                if (this.container.base.channel.isActive()) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 66 + 22, this.guiTop + 36,
                            234,
                            44, 22, 14
                    );
                    if (hoverChangeModeChannel) {
                        drawTexturedModalRect(poseStack, this.guiLeft + 66 + 22, this.guiTop + 36,
                                234,
                                115, 22, 14
                        );
                    }
                } else {
                    drawTexturedModalRect(poseStack, this.guiLeft + 66, this.guiTop + 36,
                            211,
                            44, 22, 14
                    );
                    if (hoverChangeModeChannel) {
                        drawTexturedModalRect(poseStack, this.guiLeft + 66, this.guiTop + 36,
                                234,
                                115, 22, 14
                        );
                    }
                }
                if (hoverPublicChannel) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 149, this.guiTop + 33,
                            236,
                            81, 20, 21
                    );
                }
                break;
            case 2:
                this.bindTexture();
                RenderSystem.setShaderColor(1, 1, 1, 1);
                drawTexturedModalRect(poseStack, this.guiLeft + 145, (int) (this.guiTop + 18 + (50 - 18) * (indexTesseract / (Math.floor((double) this.container.base.getChannels().size() / 7D) - 2))),
                        246,
                        0, 10, 19
                );
                int j = 0;
                for (int i = indexTesseract * 7; i < Math.min(this.container.base.getChannels().size(), indexMaxTesseract); i++, j++) {
                    new ItemWidget(this, 22 + ((j) % 7) * 17, 19 + ((j) / 7) * 17, () -> this.stack).drawBackground(
                            poseStack, this.guiLeft,
                            this.guiTop
                    );
                }
                if (hoverBack) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 6, this.guiTop + 6,
                            249,
                            130, 7, 10
                    );
                }
                break;
            case 3:

                this.fluid.drawBackground(
                        poseStack, this.guiLeft,
                        this.guiTop
                );
                if (hoverBack) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 6, this.guiTop + 6,
                            249,
                            130, 7, 10
                    );
                }
                drawTexturedModalRect(poseStack, this.guiLeft + 56, this.guiTop + 7,
                        236,
                        81, 20, 21
                );
                drawTexturedModalRect(poseStack, this.guiLeft + 10, this.guiTop + 71,
                        10,
                        172, (int) (156 * container.base.getEnergy().getFillRatio()), 8
                );
                List<ItemStack> itemStackList =
                        this.container.base
                                .getSlotItem()
                                .stream()
                                .filter(itemStack -> !itemStack.isEmpty())
                                .collect(
                                        Collectors.toList());
                for (int i = 0; i < itemStackList.size(); i++) {

                    this.drawItemStack(35 + (i % 6) * 20, 11 + (i / 6) * 22, itemStackList.get(i));

                }
                break;
            case 4:
                bindTexture();
                RenderSystem.setShaderColor(1, 1, 1, 1);
                this.bindTexture();
                RenderSystem.setShaderColor(1, 1, 1, 1);
                drawTexturedModalRect(poseStack, this.guiLeft + 145, (int) (this.guiTop + 18 + (50 - 18) * ((indexPublicTesseract) / (Math.floor((double) this.container.base.getPublicChannel().size() / 7D) - 2))),
                        246,
                        0, 10, 19
                );
                j = 0;
                for (int i = indexPublicTesseract * 7; i < Math.min(this.container.base.getPublicChannel().size(), indexPublicMaxTesseract); i++, j++) {
                    new ItemWidget(this, 22 + ((j) % 7) * 17, 19 + ((j) / 7) * 17, () -> this.stack).drawBackground(
                            poseStack, this.guiLeft,
                            this.guiTop
                    );
                }
                if (hoverBack) {
                    drawTexturedModalRect(poseStack, this.guiLeft + 6, this.guiTop + 6,
                            249,
                            130, 7, 10
                    );
                }
                break;
        }
    }


    @Override
    protected void mouseClicked(final int i, final int j, final int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin;
        int y = j - yMin;
        switch (guiScreenID) {
            case 0:
                if (hoverShowCreateChannels) {
                    new PacketUpdateServerTile(this.container.base, 1);
                    guiScreenID = 1;
                    prevId = 0;
                }
                if (hoverShowOwnChannels) {
                    guiScreenID = 2;
                    prevId = 0;
                }
                if (hoverShowInventory) {
                    guiScreenID = 3;
                    prevId = 0;
                }
                if (hoverShowPublicChannels) {
                    guiScreenID = 4;
                    prevId = 0;
                }
                break;
            case 1:
                if (hoverBack) {
                    new PacketUpdateServerTile(this.container.base, 8);
                    if (prevId != 0) {
                        guiScreenID = prevId;
                        prevId = 1;
                    } else {
                        guiScreenID = 0;
                        prevId = 1;
                    }
                }
                if (hoverUpChannel) {
                    new PacketUpdateServerTile(this.container.base, 2);
                }
                if (hoverDownChannel) {
                    new PacketUpdateServerTile(this.container.base, 3);
                }
                if (hoverDeleteChannel) {
                    new PacketUpdateServerTile(this.container.base, 9);
                    guiScreenID = 0;
                }
                if (hoverReceiveChannel) {
                    new PacketUpdateServerTile(this.container.base, 4);
                }
                if (hoverExtractChannel) {
                    new PacketUpdateServerTile(this.container.base, 4.1);
                }
                if (hoverEnergyChannel) {
                    new PacketUpdateServerTile(this.container.base, 5);
                }
                if (hoverFluidChannel) {
                    new PacketUpdateServerTile(this.container.base, 5.1);
                }
                if (hoverItemChannel) {
                    new PacketUpdateServerTile(this.container.base, 5.2);
                }
                if (hoverPublicChannel) {
                    new PacketUpdateServerTile(this.container.base, 6);
                }
                if (hoverChangeModeChannel) {
                    new PacketUpdateServerTile(this.container.base, 7);
                }
                break;
            case 2:

                int jj = 0;
                for (int ii = indexTesseract * 7; ii < Math.min(this.container.base.getChannels().size(), indexMaxTesseract); ii++, jj++) {
                    int x1 = 22 + ((jj) % 7) * 17;
                    int y1 = 19 + ((jj) / 7) * 17;
                    if (x >= x1 && x < x1 + 17 && y >= y1 && y < y1 + 17) {
                        double index2 = ii;
                        int del = 0;
                        while (index2 >= 1) {
                            index2 /= 10;
                            del++;
                        }
                        new PacketUpdateServerTile(this.container.base, 10 + del / 10D + index2 / 10D);
                        this.guiScreenID = 1;
                        prevId = 2;
                        break;
                    }
                }
                if (x >= 9 && x <= 27 && y >= 8 && y <= 20) {
                    new PacketUpdateServerTile(this.container.base, 8);
                    prevId = 2;
                    guiScreenID = 0;
                }
                break;
            case 3:
                if (x >= 9 && x <= 27 && y >= 8 && y <= 20) {
                    new PacketUpdateServerTile(this.container.base, 8);
                    guiScreenID = 0;
                }
                break;
            case 4:
                if (x >= 9 && x <= 27 && y >= 8 && y <= 20) {
                    new PacketUpdateServerTile(this.container.base, 8);
                    guiScreenID = 0;
                }
                if (index1 < this.container.base.getPublicChannel().size()) {

                    if (x >= 156 && y >= 97 && x <= 173 && y <= 109) {
                        index1 += 24;
                    }

                }
                if (index1 - 24 > 0) {
                    if (x >= 15 && y >= 97 && x <= 32 && y <= 109) {
                        index1 -= 24;
                    }
                }
                break;
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        switch (guiScreenID) {
            default:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guitesseract.png");
            case 1:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guitesseract_create_channel.png");
            case 3:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guitesseract_inventory.png");
            case 2:
            case 4:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guitesseract_public_channels.png");

        }
    }

}
