#ifndef PSO_H
#define PSO_H


static const unsigned int MAX_ITER        = 10000;
static const unsigned int SWARM_SIZE      = 30;
static const unsigned int DIMENSION       = 3;

static bool LIMIT_POSITION   = false;
static double X_UPPER        = 30.0; //100.0;  // limite máximo de posição da partícula
static double X_LOWER        = -X_UPPER;  // limite mínimo de posição da partícula

static bool LIMIT_VELOCITY   = true;
static double V_MAX          = 100000.0; // velocidade máxima da partícula
static double V_MIN          = -V_MAX; // velocidade mínima da partícula

static double STOP_CRITERIUM = 0.001;    // critério de parada (valor do gBest)
static bool IS_MINIMIZATION  = true;

static double C1             = 1.49445;  // cognitive coefficient
static double C2             = 1.49445;  // social coefficient
static double W_CONSTANT     = 0.729;
static double W_MAX          = 0.9;   // max inertia weight value
static double W_MIN          = 0.4;   // min inertia weight value



typedef double (*FITNESS_FUNCTION)(double *, int);

void pso_setFitnessFunction(FITNESS_FUNCTION ff);
void pso_run();


#endif
