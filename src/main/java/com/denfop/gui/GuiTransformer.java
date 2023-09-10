package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.container.ContainerTransformer;
import com.denfop.network.packet.PacketUpdateServerTile;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiTransformer extends GuiCore<ContainerTransformer> {

    private static final ResourceLocation background = new ResourceLocation(Constants.MOD_ID, "textures/gui/GUITransfomer.png");
    public String[] mode = new String[]{"", "", "", ""};

    public GuiTransformer(ContainerTransformer container) {
        super(container, 219);
        this.mode[1] = Localization.translate("Transformer.gui.switch.mode1");
        this.mode[2] = Localization.translate("Transformer.gui.switch.mode2");
        this.mode[3] = Localization.translate("Transformer.gui.switch.mode3");
    }

    protected void actionPerformed(@Nonnull GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        new PacketUpdateServerTile(
                this.container.base,
                guibutton.id
        );
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int x = i - this.guiLeft;
        int y = j - this.guiTop;
        if (x >= 150 && y >= 32 && x <= 167 && y <= 49) {
            new PacketUpdateServerTile(
                    this.container.base,
                    3
            );
        }

    }

    protected void drawForegroundLayer(int mouseX, int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        this.fontRenderer.drawString(Localization.translate("Transformer.gui.Output"), 6, 30, 4210752);
        this.fontRenderer.drawString(Localization.translate("Transformer.gui.Input"), 6, 43, 4210752);
        this.fontRenderer.drawString(this.container.base.getoutputflow() + " " + Localization.translate(
                Constants.ABBREVIATION + ".generic.text.EUt"), 52, 30, 2157374);
        this.fontRenderer.drawString(this.container.base.getinputflow() + " " + Localization.translate(
                Constants.ABBREVIATION + ".generic.text.EUt"), 52, 45, 2157374);
        RenderItem renderItem = this.mc.getRenderItem();
        RenderHelper.enableGUIStandardItemLighting();
        switch (this.container.base.getMode()) {
            case redstone:
                renderItem.renderItemIntoGUI(IUItem.wrench.getDefaultInstance(), 152, 67);
                break;
            case stepdown:
                renderItem.renderItemIntoGUI(IUItem.wrench.getDefaultInstance(), 152, 87);
                break;
            case stepup:
                renderItem.renderItemIntoGUI(IUItem.wrench.getDefaultInstance(), 152, 107);
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
