package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerRodFactory;
import com.denfop.network.IUpdatableTileEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class GuiRodFactory<T extends ContainerRodFactory> extends GuiIU<ContainerRodFactory> {

    public GuiRodFactory(ContainerRodFactory guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 130, 55, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 70, 36, EnumTypeComponent.PROCESS,
                new Component<>(this.container.base.componentProgress)
        ));
        this.addComponent(new GuiComponent(this, 80, 60, EnumTypeComponent.LEFT,
                new Component<>(new ComponentButton(this.container.base, 0, "-1") {
                    @Override
                    public void ClickEvent() {

                        super.ClickEvent();

                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 81 + EnumTypeComponent.RIGHT.getWeight() + 10, 60, EnumTypeComponent.RIGHT,
                new Component<>(new ComponentButton(this.container.base, 1, "+1") {
                    @Override
                    public void ClickEvent() {

                        super.ClickEvent();
                       
                    }
                })
        ));
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack,par1, par2);
        this.font.draw(poseStack,String.valueOf(this.container.base.type), 92, 65, 4210752);
    }



    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
