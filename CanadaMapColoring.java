import java.util.*;

public class CanadaMapColoring {
    // All Regions listed 
    static String[] regions = {"BC", "AB", "SK", "MB", "ON", "QC", "NB", "NS", "PEI", "NL", "NU", "NT", "YT"};

    // List of all neighboring states
    static Map<String, List<String>> neighbors = neighboringStates();

    // Colors
    static String[] colors = {"b", "r", "o", "j"};

    public static void main(String[] args) {
        // Makes sure that the k value is provided by the user. 
        if (args.length == 0) {
            System.out.println("Provide the number (1 to 4) of colors (k) as a command-line argument.");
            return;
        }
        
        int k = Integer.parseInt(args[0]);

        // Validating that its a correct k value. 
        if (k < 1 || k > 4) {
            System.out.println("k should be between 1 and 4.");
            return;
        }

        colors = Arrays.copyOf(colors, k);

        // Making the intial state k
        String[] initialState = produceInitialState(k);

        // Execute hill-climbing search
        String[] solution = hillClimbingAlgorithm(initialState);

        // Printing the results.
        printSolution(solution);
    }

    // Generates the initial state based on k colors
    private static String[] produceInitialState(int k) {
        String[] state = new String[regions.length];
        for (int i = 0; i < regions.length; i++) {
            state[i] = colors[i % k];  // Cycle through the available k colors
        }
        return state;
    }

    // Hill-climbing algorithm
    private static String[] hillClimbingAlgorithm(String[] state) {
        String[] currentState = Arrays.copyOf(state, state.length);
        int[] initialCosts = totalRegionCosts(currentState);

        while (true) {
            String[] nextState = null;
            int[] nextCosts = initialCosts.clone();

            // Processing through all colors. Successor function. 
            for (int i = 0; i < regions.length; i++) {
                for (String color : colors) {
                    if (!currentState[i].equals(color)) {
                        String[] newState = Arrays.copyOf(currentState, currentState.length);
                        newState[i] = color;
                        int[] newCosts = totalRegionCosts(newState);

                        // Choose the state with the fewest conflicts. Looping through to check for a solution state. 
                        if (calculatedAllCosts(newCosts) < calculatedAllCosts(nextCosts)) {
                            nextState = newState;
                            nextCosts = newCosts;
                        }
                    }
                }
            }

            // Checks to see if the current state is a goal State. If state has no conflicts it has 0 costs. 
            if (nextState == null || calculatedAllCosts(nextCosts) >= calculatedAllCosts(initialCosts)) {
                break;
            }

            currentState = nextState;
            initialCosts = nextCosts;
        }

        return currentState;
    }

    // Neighbors
    private static Map<String, List<String>> neighboringStates() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("BC", Arrays.asList("AB", "YT", "NT"));
        map.put("AB", Arrays.asList("BC", "SK", "NT"));
        map.put("SK", Arrays.asList("AB", "MB", "NT"));
        map.put("MB", Arrays.asList("SK", "ON", "NU"));
        map.put("ON", Arrays.asList("MB", "QC"));
        map.put("QC", Arrays.asList("ON", "NB", "NL"));
        map.put("NB", Arrays.asList("QC", "NS", "PEI"));
        map.put("NS", Arrays.asList("NB", "PEI"));
        map.put("PEI", Arrays.asList("NB", "NS"));
        map.put("NL", Arrays.asList("QC"));
        map.put("NU", Arrays.asList("MB", "NT"));
        map.put("NT", Arrays.asList("BC", "AB", "SK", "NU", "YT"));
        map.put("YT", Arrays.asList("NT", "BC"));
        return map;
    }

    // Calculating the cost of each region. Cost Function. 
    private static int[] totalRegionCosts(String[] state) {
        int[] regionCosts = new int[regions.length];

        // Check each region and its neighbors for conflicts
        for (int i = 0; i < regions.length; i++) {
            String region = regions[i];
            String color = state[i];
            regionCosts[i] = 0; 

            for (String neighbor : neighbors.get(region)) {
                int neighborIndex = Arrays.asList(regions).indexOf(neighbor);
                if (neighborIndex >= 0 && state[neighborIndex].equals(color)) {
                    regionCosts[i] = 1;  // Conflict found, set cost to 1
                    break;  
                }
            }
        }

        return regionCosts;
    }

    // Total the cost of all conflicts 
    private static int calculatedAllCosts(int[] costs) {
        int sum = 0;
        for (int cost : costs) {
            sum += cost;
        }
        return sum;
    }

    // Print the solution
    private static void printSolution(String[] state) {
        System.out.println("Final Results of the Map with Costs");
        int[] costs = totalRegionCosts(state);

        for (int i = 0; i < regions.length; i++) {
            System.out.println(regions[i] + ": " + state[i] + " (Cost: " + costs[i] + ")");
        }

        int totalCost = calculatedAllCosts(costs);
        System.out.println("Total conflicts: " + totalCost);
    }
}
