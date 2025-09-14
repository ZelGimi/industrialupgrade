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
import com.denfop.containermenu.ContainerMenuWaterSecurity;
import com.denfop.datagen.IULootTableProvider;
import com.denfop.inventory.Inventory;
import com.denfop.mixin.access.LootTableAccessor;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenVolcanoChest;
import com.denfop.screen.ScreenWaterSecurity;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.LinkedList;
import java.util.List;

public class BlockEntityVolcanoChest extends BlockEntityInventory {

    public final Inventory invSlot;

    public BlockEntityVolcanoChest(BlockPos pos, BlockState state) {
        super(BlockVolcanoChest.volcano_chest,pos,state);
        this.invSlot = new Inventory(this, Inventory.TypeItemSlot.INPUT_OUTPUT, 27);

    }

    public List<ItemStack> generateLoot() {
        List<ItemStack> stacks = new LinkedList<>();
        LootContext.Builder lootcontext$builder;
        lootcontext$builder = (new LootContext.Builder((ServerLevel) this.level)).withParameter(LootContextParams.ORIGIN, new Vec3(pos.getX(), pos.getY(), pos.getZ()));
        final LootContext context = lootcontext$builder.create(LootContextParamSets.CHEST);
        if (IUCore.VOLCANO_LOOT_POOL == null){
            IUCore.VOLCANO_TABLE =level.getServer().getLootTables().get(IULootTableProvider.VOLCANO_LOOT_TABLE);
            IUCore.VOLCANO_LOOT_POOL =  ((LootTableAccessor) IUCore.VOLCANO_TABLE).getPools();
        }
        for (int i = 0; i < 8 ; i++)
        IUCore.VOLCANO_LOOT_POOL.get(0).addRandomItems(stacks::add, context);
        return stacks;
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenVolcanoChest((ContainerMenuVolcanoChest)menu);
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
