package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerLimiter;
import ic2.core.GuiIC2;
import ic2.core.gui.IClickHandler;
import ic2.core.gui.TextBox;
import ic2.core.item.tool.ContainerMeter.Mode;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiToolLimiter extends GuiIC2<ContainerLimiter> {

    private final TextBox textBox;
    private final boolean update = true;

    public GuiToolLimiter(ContainerLimiter container) {
        super(container, 217);
        textBox = new TextBox(this, 10, 10, 100, 20);
        this.addElement(textBox);

    }

    private IClickHandler createModeSetter(final Mode mode) {
        return button -> {
            //   ((ContainerLimiter)GuiToolLimiter.this.container).setMode(mode);
        };
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        GuiToolLimiter.this.container.amount = Double.parseDouble(textBox.getText());
        GuiToolLimiter.this.container.saveLimit();

    }

    protected void drawForegroundLayer(int mouseX, int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        if (container.update) {
            textBox.setText(String.valueOf(container.amount));
            container.update = false;
        }

    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.bindTexture();


    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GUIBags.png");
    }

}
