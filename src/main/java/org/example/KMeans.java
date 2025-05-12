package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeans {
    private int k;
    private List<double[]> centroids;
    private int[] clusterAssignments;
    private Random random;
    private static final int MAX_ITERATIONS = 100;

    public KMeans(int k) {
        if (k <= 0) throw new IllegalArgumentException("k must be greater than 0");
        this.k = k;
        this.centroids = new ArrayList<>();
        this.random = new Random();
    }

    public void fit(List<double[]> data) {
        if (data == null || data.isEmpty())
            throw new IllegalArgumentException("Data cannot be null or empty");

        int numSamples = data.size();
        int numFeatures = data.get(0).length;
        clusterAssignments = new int[numSamples];
        initializeRandomClusters(numSamples);
        calculateCentroids(data);

        boolean centroidsChanged = true;
        int iteration = 0;

        while (centroidsChanged && iteration < MAX_ITERATIONS) {
            centroidsChanged = false;

            for (int i = 0; i < numSamples; i++) {
                int closestCluster = findClosestCluster(data.get(i), centroids);
                if (clusterAssignments[i] != closestCluster) {
                    clusterAssignments[i] = closestCluster;
                    centroidsChanged = true;
                }
            }

            if (centroidsChanged) calculateCentroids(data);
            iteration++;
        }

        System.out.println("K-means converged after " + iteration + " iterations");
    }

    private void initializeRandomClusters(int numSamples) {
        for (int i = 0; i < k; i++) clusterAssignments[i] = i;
        for (int i = k; i < numSamples; i++) clusterAssignments[i] = random.nextInt(k);
    }

    private void calculateCentroids(List<double[]> data) {
        int numFeatures = data.get(0).length;
        int[] counts = new int[k];
        double[][] sums = new double[k][numFeatures];

        for (int i = 0; i < data.size(); i++) {
            int cluster = clusterAssignments[i];
            counts[cluster]++;
            for (int j = 0; j < numFeatures; j++) {
                sums[cluster][j] += data.get(i)[j];
            }
        }

        centroids.clear();
        for (int i = 0; i < k; i++) {
            if (counts[i] > 0) {
                double[] centroid = new double[numFeatures];
                for (int j = 0; j < numFeatures; j++) {
                    centroid[j] = sums[i][j] / counts[i];
                }
                centroids.add(centroid);
            } else {
                double[] randomPoint = data.get(random.nextInt(data.size()));
                centroids.add(randomPoint.clone());
            }
        }
    }

    public int findClosestCluster(double[] vector, List<double[]> centroids) {
        double minDist = Double.MAX_VALUE;
        List<Integer> closestClusters = new ArrayList<>();
        for (int i = 0; i < centroids.size(); i++) {
            double dist = calculateEuclideanDistance(vector, centroids.get(i));
            if (dist < minDist) {
                minDist = dist;
                closestClusters.clear();
                closestClusters.add(i);
            } else if (dist == minDist) {
                closestClusters.add(i);
            }
        }
        return closestClusters.get(random.nextInt(closestClusters.size()));
    }

    private double calculateEuclideanDistance(double[] v1, double[] v2) {
        if (v1.length != v2.length)
            throw new IllegalArgumentException("Vectors must have the same dimensions");

        double sum = 0.0;
        for (int i = 0; i < v1.length; i++) {
            double diff = v1[i] - v2[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    public int[] getClusters() {
        return clusterAssignments;
    }

    public List<double[]> getCentroids() {
        return centroids;
    }
}
