#!/bin/bash

# Call build.sh and capture the output (absolute path to the JAR)
JAR_PATH=$(./build.sh)

# Check if the build was successful
if [ $? -ne 0 ]; then
    echo "Build failed. Cannot run tests."
    exit 1
fi

# Ensure JAR_PATH is not empty
if [ -z "$JAR_PATH" ]; then
    echo "Error: No JAR path returned from build.sh."
    exit 1
fi

# Directory to store compiled test classes
TEST_DIR="test"

# Create the test directory if it doesn't exist
mkdir -p $TEST_DIR

# Clear the test directory
rm -rf $TEST_DIR/*

# Compile all test classes
javac -cp "$JAR_PATH:lib/*" -d $TEST_DIR $(find src/test -name "*.java")

# Check if test compilation was successful
if [ $? -ne 0 ]; then
    echo "Test compilation failed."
    exit 1
fi

# Run the tests by executing the Test class with the JAR path
java -cp "$TEST_DIR:lib/*" test.Test "$JAR_PATH"
