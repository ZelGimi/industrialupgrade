package com.simplequarries;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.gui.IType;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.api.vein.Type;
import com.denfop.api.vein.Vein;
import com.denfop.api.vein.VeinSystem;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockResource;
import com.denfop.componets.Energy;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.CoolComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.items.modules.EnumQuarryModules;
import com.denfop.items.modules.EnumQuarryType;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.FakePlayerSpawner;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ExperienceUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileBaseQuarry extends TileEntityInventory implements IAudioFixer,
        IUpdatableTileEvent,
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
    public double energyconsume;
    public Energy energy;
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
    int rotating = 0;

    public TileBaseQuarry(String name, double coef, int index) {
        this.name = name;
        this.energyconsume = 450 * coef;
        this.energy = this.addComponent(Energy.asBasicSink(this, 5E7D, 14));
        this.energy1 = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 200000, 14));
        this.cold = this.addComponent(CoolComponent.asBasicSink(this, 100));

        this.outputSlot = new InvSlotOutput(this, 24);
        this.work = true;
        this.index = index;
        this.speed = Math.pow(2, index - 1);
        this.input = new InvSlotBaseQuarry(this, Math.min(4, index));
        this.constenergyconsume = 450 * coef;
        this.min_y = 0;
        this.max_y = 256;
        this.chance = 0;
        this.col = 1;
        this.furnace = false;
        this.list_modules = null;
        this.consume = this.energyconsume;
        this.exp = this.addComponent(ComponentBaseEnergy.asBasicSource(EnergyType.EXPERIENCE, this, 5000, 14));

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

        return event.isCanceled() ? -1 : event.getExpToDrop();
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.multimachine.info"));
            tooltip.add(Localization.translate("iu.simplyquarries.info") + index);
            tooltip.add(Localization.translate("iu.simplyquarries.info1") + (int) this.constenergyconsume + " EF/t");
            tooltip.add(Localization.translate("iu.simplyquarries.info2") + (int) this.speed + " " + Localization.translate("iu" +
                    ".simplyquarries.info3"));
        }


        if (this.getComp(Energy.class) != null) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }

        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        if (energy1 != 0) {
            tooltip.add(Localization.translate("iu.item.tooltip.Store") + " " + ModUtils.getString(energy1) + "/" + ModUtils.getString(
                    energy.getCapacity())
                    + " EF");
        }
        final double energy2 = nbt.getDouble("energy1");
        if (energy2 != 0) {
            tooltip.add(Localization.translate("iu.item.tooltip.Store") + " " + ModUtils.getString(energy2) + "/" + ModUtils.getString(
                    this.energy1.getCapacity())
                    + " QE");
        }
        final double energy3 = nbt.getDouble("energy2");
        if (energy3 != 0) {
            tooltip.add(Localization.translate("iu.item.tooltip.Store") + " " + ModUtils.getString(energy3) + "/" + ModUtils.getString(
                    exp.getCapacity())
                    + " EXP");
        }
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        if (!wrench) {
            switch (this.teBlock.getDefaultDrop()) {
                case Self:
                default:
                    final Energy component = this.energy;
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
                    return new ItemStack(IUItem.basemachine2, 1, 78);
                case Machine:
                    return IUItem.blockResource.getItemStack(BlockResource.Type.machine);
                case AdvMachine:
                    return IUItem.blockResource.getItemStack(BlockResource.Type.advanced_machine);
            }
        }
        final Energy component = this.getComp(Energy.class);
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

    public EnumTypeAudio getTypeAudio() {
        return typeAudio;
    }

    public void setType(EnumTypeAudio type) {
        typeAudio = type;
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

    @Override
    public boolean getEnable() {
        return this.sound;
    }

    public void initiate(int soundEvent) {
        if (this.getTypeAudio() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }
        setType(valuesAudio[soundEvent % valuesAudio.length]);
        if (sound) {
            if (soundEvent == 0) {
                this.getWorld().playSound(null, this.pos, getSound(), SoundCategory.BLOCKS, 1F, 1);
            } else if (soundEvent == 1) {
                new PacketStopSound(getWorld(), this.pos);
                this.getWorld().playSound(null, this.pos, EnumSound.InterruptOne.getSoundEvent(), SoundCategory.BLOCKS, 1F, 1);
            } else {
                new PacketStopSound(getWorld(), this.pos);
            }
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
        this.rotating = nbttagcompound.getInteger("rotating");

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
        nbttagcompound.setInteger("rotating", this.rotating);
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
        if (this.vein != VeinSystem.system.getEMPTY()) {
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


    public void onLoaded() {
        super.onLoaded();
        this.input.update();
        final Chunk chunk = this.getWorld().getChunkFromBlockCoords(this.pos);
        this.chunkx = chunk.x * 16;
        this.chunkz = chunk.z * 16;
        this.default_pos = new BlockPos(chunkx, this.min_y, chunkz);
        switch (rotating) {
            case 0:
                this.chunkx1 = this.chunkx;
                this.chunkz1 = this.chunkz;
                this.chunkx2 = this.chunkx + 15;
                this.chunkz2 = this.chunkz + 15;
                if (col != 1) {
                    this.chunkx1 = chunkx - 16 * (col - 1);
                    this.chunkz1 = chunkz - 16 * (col - 1);
                }
                this.blockpos = new BlockPos(chunkx1, this.min_y, chunkz1);
                break;
            case 1:
                this.chunkx1 = this.chunkx;
                this.chunkz1 = this.chunkz;
                this.chunkx2 = this.chunkx + 15;
                this.chunkz2 = this.chunkz + 15;
                if (col != 1) {
                    this.chunkx2 = chunkx + 16 * (col - 1);
                    this.chunkz2 = chunkz + 16 * (col - 1);
                }
                this.blockpos = new BlockPos(chunkx1, this.min_y, chunkz1);
                break;
            case 2:
                this.chunkx1 = this.chunkx;
                this.chunkz1 = this.chunkz;
                this.chunkx2 = this.chunkx + 15;
                this.chunkz2 = this.chunkz + 15;
                if (col != 1) {
                    this.chunkx1 = chunkx - 16 * (col - 1);
                    this.chunkz2 = chunkz + 16 * (col - 1);
                }
                this.blockpos = new BlockPos(chunkx1, this.min_y, chunkz1);
                break;
            case 3:
                this.chunkx1 = this.chunkx;
                this.chunkz1 = this.chunkz;
                this.chunkx2 = this.chunkx + 15;
                this.chunkz2 = this.chunkz + 15;
                if (col != 1) {
                    this.chunkx2 = chunkx + 16 * (col - 1);
                    this.chunkz1 = chunkz - 16 * (col - 1);
                }
                this.blockpos = new BlockPos(chunkx1, this.min_y, chunkz1);
                break;
        }

        this.vein = VeinSystem.system.getVein(chunk.getPos());
        if (this.vein != VeinSystem.system.getEMPTY()) {
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


    public void updateEntityServer() {
        super.updateEntityServer();
        if (!this.work || !this.need_work) {
            if (this.getActive()) {
                this.setActive(false);
            }
            return;
        }
        if (this.cold.getEnergy() >= 100) {
            return;
        }
        if (this.player == null) {
            this.player = new FakePlayerSpawner(getWorld());
        }


        if (this.blockpos == null) {
            this.blockpos = new BlockPos(chunkx1, this.min_y, chunkz1);
        }

        if (vein_need) {
            if (vein != null) {
                if (vein.get()) {
                    if (this.energy.getEnergy() > this.energyconsume) {
                        if (this.vein.getCol() > 0 && this.vein.getType() == Type.VEIN) {
                            final ItemStack stack1;
                            if (vein.isOldMineral())
                                stack1   = new ItemStack(IUItem.heavyore, 1, vein.getMeta());
                            else
                                stack1   = new ItemStack(IUItem.mineral, 1, vein.getMeta());

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

    public ContainerBaseQuarry getGuiContainer(EntityPlayer player) {
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


    public String getInventoryName() {

        return Localization.translate(name);
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.ItemInput
        );
    }

    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i1) {
        int i = (int) (i1 / 10);
        int k = (((int) i1 & 1) == 1) ? 10 : 1;
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
                this.max_y = Math.min(this.getWorld().provider.getHeight(), k + this.max_y);
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
                new PacketUpdateFieldTile(this, "sound", this.sound);

                if (!sound) {
                    if (this.getTypeAudio() == EnumTypeAudio.ON) {
                        setType(EnumTypeAudio.OFF);
                        initiate(2);
                    }
                }
                break;
            case 8:
                rotating = rotating + 1;
                if (rotating > 3) {
                    rotating = 0;
                }
                switch (rotating) {
                    case 0:
                        this.chunkx1 = this.chunkx;
                        this.chunkz1 = this.chunkz;
                        this.chunkx2 = this.chunkx + 15;
                        this.chunkz2 = this.chunkz + 15;
                        if (col != 1) {
                            this.chunkx1 = chunkx - 16 * (col - 1);
                            this.chunkz1 = chunkz - 16 * (col - 1);
                        }
                        this.blockpos = new BlockPos(chunkx1, this.min_y, chunkz1);
                        break;
                    case 1:
                        this.chunkx1 = this.chunkx;
                        this.chunkz1 = this.chunkz;
                        this.chunkx2 = this.chunkx + 15;
                        this.chunkz2 = this.chunkz + 15;
                        if (col != 1) {
                            this.chunkx2 = chunkx + 16 * (col - 1) + 15;
                            this.chunkz2 = chunkz + 16 * (col - 1) + 15;
                        }
                        this.blockpos = new BlockPos(chunkx1, this.min_y, chunkz1);
                        break;
                    case 2:
                        this.chunkx1 = this.chunkx;
                        this.chunkz1 = this.chunkz;
                        this.chunkx2 = this.chunkx + 15;
                        this.chunkz2 = this.chunkz + 15;
                        if (col != 1) {
                            this.chunkx1 = chunkx - 16 * (col - 1);
                            this.chunkz2 = chunkz + 16 * (col - 1)+ 15;
                        }
                        this.blockpos = new BlockPos(chunkx1, this.min_y, chunkz1);
                        break;
                    case 3:
                        this.chunkx1 = this.chunkx;
                        this.chunkz1 = this.chunkz;
                        this.chunkx2 = this.chunkx + 15;
                        this.chunkz2 = this.chunkz + 15;
                        if (col != 1) {
                            this.chunkx2 = chunkx + 16 * (col - 1)+ 15;
                            this.chunkz1 = chunkz - 16 * (col - 1);
                        }
                        this.blockpos = new BlockPos(chunkx1, this.min_y, chunkz1);
                        break;
                }

                break;
        }

    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("sound")) {
            try {
                this.sound = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            energyconsume = (double) DecoderHandler.decode(customPacketBuffer);
            blockpos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            col = (int) DecoderHandler.decode(customPacketBuffer);
            min_y = (int) DecoderHandler.decode(customPacketBuffer);
            max_y = (int) DecoderHandler.decode(customPacketBuffer);
            vein_need = (boolean) DecoderHandler.decode(customPacketBuffer);
            need_work = (boolean) DecoderHandler.decode(customPacketBuffer);
            default_pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            chunkx1 = (int) DecoderHandler.decode(customPacketBuffer);
            chunkz1 = (int) DecoderHandler.decode(customPacketBuffer);
            chunkx2 = (int) DecoderHandler.decode(customPacketBuffer);
            chunkz2 = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, energyconsume);
            EncoderHandler.encode(packet, blockpos);
            EncoderHandler.encode(packet, col);
            EncoderHandler.encode(packet, min_y);
            EncoderHandler.encode(packet, max_y);
            EncoderHandler.encode(packet, vein_need);
            EncoderHandler.encode(packet, need_work);
            EncoderHandler.encode(packet, default_pos);
            EncoderHandler.encode(packet, chunkx1);
            EncoderHandler.encode(packet, chunkz1);
            EncoderHandler.encode(packet, chunkx2);
            EncoderHandler.encode(packet, chunkz2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

}
