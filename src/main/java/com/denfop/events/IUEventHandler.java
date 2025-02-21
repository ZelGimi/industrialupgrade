package com.denfop.events;


import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IItemSoon;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.pollution.ChunkLevel;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.space.rovers.api.IRoversItem;
import com.denfop.api.space.upgrades.info.SpaceUpgradeItemInform;
import com.denfop.api.space.upgrades.SpaceUpgradeSystem;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeItemInform;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.blocks.BlockIUFluid;
import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerBags;
import com.denfop.container.ContainerLeadBox;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.armour.ItemAdvJetpack;
import com.denfop.items.armour.special.EnumCapability;
import com.denfop.items.armour.special.ItemSpecialArmor;
import com.denfop.items.bags.ItemEnergyBags;
import com.denfop.items.bags.ItemLeadBox;
import com.denfop.items.block.ItemBlockIU;
import com.denfop.items.modules.EnumBaseType;
import com.denfop.items.modules.EnumModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.items.modules.ItemEntityModule;
import com.denfop.items.reactors.IRadioactiveItemType;
import com.denfop.items.resource.ItemNuclearResource;
import com.denfop.network.WorldData;
import com.denfop.network.packet.PacketColorPickerAllLoggIn;
import com.denfop.network.packet.PacketRadiationUpdateValue;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.lightning_rod.IBase;
import com.denfop.tiles.lightning_rod.IController;
import com.denfop.tiles.lightning_rod.TileEntityLightningRodController;
import com.denfop.tiles.mechanism.TileEntityPalletGenerator;
import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
import com.denfop.utils.CapturedMobUtils;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.denfop.tiles.lightning_rod.TileEntityLightningRodController.controllerMap;

public class IUEventHandler {

    final TextFormatting[] name = {TextFormatting.DARK_PURPLE, TextFormatting.YELLOW, TextFormatting.BLUE,
            TextFormatting.RED, TextFormatting.GRAY, TextFormatting.GREEN, TextFormatting.DARK_AQUA, TextFormatting.AQUA};
    final String[] mattertype = {"matter.name", "sun_matter.name", "aqua_matter.name", "nether_matter.name", "night_matter.name", "earth_matter.name", "end_matter.name", "aer_matter.name"};
    Tuple<ItemStack, BaseMachineRecipe> tupleRecipe;
    Tuple<ItemStack, BaseMachineRecipe> tupleReplicatorRecipe;
    public IUEventHandler() {

    }

    public static boolean getUpgradeItem(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof IUpgradeItem;

    }


