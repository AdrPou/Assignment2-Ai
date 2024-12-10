public class KnapsackSolver {
    public static void main(String[] args) {
        // Number of items
        int n = 5;
        // Number of knapsacks
        int m = 2;
        // Values of items
        int[] values = { 60, 100, 120, 80, 50 };
        // Weights of items
        int[] weights = { 10, 20, 30, 15, 10 };
        // Capacities of knapsacks
        int[] capacities = { 50, 50 };

        // Call the greedy algorithm
        int[][] greedySolution = greedyKnapsack(n, m, values, weights, capacities);
        System.out.println("Greedy Algorithm Solution:");
        printSolution(greedySolution);

        // Call the neighborhood search algorithm
        int[][] improvedSolution = neighborhoodSearch(n, m, values, weights, capacities, greedySolution);
        System.out.println("Improved Solution with Neighborhood Search:");
        printSolution(improvedSolution);
    }

    public static int[][] greedyKnapsack(int n, int m, int[] values, int[] weights, int[] capacities) {
        // Calculate benefit-to-weight ratio
        double[] ratio = new double[n];
        for (int i = 0; i < n; i++) {
            ratio[i] = (double) values[i] / weights[i];
        }

        // Sort items by ratio descending
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        java.util.Arrays.sort(indices, (a, b) -> Double.compare(ratio[b], ratio[a]));

        // Initialize knapsacks and remaining capacities
        int[][] knapsacks = new int[m][n];
        int[] remainingCapacity = capacities.clone();

        // Greedy item assignment
        for (int i : indices) {
            for (int j = 0; j < m; j++) {
                if (weights[i] <= remainingCapacity[j]) {
                    knapsacks[j][i] = 1; // Assign item i to knapsack j
                    remainingCapacity[j] -= weights[i];
                    break;
                }
            }
        }
        return knapsacks;
    }

    public static int[][] neighborhoodSearch(int n, int m, int[] values, int[] weights, int[] capacities,
            int[][] initialSolution) {
        int[][] currentSolution = deepCopy(initialSolution);
        int currentValue = calculateTotalValue(currentSolution, values);
        boolean improved = true;

        while (improved) {
            improved = false;
            int[][] bestNeighbor = deepCopy(currentSolution);
            int bestValue = currentValue;

            // Check neighbors by moving items between knapsacks
            for (int j1 = 0; j1 < m; j1++) {
                for (int i = 0; i < n; i++) {
                    if (currentSolution[j1][i] == 1) {
                        for (int j2 = 0; j2 < m; j2++) {
                            if (j1 != j2 && weights[i] <= capacities[j2]) {
                                // Create a neighbor by moving item i
                                int[][] neighbor = deepCopy(currentSolution);
                                neighbor[j1][i] = 0; // Remove from knapsack j1
                                neighbor[j2][i] = 1; // Add to knapsack j2

                                // Calculate neighbor's value
                                int neighborValue = calculateTotalValue(neighbor, values);
                                if (neighborValue > bestValue) {
                                    bestValue = neighborValue;
                                    bestNeighbor = deepCopy(neighbor);
                                    improved = true;
                                }
                            }
                        }
                    }
                }
            }
            currentSolution = deepCopy(bestNeighbor);
            currentValue = bestValue;
        }
        return currentSolution;
    }

    private static int calculateTotalValue(int[][] solution, int[] values) {
        int totalValue = 0;
        for (int j = 0; j < solution.length; j++) {
            for (int i = 0; i < solution[j].length; i++) {
                if (solution[j][i] == 1) {
                    totalValue += values[i];
                }
            }
        }
        return totalValue;
    }

    private static int[][] deepCopy(int[][] array) {
        int[][] copy = new int[array.length][];
        for (int i = 0; i < array.length; i++) {
            copy[i] = array[i].clone();
        }
        return copy;
    }

    public static void printSolution(int[][] solution) {
        for (int j = 0; j < solution.length; j++) {
            System.out.print("Knapsack " + (j + 1) + ": ");
            for (int i = 0; i < solution[j].length; i++) {
                if (solution[j][i] == 1) {
                    System.out.print(i + " ");
                }
            }
            System.out.println();
        }
    }

}
