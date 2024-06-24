package com.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

@WebServlet("/analyze")
public class GeneExpressionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("file");
        InputStream fileContent = filePart.getInputStream();

        List<GeneData> geneDataList = parseCSV(fileContent);

        List<GeneData> significantGenes = analyzeGeneData(geneDataList);

        createBarChart(significantGenes);

        request.setAttribute("significantGenes", significantGenes);
        request.getRequestDispatcher("/results.jsp").forward(request, response);
    }

    private List<GeneData> parseCSV(InputStream fileContent) throws IOException {
        List<GeneData> geneDataList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileContent))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3 && !data[0].equals("gene")) { // Skip header
                    String gene = data[0];
                    double control = Double.parseDouble(data[1]);
                    double treated = Double.parseDouble(data[2]);
                    geneDataList.add(new GeneData(gene, control, treated));
                }
            }
        }
        return geneDataList;
    }

    private List<GeneData> analyzeGeneData(List<GeneData> geneDataList) {
        List<GeneData> significantGenes = new ArrayList<>();
        for (GeneData geneData : geneDataList) {
            double foldChange = geneData.getTreated() / geneData.getControl();
            if (foldChange > 2) {
                geneData.setFoldChange(foldChange);
                significantGenes.add(geneData);
            }
        }
        return significantGenes;
    }

    private void createBarChart(List<GeneData> significantGenes) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (GeneData gene : significantGenes) {
            dataset.addValue(gene.getFoldChange(), "Fold Change", gene.getGene());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Top Differentially Expressed Genes",
                "Gene",
                "Fold Change",
                dataset
        );

        File chartFile = new File(getServletContext().getRealPath("/static/bar_chart.png"));
        ChartUtils.saveChartAsPNG(chartFile, barChart, 800, 600);
    }
}
