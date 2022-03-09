### LabProject01
First lab project

# NEURAL NETWORKS LABORATORY PROJECT 
In this project, you must learn a function from several training patterns by using a 
multilayer perceptron neural network. The function to be learned has two real numbers 
(x,y) as inputs and a single real number as output: 
F(x,y) = sin(x) * cos(y) 
We will assume that both inputs belong to the interval [-pi,pi]. The output is in the 
interval [-1,1]. 
You will use the [Encog library](https://www.heatonresearch.com/encog/ "Encog Homepage") as the implementation of the multilayer perceptron:
You must develop a Java console application which does the following: 
1. Generate at random 1000 training samples of the function. You must 
generate the samples of (x,y) uniformly at random on the square [-pi,pi] x 
[-pi,pi]. Then you must compute the value of the function F(x,y) at those 
points. 
2. Generate at random 1000 validation samples of the function. You must 
generate the samples of (x,y) uniformly at random on the square [-pi,pi] x [-
pi,pi]. Then you must compute the value of the function F(x,y) at those 
points. 
3. Train a multilayer perceptron with the 1000 training samples of the function. 
You must choose the number of hidden neurons, the learning parameters, 
and the number of epochs. The training error and the validation error must be 
printed out for each training epoch. 
4. Generate test samples of the function. You must generate the test samples of 
(x,y) at equally spaced locations in a grid of 100x100 points on the square 
[-pi,pi] x [-pi,pi]. Then you must compute the value of the function F(x,y) at 
those points. 
5. Simulate the multilayer perceptron on the test samples, compute the mean 
squared error for the test samples, and print it out on the console. 
A plain text file named output.txt must be generated which contains the full 
output of the program.

## Optional task: 
 Generate a plot to show the evolution of the training error and the validation 
error at each training epoch. Then you should discuss the plotted results. 
 Generate a 3D figure with the true function and the learned function. Both must 
be plotted on a grid of 100x100 points on the square [-pi,pi] x [-pi,pi]. 
Tentative schedule: 
 Laboratory session 1: develop the training, validation, and test sample 
generation. 
 Laboratory session 2: develop the training code for the multilayer perceptron. 
 Laboratory session 3: present the final result to the class with the help of a slide 
show and do a practical demonstration of the software (maximum 10 minutes overall). 
All the materials (slide show, source code and output.txt file) must be submitted to the 
associated virtual campus task.
