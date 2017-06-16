
/**
 *
 * @author gian
 */
public class PsoMain {

    public Double[][] pos;
    public Double[][] vel;
    public Double[] fitness;
    public Double[] gBest;
    public Double fitgBest;
    public Double[][] pBest;
    public Double[] fitpbest;

    private int iteration = 0;

    public PsoMain() {
        pos = new Double[Params.getInstance().getSwarmSize()][Params.getInstance().getDimension()];
        vel = new Double[Params.getInstance().getSwarmSize()][Params.getInstance().getDimension()];
        fitness = new Double[Params.getInstance().getSwarmSize()];

        pBest = new Double[Params.getInstance().getSwarmSize()][Params.getInstance().getDimension()];
        fitpbest = new Double[Params.getInstance().getSwarmSize()];

        gBest = new Double[Params.getInstance().getDimension()];
        fitgBest = 0.0;
    }

    private Double sphere(Double[] vec) {
        Double sum = 0.0;
        for (Double dim : vec) {
            sum += dim * dim;
        }
        return sum;
    }

    public void printAll() {
        for (int idx = 0; idx < Params.getInstance().getSwarmSize(); idx++) {
            System.out.println("\nParticula " + idx + ":");
            System.out.println("Fitness Posição: " + fitness[idx]);
            System.out.print("Posição > ");
            for (int d = 0; d < Params.getInstance().getDimension(); d++) {
                System.out.print(pos[idx][d] + " ");
            }
            System.out.print("\nVelocidade > ");
            for (int d = 0; d < Params.getInstance().getDimension(); d++) {
                System.out.print(vel[idx][d] + " ");
            }
            System.out.print("\npBest > ");
            for (int d = 0; d < Params.getInstance().getDimension(); d++) {
                System.out.print(pBest[idx][d] + " ");
            }
            System.out.println("\nFitness pBest > " + fitpbest[idx]);
        }
    }

    private void initParticulas() {

        for (int idx = 0; idx < Params.getInstance().getSwarmSize(); idx++) {
            for (int d = 0; d < Params.getInstance().getDimension(); d++) {
                pos[idx][d] = Math.random()
                        * (Params.getInstance().getMaxPos() - Params.getInstance().getMinPos())
                        + Params.getInstance().getMinPos();
                pBest[idx][d] = pos[idx][d];
                vel[idx][d] = Math.random()
                        * (Params.getInstance().getMaxVel() - Params.getInstance().getMinVel())
                        + Params.getInstance().getMinVel();
            }
            fitpbest[idx] = 0.0;
        }
        System.out.println("Particulas inicializadas...");
    }

    private void updateFitness() {
        for (int idx = 0; idx < Params.getInstance().getSwarmSize(); idx++) {
            fitness[idx] = sphere(pos[idx]);
        }
    }

    private void update_pbest() {
        int idx;

        for (idx = 0; idx < Params.getInstance().getSwarmSize(); idx++) {

            if (Params.getInstance().compareAccordingProblemType(fitness[idx], fitpbest[idx]) || iteration == 0) {
                fitpbest[idx] = fitness[idx];
                System.arraycopy(pos[idx], 0, pBest[idx], 0, Params.getInstance().getDimension());
            }
        }
    }

    private void limit_position(int idx, int dim) {
        if (Params.getInstance().isLimitPosition()) {
            if (pos[idx][dim] > Params.getInstance().getMaxPos()) {
                pos[idx][dim] = Params.getInstance().getMaxPos();
            } else if (pos[idx][dim] < Params.getInstance().getMinPos()) {
                pos[idx][dim] = Params.getInstance().getMinPos();
            }
        }
    }

    private void limit_velocity(int idx, int dim) {
        if (Params.getInstance().isLimitVelocity()) {
            if (vel[idx][dim] > Params.getInstance().getMaxVel()) {
                vel[idx][dim] = Params.getInstance().getMaxVel();
            } else if (vel[idx][dim] < Params.getInstance().getMinVel()) {
                vel[idx][dim] = 0.0;
            }
        }
    }

    private void update_gbest() {
        int idx;

        for (idx = 0; idx < Params.getInstance().getSwarmSize(); idx++) {
            if (Params.getInstance().compareAccordingProblemType(fitness[idx], fitgBest)
                    || (iteration == 0 && fitgBest == 0.0)) {
                fitgBest = fitness[idx];
                System.arraycopy(pos[idx], 0, gBest, 0, Params.getInstance().getDimension());
                System.out.println(fitgBest);
            }
        }
    }

    private void update_velocity() {
        int idx, dim;
        double rho1, rho2;
        double inertia = Params.getInstance().getInertia();

        for (idx = 0; idx < Params.getInstance().getSwarmSize(); idx++) {
            for (dim = 0; dim < Params.getInstance().getDimension(); dim++) {

                rho1 = Params.getInstance().getC1() * Math.random();
                rho2 = Params.getInstance().getC2() * Math.random();

                /* Update the particle velocity */
                vel[idx][dim] = inertia * vel[idx][dim]
                        + rho1 * (pBest[idx][dim] - pos[idx][dim])
                        + rho2 * (gBest[dim] - pos[idx][dim]);

                /* Update the particle position */
                pos[idx][dim] += vel[idx][dim];

                limit_position(idx, dim);
                limit_velocity(idx, dim);
            }
        }
    }

    void run() {
        boolean error_reached = false;

        initParticulas();
        System.out.println("STATUS INICIAL DAS PARTICULAS:");
        printAll();
        System.out.println("\n\nRUNNING PSO...\n\n");

        while (iteration < Params.getInstance().getMaxIterations() || !error_reached) {
            updateFitness();
            update_pbest();
            update_gbest();

            if (Params.getInstance().compareAccordingProblemType(fitgBest,
                    Params.getInstance().getStopCriterium())) {
                System.out.println(
                        "\n\n--------------------------------------------\n"
                        + "> STOP CRITERIUM (" + Params.getInstance().getStopCriterium()
                        + ") REACHED (iteration " + iteration + ")."
                        + "\n--------------------------------------------\n");
                error_reached = true;
                break;
            }
            if ((iteration + 1) == Params.getInstance().getMaxIterations()) {
                System.out.println(
                        "\n\n--------------------------------------------\n"
                        + "> THE MAX NUMBER OF ITERATIONS (" + Params.getInstance().getMaxIterations()
                        + ") WAS REACHED.\n--------------------------------------------\n");
            }
            update_velocity();
            iteration++;
        }

        System.out.println("FINAL RESULTS:");
        System.out.print("gBest positions: ");
        for (int d = 0; d < Params.getInstance().getDimension(); d++) {
            System.out.print(gBest[d] + " ");
        }
        System.out.println("\nFitness gBest > " + fitgBest + "\n\n");
//        printAll();
    }

    public static void main(String[] args) {
        PsoMain pso = new PsoMain();
        pso.run();
    }

}
