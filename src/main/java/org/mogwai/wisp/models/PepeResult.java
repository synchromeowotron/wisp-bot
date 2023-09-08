package org.mogwai.wisp.models;

public class PepeResult {
    private String evaluation;
    private boolean isPepe;

    public PepeResult(String evaluation, boolean isPepe) {
        this.evaluation = evaluation;
        this.isPepe = isPepe;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public boolean isPepe() {
        return isPepe;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public void setPepe(boolean pepe) {
        isPepe = pepe;
    }
}
