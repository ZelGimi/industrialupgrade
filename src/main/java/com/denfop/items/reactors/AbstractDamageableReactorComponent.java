package com.denfop.items.reactors;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.init.Localization;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractDamageableReactorComponent extends ItemGradualInt implements IReactorComponent {

    protected AbstractDamageableReactorComponent(String name, int maxDamage) {
        super(name, maxDamage);
    }

    public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatrun) {
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
        return false;
    }

    public boolean canStoreHeat(ItemStack stack, IReactor reactor, int x, int y) {
        return false;
    }

    public int getMaxHeat(ItemStack stack, IReactor reactor, int x, int y) {
        return 0;
    }

    public int getCurrentHeat(ItemStack stack, IReactor reactor, int x, int y) {
        return 0;
    }

    public int alterHeat(ItemStack stack, IReactor reactor, int x, int y, int heat) {
        return heat;
    }

    public float influenceExplosion(ItemStack stack, IReactor reactor) {
        return 0.0F;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(
            @Nonnull ItemStack stack,
            World world,
            @Nonnull List<String> tooltip,
            @Nonnull ITooltipFlag advanced
    ) {
        super.addInformation(stack, world, tooltip, advanced);
        tooltip.add(Localization.translate("ic2.reactoritem.durability") + " " + (this.getMaxCustomDamage(stack) - this.getCustomDamage(
                stack)) + "/" + this.getMaxCustomDamage(stack));
    }

    public boolean canBePlacedIn(ItemStack stack, IReactor reactor) {
        return true;
    }

}
