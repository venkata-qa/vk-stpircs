#!/bin/bash

SERVERS_FILE="wiremock_servers.json"
MAPPINGS_DIR="mocks/mappings/"
FILES_DIR="mocks/__files/"

# Ensure known hosts and ssh-agent are configured
mkdir -p ~/.ssh
chmod 600 ~/.ssh/known_hosts

# Check for jq
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

  # Upload mappings and files
  scp -r "$MAPPINGS_DIR" "${USER}@${HOST}:/home/${USER}/wiremock/mappings/"
  scp -r "$FILES_DIR" "${USER}@${HOST}:/home/${USER}/wiremock/__files/"

  # Restart WireMock service
  ssh "${USER}@${HOST}" "sudo systemctl restart wiremock"

  if [ $? -eq 0 ]; then
    echo "Deployment successful for $HOST"
  else
    echo "Deployment failed for $HOST"
    exit 1
  fi
done

echo "Deployment complete for all servers."
