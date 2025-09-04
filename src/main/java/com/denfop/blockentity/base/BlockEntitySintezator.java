package com.denfop.blockentity.base;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.energy.event.load.EnergyTileLoadEvent;
import com.denfop.api.energy.event.unload.EnergyTileUnLoadEvent;
import com.denfop.api.energy.interfaces.EnergyAcceptor;
import com.denfop.api.energy.interfaces.EnergySource;
import com.denfop.api.energy.interfaces.EnergyTile;
import com.denfop.api.otherenergies.common.InfoTile;
import com.denfop.blockentity.panels.entity.BlockEntitySolarPanel;
import com.denfop.blockentity.panels.entity.EnumType;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSintezatorEntity;
import com.denfop.componets.WirelessComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSinSolarPanel;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventorySintezator;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSintezator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.*;

public class BlockEntitySintezator extends BlockEntityInventory implements EnergySource,
        IUpdatableTileEvent {

    public final InventorySintezator inputslot;
    public final InventorySintezator inputslotA;
    public final WirelessComponent wirelessComponent;
    public int machineTire1;
    public int solartype;
    public double generating;
    public double genDay;
    public double genNight;
    public boolean sunIsUp;
    public boolean skyIsVisible;
    public short facing;
    public boolean noSunWorld;
    public int machineTire;
    public boolean addedToEnergyNet;
    public double storage;
    public double production;
    public double maxStorage;
    public boolean rain = false;
    public double progress;
    public boolean wetBiome;
    public EnumType type;
    public BlockEntitySolarPanel.GenerationState generationState = BlockEntitySolarPanel.GenerationState.NONE;
    Map<Direction, EnergyTile> energyConductorMap = new HashMap<>();
    int hashCodeSource;
    List<InfoTile<EnergyTile>> validReceivers = new LinkedList<>();
    private int size;
    private double pastEnergy;
    private double perenergy;
    private ChunkPos chunkPos;
    private long id;

    public BlockEntitySintezator(BlockPos pos, BlockState state) {
        super(BlockSintezatorEntity.sintezator, pos, state);
        this.facing = 2;
        this.storage = 0;
        this.sunIsUp = false;
        this.skyIsVisible = false;
        this.genNight = 0;
        this.genDay = 0;
        this.maxStorage = 0;
        this.machineTire = 0;
        this.machineTire1 = 0;
        this.size = 0;
        this.inputslot = new InventorySintezator(this, "input", 0, 9);
        this.inputslotA = new InventorySintezator(this, "input1", 1, 4);
        this.solartype = 0;
        this.type = EnumType.DEFAULT;
        this.wirelessComponent = this.addComponent(new WirelessComponent(this));
        wirelessComponent.setEnergySource(this);
    }

    public void loadBeforeFirstUpdate() {
        super.loadBeforeFirstUpdate();
        this.inputslot.wirelessmodule();
    }

    public void RemoveTile(EnergyTile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<EnergyTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<EnergyTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
        }
    }

    public long getIdNetwork() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    public void AddTile(EnergyTile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
            this.energyConductorMap.put(facing1, tile);
            validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
        }
    }

    public Map<Direction, EnergyTile> getTiles() {
        return energyConductorMap;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            generationState = BlockEntitySolarPanel.GenerationState.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            sunIsUp = (boolean) DecoderHandler.decode(customPacketBuffer);
            skyIsVisible = (boolean) DecoderHandler.decode(customPacketBuffer);
            generating = (double) DecoderHandler.decode(customPacketBuffer);
            genDay = (double) DecoderHandler.decode(customPacketBuffer);
            genNight = (double) DecoderHandler.decode(customPacketBuffer);
            storage = (double) DecoderHandler.decode(customPacketBuffer);
            maxStorage = (double) DecoderHandler.decode(customPacketBuffer);
            production = (double) DecoderHandler.decode(customPacketBuffer);
            rain = (boolean) DecoderHandler.decode(customPacketBuffer);
            machineTire = (int) DecoderHandler.decode(customPacketBuffer);
            machineTire1 = (int) DecoderHandler.decode(customPacketBuffer);
            progress = (double) DecoderHandler.decode(customPacketBuffer);
            type = EnumType.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            solartype = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, generationState);
            EncoderHandler.encode(packet, sunIsUp);
            EncoderHandler.encode(packet, skyIsVisible);
            EncoderHandler.encode(packet, generating);
            EncoderHandler.encode(packet, genDay);
            EncoderHandler.encode(packet, genNight);
            EncoderHandler.encode(packet, storage);
            EncoderHandler.encode(packet, maxStorage);
            EncoderHandler.encode(packet, production);
            EncoderHandler.encode(packet, rain);
            EncoderHandler.encode(packet, machineTire);
            EncoderHandler.encode(packet, machineTire1);
            EncoderHandler.encode(packet, progress);
            EncoderHandler.encode(packet, type);
            EncoderHandler.encode(packet, solartype);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockSintezatorEntity.sintezator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blocksintezator.getBlock();
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }


    public void intialize() {
        this.noSunWorld = this.getWorld().dimensionType().hasSkyLight();
        this.updateVisibility();


    }

    public void updateVisibility() {
        this.wetBiome = this.level.getBiome(this.pos).value().getDownfall() > 0.0F;
        this.noSunWorld = this.getWorld().dimensionType().hasSkyLight();

        this.rain = this.wetBiome && (this.level.isRaining() || this.level.isThundering());
        this.sunIsUp = this.level.isDay();
        this.skyIsVisible = true;
        if (this.sunIsUp) {
            if (!(this.rain)) {
                this.generationState = BlockEntitySolarPanel.GenerationState.DAY;
            } else {
                this.generationState = BlockEntitySolarPanel.GenerationState.RAINDAY;
            }

        }
        if (!this.sunIsUp) {
            if (!(rain)) {
                this.generationState = BlockEntitySolarPanel.GenerationState.NIGHT;
            } else {
                this.generationState = BlockEntitySolarPanel.GenerationState.RAINNIGHT;
            }
        }
        if (this.level.dimension() == Level.END) {
            this.generationState = BlockEntitySolarPanel.GenerationState.END;
        }
        if (this.level.dimension() == Level.NETHER) {
            this.generationState = BlockEntitySolarPanel.GenerationState.NETHER;
        }

    }

    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);

        if (nbttagcompound.getInt("solarType") != 0) {
            this.solartype = nbttagcompound.getInt("solarType");
        }

        if (nbttagcompound.getDouble("storage") > 0) {
            this.storage = nbttagcompound.getDouble("storage");
        }
        if (nbttagcompound.getDouble("maxStorage") > 0) {
            this.genDay = nbttagcompound.getDouble("genDay");
            this.genNight = nbttagcompound.getDouble("genNight");
            this.maxStorage = nbttagcompound.getDouble("maxStorage");
            this.production = nbttagcompound.getDouble("production");

        }
    }

    public CompoundTag writeToNBT(final CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);

        nbttagcompound.putInt("solarType", this.solartype);

        if (storage > 0) {
            nbttagcompound.putDouble("storage", this.storage);
        }
        if (maxStorage > 0) {
            nbttagcompound.putDouble("genDay", this.genDay);
            nbttagcompound.putDouble("genNight", this.genNight);
            nbttagcompound.putDouble("production", this.production);

            nbttagcompound.putDouble("maxStorage", this.maxStorage);
        }
        return nbttagcompound;

    }

    public double gaugeEnergyScaled(final float i) {

        return progress * i;

    }

    public List<InfoTile<EnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide && !addedToEnergyNet) {
            this.energyConductorMap.clear();
            validReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getWorld(), this));
            this.addedToEnergyNet = true;
            new PacketUpdateFieldTile(this, "slot", this.inputslot);

        }
        this.updateVisibility();
        intialize();
        this.inputslot.update();
        this.inputslotA.update();

    }

    public void onUnloaded() {
        if (!level.isClientSide && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.getWorld(), this));
            this.addedToEnergyNet = false;
        }

        super.onUnloaded();
    }

    @Override
    public double getPerEnergy() {
        return this.perenergy;
    }

    @Override
    public double getPastEnergy() {
        return this.pastEnergy;
    }

    @Override
    public void setPastEnergy(final double pastEnergy) {
        this.pastEnergy = pastEnergy;
    }

    @Override
    public void addPerEnergy(final double setEnergy) {
        this.perenergy += setEnergy;
    }

    @Override
    public boolean isSource() {
        return true;
    }

    public double canExtractEnergy() {

        return Math.min(this.storage, this.production);
    }

    public void extractEnergy(final double amount) {
        this.storage -= amount;
    }

    public int getSourceTier() {
        return this.machineTire;
    }


    public void updateTileEntityField() {
        if (this.level != null) {
            new PacketUpdateFieldTile(this, "type", this.type);
        }

    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("type")) {
            try {
                this.type = EnumType.values()[(int) DecoderHandler.decode(is)];
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot")) {
            try {
                Inventory slot = (Inventory) DecoderHandler.decode(is);
                for (int i = 0; i < slot.size(); i++) {
                    this.inputslot.set(i, slot.get(i));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }


    public ContainerMenuSinSolarPanel getGuiContainer(Player entityPlayer) {
        return new ContainerMenuSinSolarPanel(entityPlayer, this);
    }

    public void updateEntityServer() {

        super.updateEntityServer();


        this.gainFuel();


        if (this.generating > 0D) {
            if (((this.storage + this.generating)) <= (this.maxStorage)) {
                this.storage += this.generating;
            } else {
                this.storage = (this.maxStorage);
            }
        }

        this.progress = Math.min(1, this.storage / this.maxStorage);
        if (this.storage < 0D) {
            this.storage = 0D;
        }
        if (this.maxStorage <= 0D) {
            this.storage = 0D;
        }
        if (this.getWorld().getGameTime() % 20 == 0) {
            int size1 = 0;
            for (ItemStack stack : this.inputslot) {
                if (!stack.isEmpty()) {
                    size1 += stack.getCount();
                }
            }
            if (size != size1) {
                this.inputslot.update();
                size = size1;
            }
        }
    }

    public void gainFuel() {
        if (this.getWorld().getGameTime() % 80 == 0) {
            new PacketUpdateFieldTile(this, "slot", this.inputslot);
        }
        if (this.getWorld().getGameTime() % 40 == 0) {
            this.updateVisibility();
            int type = this.solartype;
            this.solartype = this.inputslotA.solartype();
            if (type != this.solartype) {
                this.updateTileEntityField();
            }
            if (this.solartype == 0) {
                this.setActive("");
            } else {
                this.setActive(EnumType.getFromID(this.solartype).getNameType());
            }

        }


        switch (this.generationState) {
            case DAY:
                this.generating = type.coefficient_day * this.genDay;
                break;
            case NIGHT:
                this.generating = type.coefficient_night * this.genNight;
                break;
            case RAINDAY:
                this.generating = type.coefficient_rain * type.coefficient_day * this.genDay;
                break;
            case RAINNIGHT:
                this.generating = type.coefficient_rain * type.coefficient_night * this.genNight;
                break;
            case NETHER:
                this.generating = type.coefficient_nether * this.genDay;
                break;
            case END:
                this.generating = type.coefficient_end * this.genDay;
                break;
            case NONE:
                this.generating = 0;
                break;

        }

    }


    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            type = EnumType.values()[(int) DecoderHandler.decode(customPacketBuffer)];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenSintezator((ContainerMenuSinSolarPanel) menu);

    }


    public void setType(EnumType type) {
        this.type = type;
    }

    public int setSolarType(EnumType type) {
        if (type == null) {
            setType(EnumType.DEFAULT);
            return 0;
        }
        setType(type);
        switch (type) {
            case AIR:
                if (this.pos.getY() >= 130) {
                    return 1;
                }
                break;
            case EARTH:
                if (this.pos.getY() <= 40) {
                    return 2;
                }
                break;
            case NETHER:
                if (this.getWorld().dimension() == Level.NETHER) {
                    return 3;
                }
                break;
            case END:
                if (this.getWorld().dimension() == Level.END) {
                    return 4;
                }
                break;
            case NIGHT:
                if (!this.sunIsUp) {
                    return 5;
                }
                break;
            case DAY:
                if (this.sunIsUp) {
                    return 6;
                }
                break;
            case RAIN:
                if ((this.getWorld().isRaining() || this.getWorld().isThundering())) {
                    return 7;
                }
                break;

        }
        setType(EnumType.DEFAULT);
        return 0;
    }

    @Override
    public boolean emitsEnergyTo(final EnergyAcceptor var1, final Direction var2) {
        return true;
    }


    @Override
    public void updateTileServer(Player var1, double var2) {

    }
}
