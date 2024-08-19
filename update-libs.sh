#!/bin/bash

# Variables
LIB_DIR="lib"
CONFIG_FILE="lib.config"

# Clear the lib directory
echo "Clearing the lib directory..."
rm -rf $LIB_DIR/*

# Create the lib directory if it doesn't exist
mkdir -p $LIB_DIR

# Function to fetch the latest version of an artifact
get_latest_version() {
    group_id=$(echo $1 | cut -d: -f1 | tr . /)
    artifact_id=$(echo $1 | cut -d: -f2)
    metadata_url="https://repo1.maven.org/maven2/$group_id/$artifact_id/maven-metadata.xml"

    latest_version=$(curl -s $metadata_url | grep '<latest>' | sed -e 's/.*<latest>\(.*\)<\/latest>.*/\1/')

    echo $latest_version
}

# Read the config file and download each JAR
while IFS= read -r line; do
    # Skip comments and empty lines
    if [[ "$line" =~ ^#.*$ ]] || [[ -z "$line" ]]; then
        continue
    fi

    # Fetch the latest version of the artifact
    latest_version=$(get_latest_version "$line")
    
    # Construct the download URL
    group_id=$(echo $line | cut -d: -f1 | tr . /)
    artifact_id=$(echo $line | cut -d: -f2)
    jar_url="https://repo1.maven.org/maven2/$group_id/$artifact_id/$latest_version/$artifact_id-$latest_version.jar"

    # Extract the file name from the URL
    FILE_NAME=$(basename "$jar_url")

    # Download the file to the lib directory
    echo "Downloading $FILE_NAME..."
    curl -o "$LIB_DIR/$FILE_NAME" "$jar_url"

    # Check if the download was successful
    if [ $? -ne 0 ]; then
        echo "Error: Failed to download $jar_url"
        exit 1
    fi

done < "$CONFIG_FILE"

echo "All libraries have been updated successfully."
