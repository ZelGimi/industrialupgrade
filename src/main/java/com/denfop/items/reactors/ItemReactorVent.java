package com.denfop.items.reactors;

import ic2.api.reactor.IReactor;
import ic2.core.init.Localization;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemReactorVent extends ItemReactorHeatStorage {

    public final int selfVent;
    public final int reactorVent;

    public ItemReactorVent(String name, int heatStorage, int selfvent, int reactorvent) {
        super(name, heatStorage);
        this.selfVent = selfvent;
        this.reactorVent = reactorvent;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            @NotNull final ItemStack stack,
            final World world,
            @NotNull final List<String> tooltip,
            @NotNull final ITooltipFlag advanced
    ) {
        tooltip.add(Localization.translate("iu.reactorvent") + reactorVent + " eT " + Localization.translate("iu.reactorvent1") + " " + selfVent + " eT");
        super.addInformation(stack, world, tooltip, advanced);
    }

    public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatrun) {
        if (heatrun) {
            int rheat;
            if (this.reactorVent > 0) {
                rheat = reactor.getHeat();
                int reactorDrain = Math.min(rheat, this.reactorVent);

                rheat -= reactorDrain;
                if (this.alterHeat(stack, reactor, x, y, reactorDrain) > 0) {
                    return;
                }

                reactor.setHeat(rheat);
            }

            rheat = this.alterHeat(stack, reactor, x, y, -this.selfVent);
            if (rheat <= 0) {
                reactor.addEmitHeat(rheat + this.selfVent);
            }
        }

    }

}
