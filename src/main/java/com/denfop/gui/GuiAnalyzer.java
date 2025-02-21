package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.CustomButton;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.GuiVerticalSliderList;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.ImageScreen;
import com.denfop.api.gui.ItemStackImage;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerAnalyzer;
import com.denfop.tiles.base.DataOre;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GuiAnalyzer extends GuiIU<ContainerAnalyzer> implements GuiPageButtonList.GuiResponder,
        GuiVerticalSliderList.FormatHelper {

    public final ContainerAnalyzer container;
    public final String name;
    private final ResourceLocation background;
    private int xOffset;
    private int yOffset;
    private GuiVerticalSliderList slider;
    private int value = 0;

    public GuiAnalyzer(ContainerAnalyzer container1) {
        super(container1);
        this.background = new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiAnalyzer.png");
        this.container = container1;
        this.name = Localization.translate("iu.blockAnalyzer.name");
        this.ySize = 256;
        this.xSize = 212;
        this.inventory.setY(this.inventory.getY() + 90);
        this.elements.add(new ImageInterface(this, 0, 0, this.xSize, this.ySize));

        this.elements.add(new ImageScreen(this, 6, 13, 90, 18));
        this.elements.add(new ImageScreen(this, 6, 34, 90, 18));
        this.elements.add(new ImageScreen(this, 6, 74, 90, 130 - 75));
        this.addElement(new CustomButton(this, 22, 133, 74, 16, container1.base, 0, Localization.translate("button.analyzer")));
        this.addElement(new CustomButton(this, 22, 153, 74, 16, container1.base, 1, Localization.translate("button.quarry")));

        this.inventory.setX(this.inventory.getX() + 18);
        this.addComponent(new GuiComponent(this, 190, 158, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 191, 178, EnumTypeComponent.ENERGY_HEIGHT,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 5, 173, EnumTypeComponent.INFO,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 101, 156, EnumTypeComponent.PROGRESS3,
                new Component<>(new ComponentProgress(this.container.base, 1, (short) 0) {
                    @Override
                    public double getBar() {
                        return container.base.getProgress();
                    }
                })
        ));
        this.addElement(new ItemStackImage(this, 5, 133, () -> new ItemStack(IUItem.basemachine1, 1, 2)) {
            @Override
            public void drawForeground(final int mouseX, final int mouseY) {

            }
        });
        this.addElement(new ItemStackImage(this, 5, 153, () -> new ItemStack(IUItem.machines, 1, 8)) {
            @Override
            public void drawForeground(final int mouseX, final int mouseY) {

            }
        });

        for (int i2 = 0; i2 < 48; i2++) {
            int k = i2 / 6;
            final int finalI = i2;
            componentList.add(new GuiComponent(this, 98 + ((i2) - (6 * k)) * 18,
                    12 + k * 18, EnumTypeComponent.DEFAULT,
                    new Component<>(new ComponentEmpty())
            ));
            componentList.add(new GuiComponent(this, 99 + ((i2) - (6 * k)) * 18,
                    13 + k * 18, EnumTypeComponent.EMPTY,
                    new Component<>(new ComponentEmpty())
            ) {
                @Override
                public boolean visible() {
                    return finalI + 48 * value >= container.base.getDataOreList().size();
                }
            });

            this.addElement(new ItemStackImage(this, 99 + ((finalI) - (6 * k)) * 18, 13 + k * 18, () -> IUItem.pullingUpgrade) {
                @Override
                public void drawBackground(final int mouseX, final int mouseY) {
                    if (!visible()) {
                        return;
                    }
                    DataOre dataOre = container.base.getDataOreList().get(finalI + 48 * value);
                    if (!ModUtils.isEmpty(dataOre.getStack())) {
                        RenderHelper.enableGUIStandardItemLighting();
                        this.gui.drawItemStack(this.x, this.y, dataOre.getStack());
                        RenderHelper.disableStandardItemLighting();
                    }

                }

                @Override
                public void drawForeground(final int mouseX, final int mouseY) {
                    if (!visible()) {
                        return;
                    }
                    if (this.contains(mouseX, mouseY)) {
                        DataOre dataOre = container.base.getDataOreList().get(finalI + 48 * value);
                        String tooltip1 =
                                TextFormatting.GREEN + Localization.translate("chance.ore") + TextFormatting.WHITE + (dataOre.getNumber()) + ".";
                        double number = dataOre.getNumber();
                        double m = (number / (container.base.numberores * 1D)) * 100;
                        String tooltip2 = TextFormatting.GREEN + Localization.translate("chance.ore1") + TextFormatting.WHITE + (int) m +
                                "%" + ".";

                        String tooltip =
                                TextFormatting.GREEN + Localization.translate("name.ore") + TextFormatting.WHITE + dataOre
                                        .getStack()
                                        .getDisplayName();
                        String tooltip3 = TextFormatting.GREEN + Localization.translate("middleheight") + TextFormatting.WHITE + ModUtils.getString1(
                                dataOre.getAverage()) +
                                ".";
                        String tooltip4 = TextFormatting.GREEN + Localization.translate("cost.name") + TextFormatting.WHITE + ModUtils.getString(
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
                            gui.drawTooltip(x + 10, y, text);
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

    public void initGui() {
        super.initGui();


        slider = new GuiVerticalSliderList(this, 2, (this.width - this.xSize) / 2 + 207,
                (this.height - this.ySize) / 2 + 12,
                "",
                0, this.container.base.getDataOreList().size() / 48, 0,
                this, 133
        );
        this.buttonList.add(slider);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        slider.visible = this.container.base.getDataOreList().size() > 48;
        slider.setMax(this.container.base.getDataOreList().size() / 48);
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        xOffset = (this.width - this.xSize) / 2;
        yOffset = (this.height - this.ySize) / 2;


        int chunk = this.container.base.xChunk;
        int chunk1 = this.container.base.zChunk;
        int endchunk = this.container.base.xendChunk;
        int endchunk1 = this.container.base.zendChunk;

        this.fontRenderer.drawString(Localization.translate("startchunk") +
                        "X:" + chunk + " Z:" + chunk1,
                10, +18, ModUtils.convertRGBcolorToInt(13, 229, 34)
        );
        this.fontRenderer.drawString(Localization.translate("endchunk") +
                        "X:" + endchunk + " Z:" + endchunk1,
                10, 39, ModUtils.convertRGBcolorToInt(13, 229, 34)
        );

        this.fontRenderer.drawString(
                TextFormatting.GREEN + Localization.translate("analyze") +
                        TextFormatting.WHITE + ModUtils.getString(this.container.base.breakblock),
                10, 80 - 2, ModUtils.convertRGBcolorToInt(217, 217, 217)
        );
        this.fontRenderer.drawString(TextFormatting.GREEN + Localization.translate("ore") +
                        TextFormatting.WHITE + ModUtils.getString(this.container.base.numberores),
                10, 80 + 8 - 2, ModUtils.convertRGBcolorToInt(217, 217, 217)
        );

        this.fontRenderer.drawString(TextFormatting.GREEN + Localization.translate("procent_ore") +
                        TextFormatting.WHITE + ModUtils.getString1(((this.container.base.numberores / (this.container.base.breakblock * 1D)) * 100)) + "%",
                10, 80 + 8 + 8 - 2, ModUtils.convertRGBcolorToInt(217, 217, 217)
        );
        int average = 0;
        for (DataOre dataOre : this.container.base.dataOreList) {
            average += dataOre.getAverage();
        }
        average /= Math.max(1, this.container.base.dataOreList.size());
        this.fontRenderer.drawString(TextFormatting.GREEN + Localization.translate("middleheight") +
                        TextFormatting.WHITE + ModUtils.getString1(average),
                10, 80 + 8 + 8 + 8 - 2, ModUtils.convertRGBcolorToInt(217, 217, 217)
        );
        this.fontRenderer.drawString(TextFormatting.GREEN + Localization.translate("cost.name") +
                        TextFormatting.WHITE + ModUtils.getString(this.container.base.numberores * this.container.base.consume) + " EF",
                10, 80 + 8 + 8 + 8 + 8 - 2, ModUtils.convertRGBcolorToInt(217, 217, 217)
        );
        this.fontRenderer.drawString(TextFormatting.GREEN + Localization.translate("cost.name1") +
                        TextFormatting.WHITE + ModUtils.getString1(this.container.base.consume) + "EF",
                10, 80 + 8 + 8 + 8 + 8 + 8 - 2, ModUtils.convertRGBcolorToInt(217, 217, 217)
        );


        if (!(this.container.base.inputslotA.isEmpty())) {
            if (!(this.container.base.getDataOreList().isEmpty())) {
                int id = OreDictionary.getOreIDs(this.container.base.inputslotA.get(0))[0];
                String name = OreDictionary.getOreName(id);
                if (this.container.base.getDataOreList().contains(name)) {
                    DataOre dataOre = this.container.base.getDataOreList().get(name);
                    ItemStack stack = dataOre.getStack();

                    String tooltip1 =
                            TextFormatting.GREEN + Localization.translate("chance.ore") + TextFormatting.WHITE + (dataOre.getNumber());
                    double number = dataOre.getNumber();
                    double sum = this.container.base.breakblock;
                    double m = (number / sum) * 100;
                    String tooltip2 = TextFormatting.GREEN + Localization.translate("chance.ore1") + TextFormatting.WHITE + (int) (
                            m) + "%";

                    String tooltip = TextFormatting.GREEN + Localization.translate("name.ore") + TextFormatting.WHITE + stack.getDisplayName();
                    String tooltip3 = TextFormatting.GREEN + Localization.translate("middleheight") + TextFormatting.WHITE + ModUtils.getString1(
                            dataOre.getAverage());
                    String tooltip4 = TextFormatting.GREEN + Localization.translate("cost.name") + TextFormatting.WHITE + ModUtils.getString(
                            dataOre.getNumber() * this.container.base.inputslot.getenergycost()) + "EF";


                    handleUpgradeTooltip2(par1, par2 - this.guiTop,
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


    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);


    }

    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

}
