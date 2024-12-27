#!/bin/bash

SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)
MAPPINGS_DIR="$SCRIPT_DIR/mocks/mappings/"
FILES_DIR="$SCRIPT_DIR/mocks/__files/"

SERVERS_FILE="wiremock_servers.json"
MAPPINGS_DIR="mocks/mappings/"
FILES_DIR="mocks/__files/"
TMP_DIR="/tmp/wiremock_tmp"

# Check if jq is installed for parsing JSON
if ! command -v jq &> /dev/null; then
    echo "jq is required but not installed. Please install jq."
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

  # Move files from the temporary directory to the final destination with sudo
  echo "Moving files to /home/ec2-user/wiremock/ on $HOST..."
  ssh "${USER}@${HOST}" "

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

    sudo mkdir -p /home/ec2-user/wiremock/mappings /home/ec2-user/wiremock/__files &&
    sudo mv ${TMP_DIR}/mappings/* /home/ec2-user/wiremock/mappings/ &&
    sudo mv ${TMP_DIR}/__files/* /home/ec2-user/wiremock/__files/ &&
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
