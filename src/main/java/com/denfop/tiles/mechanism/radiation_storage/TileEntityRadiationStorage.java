package com.denfop.tiles.mechanism.radiation_storage;

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
import com.denfop.container.ContainerRadiationStorage;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiRadiationStorage;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.List;

public class TileEntityRadiationStorage extends TileEntityInventory implements IType {

    public final ComponentBaseEnergy radiation;
    private final EnumTypeStyle enumTypeStyle;

    public TileEntityRadiationStorage(double maxStorage1, EnumTypeStyle enumTypeStyle, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.radiation = this.addComponent((new ComponentBaseEnergy(EnergyType.RADIATION, this, maxStorage1,

                Arrays.asList(Direction.values()),
                Arrays.asList(Direction.values()),
                EnergyNetGlobal.instance.getTierFromPower(14),
                EnergyNetGlobal.instance.getTierFromPower(14), false
        )));
        this.enumTypeStyle = enumTypeStyle;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {

        tooltip.add(Localization.translate("iu.item.tooltip.Capacity") + " " + ModUtils.getString(this.radiation.getCapacity()) + " " +
                "☢");


    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        if (!wrench) {
            switch (this.teBlock.getDefaultDrop()) {
                case Self:
                default:
                    final ComponentBaseEnergy component2 = this.radiation;
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


        return drop;
    }


    @Override
    public ContainerRadiationStorage getGuiContainer(final Player entityPlayer) {
        return new ContainerRadiationStorage(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiRadiationStorage((ContainerRadiationStorage) menu);
    }


    @Override
    public EnumTypeStyle getStyle() {
        return enumTypeStyle;
    }

}
