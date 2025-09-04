package com.denfop.blockentity.panels.entity;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.energy.interfaces.EnergyNet;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.api.energy.utils.SunCoef;
import com.denfop.api.solar.EnumSolarType;
import com.denfop.api.solar.EnumTypeParts;
import com.denfop.api.solar.ISolarTile;
import com.denfop.api.solar.SolarEnergySystem;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentMiniPanel;
import com.denfop.componets.ComponentPollution;
import com.denfop.componets.ComponentTimer;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuMiniPanels;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryGlassMiniPanels;
import com.denfop.inventory.InventoryOutputMiniPanels;
import com.denfop.inventory.InventoryStorageMiniPanels;
import com.denfop.items.ItemCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenMiniPanel;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockEntityMiniPanels extends BlockEntityInventory implements ISolarTile {

    public final InventoryStorageMiniPanels invSlotStorage;
    public final InventoryOutputMiniPanels invSlotOutput;
    public final ComponentTimer timer;
    public final Inventory inventoryCore;
    public ComponentMiniPanel component;
    public InventoryGlassMiniPanels invSlotGlass;
    public ComponentPollution pollution;
    public boolean canRain;
    public boolean hasSky;
    public Holder<Biome> biome;
    public boolean wetBiome;
    public boolean noSunWorld;
    public boolean rain;
    public boolean sunIsUp;
    public boolean skyIsVisible;
    public List<List<EnumState>> listStable = new ArrayList<>(9);
    public double bonusGeneration;
    public BlockEntitySolarPanel.GenerationState activeState = BlockEntitySolarPanel.GenerationState.NONE;
    public double load;
    public double genDay = 0;
    public double genNight = 0;
    public double genDayNight = 0;
    public double generating;
    private int levelBlock;
    private SunCoef sunCoef;

    public BlockEntityMiniPanels(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.minipanel, pos, state);
        this.component = this.addComponent(ComponentMiniPanel.asBasicSource(this, 0, 14));
        this.invSlotGlass = new InventoryGlassMiniPanels(this);
        this.invSlotStorage = new InventoryStorageMiniPanels(this);
        this.invSlotOutput = new InventoryOutputMiniPanels(this);
        this.inventoryCore = new Inventory(this, Inventory.TypeItemSlot.INPUT, 2) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() instanceof ItemCore<?>;
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                SolarEnergySystem.system.calculateCores(((BlockEntityMiniPanels) this.base));
                SolarEnergySystem.system.recalculation(((BlockEntityMiniPanels) this.base), EnumTypeParts.GENERATION);
                return content;
            }
        };
        this.pollution = this.addComponent(new ComponentPollution(this));
        this.timer = this.addComponent(new ComponentTimer(this, new Timer(3, 0, 0), new Timer(2, 30, 0), new Timer(2, 0, 0)));
        this.pollution.setTimer(timer);
    }

    @Override
    public void addInformation(final ItemStack itemStack, final List<String> info) {
        info.add(Localization.translate("iu.minipanel.info"));
        info.add(Localization.translate("iu.minipanel.info2"));
        info.add(Localization.translate("iu.minipanel.info3"));

        if (this.getWorld() != null) {


            if (this.level.isDay()) {
                info.add(Localization.translate("supsolpans.iu.GenerationDay.tooltip") + " "
                        + ModUtils.getString(this.generating) + " EF/t ");
                info.add(Localization.translate("supsolpans.iu.GenerationNight.tooltip") + " "
                        + ModUtils.getString(this.genNight) + " EF/t ");
            } else {
                info.add(Localization.translate("supsolpans.iu.GenerationDay.tooltip") + " "
                        + ModUtils.getString(this.genDay) + " EF/t ");
                info.add(Localization.translate("supsolpans.iu.GenerationNight.tooltip") + " "
                        + ModUtils.getString(this.generating) + " EF/t ");
            }
            info.add(Localization.translate("iu.item.tooltip.Output") + " "
                    + ModUtils.getString(this.component.getProdution()) + " EF/t ");
            info.add(Localization.translate("iu.item.tooltip.Capacity") + " "
                    + ModUtils.getString(this.component.storage) + " EF ");
            info.add(Localization.translate("iu.tier") + ModUtils.getString(this.component.getSourceTier()));
        }


    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.minipanel;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    public List<AABB> getAabbs(boolean forCollision) {

        return Collections.singletonList(new AABB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D));

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            generating = (double) DecoderHandler.decode(customPacketBuffer);
            invSlotGlass.readFromNbt(customPacketBuffer.registryAccess(), getNBTFromSlot(customPacketBuffer));
            invSlotStorage.readFromNbt(customPacketBuffer.registryAccess(), getNBTFromSlot(customPacketBuffer));
            invSlotOutput.readFromNbt(customPacketBuffer.registryAccess(), getNBTFromSlot(customPacketBuffer));
            bonusGeneration = (double) DecoderHandler.decode(customPacketBuffer);
            rain = (boolean) DecoderHandler.decode(customPacketBuffer);
            noSunWorld = (boolean) DecoderHandler.decode(customPacketBuffer);
            hasSky = (boolean) DecoderHandler.decode(customPacketBuffer);
            skyIsVisible = (boolean) DecoderHandler.decode(customPacketBuffer);
            load = (double) DecoderHandler.decode(customPacketBuffer);
            listStable = (List<List<EnumState>>) DecoderHandler.decode(customPacketBuffer);
            sunIsUp = (boolean) DecoderHandler.decode(customPacketBuffer);
            levelBlock = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, generating);
            EncoderHandler.encode(packet, invSlotGlass);
            EncoderHandler.encode(packet, invSlotStorage);
            EncoderHandler.encode(packet, invSlotOutput);
            EncoderHandler.encode(packet, bonusGeneration);
            EncoderHandler.encode(packet, rain);
            EncoderHandler.encode(packet, noSunWorld);
            EncoderHandler.encode(packet, hasSky);
            EncoderHandler.encode(packet, skyIsVisible);
            EncoderHandler.encode(packet, load);
            EncoderHandler.encode(packet, listStable);
            EncoderHandler.encode(packet, sunIsUp);
            EncoderHandler.encode(packet, levelBlock);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void setCapacity(final double capacity) {
        this.component.setCapacity(capacity);
    }


    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, this.pollution, false);
            EncoderHandler.encode(packet, this.timer, false);
            EncoderHandler.encode(packet, this.component, false);
            EncoderHandler.encode(packet, invSlotGlass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            pollution.onNetworkUpdate(customPacketBuffer);
            timer.onNetworkUpdate(customPacketBuffer);
            component.onNetworkUpdate(customPacketBuffer);
            invSlotGlass.readFromNbt(customPacketBuffer.registryAccess(), ((Inventory) DecoderHandler.decode(customPacketBuffer)).writeToNbt(customPacketBuffer.registryAccess(), new CompoundTag()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void gainFuel() {
        double coefpollution = 1;
        switch (this.timer.getIndexWork()) {
            case -1:
                coefpollution = 0.25;
                break;
            case 1:
                coefpollution = 0.75;
                break;
            case 2:
                coefpollution = 0.5;
                break;
        }

        switch (this.activeState) {
            case DAY:
                this.generating = (this.genDay + this.genDayNight) * (1 + this.bonusGeneration);
                break;
            case NIGHT:
                this.generating = (this.genNight + this.genDayNight) * (1 + this.bonusGeneration);
                break;
            case RAINDAY:
                this.generating = 0.65 * (this.genDay + this.genDayNight) * (1 + this.bonusGeneration);
                break;
            case RAINNIGHT:
                this.generating = 0.65 * (this.genNight + this.genDayNight) * (1 + this.bonusGeneration);
                break;
            case NETHER:
            case END:
            case NONE:
                this.generating = 0;
                break;

        }
        this.generating *= coefpollution * experimental_generating();
    }

    private double experimental_generating() {
        if (this.sunCoef == null) {
            this.sunCoef = EnergyNetGlobal.instance.getSunCoefficient(this.level);
        }
        return this.sunCoef.getCoef();

    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            this.biome = this.level.getBiome(this.pos);
            this.canRain = (this.biome.value().getPrecipitationAt(this.pos) == Biome.Precipitation.RAIN || this.biome.value().getModifiedClimateSettings().downfall() > 0.0F);
            this.hasSky = this.level.dimensionType().hasSkyLight();
            updateVisibility();
            SolarEnergySystem.system.calculateCores(this);
            SolarEnergySystem.system.recalculation(this, EnumTypeParts.GENERATION);
            SolarEnergySystem.system.recalculation(this, EnumTypeParts.OUTPUT);
            SolarEnergySystem.system.recalculation(this, EnumTypeParts.CAPACITY);
            EnergyNet advEnergyNet = EnergyNetGlobal.instance;
            this.sunCoef = advEnergyNet.getSunCoefficient(this.level);
        }
    }

    public void updateVisibility() {
        this.wetBiome = this.biome.value().getModifiedClimateSettings().downfall() > 0.0F;
        this.noSunWorld = this.level.dimension() == Level.NETHER;

        this.rain = this.wetBiome && (this.level.isRaining() || this.level.isThundering());
        this.sunIsUp = this.level.isDay();
        this.skyIsVisible = this.level.canSeeSky(this.pos.above()) &&
                this.level.getBlockState(this.pos.above()).getMapColor(level, pos.above()) == MapColor.NONE &&
                !this.noSunWorld;

        if (!this.skyIsVisible) {
            this.activeState = BlockEntitySolarPanel.GenerationState.NONE;
        }
        if (this.sunIsUp && this.skyIsVisible) {
            if (!(this.rain)) {
                this.activeState = BlockEntitySolarPanel.GenerationState.DAY;
            } else {
                this.activeState = BlockEntitySolarPanel.GenerationState.RAINDAY;
            }

        }
        if (!this.sunIsUp && this.skyIsVisible) {
            if (!(rain)) {
                this.activeState = BlockEntitySolarPanel.GenerationState.NIGHT;
            } else {
                this.activeState = BlockEntitySolarPanel.GenerationState.RAINNIGHT;
            }
        }
        if (this.level.dimension() == Level.END) {
            this.activeState = BlockEntitySolarPanel.GenerationState.END;
        }
        if (this.level.dimension() == Level.NETHER) {
            this.activeState = BlockEntitySolarPanel.GenerationState.NETHER;
        }

    }


    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 80 == 0) {
            updateVisibility();
        }
        this.timer.setCanWork(!this.invSlotGlass.isEmpty());
        this.generating = 0;
        if (load >= 100) {
            return;
        }
        this.gainFuel();
        this.component.addEnergy(generating);
    }

    @Override
    public void setOutput(final double output) {
        this.component.setProdution(output);
    }

    @Override
    public void setGeneration(final EnumSolarType solarType, final double generation) {
        switch (solarType) {
            case DAY:
                this.genDay = generation;
                break;
            case NIGHT:
                this.genNight = generation;
                break;
            case DAY_NIGHT:
                this.genDayNight = generation;
                break;
        }
    }

    @Override
    public List<ItemStack> getCapacityItems() {
        return new ArrayList<>(invSlotStorage);
    }

    @Override
    public List<ItemStack> getOutputItems() {
        return new ArrayList<>(invSlotOutput);
    }

    @Override
    public List<ItemStack> getGenerationItems() {
        return new ArrayList<>(invSlotGlass);
    }

    @Override
    public void setBonus(final EnumTypeParts typeBonus, final double bonus) {

        switch (typeBonus) {
            case OUTPUT:
                this.component.setBonusProdution(bonus);
                break;
            case CAPACITY:
                this.component.setBonusCapacity(bonus);
                break;
            case GENERATION:
                this.bonusGeneration = bonus;
                break;
        }
    }

    @Override
    public double getBonus(final EnumTypeParts typeBonus) {
        switch (typeBonus) {
            case OUTPUT:
                return this.component.getBonusProdution();
            case CAPACITY:
                return this.component.getBonusCapacity();
            case GENERATION:
                return this.bonusGeneration;
        }
        return 0;
    }

    @Override
    public void setLoad(final double load) {
        this.load = load;
    }

    @Override
    public List<List<EnumState>> getStables() {
        return this.listStable;
    }

    @Override
    public void setStables(final int index, List<BlockEntityMiniPanels.EnumState> enumStateList) {
        if (index < this.listStable.size()) {
            this.listStable.set(index, enumStateList);
        } else {
            this.listStable.add(index, enumStateList);
        }

    }

    @Override
    public List<ItemStack> getCoresItems() {
        return new ArrayList<>(inventoryCore);
    }

    @Override
    public int getCoreLevel() {
        return this.levelBlock;
    }

    @Override
    public void setCoreLevel(final int level) {
        this.levelBlock = level;
    }

    @Override
    public ContainerMenuMiniPanels getGuiContainer(final Player var1) {
        return new ContainerMenuMiniPanels(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenMiniPanel((ContainerMenuMiniPanels) menu);
    }


    public enum EnumState {
        STABLE,
        UNSTABLE,
        NORMAL,
        EMPTY
    }

}
