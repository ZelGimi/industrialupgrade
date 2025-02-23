package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.tiles.mechanism.dual.TileSynthesis;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;

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
        this.addComponent(new GuiComponent(this, 9, 63, EnumTypeComponent.RAD,
                new Component<>(((TileSynthesis) this.container.base).rad_energy)
        ));
        this.addComponent(new GuiComponent(this, 6, 34, EnumTypeComponent.ENERGY,
                new Component<>(((TileSynthesis) this.container.base).energy)
        ));
        componentList.add(new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(
                        EnumTypeComponentSlot.SLOT,
                        Collections.singletonList(this.container.base.inputSlotA)
                ))
        ));
        componentList.add(new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(
                        EnumTypeComponentSlot.SLOT,
                        Collections.singletonList(((TileSynthesis) this.container.base).input_slot)
                ))
        ));
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);

    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());
        int progress = (int) (13 * this.container.base.getProgress());
        int progress1 = (int) (24 * this.container.base.getProgress());
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;


        if (progress > 0) {
            drawTexturedModalRect(xoffset + 42, yoffset + 35, 177, 33, progress + 1, 14);
        }
        if (progress1 > 0) {
            drawTexturedModalRect(xoffset + 78, yoffset + 33, 177, 52, progress1 + 1, 23);
        }

        final MachineRecipe output = this.container.base.output;
        if (output != null) {
            this.fontRenderer.drawString(
                    TextFormatting.GREEN + Localization.translate("chance") + output.getRecipe().output.metadata.getInteger(
                            "percent") + "%", xoffset + 66,
                    yoffset + 66, ModUtils.convertRGBcolorToInt(217, 217, 217)
            );
        }
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiSynthesis.png");
    }

}
