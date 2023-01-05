package com.denfop.tiles.reactors;

import com.denfop.Config;
import com.denfop.api.gui.IType;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.container.ContainerBaseNuclearReactor;
import com.denfop.gui.GuiNuclearReactor;
import com.denfop.invslot.InvSlotReactor;
import com.denfop.tiles.base.InfoInvSlots;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.energy.tile.IMetaDelegate;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IC2DamageSource;
import ic2.core.IHasGui;
import ic2.core.audio.AudioSource;
import ic2.core.audio.PositionSpec;
import ic2.core.block.invslot.InvSlot;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.init.Localization;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public abstract class TileEntityBaseNuclearReactorElectric extends TileEntityInventory implements IHasGui, IAdvReactor,
        IEnergySource, IMetaDelegate, IGuiValueProvider, INetworkClientTileEntityEventListener, IType {

    public final int sizeX;
    public final int sizeY;
    public final double coef;
    public final InvSlotReactor reactorSlot;
    public boolean getblock;
    public boolean work;
    public float output = 0.0F;
    public int updateTicker;
    public int heat = 0;
    public int maxHeat = 10000;
    public float hem = 1.0F;
    public String background;
    public AudioSource audioSourceMain;
    public AudioSource audioSourceGeiger;
    public boolean addedToEnergyNet = false;
    public List<ReactorsItem> reactorsItemList = new ArrayList<>();
    public boolean change = true;
    public int size;
    protected float lastOutput = 0.0F;
    protected List<IEnergyTile> subTiles = new ArrayList<>();
    protected float coef1 = 1;

    public TileEntityBaseNuclearReactorElectric(int sizeX, int sizeY, String background, double coef) {
        this.updateTicker = IC2.random.nextInt(this.getTickRate());
        this.reactorSlot = new InvSlotReactor(this, "reactor", sizeX * sizeY);
        this.getblock = false;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.background = background;
        this.coef = coef;
        this.size = sizeX;
    }

    public static void showHeatEffects(World world, BlockPos pos, int heat) {
        Random rnd = world.rand;
        if (rnd.nextInt(8) == 0) {
            int puffs = heat / 1000;
            if (puffs > 0) {
                puffs = rnd.nextInt(puffs);

                int n;
                for (n = 0; n < puffs; ++n) {
                    world.spawnParticle(
                            EnumParticleTypes.SMOKE_NORMAL,
                            (float) pos.getX() + rnd.nextFloat(),
                            (float) pos.getY() + 0.95F,
                            (float) pos.getZ() + rnd.nextFloat(),
                            0.0D,
                            0.0D,
                            0.0D
                    );
                }

                puffs -= rnd.nextInt(4) + 3;

                for (n = 0; n < puffs; ++n) {
                    world.spawnParticle(
                            EnumParticleTypes.FLAME,
                            (float) pos.getX() + rnd.nextFloat(),
                            pos.getY() + 1,
                            (float) pos.getZ() + rnd.nextFloat(),
                            0.0D,
                            0.0D,
                            0.0D
                    );
                }
            }

        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        tooltip.add(Localization.translate("iu.reactor_info") + this.coef);
        tooltip.add(Localization.translate("iu.reactor_info1") + this.sizeX + "x" + this.sizeY);
        super.addInformation(stack, tooltip, advanced);
    }

    public List<ReactorsItem> getReactorsItems() {
        return this.reactorsItemList;
    }

    public double getGuiValue(String name) {
        if ("heat".equals(name)) {
            return this.maxHeat == 0 ? 0.0D : (double) this.heat / (double) this.maxHeat;
        } else {
            throw new IllegalArgumentException("Invalid value: " + name);
        }
    }

    abstract void setblock();

    public void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating() && !this.isFluidCooled()) {
            this.refreshChambers();
            if (!this.addedToEnergyNet) {
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            }
            this.addedToEnergyNet = true;
            this.setActive(this.work);
        }
        this.reactorSlot.update();

    }

    public void onUnloaded() {
        if (IC2.platform.isRendering()) {
            IC2.audioManager.removeSources(this);
            this.audioSourceMain = null;
            this.audioSourceGeiger = null;
        }

        if (IC2.platform.isSimulating() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }

        super.onUnloaded();
    }

    public String getInventoryName() {
        return "Nuclear Reactor";
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.heat = nbttagcompound.getInteger("heat");
        this.output = (float) nbttagcompound.getDouble("output");
        this.getblock = nbttagcompound.getBoolean("getblock");
        this.work = nbttagcompound.getBoolean("work");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("heat", this.heat);
        nbttagcompound.setDouble("output", this.getReactorEnergyOutput());
        nbttagcompound.setBoolean("getblock", this.getblock);
        nbttagcompound.setBoolean("work", this.work);
        return nbttagcompound;
    }


    public void drawEnergy(double amount) {
    }


    public double getOfferedEnergy() {
        return this.getReactorEnergyOutput() * 5.0F * coef1;
    }


    public double getReactorEUEnergyOutput() {
        return this.getOfferedEnergy();
    }

    public int getSourceTier() {
        return Math.max(EnergyNet.instance.getTierFromPower(this.getOfferedEnergy()), 2);
    }

    abstract void getSubs();

    public void refreshChambers() {
        getSubs();
        infoInvSlotsList.clear();
        for (InvSlot slot : this.invSlots) {
            for (int k = 0; k < slot.size(); k++) {
                infoInvSlotsList.add(new InfoInvSlots(slot, k));
            }

        }
        this.size_inventory = 0;

        InvSlot invSlot;
        for (Iterator var2 = this.invSlots.iterator(); var2.hasNext(); size_inventory += invSlot.size()) {
            invSlot = (InvSlot) var2.next();
        }

    }

    @Override
    public boolean emitsEnergyTo(final IEnergyAcceptor iEnergyAcceptor, final EnumFacing enumFacing) {
        return true;
    }

    @Override
    public World getWorldObj() {
        return this.getWorld();
    }

    @Override
    public TileEntity getCoreTe() {
        return this;
    }

    public void updateEntityServer() {
        super.updateEntityServer();

        if (this.updateTicker++ % this.getTickRate() == 0) {
            if (!this.getWorld().isAreaLoaded(this.pos, 4)) {
                this.output = 0.0F;
            } else {


                this.output = 0.0F;
                this.maxHeat = 10000;
                this.hem = 1.0F;
                this.processChambers();


                if (this.calculateHeatEffects()) {
                    return;
                }
                boolean work = this.receiveredstone();
                if (this.getActive() != work) {
                    this.setActive(work);
                }

            }

        }

    }


    public boolean calculateHeatEffects() {
        if (this.heat >= 4000 && IC2.platform.isSimulating()) {
            float power = (float) this.heat / (float) this.maxHeat;
            if (power >= 1.0F) {
                if (Config.explode) {
                    this.explode();
                } else {
                    setblock();
                }
                return true;
            } else {
                Block block;
                Material mat;
                BlockPos coord1;
                if (power >= 0.85F && this.getWorld().rand.nextFloat() <= 0.2F * this.hem) {
                    coord1 = this.getRandCoord1(2);
                    if (coord1 != null) {

                        block = this.getWorld().getBlockState(coord1).getBlock();
                        final IBlockState state = this.getWorld().getBlockState(coord1);
                        if (block.isAir(state, this.getWorld(), coord1)) {
                            this.getWorld().setBlockState(coord1, Blocks.FIRE.getDefaultState());
                        } else if (state.getBlockHardness(this.getWorld(), coord1) >= 0.0F && this
                                .getWorld()
                                .getTileEntity(coord1) == null) {
                            mat = state.getMaterial();
                            if (mat != Material.ROCK && mat != Material.IRON && mat != Material.LAVA && mat != Material.GROUND && mat != Material.CLAY) {
                                this.getWorld().setBlockState(coord1, Blocks.FIRE.getDefaultState());
                            } else {
                                this.getWorld().setBlockState(coord1, Blocks.FLOWING_LAVA.getDefaultState());
                            }
                        }
                    }
                }

                if (power >= 0.7F) {
                    List<EntityLivingBase> list1 = this.getWorld().getEntitiesWithinAABB(
                            EntityLivingBase.class,
                            new AxisAlignedBB(
                                    this.getPos().getX() - 3,
                                    this.pos.getY() - 3,
                                    this.pos.getZ() - 3,
                                    this.pos.getX() + 4,
                                    this.pos.getY() + 4,
                                    this.pos.getZ() + 4
                            )
                    );

                    for (EntityLivingBase o : list1) {
                        o.attackEntityFrom(
                                IC2DamageSource.radiation,
                                (float) ((int) ((float) this.getWorld().rand.nextInt(4) * this.hem))
                        );
                    }
                }

                if (power >= 0.5F && this.getWorld().rand.nextFloat() <= this.hem) {
                    coord1 = this.getRandCoord1(2);
                    if (coord1 != null) {

                        IBlockState state = this.getWorld().getBlockState(coord1);
                        if (state.getMaterial() == Material.WATER) {
                            this.getWorld().setBlockToAir(coord1);
                        }
                    }
                }

                if (power >= 0.4F && this.getWorld().rand.nextFloat() <= this.hem) {
                    coord1 = this.getRandCoord1(2);
                    if (coord1 != null && this.getWorld().getTileEntity(coord1) == null) {
                        IBlockState state = this.getWorld().getBlockState(coord1);
                        mat = state.getMaterial();
                        if (mat == Material.WOOD || mat == Material.LEAVES || mat == Material.CLOTH) {
                            this.getWorld().setBlockState(coord1, Blocks.FIRE.getDefaultState());
                        }
                    }
                }

                return false;
            }
        } else {
            return false;
        }
    }

    public BlockPos getRandCoord1(int radius) {
        if (radius <= 0) {
            return null;
        } else {
            World world = this.getWorld();

            BlockPos ret;
            do {
                ret = this.pos.add(
                        world.rand.nextInt(2 * radius + 1) - radius,
                        world.rand.nextInt(2 * radius + 1) - radius,
                        world.rand.nextInt(2 * radius + 1) - radius
                );
            } while (ret.equals(this.pos));

            return ret;
        }
    }


    public void processChambers() {
        try {
            for (ReactorsItem reactorsItem : this.getReactorsItems()) {
                reactorsItem.update();
            }
        } catch (Exception ignored) {
            this.output = 0.0F;
            this.reactorSlot.update();
        }
    }

    public boolean produceEnergy() {
        return this.receiveredstone();
    }

    public boolean receiveredstone() {
        return this.work;
    }

    public abstract short getReactorSize();


    public int getTickRate() {
        return 20;
    }

    public ContainerBase<TileEntityBaseNuclearReactorElectric> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerBaseNuclearReactor(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiNuclearReactor(new ContainerBaseNuclearReactor(entityPlayer, this));
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {
    }

    public void onNetworkUpdate(String field) {
        if (field.equals("output")) {
            if (this.output > 0.0F) {
                if (this.lastOutput <= 0.0F) {
                    if (this.audioSourceMain == null) {
                        this.audioSourceMain = IC2.audioManager.createSource(
                                this,
                                PositionSpec.Center,
                                "Generators/NuclearReactor/NuclearReactorLoop.ogg",
                                true,
                                false,
                                IC2.audioManager.getDefaultVolume()
                        );
                    }

                    if (this.audioSourceMain != null) {
                        this.audioSourceMain.play();
                    }
                }

                if (this.output < 40.0F) {
                    if (this.lastOutput <= 0.0F || this.lastOutput >= 40.0F) {
                        if (this.audioSourceGeiger != null) {
                            this.audioSourceGeiger.remove();
                        }

                        this.audioSourceGeiger = IC2.audioManager.createSource(
                                this,
                                PositionSpec.Center,
                                "Generators/NuclearReactor/GeigerLowEU.ogg",
                                true,
                                false,
                                IC2.audioManager.getDefaultVolume()
                        );
                        if (this.audioSourceGeiger != null) {
                            this.audioSourceGeiger.play();
                        }
                    }
                } else if (this.output < 80.0F) {
                    if (this.lastOutput < 40.0F || this.lastOutput >= 80.0F) {
                        if (this.audioSourceGeiger != null) {
                            this.audioSourceGeiger.remove();
                        }

                        this.audioSourceGeiger = IC2.audioManager.createSource(
                                this,
                                PositionSpec.Center,
                                "Generators/NuclearReactor/GeigerMedEU.ogg",
                                true,
                                false,
                                IC2.audioManager.getDefaultVolume()
                        );
                        if (this.audioSourceGeiger != null) {
                            this.audioSourceGeiger.play();
                        }
                    }
                } else if (this.output >= 80.0F && this.lastOutput < 80.0F) {
                    if (this.audioSourceGeiger != null) {
                        this.audioSourceGeiger.remove();
                    }

                    this.audioSourceGeiger = IC2.audioManager.createSource(
                            this,
                            PositionSpec.Center,
                            "Generators/NuclearReactor/GeigerHighEU.ogg",
                            true,
                            false,
                            IC2.audioManager.getDefaultVolume()
                    );
                    if (this.audioSourceGeiger != null) {
                        this.audioSourceGeiger.play();
                    }
                }
            } else if (this.lastOutput > 0.0F) {
                if (this.audioSourceMain != null) {
                    this.audioSourceMain.stop();
                }

                if (this.audioSourceGeiger != null) {
                    this.audioSourceGeiger.stop();
                }
            }

            this.lastOutput = this.output;
        }

        super.onNetworkUpdate(field);
    }

    public float getWrenchDropRate() {
        return 0.8F;
    }

    public BlockPos getPosition() {
        return this.pos;
    }


    public int getHeat() {
        return this.heat;
    }

    public void setHeat(int heat1) {
        this.heat = heat1;
    }

    public int addHeat(int amount) {
        this.heat += amount;
        return this.heat;
    }

    public ItemStack getItemAt(int x, int y) {
        return x >= 0 && x < this.getReactorSize() && y >= 0 && y < this.sizeY ? this.reactorSlot.get(x, y) : null;
    }

    public void setItemAt(int x, int y, ItemStack item) {
        if (x >= 0 && x < this.getReactorSize() && y >= 0 && y < this.sizeY) {
            this.reactorSlot.put(x, y, item);
        }
    }

    public abstract void explode();

    protected void onNeighborChange(Block neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (this.addedToEnergyNet) {
            this.refreshChambers();
        }

    }

    public void addEmitHeat(int heat) {

    }

    public int getMaxHeat() {
        return this.maxHeat;
    }

    public void setMaxHeat(int newMaxHeat) {
        this.maxHeat = newMaxHeat;
    }

    public float getHeatEffectModifier() {
        return this.hem;
    }

    public void setHeatEffectModifier(float newHEM) {
        this.hem = newHEM;
    }

    public float getReactorEnergyOutput() {
        return (float) (this.output * this.coef);
    }

    public float addOutput(float energy) {
        return this.output += energy;
    }


    public boolean isFluidCooled() {
        return false;
    }


    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {
        this.work = !this.work;
        this.setActive(this.work);
    }

}
