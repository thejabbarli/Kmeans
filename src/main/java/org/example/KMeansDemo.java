package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class KMeansDemo {
    public static void main(String[] args) {
        try {
            List<double[]> irisData = loadIrisDataset("IRIS.csv");
            KMeans kMeans = new KMeans(3);
            kMeans.fit(irisData);
            int[] clusters = kMeans.getClusters();

            System.out.println("Final centroids:");
            for (int i = 0; i < kMeans.getCentroids().size(); i++) {
                System.out.println("Cluster " + i + ": " + Arrays.toString(kMeans.getCentroids().get(i)));
            }

            EvaluationMetrics metrics = new EvaluationMetrics();
            double wcss = metrics.calculateWCSS(irisData, clusters, kMeans.getCentroids());
            System.out.println("WCSS: " + wcss);

            exportClusterAssignments(irisData, clusters, "cluster_assignments.csv");
            exportCentroids(kMeans.getCentroids(), "centroids.csv");

            System.out.println("\nCluster assignment and centroid data exported for Python plotting.");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static List<double[]> loadIrisDataset(String filename) throws IOException {
        List<double[]> dataset = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/rsrc/" + filename))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] values = line.split(",");
                double[] features = new double[4];
                for (int i = 0; i < 4; i++) {
                    features[i] = Double.parseDouble(values[i]);
                }
                dataset.add(features);
            }
        }
        return dataset;
    }

    private static void exportClusterAssignments(List<double[]> data, int[] clusters, String outputFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            writer.println("SampleIndex,SepalLength,SepalWidth,PetalLength,PetalWidth,Cluster");
            for (int i = 0; i < data.size(); i++) {
                double[] features = data.get(i);
                writer.printf("%d,%.4f,%.4f,%.4f,%.4f,%d%n", i, features[0], features[1], features[2], features[3], clusters[i]);
            }
        }
    }

    private static void exportCentroids(List<double[]> centroids, String outputFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            writer.println("Cluster,SepalLength,SepalWidth,PetalLength,PetalWidth");
            for (int i = 0; i < centroids.size(); i++) {
                double[] c = centroids.get(i);
                writer.printf("%d,%.4f,%.4f,%.4f,%.4f%n", i, c[0], c[1], c[2], c[3]);
            }
        }
    }
}
