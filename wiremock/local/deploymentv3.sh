#!/bin/bash

# Determine the directory where the script is located
SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)

# Determine the base directory (repository root, assumed 4 levels up from the script)
BASE_DIR=$(cd "$SCRIPT_DIR/../../../../.." && pwd)

# Define paths relative to the base directory
MOCKS_DIR="$BASE_DIR/src/test/resources/mocks"
MAPPINGS_DIR="$MOCKS_DIR/mappings/"
FILES_DIR="$MOCKS_DIR/__files/"
SERVERS_FILE="$MOCKS_DIR/scripts/wiremock_servers.json"

# Temporary directory on the server
TMP_DIR="/tmp/wiremock_tmp"

# Logs for tracking server status
SUCCESS_LOG="success.log"
FAILURE_LOG="failure.log"
> $SUCCESS_LOG
> $FAILURE_LOG

# Debugging: Print resolved paths
echo "SCRIPT_DIR: $SCRIPT_DIR"
echo "BASE_DIR: $BASE_DIR"
echo "MOCKS_DIR: $MOCKS_DIR"
echo "MAPPINGS_DIR: $MAPPINGS_DIR"
echo "FILES_DIR: $FILES_DIR"
echo "SERVERS_FILE: $SERVERS_FILE"

# Check if jq is installed
if ! command -v jq &> /dev/null; then
    echo "Error: jq is required but not installed. Please install jq."
    exit 1
fi

# Validate local directories
if [ ! -d "$MAPPINGS_DIR" ] || [ ! -d "$FILES_DIR" ]; then
    echo "Error: Directories $MAPPINGS_DIR or $FILES_DIR do not exist."
    exit 1
fi

# Validate server configuration file
if [ ! -f "$SERVERS_FILE" ]; then
    echo "Error: Servers file $SERVERS_FILE does not exist."
    exit 1
fi

# Read servers from JSON file
servers=$(jq -c '.[]' "$SERVERS_FILE")

for server in $servers; do
  HOST=$(echo "$server" | jq -r '.host')
  USER=$(echo "$server" | jq -r '.user')

  echo "Deploying to $HOST..."

  # SCP files in a single step
  echo "Uploading mappings and files to $HOST..."
  scp -r "$MAPPINGS_DIR" "${USER}@${HOST}:${TMP_DIR}/mappings" && \
  scp -r "$FILES_DIR" "${USER}@${HOST}:${TMP_DIR}/__files"

  # SSH to the server and perform all tasks in one session
  echo "Connecting to $HOST to deploy files and restart WireMock..."
  ssh "${USER}@${HOST}" <<EOF
    # Create necessary directories
    sudo mkdir -p /home/ec2-user/wiremock/mappings /home/ec2-user/wiremock/__files
    sudo mkdir -p ${TMP_DIR}

    # Move files to their respective locations
    sudo mv -f ${TMP_DIR}/mappings/* /home/ec2-user/wiremock/mappings/
    sudo mv -f ${TMP_DIR}/__files/* /home/ec2-user/wiremock/__files/

    # Clean up temporary directory
    sudo rm -rf ${TMP_DIR}

    # Restart the WireMock service
    sudo systemctl restart wiremock
EOF

  # Check if the deployment succeeded
  if [ $? -eq 0 ]; then
    echo "Deployment successful for $HOST"
    echo "$HOST" >> $SUCCESS_LOG
  else
    echo "Deployment failed for $HOST"
    echo "$HOST" >> $FAILURE_LOG
  fi
done

# Print Summary
echo "Deployment Summary:"
echo "Successful Deployments:"
cat $SUCCESS_LOG
echo "Failed Deployments:"
cat $FAILURE_LOG

echo "Deployment complete for all servers."
