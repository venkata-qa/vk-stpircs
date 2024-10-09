#!/bin/bash

# Define the NGINX server login details
NGINX_SERVER_IP="your_nginx_server_ip"
USER="ec2-user"  # Your SSH user
LOG_FILE="/var/log/nginx/access.log"  # Path to the NGINX log file on the server

# Define the path to your SSH private key
SSH_KEY_PATH="~/.ssh/your_private_key.pem"  # Replace with the path to your private key

# Define the upstream IPs you want to track
UPSTREAM_SERVER_1_IP="10.187.208.62"
UPSTREAM_SERVER_2_IP="10.188.208.62"

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

# Convert the timestamp to match the log format (if necessary)
FORMATTED_TIMESTAMP=$(echo "$LAST_RUN_TIMESTAMP" | sed 's/:/\\:/g')

# SSH into NGINX server and retrieve logs after the last run timestamp using `grep`
echo "Logging into NGINX server $NGINX_SERVER_IP and fetching logs after $LAST_RUN_TIMESTAMP..."
ssh -i "$SSH_KEY_PATH" "$USER@$NGINX_SERVER_IP" "grep '$FORMATTED_TIMESTAMP' -A 10000 $LOG_FILE" > /tmp/nginx_access.log

# Ensure the log file was fetched successfully
if [ ! -f /tmp/nginx_access.log ]; then
    echo "Failed to retrieve logs from $NGINX_SERVER_IP"
    exit 1
fi

# Count requests for each upstream server IP in the logs
echo "Analyzing the logs for requests routed through each upstream server..."

# For Upstream server 1 (10.187.208.62)
REQUESTS_TO_SERVER_1=$(grep "$UPSTREAM_SERVER_1_IP" /tmp/nginx_access.log | wc -l)

# For Upstream server 2 (10.188.208.62)
REQUESTS_TO_SERVER_2=$(grep "$UPSTREAM_SERVER_2_IP" /tmp/nginx_access.log | wc -l)

# Display the results
echo "Requests routed to upstream server $UPSTREAM_SERVER_1_IP: $REQUESTS_TO_SERVER_1"
echo "Requests routed to upstream server $UPSTREAM_SERVER_2_IP: $REQUESTS_TO_SERVER_2"

# Update the timestamp file with the current timestamp for the next run
CURRENT_TIMESTAMP=$(date +"%d/%b/%Y:%H:%M:%S")
echo "$CURRENT_TIMESTAMP" > "$TIMESTAMP_FILE"
echo "Updated timestamp for next run: $CURRENT_TIMESTAMP"

# Cleanup local log file (optional)
rm /tmp/nginx_access.log
