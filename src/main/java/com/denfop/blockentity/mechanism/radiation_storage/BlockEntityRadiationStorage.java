package com.denfop.blockentity.mechanism.radiation_storage;

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
import com.denfop.containermenu.ContainerMenuRadiationStorage;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenRadiationStorage;
import com.denfop.utils.Localization;
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

public class BlockEntityRadiationStorage extends BlockEntityInventory implements IType {

    public final ComponentBaseEnergy radiation;
    private final EnumTypeStyle enumTypeStyle;

    public BlockEntityRadiationStorage(double maxStorage1, EnumTypeStyle enumTypeStyle, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.radiation = this.addComponent((new ComponentBaseEnergy(EnergyType.RADIATION, this, maxStorage1,

                Arrays.asList(Direction.values()),
                Arrays.asList(Direction.values()),
                EnergyNetGlobal.instance.getTierFromPower(14),
                EnergyNetGlobal.instance.getTierFromPower(14)
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
    public ContainerMenuRadiationStorage getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuRadiationStorage(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenRadiationStorage((ContainerMenuRadiationStorage) menu);
    }


    @Override
    public EnumTypeStyle getStyle() {
        return enumTypeStyle;
    }

}
