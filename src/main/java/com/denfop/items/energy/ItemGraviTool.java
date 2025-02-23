package com.denfop.items.energy;


import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.tile.IWrenchable;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockRubWood;
import com.denfop.componets.AbstractComponent;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.proxy.CommonProxy;
import com.denfop.register.Register;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.base.TileMultiMachine;
import com.denfop.utils.ElectricItemManager;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDirt.DirtType;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
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
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ItemGraviTool extends ItemTool implements IEnergyItem, IModelRegister, IUpgradeItem {

    protected static final double ROTATE = 50.0D;
    protected static final double HOE = 50.0D;
    protected static final double TAP = 50.0D;
    protected final String name;

    public ItemGraviTool(String name) {
        super(ToolMaterial.IRON, Collections.emptySet());
        this.setMaxDamage(27);
        setCreativeTab(IUCore.EnergyTab);
        this.efficiency = 16.0F;
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
        this.name = name;
        UpgradeSystem.system.addRecipe(this, EnumUpgrades.GRAVITOOL.list);
    }

    public static GraviToolMode readToolMode(ItemStack stack) {
        return GraviToolMode.getFromID(ModUtils.nbt(stack).getInteger("toolMode"));
    }

    public static GraviToolMode readNextToolMode(ItemStack stack) {
        return GraviToolMode.getFromID(ModUtils.nbt(stack).getInteger("toolMode") + 1);
    }

    public static void saveToolMode(ItemStack stack, ItemGraviTool.GraviToolMode mode) {
        ModUtils.nbt(stack).setInteger("toolMode", mode.ordinal());
    }

    public static boolean hasNecessaryPower(ItemStack stack, double usage, EntityPlayer player) {
        double coef = 1D - (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, stack).number * 0.25D : 0);

        return ElectricItem.manager.canUse(stack, usage * coef);
    }

    protected static boolean checkNecessaryPower(ItemStack stack, double usage, EntityPlayer player) {
        return checkNecessaryPower(stack, usage, player, false);
    }

    protected static boolean checkNecessaryPower(ItemStack stack, double usage, EntityPlayer player, boolean supressSound) {
        double coef = 1D - (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, stack).number * 0.25D : 0);

        if (ElectricItem.manager.use(stack, usage * coef, player)) {
            if (!supressSound && player.world.isRemote) {
                player.playSound(EnumSound.wrench.getSoundEvent(), 1F, 1);

            }

            return true;
        } else {
            CommonProxy.sendPlayerMessage(player, Localization.translate("message.text.noenergy"));
        }

        return false;
    }

    public static boolean hasToolMode(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return false;
        }
        assert stack.getTagCompound() != null;
        return stack.getTagCompound().hasKey("toolMode", 4);
    }

    private static void ejectResin(World world, BlockPos pos, EnumFacing side, int quantity) {
        double ejectX = (double) pos.getX() + 0.5D + (double) side.getFrontOffsetX() * 0.3D;
        double ejectY = (double) pos.getY() + 0.5D + (double) side.getFrontOffsetY() * 0.3D;
        double ejectZ = (double) pos.getZ() + 0.5D + (double) side.getFrontOffsetZ() * 0.3D;


        EntityItem entityitem = new EntityItem(
                world,
                ejectX,
                ejectY,
                ejectZ,
                IUItem.latex.copy()
        );
        entityitem.setDefaultPickupDelay();
        entityitem.getItem().setCount(quantity);
        world.spawnEntity(entityitem);


    }

    public static boolean attemptExtract(
            EntityPlayer player,
            World world,
            BlockPos pos,
            EnumFacing side,
            IBlockState state,
            List<ItemStack> stacks,
            final ItemStack stack
    ) {
        assert state.getBlock() == IUItem.rubWood;

        BlockRubWood.RubberWoodState rwState = state.getValue(BlockRubWood.stateProperty);
        boolean max = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.LATEX, stack);
        if (!rwState.isPlain() && rwState.facing == side) {
            if (rwState.wet) {
                if (!world.isRemote) {
                    world.setBlockState(pos, state.withProperty(BlockRubWood.stateProperty, rwState.getDry()));
                    if (stacks != null) {
                        stacks.add(ModUtils.setSize(
                                IUItem.latex,
                                world.rand.nextInt(3) + 1
                        ));
                    } else {
                        ejectResin(world, pos, side, !max ? world.rand.nextInt(3) + 1 : 3);
                    }

                }

                if (world.isRemote && player != null) {
                    player.playSound(EnumSound.Treetap.getSoundEvent(), 1F, 1);


                }

                return true;
            } else {
                if (!world.isRemote && world.rand.nextInt(5) == 0) {
                    world.setBlockState(
                            pos,
                            state.withProperty(BlockRubWood.stateProperty, BlockRubWood.RubberWoodState.plain_y)
                    );
                }

                if (world.rand.nextInt(5) == 0) {
                    if (!world.isRemote) {
                        ejectResin(world, pos, side, 1);
                        if (stacks != null) {
                            stacks.add(IUItem.latex);
                        } else {
                            ejectResin(world, pos, side, 1);
                        }
                    }

                    if (world.isRemote && player != null) {
                        player.playSound(EnumSound.Treetap.getSoundEvent(), 1F, 1);

                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public List<EnumInfoUpgradeModules> getUpgradeModules() {
        return EnumUpgrades.GRAVITOOL.list;
    }

    public boolean showDurabilityBar(final ItemStack stack) {
        return true;
    }

    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return ModUtils.convertRGBcolorToInt(33, 91, 199);
    }

    public double getDurabilityForDisplay(ItemStack stack) {
        return Math.min(
                Math.max(
                        1 - ElectricItem.manager.getCharge(stack) / ElectricItem.manager.getMaxCharge(stack),
                        0.0
                ),
                1.0
        );
    }

    public boolean isBookEnchantable(@Nonnull ItemStack stack, @Nonnull ItemStack book) {
        return false;
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

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return hasToolMode(stack) ? Localization.translate(
                "gravisuite.graviTool.set",
                Localization.translate(this.getUnlocalizedName(stack)),
                Localization.translate(readToolMode(stack).translationName)
        ) : Localization.translate(this.getUnlocalizedName(stack));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(
            @Nonnull final ItemStack par1ItemStack,
            @Nullable final World worldIn,
            @Nonnull final List<String> par3List,
            @Nonnull final ITooltipFlag flagIn
    ) {
        ItemGraviTool.GraviToolMode mode = readToolMode(par1ItemStack);
        par3List.add(Localization.translate("message.text.mode") + ": " + mode.colour + Localization.translate(mode.translationName));
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            par3List.add(Localization.translate("press.lshift"));
        }


        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            par3List.add(Localization.translate("iu.changemode_key") + Keyboard.getKeyName(Math.abs(KeyboardClient.changemode.getKeyCode())) + Localization.translate(
                    "iu.changemode_rcm"));

        }

        super.addInformation(par1ItemStack, worldIn, par3List, flagIn);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        if (IUCore.keyboard.isChangeKeyDown(player)) {
            ItemStack stack = ModUtils.get(player, hand);
            if (world.isRemote) {
                player.playSound(EnumSound.toolchange.getSoundEvent(), 1F, 1);
            } else {
                ItemGraviTool.GraviToolMode mode = readNextToolMode(stack);
                saveToolMode(stack, mode);
                CommonProxy.sendPlayerMessage(player, mode.colour + Localization.translate(mode.translationName));

            }

            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUseFirst(
            @Nonnull EntityPlayer player,
            @Nonnull World world,
            @Nonnull BlockPos pos,
            @Nonnull EnumFacing side,
            float hitX,
            float hitY,
            float hitZ,
            @Nonnull EnumHand hand
    ) {
        ItemStack stack = ModUtils.get(player, hand);
        switch (readToolMode(stack)) {
            case WRENCH:
                return this.onWrenchUse(stack, player, world, pos, side) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
            case SCREWDRIVER:
                return this.onScrewdriverUse(stack, player, world, pos) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;


            default:
                return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
        }
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(
            @Nonnull EntityPlayer player,
            @Nonnull World world,
            @Nonnull BlockPos pos,
            @Nonnull EnumHand hand,
            @Nonnull EnumFacing facing,
            float hitX,
            float hitY,
            float hitZ
    ) {
        ItemStack stack4 = ModUtils.get(player, hand);
        switch (readToolMode(stack4)) {
            case HOE:
                return this.onHoeUse(stack4, player, world, pos, facing) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
            case TREETAP:
                return this.onTreeTapUse(stack4, player, world, pos, facing) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
            case PURIFIER:
                TileEntity tile = world.getTileEntity(pos);
                ItemStack itemstack = player.getHeldItem(hand);
                if (!(tile instanceof TileEntityInventory) && !(tile instanceof IManufacturerBlock)) {
                    return EnumActionResult.PASS;
                }
                double coef = 1D - (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, player.getHeldItem(hand))
                        ?
                        UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, player.getHeldItem(hand)).number * 0.25D
                        : 0);

                if (tile instanceof TileEntityInventory) {
                    TileEntityInventory base = (TileEntityInventory) tile;
                    double energy = 10000;
                    if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PURIFIER, itemstack)) {
                        energy = 0;
                    }
                    if (!base.canEntityDestroy(player)) {
                        return EnumActionResult.FAIL;
                    }
                    for (AbstractComponent component : base.getComponentList()) {
                        if (component.canUsePurifier(player) && ElectricItem.manager.canUse(itemstack, energy * coef)) {
                            component.workPurifier();
                            return EnumActionResult.SUCCESS;
                        }
                    }
                }
                if (tile instanceof TileMultiMachine) {
                    if (!ElectricItem.manager.canUse(itemstack, 500 * coef)) {
                        return EnumActionResult.PASS;
                    }
                    if (!player.isSneaking()) {
                        TileMultiMachine base = (TileMultiMachine) tile;
                        ItemStack stack_quickly = ItemStack.EMPTY;
                        ItemStack stack_modulesize = ItemStack.EMPTY;
                        ItemStack panel = ItemStack.EMPTY;
                        ItemStack stack_modulestorage = ItemStack.EMPTY;
                        ItemStack module_infinity_water = ItemStack.EMPTY;
                        ItemStack module_separate = ItemStack.EMPTY;
                        if (base.multi_process.quickly) {
                            stack_quickly = new ItemStack(IUItem.module_quickly);
                        }
                        if (base.multi_process.modulesize) {
                            stack_modulesize = new ItemStack(IUItem.module_stack);
                        }
                        if (base.solartype != null) {
                            panel = new ItemStack(IUItem.module6, 1, base.solartype.meta);
                        }
                        if (base.multi_process.modulestorage) {
                            stack_modulestorage = new ItemStack(IUItem.module_storage);
                        }
                        if (base.multi_process.module_infinity_water) {
                            module_infinity_water = new ItemStack(IUItem.module_infinity_water);
                        }
                        if (base.multi_process.module_separate) {
                            module_separate = new ItemStack(IUItem.module_separate);
                        }
                        if (!stack_quickly.isEmpty() || !stack_modulesize.isEmpty() || !panel.isEmpty() || !module_infinity_water.isEmpty() || !module_separate.isEmpty()) {
                            final EntityItem item = new EntityItem(world);
                            if (!stack_quickly.isEmpty()) {
                                item.setItem(stack_quickly);
                                base.multi_process.shrinkModule(1);
                                base.multi_process.setQuickly(false);
                            } else if (!stack_modulesize.isEmpty()) {
                                item.setItem(stack_modulesize);
                                base.multi_process.setModulesize(false);
                                base.multi_process.shrinkModule(1);
                            } else if (!module_separate.isEmpty()) {
                                item.setItem(module_separate);
                                base.multi_process.module_separate = false;
                                base.multi_process.shrinkModule(1);
                            } else if (!module_infinity_water.isEmpty()) {
                                item.setItem(module_infinity_water);
                                base.multi_process.module_infinity_water = false;
                                base.multi_process.shrinkModule(1);
                            } else if (!panel.isEmpty()) {
                                item.setItem(panel);
                                base.solartype = null;
                            } else if (!stack_modulestorage.isEmpty()) {
                                item.setItem(stack_modulestorage);
                                base.multi_process.setModulestorage(false);
                                base.multi_process.shrinkModule(1);
                            }
                            if (!player.getEntityWorld().isRemote) {
                                item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                                item.setPickupDelay(0);
                                world.spawnEntity(item);
                                ElectricItem.manager.use(itemstack, 500 * coef, player);
                                if (IUCore.proxy.isRendering()) {
                                    player.playSound(EnumSound.purifier.getSoundEvent(), 1F, 1);
                                }
                                return EnumActionResult.SUCCESS;
                            }
                        }
                    } else {
                        TileMultiMachine base = (TileMultiMachine) tile;
                        List<ItemStack> stack_list = new ArrayList<>();
                        if (base.multi_process.quickly) {
                            stack_list.add(new ItemStack(IUItem.module_quickly));
                            base.multi_process.setQuickly(false);
                            base.multi_process.shrinkModule(1);
                        }
                        if (base.multi_process.modulesize) {
                            stack_list.add(new ItemStack(IUItem.module_stack));
                            base.multi_process.setModulesize(false);
                            base.multi_process.shrinkModule(1);
                        }
                        if (base.multi_process.module_separate) {
                            stack_list.add(new ItemStack(IUItem.module_separate));
                            base.multi_process.module_separate = false;
                            base.multi_process.shrinkModule(1);
                        }
                        if (base.solartype != null) {
                            stack_list.add(new ItemStack(IUItem.module6, 1, base.solartype.meta));
                            base.solartype = null;
                        }
                        if (base.multi_process.modulestorage) {
                            stack_list.add(new ItemStack(IUItem.module_storage));
                            base.multi_process.setModulestorage(false);
                            base.multi_process.shrinkModule(1);

                        }
                        if (base.multi_process.module_infinity_water) {
                            stack_list.add(new ItemStack(IUItem.module_infinity_water));
                            base.multi_process.module_infinity_water = false;
                            base.multi_process.shrinkModule(1);

                        }
                        for (ItemStack stack : stack_list) {
                            final EntityItem item = new EntityItem(world);
                            if (!player.getEntityWorld().isRemote) {
                                item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                                item.setPickupDelay(0);
                                item.setItem(stack);
                                world.spawnEntity(item);
                                if (IUCore.proxy.isRendering()) {
                                    player.playSound(EnumSound.purifier.getSoundEvent(), 1F, 1);
                                }

                            }
                        }
                        ElectricItem.manager.use(itemstack, 500 * coef, player);
                        return EnumActionResult.SUCCESS;
                    }
                } else {
                    if (!(tile instanceof IManufacturerBlock)) {
                        return EnumActionResult.FAIL;
                    }
                    IManufacturerBlock base = (IManufacturerBlock) tile;
                    if (player.isSneaking()) {
                        int level = base.getLevel();
                        if (level == 0) {
                            return EnumActionResult.PASS;
                        }
                        final ItemStack stack = new ItemStack(IUItem.upgrade_speed_creation, level);
                        base.setLevel(0);
                        final EntityItem item = new EntityItem(world);
                        item.setItem(stack);
                        if (!player.getEntityWorld().isRemote) {
                            item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                            item.setPickupDelay(0);
                            world.spawnEntity(item);
                            ElectricItem.manager.use(itemstack, 500, player);
                            if (IUCore.proxy.isRendering()) {
                                player.playSound(EnumSound.purifier.getSoundEvent(), 1F, 1);

                            }
                            return EnumActionResult.SUCCESS;
                        }
                    } else {
                        int level = base.getLevel();
                        if (level == 0) {
                            return EnumActionResult.PASS;
                        }
                        final ItemStack stack = new ItemStack(IUItem.upgrade_speed_creation, 1);
                        base.removeLevel(1);
                        final EntityItem item = new EntityItem(world);
                        item.setItem(stack);
                        if (!player.getEntityWorld().isRemote) {
                            item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                            item.setPickupDelay(0);
                            world.spawnEntity(item);
                            ElectricItem.manager.use(itemstack, 500, player);
                            if (IUCore.proxy.isRendering()) {
                                player.playSound(EnumSound.purifier.getSoundEvent(), 1F, 1);

                            }
                            return EnumActionResult.SUCCESS;
                        }

                    }

                }

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
            IBlockState state = world.getBlockState(pos);
            state = state.getActualState(world, pos);
            Block block = state.getBlock();
            IBlockState state1 = world.getBlockState(pos.up());
            state1 = state1.getActualState(world, pos.up());
            if (side != EnumFacing.DOWN && state1.getMaterial() == Material.AIR) {
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
        IBlockState state = world.getBlockState(pos);
        state = state.getActualState(world, pos);
        return hasNecessaryPower(
                stack,
                TAP,
                player
        ) && (state.getBlock() == IUItem.rubWood && ItemTreetap.attemptExtract(
                player,
                world,
                pos,
                side,
                state,
                null
        ) || state.getBlock() == IUItem.swampRubWood && ItemTreetap.attemptSwampExtract(
                player,
                world,
                pos,
                side,
                state,
                null
        ) || state.getBlock() == IUItem.tropicalRubWood && ItemTreetap.attemptTropicalExtract(
                player,
                world,
                pos,
                side,
                state,
                null
        )) && checkNecessaryPower(stack, TAP, player);
    }

    protected boolean onWrenchUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        state = state.getActualState(world, pos);
        Block block = state.getBlock();
        if (block.isAir(state, world, pos)) {
            return false;
        }
        if (block instanceof IWrenchable) {
            IWrenchable wrenchable = (IWrenchable) block;
            EnumFacing current = wrenchable.getFacing(world, pos);
            EnumFacing newFacing;
            if (!IUCore.keyboard.isChangeKeyDown(player)) {
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

                    int fortune = player.getEntityWorld().rand.nextInt(100);
                    if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.WRENCH, stack)) {
                        fortune = 100;
                    }

                    for (ItemStack drop : wrenchable.getWrenchDrops(world, pos, state, te, player, fortune)) {
                        ModUtils.dropAsEntity(world, pos, drop);
                    }
                    wrenchable.wrenchBreak(world, pos);
                    if (!player.capabilities.isCreativeMode && experience > 0) {
                        block.dropXpOnBlockBreak(world, pos, experience);
                    }
                }

                return checkNecessaryPower(stack, ROTATE, player);
            }
        }

        return false;
    }

    public void onUpdate(
            @Nonnull ItemStack itemStack,
            @Nonnull World p_77663_2_,
            @Nonnull Entity p_77663_3_,
            int p_77663_4_,
            boolean p_77663_5_
    ) {
        NBTTagCompound nbt = ModUtils.nbt(itemStack);

        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            nbt.setBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(p_77663_2_, this, itemStack));
        }
    }

    protected boolean onScrewdriverUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        state = state.getActualState(world, pos);
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

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, @Nonnull EntityLivingBase target, @Nonnull EntityLivingBase attacker) {
        return false;
    }

    @Override
    public boolean onBlockDestroyed(
            @Nonnull ItemStack stack,
            @Nonnull World world,
            @Nonnull IBlockState state,
            @Nonnull BlockPos pos,
            @Nonnull EntityLivingBase entityLiving
    ) {
        return true;
    }

    @Override
    public boolean doesSneakBypassUse(
            @Nonnull ItemStack stack,
            @Nonnull IBlockAccess world,
            @Nonnull BlockPos pos,
            @Nonnull EntityPlayer player
    ) {
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
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return false;
    }

    @Nonnull
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(@Nonnull EntityEquipmentSlot slot) {
        return HashMultimap.create();
    }


    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ElectricItemManager.addChargeVariants(this, items);
        }

    }

    @Override
    public boolean canProvideEnergy(ItemStack stack) {
        return false;
    }

    @Override
    public double getMaxEnergy(ItemStack stack) {
        return 300000.0D;
    }

    @Override
    public short getTierItem(ItemStack stack) {
        return 2;
    }

    @Override
    public double getTransferEnergy(ItemStack stack) {
        return 10000.0D;
    }

    public enum GraviToolMode {
        HOE(TextFormatting.GOLD),
        TREETAP(TextFormatting.LIGHT_PURPLE),
        WRENCH(TextFormatting.AQUA),
        SCREWDRIVER(TextFormatting.YELLOW),
        PURIFIER(TextFormatting.DARK_AQUA);
        private static final ItemGraviTool.GraviToolMode[] VALUES = values();
        public final String translationName = "iu.graviTool.snap." + this.name().toLowerCase(Locale.ENGLISH);
        public final TextFormatting colour;
        private final ModelResourceLocation model =
                new ModelResourceLocation(Constants.MOD_ID + ":" + "gravitool/gravitool".toLowerCase(Locale.ENGLISH) + this
                        .name()
                        .toLowerCase(Locale.ENGLISH), null);

        GraviToolMode(TextFormatting colour) {
            this.colour = colour;
        }

        public static ItemGraviTool.GraviToolMode getFromID(int ID) {
            return VALUES[ID % VALUES.length];
        }
    }

}
