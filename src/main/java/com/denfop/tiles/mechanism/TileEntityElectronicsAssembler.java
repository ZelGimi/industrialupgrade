package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgrade;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.componets.TypeUpgrade;
import com.denfop.container.ContainerElectricElectronicsAssembler;
import com.denfop.gui.GuiElectricElectronicsAssembler;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityElectronicsAssembler extends TileElectricMachine implements IUpdateTick, IHasRecipe, IUpgradableBlock {


    public final InvSlot input_slot;

    public final InvSlotUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    private final ComponentUpgrade componentUpgrades;


    public InvSlotRecipes inputSlotA;
    public MachineRecipe output;

    public TileEntityElectronicsAssembler() {
        super(300, 1, 1);
        this.inputSlotA = new InvSlotRecipes(this, "electronics", this);
        this.upgradeSlot = new InvSlotUpgrade(this, 4);
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
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (this.get().isEmpty()) {
                    ((TileEntityElectronicsAssembler) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileEntityElectronicsAssembler) this.base).inputSlotA.changeAccepts(this.get());
                }
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.recipe_schedule;
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
        if (!this.getWorld().isRemote) {
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get());
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

    public ContainerElectricElectronicsAssembler getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerElectricElectronicsAssembler(
                entityPlayer, this);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.electronic_assembler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public void init() {
        addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 578),
                new ItemStack(IUItem.crafting_elements, 1, 593),
                new ItemStack(IUItem.crafting_elements, 1, 570),
                new ItemStack(IUItem.crafting_elements, 1, 571),
                new ItemStack(IUItem.crafting_elements, 1, 572),
                new ItemStack(IUItem.crafting_elements, 1, 539)
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 578),
                new ItemStack(IUItem.crafting_elements, 1, 593),
                new ItemStack(IUItem.crafting_elements, 1, 573),
                new ItemStack(IUItem.crafting_elements, 1, 562),
                new ItemStack(IUItem.crafting_elements, 1, 572),
                new ItemStack(IUItem.crafting_elements, 1, 547)
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 578),
                new ItemStack(IUItem.crafting_elements, 1, 593),
                new ItemStack(IUItem.crafting_elements, 1, 573),
                new ItemStack(IUItem.crafting_elements, 1, 595),
                new ItemStack(IUItem.crafting_elements, 1, 577),
                new ItemStack(IUItem.crafting_elements, 1, 535)
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 578),
                new ItemStack(IUItem.crafting_elements, 1, 593),
                new ItemStack(IUItem.crafting_elements, 1, 590),
                new ItemStack(IUItem.crafting_elements, 1, 595),
                new ItemStack(IUItem.crafting_elements, 1, 577),
                new ItemStack(IUItem.crafting_elements, 1, 544)
        );

        addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 578),
                new ItemStack(IUItem.crafting_elements, 1, 593),
                new ItemStack(IUItem.crafting_elements, 1, 590),
                new ItemStack(IUItem.crafting_elements, 1, 595),
                new ItemStack(IUItem.crafting_elements, 1, 575),
                new ItemStack(IUItem.crafting_elements, 1, 553)
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 578),
                new ItemStack(IUItem.crafting_elements, 1, 593),
                new ItemStack(IUItem.crafting_elements, 1, 559),
                new ItemStack(IUItem.crafting_elements, 1, 595),
                new ItemStack(IUItem.crafting_elements, 1, 575),
                new ItemStack(IUItem.crafting_elements, 1, 550)
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 578),
                new ItemStack(IUItem.crafting_elements, 1, 593),
                new ItemStack(IUItem.crafting_elements, 1, 559),
                new ItemStack(IUItem.crafting_elements, 1, 586),
                new ItemStack(IUItem.crafting_elements, 1, 575),
                new ItemStack(IUItem.crafting_elements, 1, 537)
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 578),
                new ItemStack(IUItem.crafting_elements, 1, 593),
                new ItemStack(IUItem.crafting_elements, 1, 559),
                new ItemStack(IUItem.crafting_elements, 1, 586),
                new ItemStack(IUItem.crafting_elements, 1, 587),
                new ItemStack(IUItem.crafting_elements, 1, 546)
        );


        addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 580),
                new ItemStack(IUItem.crafting_elements, 1, 576),
                new ItemStack(IUItem.crafting_elements, 1, 569),
                new ItemStack(IUItem.basecircuit, 1, 15),
                new ItemStack(IUItem.crafting_elements, 1, 574),
                new ItemStack(IUItem.crafting_elements, 1, 538)
        );

        addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 560),
                new ItemStack(IUItem.crafting_elements, 1, 576),
                new ItemStack(IUItem.crafting_elements, 1, 569),
                new ItemStack(IUItem.basecircuit, 1, 12),
                new ItemStack(IUItem.crafting_elements, 1, 574),
                new ItemStack(IUItem.crafting_elements, 1, 557)
        );

        addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 560),
                new ItemStack(IUItem.crafting_elements, 1, 576),
                new ItemStack(IUItem.crafting_elements, 1, 600),
                new ItemStack(IUItem.basecircuit, 1, 12),
                new ItemStack(IUItem.crafting_elements, 1, 574),
                new ItemStack(IUItem.crafting_elements, 1, 552)
        );

        addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 560),
                new ItemStack(IUItem.crafting_elements, 1, 566),
                new ItemStack(IUItem.crafting_elements, 1, 591),
                new ItemStack(IUItem.basecircuit, 1, 0),
                new ItemStack(IUItem.crafting_elements, 1, 574),
                new ItemStack(IUItem.crafting_elements, 1, 542)
        );

        addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 560),
                new ItemStack(IUItem.crafting_elements, 1, 584),
                new ItemStack(IUItem.crafting_elements, 1, 591),
                new ItemStack(IUItem.basecircuit, 1, 1),
                new ItemStack(IUItem.crafting_elements, 1, 574),
                new ItemStack(IUItem.crafting_elements, 1, 540)
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 560),
                new ItemStack(IUItem.crafting_elements, 1, 584),
                new ItemStack(IUItem.crafting_elements, 1, 561),
                new ItemStack(IUItem.basecircuit, 1, 1),
                new ItemStack(IUItem.crafting_elements, 1, 574),
                new ItemStack(IUItem.crafting_elements, 1, 548)
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 589),
                new ItemStack(IUItem.crafting_elements, 1, 564),
                new ItemStack(IUItem.crafting_elements, 1, 561),
                new ItemStack(IUItem.basecircuit, 1, 2),
                new ItemStack(IUItem.crafting_elements, 1, 574),
                new ItemStack(IUItem.crafting_elements, 1, 536)
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 596),
                new ItemStack(IUItem.crafting_elements, 1, 564),
                new ItemStack(IUItem.crafting_elements, 1, 561),
                new ItemStack(IUItem.basecircuit, 1, 18),
                new ItemStack(IUItem.crafting_elements, 1, 574),
                new ItemStack(IUItem.crafting_elements, 1, 554)
        );


        addRecipe(
                new ItemStack(IUItem.basecircuit, 1, 15),
                new ItemStack(IUItem.crafting_elements, 1, 582),
                new ItemStack(IUItem.crafting_elements, 1, 494),
                new ItemStack(IUItem.crafting_elements, 2, 568),
                new ItemStack(IUItem.crafting_elements, 1, 588),
                new ItemStack(IUItem.crafting_elements, 1, 533)
        );

        addRecipe(
                new ItemStack(IUItem.basecircuit, 2, 16),
                new ItemStack(IUItem.crafting_elements, 1, 582),
                new ItemStack(IUItem.crafting_elements, 1, 494),
                new ItemStack(IUItem.crafting_elements, 2, 568),
                new ItemStack(IUItem.crafting_elements, 1, 588),
                new ItemStack(IUItem.crafting_elements, 1, 541)
        );

        addRecipe(
                new ItemStack(IUItem.basecircuit, 1, 13),
                new ItemStack(IUItem.crafting_elements, 1, 599),
                new ItemStack(IUItem.crafting_elements, 1, 533),
                new ItemStack(IUItem.crafting_elements, 2, 568),
                new ItemStack(IUItem.crafting_elements, 1, 588),
                new ItemStack(IUItem.crafting_elements, 1, 543)
        );

        addRecipe(
                new ItemStack(IUItem.basecircuit, 2, 13),
                new ItemStack(IUItem.crafting_elements, 1, 599),
                new ItemStack(IUItem.crafting_elements, 1, 541),
                new ItemStack(IUItem.crafting_elements, 2, 567),
                new ItemStack(IUItem.crafting_elements, 1, 588),
                new ItemStack(IUItem.crafting_elements, 1, 545)
        );

        addRecipe(
                new ItemStack(IUItem.basecircuit, 1, 3),
                new ItemStack(IUItem.crafting_elements, 1, 583),
                new ItemStack(IUItem.crafting_elements, 1, 543),
                new ItemStack(IUItem.crafting_elements, 2, 567),
                new ItemStack(IUItem.crafting_elements, 1, 598),
                new ItemStack(IUItem.crafting_elements, 1, 549)
        );

        addRecipe(
                new ItemStack(IUItem.basecircuit, 2, 3),
                new ItemStack(IUItem.crafting_elements, 1, 583),
                new ItemStack(IUItem.crafting_elements, 1, 545),
                new ItemStack(IUItem.crafting_elements, 2, 567),
                new ItemStack(IUItem.crafting_elements, 1, 598),
                new ItemStack(IUItem.crafting_elements, 1, 551)
        );

        addRecipe(
                new ItemStack(IUItem.basecircuit, 1, 4),
                new ItemStack(IUItem.crafting_elements, 1, 583),
                new ItemStack(IUItem.crafting_elements, 1, 549),
                new ItemStack(IUItem.crafting_elements, 2, 565),
                new ItemStack(IUItem.crafting_elements, 1, 592),
                new ItemStack(IUItem.crafting_elements, 1, 555)
        );

        addRecipe(
                new ItemStack(IUItem.basecircuit, 2, 4),
                new ItemStack(IUItem.crafting_elements, 1, 583),
                new ItemStack(IUItem.crafting_elements, 1, 551),
                new ItemStack(IUItem.crafting_elements, 2, 565),
                new ItemStack(IUItem.crafting_elements, 1, 592),
                new ItemStack(IUItem.crafting_elements, 1, 556)
        );

        addRecipe(
                new ItemStack(IUItem.basecircuit, 2, 5),
                new ItemStack(IUItem.crafting_elements, 1, 597),
                new ItemStack(IUItem.crafting_elements, 1, 555),
                new ItemStack(IUItem.crafting_elements, 2, 565),
                new ItemStack(IUItem.crafting_elements, 1, 594),
                new ItemStack(IUItem.crafting_elements, 1, 558)
        );

        addRecipe(
                new ItemStack(IUItem.basecircuit, 3, 19),
                new ItemStack(IUItem.crafting_elements, 1, 597),
                new ItemStack(IUItem.crafting_elements, 1, 556),
                new ItemStack(IUItem.crafting_elements, 2, 565),
                new ItemStack(IUItem.crafting_elements, 1, 594),
                new ItemStack(IUItem.crafting_elements, 1, 534)
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


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiElectricElectronicsAssembler(new ContainerElectricElectronicsAssembler(entityPlayer, this));
    }


    public float getWrenchDropRate() {
        return 0.85F;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemExtract, UpgradableProperty.ItemInput
        );
    }

}
