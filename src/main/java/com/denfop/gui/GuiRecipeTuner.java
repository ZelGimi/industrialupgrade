package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.CustomButton;
import com.denfop.container.ContainerTunerRecipe;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiRecipeTuner extends GuiIU<ContainerTunerRecipe> {

    public GuiRecipeTuner(ContainerTunerRecipe guiContainer) {
        super(guiContainer);
        this.addElement(new CustomButton(
                this,
                60,
                20,
                105,
                16,
                container.base,
                0,
                Localization.translate("recipe_tuner.change_mode")
        ));
        this.addElement(new CustomButton(this, 60, 40, 105, 16, container.base, 1, Localization.translate("recipe_tuner.write")));

    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);

    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
