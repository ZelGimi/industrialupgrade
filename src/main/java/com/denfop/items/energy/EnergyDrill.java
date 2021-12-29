package com.denfop.items.energy;


import com.denfop.Config;
import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.proxy.CommonProxy;
import com.denfop.utils.EnumInfoUpgradeModules;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class EnergyDrill extends ItemTool implements IElectricItem, IModelRegister {

    public static final Set<IBlockState> mineableBlocks = Sets.newHashSet(
            Blocks.COBBLESTONE.getDefaultState(),
            Blocks.DOUBLE_STONE_SLAB.getDefaultState(),
            Blocks.STONE_SLAB.getDefaultState(),
            Blocks.STONE.getDefaultState(),
            Blocks.SANDSTONE.getDefaultState(),
            Blocks.MOSSY_COBBLESTONE.getDefaultState(),
            Blocks.IRON_ORE.getDefaultState(),
            Blocks.IRON_BLOCK.getDefaultState(),
            Blocks.COAL_ORE.getDefaultState(),
            Blocks.GOLD_BLOCK.getDefaultState(),
            Blocks.GOLD_ORE.getDefaultState(),
            Blocks.DIAMOND_ORE.getDefaultState(),
            Blocks.DIAMOND_BLOCK.getDefaultState(),
            Blocks.ICE.getDefaultState(),
            Blocks.NETHERRACK.getDefaultState(),
            Blocks.LAPIS_ORE.getDefaultState(),
            Blocks.LAPIS_BLOCK.getDefaultState(),
            Blocks.REDSTONE_ORE.getDefaultState(),
            Blocks.LIT_REDSTONE_ORE.getDefaultState(),
            Blocks.RAIL.getDefaultState(),
            Blocks.DETECTOR_RAIL.getDefaultState(),
            Blocks.GOLDEN_RAIL.getDefaultState(),
            Blocks.ACTIVATOR_RAIL.getDefaultState(),
            Blocks.GRASS.getDefaultState(),
            Blocks.DIRT.getDefaultState(),
            Blocks.SAND.getDefaultState(),
            Blocks.GRAVEL.getDefaultState(),
            Blocks.SNOW_LAYER.getDefaultState(),
            Blocks.SNOW.getDefaultState(),
            Blocks.CLAY.getDefaultState(),
            Blocks.FARMLAND.getDefaultState(),
            Blocks.SOUL_SAND.getDefaultState(),
            Blocks.MYCELIUM.getDefaultState()
    );
    private static final Set<Material> materials = Sets.newHashSet(Material.IRON, Material.ANVIL,
            Material.ROCK, Material.GRASS, Material.ICE, Material.PACKED_ICE, Material.GRASS, Material.GROUND,
            Material.SAND, Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY
    );

    private static final Set<String> toolType = ImmutableSet.of("pickaxe", "shovel");
    public final String name;
    public final int efficienty;
    public final int lucky;
    private final float bigHolePower;
    private final float normalPower;
    private final int maxCharge;
    private final int tier;
    private final int energyPerOperation;
    private final int energyPerbigHolePowerOperation;
    private final int transferLimit;

    public EnergyDrill(
            Item.ToolMaterial toolMaterial, String name, int efficienty, int lucky, int transferlimit,
            int maxCharge, int tier, int normalPower, int bigHolesPower, int energyPerOperation,
            int energyPerbigHolePowerOperation
    ) {
        super(0.0F, 0.0F + toolMaterial.getAttackDamage(), toolMaterial, new HashSet());
        setMaxDamage(27);

        setCreativeTab(IUCore.EnergyTab);
        this.efficienty = efficienty;
        this.lucky = lucky;
        this.name = name;
        this.transferLimit = transferlimit;
        this.maxCharge = maxCharge;
        this.tier = tier;
        this.efficiency = this.normalPower = normalPower;
        this.bigHolePower = bigHolesPower;
        this.energyPerOperation = energyPerOperation;
        this.energyPerbigHolePowerOperation = energyPerbigHolePowerOperation;
        this.setUnlocalizedName(name);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);

    }

    public static void updateGhostBlocks(EntityPlayer player, World world) {
        if (world.isRemote) {
            return;
        }
        int xPos = (int) player.posX;
        int yPos = (int) player.posY;
        int zPos = (int) player.posZ;
        for (int x = xPos - 6; x < xPos + 6; x++) {
            for (int y = yPos - 6; y < yPos + 6; y++) {
                for (int z = zPos - 6; z < zPos + 6; z++) {
                    ((EntityPlayerMP) player).connection.sendPacket(new SPacketBlockChange(world, new BlockPos(x, y, z)));
                }
            }
        }
    }

    public static int readToolMode(ItemStack itemstack) {
        NBTTagCompound nbt = ModUtils.nbt(itemstack);
        int toolMode = nbt.getInteger("toolMode");

        if (toolMode < 0 || toolMode > 2) {
            toolMode = 0;
        }
        return toolMode;
    }


    public static RayTraceResult raytraceFromEntity(World world, Entity player, boolean par3, double range) {
        float pitch = player.rotationPitch;
        float yaw = player.rotationYaw;
        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;
        if (!world.isRemote && player instanceof EntityPlayer) {
            y++;
        }
        Vec3d vec3 = new Vec3d(x, y, z);
        float f3 = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
        float f4 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
        float f5 = -MathHelper.cos(-pitch * 0.017453292F);
        float f6 = MathHelper.sin(-pitch * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        if (player instanceof EntityPlayerMP) {
            range = ((EntityPlayerMP) player).interactionManager.getBlockReachDistance();
        }
        Vec3d vec31 = vec3.addVector(range * f7, range * f6, range * f8);
        return world.rayTraceBlocks(vec3, vec31, par3, !par3, par3);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean par5) {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);


        for (int i = 0; i < 4; i++) {
            if (nbtData.getString("mode_module" + i).equals("silk")) {
                Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(itemStack);
                enchantmentMap.put(Enchantments.SILK_TOUCH, 1);
                EnchantmentHelper.setEnchantments(enchantmentMap, itemStack);
                break;
            }
        }


    }

    boolean break_block(
            World world, Block block, int meta, RayTraceResult mop, byte mode_item, EntityPlayer player, BlockPos pos,
            ItemStack stack, IBlockState state_block
    ) {
        byte xRange = mode_item;
        byte yRange = mode_item;
        byte zRange = mode_item;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        switch (mop.sideHit.ordinal()) {
            case 0:
            case 1:
                yRange = 0;
                break;
            case 2:
            case 3:
                zRange = 0;
                break;
            case 4:
            case 5:
                xRange = 0;
                break;
        }

        boolean lowPower = false;
        boolean silktouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);

        int Yy;
        Yy = yRange > 0 ? yRange - 1 : 0;
        NBTTagCompound nbt = ModUtils.nbt(stack);
        float energy = energy(stack);
        byte dig_depth = 0;

        for (int i = 0; i < 4; i++) {
            if (nbt.getString("mode_module" + i).equals("dig_depth")) {
                dig_depth++;
            }
        }
        dig_depth = (byte) Math.min(dig_depth, EnumInfoUpgradeModules.DIG_DEPTH.max);
        zRange = zRange > 0 ? zRange : (byte) (zRange + dig_depth);
        xRange = xRange > 0 ? xRange : (byte) (xRange + dig_depth);
        nbt.setInteger("zRange", zRange);
        nbt.setInteger("xRange", xRange);
        nbt.setInteger("yRange", yRange);
        boolean save = nbt.getBoolean("save");
        if (!player.capabilities.isCreativeMode) {
            for (int xPos = x - xRange; xPos <= x + xRange; xPos++) {
                for (int yPos = y - yRange + Yy; yPos <= y + yRange + Yy; yPos++) {
                    for (int zPos = z - zRange; zPos <= z + zRange; zPos++) {
                        if (ElectricItem.manager.canUse(stack, energy)) {
                            BlockPos pos_block = new BlockPos(xPos, yPos, zPos);
                            if (save) {
                                if (world.getTileEntity(pos_block) != null) {
                                    continue;
                                }
                            }
                            IBlockState state = world.getBlockState(pos_block);
                            Block localBlock = world.getBlockState(pos_block).getBlock();
                            if (!localBlock.equals(Blocks.AIR) && canHarvestBlock(state, stack)
                                    && state.getBlockHardness(world, pos_block) >= 0.0F
                            ) {
                                if (state.getBlockHardness(world, pos_block) > 0.0F) {
                                    onBlockDestroyed(stack, world, state, pos_block,
                                            player
                                    );
                                }
                                if (!silktouch) {
                                    localBlock.dropXpOnBlockBreak(world, pos_block,
                                            localBlock.getExpDrop(state, world, pos_block, fortune)
                                    );
                                }
                                if (mop.typeOfHit == RayTraceResult.Type.MISS) {
                                    updateGhostBlocks(player, player.getEntityWorld());
                                }

                            } else {
                                if (state.getBlockHardness(world, pos_block) > 0.0F && materials.contains(state.getMaterial())) {
                                    return onBlockDestroyed(stack, world, state, pos_block,
                                            player
                                    );
                                }


                            }


                        } else {
                            lowPower = true;
                            break;
                        }
                    }
                }
            }
        } else {
            if (ElectricItem.manager.canUse(stack, energy)) {
                Block localBlock = world.getBlockState(pos).getBlock();
                IBlockState state = world.getBlockState(pos);
                if (localBlock.equals(Blocks.AIR) && canHarvestBlock(state, stack)
                        && state.getBlockHardness(world, pos) >= 0.0F
                        && (materials.contains(state.getMaterial())
                        || block == Blocks.MONSTER_EGG)) {
                    if (state.getBlockHardness(world, pos) > 0.0F) {
                        onBlockDestroyed(stack, world, state, pos,
                                player
                        );
                    }
                    if (!silktouch) {
                        localBlock.dropXpOnBlockBreak(world, pos,
                                localBlock.getExpDrop(state, world, pos, fortune)
                        );
                    }


                } else {
                    if (state.getBlockHardness(world, pos) > 0.0F) {
                        return onBlockDestroyed(stack, world, state, pos,
                                player
                        );
                    }
                }
            }
        }
        if (lowPower) {
            if (ElectricItem.manager.canUse(stack, energy)) {
                IBlockState state = world.getBlockState(pos);
                Block localBlock = state.getBlock();
                if (!localBlock.equals(Blocks.AIR) && canHarvestBlock(state, stack)
                        && state.getBlockHardness(world, pos) >= 0.0F
                        && (materials.contains(state.getMaterial())
                        || block == Blocks.MONSTER_EGG)) {

                    if (state.getBlockHardness(world, pos) > 0.0F) {
                        onBlockDestroyed(stack, world, state, pos,
                                player
                        );
                    }
                    if (!silktouch) {
                        localBlock.dropXpOnBlockBreak(world, pos,
                                localBlock.getExpDrop(state, world, pos, fortune)
                        );
                    }


                } else {
                    if (state.getBlockHardness(world, pos) > 0.0F) {
                        return onBlockDestroyed(stack, world, state, pos,
                                player
                        );
                    }
                }
            }
        } else if (!IC2.platform.isSimulating()) {
            world.playEvent(2001, pos, Block.getIdFromBlock(block) + (meta << 12));
        }
        return true;
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase damagee, EntityLivingBase damager) {
        return true;
    }

    public int getItemEnchantability() {
        return 0;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    public double getMaxCharge(ItemStack itemStack) {
        return this.maxCharge;
    }

    public int getTier(ItemStack itemStack) {
        return this.tier;
    }

    public double getTransferLimit(ItemStack itemStack) {
        return this.transferLimit;
    }

    public Set<String> getToolClasses(ItemStack stack) {
        return toolType;
    }


    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        int energy = 0;
        int speed = 0;
        for (int i = 0; i < 4; i++) {
            if (nbt.getString("mode_module" + i).equals("speed")) {
                speed++;
            }
            if (nbt.getString("mode_module" + i).equals("energy")) {
                energy++;
            }
        }
        energy = Math.min(energy, EnumInfoUpgradeModules.ENERGY.max);
        speed = Math.min(speed, EnumInfoUpgradeModules.EFFICIENCY.max);
        return !ElectricItem.manager.canUse(stack, (this.energyPerOperation - (int) (this.energyPerOperation * 0.25 * energy)))
                ? 1.0F
                : (canHarvestBlock(state, stack) ? (this.efficiency + (int) (this.efficiency * 0.2 * speed)) : 1.0F);

    }

    @Override
    public int getHarvestLevel(
            final ItemStack stack,
            final String toolClass,
            @Nullable final EntityPlayer player,
            @Nullable final IBlockState blockState
    ) {
        return (!toolClass.equals("pickaxe") && !toolClass.equals("shovel")) ? super.getHarvestLevel(
                stack,
                toolClass,
                player,
                blockState
        )
                : this.toolMaterial.getHarvestLevel();
    }


    @Override
    public boolean canHarvestBlock(final IBlockState state, final ItemStack stack) {
        return (Items.DIAMOND_PICKAXE.canHarvestBlock(state, stack)
                || Items.DIAMOND_PICKAXE.getDestroySpeed(stack, state) > 1.0F || mineableBlocks.contains(state) ||
                Items.DIAMOND_SHOVEL.canHarvestBlock(state, stack)
                || Items.DIAMOND_SHOVEL.getDestroySpeed(stack, state) > 1.0F);

    }

    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        if (readToolMode(stack) == 0) {
            World world = player.getEntityWorld();
            IBlockState state = world.getBlockState(pos);
            Block block = world.getBlockState(pos).getBlock();
            int meta = state.getBlock().getMetaFromState(state);
            if (block == Blocks.AIR) {
                return super.onBlockStartBreak(stack, pos, player);
            }

            RayTraceResult mop = raytraceFromEntity(world, player, true, 4.5D);
            NBTTagCompound nbt = ModUtils.nbt(stack);
            byte aoe = 0;

            for (int i = 0; i < 4; i++) {
                if (nbt.getString("mode_module" + i).equals("AOE_dig")) {
                    aoe++;

                }
            }
            aoe = (byte) Math.min(aoe, EnumInfoUpgradeModules.AOE_DIG.max);


            return break_block(world, block, meta, mop, aoe, player, pos, stack, state);
        }
        if (readToolMode(stack) == 1) {
            World world = player.getEntityWorld();

            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            int meta = block.getMetaFromState(state);

            if (block.equals(Blocks.AIR)) {
                return super.onBlockStartBreak(stack, pos, player);
            }
            RayTraceResult mop = raytraceFromEntity(world, player, true, 4.5D);


            NBTTagCompound nbt = ModUtils.nbt(stack);
            byte aoe = 0;

            for (int i = 0; i < 4; i++) {
                if (nbt.getString("mode_module" + i).equals("AOE_dig")) {
                    aoe++;

                }
            }
            aoe = (byte) Math.min(aoe, EnumInfoUpgradeModules.AOE_DIG.max);
            if (materials.contains(state.getMaterial()) || block == Blocks.MONSTER_EGG) {
                if (player.isSneaking()) {
                    return break_block(world, block, meta, mop, aoe, player, pos, stack, state);
                }


                return break_block(world, block, meta, mop, (byte) (1 + aoe), player, pos, stack, state);
            }
        }
        if (readToolMode(stack) == 2) {
            World world = player.getEntityWorld();

            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            int meta = block.getMetaFromState(state);

            if (block.equals(Blocks.AIR)) {
                return super.onBlockStartBreak(stack, pos, player);
            }
            RayTraceResult mop = raytraceFromEntity(world, player, true, 4.5D);

            boolean silktouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
            int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);


            NBTTagCompound nbt = ModUtils.nbt(stack);
            nbt.setInteger("ore", 1);

            if (!mop.typeOfHit.equals(RayTraceResult.Type.MISS)) {
                ore_break(world, pos, player, silktouch, fortune, false, stack, block);
            }

        }
        return super.onBlockStartBreak(stack, pos, player);
    }

    private void ore_break(
            World world, BlockPos pos, EntityPlayer player, boolean silktouch, int fortune, boolean lowPower,
            ItemStack stack, Block block1
    ) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        int energy = 0;
        for (int i = 0; i < 4; i++) {
            if (nbt.getString("mode_module" + i).equals("energy")) {
                energy++;
            }
        }
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        energy = Math.min(energy, EnumInfoUpgradeModules.ENERGY.max);
        for (int Xx = x - 1; Xx <= x + 1; Xx++) {
            for (int Yy = y - 1; Yy <= y + 1; Yy++) {
                for (int Zz = z - 1; Zz <= z + 1; Zz++) {
                    NBTTagCompound NBTTagCompound = ModUtils.nbt(stack);
                    int ore = NBTTagCompound.getInteger("ore");
                    if (ore < 16) {
                        if (ElectricItem.manager.canUse(
                                stack,
                                (this.energyPerOperation - this.energyPerOperation * 0.25 * energy)
                        )) {
                            BlockPos pos_block = new BlockPos(Xx, Yy, Zz);
                            Block localBlock = world.getBlockState(pos_block).getBlock();
                            IBlockState state = world.getBlockState(pos_block);
                            if (ModUtils.getore(localBlock, block1)) {


                                if (!player.capabilities.isCreativeMode) {

                                    if (state.getBlockHardness(world, pos_block) > 0.0F) {
                                        onBlockDestroyed(stack, world, state, pos_block,
                                                player
                                        );

                                    }
                                    if (!silktouch) {
                                        localBlock.dropXpOnBlockBreak(world, pos_block,
                                                localBlock.getExpDrop(state, world, pos_block, fortune)
                                        );
                                    }


                                    ore = ore + 1;
                                    NBTTagCompound.setInteger("ore", ore);
                                    ore_break(world, pos_block, player, silktouch, fortune, lowPower, stack, block1);
                                } else {
                                    break;
                                }

                                world.markBlockRangeForRenderUpdate(pos_block, pos_block);

                            }
                        } else {
                            lowPower = true;
                            break;
                        }
                    }
                }
            }
        }

    }

    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity) {

        Block block = state.getBlock();
        if (block.equals(Blocks.AIR)) {
            return false;
        } else {

            if (world.isAirBlock(pos)) {
                return false;
            }
            if (state.getMaterial() instanceof MaterialLiquid || (state.getBlockHardness(
                    world,
                    pos
            ) == -1 && !((EntityPlayer) entity).capabilities.isCreativeMode)) {
                return false;
            }

            if (!world.isRemote) {
                block.onBlockHarvested(world, pos, state, (EntityPlayerMP) entity);

                if (block.removedByPlayer(state, world, pos, (EntityPlayerMP) entity, true)) {
                    block.onBlockDestroyedByPlayer(world, pos, state);
                    block.harvestBlock(world, (EntityPlayerMP) entity, pos, state, null, stack);
                    NBTTagCompound nbt = ModUtils.nbt(stack);

                    int xMin = nbt.getInteger("xRange"), xMax = nbt.getInteger("xRange");
                    int yMin = nbt.getInteger("yRange"), yMax = nbt.getInteger("yRange");
                    int zMin = nbt.getInteger("zRange"), zMax = nbt.getInteger("zRange");
                    List<EntityItem> items = entity.getEntityWorld().getEntitiesWithinAABB(
                            EntityItem.class,
                            new AxisAlignedBB(pos.getX() - xMin, pos.getY() - yMin, pos.getZ() - zMin, pos.getX() + xMax + 1,
                                    pos.getY() + yMax + 1,
                                    pos.getZ() + zMax + 1
                            )
                    );


                    ((EntityPlayerMP) entity).addExhaustion(-0.025F);
                    if ((ModUtils.getore(block, block.getMetaFromState(state)) && nbt.getBoolean("black") && check_list(
                            block,
                            block.getMetaFromState(state),
                            stack
                    )) || !Config.blacklist || !nbt.getBoolean("black")) {
                        for (EntityItem item : items) {
                            if (!entity.getEntityWorld().isRemote) {
                                item.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, 0.0F, 0.0F);
                                ((EntityPlayerMP) entity).connection.sendPacket(new SPacketEntityTeleport(item));
                                item.setPickupDelay(0);

                            }
                        }
                    } else {
                        if (nbt.getBoolean("black")) {
                            for (EntityItem item : items) {
                                if (!entity.getEntityWorld().isRemote) {

                                    item.setDead();

                                }
                            }
                        }
                    }
                }
                ForgeHooks.onBlockBreakEvent(world, world.getWorldInfo().getGameType(), (EntityPlayerMP) entity, pos);
                EntityPlayerMP mpPlayer = (EntityPlayerMP) entity;
                mpPlayer.connection.sendPacket(new SPacketBlockChange(world, new BlockPos(pos)));
            } else {
                if (block.removedByPlayer(state, world, pos, (EntityPlayer) entity, true)) {
                    block.onBlockDestroyedByPlayer(world, pos, state);
                }

                Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).sendPacket(new CPacketPlayerDigging(
                        CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                        pos,
                        Minecraft.getMinecraft().objectMouseOver.sideHit
                ));
            }
            if (entity.isEntityAlive()) {
                float energy = energy(stack);
                if (energy != 0.0F && state.getBlockHardness(world, pos) != 0.0F) {
                    ElectricItem.manager.use(stack, energy, entity);
                }
            }

            return true;
        }
    }


    public float energy(ItemStack stack) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        int energy1 = 0;

        for (int i = 0; i < 4; ++i) {
            if (nbt.getString("mode_module" + i).equals("energy")) {
                ++energy1;
            }
        }

        energy1 = Math.min(energy1, EnumInfoUpgradeModules.ENERGY.max);
        int toolMode = readToolMode(stack);
        float energy;
        switch (toolMode) {
            case 1:
            case 2:
                energy = (float) (this.energyPerbigHolePowerOperation - this.energyPerbigHolePowerOperation * 0.25 * energy1);
                break;

            default:
                energy = (float) (this.energyPerOperation - this.energyPerOperation * 0.25 * energy1);
                break;
        }
        return energy;


    }

    public void saveToolMode(ItemStack itemstack, int toolMode) {
        NBTTagCompound nbt = ModUtils.nbt(itemstack);
        nbt.setInteger("toolMode", toolMode);
        itemstack.setTagCompound(nbt);
    }

    @Override
    public EnumActionResult onItemUse(
            final EntityPlayer player,
            final World world,
            final BlockPos pos,
            final EnumHand hand,
            final EnumFacing facing,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
            ItemStack torchStack = player.inventory.mainInventory.get(i);
            if (!torchStack.isEmpty() && torchStack.getUnlocalizedName().toLowerCase().contains("torch")) {
                Item item = torchStack.getItem();
                if (item instanceof net.minecraft.item.ItemBlock) {
                    int oldMeta = torchStack.getItemDamage();
                    int oldSize = torchStack.stackSize;
                    boolean result = torchStack.onItemUse(player, world, pos, hand, facing, hitX,
                            hitY, hitZ
                    ) == EnumActionResult.SUCCESS;
                    if (player.capabilities.isCreativeMode) {
                        torchStack.setItemDamage(oldMeta);
                        torchStack.stackSize = oldSize;
                    } else if (torchStack.stackSize <= 0) {
                        ForgeEventFactory.onPlayerDestroyItem(player, torchStack, hand);
                        player.inventory.mainInventory.set(i, new ItemStack(Items.AIR));
                    }
                    if (result) {
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }


    @Override
    public ActionResult onItemRightClick(final World worldIn, final EntityPlayer player, final EnumHand hand) {

        ItemStack itemStack = player.getHeldItem(hand);
        if (IUCore.keyboard.isSaveModeKeyDown(player)) {
            NBTTagCompound nbt = ModUtils.nbt(itemStack);
            boolean save = !nbt.getBoolean("save");
            CommonProxy.sendPlayerMessage(
                    player,
                    TextFormatting.GREEN + Localization.translate("message.savemode") +
                            (save ? Localization.translate("message.allow") : Localization.translate("message.disallow"))
            );
            nbt.setBoolean("save", save);
        }
        if (IUCore.keyboard.isBlackListModeKeyDown(player)) {
            NBTTagCompound nbt = ModUtils.nbt(itemStack);
            boolean black = !nbt.getBoolean("black");
            CommonProxy.sendPlayerMessage(
                    player,
                    TextFormatting.GREEN + Localization.translate("message.blacklist") +
                            (black ? Localization.translate("message.allow") : Localization.translate("message.disallow"))
            );
            nbt.setBoolean("black", black);
        }
        if (IUCore.keyboard.isChangeKeyDown(player)) {
            int toolMode = readToolMode(itemStack) + 1;
            if (!IC2.platform.isRendering()) {
                IUCore.audioManager.playOnce(
                        player,
                        com.denfop.audio.PositionSpec.Hand,
                        "Tools/toolChange.ogg",
                        true,
                        IC2.audioManager.getDefaultVolume()
                );
            }
            if (toolMode > 2) {
                toolMode = 0;
            }
            saveToolMode(itemStack, toolMode);
            Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(itemStack);
            switch (toolMode) {
                case 0:
                    if (IC2.platform.isSimulating()) {
                        IC2.platform.messagePlayer(
                                player,
                                TextFormatting.GREEN + Localization.translate("message.text.mode") + ": "
                                        + Localization.translate("message.ultDDrill.mode.normal")
                        );
                    }
                    this.efficiency = this.normalPower;
                    if (this.efficienty != 0) {
                        enchantmentMap.put(Enchantments.EFFICIENCY, this.efficienty);
                    }
                    if (this.lucky != 0) {
                        enchantmentMap.put(Enchantments.FORTUNE, this.lucky);
                    }

                    EnchantmentHelper.setEnchantments(enchantmentMap, itemStack);
                    break;

                case 1:

                    enchantmentMap.put(Enchantments.EFFICIENCY, this.efficienty);
                    enchantmentMap.put(Enchantments.FORTUNE, this.lucky);

                    EnchantmentHelper.setEnchantments(enchantmentMap, itemStack);
                    if (IC2.platform.isSimulating()) {
                        IC2.platform.messagePlayer(
                                player,
                                TextFormatting.DARK_PURPLE + Localization.translate("message.text.mode") + ": "
                                        + Localization.translate("message.ultDDrill.mode.bigHoles")
                        );
                    }
                    this.efficiency = this.bigHolePower;
                    break;
                case 2:

                    if (IC2.platform.isSimulating()) {
                        IC2.platform.messagePlayer(
                                player,
                                TextFormatting.GOLD + Localization.translate("message.text.mode") + ": "
                                        + Localization.translate("message.ultDDrill.mode.pickaxe")
                        );
                    }
                    this.efficiency = this.normalPower;
                    break;
            }
            return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
        }
        return new ActionResult(EnumActionResult.PASS, player.getHeldItem(hand));

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(
            final ItemStack par1ItemStack,
            @Nullable final World worldIn,
            final List<String> par3List,
            final ITooltipFlag flagIn
    ) {
        int toolMode = readToolMode(par1ItemStack);
        switch (toolMode) {
            case 0:
                par3List.add(TextFormatting.GOLD + Localization.translate("message.text.mode") + ": "
                        + TextFormatting.WHITE + Localization.translate("message.ultDDrill.mode.normal"));
                par3List.add(Localization.translate("message.description.normal"));
                break;
            case 1:
                par3List.add(TextFormatting.GOLD + Localization.translate("message.text.mode") + ": "
                        + TextFormatting.WHITE + Localization.translate("message.ultDDrill.mode.bigHoles"));
                par3List.add(Localization.translate("message.description.bigHoles"));
                break;
            case 2:

                par3List.add(TextFormatting.GOLD + Localization.translate("message.text.mode") + ": "
                        + TextFormatting.WHITE + Localization.translate("message.ultDDrill.mode.pickaxe"));
                par3List.add(Localization.translate("message.description.pickaxe"));
                break;

        }
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            par3List.add(Localization.translate("press.lshift"));

        }


        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            par3List.add(Localization.translate("iu.changemode_key") + Keyboard.getKeyName(KeyboardClient.changemode.getKeyCode()) + Localization.translate(
                    "iu.changemode_rcm"));
            par3List.add(Localization.translate("iu.blacklist_key") + Keyboard.getKeyName(KeyboardClient.blackmode.getKeyCode()) + Localization.translate(
                    "iu.changemode_rcm"));

        }
        ModUtils.mode(par1ItemStack, par3List);
        super.addInformation(par1ItemStack, worldIn, par3List, flagIn);
    }

    public boolean check_list(Block block, int metaFromState, ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        if (!nbt.getBoolean("list")) {
            return true;
        }
        ItemStack stack1 = new ItemStack(block, 1, metaFromState);

        if (OreDictionary.getOreIDs(stack1).length < 1) {
            return true;
        }

        String name = OreDictionary.getOreName(OreDictionary.getOreIDs(stack1)[0]);
        List<String> lst = new ArrayList();
        for (int j = 0; j < 9; j++) {
            String l = "number_" + j;
            if (!nbt.getString(l).isEmpty()) {
                lst.add(nbt.getString(l));
            }

        }


        return !lst.isEmpty() && !lst.contains(name);
    }

    @Override
    public void getSubItems(final CreativeTabs subs, final NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(subs)) {
            ItemStack stack = new ItemStack(this, 1);

            Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
            if (this.efficienty != 0) {
                enchantmentMap.put(Enchantments.EFFICIENCY, this.efficienty);
            }
            if (this.lucky != 0) {
                enchantmentMap.put(Enchantments.FORTUNE, this.lucky);
            }
            EnchantmentHelper.setEnchantments(enchantmentMap, stack);

            ElectricItem.manager.charge(stack, 2.147483647E9D, 2147483647, true, false);
            items.add(stack);
            ItemStack itemstack = new ItemStack(this, 1, getMaxDamage());

            EnchantmentHelper.setEnchantments(enchantmentMap, itemstack);
            items.add(itemstack);
        }
    }

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name, String extraName) {
        StringBuilder loc = new StringBuilder();
        loc.append(Constants.MOD_ID);
        loc.append(':');
        loc.append("energy_tools").append("/").append(name + extraName);

        return new ModelResourceLocation(loc.toString(), null);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            final NBTTagCompound nbt = ModUtils.nbt(stack);

            return getModelLocation1(name, nbt.getString("mode"));
        });
        String[] mode = {"", "Zelen", "Demon", "Dark", "Cold", "Ender"};
        for (final String s : mode) {

            ModelBakery.registerItemVariants(this, getModelLocation1(name, s));

        }

    }

}
