package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerGenStone;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.mechanism.TileBaseGenStone;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiGenStone<T extends ContainerGenStone> extends GuiIU<ContainerGenStone> {

    public final ContainerGenStone container;

    public GuiGenStone(ContainerGenStone container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 10, 36, EnumTypeComponent.ENERGY,
                new Component<>(container.base.energy)
        ));
    }

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 62 && x <= 79 && y >= 63 && y <= 80) {
            new PacketUpdateServerTile(this.container.base, 0);
        }
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);

        new Area(this, 63, 64, 18, 18)
                .withTooltip(Localization.translate("message.text.mode") + ": " +
                        (this.container.base.getMode() == TileBaseGenStone.Mode.SAND ?
                                new ItemStack(Blocks.SAND).getDisplayName().getString() :
                                this.container.base.getMode() == TileBaseGenStone.Mode.GRAVEL
                                        ?
                                        new ItemStack(Blocks.GRAVEL).getDisplayName().getString()
                                        : new ItemStack(Blocks.COBBLESTONE).getDisplayName().getString()
                        ))
                .drawForeground(poseStack, mouseX
                        , mouseY);
    }

    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft(), this.guiTop(), 0, 0, this.imageWidth, this.imageHeight);
    }

    protected void renderBg(PoseStack poseStack, float f, int x, int y) {
        super.renderBg(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);


        bindTexture(getTexture());


        if (this.container.base.output == null) {
            new ItemStackImage(this, 64,
                    28, () -> new ItemStack(Blocks.STONE)
            ).drawBackground(poseStack, guiLeft(), guiTop());
        } else {
            switch (this.container.base.getMode()) {
                case SAND:
                    new ItemStackImage(this, 64,
                            28, () -> new ItemStack(Blocks.SAND)
                    ).drawBackground(poseStack, guiLeft(), guiTop());
                    break;
                case GRAVEL:
                    new ItemStackImage(this, 64,
                            28, () -> new ItemStack(Blocks.GRAVEL)
                    ).drawBackground(poseStack, guiLeft(), guiTop());
                    break;
                case COBBLESTONE:
                    new ItemStackImage(this, 64,
                            28, () -> new ItemStack(Blocks.COBBLESTONE)
                    ).drawBackground(poseStack, guiLeft(), guiTop());
                    break;
            }
        }


        switch (this.container.base.getMode()) {
            case SAND:
                new ItemStackImage(this, 63,
                        64, () -> new ItemStack(Blocks.SAND)
                ).drawBackground(poseStack, guiLeft(), guiTop());
                break;
            case GRAVEL:
                new ItemStackImage(this, 63,
                        64, () -> new ItemStack(Blocks.GRAVEL)
                ).drawBackground(poseStack, guiLeft(), guiTop());
                break;
            case COBBLESTONE:
                new ItemStackImage(this, 63,
                        64, () -> new ItemStack(Blocks.COBBLESTONE)
                ).drawBackground(poseStack, guiLeft(), guiTop());
                break;
        }

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiGenStone.png".toLowerCase());
    }

}
