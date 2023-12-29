package com.denfop.api.reactors;

import com.denfop.blocks.FluidName;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogicGraphiteReactor extends LogicReactor{

    private final IGraphiteReactor graphiteReactor;
    Map<Integer, Double> mapGraphiteConsume;
    public double temp_heat = 0;
    public LogicGraphiteReactor(final IGraphiteReactor advReactor) {
        super(advReactor);
        this.graphiteReactor = advReactor;
    }

    @Override
    public void calculateComponent() {
        super.calculateComponent();
        mapGraphiteConsume = new HashMap<>();
        for(int i =0; i < this.x;i++) {
            double col = 0;
            for(LogicComponent component : this.rodsList){
                if(component.getX() == i)
                    col+= (component.heat * 0.83)/160;
            }
            mapGraphiteConsume.put(i,col);
        }
    }

    @Override
    public void onTick() {
        if(this.graphiteReactor.canWorkWithGraphite()) {
            super.onTick();
            if(temp_heat < getMaxHeat() && getMaxHeat() != 0){
                double temp = getMaxHeat() - temp_heat ;
                temp_heat += rand.nextInt(Math.max((int) temp,1));
            }
            temp_heat =  this.graphiteReactor.workCoolant(temp_heat);
            for(int i =0; i < reactor.getWidth();i++) {
                if(this.mapGraphiteConsume.get(i) == 0)
                    continue;
                double col = this.mapGraphiteConsume.get(i);
                if(graphiteReactor.getFuelGraphite(i) > col) {
                    graphiteReactor.consumeFuelGraphite(i,col);
                    final int level = this.graphiteReactor.getLevelGraphite(i);
                    if(this.graphiteReactor.getWaterTank().getFluidAmount() >= col  * level) {
                        if(this.graphiteReactor.getCoalDioxideTank().getFluidAmount() + col  * level <=this.graphiteReactor.getCoalDioxideTank().getCapacity() ){
                            this.graphiteReactor.getWaterTank().drain((int) (col * level),true);
                            this.graphiteReactor.getCoalDioxideTank().fill(new FluidStack(FluidName.fluidco2.getInstance(),
                                    (int) (col  * level)
                            ),true);
                            if(temp_heat > getMaxHeat()){
                                temp_heat -= rand.nextInt((int) (temp_heat - getMaxHeat()));
                            }
                            if(this.graphiteReactor.getSteamTank().getFluidAmount() >= col ){
                                if(this.graphiteReactor.getCoalDioxideTank().getFluidAmount() >= col * level){
                                    if(this.graphiteReactor.getOxideTank().getFluidAmount() +  col * level <= this.graphiteReactor.getOxideTank().getFluidAmount()){
                                        this.graphiteReactor.getSteamTank().drain((int) (col )
                                                ,true);
                                    }
                                    this.graphiteReactor.getCoalDioxideTank().drain((int) (col * level)
                                            ,true);
                                    this.graphiteReactor.getOxideTank().fill(new FluidStack(FluidName.fluidoxy.getInstance(),
                                            (int) (col * level)
                                    ),true);
                                    temp_heat-=rand.nextInt((int) (10*col));

                                }
                            }
                        }else{
                            temp_heat += rand.nextInt(50);
                        }
                    }else{
                        if(this.graphiteReactor.getCoalDioxideTank().getFluidAmount() + col  * level <=this.graphiteReactor.getCoalDioxideTank().getCapacity() ){
                            this.graphiteReactor.getCoalDioxideTank().fill(new FluidStack(FluidName.fluidco2.getInstance(),
                                    (int) (col  * level)
                            ),true);
                            if(temp_heat > getMaxHeat()){
                                temp_heat -= rand.nextInt((int) (temp_heat - getMaxHeat()));
                            }
                            if(this.graphiteReactor.getSteamTank().getFluidAmount() >= col ){
                                if(this.graphiteReactor.getCoalDioxideTank().getFluidAmount() >= col * level){
                                    if(this.graphiteReactor.getOxideTank().getFluidAmount() +  col * level <= this.graphiteReactor.getOxideTank().getFluidAmount()){
                                        this.graphiteReactor.getSteamTank().drain((int) (col )
                                                ,true);
                                    }
                                    this.graphiteReactor.getCoalDioxideTank().drain((int) (col * level)
                                            ,true);
                                    this.graphiteReactor.getOxideTank().fill(new FluidStack(FluidName.fluidoxy.getInstance(),
                                            (int) (col * level)
                                    ),true);
                                    temp_heat-=rand.nextInt((int) (10*col));

                                }
                            }
                        }else{
                            temp_heat += rand.nextInt(100);
                        }
                    }
                }else{
                    if(!graphiteReactor.getGraphite(i).isEmpty()) {
                        graphiteReactor.consumeGraphite(i);
                        if (graphiteReactor.getFuelGraphite(i) < col) {
                            temp_heat += rand.nextInt(100);
                        } else {
                            graphiteReactor.consumeFuelGraphite(i,col);

                        }
                    }else{
                        temp_heat += rand.nextInt((int) (30 * col));
                    }
                }
            }
        }else{

            temp_heat += rand.nextInt(200);

        }
        temp_heat = Math.max(0,temp_heat);
        graphiteReactor.setHeat(temp_heat);
    }

    public void setTemp_heat(final double temp_heat) {
        this.temp_heat = temp_heat;
    }

    public double getTemp_heat() {
        return temp_heat;
    }

}
