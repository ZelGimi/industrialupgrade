package com.denfop.blockentity.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blockentity.base.FakePlayerSpawner;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockVolcanoChest;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuVolcanoChest;
import com.denfop.datagen.IULootTableProvider;
import com.denfop.inventory.Inventory;
import com.denfop.items.resource.ItemIngots;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenVolcanoChest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.LinkedList;
import java.util.List;

public class BlockEntityVolcanoChest extends BlockEntityInventory {

    public final Inventory invSlot;

    public BlockEntityVolcanoChest(BlockPos pos, BlockState state) {
        super(BlockVolcanoChest.volcano_chest, pos, state);
        this.invSlot = new Inventory(this, Inventory.TypeItemSlot.INPUT_OUTPUT, 27);

    }

    public List<ItemStack> generateLoot() {
        List<ItemStack> stacks = new LinkedList<>();
        LootParams.Builder lootcontext$builder;
        lootcontext$builder = (new LootParams.Builder((ServerLevel) this.level)).withParameter(LootContextParams.ORIGIN, new Vec3(pos.getX(), pos.getY(), pos.getZ()));
        final LootParams context = lootcontext$builder.create(LootContextParamSets.CHEST);
        if (IUCore.VOLCANO_TABLE == null) {
            IUCore.VOLCANO_TABLE =getVolcano().build();
        }
        for (int i = 0; i < 8; i++)
            IUCore.VOLCANO_TABLE.getRandomItems(context, stacks::add);
        return stacks;
    }
    public LootPoolSingletonContainer.Builder<?> addItem(ItemStack stack, int weight, int min, int max) {
        return LootItem.lootTableItem(stack.getItem()).setWeight(weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
    }
    public LootTable.Builder getVolcano() {
        return LootTable.lootTable().withPool(LootPool.lootPool().name("main").setRolls(UniformGenerator.between(1.0F, 3.0F))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.mikhail_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.aluminium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.vanadium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.tungsten_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.invar_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.caravky_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.cobalt_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.magnesium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.nickel_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.platinum_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.titanium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.chromium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.spinel_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.electrum_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.silver_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.zinc_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.manganese_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.iridium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.germanium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.alloy_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.bronze_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.lead_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.steel_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.osmium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.tantalum.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.cadmium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.arsenic.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.barium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.bismuth.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.gadolinium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.gallium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.hafnium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.yttrium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.molybdenum.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.neodymium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.niobium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.palladium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.polonium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.thallium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.zirconium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.tin_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(Items.DIAMOND, 1), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(Items.IRON_INGOT, 1), 8, 1, 4))
                .add(addItem(ItemStackHelper.fromData(Items.COPPER_INGOT, 1), 8, 2, 6))
                .add(addItem(ItemStackHelper.fromData(Items.GOLD_INGOT, 1), 7, 1, 3))
                .add(addItem(ItemStackHelper.fromData(Items.LAPIS_LAZULI, 1), 8, 1, 4))
                .add(addItem(ItemStackHelper.fromData(Items.ENDER_PEARL, 1), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(Items.LEATHER, 1), 6, 1, 6))
                .add(addItem(ItemStackHelper.fromData(Items.BLAZE_ROD, 1), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(Items.EMERALD, 1), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(IUItem.ForgeHammer, 1), 6, 1, 1))
                .add(addItem(ItemStackHelper.fromData(IUItem.plast, 1), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(IUItem.plastic_plate, 1), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,319), 6, 1, 3))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,297), 7, 1, 3))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,286), 6, 1, 3))
                .add(addItem(ItemStackHelper.fromData(IUItem.basecircuit, 1,13), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(IUItem.basecircuit, 1,14), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,290), 6, 1, 4))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,274), 6, 1, 4))
                .add(addItem(ItemStackHelper.fromData(IUItem.cutter, 1), 6, 1, 1))
                .add(addItem(ItemStackHelper.fromData(IUItem.treetap, 1), 6, 1, 1))
                .add(addItem(ItemStackHelper.fromData(IUItem.wrench, 1), 6, 1, 1))
                .add(addItem(ItemStackHelper.fromData(IUItem.basecircuit, 1,12), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(IUItem.basecircuit, 1,15), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(IUItem.itemiu, 1,2), 6, 1, 4))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,271), 6, 1, 4))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,280), 6, 1, 4))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,276), 6, 1, 1))
                .add(addItem(ItemStackHelper.fromData(Items.REDSTONE, 1), 8, 1, 4))
                .add(addItem(ItemStackHelper.fromData(IUItem.basecircuit, 1,17), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(IUItem.basecircuit, 1,16), 6, 1, 2))
                .add(EmptyLootItem.emptyItem().setWeight(40))

        );
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenVolcanoChest((ContainerMenuVolcanoChest) menu);
    }


    @Override
    public ContainerMenuVolcanoChest getGuiContainer(Player var1) {
        return new ContainerMenuVolcanoChest(this, var1);
    }


    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);
        if (this.getWorld().isClientSide) {
            return;
        }
        List<ItemStack> stacks = generateLoot();
        if (placer instanceof FakePlayerSpawner)
        for (ItemStack stack1 : stacks) {
            int index;
            do {
                index = level.random.nextInt(27);
            } while (!this.invSlot.get(index).isEmpty());
            this.invSlot.set(index, stack1);
        }
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.volcanoChest.getBlock(0);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockVolcanoChest.volcano_chest;
    }

}
