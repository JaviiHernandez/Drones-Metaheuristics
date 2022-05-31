package metaheurdrones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Population {

    public List<Individual> population;

    public Population(int initpop) {
        population = new ArrayList<>();

        for (int i = 0; i < initpop; i++) {
            population.add(new Individual());
        }
    }

    public void evolve() {
        
        Collections.sort(this.population);

        List<Individual> nextgeneration = new ArrayList<>();

        while (nextgeneration.size() < population.size() / 2) {
            Individual p1, p2;
            p1 = this.population.get(new Random().nextInt(this.population.size()));
            p2 = this.population.get(new Random().nextInt(this.population.size()));
            
            nextgeneration.add(Individual.crossover(p1, p2)); 
        }
        
        int i = 0;
         while (nextgeneration.size() < population.size()) {
            nextgeneration.add(population.get(i++)); 
        }
         
         population = nextgeneration;

        Collections.sort(this.population);
    }

    public List<Individual> getPopulation() {
        return population;
    }

    @Override
    public String toString() {
        return "Population{" + "population=" + population + '}';
    }

}
