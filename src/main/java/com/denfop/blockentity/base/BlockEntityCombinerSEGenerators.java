package com.denfop.blockentity.base;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuCombinerSE;
import com.denfop.inventory.InventoryCombinerSEG;
import com.denfop.inventory.InventoryGenCombinerSunarrium;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenCombinerSE;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityCombinerSEGenerators extends BlockEntityInventory implements
        IUpgradableBlock {


    public final InventoryCombinerSEG inputSlot;
    public final InventoryUpgrade upgradeSlot;
    public final InventoryOutput outputSlot;
    public final ComponentBaseEnergy sunenergy;
    public final InventoryGenCombinerSunarrium input;
    public final ItemStack itemstack;
    public double coef_day;
    public double coef_night;
    public double update_night;
    public int count;
    public List<Double> lst;
    public int coef = 0;
    public double generation;
    private boolean noSunWorld;
    private boolean skyIsVisible;
    private boolean sunIsUp;

    public BlockEntityCombinerSEGenerators(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.combiner_se_generators, pos, state);
        itemstack = new ItemStack(IUItem.sunnarium.getStack(4), 1);
        this.inputSlot = new InventoryCombinerSEG(this);
        this.input = new InventoryGenCombinerSunarrium(this);

        this.outputSlot = new InventoryOutput(this, 9);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.sunenergy = this.addComponent(ComponentBaseEnergy
                .asBasicSource(EnergyType.SOLARIUM, this, 0, 1));
        this.lst = new ArrayList<>();
        this.lst.add(0D);
        this.lst.add(0D);
        this.lst.add(0D);
        this.coef_day = 0;
        this.coef_night = 0;
        this.update_night = 0;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.combiner_se_generators;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public int getInventoryStackLimit() {
        return 4;
    }


    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            generation = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, generation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }


    public void onLoaded() {
        super.onLoaded();
        this.inputSlot.update();
        this.lst = this.input.coefday();
        this.coef_day = this.lst.get(0);
        this.coef_night = this.lst.get(1);
        this.update_night = this.lst.get(2);
        this.noSunWorld = this.level.dimension() == Level.NETHER;
        updateVisibility();
    }

    public void updateVisibility() {
        this.skyIsVisible = this.getLevel().canSeeSky(this.worldPosition) &&
                (this.getLevel().getBlockState(this.worldPosition.above()).getMapColor(getLevel(), this.worldPosition.above()) == MapColor.NONE) &&
                !this.noSunWorld;
        this.sunIsUp = this.getLevel().isDay();
    }

    public void energy(long tick) {
        double k = 0;
        if (this.sunIsUp) {
            if (tick <= 1000L) {
                k = 5;
            }
            if (tick > 1000L && tick <= 4000L) {
                k = 10;
            }
            if (tick > 4000L && tick <= 8000L) {
                k = 30;
            }
            if (tick > 8000L && tick <= 11000L) {
                k = 10;
            }
            if (tick > 11000L) {
                k = 5;
            }
            generation = k * this.coef * (1 + coef_day);
            this.sunenergy.addEnergy(generation);
        }

        if (this.update_night > 0 && !this.sunIsUp) {
            double tick1 = tick - 12000;
            if (tick1 <= 1000L) {
                k = 5;
            }
            if (tick1 > 1000L && tick1 <= 4000L) {
                k = 10;
            }
            if (tick1 > 4000L && tick1 <= 8000L) {
                k = 30;
            }
            if (tick1 > 8000L && tick1 <= 11000L) {
                k = 10;
            }
            if (tick1 > 11000L) {
                k = 5;
            }
            generation = k * this.coef * (this.update_night - 1) * (1 + this.coef_night);
            this.sunenergy.addEnergy(generation);

        }

    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.level.getGameTime() % 80 == 0) {
            updateVisibility();
            this.inputSlot.update();
        }
        long tick = this.getWorld().getGameTime() % 24000L;
        generation = 0;
        if (this.skyIsVisible) {
            energy(tick);
            while (this.sunenergy.getEnergy() >= 9000 && this.outputSlot.add(itemstack)) {
                this.sunenergy.addEnergy(-9000);
            }
        }
        this.upgradeSlot.tickNoMark();
    }

    public void onUnloaded() {
        super.onUnloaded();


    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenCombinerSE((ContainerMenuCombinerSE) menu);
    }

    public ContainerMenuCombinerSE getGuiContainer(Player entityPlayer) {
        return new ContainerMenuCombinerSE(entityPlayer, this);
    }


    public String getStartSoundFile() {
        return null;
    }

    public String getInterruptSoundFile() {
        return null;
    }


    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.ItemExtract
        );
    }


    public String getInventoryName() {
        return null;
    }


}
