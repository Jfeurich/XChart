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
package org.knowm.xchart.internal.chartpart;

import org.knowm.xchart.charts.XYSeries;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.XYStyler;

/**
 * @author timmolter
 */
public class Plot_XY<ST extends AxesChartStyler, S extends Series> extends Plot_AxesChart {

  /**
   * Constructor
   *
   * @param chart
   */
  public Plot_XY(Chart<XYStyler, XYSeries> chart) {

    super(chart);
    this.plotContent = new PlotContent_XY<XYStyler, XYSeries>(chart);
  }

}
