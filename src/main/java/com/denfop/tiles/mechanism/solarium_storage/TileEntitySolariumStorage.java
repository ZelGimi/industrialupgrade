package com.denfop.tiles.mechanism.solarium_storage;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.gui.IType;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockResource;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSolariumStorage;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSolariumStorage;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class TileEntitySolariumStorage extends TileEntityInventory implements IType {

    public final ComponentBaseEnergy se;
    private final EnumTypeStyle enumTypeStyle;

    public TileEntitySolariumStorage(double maxStorage1, EnumTypeStyle enumTypeStyle, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.se = this.addComponent((new ComponentBaseEnergy(EnergyType.SOLARIUM, this, maxStorage1,

                Arrays.stream(Direction.values()).filter(f -> f != this.getFacing()).collect(Collectors.toList()),
                Collections.singletonList(this.getFacing()),
                EnergyNetGlobal.instance.getTierFromPower(14),
                EnergyNetGlobal.instance.getTierFromPower(14), false
        )));
        this.enumTypeStyle = enumTypeStyle;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!getLevel().isClientSide) {
            this.se.setDirections(
                    new HashSet<>(Arrays.stream(Direction.values())
                            .filter(facing1 -> facing1 != Direction.UP && facing1 != getFacing())
                            .collect(Collectors.toList())), new HashSet<>(Collections.singletonList(this.getFacing())));
        }
    }

    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        final CompoundTag nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        tooltip.add(Localization.translate("iu.item.tooltip.Capacity") + " " + ModUtils.getString(this.se.getCapacity()) + " " +
                "SE");
        if (energy1 != 0) {
            tooltip.add(Localization.translate("iu.item.tooltip.Store") + " " + ModUtils.getString(energy1) + "/" + ModUtils.getString(
                    se.getCapacity())
                    + " SE");
        }
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        if (!wrench) {
            switch (this.teBlock.getDefaultDrop()) {
                case Self:
                default:
                    final ComponentBaseEnergy component2 = this.se;
                    if (component2 != null) {
                        if (component2.getEnergy() != 0) {
                            final CompoundTag nbt = ModUtils.nbt(drop);
                            nbt.putDouble("energy", component2.getEnergy());
                        }
                    }
                    return drop;
                case None:
                    return null;
                case Generator:
                   return new ItemStack(IUItem.basemachine2.getItem(78), 1);
                case Machine:
                    return IUItem.blockResource.getItemStack(BlockResource.Type.machine);
                case AdvMachine:
                    return IUItem.blockResource.getItemStack(BlockResource.Type.advanced_machine);
            }
        }

        final ComponentBaseEnergy component2 = this.se;
        if (component2 != null) {
            if (component2.getEnergy() != 0) {
                final CompoundTag nbt = ModUtils.nbt(drop);
                nbt.putDouble("energy", component2.getEnergy());
            }
        }

        return drop;
    }

    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);
        final CompoundTag nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        if (energy1 != 0) {
            this.se.addEnergy(energy1);
        }
    }

    @Override
    public ContainerSolariumStorage getGuiContainer(final Player entityPlayer) {
        return new ContainerSolariumStorage(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(final Player entityPlayer, final ContainerBase<? extends IAdvInventory> b) {
        return new GuiSolariumStorage((ContainerSolariumStorage) b);
    }


    @Override
    public EnumTypeStyle getStyle() {
        return enumTypeStyle;
    }

}
