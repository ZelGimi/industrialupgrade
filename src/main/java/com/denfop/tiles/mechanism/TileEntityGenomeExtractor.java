package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.agriculture.ICropItem;
import com.denfop.api.agriculture.genetics.GeneticTraits;
import com.denfop.api.agriculture.genetics.Genome;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.Energy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerGenomeExtractor;
import com.denfop.container.ContainerSaplingGardener;
import com.denfop.container.ContainerTreeBreaker;
import com.denfop.gui.GuiGenomeExtractor;
import com.denfop.gui.GuiSaplingGardener;
import com.denfop.gui.GuiTreeBreaker;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.items.bee.ItemJarBees;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityGenomeExtractor extends TileEntityInventory  implements IUpdatableTileEvent {

    public final InvSlotOutput slot;
    public final ComponentBaseEnergy energy;
    public final InvSlot input;
    public  Genome genCrop;
    public  com.denfop.api.bee.genetics.Genome genBee;
    private boolean work;
    int meta = -1;

    public TileEntityGenomeExtractor() {
        this.slot = new InvSlotOutput(this, 1);
        this.input = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1){
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ICropItem || stack.getItem() instanceof ItemJarBees;
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (content.isEmpty()){
                    genBee = null;
                    genCrop = null;
                }else{
                    if (content.getItem() instanceof ICropItem){
                        genCrop = new Genome(content);
                        genBee = null;
                    }else{
                        genBee = new com.denfop.api.bee.genetics.Genome(content);
                        genCrop = null;
                    }
                }
            }
        };

        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.25));
        energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM,this,1000));
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        final boolean hasGenCrop = customPacketBuffer.readBoolean();
        final boolean hasGenBee = customPacketBuffer.readBoolean();
        if (hasGenCrop){
            try {
                ItemStack stack = customPacketBuffer.readItemStack();
                genCrop = new Genome(stack);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }else{
            genCrop = null;
        }
        if (hasGenBee){
            try {
                ItemStack stack = customPacketBuffer.readItemStack();
                genBee = new com.denfop.api.bee.genetics.Genome(stack);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }else{
            genBee = null;
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(genCrop != null);
        customPacketBuffer.writeBoolean(genBee != null);
        if (genCrop != null){
            customPacketBuffer.writeItemStack(genCrop.getStack());
        }
        if (genBee != null){
            customPacketBuffer.writeItemStack(genBee.getStack());
        }
        return customPacketBuffer;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.genome_extractor;
    }


    @Override
    public ContainerGenomeExtractor getGuiContainer(final EntityPlayer var1) {
        return new ContainerGenomeExtractor(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiGenomeExtractor(getGuiContainer(var1));
    }




    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.work && !this.input.get().isEmpty() && this.slot.isEmpty() && meta != -1){
            if (this.energy.getEnergy() >= 50) {
                this.energy.useEnergy(50);
                work = false;
                if (genCrop != null) {
                    genCrop.removeGenome(GeneticTraits.values()[meta], this.input.get());
                    this.slot.add(new ItemStack(IUItem.genome_crop,1,meta));
                } else if (genBee != null) {
                    genBee.removeGenome(com.denfop.api.bee.genetics.GeneticTraits.values()[meta], this.input.get());
                    this.slot.add(new ItemStack(IUItem.genome_bee,1,meta));
                }
            }
            meta = -1;
        }else{
            work = false;
            if (this.input.get().isEmpty()) {
                this.genCrop = null;
                this.genBee = null;
            }
        }
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        if (var2 == -1)
            work = true;
        else
            meta = (int) var2;
    }

}
