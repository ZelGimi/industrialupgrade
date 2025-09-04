package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.TooltipWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.mechanism.BlockEntityBaseAdditionGenStone;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuAdditionGenStone;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;


public class ScreenAdditionGenStone<T extends ContainerMenuAdditionGenStone> extends ScreenMain<ContainerMenuAdditionGenStone> {

    public final ContainerMenuAdditionGenStone container;

    public ScreenAdditionGenStone(ContainerMenuAdditionGenStone container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new ScreenWidget(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 10, 36, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(container.base.energy)
        ));
    }

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        int xMin = guiLeft;
        int yMin = guiTop;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 62 && x <= 79 && y >= 63 && y <= 80) {
            new PacketUpdateServerTile(this.container.base, 0);
        }
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);

        new TooltipWidget(this, 63, 64, 18, 18)
                .withTooltip(Localization.translate("message.text.mode") + ": " +
                        (this.container.base.getMode() == BlockEntityBaseAdditionGenStone.Mode.DIORITE ?
                                this.container.base.diorite.getDisplayName().getString() :
                                this.container.base.getMode() == BlockEntityBaseAdditionGenStone.Mode.ANDESITE
                                        ?
                                        this.container.base.andesite.getDisplayName().getString()
                                        : this.container.base.granite.getDisplayName().getString()
                        ))
                .drawForeground(poseStack, mouseX
                        , mouseY);
    }

    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int xoffset = guiLeft;
        int yoffset = guiTop;
        bindTexture(getTexture());


        if (this.container.base.output == null) {
            this.drawItemStack(container.base.granite, 64,
                    28
            );
        } else {
            switch (this.container.base.getMode()) {
                case GRANITE:
                    this.drawItemStack(container.base.granite, 64,
                            28
                    );
                    break;
                case DIORITE:
                    this.drawItemStack(container.base.diorite, 64,
                            28
                    );
                    break;
                case ANDESITE:
                    this.drawItemStack(container.base.andesite, 64,
                            28
                    );
                    break;
            }
        }


        switch (this.container.base.getMode()) {
            case GRANITE:
                this.drawItemStack(container.base.granite, 63,
                        64
                );
                break;
            case DIORITE:
                this.drawItemStack(container.base.diorite, 63,
                        64
                );
                break;
            case ANDESITE:
                this.drawItemStack(container.base.andesite, 63,
                        64
                );
                break;
        }

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiGenStone.png".toLowerCase());
    }

}
