package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.tiles.mechanism.dual.TileEnrichment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiEnriched<T extends ContainerDoubleElectricMachine> extends GuiIU<ContainerDoubleElectricMachine> {

    public final ContainerDoubleElectricMachine container;

    public GuiEnriched(ContainerDoubleElectricMachine container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 29, 57, EnumTypeComponent.ENERGY, new Component<>(this.container.base.energy)));
        this.addComponent(new GuiComponent(this, 80, 61, EnumTypeComponent.RAD,
                new Component<>(((TileEnrichment) this.container.base).rad_energy)
        ));

    }

    @Override
    protected void drawForegroundLayer(GuiGraphics stack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(stack, mouseX, mouseY);

    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics stack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(stack, f, x, y);
        int xoffset = guiLeft();
        int yoffset = guiTop();
        bindTexture(getTexture());

        int progress = (int) (15 * this.container.base.getProgress());
        if (progress > 0) {
            drawTexturedModalRect(stack, xoffset + 67 + 2, yoffset + 36, 177, 32, progress + 1, 15);
        }

    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/GUEnrichment.png".toLowerCase());
    }

}
