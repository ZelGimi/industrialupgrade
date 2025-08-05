package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCombinerSolid;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerCombinerSolidMatter;
import com.denfop.gui.GuiCombinerSolidMatter;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlotSolidMatter;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.solidmatter.EnumSolidMatter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityCombinerSolidMatter extends TileEntityInventory implements IUpgradableBlock {
    private static final List<AABB> aabbs = Collections.singletonList(new AABB(
            -0.5625,
            0.0D,
            -0.5625,
            1.5625,
            1.5D,
            1.5625
    ));
    public final InvSlotSolidMatter inputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotOutput outputSlot;
    public final Energy energy;
    public EnumSolidMatter[] solid;
    public int[] solid_col;

    public TileEntityCombinerSolidMatter(BlockPos pos, BlockState state) {
        super(BlockCombinerSolid.combiner_solid_matter, pos, state);
        this.inputSlot = new InvSlotSolidMatter(this);

        this.outputSlot = new InvSlotOutput(this, 9);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);

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
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiCombinerSolidMatter((ContainerCombinerSolidMatter) menu);
    }

    public ContainerCombinerSolidMatter getGuiContainer(Player entityPlayer) {
        return new ContainerCombinerSolidMatter(entityPlayer, this);
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
    public IMultiTileBlock getTeBlock() {
        return BlockCombinerSolid.combiner_solid_matter;
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Transformer, UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
        );
    }
}
