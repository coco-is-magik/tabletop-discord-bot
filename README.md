# Tabletop Discord Bot

A Discord bot designed to assist with managing tabletop RPGs, focusing on character sheets, rulebooks, dice rolls, and combat tracking. The bot is primarily aimed at d10 systems, like the old World of Darkness, but is being developed for broader applications.

## Table of Contents
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Shell Scripts](#shell-scripts)
- [License](#license)

## Features
- Manage character sheets and rulebooks.
- Roll dice and track combat stats (initiative, positioning).
- Export character sheets and rulebooks to PDF.
- Multi-character management for users.
- Support for user-friendly character building and updates during campaigns.

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/coco-is-magik/tabletop-discord-bot.git
   cd tabletop-discord-bot```
2. Build the project using the provided [Shell Scripts](#shell-scripts)
## Usage
### Running the built application
Run the bot by executing the generated JAR file:
```bash
java -jar dist/tabletop-discord-bot_Dec252024.jar
```
The name of the jar will depend on the month, day, and year when you build it.
## Project Structure
- src/main/java: Main source code.
- src/test: Test source code.
- lib: External dependencies (JAR files).
- dist: Final distributable JAR file.
- build.sh: Compiles and packages the code.
- test.sh: Compiles and runs tests.
- update-libs.sh: Manages dependencies.
## Shell Scripts
### 1. build.sh
Compiles and packages the project into a fat JAR file:
```bash
./build.sh
```
### 2. test.sh
Compiles and runs test files against the built jar file:
```bash
./test.sh
```
### 3. update-libs.sh
Updates external dependencies and downloads them into the lib folder:
```bash
./update-libs.sh
```
## License
This project is licensed under the MIT License. See the LICENSE file for details.
