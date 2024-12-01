#!/bin/bash

# Variables
SRC_DIR="src/main/java"
BIN_DIR="bin"
LIB_DIR="lib"
DIST_DIR="dist"

# Clear the bin and dist directories
rm -rf $DIST_DIR/*.log $DIST_DIR/*.jar $DIST_DIR/*.json
mkdir -p $BIN_DIR
mkdir -p $DIST_DIR

# Get the root folder name
ROOT_FOLDER_NAME=$(basename "$PWD")

# Get the current date in the format MMMDDYYYY
CURRENT_DATE=$(date +"%b%d%Y")

# Generate the JAR file name using the root folder name and the current date
JAR_NAME="${ROOT_FOLDER_NAME}_${CURRENT_DATE}.jar"

# Initialize MAIN_CLASS
MAIN_CLASS=""

# Working directory
w_dir="$(pwd)"

# 1. Extract the non-JUnit JARs into the bin directory
for jar in $LIB_DIR/*.jar; do
    if [[ "$jar" != *"junit"* ]]; then
        r_jar="$(realpath "$jar")"
        jar_mod_time=$(stat -c %Y "$r_jar")
        #dir_mod_time=$(stat -c %Y "$w_dir/extracted_jars/extracted_$(basename "$jar" .jar)")
        dir_mod_time=$(stat -c %Y "$w_dir/extracted_jars/extracted_$(basename "$jar" .jar)" 2>/dev/null || echo 0)
        if [ ! -d "$w_dir/extracted_jars/extracted_$(basename "$jar" .jar)" ] || [ "$jar_mod_time" -gt "$dir_mod_time" ]; then
            # Create a marker directory to signify extraction has been done
            mkdir -p "$w_dir/extracted_jars/extracted_$(basename "$jar" .jar)"
            rm -rf "$w_dir/extracted_jars/extracted_$(basename "$jar" .jar)/*"
            # Extract the JAR file contents directly into the bin directory
            cd "$w_dir/extracted_jars/extracted_$(basename "$jar" .jar)"
            jar -xf "$r_jar"
            cd "$w_dir"
            rsync -a "$w_dir/extracted_jars/extracted_$(basename "$jar" .jar)/" "$BIN_DIR/"
        fi
    fi
done

# 2. Compile the Java files against the extracted classes
javac -d $BIN_DIR -sourcepath $SRC_DIR -cp "$BIN_DIR" $(find $SRC_DIR -name "*.java")
if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

# 3. Find the Main class
MAIN_CLASS=$(grep -rl 'public static void main' $SRC_DIR | sed "s|$SRC_DIR/||;s|.java||;s|/|.|g")

if [ -z "$MAIN_CLASS" ]; then
    echo "Error: Could not find a Main class."
    exit 1
fi

# 4. Create the Manifest file
MANIFEST_FILE="MANIFEST.MF"
echo "Manifest-Version: 1.0" > $MANIFEST_FILE
echo "Main-Class: $MAIN_CLASS" >> $MANIFEST_FILE

# 5. Package everything into the final fat JAR
jar cfm $DIST_DIR/$JAR_NAME $MANIFEST_FILE -C $BIN_DIR .

# Clean up the manifest file
rm $MANIFEST_FILE

# Output the absolute path of the newly created JAR file
ABSOLUTE_JAR_PATH=$(realpath "$DIST_DIR/$JAR_NAME")
echo $ABSOLUTE_JAR_PATH
