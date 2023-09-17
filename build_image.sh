#!/bin/bash

# Specify the desired tag for the Docker image
BASE_TAG="koeltv/packaging-demo"

# Iterate over all .dockerfile files in the current directory
for DOCKERFILE_NAME in *.dockerfile; do
  # Extract the base name (without the .dockerfile extension)
  NAME="${DOCKERFILE_NAME%.dockerfile}"

  # Define the tag for the Docker image
  IMAGE_TAG="$BASE_TAG:$NAME"

  echo "Building docker image with tag: $IMAGE_TAG"

  # Build the Docker image using the Dockerfile and tag
  docker build -t "$IMAGE_TAG" -f "$DOCKERFILE_NAME" .

  # Check if the build was successful
  if [ $? -eq 0 ]; then
    echo "Docker image successfully built with tag: $IMAGE_TAG"
  else
    echo "Docker image build for $DOCKERFILE_NAME failed."
  fi
done