MarsGrid Energy Network Simulator

Overview

    MarsGrid is a simulation system designed to manage the energy network of
an autonomous colony on Mars. The application models energy producers,
consumers, and storage units, and simulates how energy is balanced
across the network during discrete time steps (ticks).

The system is implemented using Object-Oriented Programming principles,
including abstraction, inheritance, encapsulation, and polymorphism.


    Architecture

Base Class

    NetworkComponent - Common attributes: `id`, `statusOperational` -
Parent class for all grid components

Energy Producers

    Abstract class: EnergyProducer - Defines `calculateProduction()` and
`displayDetails()`

    Concrete implementations: - SolarPanel --- production depends on
sunlight factor - WindTurbine --- production depends on wind
factor - NuclearReactor --- constant production if operational

Energy Consumers

    Abstract class: EnergyConsumer

    Attributes: - energyDemand - priority - isPowered

    Methods: - `getCurrentDemand()` - `disconnectFromGrid()` - getters for
priority and demand

    Concrete implementations: - LifeSupportSystem (priority 1) -
ScientificLaboratory (priority 2) - LightingSystem (priority 3)

Energy Storage
    Battery- maximumCapacity - storedEnergy

    Methods: - `charge()` --- stores energy without exceeding capacity -
`discharge()` --- provides energy if available - `displayDetails()`

Simulation Logic

The system operates in simulation ticks.

Each tick performs the following steps:

1.  Reset all consumers as powered.
2.  Calculate total production.
3.  Calculate total demand.
4.  If surplus exists → charge batteries.
5.  If deficit exists → discharge batteries.
6.  If deficit remains → disconnect consumers by priority (3, then 2).
7.  If deficit still remains → BLACKOUT.
8.  Stop future simulation when blackout occurs.
9.  Store simulation events in history.

Command System

The application is interactive and processes commands from the console.

Main Commands

-   Add producer
-   Add consumer
-   Add battery
-   Simulate next tick
-   Set component operational status
-   Display grid status
-   Display event history
-   Exit simulator

All inputs are validated, including unique IDs and positive numeric
values.

Technologies Used

-   Java
-   Object-Oriented Programming
-   ArrayLists for component management
-   Command-line interface

 How to Run

javac -d bin src/**/*.java
java -cp bin Main

Example Simulation Behavior

-   Energy production and consumption are balanced dynamically.
-   Batteries store surplus energy and supply deficit energy.
-   Lower-priority consumers are disconnected if needed.
-   System enters BLACKOUT if critical demand cannot be sustained.

Event History

The system records major events such as: - consumer disconnections -
deficits - blackout events
