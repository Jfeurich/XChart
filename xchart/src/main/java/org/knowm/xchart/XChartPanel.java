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
package org.knowm.xchart;

import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import org.knowm.xchart.internal.Series_AxesChart;
import org.knowm.xchart.internal.chartpart.Chart;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Swing JPanel that contains a Chart
 * <p>
 * Right-click + Save As... or ctrl+S pops up a Save As dialog box for saving the chart as a JPeg or PNG file.
 *
 * @author timmolter
 */
public class XChartPanel<T extends Chart> extends JPanel {

  private final T chart;
  private final Dimension preferredSize;
  private String saveAsString = "Save As...";

  /**
   * Constructor
   *
   * @param chart
   */
  public XChartPanel(final T chart) {

    this.chart = chart;
    preferredSize = new Dimension(chart.getWidth(), chart.getHeight());

    // Right-click listener for saving chart
    this.addMouseListener(new PopUpMenuClickListener());

    // Control+S key listener for saving chart
    KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(ctrlS, "save");
    this.getActionMap().put("save", new SaveAction());
  }

  /**
   * Set the "Save As..." String if you want to localize it.
   *
   * @param saveAsString
   */
  public void setSaveAsString(String saveAsString) {

    this.saveAsString = saveAsString;
  }

  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g.create();
    chart.paint(g2d, getWidth(), getHeight());
    g2d.dispose();
  }

  public T getChart() {

    return this.chart;
  }

  @Override
  public Dimension getPreferredSize() {

    return this.preferredSize;
  }

  private class SaveAction extends AbstractAction {

    public SaveAction() {

      super("save");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

      showSaveAsDialog();
    }
  }

  private void showSaveAsDialog() {

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.addChoosableFileFilter(new SuffixSaveFilter("jpg"));
    FileFilter pngFileFilter = new SuffixSaveFilter("png");
    fileChooser.addChoosableFileFilter(pngFileFilter);
    fileChooser.addChoosableFileFilter(new SuffixSaveFilter("bmp"));
    fileChooser.addChoosableFileFilter(new SuffixSaveFilter("gif"));

    // VectorGraphics2D is optional, so if it's on the classpath, allow saving charts as vector graphic
    try {
      Class.forName("de.erichseifert.vectorgraphics2d.VectorGraphics2D");
      fileChooser.addChoosableFileFilter(new SuffixSaveFilter("svg"));
      fileChooser.addChoosableFileFilter(new SuffixSaveFilter("eps"));
      fileChooser.addChoosableFileFilter(new SuffixSaveFilter("pdf"));
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    fileChooser.setAcceptAllFileFilterUsed(false);

    fileChooser.setFileFilter(pngFileFilter);

    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile() != null) {
      File theFileToSave = fileChooser.getSelectedFile();
      if (fileChooser.getFileFilter() == null) {
        try {
          BitmapEncoder.saveBitmap(chart, theFileToSave.getCanonicalPath(), BitmapFormat.PNG);
          fileDingeser(fileChooser, theFileToSave);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void fileDingeser(JFileChooser fileChooser, File theFileToSave) throws IOException {
    switch (fileChooser.getFileFilter().getDescription()) {
      case "*.jpg,*.JPG":
        BitmapEncoder.saveJPGWithQuality(chart, BitmapEncoder.addFileExtension(theFileToSave.getCanonicalPath(), BitmapFormat.JPG), 1.0f);
        break;
      case "*.png,*.PNG":
        BitmapEncoder.saveBitmap(chart, theFileToSave.getCanonicalPath(), BitmapFormat.PNG);
        break;
      case "*.bmp,*.BMP":
        BitmapEncoder.saveBitmap(chart, theFileToSave.getCanonicalPath(), BitmapFormat.BMP);
        break;
      case "*.gif,*.GIF":
        BitmapEncoder.saveBitmap(chart, theFileToSave.getCanonicalPath(), BitmapFormat.GIF);
        break;
      case "*.svg,*.SVG":
        VectorGraphicsEncoder.saveVectorGraphic(chart, theFileToSave.getCanonicalPath(), VectorGraphicsFormat.SVG);
        break;
      case "*.eps,*.EPS":
        VectorGraphicsEncoder.saveVectorGraphic(chart, theFileToSave.getCanonicalPath(), VectorGraphicsFormat.EPS);
        break;
      case "*.pdf,*.PDF":
        VectorGraphicsEncoder.saveVectorGraphic(chart, theFileToSave.getCanonicalPath(), VectorGraphicsFormat.PDF);
        break;
      default : break;
    }
  }

  /**
   * File filter based on the suffix of a file. This file filter accepts all files that end with .suffix or the capitalized suffix.
   *
   * @author Benedikt Bünz
   */
  private class SuffixSaveFilter extends FileFilter {

    private final String suffix;

    /**
     * @param suffix This file filter accepts all files that end with .suffix or the capitalized suffix.
     */
    public SuffixSaveFilter(String suffix) {

      this.suffix = suffix;
    }

    @Override
    public boolean accept(File f) {

      if (f.isDirectory()) {
        return true;
      }

      String s = f.getName();

      return s.endsWith("." + suffix) || s.endsWith("." + suffix.toUpperCase());
    }

    @Override
    public String getDescription() {

      return "*." + suffix + ",*." + suffix.toUpperCase();
    }
  }

  private class PopUpMenuClickListener extends MouseAdapter {

    @Override
    public void mousePressed(MouseEvent e) {

      if (e.isPopupTrigger()) {
        doPop(e);
      }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

      if (e.isPopupTrigger()) {
        doPop(e);
      }
    }

    private void doPop(MouseEvent e) {

      XChartPanelPopupMenu menu = new XChartPanelPopupMenu();
      menu.show(e.getComponent(), e.getX(), e.getY());
    }
  }

  private class XChartPanelPopupMenu extends JPopupMenu {

    JMenuItem saveAsMenuItem;

    XChartPanelPopupMenu() {

      saveAsMenuItem = new JMenuItem(saveAsString);
      saveAsMenuItem.addMouseListener(new MyMouseListener());
      add(saveAsMenuItem);
    }

    private class MyMouseListener implements MouseListener {

      @Override
      public void mouseReleased(MouseEvent e) {
        showSaveAsDialog();
      }

      @Override
      public void mousePressed(MouseEvent e) {
        // empty because Override
      }

      @Override
      public void mouseExited(MouseEvent e) {
        // empty because Override
      }

      @Override
      public void mouseEntered(MouseEvent e) {
        // empty because Override
      }

      @Override
      public void mouseClicked(MouseEvent e) {
        // empty because Override
      }
    }
  }

  /**
   * Update a series by updating the X-Axis, Y-Axis and error bar data
   *
   * @param seriesName
   * @param newXData - set null to be automatically generated as a list of increasing Integers starting from
   *          1 and ending at the size of the new Y-Axis data list.
   * @param newYData
   * @param newErrorBarData - set null if there are no error bars
   * @return
   */
  public Series_AxesChart updateSeries(String seriesName, List<?> newXData, List<? extends Number> newYData, List<? extends Number> newErrorBarData) {

    Map<String, Series_AxesChart> seriesMap = chart.getSeriesMap();
    Series_AxesChart series = seriesMap.get(seriesName);
    if (series == null) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
    }
    if (newXData == null) {
      // generate X-Data
      List<Integer> generatedXData = new ArrayList<>();
      for (int i = 1; i <= newYData.size(); i++) {
        generatedXData.add(i);
      }
      series.replaceData(generatedXData, newYData, newErrorBarData);
    }
    else {
      series.replaceData(newXData, newYData, newErrorBarData);
    }

    // Re-display the chart
    revalidate();
    repaint();

    return series;
  }
}
