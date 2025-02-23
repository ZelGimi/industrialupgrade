package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.agriculture.CropNetwork;
import com.denfop.api.agriculture.ICropItem;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.Energy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerPigFarm;
import com.denfop.container.ContainerPlantCollector;
import com.denfop.container.ContainerPlantFertilizer;
import com.denfop.container.ContainerPlantGardener;
import com.denfop.gui.GuiPigFarm;
import com.denfop.gui.GuiPlantCollector;
import com.denfop.gui.GuiPlantFertilizer;
import com.denfop.gui.GuiPlantGardener;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.crop.TileEntityCrop;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityPlantGardener extends TileEntityInventory  implements IUpgradableBlock {


    private static final int RADIUS = 8;
    public final Energy energy;
    public final InvSlot output;
    AxisAlignedBB searchArea = new AxisAlignedBB(
            pos.add(-RADIUS, -RADIUS, -RADIUS),
            pos.add(RADIUS, RADIUS, RADIUS)
    );

    public TileEntityPlantGardener() {
        this.output = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 9){
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ICropItem;
            }
        };
        this.energy = this.addComponent(Energy.asBasicSink(this, 1024, 4));
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.2));
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
        return BlockBaseMachine3.plant_gardener;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.plant_gardener.info"));
    }

    List<List<TileEntityCrop>> list = new ArrayList<>();

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            final AxisAlignedBB aabb = searchArea.offset(pos);
            searchArea = aabb;
            int j2 = MathHelper.floor((aabb.minX - 2) / 16.0D);
            int k2 = MathHelper.ceil((aabb.maxX + 2) / 16.0D);
            int l2 = MathHelper.floor((aabb.minZ - 2) / 16.0D);
            int i3 = MathHelper.ceil((aabb.maxZ + 2) / 16.0D);
            List<Chunk> chunks = new ArrayList<>();
            for (int j3 = j2; j3 < k2; ++j3) {
                for (int k3 = l2; k3 < i3; ++k3) {
                    final Chunk chunk = world.getChunkFromChunkCoords(j3, k3);
                    if (!chunks.contains(chunk)) {
                        chunks.add(chunk);
                    }
                }
            }
            for (Chunk chunk : chunks) {
                this.list.add(CropNetwork.instance.getCropsFromChunk(world, chunk.getPos()));
            }
        }
    }

    @Override
    public ContainerPlantGardener getGuiContainer(final EntityPlayer var1) {
        return new ContainerPlantGardener(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiPlantGardener(getGuiContainer(var1));
    }

    public boolean contains(BlockPos vec) {
        if (vec.getX() > this.searchArea.minX && vec.getX() < searchArea.maxX) {
            if (vec.getY() > this.searchArea.minY && vec.getY() < searchArea.maxY) {
                return vec.getZ() > searchArea.minZ && vec.getZ() < searchArea.maxZ;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().provider.getWorldTime() % 20 == 0 && this.energy.canUseEnergy(10)) {
            cycle:
            for (List<TileEntityCrop> crops : list) {
                for (TileEntityCrop crop : crops) {
                    if (this.energy.getEnergy() > 10) {
                        if (this.contains(crop.getPos())) {
                            for (ItemStack stack : this.output.gets()) {
                                if (!stack.isEmpty() && crop.getCrop() == null && !crop.isHasDouble() && CropNetwork.instance.canPlantCrop(
                                        stack,
                                        world,
                                        pos,
                                        crop.getDownState(),
                                        crop.getBiome()
                                )) {
                                    crop.plantNewCrop(stack);
                                    this.energy.useEnergy(10);
                                }
                            }
                        }
                    } else {
                        break cycle;
                    }
                }
            }
        }


    }


}
