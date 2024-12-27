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

  # Create a temporary directory on the server
  ssh "${USER}@${HOST}" "sudo mkdir -p ${TMP_DIR} && sudo chown ${USER}:${USER} ${TMP_DIR}"

  # Upload files to the temporary directory
  echo "Uploading mappings to $TMP_DIR on $HOST..."
  scp -r "$MAPPINGS_DIR" "${USER}@${HOST}:${TMP_DIR}/mappings"

  echo "Uploading files to $TMP_DIR on $HOST..."
  scp -r "$FILES_DIR" "${USER}@${HOST}:${TMP_DIR}/__files"

  # Check directory existence and move files
  echo "Moving files to /home/ec2-user/wiremock/ on $HOST..."
  ssh "${USER}@${HOST}" "
    if [ ! -d /home/ec2-user/wiremock/mappings ]; then
      echo 'Creating mappings directory...'
      sudo mkdir -p /home/ec2-user/wiremock/mappings
    fi

    if [ ! -d /home/ec2-user/wiremock/__files ]; then
      echo 'Creating __files directory...'
      sudo mkdir -p /home/ec2-user/wiremock/__files
    fi

    sudo mv -f ${TMP_DIR}/mappings/* /home/ec2-user/wiremock/mappings/ &&
    sudo mv -f ${TMP_DIR}/__files/* /home/ec2-user/wiremock/__files/ &&
    sudo rm -rf ${TMP_DIR}
  "

  # Restart the WireMock service
  echo "Restarting WireMock service on $HOST..."
  ssh "${USER}@${HOST}" "sudo systemctl restart wiremock"

  if [ $? -eq 0 ]; then
    echo "Deployment successful for $HOST"
  else
    echo "Deployment failed for $HOST"
    exit 1
  fi
done

echo "Deployment complete for all servers."
