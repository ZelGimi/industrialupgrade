package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerReplicator;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.mechanism.TileBaseReplicator;
import com.denfop.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiReplicator<T extends ContainerReplicator> extends GuiIU<ContainerReplicator> {

    public GuiReplicator(final ContainerReplicator container) {
        super(container, container.base.getStyle());
        this.imageHeight = 184;
        componentList.clear();
        this.invSlotList.add(container.base.outputSlot);
        inventory = new GuiComponent(this, 7, 101, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE, this.invSlotList))
        );

        componentList.add(inventory);
        componentList.add(slots);
        this.addElement(TankGauge.createNormal(this, 27, 30, (container.base).fluidTank));
        this.addElement((new ItemImage(this, 91, 17,
                () -> container.base.pattern != null ? container.base.pattern.getStack() : null
        )).withTooltip(() -> {
            TileBaseReplicator te = container.base;
            if (te.pattern == null) {
                return null;
            } else {
                String uuReq = ModUtils.getString(te.patternUu) + Localization.translate("iu.generic.text.bucketUnit");
                String euReq = ModUtils.getString(te.patternEu) + Localization.translate("iu.generic.text.EF");
                return te.pattern.getStack().getDisplayName().getString() + " UU: " + uuReq + " EF: " + euReq;
            }
        }));
        this.addComponent(new GuiComponent(this, 136, 84, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
    }

    @Override
    protected void mouseClicked(int i, int j, final int k) {
        super.mouseClicked(i, j, k);
        i -= this.guiLeft;
        j -= this.guiTop;
        if (i >= 80 && i <= 80 + 9 && j >= 16 && j <= 16 + 18) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            new PacketUpdateServerTile(this.container.base, 1);
        }

        if (i >= 109 && i <= 109 + 9 && j >= 16 && j <= 16 + 18) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            new PacketUpdateServerTile(this.container.base, 1);
        }

        if (i >= 75 && i <= 109 + 16 && j >= 82 && j <= 82 + 16) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            new PacketUpdateServerTile(this.container.base, 3);
        }

        if (i >= 92 && i <= 109 + 16 && j >= 82 && j <= 82 + 16) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            new PacketUpdateServerTile(this.container.base, 4);
        }

        if (i >= 109 && i <= 109 + 16 && j >= 82 && j <= 82 + 16) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            new PacketUpdateServerTile(this.container.base, 5);
        }
    }

    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/GUIReplicator.png".toLowerCase());
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        String data;
        TileBaseReplicator te = container.base;
        if (te.getMode() == TileBaseReplicator.Mode.STOPPED) {
            data = Localization.translate("Replicator.gui.info.Waiting");
        } else {
            int progressUu = 0;
            int progressEu = 0;
            if (te.patternUu != 0.0D) {
                progressUu = Math.min((int) Math.round(100.0D * te.uuProcessed / te.patternUu), 100);
            }

            data = String.format(
                    "UU:%d%%  EF:%d%%  >%s",
                    progressUu,
                    progressEu,
                    te.getMode() == TileBaseReplicator.Mode.SINGLE ? "" : ">"
            );
        }
        draw(poseStack, data, 49,
                36, container.base.getMode() == TileBaseReplicator.Mode.STOPPED ? 15461152 : 2157374
        );
        new Area(this, 80, 16, 9, 18).withTooltip(Localization.translate("Replicator.gui.info.last")).drawForeground(poseStack, par1, par2);
        new Area(this, 109, 16, 9, 18).withTooltip(Localization.translate("Replicator.gui.info.next")).drawForeground(poseStack, par1, par2);
        new Area(this, 75, 82, 16, 16).withTooltip(Localization.translate("Replicator.gui.info.Stop")).drawForeground(poseStack, par1, par2);
        new Area(this, 92, 82, 16, 16).withTooltip(Localization.translate("Replicator.gui.info.single")).drawForeground(poseStack,
                par1,
                par2
        );
        new Area(this, 109, 82, 16, 16).withTooltip(Localization.translate("Replicator.gui.info.repeat")).drawForeground(poseStack,
                par1,
                par2
        );

    }

}
