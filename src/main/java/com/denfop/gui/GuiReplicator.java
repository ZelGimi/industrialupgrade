package com.denfop.gui;

import com.denfop.api.gui.Component;
import com.denfop.api.gui.CustomButton;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ItemImage;
import com.denfop.api.gui.TankGauge;
import com.denfop.api.gui.Text;
import com.denfop.api.gui.TextProvider;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerReplicator;
import com.denfop.tiles.mechanism.TileEntityBaseReplicator;
import ic2.core.init.Localization;
import ic2.core.util.Util;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiReplicator extends GuiIU<ContainerReplicator> {

    public GuiReplicator(final ContainerReplicator container) {
        super(container, container.base.getStyle());
        this.ySize = 184;
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
            TileEntityBaseReplicator te = container.base;
            if (te.pattern == null) {
                return null;
            } else {
                String uuReq = Util.toSiString(te.patternUu, 4) + Localization.translate("ic2.generic.text.bucketUnit");
                String euReq = Util.toSiString(te.patternEu, 4) + Localization.translate("ic2.generic.text.EU");
                return te.pattern.getStack().getDisplayName() + " UU: " + uuReq + " EU: " + euReq;
            }
        }));
        this.addComponent(new GuiComponent(this, 136, 84, EnumTypeComponent.ENERGY_CLASSIC,
                new Component<>(this.container.base.energy)
        ));
        this.addElement((new CustomButton(this, 80, 16, 9, 18, this.createEventSender(0))).withTooltip(
                "ic2.Replicator.gui.info.last"));
        this.addElement((new CustomButton(this, 109, 16, 9, 18, this.createEventSender(1))).withTooltip(
                "ic2.Replicator.gui.info.next"));
        this.addElement((new CustomButton(this, 75, 82, 16, 16, this.createEventSender(3))).withTooltip(
                "ic2.Replicator.gui.info.Stop"));
        this.addElement((new CustomButton(this, 92, 82, 16, 16, this.createEventSender(4))).withTooltip(
                "ic2.Replicator.gui.info.single"));
        this.addElement((new CustomButton(this, 109, 82, 16, 16, this.createEventSender(5))).withTooltip(
                "ic2.Replicator.gui.info.repeat"));
        this.addElement(Text.create(
                this,
                49,
                36,
                96,
                16,
                TextProvider.of(() -> {
                    TileEntityBaseReplicator te = container.base;
                    if (te.getMode() == TileEntityBaseReplicator.Mode.STOPPED) {
                        return Localization.translate("ic2.Replicator.gui.info.Waiting");
                    } else {
                        int progressUu = 0;
                        int progressEu = 0;
                        if (te.patternUu != 0.0D) {
                            progressUu = Math.min((int) Math.round(100.0D * te.uuProcessed / te.patternUu), 100);
                        }

                        return String.format(
                                "UU:%d%%  EU:%d%%  >%s",
                                progressUu,
                                progressEu,
                                te.getMode() == TileEntityBaseReplicator.Mode.SINGLE ? "" : ">"
                        );
                    }
                }),
                () -> container.base.getMode() == TileEntityBaseReplicator.Mode.STOPPED ? 15461152 : 2157374,
                false,
                4,
                0,
                false,
                true
        ));
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation("ic2", "textures/gui/GUIReplicator.png");
    }

}
