package com.denfop.gui;

import com.denfop.container.ContainerTransformer;
import ic2.core.GuiIC2;
import ic2.core.IC2;
import ic2.core.init.Localization;
import ic2.core.ref.ItemName;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiTransformer extends GuiIC2<ContainerTransformer> {

    public String[] mode = new String[]{"", "", "", ""};
    private static final ResourceLocation background = new ResourceLocation("ic2", "textures/gui/GUITransfomer.png");

    public GuiTransformer(ContainerTransformer container) {
        super(container, 219);
        this.mode[1] = Localization.translate("ic2.Transformer.gui.switch.mode1");
        this.mode[2] = Localization.translate("ic2.Transformer.gui.switch.mode2");
        this.mode[3] = Localization.translate("ic2.Transformer.gui.switch.mode3");
    }

    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        IC2.network.get(false).initiateClientTileEntityEvent(
                this.container.base,
                guibutton.id
        );
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int x = i - this.guiLeft;
        int y = j - this.guiTop;
        if (x >= 150 && y >= 32 && x <= 167 && y <= 49) {
            IC2.network.get(false).initiateClientTileEntityEvent(
                    this.container.base,
                    3
            );
        }

    }

    protected void drawForegroundLayer(int mouseX, int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        this.fontRenderer.drawString(Localization.translate("ic2.Transformer.gui.Output"), 6, 30, 4210752);
        this.fontRenderer.drawString(Localization.translate("ic2.Transformer.gui.Input"), 6, 43, 4210752);
        this.fontRenderer.drawString(this.container.base.getoutputflow() + " " + Localization.translate(
                "ic2.generic.text.EUt"), 52, 30, 2157374);
        this.fontRenderer.drawString(this.container.base.getinputflow() + " " + Localization.translate(
                "ic2.generic.text.EUt"), 52, 45, 2157374);
        RenderItem renderItem = this.mc.getRenderItem();
        RenderHelper.enableGUIStandardItemLighting();
        switch (this.container.base.getMode()) {
            case redstone:
                renderItem.renderItemIntoGUI(ItemName.wrench.getItemStack(), 152, 67);
                break;
            case stepdown:
                renderItem.renderItemIntoGUI(ItemName.wrench.getItemStack(), 152, 87);
                break;
            case stepup:
                renderItem.renderItemIntoGUI(ItemName.wrench.getItemStack(), 152, 107);
        }

        RenderHelper.disableStandardItemLighting();
    }

    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(
                0,
                (this.width - this.xSize) / 2 + 7,
                (this.height - this.ySize) / 2 + 65,
                144,
                20,
                this.mode[1]
        ));
        this.buttonList.add(new GuiButton(
                1,
                (this.width - this.xSize) / 2 + 7,
                (this.height - this.ySize) / 2 + 85,
                144,
                20,
                this.mode[2]
        ));
        this.buttonList.add(new GuiButton(
                2,
                (this.width - this.xSize) / 2 + 7,
                (this.height - this.ySize) / 2 + 105,
                144,
                20,
                this.mode[3]
        ));
    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
