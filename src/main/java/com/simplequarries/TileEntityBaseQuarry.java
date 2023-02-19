package com.simplequarries;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.gui.IType;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.vein.Type;
import com.denfop.api.vein.Vein;
import com.denfop.api.vein.VeinSystem;
import com.denfop.audio.AudioSource;
import com.denfop.audio.PositionSpec;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.CoolComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.items.modules.EnumQuarryModules;
import com.denfop.items.modules.EnumQuarryType;
import com.denfop.tiles.base.FakePlayerSpawner;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ExperienceUtils;
import com.denfop.utils.ModUtils;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.type.ResourceBlock;
import ic2.core.init.Localization;
import ic2.core.ref.BlockName;
import ic2.core.ref.TeBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityBaseQuarry extends TileEntityInventory implements IHasGui, IAudioFixer,
        INetworkClientTileEntityEventListener,
        IUpgradableBlock, IType {

    public final String name;
    public final int index;
    public final InvSlotBaseQuarry input;
    public final double constenergyconsume;
    public final double speed;
    public final InvSlotOutput outputSlot;
    public final ComponentBaseEnergy exp;
    public final CoolComponent cold;
    public int min_y;
    public int max_y;
    public AudioSource audioSource;
    public double energyconsume;
    public AdvEnergy energy;
    public BlockPos blockpos = null;
    public FakePlayerSpawner player;
    public boolean work;
    public boolean need_work = true;
    public List<ItemStack> list = new ArrayList<>();
    public ComponentBaseEnergy energy1;
    public boolean analyzer;
    public int progress;
    public EnumQuarryModules list_modules;
    public boolean mac_enabled = false;
    public boolean comb_mac_enabled = false;
    public double consume;
    public boolean furnace;
    public int chance;
    public int col;
    public Vein vein;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    public boolean vein_need = false;
    public BlockPos default_pos;
    public int chunkx1;
    public int chunkz1;
    public int chunkx2;
    public int chunkz2;
    public int chunkx;
    public int chunkz;
    private boolean sound = true;

    public TileEntityBaseQuarry(String name, double coef, int index) {
        this.name = name;
        this.energyconsume = 250 * coef;
        this.energy = this.addComponent(AdvEnergy.asBasicSink(this, 5E7D, 14));
        this.energy1 = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 200000, 14));
        this.cold = this.addComponent(CoolComponent.asBasicSink(this, 100));

        this.outputSlot = new InvSlotOutput(this, "output", 24);
        this.work = true;
        this.index = index;
        this.speed = Math.pow(2, index - 1);
        this.input = new InvSlotBaseQuarry(this, index);
        this.constenergyconsume = 250 * coef;
        this.min_y = 0;
        this.max_y = 256;
        this.chance = 0;
        this.col = 1;
        this.furnace = false;
        this.list_modules = null;
        this.consume = this.energyconsume;
        this.exp = this.addComponent(ComponentBaseEnergy.asBasicSource(EnergyType.EXPERIENCE,this, 5000, 14));

    }

    public static int onBlockBreakEvent(World world, GameType gameType, EntityPlayerMP entityPlayer, BlockPos pos) {
        // Logic from tryHarvestBlock for pre-canceling the event
        boolean preCancelEvent = false;
        ItemStack itemstack = entityPlayer.getHeldItemMainhand();
        if (gameType.isCreative() && !itemstack.isEmpty()
                && !itemstack.getItem().canDestroyBlockInCreative(world, pos, itemstack, entityPlayer)) {
            preCancelEvent = true;
        }

        if (gameType.hasLimitedInteractions()) {
            if (gameType == GameType.SPECTATOR) {
                preCancelEvent = true;
            }

            if (!entityPlayer.isAllowEdit()) {
                if (itemstack.isEmpty() || !itemstack.canDestroy(world.getBlockState(pos).getBlock())) {
                    preCancelEvent = true;
                }
            }
        }


        // Post the block break event
        IBlockState state = world.getBlockState(pos);
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, state, entityPlayer);
        event.setCanceled(preCancelEvent);
        MinecraftForge.EVENT_BUS.post(event);

        // Handle if the event is canceled
        if (event.isCanceled()) {
            // Let the client know the block still exists
            entityPlayer.connection.sendPacket(new SPacketBlockChange(world, pos));

            // Update any tile entity data for this block
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity != null) {
                Packet<?> pkt = tileentity.getUpdatePacket();
                if (pkt != null) {
                    entityPlayer.connection.sendPacket(pkt);
                }
            }
        }
        return event.isCanceled() ? -1 : event.getExpToDrop();
    }

    public void changeSound() {
        sound = !sound;
        IC2.network.get(true).updateTileEntityField(this, "sound");

        if (!sound) {
            if (this.getType() == EnumTypeAudio.ON) {
                setType(EnumTypeAudio.OFF);
                IC2.network.get(true).initiateTileEntityEvent(this, 2, true);

            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.multimachine.info"));
            tooltip.add(Localization.translate("iu.simplyquarries.info") + index);
            tooltip.add(Localization.translate("iu.simplyquarries.info1") + (int) this.constenergyconsume + " EU/t");
            tooltip.add(Localization.translate("iu.simplyquarries.info2") + (int) this.speed + " " + Localization.translate("iu" +
                    ".simplyquarries.info3"));
        }

        if (this.hasComponent(AdvEnergy.class)) {
            AdvEnergy energy = this.getComponent(AdvEnergy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        if (energy1 != 0) {
            tooltip.add(Localization.translate("ic2.item.tooltip.Store") + " " + ModUtils.getString(energy1) + "/" + ModUtils.getString(
                    energy.getCapacity())
                    + " EU");
        }
        final double energy2 = nbt.getDouble("energy1");
        if (energy2 != 0) {
            tooltip.add(Localization.translate("ic2.item.tooltip.Store") + " " + ModUtils.getString(energy2) + "/" + ModUtils.getString(
                    this.energy1.getCapacity())
                    + " QE");
        }
        final double energy3 = nbt.getDouble("energy2");
        if (energy3 != 0) {
            tooltip.add(Localization.translate("ic2.item.tooltip.Store") + " " + ModUtils.getString(energy3) + "/" + ModUtils.getString(
                    exp.getCapacity())
                    + " EXP");
        }
    }

    protected ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        if (!wrench) {
            switch (this.teBlock.getDefaultDrop()) {
                case Self:
                default:
                    final AdvEnergy component = this.energy;
                    if (component != null) {
                        if (component.getEnergy() != 0) {
                            final NBTTagCompound nbt = ModUtils.nbt(drop);
                            nbt.setDouble("energy", component.getEnergy());
                        }
                    }
                    final ComponentBaseEnergy component1 = this.energy1;
                    if (component1 != null) {
                        if (component1.getEnergy() != 0) {
                            final NBTTagCompound nbt = ModUtils.nbt(drop);
                            nbt.setDouble("energy1", component1.getEnergy());
                        }
                    }
                    final ComponentBaseEnergy component2 = this.exp;
                    if (component2 != null) {
                        if (component2.getEnergy() != 0) {
                            final NBTTagCompound nbt = ModUtils.nbt(drop);
                            nbt.setDouble("energy2", component2.getEnergy());
                        }
                    }
                    final CoolComponent component3 = this.cold;
                    if (component3 != null) {
                        if (component3.getEnergy() != 0) {
                            final NBTTagCompound nbt = ModUtils.nbt(drop);
                            nbt.setDouble("energy3", component3.getEnergy());
                        }
                    }
                    return drop;
                case None:
                    return null;
                case Generator:
                    return BlockName.te.getItemStack(TeBlock.generator);
                case Machine:
                    return BlockName.resource.getItemStack(ResourceBlock.machine);
                case AdvMachine:
                    return BlockName.resource.getItemStack(ResourceBlock.advanced_machine);
            }
        }
        final AdvEnergy component = this.getComponent(AdvEnergy.class);
        if (component != null) {
            if (component.getEnergy() != 0) {
                final NBTTagCompound nbt = ModUtils.nbt(drop);
                nbt.setDouble("energy", component.getEnergy());
            }
        }
        final ComponentBaseEnergy component1 = this.energy1;
        if (component1 != null) {
            if (component1.getEnergy() != 0) {
                final NBTTagCompound nbt = ModUtils.nbt(drop);
                nbt.setDouble("energy1", component1.getEnergy());
            }
        }
        final ComponentBaseEnergy component2 = this.exp;
        if (component2 != null) {
            if (component2.getEnergy() != 0) {
                final NBTTagCompound nbt = ModUtils.nbt(drop);
                nbt.setDouble("energy2", component2.getEnergy());
            }
        }
        final CoolComponent component3 = this.cold;
        if (component3 != null) {
            if (component3.getEnergy() != 0) {
                final NBTTagCompound nbt = ModUtils.nbt(drop);
                nbt.setDouble("energy3", component3.getEnergy());
            }
        }
        return drop;
    }

    public EnumTypeAudio getType() {
        return typeAudio;
    }

    public void setType(EnumTypeAudio type) {
        typeAudio = type;
    }

    @Override
    public boolean getEnable() {
        return this.sound;
    }

    public void initiate(int soundEvent) {
        if (this.getType() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }
        setType(valuesAudio[soundEvent % valuesAudio.length]);
        if (sound) {
            IC2.network.get(true).initiateTileEntityEvent(this, soundEvent, true);
        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        int x = nbttagcompound.getInteger("blockpos_x");
        int y = nbttagcompound.getInteger("blockpos_y");
        int z = nbttagcompound.getInteger("blockpos_z");
        this.min_y = nbttagcompound.getInteger("min_y");
        this.max_y = nbttagcompound.getInteger("max_y");
        this.blockpos = new BlockPos(x, y, z);
        this.work = nbttagcompound.getBoolean("work");
        this.vein_need = nbttagcompound.getBoolean("vein_need");
        this.need_work = nbttagcompound.getBoolean("need_work");
        this.sound = nbttagcompound.getBoolean("sound");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if (this.blockpos != null) {
            nbttagcompound.setInteger("blockpos_x", this.blockpos.getX());
            nbttagcompound.setInteger("blockpos_y", this.blockpos.getY());
            nbttagcompound.setInteger("blockpos_z", this.blockpos.getZ());
        }
        nbttagcompound.setBoolean("sound", this.sound);

        nbttagcompound.setInteger("min_y", this.min_y);
        nbttagcompound.setInteger("max_y", this.max_y);
        nbttagcompound.setBoolean("work", work);
        nbttagcompound.setBoolean("vein_need", vein_need);
        nbttagcompound.setBoolean("need_work", need_work);

        return nbttagcompound;
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        this.max_y = placer.getEntityWorld().provider.getHeight();
        this.vein = VeinSystem.system.getVein(this.getWorld().getChunkFromBlockCoords(this.pos).getPos());
        if (this.vein != null) {
            if (this.vein.getType() != Type.VEIN) {
                this.vein = null;
            }


        }
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        if (energy1 != 0) {
            this.energy.addEnergy(energy1);
        }
        final double energy2 = nbt.getDouble("energy1");
        if (energy1 != 0) {
            this.energy1.addEnergy(energy2);
        }
        final double energy3 = nbt.getDouble("energy2");
        if (energy1 != 0) {
            this.exp.addEnergy(energy3);
        }
        final double energy4 = nbt.getDouble("energy3");
        if (energy1 != 0) {
            this.cold.addEnergy(energy4);
        }
    }

    protected void onLoaded() {
        super.onLoaded();
        this.input.update();
        final Chunk chunk = this.getWorld().getChunkFromBlockCoords(this.pos);
        this.chunkx = chunk.x * 16;
        this.chunkz = chunk.z * 16;
        this.default_pos = new BlockPos(chunkx, this.min_y, chunkz);
        this.chunkx1 = this.chunkx;
        this.chunkz1 = this.chunkz;
        this.chunkx2 = this.chunkx + 15;
        this.chunkz2 = this.chunkz + 15;
        if (col != 1) {
            this.chunkx1 = chunkx - 16 * (col - 1);
            this.chunkz1 = chunkz - 16 * (col - 1);
        }

        this.vein = VeinSystem.system.getVein(chunk.getPos());
        if (this.vein != null) {
            if (this.vein.getType() != Type.VEIN) {
                this.vein = null;
            }


        }
    }

    public boolean list(EnumQuarryModules type, ItemStack stack1) {
        if (type == null) {
            return false;
        }
        if (type.type == EnumQuarryType.BLACKLIST) {

            return this.list.contains(stack1);


        } else if (type.type == EnumQuarryType.WHITELIST) {

            return !this.list.contains(stack1);

        }
        return false;
    }

    public List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        ItemStack colling = ItemStack.EMPTY;

        if (this.cold.upgrade) {
            colling = new ItemStack(IUItem.coolupgrade, 1, this.cold.meta);

        }
        if (!colling.isEmpty()) {

            ret.add(colling);
            this.cold.upgrade = false;
            this.cold.meta = 0;
        }
        return ret;
    }

    protected void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
        }

    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        if (!this.work || !this.need_work) {
            return;
        }
        if (this.cold.getEnergy() >= 100) {
            return;
        }
        if (this.player == null) {
            this.player = new FakePlayerSpawner(getWorld());
        }


        if (this.blockpos == null) {
            this.blockpos = new BlockPos(chunkx, this.min_y, chunkz);
        }

        if (vein_need) {
            if (vein != null) {
                if (vein.get()) {
                    if (this.energy.getEnergy() > this.energyconsume) {
                        if (this.vein.getCol() > 0 && this.vein.getType() == Type.VEIN) {
                            final ItemStack stack1 = new ItemStack(IUItem.heavyore, 1, vein.getMeta());
                            if (this.outputSlot.add(stack1)) {
                                this.energy.useEnergy(this.energyconsume);
                                this.vein.removeCol(1);
                                this.cold.addEnergy(0.05);
                                int chance = 0;
                                if (this.energy1.canUseEnergy(80)) {
                                    chance = this.getWorld().rand.nextInt(101);
                                    this.energy1.useEnergy(80);
                                }
                                if (chance > 85) {
                                    this.outputSlot.add(stack1);
                                }
                            }
                        }
                    }
                }
            }
        }

        ItemStack stack1 = new ItemStack(Items.ENCHANTED_BOOK);
        if (this.chance != 0) {
            stack1.addEnchantment(Enchantments.FORTUNE, this.chance);
        }
        for (int i = 0; i < this.speed; i++) {
            if (this.energy.canUseEnergy(this.energyconsume)) {
                this.energy.useEnergy(this.energyconsume);
                if (!this.getActive()) {
                    initiate(0);
                    setActive(true);

                }

                final IBlockState state = this
                        .getWorld()
                        .getBlockState(this.blockpos);
                if (!(state.getMaterial() == Material.AIR)) {
                    Block block = state.getBlock();
                    ItemStack stack = new ItemStack(block, 1,
                            block.getMetaFromState(state)
                    );
                    this.cold.addEnergy(0.005);
                    if (!stack.isEmpty()) {
                        if ((state.getMaterial() == Material.IRON || state
                                .getMaterial() == Material.ROCK) && OreDictionary.getOreIDs(stack).length > 0) {
                            int id = OreDictionary.getOreIDs(stack)[0];
                            String name = OreDictionary.getOreName(id);

                            if (name.startsWith("ore")) {

                                if (list(this.list_modules, stack)) {
                                    if (this.blockpos.getX() < this.chunkx2) {
                                        this.blockpos = blockpos.add(1, 0, 0);
                                    } else if (this.blockpos.getZ() < chunkz2) {
                                        this.blockpos = new BlockPos(chunkx1, this.blockpos.getY(), this.blockpos.getZ() + 1);
                                    } else if (this.blockpos.getY() < this.max_y) {
                                        this.blockpos = new BlockPos(chunkx1, this.blockpos.getY() + 1, chunkz1);
                                    } else {
                                        this.work = false;
                                    }
                                    return;
                                }
                                if (!(onBlockBreakEvent(world, world.getWorldInfo().getGameType(),
                                        player, this.blockpos
                                ) == -1)) {


                                    block.onBlockHarvested(this.world, this.blockpos, state, player);
                                    final List<ItemStack> drops = block.getDrops(world, this.blockpos, state, this.chance);
                                    int chance = 0;
                                    if (this.energy1.canUseEnergy(80)) {
                                        chance = this.getWorld().rand.nextInt(101);
                                        this.energy1.useEnergy(80);
                                    }
                                    boolean need = false;
                                    for (ItemStack item : drops) {
                                        if (!this.furnace) {

                                            if (this.mac_enabled) {
                                                ItemStack stack4;
                                                BaseMachineRecipe recipe = Recipes.recipes.getRecipeOutput(
                                                        "macerator",
                                                        false,
                                                        item
                                                );
                                                if (recipe != null) {
                                                    stack4 = recipe.getOutput().items.get(0);
                                                } else {
                                                    stack4 = item;
                                                }
                                                if (this.outputSlot.add(stack4)) {
                                                    need = true;
                                                    if (chance > 85) {
                                                        this.outputSlot.add(stack4);
                                                    }
                                                }
                                            } else if (this.comb_mac_enabled) {
                                                ItemStack stack4;
                                                BaseMachineRecipe recipe = Recipes.recipes.getRecipeOutput(
                                                        "comb_macerator",
                                                        false,
                                                        item
                                                );
                                                if (recipe != null) {
                                                    stack4 = recipe.getOutput().items.get(0);
                                                } else {
                                                    stack4 = item;
                                                }
                                                if (this.outputSlot.add(stack4)) {
                                                    need = true;
                                                    if (chance > 85) {
                                                        this.outputSlot.add(stack4);
                                                    }
                                                }
                                            } else {
                                                if (this.outputSlot.add(item)) {
                                                    need = true;
                                                    if (chance > 85) {
                                                        this.outputSlot.add(item);
                                                    }
                                                }
                                            }
                                        } else {
                                            ItemStack stack4 = item;
                                            ItemStack stack5 = ItemStack.EMPTY;
                                            if (this.mac_enabled) {
                                                BaseMachineRecipe recipe = Recipes.recipes.getRecipeOutput(
                                                        "macerator",
                                                        false,
                                                        item
                                                );
                                                if (recipe != null) {
                                                    stack5 = recipe.getOutput().items.get(0).copy();
                                                }


                                            } else if (this.comb_mac_enabled) {
                                                BaseMachineRecipe recipe = Recipes.recipes.getRecipeOutput(
                                                        "comb_macerator",
                                                        false,
                                                        item
                                                );
                                                if (recipe != null) {
                                                    stack5 = recipe.getOutput().items.get(0).copy();
                                                }


                                            }
                                            BaseMachineRecipe recipe;
                                            if (stack5.isEmpty()) {
                                                recipe = Recipes.recipes.getRecipeOutput("furnace", false,
                                                        stack4
                                                );
                                            } else {
                                                recipe = Recipes.recipes.getRecipeOutput("furnace", false,
                                                        stack5
                                                );
                                            }

                                            if (recipe != null) {
                                                stack4 = recipe.getOutput().items.get(0).copy();
                                            }

                                            if (!stack5.isEmpty()) {
                                                stack4.setCount(stack5.getCount());
                                            } else if (!stack4.isEmpty()) {
                                                stack4.setCount(item.getCount());
                                            }
                                            if (stack4.isEmpty()) {

                                                if (this.outputSlot.add(item)) {
                                                    need = true;
                                                    if (chance > 85) {
                                                        this.outputSlot.add(item);
                                                    }
                                                }
                                            } else {
                                                if (this.outputSlot.add(stack4)) {
                                                    need = true;
                                                    if (chance > 85) {
                                                        this.outputSlot.add(stack4);
                                                    }
                                                }
                                            }

                                        }

                                    }
                                    if (need) {
                                        block.removedByPlayer(state, world, this.blockpos, player, true);
                                        int exp = block.getExpDrop(state, world, this.blockpos, this.chance);
                                        this.exp.addEnergy(exp);
                                    }
                                }
                            }
                        }
                    }
                }
                if (this.blockpos.getX() < this.chunkx2) {
                    this.blockpos = blockpos.add(1, 0, 0);
                } else if (this.blockpos.getZ() < chunkz2) {
                    this.blockpos = new BlockPos(chunkx1, this.blockpos.getY(), this.blockpos.getZ() + 1);
                } else if (this.blockpos.getY() < this.max_y) {
                    this.blockpos = new BlockPos(chunkx1, this.blockpos.getY() + 1, chunkz1);
                } else {
                    this.work = false;
                }
            } else {
                if (this.getActive()) {
                    initiate(2);
                    setActive(false);
                }
            }

        }


        if (!this.outputSlot.isEmpty() && this.world.provider.getWorldTime() % 15 == 0) {
            ModUtils.tick(this.outputSlot, this);
        }
    }

    public ContainerBase<? extends TileEntityBaseQuarry> getGuiContainer(EntityPlayer player) {
        return new ContainerBaseQuarry(player, this);

    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {

        return new GuiBaseQuarry(new ContainerBaseQuarry(player, this));
    }

    public String getStartSoundFile() {
        return "Machines/quarry.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public void onNetworkEvent(int event) {
        if (this.audioSource == null && this.getStartSoundFile() != null) {
            this.audioSource = IUCore.audioManager.createSource(this, this.getStartSoundFile());
        }

        switch (event) {
            case 0:
                if (this.audioSource != null) {
                    this.audioSource.play();
                }
                break;
            case 1:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    if (this.getInterruptSoundFile() != null) {
                        IUCore.audioManager.playOnce(
                                this,
                                PositionSpec.Center,
                                this.getInterruptSoundFile(),
                                false,
                                IUCore.audioManager.getDefaultVolume()
                        );
                    }
                }
                break;
            case 2:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                }
            case 3:
        }

    }


    public void onGuiClosed(EntityPlayer player) {
    }


    public String getInventoryName() {

        return Localization.translate(name);
    }

    @Override
    public double getEnergy() {
        return 0;
    }

    @Override
    public boolean useEnergy(final double v) {
        return false;
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.ItemProducing
        );
    }

    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i1) {
        int i = i1 / 10;
        int k = ((i1 & 1) == 1) ? 10 : 1;
        switch (i) {
            case 0:


                this.min_y = Math.min(this.max_y, this.min_y + Math.min(k, this.max_y - this.min_y));

                if (this.blockpos.getY() < this.min_y) {
                    int temp = this.min_y - this.blockpos.getY();
                    this.blockpos = this.blockpos.add(0, temp, 0);
                }

                break;
            case 1:
                this.min_y = Math.max(0, this.min_y - Math.min(k, this.min_y));

                if (this.blockpos.getY() < this.min_y) {
                    int temp = this.min_y - this.blockpos.getY();
                    this.blockpos = this.blockpos.add(0, temp, 0);
                }

                break;
            case 2:
                this.max_y = Math.min(this.getWorld().provider.getHeight(), Math.min(
                        k,
                        this.getWorld().provider.getHeight() - this.max_y
                ));
                if (this.min_y > this.max_y) {
                    this.min_y = this.max_y;
                }

                if (this.blockpos.getY() > this.max_y) {
                    int temp = this.max_y - this.blockpos.getY();
                    this.blockpos = this.blockpos.add(0, temp, 0);
                }

                break;
            case 3:
                this.max_y = Math.max(0, this.max_y - Math.min(k, this.max_y));
                if (this.min_y > this.max_y) {
                    this.min_y = this.max_y;
                }
                if (this.blockpos.getY() > this.max_y) {
                    int temp = this.max_y - this.blockpos.getY();
                    this.blockpos = this.blockpos.add(0, temp, 0);
                }
                break;
            case 4:
                ExperienceUtils.addPlayerXP1(entityPlayer, (int) this.exp.getEnergy());
                this.exp.useEnergy(this.exp.getEnergy());
                break;
            case 5:
                vein_need = !vein_need;
                break;
            case 6:
                need_work = !need_work;
                break;
            case 7:
                sound = !sound;
                IC2.network.get(true).updateTileEntityField(this, "sound");

                if (!sound) {
                    if (this.getType() == EnumTypeAudio.ON) {
                        setType(EnumTypeAudio.OFF);
                        IC2.network.get(true).initiateTileEntityEvent(this, 2, true);

                    }
                }
                break;
        }

    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

}
