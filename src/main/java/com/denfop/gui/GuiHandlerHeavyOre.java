package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerHandlerHeavyOre;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHandlerHeavyOre extends GuiIU<ContainerHandlerHeavyOre> {

    public final ContainerHandlerHeavyOre container;

    public GuiHandlerHeavyOre(ContainerHandlerHeavyOre container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 51, 52, EnumTypeComponent.HEAT,
                new Component<>(container1.base.heat)
        ));
        this.addComponent(new GuiComponent(this, 26, 56, EnumTypeComponent.ENERGY, new Component<>(this.container.base.energy)));
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(this.xSize / 2, 2, name, 4210752, false);
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);


    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.bindTexture();

        int progress = (int) (44 * this.container.base.getProgress());

        if (progress > 0) {
            drawTexturedModalRect(this.guiLeft + 48, this.guiTop + 31, 177, 32, progress + 1, 14);
        }


    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIHandlerHO.png");
    }

}
