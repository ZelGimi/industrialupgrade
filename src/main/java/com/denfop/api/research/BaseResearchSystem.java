package com.denfop.api.research;

import com.denfop.IUCore;
import com.denfop.api.research.event.EventDownloadData;
import com.denfop.api.research.event.EventLoadData;
import com.denfop.api.research.event.EventLoadResearchItem;
import com.denfop.api.research.event.EventUnLoadData;
import com.denfop.api.research.main.*;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseResearchSystem implements IResearchSystem {

    public int max;
    List<String> list_uuids;
    List<IResearch> list_research;
    List<IResearchPages> list_research_pages;
    List<IResearchPart> list_researcher_parts;
    Map<String, List<IResearch>> map_players;
    Map<Integer, IResearch> map_research;
    Map<String, BaseLevelSystem> map_level;
    IDataResearches data;
    IResearchSystemParts systemParts;

    public BaseResearchSystem() {
        this.list_uuids = new ArrayList<>();
        this.data = new DataResearches();
        this.map_players = new HashMap<>();
        ResearchSystem.systemParts = new BaseResearchSystemParts();
        this.systemParts = ResearchSystem.systemParts;
        this.map_research = new HashMap<>();
        this.list_research = new ArrayList<>();
        this.list_research_pages = new ArrayList<>();
        this.list_researcher_parts = new ArrayList<>();
        this.map_level = new HashMap<>();
        this.max = 0;
        MinecraftForge.EVENT_BUS.register(this);

    }

    public Map<String, BaseLevelSystem> getMap_level() {
        return map_level;
    }


    public Map<String, List<IResearch>> getMap_players() {
        return map_players;
    }

    @SubscribeEvent
    public void loadDataItem(EventDownloadData event) {
        final ItemStack stack = event.stack;
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final int id = this.max;
        this.max++;
        nbt.setInteger("research_id", id);
        this.map_research.put(id, event.research);
        final NBTTagCompound tag = ModUtils.nbt();
        nbt.setTag("research_parts", tag);
    }

    @Override
    public List<IResearchPart> getCopy(IResearch research) {
        return new ArrayList<>(research.getPartsResearch());
    }

    @Override
    public BaseLevelSystem getLevel(final EntityPlayer player) {
        return this.map_level.get(player.getName());
    }

    @SubscribeEvent
    public void loadDataItem(EventLoadResearchItem event) {
        final ItemStack stack = event.stack;
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final List<IResearchPart> list = getCopy(event.item.getResearch(stack));
        final int id = this.max;
        this.max++;
        nbt.setInteger("research_id", id);
        final boolean full = nbt.getBoolean("iu.research.full");
        if (full) {
            this.map_research.put(id, event.item.getResearch(stack));
            return;
        }
        final NBTTagCompound tag = (NBTTagCompound) nbt.getTag("research_parts");
        final List<IResearchPart> list_copy = new ArrayList<>();
        int[] list_id = tag.getIntArray("list_parts");
        for (final int j : list_id) {
            list_copy.add(list.get(j));
        }
        final IResearch research = event.item.getResearch(stack);
        research.setPartsResearch(list_copy);
        this.map_research.put(id, research);
    }

    @SubscribeEvent
    public void downloadData(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player.getEntityWorld().isRemote) {
            return;
        }
        if (!this.checkData(event.player)) {
            MinecraftForge.EVENT_BUS.post(new EventLoadData(event.player));
        }
    }

    @SubscribeEvent
    public void downloadData(EventLoadData event) {
        this.downloadData(event.player);
    }

    @SubscribeEvent
    public void uploadData(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player.getEntityWorld().isRemote) {
            return;
        }
        if (this.checkData(event.player)) {
            MinecraftForge.EVENT_BUS.post(new EventUnLoadData(event.player));
        }
    }

    @SubscribeEvent
    public void attackEntity(LivingDeathEvent event) {
        if (event.getEntity().getEntityWorld().isRemote) {
            return;
        }
        if (event.getSource().damageType.equals("player")) {
            if (event.getSource() instanceof EntityDamageSource && !(event.getSource().getTrueSource() instanceof FakePlayer)) {
                EntityDamageSource damage = (EntityDamageSource) event.getSource();
                BaseLevelSystem system = ResearchSystem.instance.getLevel((EntityPlayer) damage.getTrueSource());
                system.addLevel(EnumLeveling.PVP, 1);
                IUCore.network.get(true).initiateResearchSystemAdd(EnumLeveling.PVP, 1,
                        damage.getTrueSource().getName()
                );
            }
        } else if (event.getSource().damageType.equals("arrow")) {
            if (event
                    .getSource()
                    .getTrueSource() instanceof EntityPlayer && event.getSource() instanceof EntityDamageSource && !(event
                    .getSource()
                    .getTrueSource() instanceof FakePlayer)) {
                EntityDamageSource damage = (EntityDamageSource) event.getSource();
                BaseLevelSystem system = ResearchSystem.instance.getLevel((EntityPlayer) damage.getTrueSource());
                system.addLevel(EnumLeveling.PVP, 1);
                IUCore.network.get(true).initiateResearchSystemAdd(EnumLeveling.PVP, 1,
                        damage.getTrueSource().getName()
                );
            }
        }
    }

    @SubscribeEvent
    public void uploadData(EventUnLoadData event) {
        if (event.player.getEntityWorld().isRemote) {
            return;
        }
        this.uploadData(event.player);
    }

    @Override
    public void uploadData(final EntityPlayer player) {
        this.map_players.remove(player.getName());
        this.map_level.remove(player.getName());
        this.getUUIDs().remove(player.getName());
        IUCore.network.get(true).initiateResearchSystemDelete(player.getName());
    }

    @Override
    public void downloadData(final EntityPlayer player) {
        final NBTTagCompound nbt = player.getEntityData();
        final List<IResearch> list = data.getListResearches();
        final List<IResearch> list_researches = new ArrayList<>();
        for (IResearch research : list) {
            if (nbt.getBoolean("iu.research." + research.getName())) {
                list_researches.add(research);
            }
        }
        BaseLevelSystem levelSystem = new BaseLevelSystem(player);
        this.map_level.put(player.getName(), levelSystem);
        this.map_players.put(player.getName(), list_researches);
        this.getUUIDs().add(player.getName());
        IUCore.network.get(true).initiateResearchSystem(levelSystem);
    }

    @Override
    public boolean checkData(final EntityPlayer player) {
        return getUUIDs().contains(player.getName());
    }

    @Override
    public List<String> getUUIDs() {
        return this.list_uuids;
    }

    @Override
    public IResearch getResearchFromItem(final ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        int id = nbt.getInteger("research_id");
        return this.map_research.get(id);
    }

    @Override
    public IResearch getResearchFromID(final int id) {

        return this.data.getListResearches().get(id);
    }

}
