package metaheurdrones;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Individual implements Comparable<Individual> {

    // Drones autonomy in Km
    public static final double[] autonomy = {5.0, 7.5, 10.0, 26.8, 50.0};

    //Drones max speed in Km/h
    public static final double[] speed = {10.0, 15.3, 7.5, 17.4, 20.5};

    // Distances between zones and depot in Km
    public static final double[] distances = {23.00, 12.2, 2.0, 3.5, 4.6};

    private Integer[] genotype;

    public Individual() {

        genotype = new Integer[autonomy.length];
        randomise();
    }

    public Individual(Individual ind) {
        super();
        setGenotype(ind.genotype);
    }

    public Individual getBestNeigbour() {
        
        Individual neighbour = new Individual(this);
        neighbour.swap(0, 1);

        for (int i = 0; i < this.genotype.length; i++) {

            for (int j = i+1; j < this.genotype.length; j++) {

                Individual neighbour2 = new Individual(this);
                neighbour2.swap(i, j);
                
                if (neighbour2.fitness() <= neighbour.fitness())
                {
                    neighbour = neighbour2;
                }

            }
        }

        return neighbour;
    }

    public boolean isLegal() {

        for (int i = 0; i < this.genotype.length; i++) {
            // Each drone has to travel twice (round trip)
            double remainingAutonomy = autonomy[this.genotype[i]] - 2 * distances[i];

            if (remainingAutonomy < 0) {
                return false;
            }

        }

        return true;
    }

    public double fitness() {
        double fitness = 0;

        for (int i = 0; i < this.genotype.length; i++) {
            // Each drone has to travel twice (round trip)
            double remainingAutonomy = autonomy[this.genotype[i]] - 2 * distances[i];

            double timeSpent = (2 * distances[i]) / (speed[this.genotype[i]]);

            if (remainingAutonomy < 0) {
                timeSpent *= this.getGenotype().length; // Penalise if ilegal solution
                // Avoiding to return maxdouble constant to introduce soft oriented solution
            }

            //Fitnes equals to time used by the last drone (the slower one)
            fitness = timeSpent > fitness ? timeSpent : fitness;

        }

        return fitness;
    }

    public void mutate() {
        int i = 0, j = 0;

        while (i == j) {
            i = new Random().nextInt(genotype.length);
            j = new Random().nextInt(genotype.length);
        }

        swap(i, j);
    }

    private void swap(int i, int j) {

        Integer aux = this.genotype[i];
        this.genotype[i] = this.genotype[j];
        this.genotype[j] = aux;
    }

    public void randomise() {
        List<Integer> l = new ArrayList<>();

        for (int i = 0; i < genotype.length; i++) {
            l.add(i);
        }

        Collections.shuffle(l);

        l.toArray(this.genotype);
    }

    public Integer[] getGenotype() {
        return genotype;
    }

    public void setGenotype(Integer[] genotype) {
        this.genotype = new Integer[genotype.length];
        System.arraycopy(genotype, 0, this.genotype, 0, this.genotype.length);
    }

    @Override
    public String toString() {
        return "\n " + (isLegal() ? "" : " ILEGAL ***") + " Individual{" + "genotype=" + Arrays.toString(genotype) + '}' + " Fitness: " + fitness();
    }

    public static Individual crossover(Individual ind1, Individual ind2) {

        Integer[] p1 = new Individual(ind1).getGenotype();
        Integer[] p2 = new Individual(ind2).getGenotype();

        Individual son = new Individual();

        for (int i = 0; i < son.genotype.length; i++) {
            Integer drone = new Random().nextBoolean() ? p1[0] : p2[0];
            deleteDrone(p1, drone);
            deleteDrone(p2, drone);

            son.genotype[i] = drone;
        }

        if (new Random().nextBoolean()) {
            son.mutate();
        }

        return son;
    }

    private static void deleteDrone(Integer[] genotype, Integer drone) {
        int index = getIndex(genotype, drone);

        for (int i = index; i < genotype.length - 1; i++) {
            genotype[i] = genotype[i + 1];
        }

    }

    private static int getIndex(Integer[] genotype, Integer drone) {
        int index = -1;

        for (int i = 0; i < genotype.length; i++) {
            if (genotype[i].equals(drone)) {
                return i;
            }
        }

        return index;
    }

    @Override
    public int compareTo(Individual o) {
        return (this.fitness() - o.fitness()) < 0 ? -1 : ((this.fitness() - o.fitness() == 0) ? 0 : 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Individual other = (Individual) obj;
        return Arrays.deepEquals(this.genotype, other.genotype);
    }

}
