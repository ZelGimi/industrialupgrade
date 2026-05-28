package com.denfop.blockentity.mechanism.generator.things.matter;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMultiMatter;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPhotonicMatter extends BlockEntityMultiMatter {


    public BlockEntityPhotonicMatter(BlockPos pos, BlockState state) {
        super(ModConfig.mechanismDouble("photonic_matter_fabricator_energy_storage", 600000.0D), ModConfig.mechanismInt("photonic_matter_fabricator_tank_capacity", 16), ModConfig.mechanismDouble("photonic_matter_fabricator_energy_storage", 512000000.0D), BlocksPhotonicMachine.photonic_gen_matter, pos, state);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PHOTONIC;
    }

    public MultiBlockEntity getTeBlock() {
        return BlocksPhotonicMachine.photonic_gen_matter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine.getBlock(getTeBlock().getId());
    }

}
