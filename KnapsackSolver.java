public class KnapsackSolver {
    public static void main(String[] args) {
        // Number of items
        int n = 10;
        // Number of knapsacks
        int m = 2;
        // Values of items
        int[] values = { 100, 90, 60, 40, 60, 50, 70, 20, 10, 30 };
        // Weights of items
        int[] weights = { 50, 40, 30, 10, 20, 10, 30, 10, 5, 20 };
        // Capacities of knapsacks
        int[] capacities = { 100, 100 };

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

            // Explore neighbors
            for (int j1 = 0; j1 < m; j1++) {
                for (int i1 = 0; i1 < n; i1++) {
                    if (currentSolution[j1][i1] == 1) {
                        // Move item from one knapsack to another
                        for (int j2 = 0; j2 < m; j2++) {
                            if (j1 != j2 && weights[i1] <= capacities[j2]) {
                                int[][] neighbor = deepCopy(currentSolution);
                                neighbor[j1][i1] = 0; // Remove from knapsack j1
                                neighbor[j2][i1] = 1; // Add to knapsack j2

                                int neighborValue = calculateTotalValue(neighbor, values);
                                if (isValidSolution(neighbor, weights, capacities) && neighborValue > bestValue) {
                                    bestValue = neighborValue;
                                    bestNeighbor = deepCopy(neighbor);
                                    improved = true;
                                }
                            }
                        }
                    }
                }
            }

            // Update solution
            currentSolution = deepCopy(bestNeighbor);
            currentValue = bestValue;

            // Debugging
            System.out.println("Current Value: " + currentValue);
            printSolution(currentSolution);
        }

        return currentSolution;
    }
    public static boolean isValidSolution(int[][] solution, int[] weights, int[] capacities) {
        for (int j = 0; j < solution.length; j++) {
            int totalWeight = 0;
            for (int i = 0; i < solution[j].length; i++) {
                if (solution[j][i] == 1) {
                    totalWeight += weights[i];
                }
            }
            if (totalWeight > capacities[j]) {
                return false; // Exceeds capacity
            }
        }
        return true;
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
