package com.denfop.items.reactors;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.IC2Potion;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.item.type.NuclearResourceType;
import ic2.core.ref.ItemName;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

public class ItemReactor extends AbstractDamageableReactorComponent {

    public final int numberOfCells;

    protected ItemReactor(String name, int cells, int duration) {
        super(name, duration);
        this.setMaxStackSize(64);
        this.numberOfCells = cells;
    }

    protected static int checkPulseable(IReactor reactor, int x, int y, ItemStack stack, int mex, int mey, boolean heatrun) {
        ItemStack other = reactor.getItemAt(x, y);
        return other != null && other.getItem() instanceof IReactorComponent && ((IReactorComponent) other.getItem()).acceptUraniumPulse(
                other,
                reactor,
                stack,
                x,
                y,
                mex,
                mey,
                heatrun
        ) ? 1 : 0;
    }

    protected static int triangularNumber(int x) {
        return (x * x + x) / 2;
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(5);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(String name) {
        this.registerModel(0, name, "reactors");
        this.registerModel(1, name, "reactors");
    }

    public int getMetadata(@Nonnull ItemStack stack) {
        return this.getCustomDamage(stack) > 0 ? 1 : 0;
    }

    public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatRun) {
        if (reactor.produceEnergy()) {
            int basePulses = 1 + this.numberOfCells / 2;

            for (int iteration = 0; iteration < this.numberOfCells; ++iteration) {
                int pulses = basePulses;
                int heat;
                if (!heatRun) {
                    for (heat = 0; heat < pulses; ++heat) {
                        this.acceptUraniumPulse(stack, reactor, stack, x, y, x, y, false);
                    }

                    int var10000 = pulses + checkPulseable(reactor, x - 1, y, stack, x, y, false) + checkPulseable(
                            reactor,
                            x + 1,
                            y,
                            stack,
                            x,
                            y,
                            false
                    ) + checkPulseable(reactor, x, y - 1, stack, x, y, false) + checkPulseable(
                            reactor,
                            x,
                            y + 1,
                            stack,
                            x,
                            y,
                            false
                    );
                } else {
                    pulses = basePulses + checkPulseable(reactor, x - 1, y, stack, x, y, true) + checkPulseable(
                            reactor,
                            x + 1,
                            y,
                            stack,
                            x,
                            y,
                            true
                    ) + checkPulseable(reactor, x, y - 1, stack, x, y, true) + checkPulseable(
                            reactor,
                            x,
                            y + 1,
                            stack,
                            x,
                            y,
                            true
                    );
                    heat = triangularNumber(pulses) * 4;
                    heat = this.getFinalHeat(stack, reactor, x, y, heat);
                    Queue<ItemReactor.ItemStackCoord> heatAcceptors = new ArrayDeque<>();
                    this.checkHeatAcceptor(reactor, x - 1, y, heatAcceptors);
                    this.checkHeatAcceptor(reactor, x + 1, y, heatAcceptors);
                    this.checkHeatAcceptor(reactor, x, y - 1, heatAcceptors);
                    this.checkHeatAcceptor(reactor, x, y + 1, heatAcceptors);

                    while (!heatAcceptors.isEmpty() && heat > 0) {
                        int dheat = heat / heatAcceptors.size();
                        heat -= dheat;
                        ItemReactor.ItemStackCoord acceptor = heatAcceptors.remove();
                        IReactorComponent acceptorComp = (IReactorComponent) acceptor.stack.getItem();
                        dheat = acceptorComp.alterHeat(acceptor.stack, reactor, acceptor.x, acceptor.y, dheat);
                        heat += dheat;
                    }

                    if (heat > 0) {
                        reactor.addHeat(heat);
                    }
                }
            }

            if (!heatRun && this.getCustomDamage(stack) >= this.getMaxCustomDamage(stack) - 1) {
                reactor.setItemAt(x, y, this.getDepletedStack(stack, reactor));
            } else if (!heatRun) {
                this.applyCustomDamage(stack, 1, null);
            }

        }
    }

    protected int getFinalHeat(ItemStack stack, IReactor reactor, int x, int y, int heat) {
        return heat;
    }

    protected ItemStack getDepletedStack(ItemStack stack, IReactor reactor) {
        ItemStack ret;
        switch (this.numberOfCells) {
            case 1:
                ret = ItemName.nuclear.getItemStack(NuclearResourceType.depleted_uranium);
                break;
            case 2:
                ret = ItemName.nuclear.getItemStack(NuclearResourceType.depleted_dual_uranium);
                break;
            case 3:
            default:
                throw new RuntimeException("invalid cell count: " + this.numberOfCells);
            case 4:
                ret = ItemName.nuclear.getItemStack(NuclearResourceType.depleted_quad_uranium);
        }

        return ret.copy();
    }

    protected void checkHeatAcceptor(IReactor reactor, int x, int y, Collection<ItemReactor.ItemStackCoord> heatAcceptors) {
        ItemStack stack = reactor.getItemAt(x, y);
        if (stack != null && stack.getItem() instanceof IReactorComponent && ((IReactorComponent) stack.getItem()).canStoreHeat(
                stack,
                reactor,
                x,
                y
        )) {
            heatAcceptors.add(new ItemReactor.ItemStackCoord(stack, x, y));
        }

    }

    public boolean acceptUraniumPulse(
            ItemStack stack,
            IReactor reactor,
            ItemStack pulsingStack,
            int youX,
            int youY,
            int pulseX,
            int pulseY,
            boolean heatrun
    ) {
        if (!heatrun) {
            reactor.addOutput(1.0F);
        }

        return true;
    }

    public float influenceExplosion(ItemStack stack, IReactor reactor) {
        return (float) (2 * this.numberOfCells);
    }

    public void onUpdate(
            @Nonnull ItemStack stack,
            @Nonnull World world,
            @Nonnull Entity entity,
            int slotIndex,
            boolean isCurrentItem
    ) {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLiving = (EntityLivingBase) entity;
            if (!ItemArmorHazmat.hasCompleteHazmat(entityLiving)) {
                IC2Potion.radiation.applyTo(entityLiving, 200, 100);
            }
        }

    }

    private static class ItemStackCoord {

        public final ItemStack stack;
        public final int x;
        public final int y;

        public ItemStackCoord(ItemStack stack, int x, int y) {
            this.stack = stack;
            this.x = x;
            this.y = y;
        }

    }

}
