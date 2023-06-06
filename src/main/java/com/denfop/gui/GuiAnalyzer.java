package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerAnalyzer;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GuiAnalyzer extends GuiIU<ContainerAnalyzer> {

    public final ContainerAnalyzer container;
    public final String name;
    private final ResourceLocation background;
    private int xOffset;
    private int yOffset;


    public GuiAnalyzer(ContainerAnalyzer container1) {
        super(container1);
        this.background = new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiAnalyzer.png");
        this.container = container1;
        this.name = Localization.translate("iu.blockAnalyzer.name");
        this.ySize = 256;
        this.xSize = 212;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 3, 3, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
    }

    private static List<String> getInformation1(String name, String name1, String name2, String name3) {
        List<String> ret = new ArrayList<>();
        ret.add(name);
        ret.add(name1);
        ret.add(name2);
        ret.add(name3);

        return ret;
    }

    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(0, (this.width - this.xSize) / 2 + 22, (this.height - this.ySize) / 2 + 132,
                74, 16, Localization.translate("button.analyzer")
        ));
        this.buttonList.add(new GuiButton(1, (this.width - this.xSize) / 2 + 22, (this.height - this.ySize) / 2 + 152,
                74, 16, Localization.translate("button.quarry")
        ));

    }

    protected void drawForegroundLayer(int par1, int par2) {
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2, 3, 4210752);
        xOffset = (this.width - this.xSize) / 2;
        yOffset = (this.height - this.ySize) / 2;

        this.drawForeground(par1, par2);
        int i2;
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
                        TextFormatting.WHITE + ModUtils.getString(this.container.base.sum),
                10, 80 + 8 - 2, ModUtils.convertRGBcolorToInt(217, 217, 217)
        );

        this.fontRenderer.drawString(TextFormatting.GREEN + Localization.translate("procent_ore") +
                        TextFormatting.WHITE + ModUtils.getString1((this.container.base.sum / this.container.base.breakblock) * 100) + "%",
                10, 80 + 8 + 8 - 2, ModUtils.convertRGBcolorToInt(217, 217, 217)
        );
        this.fontRenderer.drawString(TextFormatting.GREEN + Localization.translate("middleheight") +
                        TextFormatting.WHITE + ModUtils.getString1(((double) this.container.base.sum1 / this.container.base.sum)),
                10, 80 + 8 + 8 + 8 - 2, ModUtils.convertRGBcolorToInt(217, 217, 217)
        );
        this.fontRenderer.drawString(TextFormatting.GREEN + Localization.translate("cost.name") +
                        TextFormatting.WHITE + ModUtils.getString(this.container.base.sum * this.container.base.consume) + " EU",
                10, 80 + 8 + 8 + 8 + 8 - 2, ModUtils.convertRGBcolorToInt(217, 217, 217)
        );
        this.fontRenderer.drawString(TextFormatting.GREEN + Localization.translate("cost.name1") +
                        TextFormatting.WHITE + ModUtils.getString1(this.container.base.consume) + "EU",
                10, 80 + 8 + 8 + 8 + 8 + 8 - 2, ModUtils.convertRGBcolorToInt(217, 217, 217)
        );

        new AdvArea(this, 101, 159, 139, 170)
                .withTooltip(Localization.translate("gui.MolecularTransformer.progress") + ": " + ModUtils.getString(this.container.base.getProgress() * 100) + "%")
                .drawForeground(par1, par2);
        new AdvArea(this, 148, 159, 186, 170)
                .withTooltip("EU: " + ModUtils.getString(this.container.base.energy.getEnergy()) + "/" + ModUtils.getString(
                        this.container.base.energy.getCapacity()))
                .drawForeground(par1, par2);

        if (!(this.container.base.inputslotA.isEmpty())) {
            if (!(this.container.base.listore.isEmpty())) {
                int id = OreDictionary.getOreIDs(this.container.base.inputslotA.get(0))[0];
                String name = OreDictionary.getOreName(id);
                if (this.container.base.listore.contains(name)) {
                    int index = this.container.base.listore.indexOf(name);
                    ItemStack stack = OreDictionary.getOres(this.container.base.listore.get(index)).get(0);

                    String tooltip1 =
                            TextFormatting.GREEN + Localization.translate("chance.ore") + TextFormatting.WHITE + (this.container.base.listnumberore.get(
                                    index) - 1);
                    double number = this.container.base.listnumberore.get(index) - 1;
                    double sum = this.container.base.sum;
                    double m = (number / sum) * 100;
                    String tooltip2 = TextFormatting.GREEN + Localization.translate("chance.ore1") + TextFormatting.WHITE + ModUtils.getString1(
                            m) + "%";

                    String tooltip = TextFormatting.GREEN + Localization.translate("name.ore") + TextFormatting.WHITE + stack.getDisplayName();
                    String tooltip3 = TextFormatting.GREEN + Localization.translate("middleheight") + TextFormatting.WHITE + ModUtils.getString1(
                            index < this.container.base.middleheightores.size() ?
                                    this.container.base.middleheightores.get(index) : 0);
                    String tooltip4 = TextFormatting.GREEN + Localization.translate("cost.name") + TextFormatting.WHITE + ModUtils.getString(
                            this.container.base.listnumberore.get(index) * this.container.base.inputslot.getenergycost()) + "EU";


                    handleUpgradeTooltip2(par1, par2 - this.guiTop,
                            tooltip, tooltip1, tooltip2, tooltip3, tooltip4
                    );

                }

            }
        }
        for (i2 = 0; i2 < Math.min(this.container.base.numberores, 48); i2++) {
            int k = i2 / 6;
            ItemStack stack = OreDictionary.getOres(this.container.base.listore.get(i2)).get(0);
            String tooltip1 =
                    TextFormatting.GREEN + Localization.translate("chance.ore") + TextFormatting.WHITE + (this.container.base.listnumberore.get(
                            i2) - 1) + ".";
            double number = this.container.base.listnumberore.get(i2) - 1;
            double sum = this.container.base.sum;
            double m = (number / sum) * 100;
            String tooltip2 = TextFormatting.GREEN + Localization.translate("chance.ore1") + TextFormatting.WHITE + ModUtils.getString1(
                    m) + "%" + ".";

            String tooltip = TextFormatting.GREEN + Localization.translate("name.ore") + TextFormatting.WHITE + stack.getDisplayName();
            String tooltip3 = TextFormatting.GREEN + Localization.translate("middleheight") + TextFormatting.WHITE + ModUtils.getString1(
                    i2 < this.container.base.middleheightores.size() ? this.container.base.middleheightores.get(i2) : 0) +
                    ".";
            String tooltip4 = TextFormatting.GREEN + Localization.translate("cost.name") + TextFormatting.WHITE + ModUtils.getString(
                    this.container.base.listnumberore.get(i2) * this.container.base.inputslot.getenergycost()) + "EU";


            handleUpgradeTooltip(
                    par1,
                    par2,
                    99 + (i2 - (6 * k)) * 18,
                    13 + k * 18,
                    99 + (i2 - (6 * k)) * 18 + 16,
                    13 + k * 18 + 16,
                    tooltip,
                    tooltip1,
                    tooltip2,
                    tooltip3,
                    tooltip4
            );
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

    private void handleUpgradeTooltip(
            int x, int y, int minX, int minY, int maxX, int maxY,
            String tooltip, String tooltip1, String tooltip2, String tooltip3, String tooltip4
    ) {
        if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
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
    //

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

    //
    protected void actionPerformed(GuiButton guibutton) {

        if (guibutton.id == 0) {
            IUCore.network.get(false).initiateClientTileEntityEvent(this.container.base, 0);

        }
        if (guibutton.id == 1) {
            IUCore.network.get(false).initiateClientTileEntityEvent(this.container.base, 1);

        }


    }

    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(this.background);


        xOffset = (this.width - this.xSize) / 2;
        yOffset = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, this.xSize, this.ySize);
        this.drawBackground();
        this.mc.getTextureManager().bindTexture(this.background);

        double progress = this.container.base.getProgress() * 38;

        if (progress > 0) {
            drawTexturedModalRect(this.xOffset + 101, this.yOffset + 159, 212,
                    24, (int) progress, 11
            );
        }
        double energy = (this.container.base.energy.getEnergy() / this.container.base.energy.getCapacity()) * 38;
        if (energy > 0) {
            drawTexturedModalRect(this.xOffset + 148, this.yOffset + 159, 212,
                    36, (int) energy, 11
            );
        }

        int i2;

        for (i2 = this.container.base.numberores; i2 < 48; i2++) {
            int k = i2 / 6;

            this.drawTexturedModalRect(xOffset + 99 + (i2 - (6 * k)) * 18, yOffset + 13 + k * 18, 213, 1, 16, 16);

        }
        for (i2 = 0; i2 < Math.min(this.container.base.numberores, 48); i2++) {
            int k = i2 / 6;
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glPushMatrix();
            GL11.glColor4f(0.1F, 1, 0.1F, 1);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            this.zLevel = 100.0F;
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            itemRender.renderItemAndEffectIntoGUI(
                    OreDictionary.getOres(this.container.base.listore.get(i2)).get(0),
                    xOffset + 99 + (i2 - (6 * k)) * 18,
                    yOffset + 13 + k * 18
            );
            GL11.glEnable(GL11.GL_LIGHTING);
            GlStateManager.enableLighting();

            RenderHelper.enableStandardItemLighting();
            GL11.glColor4f(0.1F, 1, 0.1F, 1);
            GL11.glPopMatrix();

        }

        RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        itemRender.zLevel = 100;


        itemRender.renderItemAndEffectIntoGUI(new ItemStack(IUItem.basemachine1, 1, 2), xOffset + 5,
                yOffset + 133
        );
        GlStateManager.enableLighting();
        GL11.glEnable(GL11.GL_LIGHTING);

        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        itemRender.zLevel = 100;
        itemRender.renderItemAndEffectIntoGUI(new ItemStack(IUItem.machines, 1, 8), xOffset + 5,
                yOffset + 133 + 20
        );

        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.enableLighting();
        GL11.glPopMatrix();

    }

    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

}
