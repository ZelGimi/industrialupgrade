package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.TankWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuPlasticPlateCreator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;

@OnlyIn(Dist.CLIENT)
public class ScreenPlasticPlateCreator<T extends ContainerMenuPlasticPlateCreator> extends ScreenMain<ContainerMenuPlasticPlateCreator> {

    public final ContainerMenuPlasticPlateCreator container;

    public ScreenPlasticPlateCreator(ContainerMenuPlasticPlateCreator container1) {
        super(container1);
        this.container = container1;
        componentList.clear();
        this.inventoryList.add(container.base.outputSlot);
        inventory = new ScreenWidget(this, 7, 83, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE, this.inventoryList))
        );

        componentList.add(inventory);
        componentList.add(slots);
        this.addComponent(new ScreenWidget(this, 80, 35, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(this.container.base.componentProgress)
        ));
        this.addComponent(new ScreenWidget(this, 117, 60, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        componentList.add(new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(
                        EnumTypeComponentSlot.SLOT,
                        Collections.singletonList(this.container.base.input_slot)
                ))
        ));
        this.addComponent(new ScreenWidget(this, 58, 35, EnumTypeComponent.ENERGY, new WidgetDefault<>(this.container.base.energy)));

        this.addWidget(TankWidget.createNormal(this, 6, 5, container.base.fluidTank));
    }


    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        bindTexture(getTexture());

        bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
    }

    public String getName() {
        return this.container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
