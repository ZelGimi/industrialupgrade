package com.denfop.items.energy;


import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.audio.PositionSpec;
import com.denfop.proxy.CommonProxy;
import com.denfop.tiles.base.TileEntityMultiMachine;
import com.denfop.tiles.panels.entity.TileEntitySolarPanel;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.block.TileEntityBarrel;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.init.MainConfig;
import ic2.core.item.ElectricItemManager;
import ic2.core.item.tool.ItemTreetap;
import ic2.core.ref.BlockName;
import ic2.core.util.ConfigUtil;
import ic2.core.util.LogCategory;
import ic2.core.util.StackUtil;
import ic2.core.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDirt.DirtType;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.Locale;

public class ItemGraviTool extends ItemTool implements IElectricItem, IModelRegister {

    protected static final double ROTATE = 50.0D;
    protected static final double HOE = 50.0D;
    protected static final double TAP = 50.0D;
    protected static final double SCREW = 500.0D;
    protected final String name;

    public ItemGraviTool(String name) {
        super(ToolMaterial.IRON, Collections.emptySet());
        this.setMaxDamage(27);
        setCreativeTab(IUCore.EnergyTab);
        this.efficiency = 16.0F;
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
        this.name = name;
    }


    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            GraviToolMode mode;
            mode = ItemGraviTool.readToolMode(stack);

