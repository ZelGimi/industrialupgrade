package com.denfop.items.reactors;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.init.Localization;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemReactorVentSpread extends AbstractReactorComponent {

    public final int sideVent;

    public ItemReactorVentSpread(String name, int sidevent) {
        super(name);
        this.sideVent = sidevent;
    }

    public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatrun) {
        if (heatrun) {
            this.cool(reactor, x - 1, y);
            this.cool(reactor, x + 1, y);
            this.cool(reactor, x, y - 1);
            this.cool(reactor, x, y + 1);
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            @NotNull final ItemStack stack,
            final World world,
            @NotNull final List<String> tooltip,
            @NotNull final ITooltipFlag advanced
    ) {
        tooltip.add(Localization.translate("iu.reactorventspread") + sideVent + " eT");
        super.addInformation(stack, world, tooltip, advanced);
    }

    private void cool(IReactor reactor, int x, int y) {
        ItemStack stack = reactor.getItemAt(x, y);
        if (stack != null && stack.getItem() instanceof IReactorComponent) {
            IReactorComponent comp = (IReactorComponent) stack.getItem();
            if (comp.canStoreHeat(stack, reactor, x, y)) {
                int self = comp.alterHeat(stack, reactor, x, y, -this.sideVent);
                if (self <= 0) {
                    reactor.addEmitHeat(self + this.sideVent);
                }
            }
        }

    }

}
