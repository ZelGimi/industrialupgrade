package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.utils.ModUtils;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSynthesis extends GuiIU<ContainerDoubleElectricMachine> {

    public final ContainerDoubleElectricMachine container;

    public GuiSynthesis(ContainerDoubleElectricMachine container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        String tooltip2 =
                ModUtils.getString(Math.min(
                        this.container.base.energy.getEnergy(),
                        this.container.base.energy.getCapacity()
                )) + "/" + ModUtils.getString(this.container.base.energy.getCapacity()) + " " +
                        "EU";
        new AdvArea(this, 25, 55, 36, 70)
                .withTooltip(tooltip2)
                .drawForeground(mouseX, mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());
        int chargeLevel = (int) (14.0F * this.container.base.getChargeLevel());
        int progress = (int) (10 * this.container.base.getProgress());
        int progress1 = (int) (24 * this.container.base.getProgress());
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;

        if (chargeLevel > 0) {
            drawTexturedModalRect(xoffset + 24, yoffset + 56 + 14 - chargeLevel, 176, 14 - chargeLevel,
                    14, chargeLevel
            );
        }
        if (progress > 0) {
            drawTexturedModalRect(xoffset + 39, yoffset + 37, 177, 35, progress + 1, 9);
        }
        if (progress1 > 0) {
            drawTexturedModalRect(xoffset + 82, yoffset + 30, 177, 52, progress1 + 1, 23);
        }

        final MachineRecipe output = this.container.base.output;
        if (output != null) {
            this.fontRenderer.drawString(
                    TextFormatting.GREEN + Localization.translate("chance") + output.getRecipe().output.metadata.getInteger(
                            "percent") + "%", xoffset + 60,
                    yoffset + 67, ModUtils.convertRGBcolorToInt(217, 217, 217)
            );
        }
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiSynthesis.png");
    }

}
