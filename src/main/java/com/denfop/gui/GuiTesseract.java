package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ItemImage;
import com.denfop.api.gui.ItemStackImageText;
import com.denfop.api.gui.TankGauge;
import com.denfop.api.tesseract.Channel;
import com.denfop.api.tesseract.TypeChannel;
import com.denfop.api.tesseract.TypeMode;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerTesseract;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.recipes.BasicRecipeTwo;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class GuiTesseract extends GuiIU<ContainerTesseract> {

    private final ItemStack stack;
    private final GuiComponent energy;
    private final TankGauge fluid;
    private int guiScreenID = 0;
    private int index = 24;
    private int index1 = 24;

    public GuiTesseract(ContainerTesseract guiContainer) {
        super(guiContainer);
        this.xSize = 191;
        this.ySize = 208;
        this.componentList.clear();
        this.stack = BasicRecipeTwo.getBlockStack(BlockBaseMachine3.tesseract);
        this.energy = new GuiComponent(this, 140, 95, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.getEnergy())
        );
        this.fluid = TankGauge.createNormal(this, 150, 35, container.base.getTank());
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
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        handleUpgradeTooltip(par1, par2);
        switch (guiScreenID) {
            case 1:
                new AdvArea(this, 16, 36, 32, 62).withTooltip(Localization.translate("tesseract.energy")).drawForeground(
                        par1,
                        par2
                );
                new AdvArea(this, 37, 34, 52, 62).withTooltip(Localization.translate("tesseract.fluid")).drawForeground(
                        par1,
                        par2
                );
                new AdvArea(this, 56, 34, 75, 62).withTooltip(Localization.translate("tesseract.item")).drawForeground(
                        par1,
                        par2
                );
                new AdvArea(this, 20, 91, 30, 101).withTooltip(Localization.translate("tesseract.plus")).drawForeground(
                        par1,
                        par2
                );
                new AdvArea(this, 60, 91, 70, 101).withTooltip(Localization.translate("tesseract.minus")).drawForeground(
                        par1,
                        par2
                );
                new AdvArea(this, 79, 69, 91, 86).withTooltip(Localization.translate("tesseract.sink")).drawForeground(
                        par1,
                        par2
                );
                new AdvArea(this, 95, 69, 107, 86).withTooltip(Localization.translate("tesseract.source")).drawForeground(
                        par1,
                        par2
                );
                new AdvArea(this, 112, 67, 122, 89)
                        .withTooltip(Localization.translate("tesseract.private_channel"))
                        .drawForeground(
                                par1,
                                par2
                        );
                new AdvArea(this, 170, 103, 180, 112).withTooltip(Localization.translate("tesseract.activate")).drawForeground(
                        par1,
                        par2
                );
                break;
            case 2:
                for (int i = index - 24; i < Math.min(this.container.base.getChannels().size(), index); i++) {
                    final Channel channel = this.container.base.getChannel(i);
                    new ItemStackImageText(this, 18 + ((i % 24) % 8) * 20, 31 + ((i % 24) / 8) * 22, () -> this.stack,
                            Localization.translate("tesseract.channel") + channel.getChannel() + "\n" +
                                    Localization.translate("tesseract.private") + (channel.isPrivate() ? "да" : "нет") + "\n" +
                                    Localization.translate("tesseract.player") + channel.getTesseract().getPlayer() + "\n" +
                                    Localization.translate("tesseract.type_channel") + channel.getTypeChannel().name() + "\n" +
                                    Localization.translate("tesseract.mode") + channel.getMode().name() + "\n" +
                                    Localization.translate("tesseract.private") + (channel.isActive() ? "да" : "нет") + "\n"

                    ).drawForeground(par1, par2);
                }
                break;
            case 3:
                this.energy.drawForeground(par1, par2);
                this.fluid.drawForeground(par1, par2);
                List<ItemStack> itemStackList =
                        this.container.base
                                .getSlotItem()
                                .getContents()
                                .stream()
                                .filter(itemStack -> !itemStack.isEmpty())
                                .collect(
                                        Collectors.toList());
                for (int i = 0; i < itemStackList.size(); i++) {
                    final int finalI = i;
                    new Area(this, 18 + (i % 6) * 20, 35 + (i / 6) * 22, 18, 18).withTooltip(() -> itemStackList
                            .get(finalI)
                            .getDisplayName()).drawForeground(par1, par2);
                }
                break;
            case 4:
                for (int i = index1 - 24; i < Math.min(this.container.base.getPublicChannel().size(), index1); i++) {
                    final Channel channel = this.container.base.getPublicChannel().get(i);
                    new ItemStackImageText(this, 18 + ((i % 24) % 8) * 20, 31 + ((i % 24) / 8) * 22, () -> this.stack,
                            Localization.translate("tesseract.channel") + channel.getChannel() + "\n" +
                                    Localization.translate("tesseract.player") + channel.getTesseract().getPlayer() + "\n" +
                                    Localization.translate("tesseract.channel") + channel.getTypeChannel().name() + "\n" +
                                    Localization.translate("tesseract.mode") + channel.getMode().name() + "\n" +
                                    Localization.translate("tesseract.active") + (channel.isActive() ? "да" : "нет") + "\n" +
                                    Localization.translate("tesseract.coordinate") + "x:" + channel
                                    .getTesseract()
                                    .getBlockPos()
                                    .getX() + " y: " + channel
                                    .getTesseract()
                                    .getBlockPos()
                                    .getY() + " z: " + channel.getTesseract().getBlockPos().getZ() + "\n"

                    ).drawForeground(par1, par2);
                }

                break;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        switch (guiScreenID) {
            case 0:
                this.drawXCenteredString(
                        this.xSize / 2,
                        25,
                        Localization.translate("tesseract.lists"),
                        ModUtils.convertRGBcolorToInt(13, 229, 34),
                        false
                );
                this.drawXCenteredString(
                        50,
                        65,
                        Localization.translate("tesseract.inventory"),
                        ModUtils.convertRGBcolorToInt(13, 229, 34),
                        false
                );
                this.drawXCenteredString(
                        150,
                        65,
                        Localization.translate("tesseract.create_channel"),
                        ModUtils.convertRGBcolorToInt(13, 229, 34),
                        false
                );
                this.drawXCenteredString(
                        this.xSize / 2,
                        95,
                        Localization.translate("tesseract.public_channel"),
                        ModUtils.convertRGBcolorToInt(13, 229, 34),
                        false
                );

                break;
            case 1:
                if (this.container.base.channel == null) {
                    return;
                }
                this.drawXCenteredString(46, 73, String.valueOf(this.container.base.channel.getChannel()),
                        ModUtils.convertRGBcolorToInt(13,

                                229, 34
                        ), false
                );
                this.drawXCenteredString(
                        105,
                        100,
                        Localization.translate("tesseract.delete"),
                        ModUtils.convertRGBcolorToInt(13, 229, 34),
                        false
                );

                this.drawXCenteredString(137, 40, Localization.translate("tesseract.player_channel"),
                        ModUtils.convertRGBcolorToInt(13, 229, 34), false
                );
                this.drawXCenteredString(137, 50, this.container.base.channel.getTesseract().getPlayer(),
                        ModUtils.convertRGBcolorToInt(13, 229, 34), false
                );


                this.bindTexture();
                if (this.container.base.channel.getMode() == TypeMode.INOUT || this.container.base.channel.getMode() == TypeMode.INPUT) {
                    drawTexturedModalRect(this.guiLeft + 79, this.guiTop + 69,
                            217,
                            59, 13, 27
                    );
                }
                if (this.container.base.channel.getMode() == TypeMode.INOUT || this.container.base.channel.getMode() == TypeMode.OUTPUT) {
                    drawTexturedModalRect(this.guiLeft + 95, this.guiTop + 69,
                            230,
                            59, 13, 27
                    );
                }
                if (this.container.base.channel.getTypeChannel() == TypeChannel.ENERGY) {
                    drawTexturedModalRect(this.guiLeft + 20, this.guiTop + 52,
                            232,
                            97, 10, 11
                    );
                }
                if (this.container.base.channel.getTypeChannel() == TypeChannel.FLUID) {
                    drawTexturedModalRect(this.guiLeft + 41, this.guiTop + 52,
                            232,
                            97, 10, 11
                    );
                }
                if (this.container.base.channel.getTypeChannel() == TypeChannel.ITEM) {
                    drawTexturedModalRect(this.guiLeft + 61, this.guiTop + 52,
                            232,
                            97, 10, 11
                    );
                }
                if (this.container.base.channel.isPrivate()) {
                    drawTexturedModalRect(this.guiLeft + 113, this.guiTop + 78,
                            232,
                            97, 10, 11
                    );
                }
                if (this.container.base.channel.isActive()) {
                    drawTexturedModalRect(this.guiLeft + 171, this.guiTop + 102,
                            232,
                            97, 10, 11
                    );
                }
                break;
            case 2:
                this.bindTexture();
                GlStateManager.color(1, 1, 1, 1);
                if (index < this.container.base.getChannels().size()) {
                    drawTexturedModalRect(this.guiLeft + 156, this.guiTop + 97,
                            227,
                            124, 243 - 226, 135 - 123
                    );
                }
                if (index - 24 > 0) {
                    drawTexturedModalRect(this.guiLeft + 15, this.guiTop + 97,
                            204,
                            124, 243 - 226, 135 - 123
                    );
                }
                for (int i = index - 24; i < Math.min(this.container.base.getChannels().size(), index); i++) {
                    new ItemImage(this, 18 + ((i % 24) % 8) * 20, 31 + ((i % 24) / 8) * 22, () -> this.stack).drawBackground(
                            this.guiLeft,
                            this.guiTop
                    );
                }

                break;
            case 3:
                this.energy.drawBackground(
                        this.guiLeft,
                        this.guiTop
                );
                this.fluid.drawBackground(
                        this.guiLeft,
                        this.guiTop
                );
                List<ItemStack> itemStackList =
                        this.container.base
                                .getSlotItem()
                                .getContents()
                                .stream()
                                .filter(itemStack -> !itemStack.isEmpty())
                                .collect(
                                        Collectors.toList());
                for (int i = 0; i < itemStackList.size(); i++) {
                    RenderHelper.enableGUIStandardItemLighting();
                    this.drawItemStack(18 + (i % 6) * 20, 35 + (i / 6) * 22, itemStackList.get(i));
                    RenderHelper.disableStandardItemLighting();
                }
                break;
            case 4:
                bindTexture();
                GlStateManager.color(1, 1, 1, 1);
                if (index1 < this.container.base.getPublicChannel().size()) {
                    drawTexturedModalRect(this.guiLeft + 156, this.guiTop + 97,
                            227,
                            124, 243 - 226, 135 - 123
                    );
                }
                if (index1 - 24 > 0) {
                    drawTexturedModalRect(this.guiLeft + 15, this.guiTop + 97,
                            204,
                            124, 243 - 226, 135 - 123
                    );
                }
                for (int i = index1 - 24; i < Math.min(this.container.base.getPublicChannel().size(), index1); i++) {
                    new ItemImage(this, 18 + ((i % 24) % 8) * 20, 31 + ((i % 24) / 8) * 22, () -> this.stack).drawBackground(
                            this.guiLeft,
                            this.guiTop
                    );
                }
                break;
        }
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        switch (guiScreenID) {
            case 0:
                if (x >= 117 && x <= 181 && y >= 53 && y <= 86) {
                    new PacketUpdateServerTile(this.container.base, 1);
                    guiScreenID = 1;
                }
                if (x >= 19 && x <= 182 && y >= 15 && y <= 48) {
                    guiScreenID = 2;
                }
                if (x >= 19 && x <= 84 && y >= 53 && y <= 86) {
                    guiScreenID = 3;
                }
                if (x >= 19 && x <= 182 && y >= 90 && y <= 110) {
                    guiScreenID = 4;
                }
                break;
            case 1:
                if (x >= 9 && x <= 27 && y >= 8 && y <= 20) {
                    new PacketUpdateServerTile(this.container.base, 8);
                    guiScreenID = 0;
                }
                if (x >= 20 && x <= 30 && y >= 91 && y <= 101) {
                    new PacketUpdateServerTile(this.container.base, 2);
                }
                if (x >= 60 && x <= 70 && y >= 91 && y <= 101) {
                    new PacketUpdateServerTile(this.container.base, 3);
                }
                if (x >= 90 && x <= 127 && y >= 103 && y <= 111) {
                    new PacketUpdateServerTile(this.container.base, 9);
                    guiScreenID = 0;
                }
                if (x >= 79 && x <= 91 && y >= 69 && y <= 86) {
                    new PacketUpdateServerTile(this.container.base, 4);
                }
                if (x >= 95 && x <= 107 && y >= 69 && y <= 86) {
                    new PacketUpdateServerTile(this.container.base, 4.1);
                }
                if (x >= 20 && x <= 27 && y >= 54 && y <= 61) {
                    new PacketUpdateServerTile(this.container.base, 5);
                }
                if (x >= 41 && x <= 48 && y >= 54 && y <= 61) {
                    new PacketUpdateServerTile(this.container.base, 5.1);
                }
                if (x >= 61 && x <= 68 && y >= 54 && y <= 61) {
                    new PacketUpdateServerTile(this.container.base, 5.2);
                }
                if (x >= 113 && x <= 120 && y >= 80 && y <= 87) {
                    new PacketUpdateServerTile(this.container.base, 6);
                }
                if (x >= 171 && x <= 178 && y >= 105 && y <= 112) {
                    new PacketUpdateServerTile(this.container.base, 7);
                }
                break;
            case 2:
                if (index < this.container.base.getChannels().size()) {
                    if (x >= 156 && y >= 97 && x <= 173 && y <= 109) {
                        index += 24;
                    }

                }
                if (index - 24 > 0) {
                    if (x >= 15 && y >= 97 && x <= 32 && y <= 109) {
                        index -= 24;
                    }
                }
                for (int index1 = index - 24; index1 < Math.min(this.container.base.getChannels().size(), index); index1++) {
                    final int x1 = 18 + ((index1 % 24) % 8) * 20;
                    final int y1 = 31 + ((index1 % 24) / 8) * 22;
                    if (x >= x1 && x < x1 + 20 && y >= y1 && y < y1 + 22) {
                        double index2 = index1;
                        int del = 0;
                        while (index2 >= 1) {
                            index2 /= 10;
                            del++;
                        }
                        new PacketUpdateServerTile(this.container.base, 10 + del / 10D + index2 / 10D);
                        this.guiScreenID = 1;
                        break;
                    }
                }
                if (x >= 9 && x <= 27 && y >= 8 && y <= 20) {
                    new PacketUpdateServerTile(this.container.base, 8);
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
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guitesseract_main.png");
            case 1:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_tesseract_4.png");
            case 2:
            case 3:
            case 4:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_tesseract_2.png");

        }
    }

}
