#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include "pso.h"

/* My particular fitness function */
double sphere(double *vec, int d) {
    double sum = 0.0;
    for (int i = 0; i < d; i++) {
        sum += pow(vec[i], 2);
    }
    return sum;
}


int main(int argc, char const *argv[]) {
    /* Fitness function "object" (function pointer)  */
    FITNESS_FUNCTION f_func;
    /* Set the function address to the function pointer */
    f_func = &sphere;
    /* set the fitness function pre-defined */
    pso_setFitnessFunction(f_func);
    /* Run the algorithm */
    pso_run();
    return EXIT_SUCCESS;
}
