/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.charts;

import org.knowm.xchart.style.markers.SeriesMarkers;

import java.util.List;

/**
 * A convenience class for making Charts with one line of code
 *
 * @author timmolter
 */
public final class QuickChart {

  private static final int WIDTH = 600;
  private static final int HEIGHT = 400;

  /**
   * private Constructor
   */
  private QuickChart() {

  }

  /**
   * Creates a Chart with default style
   *
   * @param chartTitle the Chart title
   * @param xTitle The X-Axis title
   * @param yTitle The Y-Axis title
   * @param seriesName The name of the series
   * @param xData An array containing the X-Axis data
   * @param yData An array containing Y-Axis data
   * @return a Chart Object
   */
  public static XYChart getChart(String chartTitle, String xTitle, String yTitle, String seriesName, double[] xData, double[] yData) {

    double[][] yData2d = { yData };
    if (seriesName == null) {
      return getChart(new ChartData(chartTitle, xTitle, yTitle, null, xData, yData2d));
    }
    else {
      return getChart(new ChartData(chartTitle, xTitle, yTitle, new String[]{seriesName}, xData, yData2d));
    }
  }

  /**
   * Creates a Chart with multiple Series for the same X-Axis data with default style
   *
   *
   * @param chartData@return a Chart Object
   */
  public static XYChart getChart(ChartData chartData) {

    // Create Chart
    XYChart chart = new XYChart(WIDTH, HEIGHT);

    // Customize Chart
    chart.setTitle(chartData.getChartTitle());
    chart.setXAxisTitle(chartData.getxTitle());
    chart.setYAxisTitle(chartData.getyTitle());

    // Series
    for (int i = 0; i < chartData.getyData().length; i++) {
      XYSeries series;
      if (chartData.getSeriesNames() != null) {
        series = chart.addSeries(chartData.getSeriesNames()[i], chartData.getxData(), chartData.getyData()[i]);
      }
      else {
        chart.getStyler().setLegendVisible(false);
        series = chart.addSeries(" " + i, chartData.getxData(), chartData.getyData()[i]);
      }
      series.setMarker(SeriesMarkers.NONE);
    }

    return chart;
  }

  /**
   * Creates a Chart with default style
   *
   * @param chartTitle the Chart title
   * @param xTitle The X-Axis title
   * @param yTitle The Y-Axis title
   * @param seriesName The name of the series
   * @param xData A Collection containing the X-Axis data
   * @param yData A Collection containing Y-Axis data
   * @return a Chart Object
   */
  public static XYChart getChart(String chartTitle, String xTitle, String yTitle, String seriesName, List<? extends Number> xData, List<? extends Number> yData) {

    // Create Chart
    XYChart chart = new XYChart(WIDTH, HEIGHT);

    // Customize Chart
    chart.setTitle(chartTitle);
    chart.setXAxisTitle(xTitle);
    chart.setYAxisTitle(yTitle);

    XYSeries series = chart.addSeries(seriesName, xData, yData);
    series.setMarker(SeriesMarkers.NONE);

    return chart;

  }

}
