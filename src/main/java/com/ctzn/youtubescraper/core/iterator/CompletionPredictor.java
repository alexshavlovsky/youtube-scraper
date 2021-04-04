package com.ctzn.youtubescraper.core.iterator;

public class CompletionPredictor {

    private final int T_DIF = 10000; // ms
    private final double EPS = 0.001;
    private final double K_FIL = 0.99;
    private final double targetValue;

    private long timeRef;
    private double valueRef;
    private boolean doInit = true;

    public CompletionPredictor(double targetValue) {
        this.targetValue = targetValue;
    }

    private long prediction = -1;
    private Double dif = null;

    public void predict(double currentValue) {
        double remaining = targetValue - currentValue;
        if (remaining <= EPS) {
            prediction = 0;
            return;
        }
        long now = System.currentTimeMillis();
        if (doInit) {
            doInit = false;
            resetRefs(now, currentValue);
            return;
        }
        updateDif(now, currentValue);
        if (dif != null) prediction = (long) (remaining * dif);
    }

    private void updateDif(long now, double currentValue) {
        long difTime = now - timeRef;
        double difValue = currentValue - valueRef;
        if (difTime > T_DIF && difValue > EPS) {
            resetRefs(now, currentValue);
            double curDif = difTime / difValue;
            if (dif == null) dif = curDif;
            else dif = dif * K_FIL + curDif * (1 - K_FIL);
        }
    }

    private void resetRefs(long now, double currentValue) {
        timeRef = now;
        valueRef = currentValue;
    }

    public String format() {
        if (prediction <= 0) return "";
        long s0 = prediction / 1000, h = s0 / 3600, m = (s0 % 3600) / 60, s = s0 % 60;
        return h == 0 ?
                String.format("%02d:%02d - ", m, s) :
                String.format("%d:%02d:%02d - ", h, m, s);
    }

}
