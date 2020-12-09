package com.company.task2;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

public class LineChart extends JFrame{
    private XYDataset dataset;
    private String yLabel;
    private String xLabel;
    private String title;
    private Color color;

    public LineChart() {
        initUI();
    }

    public LineChart(XYDataset dataset, String yLabel, String xLabel, String title, Color color) {
        this.dataset = dataset;
        this.yLabel = yLabel;
        this.xLabel = xLabel;
        this.title = title;
        this.color = color;
        initUI();
    }

    private void initUI() {
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chartPanel.setBackground(Color.white);
        add(chartPanel);
        pack();
        setTitle("Line chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xLabel,
                yLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, color);
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.BLACK);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.green);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.green);
        chart.getLegend().setFrame(BlockBorder.NONE);
        chart.setTitle(new TextTitle(title,
                        new Font("Times New Roman", java.awt.Font.BOLD, 20)
                )
        );

        return chart;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            LineChart ex = new LineChart();
            ex.setVisible(true);
        });
    }
}
