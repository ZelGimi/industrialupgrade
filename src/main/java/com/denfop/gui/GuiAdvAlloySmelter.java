package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerTripleElectricMachine;
import com.denfop.tiles.mechanism.triple.heat.TileAdvAlloySmelter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAdvAlloySmelter extends GuiIU<ContainerTripleElectricMachine> {

    public final ContainerTripleElectricMachine container;

    public GuiAdvAlloySmelter(ContainerTripleElectricMachine container1) {
        super(container1, ((TileAdvAlloySmelter) container1.base).getStyle());
        this.container = container1;
        this.invSlotList.add(container.base.outputSlot);
        componentList.clear();
        inventory = new GuiComponent(this, 7, 83, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE, this.invSlotList))
        );

        componentList.add(inventory);
        componentList.add(slots);
        this.addComponent(new GuiComponent(this, 105, 59, EnumTypeComponent.HEAT,
                new Component<>(((TileAdvAlloySmelter) this.container.base).heat)
        ));
        this.addComponent(new GuiComponent(this, 58, 35, EnumTypeComponent.ENERGY, new Component<>(this.container.base.energy)));
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);


        new AdvArea(this, 80, 35, 101, 49)
                .withTooltip(Localization.translate("gui.MolecularTransformer.progress") + ": " + (int) (Math.min(
                        this.container.base.componentProgress.getBar(),
                        1D
                ) * 100) + "%")
                .drawForeground(mouseX, mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());
        int progress = (int) (24.0F * this.container.base.componentProgress.getBar());

        if (progress > 0) {
            drawTexturedModalRect(xoffset + 79, yoffset + 34, 176, 14, progress + 1, 16);
        }


    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiAdvAlloySmelter.png");
    }


}
