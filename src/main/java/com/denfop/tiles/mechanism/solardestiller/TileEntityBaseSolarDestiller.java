package com.denfop.tiles.mechanism.solardestiller;

import com.denfop.api.gui.IType;
import com.denfop.api.inv.IHasGui;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerSolarDestiller;
import com.denfop.gui.GuiSolarDestiller;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotConsumableLiquid;
import com.denfop.invslot.InvSlotConsumableLiquidByList;
import com.denfop.invslot.InvSlotConsumableLiquidByTank;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.profile.NotClassic;
import ic2.core.ref.FluidName;
import ic2.core.util.BiomeUtil;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

@NotClassic
public class TileEntityBaseSolarDestiller extends TileEntityInventory implements IHasGui, IUpgradableBlock, IType {

    public final FluidTank inputTank;
    public final FluidTank outputTank;
    public final InvSlotOutput wateroutputSlot;
    public final InvSlotOutput destiwateroutputSlott;
    public final InvSlotConsumableLiquidByList waterinputSlot;
    public final InvSlotConsumableLiquidByTank destiwaterinputSlot;
    public final InvSlotUpgrade upgradeSlot;
    protected final Fluids fluids = this.addComponent(new Fluids(this));
    private final EnumTypeStyle style;
    private int tickrate;
    private boolean skyLight;
    private boolean hasSky;

    public TileEntityBaseSolarDestiller(EnumTypeStyle style) {
        this.style = style;
        this.inputTank = this.fluids.addTankInsert("inputTank", 10000, Fluids.fluidPredicate(FluidRegistry.WATER));
        this.outputTank = this.fluids.addTankExtract("outputTank", 10000);
        this.waterinputSlot = new InvSlotConsumableLiquidByList(
                this,
                "waterInput",
                InvSlot.Access.I,
                1,
                InvSlot.InvSide.ANY,
                InvSlotConsumableLiquid.OpType.Drain,
                FluidRegistry.WATER
        );
        this.destiwaterinputSlot = new InvSlotConsumableLiquidByTank(
                this,
                "destilledWaterInput",
                InvSlot.Access.I,
                1,
                InvSlot.InvSide.BOTTOM,
                InvSlotConsumableLiquid.OpType.Fill,
                this.outputTank
        );
        this.wateroutputSlot = new InvSlotOutput(this, "waterOutput", 1);
        this.destiwateroutputSlott = new InvSlotOutput(this, "destilledWaterOutput", 1);
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 3);
    }

    protected void onLoaded() {
        super.onLoaded();
        this.tickrate = this.getTickRate();
        this.hasSky = !this.world.provider.isNether();
        updateVisibility();
    }

    public void updateVisibility() {
        this.skyLight = this.world.canBlockSeeSky(this.pos.up()) &&
                (this.world.getBlockState(this.pos.up()).getMaterial().getMaterialMapColor() ==
                        MapColor.AIR) && this.hasSky && this.world.isDaytime();
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        this.waterinputSlot.processIntoTank(this.inputTank, this.wateroutputSlot);
        if (this.getWorld().provider.getWorldTime() % 40 == 0) {
            updateVisibility();

        }
        if (!this.skyLight) {
            return;
        }
        if (this.getWorld().provider.getWorldTime() % this.tickrate == 0) {

            if (this.canWork()) {
                this.inputTank.drainInternal(1, true);
                this.outputTank.fillInternal(new FluidStack(FluidName.distilled_water.getInstance(), 1), true);
            }


        }

        this.destiwaterinputSlot.processFromTank(this.outputTank, this.destiwateroutputSlott);
        this.upgradeSlot.tickNoMark();
    }


    public ContainerSolarDestiller getGuiContainer(EntityPlayer player) {
        return new ContainerSolarDestiller(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiSolarDestiller(new ContainerSolarDestiller(player, this));
    }

    public void onGuiClosed(EntityPlayer player) {
    }

    public int getTickRate() {
        Biome biome = BiomeUtil.getBiome(this.getWorld(), this.pos);
        if (BiomeDictionary.hasType(biome, Type.HOT)) {
            switch (style) {
                case DEFAULT:
                    return 36;
                case ADVANCED:
                    return 30;
                case IMPROVED:
                    return 24;
                case PERFECT:
                    return 15;
            }
        } else {
            switch (style) {
                case DEFAULT:
                    return BiomeDictionary.hasType(biome, Type.COLD) ? 144 : 72;
                case ADVANCED:
                    return BiomeDictionary.hasType(biome, Type.COLD) ? 120 : 60;
                case IMPROVED:
                    return BiomeDictionary.hasType(biome, Type.COLD) ? 96 : 48;
                case PERFECT:
                    return BiomeDictionary.hasType(biome, Type.COLD) ? 60 : 30;
            }

        }
        return 0;
    }

    public int gaugeLiquidScaled(int i, int tank) {
        switch (tank) {
            case 0:
                if (this.inputTank.getFluidAmount() <= 0) {
                    return 0;
                }

                return this.inputTank.getFluidAmount() * i / this.inputTank.getCapacity();
            case 1:
                if (this.outputTank.getFluidAmount() <= 0) {
                    return 0;
                }

                return this.outputTank.getFluidAmount() * i / this.outputTank.getCapacity();
            default:
                return 0;
        }
    }

    public boolean canWork() {
        return this.inputTank.getFluidAmount() > 0 && this.outputTank.getFluidAmount() < this.outputTank.getCapacity() && this.skyLight;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing, UpgradableProperty.FluidProducing);
    }

    public double getEnergy() {
        return 40.0D;
    }

    public boolean useEnergy(double amount) {
        return true;
    }

    @Override
    public EnumTypeStyle getStyle() {
        return style;
    }

}
