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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemReactorHeatSwitch extends ItemReactorHeatStorage {

    public final int switchSide;
    public final int switchReactor;

    public ItemReactorHeatSwitch(String name, int heatStorage, int switchside, int switchreactor) {
        super(name, heatStorage);
        this.switchSide = switchside;
        this.switchReactor = switchreactor;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            @NotNull final ItemStack stack,
            final World world,
            @NotNull final List<String> tooltip,
            @NotNull final ITooltipFlag advanced
    ) {
        if (switchReactor != 0) {
            tooltip.add(Localization.translate("iu.reactorheatswitch") + switchReactor + " eT");
        }
        if (switchSide != 0) {
            tooltip.add(Localization.translate("iu.reactorheatswitch1") + " " + switchSide + " eT");
        }

        super.addInformation(stack, world, tooltip, advanced);
    }

    public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatrun) {
        if (heatrun) {
            int myHeat = 0;
            ArrayList<ItemReactorHeatSwitch.ItemStackCoord> heatAcceptors = new ArrayList<>();
            if (this.switchSide > 0) {
                this.checkHeatAcceptor(reactor, x - 1, y, heatAcceptors);
                this.checkHeatAcceptor(reactor, x + 1, y, heatAcceptors);
                this.checkHeatAcceptor(reactor, x, y - 1, heatAcceptors);
                this.checkHeatAcceptor(reactor, x, y + 1, heatAcceptors);
            }

            int add;
            if (this.switchSide > 0) {
                for (Iterator var8 = heatAcceptors.iterator(); var8.hasNext(); myHeat += add) {
                    ItemReactorHeatSwitch.ItemStackCoord stackcoord = (ItemReactorHeatSwitch.ItemStackCoord) var8.next();
                    IReactorComponent heatable = (IReactorComponent) stackcoord.stack.getItem();
                    double mymed = (double) this.getCurrentHeat(stack, reactor, x, y) * 100.0D / (double) this.getMaxHeat(
                            stack,
                            reactor,
                            x,
                            y
                    );
                    double heatablemed = (double) heatable.getCurrentHeat(
                            stackcoord.stack,
                            reactor,
                            stackcoord.x,
                            stackcoord.y
                    ) * 100.0D / (double) heatable.getMaxHeat(stackcoord.stack, reactor, stackcoord.x, stackcoord.y);
                    add = (int) ((double) heatable.getMaxHeat(
                            stackcoord.stack,
                            reactor,
                            stackcoord.x,
                            stackcoord.y
                    ) / 100.0D * (heatablemed + mymed / 2.0D));
                    if (add > this.switchSide) {
                        add = this.switchSide;
                    }

                    if (heatablemed + mymed / 2.0D < 1.0D) {
                        add = this.switchSide / 2;
                    }

                    if (heatablemed + mymed / 2.0D < 0.75D) {
                        add = this.switchSide / 4;
                    }

                    if (heatablemed + mymed / 2.0D < 0.5D) {
                        add = this.switchSide / 8;
                    }

                    if (heatablemed + mymed / 2.0D < 0.25D) {
                        add = 1;
                    }

                    if ((double) Math.round(heatablemed * 10.0D) / 10.0D > (double) Math.round(mymed * 10.0D) / 10.0D) {
                        add -= 2 * add;
                    } else if ((double) Math.round(heatablemed * 10.0D) / 10.0D == (double) Math.round(mymed * 10.0D) / 10.0D) {
                        add = 0;
                    }

                    myHeat -= add;
                    add = heatable.alterHeat(stackcoord.stack, reactor, stackcoord.x, stackcoord.y, add);
                }
            }

            if (this.switchReactor > 0) {
                double mymed = (double) this.getCurrentHeat(stack, reactor, x, y) * 100.0D / (double) this.getMaxHeat(
                        stack,
                        reactor,
                        x,
                        y
                );
                double Reactormed = (double) reactor.getHeat() * 100.0D / (double) reactor.getMaxHeat();
                add = (int) Math.round((double) reactor.getMaxHeat() / 100.0D * (Reactormed + mymed / 2.0D));
                if (add > this.switchReactor) {
                    add = this.switchReactor;
                }

                if (Reactormed + mymed / 2.0D < 1.0D) {
                    add = this.switchSide / 2;
                }

                if (Reactormed + mymed / 2.0D < 0.75D) {
                    add = this.switchSide / 4;
                }

                if (Reactormed + mymed / 2.0D < 0.5D) {
                    add = this.switchSide / 8;
                }

                if (Reactormed + mymed / 2.0D < 0.25D) {
                    add = 1;
                }

                if ((double) Math.round(Reactormed * 10.0D) / 10.0D > (double) Math.round(mymed * 10.0D) / 10.0D) {
                    add -= 2 * add;
                } else if ((double) Math.round(Reactormed * 10.0D) / 10.0D == (double) Math.round(mymed * 10.0D) / 10.0D) {
                    add = 0;
                }

                myHeat -= add;
                reactor.setHeat(reactor.getHeat() + add);
            }

            this.alterHeat(stack, reactor, x, y, myHeat);
        }
    }

    private void checkHeatAcceptor(
            IReactor reactor,
            int x,
            int y,
            ArrayList<ItemReactorHeatSwitch.ItemStackCoord> heatAcceptors
    ) {
        ItemStack stack = reactor.getItemAt(x, y);
        if (stack != null && stack.getItem() instanceof IReactorComponent) {
            IReactorComponent comp = (IReactorComponent) stack.getItem();
            if (comp.canStoreHeat(stack, reactor, x, y)) {
                heatAcceptors.add(new ItemStackCoord(stack, x, y));
            }
        }

    }

    private static class ItemStackCoord {

        public ItemStack stack;
        public int x;
        public int y;

        public ItemStackCoord(ItemStack stack1, int x1, int y1) {
            this.stack = stack1;
            this.x = x1;
            this.y = y1;
        }

    }

}
