package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerBaseWitherMaker;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiWitherMaker extends GuiIU<ContainerBaseWitherMaker> {

    public final ContainerBaseWitherMaker container;

    public GuiWitherMaker(
            ContainerBaseWitherMaker container1
    ) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 6, 65, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 122, 63, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);

    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);

        int progress = (int) (22 * this.container.base.componentProgress.getBar());
        this.mc.getTextureManager().bindTexture(getTexture());
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;


        if (progress > 0) {
            drawTexturedModalRect(xoffset + 77, yoffset + 35, 177, 0, progress, 18);
        }

    }

    public String getName() {
        return "";
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GuiWitherMaker.png");
    }

}
