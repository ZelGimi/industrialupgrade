package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.tiles.mechanism.dual.heat.TileWeldingMachine;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;

@SideOnly(Side.CLIENT)
public class GuiWelding extends GuiIU<ContainerDoubleElectricMachine> {

    public final ContainerDoubleElectricMachine container;

    public GuiWelding(ContainerDoubleElectricMachine container1) {
        super(container1);
        this.container = container1;
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 104, 58, EnumTypeComponent.HEAT,
                new Component<>(this.container.base.heat)
        ));
        this.addComponent(new GuiComponent(this, 58, 35, EnumTypeComponent.ENERGY, new Component<>(this.container.base.energy)));
        componentList.add(new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(
                        EnumTypeComponentSlot.SLOT,
                        Collections.singletonList(((TileWeldingMachine) this.container.base).input_slot)
                ))
        ));
        this.addComponent(new GuiComponent(this, 80, 35, EnumTypeComponent.PROCESS,
                new Component<>(this.container.base.componentProgress)
        ));
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);

    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());


    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
