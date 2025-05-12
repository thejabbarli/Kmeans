import pandas as pd
import matplotlib.pyplot as plt

# Load CSVs
data = pd.read_csv("cluster_assignments.csv")
centroids = pd.read_csv("centroids.csv")

# Column names
features = ["SepalLength", "SepalWidth", "PetalLength", "PetalWidth"]

# Plot each feature pair
for i in range(len(features)):
    for j in range(i + 1, len(features)):
        plt.figure(figsize=(6, 5))
        for cluster_id in sorted(data['Cluster'].unique()):
            cluster_data = data[data['Cluster'] == cluster_id]
            plt.scatter(
                cluster_data[features[i]],
                cluster_data[features[j]],
                label=f"Cluster {cluster_id}",
                alpha=0.6
            )
        # Plot centroids
        plt.scatter(
            centroids[features[i]],
            centroids[features[j]],
            color='black',
            marker='X',
            s=100,
            label='Centroids'
        )
        plt.xlabel(features[i])
        plt.ylabel(features[j])
        plt.title(f"{features[i]} vs {features[j]}")
        plt.legend()
        plt.tight_layout()
        plt.savefig(f"plot_{features[i]}_vs_{features[j]}.png")
        plt.close()
