package com.denfop.blockentity.mechanism.quantum_storage;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.widget.IType;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockResource;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuQuantumStorage;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenQuantumStorage;
import com.denfop.utils.Localization;
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

public class BlockEntityQuantumStorage extends BlockEntityInventory implements IType {

    public final ComponentBaseEnergy qe;
    private final EnumTypeStyle enumTypeStyle;

    public BlockEntityQuantumStorage(double maxStorage1, EnumTypeStyle enumTypeStyle, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.qe = this.addComponent((new ComponentBaseEnergy(EnergyType.QUANTUM, this, maxStorage1,

                Arrays.stream(Direction.values()).filter(f -> f != this.getFacing()).collect(Collectors.toList()),
                Collections.singletonList(this.getFacing()),
                EnergyNetGlobal.instance.getTierFromPower(14),
                EnergyNetGlobal.instance.getTierFromPower(14)
        )));
        this.enumTypeStyle = enumTypeStyle;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getLevel().isClientSide) {
            this.qe.setDirections(
                    new HashSet<>(Arrays.stream(Direction.values())
                            .filter(facing1 -> facing1 != Direction.UP && facing1 != getFacing())
                            .collect(Collectors.toList())), new HashSet<>(Collections.singletonList(this.getFacing())));
        }
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        final CompoundTag nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        tooltip.add(Localization.translate("iu.item.tooltip.Capacity") + " " + ModUtils.getString(this.qe.getCapacity()) + " " +
                "QE");

        if (energy1 != 0) {
            tooltip.add(Localization.translate("iu.item.tooltip.Store") + " " + ModUtils.getString(energy1) + "/" + ModUtils.getString(
                    qe.getCapacity())
                    + " QE");
        }
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        if (!wrench) {
            switch (this.teBlock.getDefaultDrop()) {
                case Self:
                default:
                    final ComponentBaseEnergy component2 = this.qe;
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

        final ComponentBaseEnergy component2 = this.qe;
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
            this.qe.addEnergy(energy1);
        }
    }

    @Override
    public ContainerMenuQuantumStorage getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuQuantumStorage(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(final Player entityPlayer, final ContainerMenuBase<? extends CustomWorldContainer> b) {
        return new ScreenQuantumStorage((ContainerMenuQuantumStorage) b);
    }


    @Override
    public EnumTypeStyle getStyle() {
        return enumTypeStyle;
    }

}
