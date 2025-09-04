package com.denfop.blockentity.base;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuDigger;
import com.denfop.inventory.InventoryDigger;
import com.denfop.inventory.InventoryInput;
import com.denfop.screen.ScreenDigger;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class BlockEntityAutoDigger extends BlockEntityInventory {

    public final InventoryOutput outputSlot;
    public final Energy energy;
    public final InventoryInput inputslot;
    public final InventoryDigger slot_upgrade;
    public boolean mac_enabled = false;
    public boolean comb_mac_enabled = false;
    public boolean furnace;
    public int chance;
    public int col;
    public BaseMachineRecipe[] baseMachineRecipe = new BaseMachineRecipe[16];
    public double consume;
    public double energyconsume;

    public BlockEntityAutoDigger(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.auto_digger, pos, state);
        this.chance = 0;
        this.col = 1;
        this.furnace = false;
        this.outputSlot = new InventoryOutput(this, 48);
        this.energy = this.addComponent(Energy.asBasicSink(this, 500000, 14));
        this.inputslot = new InventoryInput(this, 16);

        this.energyconsume = 500;
        this.consume = 500;
        this.slot_upgrade = new InventoryDigger(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.5));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.5));

    }


    public void addInformation(ItemStack stack, List<String> tooltip) {
        tooltip.add(Localization.translate("iu.excavator.info"));
        tooltip.add(Localization.translate("iu.excavator.info1"));

        super.addInformation(stack, tooltip);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.auto_digger;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.slot_upgrade.update();
        this.inputslot.update();

    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        for (int k = 0; k < this.col; k++) {
            for (int i = 0; i < this.inputslot.size(); i++) {
                final BaseMachineRecipe baseMachineRecipe1 = baseMachineRecipe[i];
                if (baseMachineRecipe1 == null) {
                    continue;
                }
                if (this.energy.canUseEnergy(this.consume) && this.outputSlot.canAdd(baseMachineRecipe1.getOutput().items)) {
                    this.energy.useEnergy(this.consume);
                    for (ItemStack stack : baseMachineRecipe1.getOutput().items) {
                        this.outputSlot.add(stack);
                    }
                    this.inputslot.get(i).shrink(1);
                    if (this.inputslot.get(i).isEmpty()) {
                        baseMachineRecipe[i] = null;
                    }
                }
            }
        }
        if (this.level.getGameTime() % 20 == 0 && !this.outputSlot.isEmpty()) {
            ModUtils.tick(this.outputSlot, this);
        }
    }

    public void setBaseMachineRecipe(int slotid, final BaseMachineRecipe baseMachineRecipe) {
        this.baseMachineRecipe[slotid] = baseMachineRecipe;
    }


    @Override
    public ContainerMenuDigger getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuDigger(this, entityPlayer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenDigger((ContainerMenuDigger) menu);
    }


}
