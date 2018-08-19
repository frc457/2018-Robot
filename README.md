# 2018-Robot

![FRC 457](https://img.shields.io/badge/FRC-457-blue.svg)

FRC 457's code for our 2018 robot, written in Java using [WPILibJ](https://github.com/wpilibsuite/allwpilib), [Phoenix](https://github.com/CrossTheRoadElec/Phoenix-Documentation), [Pathfinder](https://github.com/JacisNonsense/Pathfinder) and [GradleRIO](https://github.com/Open-RIO/GradleRIO).

## Features
- Path generation using Jaci's [Pathfinder](https://github.com/JacisNonsense/Pathfinder)
- A [path manager](https://github.com/frc457/2018-Robot/blob/master/src/main/java/org/greasemonkeys457/robot2018/util/paths/Path.java) that:
  - Saves paths
  - Loads saved paths
  - Validates saved paths
- An [autonomous routine selector](https://github.com/frc457/2018-Robot/blob/master/src/main/java/org/greasemonkeys457/robot2018/commands/AutonomousSelector.java) based on starting position and goal
