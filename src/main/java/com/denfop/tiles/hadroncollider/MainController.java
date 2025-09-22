package com.denfop.tiles.hadroncollider;

import com.denfop.api.hadroncollider.EnumLevelCollider;
import com.denfop.api.hadroncollider.IExtractBlock;
import com.denfop.api.hadroncollider.IMainController;
import com.denfop.api.hadroncollider.IOverclockingBlock;
import com.denfop.api.hadroncollider.IPurifierBlock;
import com.denfop.api.hadroncollider.Protons;
import com.denfop.api.hadroncollider.Structures;
import com.denfop.componets.Energy;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public class MainController extends TileEntityInventory implements IMainController {

    private final EnumLevelCollider enumLevelCollider;
    private final boolean work;
    Energy energy;
    List<Protons> protonsList;
    private IExtractBlock extractBlock;
    private IOverclockingBlock OverclockingBlock;
    private IPurifierBlock PurifierBlock;
    private Structures structure;

    public MainController(EnumLevelCollider enumLevelCollider) {
        this.extractBlock = null;
        this.OverclockingBlock = null;
        this.PurifierBlock = null;
        this.enumLevelCollider = enumLevelCollider;
        energy = this.addComponent(Energy.asBasicSink(this, 100000000D, true));
        protonsList = new ArrayList<>();
        this.work = false;

    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        this.structure = new Structures(enumLevelCollider, this.pos);

    }

    @Override
    public IExtractBlock getExtractBlock() {
        return extractBlock;
    }

    @Override
    public void setExtractBlock(final IExtractBlock extractBlock) {
        this.extractBlock = extractBlock;
    }

    @Override
    public IOverclockingBlock getOverclockingBlock() {
        return OverclockingBlock;
    }

    @Override
    public void setOverclockingBlock(final IOverclockingBlock overclockingBlock) {
        this.OverclockingBlock = overclockingBlock;
    }

    @Override
    public IPurifierBlock getPurifierBlock() {
        return PurifierBlock;
    }

    @Override
    public void setPurifierBlock(final IPurifierBlock purifierBlock) {
        this.PurifierBlock = purifierBlock;
    }

    @Override
    public Energy getEnergy() {
        return energy;
    }

    @Override
    public EnumLevelCollider getEnumLevel() {
        return enumLevelCollider;
    }

    @Override
    public boolean canContainProtons() {
        return false;
    }

    @Override
    public List<Protons> getProtons() {
        return null;
    }

    @Override
    public Structures getStructure() {
        return this.structure;
    }

    @Override
    public boolean canWork() {
        return this.work;
    }


}
