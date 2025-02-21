package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.Energy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerSaplingGardener;
import com.denfop.gui.GuiSaplingGardener;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntitySaplingGardener extends TileEntityInventory  implements IUpgradableBlock {

    public final InvSlot slot;
    public final Energy energy;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileEntitySaplingGardener() {
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() instanceof IPlantable && ((IPlantable) ((ItemBlock) stack.getItem()).getBlock()) .getPlantType(null,null) == EnumPlantType.Plains;
            }
        };
        this.energy = this.addComponent(Energy.asBasicSink(this, 1024, 4));
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));

        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }
    public final InvSlotUpgrade upgradeSlot;
    private final ComponentUpgradeSlots componentUpgrade;
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Transformer, UpgradableProperty.EnergyStorage, UpgradableProperty.ItemInput
        );
    }
    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.sapling_gardener;
    }
    @Override
    public ContainerSaplingGardener getGuiContainer(final EntityPlayer var1) {
        return new ContainerSaplingGardener(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiSaplingGardener(getGuiContainer(var1));
    }



    private static final int RADIUS = 4;

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().provider.getWorldTime() % 40 == 0) {
            plantSaplingsInRadius();
        }
    }
    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.sapling_gardener.info"));
    }
    private void plantSaplingsInRadius() {
        for (int x = -RADIUS; x <= RADIUS; x += 2) {
            for (int z = -RADIUS; z <= RADIUS; z += 2) {
                BlockPos targetPos = pos.add(x, 0, z);

                if (canPlantSaplingAt(targetPos) && this.energy.canUseEnergy(25)) {
                    ItemStack saplingStack = this.slot.get();
                    if (!saplingStack.isEmpty()) {
                        this.energy.useEnergy(25);
                        plantSapling(targetPos, saplingStack);
                        if (this.energy.getEnergy() < 25) {
                            return;
                        }
                    }
                }
            }
        }
    }

    private boolean canPlantSaplingAt(BlockPos pos) {

        Block blockAt = world.getBlockState(pos).getBlock();
        if (blockAt != Blocks.AIR)
            return false;
        Block blockBelow = world.getBlockState(pos.down()).getBlock();


        return blockBelow == Blocks.DIRT || blockBelow == Blocks.GRASS;
    }

    private void plantSapling(BlockPos targetPos, ItemStack saplingStack) {
        Block block = ((ItemBlock) saplingStack.getItem()).getBlock();
        if (block instanceof IGrowable) {
            world.setBlockState(targetPos, block.getStateFromMeta(saplingStack.getItemDamage()));
            saplingStack.shrink(1);
        }
    }

}
