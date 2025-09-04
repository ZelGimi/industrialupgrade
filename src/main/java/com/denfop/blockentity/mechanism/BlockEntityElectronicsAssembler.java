package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.*;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.*;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuElectricElectronicsAssembler;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenElectricElectronicsAssembler;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityElectronicsAssembler extends BlockEntityElectricMachine implements IUpdateTick, IHasRecipe, BlockEntityUpgrade {


    public final Inventory input_slot;

    public final InventoryUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    private final ComponentUpgrade componentUpgrades;


    public InventoryRecipes inputSlotA;
    public MachineRecipe output;

    public BlockEntityElectronicsAssembler(BlockPos pos, BlockState state) {
        super(300, 1, 1, BlockBaseMachine3Entity.electronic_assembler, pos, state);
        this.inputSlotA = new InventoryRecipes(this, "electronics", this);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.output = null;

        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, 300, 1));
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));

        Recipes.recipes.addInitRecipes(this);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
        this.input_slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((BlockEntityElectronicsAssembler) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((BlockEntityElectronicsAssembler) this.base).inputSlotA.changeAccepts(this.get(0));
                }
                return content;
            }

            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.recipe_schedule.getItem();
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }
        };

    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    public static void addRecipe(
            ItemStack container, ItemStack container1, ItemStack container2,
            ItemStack container3, ItemStack container4, ItemStack output
    ) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "electronics",
                new BaseMachineRecipe(
                        new Input(input.getInput(container), input.getInput(container1), input.getInput(container2)
                                , input.getInput(container3), input.getInput(container4)),
                        new RecipeOutput(null, output)
                )
        );


    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get(0));
            }
            inputSlotA.load();
            this.getOutput();
        }
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            guiChargeLevel = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getEnergyConsume() + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getOperationsPerTick());

        }
        super.addInformation(stack, tooltip);
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.genmirc.getSoundEvent();
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();


        return this.output;
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, guiChargeLevel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public ContainerMenuElectricElectronicsAssembler getGuiContainer(Player entityPlayer) {
        return new ContainerMenuElectricElectronicsAssembler(
                entityPlayer, this);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.electronic_assembler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    public void init() {
        addRecipe(
                new ItemStack(IUItem.crafting_elements.getStack(578), 2),
                new ItemStack(IUItem.crafting_elements.getStack(593)),
                new ItemStack(IUItem.crafting_elements.getStack(570)),
                new ItemStack(IUItem.crafting_elements.getStack(571)),
                new ItemStack(IUItem.crafting_elements.getStack(572)),
                new ItemStack(IUItem.crafting_elements.getStack(539))
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements.getStack(578), 2),
                new ItemStack(IUItem.crafting_elements.getStack(593)),
                new ItemStack(IUItem.crafting_elements.getStack(573)),
                new ItemStack(IUItem.crafting_elements.getStack(562)),
                new ItemStack(IUItem.crafting_elements.getStack(572)),
                new ItemStack(IUItem.crafting_elements.getStack(547))
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements.getStack(578), 2),
                new ItemStack(IUItem.crafting_elements.getStack(593)),
                new ItemStack(IUItem.crafting_elements.getStack(573)),
                new ItemStack(IUItem.crafting_elements.getStack(595)),
                new ItemStack(IUItem.crafting_elements.getStack(577)),
                new ItemStack(IUItem.crafting_elements.getStack(535))
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements.getStack(578), 2),
                new ItemStack(IUItem.crafting_elements.getStack(593)),
                new ItemStack(IUItem.crafting_elements.getStack(590)),
                new ItemStack(IUItem.crafting_elements.getStack(595)),
                new ItemStack(IUItem.crafting_elements.getStack(577)),
                new ItemStack(IUItem.crafting_elements.getStack(544))
        );

        addRecipe(
                new ItemStack(IUItem.crafting_elements.getStack(578), 2),
                new ItemStack(IUItem.crafting_elements.getStack(593)),
                new ItemStack(IUItem.crafting_elements.getStack(590)),
                new ItemStack(IUItem.crafting_elements.getStack(595)),
                new ItemStack(IUItem.crafting_elements.getStack(575)),
                new ItemStack(IUItem.crafting_elements.getStack(553))
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements.getStack(578), 2),
                new ItemStack(IUItem.crafting_elements.getStack(593)),
                new ItemStack(IUItem.crafting_elements.getStack(559)),
                new ItemStack(IUItem.crafting_elements.getStack(595)),
                new ItemStack(IUItem.crafting_elements.getStack(575)),
                new ItemStack(IUItem.crafting_elements.getStack(550))
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements.getStack(578), 2),
                new ItemStack(IUItem.crafting_elements.getStack(593)),
                new ItemStack(IUItem.crafting_elements.getStack(559)),
                new ItemStack(IUItem.crafting_elements.getStack(586)),
                new ItemStack(IUItem.crafting_elements.getStack(575)),
                new ItemStack(IUItem.crafting_elements.getStack(537))
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements.getStack(578), 2),
                new ItemStack(IUItem.crafting_elements.getStack(593)),
                new ItemStack(IUItem.crafting_elements.getStack(559)),
                new ItemStack(IUItem.crafting_elements.getStack(586)),
                new ItemStack(IUItem.crafting_elements.getStack(587)),
                new ItemStack(IUItem.crafting_elements.getStack(546))
        );


        addRecipe(
                new ItemStack(IUItem.crafting_elements.getStack(580)),
                new ItemStack(IUItem.crafting_elements.getStack(576)),
                new ItemStack(IUItem.crafting_elements.getStack(569)),
                new ItemStack(IUItem.basecircuit.getStack(15)),
                new ItemStack(IUItem.crafting_elements.getStack(574)),
                new ItemStack(IUItem.crafting_elements.getStack(538))
        );

        addRecipe(
                new ItemStack(IUItem.crafting_elements.getStack(560)),
                new ItemStack(IUItem.crafting_elements.getStack(576)),
                new ItemStack(IUItem.crafting_elements.getStack(569)),
                new ItemStack(IUItem.basecircuit.getStack(12)),
                new ItemStack(IUItem.crafting_elements.getStack(574)),
                new ItemStack(IUItem.crafting_elements.getStack(557))
        );

        addRecipe(
                new ItemStack(IUItem.crafting_elements.getStack(560)),
                new ItemStack(IUItem.crafting_elements.getStack(576)),
                new ItemStack(IUItem.crafting_elements.getStack(600)),
                new ItemStack(IUItem.basecircuit.getStack(12)),
                new ItemStack(IUItem.crafting_elements.getStack(574)),
                new ItemStack(IUItem.crafting_elements.getStack(552))
        );

        addRecipe(
                new ItemStack(IUItem.crafting_elements.getStack(560)),
                new ItemStack(IUItem.crafting_elements.getStack(566)),
                new ItemStack(IUItem.crafting_elements.getStack(591)),
                new ItemStack(IUItem.basecircuit.getStack(0)),
                new ItemStack(IUItem.crafting_elements.getStack(574)),
                new ItemStack(IUItem.crafting_elements.getStack(542))
        );

        addRecipe(
                new ItemStack(IUItem.crafting_elements.getStack(560)),
                new ItemStack(IUItem.crafting_elements.getStack(584)),
                new ItemStack(IUItem.crafting_elements.getStack(591)),
                new ItemStack(IUItem.basecircuit.getStack(1)),
                new ItemStack(IUItem.crafting_elements.getStack(574)),
                new ItemStack(IUItem.crafting_elements.getStack(540))
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements.getStack(560)),
                new ItemStack(IUItem.crafting_elements.getStack(584)),
                new ItemStack(IUItem.crafting_elements.getStack(561)),
                new ItemStack(IUItem.basecircuit.getStack(1)),
                new ItemStack(IUItem.crafting_elements.getStack(574)),
                new ItemStack(IUItem.crafting_elements.getStack(548))
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements.getStack(589)),
                new ItemStack(IUItem.crafting_elements.getStack(564)),
                new ItemStack(IUItem.crafting_elements.getStack(561)),
                new ItemStack(IUItem.basecircuit.getStack(2)),
                new ItemStack(IUItem.crafting_elements.getStack(574)),
                new ItemStack(IUItem.crafting_elements.getStack(536))
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements.getStack(596)),
                new ItemStack(IUItem.crafting_elements.getStack(564)),
                new ItemStack(IUItem.crafting_elements.getStack(561)),
                new ItemStack(IUItem.basecircuit.getStack(18)),
                new ItemStack(IUItem.crafting_elements.getStack(574)),
                new ItemStack(IUItem.crafting_elements.getStack(554))
        );


        addRecipe(
                new ItemStack(IUItem.basecircuit.getStack(15)),
                new ItemStack(IUItem.crafting_elements.getStack(582)),
                new ItemStack(IUItem.crafting_elements.getStack(494)),
                new ItemStack(IUItem.crafting_elements.getStack(568), 2),
                new ItemStack(IUItem.crafting_elements.getStack(588)),
                new ItemStack(IUItem.crafting_elements.getStack(533))
        );

        addRecipe(
                new ItemStack(IUItem.basecircuit.getStack(16), 2),
                new ItemStack(IUItem.crafting_elements.getStack(582)),
                new ItemStack(IUItem.crafting_elements.getStack(494)),
                new ItemStack(IUItem.crafting_elements.getStack(568), 2),
                new ItemStack(IUItem.crafting_elements.getStack(588)),
                new ItemStack(IUItem.crafting_elements.getStack(541))
        );

        addRecipe(
                new ItemStack(IUItem.basecircuit.getStack(13)),
                new ItemStack(IUItem.crafting_elements.getStack(599)),
                new ItemStack(IUItem.crafting_elements.getStack(533)),
                new ItemStack(IUItem.crafting_elements.getStack(568), 2),
                new ItemStack(IUItem.crafting_elements.getStack(588)),
                new ItemStack(IUItem.crafting_elements.getStack(543))
        );

        addRecipe(
                new ItemStack(IUItem.basecircuit.getStack(13), 2),
                new ItemStack(IUItem.crafting_elements.getStack(599)),
                new ItemStack(IUItem.crafting_elements.getStack(541)),
                new ItemStack(IUItem.crafting_elements.getStack(567), 2),
                new ItemStack(IUItem.crafting_elements.getStack(588)),
                new ItemStack(IUItem.crafting_elements.getStack(545))
        );

        addRecipe(
                new ItemStack(IUItem.basecircuit.getStack(3)),
                new ItemStack(IUItem.crafting_elements.getStack(583)),
                new ItemStack(IUItem.crafting_elements.getStack(543)),
                new ItemStack(IUItem.crafting_elements.getStack(567), 2),
                new ItemStack(IUItem.crafting_elements.getStack(598)),
                new ItemStack(IUItem.crafting_elements.getStack(549))
        );

        addRecipe(
                new ItemStack(IUItem.basecircuit.getStack(3), 2),
                new ItemStack(IUItem.crafting_elements.getStack(583)),
                new ItemStack(IUItem.crafting_elements.getStack(545)),
                new ItemStack(IUItem.crafting_elements.getStack(567), 2),
                new ItemStack(IUItem.crafting_elements.getStack(598)),
                new ItemStack(IUItem.crafting_elements.getStack(551))
        );

        addRecipe(
                new ItemStack(IUItem.basecircuit.getStack(4)),
                new ItemStack(IUItem.crafting_elements.getStack(583)),
                new ItemStack(IUItem.crafting_elements.getStack(549)),
                new ItemStack(IUItem.crafting_elements.getStack(565), 2),
                new ItemStack(IUItem.crafting_elements.getStack(592)),
                new ItemStack(IUItem.crafting_elements.getStack(555))
        );

        addRecipe(
                new ItemStack(IUItem.basecircuit.getStack(4), 2),
                new ItemStack(IUItem.crafting_elements.getStack(583)),
                new ItemStack(IUItem.crafting_elements.getStack(551)),
                new ItemStack(IUItem.crafting_elements.getStack(565), 2),
                new ItemStack(IUItem.crafting_elements.getStack(592)),
                new ItemStack(IUItem.crafting_elements.getStack(556))
        );

        addRecipe(
                new ItemStack(IUItem.basecircuit.getStack(5), 2),
                new ItemStack(IUItem.crafting_elements.getStack(597)),
                new ItemStack(IUItem.crafting_elements.getStack(555)),
                new ItemStack(IUItem.crafting_elements.getStack(565), 2),
                new ItemStack(IUItem.crafting_elements.getStack(594)),
                new ItemStack(IUItem.crafting_elements.getStack(558))
        );

        addRecipe(
                new ItemStack(IUItem.basecircuit.getStack(19), 3),
                new ItemStack(IUItem.crafting_elements.getStack(597)),
                new ItemStack(IUItem.crafting_elements.getStack(556)),
                new ItemStack(IUItem.crafting_elements.getStack(565), 2),
                new ItemStack(IUItem.crafting_elements.getStack(594)),
                new ItemStack(IUItem.crafting_elements.getStack(534))
        );
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenElectricElectronicsAssembler((ContainerMenuElectricElectronicsAssembler) menu);
    }


    public float getWrenchDropRate() {
        return 0.85F;
    }

    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(EnumBlockEntityUpgrade.Processing, EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.EnergyStorage, EnumBlockEntityUpgrade.ItemExtract, EnumBlockEntityUpgrade.ItemInput
        );
    }

}
