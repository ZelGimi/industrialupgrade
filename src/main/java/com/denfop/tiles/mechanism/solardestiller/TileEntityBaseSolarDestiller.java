package com.denfop.tiles.mechanism.solardestiller;

import com.denfop.api.gui.IType;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.FluidName;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerSolarDestiller;
import com.denfop.gui.GuiSolarDestiller;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.invslot.InvSlotTank;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
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

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

public class TileEntityBaseSolarDestiller extends TileEntityInventory implements IUpgradableBlock, IType {

    public final FluidTank inputTank;
    public final FluidTank outputTank;
    public final InvSlotOutput wateroutputSlot;
    public final InvSlotOutput destiwateroutputSlott;
    public final InvSlotFluidByList waterinputSlot;
    public final InvSlotTank destiwaterinputSlot;
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
        this.waterinputSlot = new InvSlotFluidByList(
                this,
                InvSlot.TypeItemSlot.INPUT,
                1,
                InvSlotFluid.TypeFluidSlot.INPUT,
                FluidRegistry.WATER
        );
        this.destiwaterinputSlot = new InvSlotTank(
                this,
                InvSlot.TypeItemSlot.INPUT,
                1,
                InvSlotFluid.TypeFluidSlot.OUTPUT,
                this.outputTank
        );
        this.wateroutputSlot = new InvSlotOutput(this, "waterOutput", 1);
        this.destiwateroutputSlott = new InvSlotOutput(this, "destilledWaterOutput", 1);
        this.upgradeSlot = new InvSlotUpgrade(this, 3);
    }

    public void onLoaded() {
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

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            skyLight = (boolean) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, skyLight);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        this.waterinputSlot.processIntoTank(this.inputTank, this.wateroutputSlot);
        if (this.getWorld().provider.getWorldTime() % 40 == 0) {
            updateVisibility();

        }
        this.destiwaterinputSlot.processFromTank(this.outputTank, this.destiwateroutputSlott);
        if (!this.skyLight) {
            return;
        }
        if (this.getWorld().provider.getWorldTime() % this.tickrate == 0) {

            if (this.canWork()) {
                this.inputTank.drainInternal(1, true);
                this.outputTank.fillInternal(new FluidStack(FluidName.fluiddistilled_water.getInstance(), 1), true);
            }


        }


        this.upgradeSlot.tickNoMark();
    }


    public ContainerSolarDestiller getGuiContainer(EntityPlayer player) {
        return new ContainerSolarDestiller(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiSolarDestiller(new ContainerSolarDestiller(player, this));
    }



    public int getTickRate() {
        Biome biome = world.getBiome(pos);
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
        return EnumSet.of(UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing, UpgradableProperty.FluidProducing
                , UpgradableProperty.FluidConsuming);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return style;
    }

}
