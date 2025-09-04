package com.denfop.blockentity.base;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blockentity.solidmatter.EnumSolidMatter;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCombinerSolidEntity;
import com.denfop.componets.Energy;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuCombinerSolidMatter;
import com.denfop.inventory.InventorySolidMatter;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.screen.ScreenCombinerSolidMatter;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityCombinerSolidMatter extends BlockEntityInventory implements BlockEntityUpgrade {
    private static final List<AABB> aabbs = Collections.singletonList(new AABB(
            -0.5625,
            0.0D,
            -0.5625,
            1.5625,
            1.5D,
            1.5625
    ));
    public final InventorySolidMatter inputSlot;
    public final InventoryUpgrade upgradeSlot;
    public final InventoryOutput outputSlot;
    public final Energy energy;
    public EnumSolidMatter[] solid;
    public int[] solid_col;

    public BlockEntityCombinerSolidMatter(BlockPos pos, BlockState state) {
        super(BlockCombinerSolidEntity.combiner_solid_matter, pos, state);
        this.inputSlot = new InventorySolidMatter(this);

        this.outputSlot = new InventoryOutput(this, 9);
        this.upgradeSlot = new InventoryUpgrade(this, 4);

        this.energy = this.addComponent(Energy.asBasicSink(this, 0, 14));
        this.solid = new EnumSolidMatter[9];
        this.solid_col = new int[9];
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        if (this.getComp(Energy.class) != null) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }

    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenCombinerSolidMatter((ContainerMenuCombinerSolidMatter) menu);
    }

    public ContainerMenuCombinerSolidMatter getGuiContainer(Player entityPlayer) {
        return new ContainerMenuCombinerSolidMatter(entityPlayer, this);
    }

    public void onLoaded() {
        super.onLoaded();
        this.inputSlot.update();

    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.energy.getCapacity() > 0 && this.energy.getEnergy() == this.energy.getCapacity()) {
            boolean need = false;
            for (int i = 0; i < this.solid_col.length; i++) {
                if (this.solid_col[i] == 0) {
                    continue;
                }
                ItemStack stack = this.solid[i].stack.copy();
                stack.setCount(this.solid_col[i]);
                if (this.outputSlot.add(stack)) {
                    need = true;
                }
            }
            if (need) {
                this.energy.useEnergy(this.energy.getEnergy());
            }
        }
        this.upgradeSlot.tickNoMark();
    }

    public List<AABB> getAabbs(boolean forCollision) {
        return aabbs;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.combinersolidmatter.getBlock();
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockCombinerSolidEntity.combiner_solid_matter;
    }

    @Override
    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(
                EnumBlockEntityUpgrade.Transformer, EnumBlockEntityUpgrade.ItemExtract,
                EnumBlockEntityUpgrade.ItemInput
        );
    }
}
