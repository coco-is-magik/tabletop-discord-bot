#!/bin/bash

# Variables
LIB_DIR="lib"
CONFIG_FILE="lib.config"

# Clear the lib directory
echo "Clearing the lib directory..."
rm -rf $LIB_DIR/*

# Clear extracted_jars directory
echo "Clearing the extracted_jars directory..."
rm -rf extracted_jars/*

# Create the lib directory if it doesn't exist
mkdir -p $LIB_DIR

# Function to fetch the latest version of an artifact from Maven
get_latest_version() {
    group_id=$(echo $1 | cut -d: -f1 | tr . /)
    artifact_id=$(echo $1 | cut -d: -f2)
    metadata_url="https://repo1.maven.org/maven2/$group_id/$artifact_id/maven-metadata.xml"

    latest_version=$(curl -s $metadata_url | grep '<latest>' | sed -e 's/.*<latest>\(.*\)<\/latest>.*/\1/')

    echo $latest_version
}

# Function to download a specific asset from GitHub release
download_github_asset() {
    repo=$1
    asset_name_substring=$2

    # Fetch the latest release information from GitHub
    echo "Fetching latest from https://api.github.com/repos/$repo/releases/latest"
    release_info=$(curl -Ls "https://api.github.com/repos/$repo/releases/latest")

    # Parse the release assets and look for the desired asset using jq
    download_url=$(echo "$release_info" | jq -r --arg substring "$asset_name_substring" '.assets[] | select(.name | contains($substring)) | .browser_download_url')

    if [ -z "$download_url" ] || [ "$download_url" == "null" ]; then
        echo "Error: Could not find an asset containing '$asset_name_substring' in the latest release of $repo"
        exit 1
    fi

    # Extract the file name from the download URL
    file_name=$(basename "$download_url")

    echo "Downloading $file_name from $repo GitHub repository..."
    curl -L -o "$LIB_DIR/$file_name" "$download_url"
    if [ $? -ne 0 ]; then
        echo "Error: Failed to download $download_url"
        exit 1
    fi
}

# Read the config file and download each library
while IFS= read -r line; do
    # Skip comments and empty lines
    if [[ "$line" =~ ^#.*$ ]] || [[ -z "$line" ]]; then
        continue
    fi

    if [[ "$line" =~ ^github: ]]; then
        # Parse GitHub entry with asset substring
        repo=$(echo $line | cut -d: -f2)
        asset_name_substring=$(echo $line | cut -d: -f3)
        download_github_asset "$repo" "$asset_name_substring"
    else
        # Parse Maven entry
        latest_version=$(get_latest_version "$line")
        group_id=$(echo $line | cut -d: -f1 | tr . /)
        artifact_id=$(echo $line | cut -d: -f2)
        jar_url="https://repo1.maven.org/maven2/$group_id/$artifact_id/$latest_version/$artifact_id-$latest_version.jar"
        FILE_NAME=$(basename "$jar_url")
        echo "Downloading $FILE_NAME from Maven..."
        curl -o "$LIB_DIR/$FILE_NAME" "$jar_url"
        if [ $? -ne 0 ]; then
            echo "Error: Failed to download $jar_url"
            exit 1
        fi
    fi
done < "$CONFIG_FILE"

echo "All libraries have been updated successfully."
