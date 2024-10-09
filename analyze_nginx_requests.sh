#!/bin/bash

# Define the NGINX server login details
NGINX_SERVER_IP="your_nginx_server_ip"
USER="ec2-user"  # Your SSH user
LOG_FILE="/var/log/nginx/access.log"  # Path to the NGINX log file on the server

# Define the path to your SSH private key
SSH_KEY_PATH="~/.ssh/your_private_key.pem"  # Replace with the path to your private key

# Define the Kong server IPs (the upstream servers)
KONG_SERVER_1_IP="192.168.1.187"
KONG_SERVER_2_IP="192.168.1.179"

# Define the API endpoints you want to count
ENDPOINT_1="/api/v1"
ENDPOINT_2="/api/v2"

# Define a file to store the timestamp of the last run
TIMESTAMP_FILE="/tmp/nginx_last_run_timestamp.txt"

# If the timestamp file doesn't exist, set a default timestamp (e.g., 1 hour ago or some starting point)
if [ ! -f "$TIMESTAMP_FILE" ]; then
    echo "Timestamp file not found. Setting default timestamp as 1 hour ago."
    LAST_RUN_TIMESTAMP=$(date -d "1 hour ago" +"%d/%b/%Y:%H:%M:%S")
    echo "$LAST_RUN_TIMESTAMP" > "$TIMESTAMP_FILE"
else
    # Read the last run timestamp from the file
    LAST_RUN_TIMESTAMP=$(cat "$TIMESTAMP_FILE")
    echo "Last run timestamp: $LAST_RUN_TIMESTAMP"
fi

# SSH into NGINX server and retrieve logs after the last run timestamp
echo "Logging into NGINX server $NGINX_SERVER_IP and fetching logs after $LAST_RUN_TIMESTAMP..."
ssh -i "$SSH_KEY_PATH" "$USER@$NGINX_SERVER_IP" "sed -n '/$LAST_RUN_TIMESTAMP/,\$p' $LOG_FILE" > /tmp/nginx_access.log

# Ensure the log file was fetched successfully
if [ ! -f /tmp/nginx_access.log ]; then
    echo "Failed to retrieve logs from $NGINX_SERVER_IP"
    exit 1
fi

# Count requests for each endpoint going through each Kong server
echo "Analyzing the logs for requests to each endpoint on Kong servers..."

# For Kong server 1 (192.168.1.187)
REQUESTS_TO_SERVER_1_ENDPOINT_1=$(grep "$KONG_SERVER_1_IP" /tmp/nginx_access.log | grep "$ENDPOINT_1" | wc -l)
REQUESTS_TO_SERVER_1_ENDPOINT_2=$(grep "$KONG_SERVER_1_IP" /tmp/nginx_access.log | grep "$ENDPOINT_2" | wc -l)

# For Kong server 2 (192.168.1.179)
REQUESTS_TO_SERVER_2_ENDPOINT_1=$(grep "$KONG_SERVER_2_IP" /tmp/nginx_access.log | grep "$ENDPOINT_1" | wc -l)
REQUESTS_TO_SERVER_2_ENDPOINT_2=$(grep "$KONG_SERVER_2_IP" /tmp/nginx_access.log | grep "$ENDPOINT_2" | wc -l)

# Display the results
echo "Requests sent to $ENDPOINT_1 on Kong server $KONG_SERVER_1_IP: $REQUESTS_TO_SERVER_1_ENDPOINT_1"
echo "Requests sent to $ENDPOINT_1 on Kong server $KONG_SERVER_2_IP: $REQUESTS_TO_SERVER_2_ENDPOINT_1"
echo "Requests sent to $ENDPOINT_2 on Kong server $KONG_SERVER_1_IP: $REQUESTS_TO_SERVER_1_ENDPOINT_2"
echo "Requests sent to $ENDPOINT_2 on Kong server $KONG_SERVER_2_IP: $REQUESTS_TO_SERVER_2_ENDPOINT_2"

# Optionally, calculate total requests for each endpoint across both servers
TOTAL_REQUESTS_ENDPOINT_1=$(($REQUESTS_TO_SERVER_1_ENDPOINT_1 + $REQUESTS_TO_SERVER_2_ENDPOINT_1))
TOTAL_REQUESTS_ENDPOINT_2=$(($REQUESTS_TO_SERVER_1_ENDPOINT_2 + $REQUESTS_TO_SERVER_2_ENDPOINT_2))

echo "Total requests processed for $ENDPOINT_1: $TOTAL_REQUESTS_ENDPOINT_1"
echo "Total requests processed for $ENDPOINT_2: $TOTAL_REQUESTS_ENDPOINT_2"

# Update the timestamp file with the current timestamp for the next run
CURRENT_TIMESTAMP=$(date +"%d/%b/%Y:%H:%M:%S")
echo "$CURRENT_TIMESTAMP" > "$TIMESTAMP_FILE"
echo "Updated timestamp for next run: $CURRENT_TIMESTAMP"

# Cleanup local log file (optional)
rm /tmp/nginx_access.log
