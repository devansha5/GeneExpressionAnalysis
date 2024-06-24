package com.example;

public class GeneData {
    private String gene;
    private double control;
    private double treated;
    private double foldChange;

    public GeneData(String gene, double control, double treated) {
        this.gene = gene;
        this.control = control;
        this.treated = treated;
    }

    public String getGene() {
        return gene;
    }

    public double getControl() {
        return control;
    }

    public double getTreated() {
        return treated;
    }

    public double getFoldChange() {
        return foldChange;
    }

    public void setFoldChange(double foldChange) {
        this.foldChange = foldChange;
    }
}

