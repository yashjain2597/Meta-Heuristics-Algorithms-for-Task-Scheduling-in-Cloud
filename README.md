# Meta-Heuristics-Algorithms-for-Task-Scheduling-in-Cloud

# Implementation of Greedy Particle Swarm Optimization,HSGA and Hybrid(GA+PSO) for the purpose of Task Scheduling in cloud computing environment

Cloud Service providers are facing problem with optimized scheduling of tasks to the virtual machines in cloud computing environment. Scheduling of resources must be done in such a way that it reduces the total resource cost, reduces total time of execution, improves throughtput, reduces failure, balances server load and provides the highest possible QOS. The problem of task scheduling comes under the category of NP-hard problems. Meta-heuristics algorithms find the best or near-best solution in reasonable amount of time by making random choices to find the solution.

In this work three different meta-heuristic algorithms which were GPSO, HSGA and Hybrid (GA+PSO) were implemented for task scheduling and a comparison was made between them.

For implementing this project I have used Cloudsim 4.0 which is an open source framework used to simulate the cloud computing infrastructure and services. It is entirely written in JAVA and it enables the modelling and simulation of core features of cloud like: task queues, event processing, cloud entity formation (data centers, data center broker etc.), communication between entities, borker policy implementation and so on. With this I have used Eclipse IDE and JAVA version 11.0.10.

Comparing the algorithms on the basis of execution time indicates that Hybrid (GA+PSO) performs the best followed by HSGA and then GPSO. 
