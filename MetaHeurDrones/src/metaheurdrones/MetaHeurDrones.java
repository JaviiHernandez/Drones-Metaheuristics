package metaheurdrones;

import java.util.Random;


public class MetaHeurDrones {
    
    private static double temperature = 50;
    private static Individual bestIndividual, candidate, neighbour;
    private static final double COOLING_RATE = 0.1;

    public static void main(String[] args) {
     
        
        //Genetic algorithm
        
        System.out.println("\n---GENETIC ALGORITHM---\n");
        
        Population pop = new Population(10);
        System.out.println("Init pop: ");
        System.out.println(pop);
        
        System.out.println("\n\n");
        
        for (int i = 0; i < 20; i++)
        {
            pop.evolve();
        }
        
        System.out.println("Final pop: " + pop.getPopulation());
        
        
        System.out.println("-------------------------------");
        
        // Simulated annealing
        
        System.out.println("\n---SIMULATED ANNEALING---\n");
        
        candidate = neighbour = bestIndividual = new Individual();
        
        while (temperature > 1) {
            
            
            neighbour = candidate.getBestNeigbour();

            double delta = neighbour.fitness()- candidate.fitness();

            if (delta < 0 || ap()) { //Minimise

                candidate = neighbour;

                if (candidate.fitness()< bestIndividual.fitness()) {  //Minimise
                    
                    bestIndividual = candidate;
                }
            }
            temperature *= COOLING_RATE;
        }
        
        
        
        System.out.println("BEST INDIVIDUAL: "+bestIndividual);
        
    }
    
    
    // Acceptance probability - Minimise
    private static boolean ap() {
       return (new Random().nextDouble() < Math.exp(-(candidate.fitness()- bestIndividual.fitness()) / (temperature)));
    }
    
}
