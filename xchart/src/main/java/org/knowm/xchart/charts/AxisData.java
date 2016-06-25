package org.knowm.xchart.charts;

import java.util.ArrayList;
import java.util.List;

public class AxisData {
    private List<Double> xAxisData;
    private List<Double> yAxisData;

    public AxisData(){}

    public AxisData(List<Double> y, List<Double> x){
        xAxisData = x;
        yAxisData = y;
    }

    public List<Double> getxAxisData(){
        return xAxisData;
    }

    public List<Double> getyAxisData(){
        return yAxisData;
    }

    public void setxAxisData(List<Double> x){
        xAxisData = x;
    }

    public void setyAxisData(List<Double> y){
        yAxisData = y;
    }

}
