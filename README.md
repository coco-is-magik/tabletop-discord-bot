# Description
This is a discord bot for handling character sheets, rulebooks, rolling dice, and tracking basic combat stats such as initiative and positioning. The main focus of this is d10 systems, such as old World of Darkness, but generalized application is the ultimate goal. Planned functions include exporting PDF versions of character sheets and rulebooks, as well as support for users building characters and updating them throughout the course of a campaign, and allowing for single users to control and switch between multiple characters at one time with ease.

## Project Structure
├── dist\
│ └── [compiled JARs will be placed here]\
├── lib\
│ └── [external libraries will be downloaded here]\
├── src\
│ ├── main\
│ │ ├── java\
│ │ │ ├── cocoismagik\
│ │ │ │ ├── main\
│ │ │ │ │ ├── DataOutputter.java\
│ │ │ │ │ └── Main.java\
│ │ └── resources\
│ ├── test\
│ │ ├── MainTest.java\
│ │ └── Test.java\
├── test\
│ └── [compiled test classes will be placed here]\
├── build.sh\
├── lib.config\
├── LICENSE\
├── README.md\
├── test.sh\
└── update-libs.sh

- **src/main/java**: Contains the main source code for your application.
- **src/test**: Contains the test source code for your application.
- **lib/**: Directory where external dependencies (JAR files) are downloaded.
- **dist/**: Directory where the final distributable JAR file is placed.
- **test/**: Directory where compiled test classes are placed.

## Shell Scripts
### 1. build.sh
This script compiles the Java source code and packages it into a JAR file, excluding test dependencies.

**Usage:**
1. Run the script:
```bash
./build.sh
```

**This script will:**
- Clear and re-create a bin/ directory.
- Compile the source code in src/main/java/.
- Package the compiled code into a JAR file and place it in the dist/ directory.
- Output the absolute path of the generated JAR file.

### 2. test.sh
This script compiles the test classes and runs them against the JAR file produced by build.sh.

**Usage:**
1. Run the script:
```bash
./test.sh
```
**This script will:**
- Call build.sh to compile and package the main code.
- Compile the test classes located in src/test/java/.
- Run the tests against the JAR file generated by build.sh.
- The test classes are expected to retrieve the JAR path from the jar.path system property.

### 3. update-libs.sh
This script manages the project's dependencies by downloading the latest JAR files specified in the `lib.config` file. Currently, it expects to see repo links to maven repositories in order to pull the latest jar files. This may expand to other repository types in future if needs change.

**Usage:**
1. Ensure that `lib.config` is correctly set up with the dependencies you need.
2. Run the script:
```bash
./update-libs.sh
```
**This script will:**
- Clear the lib/ directory.
- Download the latest versions of the JAR files listed in lib.config.
- Place the downloaded JAR files in the lib/ directory.

## lib.config
The lib.config file lists the dependencies for the project. Each line specifies a dependency in the form of groupId:artifactId. Comments are added using #.
