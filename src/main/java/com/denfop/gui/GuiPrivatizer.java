package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.CustomButton;
import com.denfop.container.ContainerPrivatizer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiPrivatizer extends GuiIU<ContainerPrivatizer> {

    public final ContainerPrivatizer container;

    public GuiPrivatizer(ContainerPrivatizer container1) {
        super(container1);
        this.container = container1;
        this.addElement(new CustomButton(this, 103, 21, 68, 17, container1.base, 0, Localization.translate("button.write")));

    }

    public void initGui() {
        super.initGui();

    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());

    }


    protected void actionPerformed(@Nonnull GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);

    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