    @SubscribeEvent
    public void onWorldTick1(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.WorldTickEvent.Phase.END) {
            World world = event.world;


            if (!world.isThundering() || world.isRemote || world.getWorldTime() % 20 != 0) {
                return;
            }

            if (WorldBaseGen.random.nextInt(100) < 2) {

                for (Map.Entry<BlockPos, IController> entry : controllerMap.entrySet()) {
                    IController controller = entry.getValue();

                    if (controller.isFull() && !controller.getTimer().isCanWork()) {
                        BlockPos antennaPos = controller.getBlockAntennaPos();
                        EntityLightningBolt lightning = new EntityLightningBolt(world,
                                antennaPos.getX(), antennaPos.getY(), antennaPos.getZ(), true
                        );
                        world.addWeatherEffect(lightning);
                        controller.getTimer().setCanWork(true);
                        controller.getTimer().resetTime();
                        controller.getEnergy().addEnergy(500000);
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            NBTTagCompound nbt = player.getEntityData();
            nbt.setDouble("radiation", 0.0);
        }
    }

    public static List<EntityItem> entityItemList = new LinkedList<>();
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            World world = event.world;
            for (Entity entity : (new  ArrayList<>(world.loadedEntityList))) {
                if (entity instanceof EntityItem) {
                    EntityItem itemEntity = (EntityItem) entity;
                    if (itemEntity.isDead)
                        continue;
                    ItemStack stack = itemEntity.getItem();

                    if (world.getBlockState(itemEntity.getPosition()).getBlock() == Blocks.WATER) {
                        EntityItem entityItem = (checkAndTransform(world, itemEntity));
                        if (entityItem != null)
                            entityItemList.add(entityItem);
                    }
                }
            }
            entityItemList.forEach(world::spawnEntity);
        }
    }
    private EntityItem checkAndTransform(World world, EntityItem entityItem) {
        List<EntityItem> nearbyItems = world.getEntitiesWithinAABB(EntityItem.class,
                new AxisAlignedBB(entityItem.posX - 1, entityItem.posY - 1, entityItem.posZ - 1,
                        entityItem.posX + 1, entityItem.posY + 1, entityItem.posZ + 1));

        int redstoneNeeded = 4;
        int poloniumNeeded = 1;
        int electrumNeeded = 1;

        List<EntityItem> redstoneItems = new LinkedList<>();
        List<EntityItem> poloniumItems = new LinkedList<>();
        List<EntityItem> electrumItems = new LinkedList<>();


        for (EntityItem item : nearbyItems) {
            ItemStack stack = item.getItem();

            if (stack.getItem() == Items.REDSTONE && redstoneNeeded > 0) {
                redstoneItems.add(item);
                redstoneNeeded -= stack.getCount();
            } else if (stack.isItemEqual(new ItemStack(IUItem.iudust, 1, 55)) && poloniumNeeded > 0) {
                poloniumItems.add(item);
                poloniumNeeded -= stack.getCount();
            } else if (stack.isItemEqual(new ItemStack(IUItem.iuingot, 1, 13)) && electrumNeeded > 0) {
                electrumItems.add(item);
                electrumNeeded -= stack.getCount();
            }
        }


        if (redstoneNeeded <= 0 && poloniumNeeded <= 0 && electrumNeeded <= 0) {
            int remainingRedstone = 4;
            for (EntityItem item : redstoneItems) {
                ItemStack stack = item.getItem();
                if (stack.getCount() <= remainingRedstone) {
                    remainingRedstone -= stack.getCount();
                    item.setDead();
                } else {
                    stack.shrink(remainingRedstone);
                    break;
                }
            }


            int remainingPolonium = 1;
            for (EntityItem item : poloniumItems) {
                ItemStack stack = item.getItem();
                if (stack.getCount() <= remainingPolonium) {
                    remainingPolonium -= stack.getCount();
                    item.setDead();
                } else {
                    stack.shrink(remainingPolonium);
                    break;
                }
            }


            int remainingElectrum = 1;
            for (EntityItem item : electrumItems) {
                ItemStack stack = item.getItem();
                if (stack.getCount() <= remainingElectrum) {
                    remainingElectrum -= stack.getCount();
                    item.setDead();
                } else {
                    stack.shrink(remainingElectrum);
                    break;
                }
            }


            ItemStack chargedRedstone = new ItemStack(IUItem.charged_redstone);
            return new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ, chargedRedstone);
        }

