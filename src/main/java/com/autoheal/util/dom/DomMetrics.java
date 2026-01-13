package com.autoheal.util.dom;


public final class DomMetrics {

    private final int originalNodeCount;
    private final int optimizedNodeCount;
    private final int originalSizeChars;
    private final int optimizedSizeChars;
    private final double reductionPercentage;

    public DomMetrics(int originalNodeCount,
                      int optimizedNodeCount,
                      int originalSizeChars,
                      int optimizedSizeChars) {

        this.originalNodeCount = originalNodeCount;
        this.optimizedNodeCount = optimizedNodeCount;
        this.originalSizeChars = originalSizeChars;
        this.optimizedSizeChars = optimizedSizeChars;
        this.reductionPercentage =
                originalSizeChars == 0 ? 0 :
                        100.0 * (originalSizeChars - optimizedSizeChars) / originalSizeChars;
    }

    public int getOriginalNodeCount() { return originalNodeCount; }
    public int getOptimizedNodeCount() { return optimizedNodeCount; }
    public int getOriginalSizeChars() { return originalSizeChars; }
    public int getOptimizedSizeChars() { return optimizedSizeChars; }
    public double getReductionPercentage() { return reductionPercentage; }
}