            return mode.model;
        });

        for (ItemGraviTool.GraviToolMode mode : ItemGraviTool.GraviToolMode.VALUES) {
            ModelBakery.registerItemVariants(this, mode.model);
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    public static boolean hasToolMode(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("toolMode", 4);
    }

    public static GraviToolMode readToolMode(ItemStack stack) {
        return GraviToolMode.getFromID(StackUtil.getOrCreateNbtData(stack).getInteger("toolMode"));
    }

    public static GraviToolMode readNextToolMode(ItemStack stack) {
        return GraviToolMode.getFromID(StackUtil.getOrCreateNbtData(stack).getInteger("toolMode") + 1);
    }

    public static void saveToolMode(ItemStack stack, ItemGraviTool.GraviToolMode mode) {
        StackUtil.getOrCreateNbtData(stack).setInteger("toolMode", mode.ordinal());
    }


    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return hasToolMode(stack) ? Localization.translate(
                "gravisuite.graviTool.set",
                Localization.translate(this.getUnlocalizedName(stack)),
                Localization.translate(readToolMode(stack).translationName)
        ) : Localization.translate(this.getUnlocalizedName(stack));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (IC2.keyboard.isModeSwitchKeyDown(player)) {
            ItemStack stack = StackUtil.get(player, hand);
            if (world.isRemote) {
                IUCore.audioManager.playOnce(
                        player,
                        com.denfop.audio.PositionSpec.Hand,
                        "Tools/toolchange.ogg",
                        true,
                        IUCore.audioManager.getDefaultVolume()
                );
            } else {
                ItemGraviTool.GraviToolMode mode = readNextToolMode(stack);
                saveToolMode(stack, mode);
                CommonProxy.sendPlayerMessage(player, mode.colour + Localization.translate(mode.translationName));

            }

            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public EnumActionResult onItemUseFirst(
            EntityPlayer player,
            World world,
            BlockPos pos,
            EnumFacing side,
            float hitX,
            float hitY,
            float hitZ,
            EnumHand hand
    ) {
        ItemStack stack = StackUtil.get(player, hand);
        switch (readToolMode(stack)) {
            case WRENCH:
                return this.onWrenchUse(stack, player, world, pos, side) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
            case SCREWDRIVER:
                return this.onScrewdriverUse(stack, player, world, pos) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;


            default:
                return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
        }
    }

    @Override
    public EnumActionResult onItemUse(
            EntityPlayer player,
            World world,
            BlockPos pos,
            EnumHand hand,
            EnumFacing facing,
            float hitX,
            float hitY,
            float hitZ
    ) {
        ItemStack stack = StackUtil.get(player, hand);
        switch (readToolMode(stack)) {
            case HOE:
                return this.onHoeUse(stack, player, world, pos, facing) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
            case TREETAP:
                return this.onTreeTapUse(stack, player, world, pos, facing) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
            case PURIFIER:
                TileEntity tile = world.getTileEntity(pos);
                if (!(tile instanceof TileEntitySolarPanel) && !(tile instanceof TileEntityMultiMachine)) {
                    return EnumActionResult.FAIL;
                }
                if(tile instanceof TileEntitySolarPanel){
                double energy = 10000;
                if (((TileEntitySolarPanel) tile).time > 0) {
                    energy = (double) 10000 / (double) (((TileEntitySolarPanel) tile).time / 20);
                }
                if (ElectricItem.manager.canUse(stack, energy)) {
                    ((TileEntitySolarPanel) tile).time = 28800;
                    ((TileEntitySolarPanel) tile).time1 = 14400;
                    ((TileEntitySolarPanel) tile).time2 = 14400;
                    ElectricItem.manager.use(stack, energy, player);
                    return EnumActionResult.SUCCESS;
                }
                //
        } else {
                    if(!ElectricItem.manager.canUse(stack, 500))
                        return EnumActionResult.PASS;
                    TileEntityMultiMachine base = (TileEntityMultiMachine) tile;
                    ItemStack stack_rf = null;
                    ItemStack stack_quickly = null;
                    ItemStack stack_modulesize = null;
                    ItemStack panel = null;
                    if( base.rf)
                        stack_rf = new ItemStack(IUItem.module7,1,4);
                    if( base.quickly)
                        stack_quickly = new ItemStack(IUItem.module_quickly);
                    if( base.modulesize)
                        stack_modulesize = new ItemStack(IUItem.module_stack);
                    if( base.solartype != null)
                        panel = new ItemStack(IUItem.module6,1,base.solartype.meta);
                    if(stack_rf != null || stack_quickly != null || stack_modulesize != null || panel != null){
                        final EntityItem item = new EntityItem(world);

                        if(stack_rf != null) {
                            item.setItem(stack_rf);
                            base.module=0;
                            base.rf = false;
                        }
                       else if(stack_quickly != null) {
                            item.setItem(stack_quickly);
                            base.module=0;
                            base.quickly = false;
                        }
                        else  if(stack_modulesize != null) {
                            item.setItem(stack_modulesize);
                            base.modulesize = false;
                            base.module=0;
                        }
                        else     if(panel != null) {
                            item.setItem(panel);
                            base.solartype = null;
                        }
                        if (!player.getEntityWorld().isRemote) {
                            item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                            item.setPickupDelay(0);
                            world.spawnEntity(item);
                            ElectricItem.manager.use(stack, 500,null);
                            return EnumActionResult.SUCCESS;
                        }

                    }
                }
                //
                return EnumActionResult.FAIL;
            default:
                return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
        }
    }

    protected boolean onHoeUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
        if (player.canPlayerEdit(pos.offset(side), side, stack) && hasNecessaryPower(stack, HOE, player)) {
            UseHoeEvent event = new UseHoeEvent(player, stack, world, pos);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return false;
            }
            if (event.getResult() == Result.ALLOW) {
                return checkNecessaryPower(stack, HOE, player, true);
            }
            IBlockState state = Util.getBlockState(world, pos);
            Block block = state.getBlock();
            if (side != EnumFacing.DOWN && world.isAirBlock(pos.up())) {
                if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
                    return this.setHoedBlock(stack, player, world, pos, Blocks.FARMLAND.getDefaultState());
                }

                if (block == Blocks.DIRT) {
                    switch (state.getValue(BlockDirt.VARIANT)) {
                        case DIRT:
                            return this.setHoedBlock(stack, player, world, pos, Blocks.FARMLAND.getDefaultState());
                        case COARSE_DIRT:
                            return this.setHoedBlock(
                                    stack,
                                    player,
                                    world,
                                    pos,
                                    Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, DirtType.DIRT)
                            );
                    }
                }
            }

            return false;
        }
        return false;
    }

    protected boolean setHoedBlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos, IBlockState state) {
        if (checkNecessaryPower(stack, HOE, player, true)) {
            world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!world.isRemote) {
                world.setBlockState(pos, state, 11);
            }

            return true;
        }
        return false;
    }

    protected boolean onTreeTapUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
        IBlockState state = Util.getBlockState(world, pos);
        TileEntity te;
        if (side.getAxis() != Axis.Y && (te = world.getTileEntity(pos)) instanceof TileEntityBarrel) {
            TileEntityBarrel barrel = (TileEntityBarrel) te;
            if (!barrel.getActive()) {
                if (checkNecessaryPower(stack, TAP, player, true)) {
                    if (!world.isRemote) {
                        barrel.setActive(true);
                        barrel.onPlaced(stack, null, side.getOpposite());
                    }

                    return true;
                }
                return false;
            }
            return false;
        }
        return state.getBlock() == BlockName.rubber_wood.getInstance() && hasNecessaryPower(
                stack,
                TAP,
                player
        ) && ItemTreetap.attemptExtract(player, world, pos, side, state, null) && checkNecessaryPower(stack, TAP, player);
    }

    protected boolean onWrenchUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
        IBlockState state = Util.getBlockState(world, pos);
        Block block = state.getBlock();
        if (block.isAir(state, world, pos)) {
            return false;
        }
        if (block instanceof IWrenchable) {
            IWrenchable wrenchable = (IWrenchable) block;
            EnumFacing current = wrenchable.getFacing(world, pos);
            EnumFacing newFacing;
            if (!IC2.keyboard.isAltKeyDown(player)) {
                if (player.isSneaking()) {
                    newFacing = side.getOpposite();
                } else {
                    newFacing = side;
                }
            } else {
                Axis axis = side.getAxis();
                if ((player.isSneaking() || side.getAxisDirection() != AxisDirection.POSITIVE) && (!player.isSneaking() || side.getAxisDirection() != AxisDirection.NEGATIVE)) {
                    newFacing = current.rotateAround(axis).rotateAround(axis).rotateAround(axis);
                } else {
                    newFacing = current.rotateAround(axis);
                }
            }

            if (current != newFacing) {
                if (!hasNecessaryPower(stack, ROTATE, player)) {
                    return false;
                }

                if (wrenchable.setFacing(world, pos, newFacing, player)) {
                    return checkNecessaryPower(stack, ROTATE, player);
                }
            }

            if (wrenchable.wrenchCanRemove(world, pos, player)) {
                if (!hasNecessaryPower(stack, ROTATE, player)) {
                    return false;
                }

                if (!world.isRemote) {
                    TileEntity te = world.getTileEntity(pos);
                    if (ConfigUtil.getBool(MainConfig.get(), "protection/wrenchLogging")) {
                        IC2.log.info(
                                LogCategory.PlayerActivity,
                                "Player %s used a wrench to remove the %s (%s) at %s.",
                                player.getGameProfile().getName() + "/" + player.getGameProfile().getId(),
                                state,
                                te != null ? te.getClass().getSimpleName().replace("TileEntity", "") : "no te",
                                Util.formatPosition(world, pos)
                        );
                    }

                    int experience;
                    if (player instanceof EntityPlayerMP) {
                        experience = ForgeHooks.onBlockBreakEvent(
                                world,
                                ((EntityPlayerMP) player).interactionManager.getGameType(),
                                (EntityPlayerMP) player,
                                pos
                        );
                        if (experience < 0) {
                            return false;
                        }
                    } else {
                        experience = 0;
                    }


                    block.onBlockHarvested(world, pos, state, player);

                    if (!block.removedByPlayer(state, world, pos, player, true)) {
                        return false;
                    }

                    block.onBlockDestroyedByPlayer(world, pos, state);

                    for (ItemStack drop : wrenchable.getWrenchDrops(world, pos, state, te, player, 0)) {
                        StackUtil.dropAsEntity(world, pos, drop);
                    }

                    if (!player.capabilities.isCreativeMode && experience > 0) {
                        block.dropXpOnBlockBreak(world, pos, experience);
                    }
                }

                return checkNecessaryPower(stack, ROTATE, player);
            }
        }

        return false;
    }

    protected boolean onScrewdriverUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos) {
        IBlockState state = Util.getBlockState(world, pos);
        Block block = state.getBlock();
        if (!block.isAir(state, world, pos) && block instanceof BlockHorizontal && checkNecessaryPower(stack, 500.0D, player)) {
            EnumFacing facing = state.getValue(BlockHorizontal.FACING);
            if (player.isSneaking()) {
                facing = facing.rotateYCCW();
            } else {
                facing = facing.rotateY();
            }

            world.setBlockState(pos, state.withProperty(BlockHorizontal.FACING, facing));
            return true;
        }
        return false;
    }

    public static boolean hasNecessaryPower(ItemStack stack, double usage, EntityPlayer player) {
        ElectricItem.manager.chargeFromArmor(stack, player);
        return Util.isSimilar(ElectricItem.manager.discharge(stack, usage, Integer.MAX_VALUE, true, false, true), usage);
    }

    protected static boolean checkNecessaryPower(ItemStack stack, double usage, EntityPlayer player) {
        return checkNecessaryPower(stack, usage, player, false);
    }

    protected static boolean checkNecessaryPower(ItemStack stack, double usage, EntityPlayer player, boolean supressSound) {
        if (ElectricItem.manager.use(stack, usage, player)) {
            if (!supressSound && player.world.isRemote) {
                IUCore.audioManager.playOnce(
                        player,
                        PositionSpec.Hand,
                        "Tools/wrench.ogg",
                        true,
                        IUCore.audioManager.getDefaultVolume()
                );
            }

            return true;
        }


        CommonProxy.sendPlayerMessage(player, Localization.translate("message.text.noenergy"));

        return false;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return false;
    }

    @Override
    public boolean onBlockDestroyed(
            ItemStack stack,
            World world,
            IBlockState state,
            BlockPos pos,
            EntityLivingBase entityLiving
    ) {
        return true;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    public boolean isRepairable() {
        return false;
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
        return HashMultimap.create();
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ElectricItemManager.addChargeVariants(this, items);
        }

    }

    @Override
    public boolean canProvideEnergy(ItemStack stack) {
        return false;
    }

    @Override
    public double getMaxCharge(ItemStack stack) {
        return 300000.0D;
    }

    @Override
    public int getTier(ItemStack stack) {
        return 2;
    }

    @Override
    public double getTransferLimit(ItemStack stack) {
        return 10000.0D;
    }

    public enum GraviToolMode {
        HOE(TextFormatting.GOLD),
        TREETAP(TextFormatting.LIGHT_PURPLE),
        WRENCH(TextFormatting.AQUA),
        SCREWDRIVER(TextFormatting.YELLOW),
        PURIFIER(TextFormatting.DARK_AQUA);
        private final ModelResourceLocation model =
                new ModelResourceLocation(Constants.MOD_ID + ":" + "gravitool/gravitool".toLowerCase(Locale.ENGLISH) + this
                        .name()
                        .toLowerCase(Locale.ENGLISH), null);
        public final String translationName = "iu.graviTool.snap." + this.name().toLowerCase(Locale.ENGLISH);
        public final TextFormatting colour;
        private static final ItemGraviTool.GraviToolMode[] VALUES = values();

        GraviToolMode(TextFormatting colour) {
            this.colour = colour;
        }

        public static ItemGraviTool.GraviToolMode getFromID(int ID) {
            return VALUES[ID % VALUES.length];
        }
    }

}