        return null;
    }

    @SubscribeEvent
    public void onBlockHarvested(BlockEvent.HarvestDropsEvent event) {

        if (event.getState().getBlock() == Blocks.GOLD_ORE) {
            if(event.getHarvester() != null && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH,
                    event.getHarvester().getHeldItem(EnumHand.MAIN_HAND)) > 0){

            }else {
                event.getDrops().clear();

                event.getDrops().add(new ItemStack(IUItem.rawMetals, Math.min(4,
                        1 + event.getWorld().rand.nextInt(Math.min(4,Math.max(1, event.getFortuneLevel()))))
                        , 17));
                event.setDropChance(1 + event.getFortuneLevel());
            }
        } else if (event.getState().getBlock() == Blocks.IRON_ORE) {
            if(event.getHarvester() != null && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH,
                    event.getHarvester().getHeldItem(EnumHand.MAIN_HAND)) > 0){

            }else {
                event.getDrops().clear();
                event.getDrops().add(new ItemStack(IUItem.rawMetals, Math.min(4,
                        1 + event.getWorld().rand.nextInt(Math.min(4,Math.max(1, event.getFortuneLevel()))))
                        , 18));
                event.setDropChance( 1 + event.getFortuneLevel());
            }
        }
    }
    @SubscribeEvent
    public void onCropGrowPre1(BlockEvent.CropGrowEvent.Pre event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos().down();
        IBlockState state = world.getBlockState(pos);


        if (state.getBlock() == IUItem.humus) {
            if (world.rand.nextFloat() < 0.5f) {
                event.setResult(net.minecraftforge.fml.common.eventhandler.Event.Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    public void onCropGrowPre(BlockEvent.CropGrowEvent.Pre event) {
        BlockPos pos = event.getPos();
        ChunkPos chunkPos = new ChunkPos(pos);
        final ChunkLevel pollution = PollutionManager.pollutionManager.getChunkLevelSoil(chunkPos);
        if (pollution != null && pollution.getLevelPollution().ordinal() >= 4) {
            event.setResult(Event.Result.DENY);
        } else if (pollution != null && pollution.getLevelPollution().ordinal() >= 1) {
            if (pollution.getLevelPollution().ordinal() == 1) {
                if (WorldBaseGen.random.nextInt(100) < 25) {
                    event.setResult(Event.Result.DENY);
                }
            } else if (pollution.getLevelPollution().ordinal() == 2) {
                if (WorldBaseGen.random.nextInt(100) < 50) {
                    event.setResult(Event.Result.DENY);
                }
            } else {
                if (WorldBaseGen.random.nextInt(100) < 75) {
                    event.setResult(Event.Result.DENY);
                }
            }

        }
    }


    @SubscribeEvent
    public void loginPlayer(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player.getEntityWorld().isRemote) {
            return;
        }
        RadiationSystem.rad_system.update(event.player);
    }

    @SubscribeEvent
    public void workCutters(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote) {
            return;
        }
        ItemStack stack = event.getItemStack();

        if (stack.getItem() == IUItem.cutter) {

            TileEntity tile = event.getWorld().getTileEntity(event.getPos());
            if (tile instanceof TileEntityMultiCable) {
                TileEntityMultiCable cable = (TileEntityMultiCable) tile;
                ItemStack drop = cable.getPickBlock(event.getEntityPlayer(), null);
                if (!drop.isEmpty()) {
                    ModUtils.dropAsEntity(event.getWorld(), event.getPos(), drop);
                }
                cable.removeConductor();
            }
        } else if (!stack.isEmpty()) {
            TileEntity tile = event.getWorld().getTileEntity(event.getPos());
            if (tile instanceof TileEntityBlock && event.getEntityPlayer() != null && event.getEntityPlayer().isSneaking()) {
                ((TileEntityBlock) tile).onSneakingActivated(event.getEntityPlayer(), event.getHand(), event.getFace(),
                        (float) event.getHitVec().x, (float) event.getHitVec().y, (float) event.getHitVec().z
                );
            }

        }

    }

    @SubscribeEvent
    public void initiatePlayer(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player.getEntityWorld().isRemote) {
            return;
        }
        new PacketColorPickerAllLoggIn();
        new PacketRadiationUpdateValue(event.player, event.player.getEntityData().getDouble("radiation"));
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        WorldData.onWorldUnload(event.getWorld());
    }


    @SubscribeEvent
    public void bag_pickup(EntityItemPickupEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        try {
            boolean isContainerBags = !(player.openContainer instanceof ContainerBags);
            boolean isContainerBox = !(player.openContainer instanceof ContainerLeadBox);

            if (isContainerBags) {
                InventoryPlayer inventory = player.inventory;

                for (int i = 0; i < inventory.mainInventory.size(); ++i) {
                    ItemStack stack = inventory.mainInventory.get(i);
                    if (stack.getItem() instanceof ItemEnergyBags) {
                        if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.BAGS, stack)) {
                            ItemEnergyBags bags = (ItemEnergyBags) stack.getItem();
                            ItemStack stack2 = event.getItem().getItem();
                            if (!event.getItem().isDead && !(stack2.getItem() instanceof ItemEnergyBags)) {
                                if (bags.canInsert(player, stack, stack2)) {
                                    final ItemStack stack1 = stack2.copy();
                                    stack1.setCount(Math.min(stack1.getCount(), stack1.stackSize));
                                    bags.insert(player, stack, stack1);
                                    event.getItem().setItem(stack1);
                                    if (stack1.isEmpty()) {
                                        event.getItem().setDead();
                                        event.setCanceled(true);
                                    }
                                    return;
                                }
                            }
                        }
                    }
                }

            }
            if (isContainerBox) {
                InventoryPlayer inventory = player.inventory;
                for (int i = 0; i < inventory.mainInventory.size(); ++i) {
                    ItemStack stack = inventory.mainInventory.get(i);
                    if (stack.getItem() instanceof ItemLeadBox) {
                        ItemLeadBox bags = (ItemLeadBox) stack.getItem();
                        ItemStack stack2 = event.getItem().getItem();
                        if (!event.getItem().isDead && !(stack2.getItem() instanceof ItemLeadBox)) {
                            if (bags.canInsert(player, stack, stack2)) {
                                final ItemStack stack1 = stack2.copy();
                                stack1.setCount(Math.min(stack1.getCount(), stack1.stackSize));
                                bags.insert(player, stack, stack1);
                                event.getItem().setItem(stack1);
                                if (stack1.isEmpty()) {
                                    event.getItem().setDead();
                                    event.setCanceled(true);
                                }
                                return;
                            }
                        }

                    }
                }

            }
        } catch (NoClassDefFoundError ignored) {

        }
    }


    public void setFly(EntityPlayer player, boolean fly, ItemStack stack) {
        player.capabilities.isFlying = fly;
        player.capabilities.allowFlying = fly;
        player.getEntityData().setBoolean("isFlyActive", fly);
        if (player.getEntityWorld().isRemote && !fly) {
            player.capabilities.setFlySpeed((float) 0.05);
            player.fallDistance = 0;
        } else {
            boolean edit = player.getEntityData().getBoolean("edit_fly");
            if (!edit) {
                int flyspeed = (UpgradeSystem.system.hasModules(
                        EnumInfoUpgradeModules.FLYSPEED,
                        stack
                ) ?
                        UpgradeSystem.system.getModules(
                                EnumInfoUpgradeModules.FLYSPEED,
                                stack
                        ).number : 0);

                if (player.getEntityWorld().isRemote) {
                    player.capabilities.setFlySpeed((float) ((float) 0.1 + 0.05 * flyspeed));
                }
            } else {
                if (player.getEntityWorld().isRemote) {
                    player.capabilities.setFlySpeed(player.getEntityData().getFloat("fly_speed"));
                }
            }
        }
    }

    public boolean canFly(ItemStack stack) {
        return stack
                .getItem() == IUItem.spectral_chestplate ||
                (stack.getItem() instanceof ItemSpecialArmor && (((ItemSpecialArmor) stack.getItem())
                        .getListCapability()
                        .contains(EnumCapability.FLY) || ((ItemSpecialArmor) stack.getItem()).getListCapability().contains(
                        EnumCapability.JETPACK_FLY))) || (stack
                .getItem() instanceof ItemAdvJetpack && UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.FLY, stack));
    }

    @SubscribeEvent
    public void FlyUpdate(LivingEvent.LivingUpdateEvent event) {

        if (!(event.getEntityLiving() instanceof EntityPlayer) || event
                .getEntityLiving()
                .getEntityWorld().provider.getWorldTime() % 2 != 0) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        NBTTagCompound nbtData = player.getEntityData();
        if (!player.capabilities.isCreativeMode) {

            if (!player.inventory.armorInventory.get(2).isEmpty()) {
                if (canFly(player.inventory.armorInventory.get(2))) {
                    NBTTagCompound nbtData1 = ModUtils.nbt(player.inventory.armorInventory.get(2));
                    boolean jetpack = nbtData1.getBoolean("jetpack");
                    if (!jetpack) {
                        if (nbtData.getBoolean("isFlyActive")) {
                            nbtData.setBoolean("hasFly", true);
                            setFly(player, false, player.inventory.armorInventory
                                    .get(2));
                        }
                    } else {
                        if (!player.onGround) {
                            if (nbtData1.getBoolean("canFly")) {
                                setFly(player, true, player.inventory.armorInventory
                                        .get(2));
                                nbtData1.setBoolean("canFly", false);
                                nbtData.setBoolean("canjump", false);
                            }
                        } else {
                            if (nbtData.getBoolean("isFlyActive")) {
                                setFly(player, false, player.inventory.armorInventory
                                        .get(2));
                            }
                        }
                    }
                } else if (!player.inventory.armorInventory.get(2).isEmpty()) {
                    if (nbtData.getBoolean("isFlyActive")) {
                        setFly(player, false, player.inventory.armorInventory
                                .get(2));
                    }
                }
            } else {
                if (nbtData.getBoolean("isFlyActive")) {
                    setFly(player, false, player.inventory.armorInventory
                            .get(2));
                }
            }
        } else {
            if (nbtData.getBoolean("isFlyActive")) {
                setFly(player, false, player.inventory.armorInventory
                        .get(2));
            }
        }


    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void addInformItem(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        if (!(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }

        if (item instanceof IItemSoon) {
            event.getToolTip().add(((IItemSoon) item).getDescription());
        }

        if (item == IUItem.charged_redstone) {
            event.getToolTip().add(Localization.translate("charged_redstone.info"));
        }

        if (event.getItemStack().getItem() instanceof ItemBlock){
            ItemBlock block = (ItemBlock) event.getItemStack().getItem();
            final IBlockState state = block.getBlock().getStateFromMeta(event.getItemStack().getItemDamage());
            List<String> list = ModUtils.getInformationFromOre(state);
            if (!list.isEmpty()) {
                event.getToolTip().add(Localization.translate("veins_ores.info1"));
                event.getToolTip().add(Localization.translate("veins_ores.info"));
                event.getToolTip().addAll(list);
            }
        }
        if (item instanceof IRadioactiveItemType || item instanceof ItemNuclearResource) {
            event.getToolTip().add(Localization.translate("iu.radiation.warning"));
            event.getToolTip().add(Localization.translate("iu.radioprotector.info"));

        }

        if (item.equals(IUItem.tank.item)) {
            switch (stack.getItemDamage()) {
                case 1:
                    event.getToolTip().add(Localization.translate("iu.storage_fluid") + 160 + " B");
                    break;
                case 2:
                    event.getToolTip().add(Localization.translate("iu.storage_fluid") + 480 + " B");
                    break;
                case 3:
                    event.getToolTip().add(Localization.translate("iu.storage_fluid") + 2560 + " B");
                    break;
                case 0:
                    event.getToolTip().add(Localization.translate("iu.storage_fluid") + 40 + " B");
                    break;

            }
        }
        for (Map.Entry<ItemStack, Double> entry : TileEntityPalletGenerator.integerMap.entrySet()) {
            if (entry.getKey().isItemEqual(stack)) {
                event.getToolTip().add(Localization.translate("iu.pellets.info") + entry.getValue());
                event.getToolTip().add(Localization.translate("iu.pellets.info1"));
            }
        }
        if (item.equals(IUItem.module_quickly)) {
            event.getToolTip().add(Localization.translate("iu.info.module.speed"));
        }
        if (item.equals(IUItem.module_stack)) {
            event.getToolTip().add(Localization.translate("iu.info.module.stack"));
        }
        if (item.equals(IUItem.module_storage)) {
            event.getToolTip().add(Localization.translate("iu.info.module.sorting"));
        }
        if (item.equals(IUItem.crafting_elements) && stack.getItemDamage() >= 206 && stack.getItemDamage() <= 216) {
            int meta = stack.getItemDamage() - 205;
            event
                    .getToolTip()
                    .add(Localization.translate("iu.limiter.info9") + EnergyNetGlobal.instance.getPowerFromTier(meta) + " " +
                            "EF");
        }
        int meta = stack.getItemDamage();
        if ((item.equals(IUItem.basecircuit) && (meta == 9 || meta == 10 || meta == 11 || meta == 21)) || (item.equals(IUItem.crafting_elements) && (meta == 272 || meta == 273))) {
            final NBTTagCompound nbt = ModUtils.nbt(stack);
            final int level = nbt.getInteger("level");
            if (level != 0) {
                event.getToolTip().add(Localization.translate("circuit.level") + " " + level);
            }
        }

        if (item.equals(IUItem.module7) && stack.getItemDamage() == 9) {
            event.getToolTip().add(Localization.translate("module.wireless"));
        }
        if (item.equals(IUItem.upgrade_speed_creation) || item.equals(IUItem.autoheater) || item.equals(IUItem.coolupgrade) || item.equals(
                IUItem.module_quickly) || item.equals(
                IUItem.module_stack) || item.equals(IUItem.module_storage) || (item.equals(
                IUItem.module7) && (stack.getItemDamage() == 4 || stack.getItemDamage() == 10))) {
            event.getToolTip().add(Localization.translate("module.wireless"));
        }
        if (item.equals(IUItem.entitymodules) || stack.getItemDamage() == 4) {
            CapturedMobUtils capturedMobUtils = CapturedMobUtils.create(stack);
            if (capturedMobUtils != null) {
                Entity entity = Objects.requireNonNull(capturedMobUtils).getEntity(event.getEntity().getEntityWorld(), true);
                event.getToolTip().add(Objects.requireNonNull(entity).getName());
            }
        }
        if (item instanceof ItemBlockIU) {
            ItemBlockIU itemBlockTileEntity = (ItemBlockIU) item;
            if (itemBlockTileEntity.getBlock() instanceof BlockIUFluid) {
                BlockIUFluid blockFluid = (BlockIUFluid) itemBlockTileEntity.getBlock();
                if (blockFluid.getFluid() == FluidName.fluidgas.getInstance()) {
                    event.getToolTip().add(Localization.translate("find.gas_vein"));
                }
                if (blockFluid.getFluid() == FluidName.fluidneft.getInstance()) {
                    event.getToolTip().add(Localization.translate("find.oil_vein"));
                }

            }
        }
        if (item.equals(
                IUItem.module_quickly) || item.equals(
                IUItem.module_stack) || item.equals(IUItem.module_storage)) {
            event.getToolTip().add(Localization.translate("using_kit"));
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {

                event.getToolTip().add(ListInformationUtils.mechanism_info.get(ListInformationUtils.index));

            } else {
                for (String name : ListInformationUtils.mechanism_info) {
                    event.getToolTip().add(name);
                }
            }

        }
        if (item.equals(
                IUItem.coolupgrade)) {
            event.getToolTip().add(Localization.translate("using_kit"));
            final List<String> list = ListInformationUtils.integerListMap.get(stack.getItemDamage());
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                event.getToolTip().add(list.get(ListInformationUtils.index % list.size()));
            } else {
                for (String name : list) {
                    event.getToolTip().add(name);
                }
            }

        }
        if (item.equals(IUItem.autoheater)) {
            event.getToolTip().add(Localization.translate("using_kit"));

            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                event.getToolTip().add(ListInformationUtils.mechanism_info2.get(ListInformationUtils.index2));
            } else {
                for (String name : ListInformationUtils.mechanism_info2) {
                    event.getToolTip().add(name);
                }
            }
        }
        if (item.equals(
                IUItem.upgrade_speed_creation)) {
            event.getToolTip().add(Localization.translate("using_kit"));
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                event.getToolTip().add(new ArrayList<>(ListInformationUtils.mechanism_info1.values()).get(ListInformationUtils.index1));
            } else {
                for (String name : ListInformationUtils.mechanism_info1.values()) {
                    event.getToolTip().add(name);
                }
            }
        }
        if (stack.getItem().equals(IUItem.analyzermodule)) {
            event.getToolTip().add(Localization.translate("iu.analyzermodule"));
        }
        if (stack.getItem().equals(IUItem.quarrymodule)) {
            event.getToolTip().add(Localization.translate("iu.quarrymodule"));
        }
        if (stack.getItem().equals(IUItem.expmodule)) {
            event.getToolTip().add(Localization.translate("iu.expierence_module.info"));
        }

        if (stack.getItem() instanceof ItemEntityModule) {
            meta = stack.getItemDamage();
            if (meta == 0) {
                event.getToolTip().add(Localization.translate("iu.entitymodule"));
            }
            if (meta == 1) {
                event.getToolTip().add(Localization.translate("iu.entitymodule1"));
            }

        }

        if (stack.getItem() instanceof ItemBaseModules) {
            EnumModule module = EnumModule.getFromID(stack.getItemDamage());
            if (module.type.equals(EnumBaseType.PHASE)) {
                event.getToolTip().add(Localization.translate("iu.phasemodule"));

            }
            if (module.type.equals(EnumBaseType.MOON_LINSE)) {
                event.getToolTip().add(Localization.translate("iu.moonlinse"));

            }

        }
        if (getUpgradeItem(stack) && UpgradeSystem.system.hasInMap(stack)) {
            final List<UpgradeItemInform> lst = UpgradeSystem.system.getInformation(stack);
            final int col = UpgradeSystem.system.getRemaining(stack);
            if (!lst.isEmpty()) {
                for (UpgradeItemInform upgrade : lst) {
                    event.getToolTip().add(upgrade.getName());
                }
            }
            if (col != 0) {
                event.getToolTip().add(Localization.translate("free_slot") + col + Localization.translate(
                        "free_slot1"));
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    event.getToolTip().add(Localization.translate("iu.can_upgrade_item"));
                    IUpgradeItem iUpgradeItem = (IUpgradeItem) stack.getItem();
                    final List<String> list = UpgradeSystem.system.getAvailableUpgrade(iUpgradeItem, stack);
                    event.getToolTip().addAll(list);
                }
            } else {
                event.getToolTip().add(Localization.translate("not_free_slot"));

            }


        }
        if (stack.getItem() instanceof IRoversItem && SpaceUpgradeSystem.system.hasInMap(stack)) {
            final List<SpaceUpgradeItemInform> lst = SpaceUpgradeSystem.system.getInformation(stack);
            final int col = SpaceUpgradeSystem.system.getRemaining(stack);
            if (!lst.isEmpty()) {
                for (SpaceUpgradeItemInform upgrade : lst) {
                    event.getToolTip().add(upgrade.getName());
                }
            }
            if (col != 0) {
                event.getToolTip().add(Localization.translate("free_slot") + col + Localization.translate(
                        "free_slot1"));
             if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    event.getToolTip().add(Localization.translate("iu.can_upgrade_item"));
                    IRoversItem iUpgradeItem = (IRoversItem) stack.getItem();
                    final List<String> list = SpaceUpgradeSystem.system.getAvailableUpgrade(iUpgradeItem, stack);
                    event.getToolTip().addAll(list);
                }
            } else {
                event.getToolTip().add(Localization.translate("not_free_slot"));

            }


        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void addInfo(ItemTooltipEvent event) {

        ItemStack stack = event.getItemStack();

        if (tupleRecipe == null) {
            for (Map.Entry<ItemStack, BaseMachineRecipe> entry : IUItem.machineRecipe) {
                if (entry.getKey().isItemEqual(stack)) {
                    tupleRecipe = new Tuple<>(stack,entry.getValue());
                    if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        event.getToolTip().add(Localization.translate("press.lshift"));
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        event.getToolTip().add(Localization.translate("clonning"));

                        final RecipeOutput output1 = entry.getValue().output;
                        for (int i = 0; i < this.name.length; i++) {
                            if (output1.metadata.getDouble(("quantitysolid_" + i)) != 0) {
                                event
                                        .getToolTip()
                                        .add(name[i] + Localization.translate(mattertype[i]) + ": " + output1.metadata.getDouble(
                                                ("quantitysolid_" + i)) + Localization.translate("matternumber"));
                            }
                        }
                    }
                    break;
                }
            }
            if (tupleRecipe == null){
                tupleRecipe = new Tuple<>(stack,null);
            }
        }else{
            if (tupleRecipe.getFirst().isItemEqual(stack)){
                if (tupleRecipe.getSecond() != null) {
                    if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        event.getToolTip().add(Localization.translate("press.lshift"));
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        event.getToolTip().add(Localization.translate("clonning"));

                        final RecipeOutput output1 = tupleRecipe.getSecond().output;
                        for (int i = 0; i < this.name.length; i++) {
                            if (output1.metadata.getDouble(("quantitysolid_" + i)) != 0) {
                                event
                                        .getToolTip()
                                        .add(name[i] + Localization.translate(mattertype[i]) + ": " + output1.metadata.getDouble(
                                                ("quantitysolid_" + i)) + Localization.translate("matternumber"));
                            }
                        }
                    }
                }
            }else{
                boolean find = false;
                for (Map.Entry<ItemStack, BaseMachineRecipe> entry : IUItem.machineRecipe) {
                    if (entry.getKey().isItemEqual(stack)) {
                        tupleRecipe = new Tuple<>(stack,entry.getValue());
                        find = true;
                        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                            event.getToolTip().add(Localization.translate("press.lshift"));
                        }
                        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                            event.getToolTip().add(Localization.translate("clonning"));

                            final RecipeOutput output1 = entry.getValue().output;
                            for (int i = 0; i < this.name.length; i++) {
                                if (output1.metadata.getDouble(("quantitysolid_" + i)) != 0) {
                                    event
                                            .getToolTip()
                                            .add(name[i] + Localization.translate(mattertype[i]) + ": " + output1.metadata.getDouble(
                                                    ("quantitysolid_" + i)) + Localization.translate("matternumber"));
                                }
                            }
                        }
                        break;
                    }
                }
                if (!find){
                    tupleRecipe = new Tuple<>(stack,null);
                }
            }
        }
        if (tupleReplicatorRecipe == null) {
            for (Map.Entry<ItemStack, BaseMachineRecipe> entry : IUItem.fluidMatterRecipe) {
                if (entry.getKey().isItemEqual(stack)) {
                    if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        if (!event.getToolTip().contains(Localization.translate("press.lshift"))) {
                            event.getToolTip().add(Localization.translate("press.lshift"));
                        }
                    }
                    tupleReplicatorRecipe = new Tuple<>(stack,entry.getValue());
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {

                        final RecipeOutput output1 = entry.getValue().output;
                        final double matter = output1.metadata.getDouble("matter");
                        String usingMatter = ModUtils.getStringBukket(matter) + Localization.translate(Constants.ABBREVIATION +
                                ".generic.text.bucketUnit");
                        event
                                .getToolTip()
                                .add(Localization.translate(Constants.ABBREVIATION + ".replicator_using_matter") + TextFormatting.DARK_PURPLE + usingMatter);

                    }
                    break;
                }
            }
            if (tupleReplicatorRecipe == null){
                tupleReplicatorRecipe = new Tuple<>(stack,null);
            }
        }else{
            if (tupleReplicatorRecipe.getFirst().isItemEqual(stack)) {
                if (tupleReplicatorRecipe.getSecond() != null) {
                    if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        if (!event.getToolTip().contains(Localization.translate("press.lshift"))) {
                            event.getToolTip().add(Localization.translate("press.lshift"));
                        }
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {

                        final RecipeOutput output1 = tupleReplicatorRecipe.getSecond().output;
                        final double matter = output1.metadata.getDouble("matter");
                        String usingMatter = ModUtils.getStringBukket(matter) + Localization.translate(Constants.ABBREVIATION +
                                ".generic.text.bucketUnit");
                        event
                                .getToolTip()
                                .add(Localization.translate(Constants.ABBREVIATION + ".replicator_using_matter") + TextFormatting.DARK_PURPLE + usingMatter);

                    }
                }
            }else{
                tupleReplicatorRecipe = null;
                for (Map.Entry<ItemStack, BaseMachineRecipe> entry : IUItem.fluidMatterRecipe) {
                    if (entry.getKey().isItemEqual(stack)) {
                        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                            if (!event.getToolTip().contains(Localization.translate("press.lshift"))) {
                                event.getToolTip().add(Localization.translate("press.lshift"));
                            }
                        }
                        tupleReplicatorRecipe = new Tuple<>(stack,entry.getValue());
                        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {

                            final RecipeOutput output1 = entry.getValue().output;
                            final double matter = output1.metadata.getDouble("matter");
                            String usingMatter = ModUtils.getStringBukket(matter) + Localization.translate(Constants.ABBREVIATION +
                                    ".generic.text.bucketUnit");
                            event
                                    .getToolTip()
                                    .add(Localization.translate(Constants.ABBREVIATION + ".replicator_using_matter") + TextFormatting.DARK_PURPLE + usingMatter);

                        }
                        break;
                    }
                }
                if (tupleReplicatorRecipe == null){
                    tupleReplicatorRecipe = new Tuple<>(stack,null);
                }
            }
        }
    }

}
