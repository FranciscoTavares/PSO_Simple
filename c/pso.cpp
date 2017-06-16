#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <time.h>
#include <float.h>
#include <string.h>
#include "randomlib.h"
#include "pso.h"


double pos[SWARM_SIZE][DIMENSION];
double vel[SWARM_SIZE][DIMENSION];
double fit[SWARM_SIZE];
double pbest[SWARM_SIZE][DIMENSION];
double fitpbest[SWARM_SIZE];
double gbest[DIMENSION];
double fitgbest = DBL_MAX;

unsigned int it = 0;
double inertia = 0.0;

FITNESS_FUNCTION *f_function;




void pso_setFitnessFunction(FITNESS_FUNCTION ff) {
    f_function = &ff;
}

const bool compareAccordingProblemType(const double a, const double b) {
    if(IS_MINIMIZATION) {
        return (a < b);
    } else {
        return (a > b);
    }
}

/* Initialize random number generator from randomlib.h */
void initialize_rand() {
    unsigned int i, j;
    srand((unsigned int)time((time_t *)NULL));
    i = (unsigned int) (31329.0 * rand() / (RAND_MAX + 1.0));
    j = (unsigned int) (30082.0 * rand() / (RAND_MAX + 1.0));
    RandomInitialise(i,j);
}

/* Initialize the pos[][] vector related to particles' position */
void initialize_position() {
    unsigned int idx, dim;
    for (idx = 0; idx < SWARM_SIZE; idx++) {
        for (dim = 0; dim < DIMENSION; dim++) {
            pos[idx][dim] = RandomDouble(X_LOWER, X_UPPER);
            pbest[idx][dim] = pos[idx][dim];
        }
    }
}

/* Initialize the vel[][] vector related to particles' velocity */
void initialize_velocity() {
    unsigned int idx, dim;
    for (idx = 0; idx < SWARM_SIZE; idx++) {
        for (dim = 0; dim < DIMENSION; dim++) {
            vel[idx][dim] = RandomDouble(V_MIN, V_MAX);
        }
    }
}

void update_fitness() {
    unsigned int idx, dim;
    for (idx = 0; idx < SWARM_SIZE; idx++) {
        fit[idx] = (*f_function)(pos[idx], DIMENSION);
    }
}

void update_pbest() {
    unsigned int idx, dim;
    for (idx = 0; idx < SWARM_SIZE; idx++) {
        if(compareAccordingProblemType(fit[idx], fitpbest[idx]) || it == 0) {
            fitpbest[idx] = fit[idx];
            memmove((void *)&pbest[idx],
                    (void *)&pos[idx],
                    (sizeof(double)*DIMENSION) );
        }
    }
}

void update_gbest() {
    unsigned int idx, dim;
    for (idx = 0; idx < SWARM_SIZE; idx++) {
        if(compareAccordingProblemType(fitpbest[idx], fitgbest) || it == 0) {
            fitgbest = fitpbest[idx];
            memmove((void *)&gbest,
                    (void *)&pbest[idx],
                    (sizeof(double)*DIMENSION) );
        }
    }
}

void limit_position(const unsigned int idx, const unsigned int dim) {
    if(LIMIT_POSITION) {
        if(pos[idx][dim] > X_UPPER) {
            pos[idx][dim] = X_UPPER;
        } else if(pos[idx][dim] < X_LOWER) {
            pos[idx][dim] = X_LOWER;
        }
    }
}

void limit_velocity(const unsigned int idx, const unsigned int dim) {
    if(LIMIT_VELOCITY) {
        if(vel[idx][dim] > V_MAX) {
             vel[idx][dim] = V_MAX;
        } else if(vel[idx][dim] < V_MIN) {
             vel[idx][dim] = 0.0;
        }
    }
}

void update_velocity() {
    unsigned int idx, dim;
    double rho1, rho2;

    for (idx = 0; idx < SWARM_SIZE; idx++) {
        inertia = W_CONSTANT; /* get the current inertia value */

        for (dim = 0; dim < DIMENSION; dim++) {
            rho1 = C1 * RandomDouble(0.0, 1.0);
            rho2 = C2 * RandomDouble(0.0, 1.0);

            /* Update the particle velocity */
            vel[idx][dim] = inertia * vel[idx][dim] + \
                    rho1 * (pbest[idx][dim] - pos[idx][dim]) + \
                    rho2 * (gbest[dim] - pos[idx][dim]);

            /* Update the particle position */
            pos[idx][dim] += vel[idx][dim];

            limit_position(idx, dim);
            limit_velocity(idx, dim);
        }
    }
}


void pso_run() {
    bool is_error_reached = false;
    clock_t t;
    unsigned int idx, dim;

    initialize_rand();
    initialize_position();
    initialize_velocity();

    t = clock(); // start time
    while (it < MAX_ITER && !is_error_reached) {
        update_fitness();
        update_pbest();
        update_gbest();

        /*if(it == 0) {
            printf("\tPARTICLES START FITNESS:\n");
            for (idx = 0; idx < SWARM_SIZE; idx++) {
                printf("%f\n", fit[idx]);
            }
            printf("\n");
        }*/

        if(compareAccordingProblemType(fitgbest, STOP_CRITERIUM)) {
            printf(
                "\n\n--------------------------------------------\n"
                "> STOP CRITERIUM (%f) REACHED (iteration %d)."
                "\n--------------------------------------------\n", STOP_CRITERIUM, it);
            is_error_reached = true;
            break;
        }
        if((it+1) == MAX_ITER) {
            printf(
                "\n\n--------------------------------------------\n"
                "> THE MAX NUMBER OF ITERATIONS (%d) WAS REACHED."
                "\n--------------------------------------------\n", MAX_ITER);
        }

        update_velocity();
        it++;
    }
    t = clock() - t;

    printf("\n***** GBEST *****\n");
    for (idx = 0; idx < DIMENSION; idx++) {
        printf("\t%.3f\n", gbest[idx]);
    }
    printf("\nFitness gbest: %f\n\n", fitgbest);
    printf("> ELAPSED TIME: %f\n\n\n", ((double)t)/CLOCKS_PER_SEC);
}
