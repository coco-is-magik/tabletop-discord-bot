#!/bin/bash

# Variables
SRC_DIR="src/main/java"
BIN_DIR="bin"
LIB_DIR="lib"
DIST_DIR="dist"

# Clear the bin directory
rm -rf $BIN_DIR/*
mkdir -p $BIN_DIR

# Get the root folder name
ROOT_FOLDER_NAME=$(basename "$PWD")

# Get the current date in the format MMMDDYYYY
CURRENT_DATE=$(date +"%b%d%Y")

# Generate the JAR file name using the root folder name and the current date
JAR_NAME="${ROOT_FOLDER_NAME}_${CURRENT_DATE}.jar"

# Initialize MAIN_CLASS
MAIN_CLASS=""

# 1. Find the Main class
MAIN_CLASS=$(grep -rl 'public static void main' $SRC_DIR | sed "s|$SRC_DIR/||;s|.java||;s|/|.|g")

if [ -z "$MAIN_CLASS" ]; then
    echo "Error: Could not find a Main class."
    exit 1
fi

# 2. Find all Java files
JAVA_FILES=$(find $SRC_DIR -name "*.java")

# 3. Compile the Java files, excluding JUnit jars
NON_JUNIT_CLASSPATH=$(find $LIB_DIR -name "*.jar" | grep -v "junit" | tr '\n' ':')
javac -d $BIN_DIR -sourcepath $SRC_DIR -cp "$NON_JUNIT_CLASSPATH" $JAVA_FILES
if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

# 4. Create the Manifest file
MANIFEST_FILE="MANIFEST.MF"
echo "Manifest-Version: 1.0" > $MANIFEST_FILE
echo "Main-Class: $MAIN_CLASS" >> $MANIFEST_FILE

# Generate Class-Path for all non-JUnit jars in lib directory
echo -n "Class-Path: " >> $MANIFEST_FILE
for jar in $LIB_DIR/*.jar; do
    jar_name=$(basename $jar)
    if [[ $jar_name != *"junit"* ]]; then
        echo -n "lib/$jar_name " >> $MANIFEST_FILE
    fi
done
echo "" >> $MANIFEST_FILE  # Newline at the end

# 5. Package everything into a JAR, excluding the lib directory itself
mkdir -p $DIST_DIR
jar cfm $DIST_DIR/$JAR_NAME $MANIFEST_FILE -C $BIN_DIR .

# Clean up
rm $MANIFEST_FILE

# Output the absolute path of the newly created JAR file
ABSOLUTE_JAR_PATH=$(realpath "$DIST_DIR/$JAR_NAME")
echo $ABSOLUTE_JAR_PATH
