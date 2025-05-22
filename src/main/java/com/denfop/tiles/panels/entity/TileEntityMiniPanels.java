package com.denfop.tiles.panels.entity;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IAdvEnergyNet;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.SunCoef;
import com.denfop.api.solar.EnumSolarType;
import com.denfop.api.solar.EnumTypeParts;
import com.denfop.api.solar.ISolarTile;
import com.denfop.api.solar.SolarEnergySystem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentMiniPanel;
import com.denfop.componets.ComponentPollution;
import com.denfop.componets.ComponentTimer;
import com.denfop.container.ContainerMiniPanels;
import com.denfop.gui.GuiMiniPanel;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotGlassMiniPanels;
import com.denfop.invslot.InvSlotOutputMiniPanels;
import com.denfop.invslot.InvSlotStorageMiniPanels;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import net.minecraft.block.material.MapColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TileEntityMiniPanels extends TileEntityInventory implements ISolarTile {

    public final InvSlotStorageMiniPanels invSlotStorage;
    public final InvSlotOutputMiniPanels invSlotOutput;
    public final ComponentTimer timer;
    public final InvSlot invSlotCore;
    public ComponentMiniPanel component;
    public InvSlotGlassMiniPanels invSlotGlass;
    public ComponentPollution pollution;
    public boolean canRain;
    public boolean hasSky;
    public Biome biome;
    public boolean wetBiome;
    public boolean noSunWorld;
    public boolean rain;
    public boolean sunIsUp;
    public boolean skyIsVisible;
    public List<List<EnumState>> listStable = new ArrayList<>(9);
    public double bonusGeneration;
    public TileSolarPanel.GenerationState activeState = TileSolarPanel.GenerationState.NONE;
    public double load;
    public double genDay = 0;
    public double genNight = 0;
    public double genDayNight = 0;
    public double generating;
    private int level;
    private SunCoef sunCoef;

    public TileEntityMiniPanels() {
        this.component = this.addComponent(ComponentMiniPanel.asBasicSource(this, 0, 14));
        this.invSlotGlass = new InvSlotGlassMiniPanels(this);
        this.invSlotStorage = new InvSlotStorageMiniPanels(this);
        this.invSlotOutput = new InvSlotOutputMiniPanels(this);
        this.invSlotCore = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 2) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.core;
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                SolarEnergySystem.system.calculateCores(((TileEntityMiniPanels) this.base));
                SolarEnergySystem.system.recalculation(((TileEntityMiniPanels) this.base), EnumTypeParts.GENERATION);
            }
        };
        this.pollution = this.addComponent(new ComponentPollution(this));
        this.timer = this.addComponent(new ComponentTimer(this, new Timer(3, 0, 0), new Timer(2, 30, 0), new Timer(2, 0, 0)));
        this.pollution.setTimer(timer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemStack, final List<String> info) {


        if (this.getWorld() != null) {


            if (this.world.isDaytime()) {
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

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.minipanel;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {

        return Collections.singletonList(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D));

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            generating = (double) DecoderHandler.decode(customPacketBuffer);
            invSlotGlass.readFromNbt(getNBTFromSlot(customPacketBuffer));
            invSlotStorage.readFromNbt(getNBTFromSlot(customPacketBuffer));
            invSlotOutput.readFromNbt(getNBTFromSlot(customPacketBuffer));
            bonusGeneration = (double) DecoderHandler.decode(customPacketBuffer);
            rain = (boolean) DecoderHandler.decode(customPacketBuffer);
            noSunWorld = (boolean) DecoderHandler.decode(customPacketBuffer);
            hasSky = (boolean) DecoderHandler.decode(customPacketBuffer);
            skyIsVisible = (boolean) DecoderHandler.decode(customPacketBuffer);
            load = (double) DecoderHandler.decode(customPacketBuffer);
            listStable = (List<List<EnumState>>) DecoderHandler.decode(customPacketBuffer);
            sunIsUp = (boolean) DecoderHandler.decode(customPacketBuffer);
            level = (int) DecoderHandler.decode(customPacketBuffer);
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
            EncoderHandler.encode(packet, level);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void setCapacity(final double capacity) {
        this.component.setCapacity(capacity);
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
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
            invSlotGlass.readFromNbt(((InvSlot) DecoderHandler.decode(customPacketBuffer)).writeToNbt(new NBTTagCompound()));
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
            this.sunCoef = EnergyNetGlobal.instance.getSunCoefficient(this.world);
        }
        return this.sunCoef.getCoef();

    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!world.isRemote) {
            this.canRain = (this.world.getBiome(this.pos).canRain() || this.world.getBiome(this.pos).getRainfall() > 0.0F);
            this.hasSky = !this.world.provider.isNether();
            this.biome = this.world.getBiome(this.pos);
            updateVisibility();
            SolarEnergySystem.system.calculateCores(this);
            SolarEnergySystem.system.recalculation(this, EnumTypeParts.GENERATION);
            SolarEnergySystem.system.recalculation(this, EnumTypeParts.OUTPUT);
            SolarEnergySystem.system.recalculation(this, EnumTypeParts.CAPACITY);
            IAdvEnergyNet advEnergyNet = EnergyNetGlobal.instance;
            this.sunCoef = advEnergyNet.getSunCoefficient(this.world);
        }
    }

    public void updateVisibility() {
        this.wetBiome = this.biome.getRainfall() > 0.0F;
        this.noSunWorld = this.world.provider.isNether();

        this.rain = this.wetBiome && (this.world.isRaining() || this.world.isThundering());
        this.sunIsUp = this.world.isDaytime();
        this.skyIsVisible = this.world.canBlockSeeSky(this.pos.up()) &&
                (this.world.getBlockState(this.pos.up()).getMaterial().getMaterialMapColor() ==
                        MapColor.AIR) && !this.noSunWorld;
        if (!this.skyIsVisible) {
            this.activeState = TileSolarPanel.GenerationState.NONE;
        }
        if (this.sunIsUp && this.skyIsVisible) {
            if (!(this.rain)) {
                this.activeState = TileSolarPanel.GenerationState.DAY;
            } else {
                this.activeState = TileSolarPanel.GenerationState.RAINDAY;
            }

        }
        if (!this.sunIsUp && this.skyIsVisible) {
            if (!(rain)) {
                this.activeState = TileSolarPanel.GenerationState.NIGHT;
            } else {
                this.activeState = TileSolarPanel.GenerationState.RAINNIGHT;
            }
        }
        if (this.world.provider.getDimension() == 1) {
            this.activeState = TileSolarPanel.GenerationState.END;
        }
        if (this.world.provider.getDimension() == -1) {
            this.activeState = TileSolarPanel.GenerationState.NETHER;
        }

    }

    @Override
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().provider.getWorldTime() % 80 == 0) {
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
        return invSlotStorage;
    }

    @Override
    public List<ItemStack> getOutputItems() {
        return invSlotOutput;
    }

    @Override
    public List<ItemStack> getGenerationItems() {
        return invSlotGlass;
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
    public void setStables(final int index, List<TileEntityMiniPanels.EnumState> enumStateList) {
        if (index < this.listStable.size()) {
            this.listStable.set(index, enumStateList);
        } else {
            this.listStable.add(index, enumStateList);
        }

    }

    @Override
    public List<ItemStack> getCoresItems() {
        return invSlotCore;
    }

    @Override
    public int getCoreLevel() {
        return this.level;
    }

    @Override
    public void setCoreLevel(final int level) {
        this.level = level;
    }

    @Override
    public ContainerMiniPanels getGuiContainer(final EntityPlayer var1) {
        return new ContainerMiniPanels(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiMiniPanel getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiMiniPanel(getGuiContainer(var1));
    }


    public enum EnumState {
        STABLE,
        UNSTABLE,
        NORMAL,
        EMPTY
    }

}
