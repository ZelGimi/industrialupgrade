package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.widget.*;
import com.denfop.blockentity.base.DataOre;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuAnalyzer;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ScreenAnalyzer<T extends ContainerMenuAnalyzer> extends ScreenMain<ContainerMenuAnalyzer> implements ButtonListSliderWidget.WidgetResponder,
        VerticalSliderListWidget.FormatHelper {

    public final ContainerMenuAnalyzer container;
    public final String name;
    private final ResourceLocation background;
    private int xOffset;
    private int yOffset;
    private VerticalSliderListWidget slider;
    private int value = 0;

    public ScreenAnalyzer(ContainerMenuAnalyzer container1) {
        super(container1);
        this.background = new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiAnalyzer.png".toLowerCase());
        this.container = container1;
        this.name = Localization.translate("iu.blockAnalyzer.name".toLowerCase());
        this.imageHeight = 256;
        this.imageWidth = 212;
        this.inventory.setY(this.inventory.getY() + 90);
        this.elements.add(new ImageInterfaceWidget(this, 0, 0, this.imageWidth, this.imageHeight));

        this.elements.add(new ImageScreenWidget(this, 6, 13, 90, 18));
        this.elements.add(new ImageScreenWidget(this, 6, 34, 90, 18));
        this.elements.add(new ImageScreenWidget(this, 6, 74, 90, 130 - 75));
        this.addWidget(new ButtonWidget(this, 22, 133, 74, 16, container1.base, 0, Localization.translate("button.analyzer")));
        this.addWidget(new ButtonWidget(this, 22, 153, 74, 16, container1.base, 1, Localization.translate("button.quarry")));

        this.inventory.setX(this.inventory.getX() + 18);
        this.addComponent(new ScreenWidget(this, 190, 158, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 191, 178, EnumTypeComponent.ENERGY_HEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addComponent(new ScreenWidget(this, 5, 173, EnumTypeComponent.INFO,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 101, 156, EnumTypeComponent.PROGRESS3,
                new WidgetDefault<>(new ComponentProgress(this.container.base, 1, (short) 0) {
                    @Override
                    public double getBar() {
                        return container.base.getProgress();
                    }
                })
        ));
        this.addWidget(new ItemStackWidget(this, 5, 133, () -> this.container.base.getPickBlock(null, null)) {
            @Override
            public void drawForeground(PoseStack poseStack, final int mouseX, final int mouseY) {

            }
        });
        this.addWidget(new ItemStackWidget(this, 5, 153, () -> new ItemStack(IUItem.machines.getItem(8), 1)) {
            @Override
            public void drawForeground(PoseStack poseStack, final int mouseX, final int mouseY) {

            }
        });

        for (int i2 = 0; i2 < 48; i2++) {
            int k = i2 / 6;
            final int finalI = i2;
            componentList.add(new ScreenWidget(this, 98 + ((i2) - (6 * k)) * 18,
                    12 + k * 18, EnumTypeComponent.DEFAULT,
                    new WidgetDefault<>(new EmptyWidget())
            ));
            componentList.add(new ScreenWidget(this, 99 + ((i2) - (6 * k)) * 18,
                    13 + k * 18, EnumTypeComponent.EMPTY,
                    new WidgetDefault<>(new EmptyWidget())
            ) {
                @Override
                public boolean visible() {
                    return finalI + 48 * value >= container.base.getDataOreList().size();
                }
            });

            this.addWidget(new ItemStackWidget(this, 99 + ((finalI) - (6 * k)) * 18, 13 + k * 18, () -> new ItemStack(IUItem.module7.getItemFromMeta(0))) {
                @Override
                public void drawBackground(PoseStack poseStack, final int mouseX, final int mouseY) {
                    if (!visible()) {
                        return;
                    }
                    DataOre dataOre = container.base.getDataOreList().get(finalI + 48 * value);
                    if (!ModUtils.isEmpty(dataOre.getStack())) {
                        this.gui.drawItemStack(this.x, this.y, dataOre.getStack());
                    }

                }

                @Override
                public void drawForeground(PoseStack poseStack, final int mouseX, final int mouseY) {
                    if (!visible()) {
                        return;
                    }
                    if (this.contains(mouseX, mouseY)) {
                        DataOre dataOre = container.base.getDataOreList().get(finalI + 48 * value);
                        String tooltip1 =
                                ChatFormatting.GREEN + Localization.translate("chance.ore") + ChatFormatting.WHITE + (dataOre.getNumber()) + ".";
                        double number = dataOre.getNumber();
                        double m = (number / (container.base.numberores * 1D)) * 100;
                        String tooltip2 = ChatFormatting.GREEN + Localization.translate("chance.ore1") + ChatFormatting.WHITE + (int) m +
                                "%" + ".";

                        String tooltip =
                                ChatFormatting.GREEN + Localization.translate("name.ore") + ChatFormatting.WHITE + dataOre
                                        .getStack()
                                        .getDisplayName().getString();
                        String tooltip3 = ChatFormatting.GREEN + Localization.translate("middleheight") + ChatFormatting.WHITE + ModUtils.getString1(
                                dataOre.getAverage()) +
                                ".";
                        String tooltip4 = ChatFormatting.GREEN + Localization.translate("cost.name") + ChatFormatting.WHITE + ModUtils.getString(
                                dataOre.getNumber() * container.base.inputslot.getenergycost()) + "EF";

                        List<String> text = new ArrayList<>();
                        text.add(tooltip);
                        List<String> compatibleUpgrades = getInformation1(tooltip1, tooltip2, tooltip3, tooltip4);
                        Iterator<String> var5 = compatibleUpgrades.iterator();
                        String itemstack;
                        while (var5.hasNext()) {
                            itemstack = var5.next();
                            text.add(itemstack);
                        }
                        ItemStack stack = dataOre.getStack();
                        if (!ModUtils.isEmpty(stack)) {
                            gui.drawTooltip(x + 10, y + 4, text);
                        }
                    }

                }

                @Override
                public boolean visible() {
                    return finalI + 48 * value < container.base.getDataOreList().size();

                }
            });
        }


    }

    private static List<String> getInformation1(String name, String name1, String name2, String name3) {
        List<String> ret = new ArrayList<>();
        ret.add(name);
        ret.add(name1);
        ret.add(name2);
        ret.add(name3);

        return ret;
    }

    @Override
    public String getText(final int var1, final String var2, final float var3) {
        return "";
    }

    @Override
    public void setEntryValue(final int i, final boolean b) {

    }

    @Override
    public void setEntryValue(final int i, final float v) {
        value = (int) v;

    }

    @Override
    public void setEntryValue(final int i, final String s) {

    }

    public void init() {
        super.init();


        slider = new VerticalSliderListWidget(this, 2, this.guiLeft() + 2 + 207,
                guiTop() + 12,
                "",
                0, this.container.base.getDataOreList().size() / 48, 0,
                this, 133
        );
        this.addWidget(slider);
        this.addRenderableWidget(slider);
    }

    @Override
    public void render(PoseStack p_97795_, int p_97796_, int p_97797_, float p_97798_) {
        super.render(p_97795_, p_97796_, p_97797_, p_97798_);
        slider.visible = this.container.base.getDataOreList().size() > 48;
        slider.setMax(this.container.base.getDataOreList().size() / 48);
    }

    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        xOffset = guiLeft();
        yOffset = guiTop();


        int chunk = this.container.base.xChunk;
        int chunk1 = this.container.base.zChunk;
        int endchunk = this.container.base.xendChunk;
        int endchunk1 = this.container.base.zendChunk;

        this.font.draw(poseStack, Localization.translate("startchunk") +
                        "X:" + chunk + " Z:" + chunk1,
                10, +18, ModUtils.convertRGBcolorToInt(13, 229, 34)
        );
        this.font.draw(poseStack, Localization.translate("endchunk") +
                        "X:" + endchunk + " Z:" + endchunk1,
                10, 39, ModUtils.convertRGBcolorToInt(13, 229, 34)
        );

        this.font.draw(poseStack,
                ChatFormatting.GREEN + Localization.translate("analyze") +
                        ChatFormatting.WHITE + ModUtils.getString(this.container.base.breakblock),
                10, 80 - 2, ModUtils.convertRGBcolorToInt(217, 217, 217)
        );
        this.font.draw(poseStack, ChatFormatting.GREEN + Localization.translate("ore") +
                        ChatFormatting.WHITE + ModUtils.getString(this.container.base.numberores),
                10, 80 + 8 - 2, ModUtils.convertRGBcolorToInt(217, 217, 217)
        );

        this.font.draw(poseStack, ChatFormatting.GREEN + Localization.translate("procent_ore") +
                        ChatFormatting.WHITE + ModUtils.getString1(((this.container.base.numberores / (this.container.base.breakblock * 1D)) * 100)) + "%",
                10, 80 + 8 + 8 - 2, ModUtils.convertRGBcolorToInt(217, 217, 217)
        );
        int average = 0;
        for (DataOre dataOre : this.container.base.dataOreList) {
            average += dataOre.getAverage();
        }
        average /= Math.max(1, this.container.base.dataOreList.size());
        this.font.draw(poseStack, ChatFormatting.GREEN + Localization.translate("middleheight") +
                        ChatFormatting.WHITE + ModUtils.getString1(average),
                10, 80 + 8 + 8 + 8 - 2, ModUtils.convertRGBcolorToInt(217, 217, 217)
        );
        this.font.draw(poseStack, ChatFormatting.GREEN + Localization.translate("cost.name") +
                        ChatFormatting.WHITE + ModUtils.getString(this.container.base.numberores * this.container.base.consume) + " EF",
                10, 80 + 8 + 8 + 8 + 8 - 2, ModUtils.convertRGBcolorToInt(217, 217, 217)
        );
        this.font.draw(poseStack, ChatFormatting.GREEN + Localization.translate("cost.name1") +
                        ChatFormatting.WHITE + ModUtils.getString1(this.container.base.consume) + "EF",
                10, 80 + 8 + 8 + 8 + 8 + 8 - 2, ModUtils.convertRGBcolorToInt(217, 217, 217)
        );


        if (!(this.container.base.inputslotA.isEmpty())) {
            if (!(this.container.base.getDataOreList().isEmpty())) {
                TagKey<Item> id = this.container.base.inputslotA.get(0).getItemHolder().getTagKeys().toList().get(0);
                String name = id.location().getPath();
                if (this.container.base.getDataOreList().contains(name)) {
                    DataOre dataOre = this.container.base.getDataOreList().get(name);
                    ItemStack stack = dataOre.getStack();

                    String tooltip1 =
                            ChatFormatting.GREEN + Localization.translate("chance.ore") + ChatFormatting.WHITE + (dataOre.getNumber());
                    double number = dataOre.getNumber();
                    double sum = this.container.base.breakblock;
                    double m = (number / sum) * 100;
                    String tooltip2 = ChatFormatting.GREEN + Localization.translate("chance.ore1") + ChatFormatting.WHITE + (int) (
                            m) + "%";

                    String tooltip = ChatFormatting.GREEN + Localization.translate("name.ore") + ChatFormatting.WHITE + stack.getDisplayName().getString();
                    String tooltip3 = ChatFormatting.GREEN + Localization.translate("middleheight") + ChatFormatting.WHITE + ModUtils.getString1(
                            dataOre.getAverage());
                    String tooltip4 = ChatFormatting.GREEN + Localization.translate("cost.name") + ChatFormatting.WHITE + ModUtils.getString(
                            dataOre.getNumber() * this.container.base.inputslot.getenergycost()) + "EF";


                    handleUpgradeTooltip2(par1, par2 - this.guiTop(),
                            tooltip, tooltip1, tooltip2, tooltip3, tooltip4
                    );

                }

            }
        }
        drawUpgradeslotTooltip(par1, par2);
    }

    private void drawUpgradeslotTooltip(int x, int y) {
        if (x >= 5 && x <= 22 && y >= 173 && y <= 190) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.analyzerinformation"));
            List<String> compatibleUpgrades = ListInformationUtils.analyzeinform;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(x, y, text);
        }
    }


    private void handleUpgradeTooltip2(
            int x,
            int y,
            String tooltip,
            String tooltip1,
            String tooltip2,
            String tooltip3,
            String tooltip4
    ) {
        if (x >= 77 && x <= 94 && y >= 55 && y <= 72) {
            List<String> text = new ArrayList<>();
            text.add(tooltip);
            List<String> compatibleUpgrades = getInformation1(tooltip1, tooltip2, tooltip3, tooltip4);
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(x, y, text);
        }
    }


    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

}
