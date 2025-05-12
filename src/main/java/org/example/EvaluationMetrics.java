package org.example;

import java.util.List;

public class EvaluationMetrics {
    public double calculateWCSS(List<double[]> data, int[] clusters, List<double[]> centroids) {
        if (data == null || clusters == null || centroids == null)
            throw new IllegalArgumentException("Data, clusters, and centroids cannot be null");

        if (data.size() != clusters.length)
            throw new IllegalArgumentException("Data size must match cluster assignment length");

        double wcss = 0.0;
        for (int i = 0; i < data.size(); i++) {
            double[] point = data.get(i);
            int clusterIndex = clusters[i];
            double[] centroid = centroids.get(clusterIndex);
            wcss += calculateSquaredEuclideanDistance(point, centroid);
        }

        return wcss;
    }

    private double calculateSquaredEuclideanDistance(double[] v1, double[] v2) {
        if (v1.length != v2.length)
            throw new IllegalArgumentException("Vectors must have the same dimensions");

        double sum = 0.0;
        for (int i = 0; i < v1.length; i++) {
            double diff = v1[i] - v2[i];
            sum += diff * diff;
        }
        return sum;
    }
}
