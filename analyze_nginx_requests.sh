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

# Define the API endpoints you're testing
API_1="/individuals/opencases"
API_2="/individuals/deletecases"

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

# Count requests for each upstream server and API

# For Upstream server 1 (10.187.208.62)
REQUESTS_TO_SERVER_1_API_1=$(grep "$UPSTREAM_SERVER_1_IP" /tmp/nginx_access.log | grep "$API_1" | wc -l)
REQUESTS_TO_SERVER_1_API_2=$(grep "$UPSTREAM_SERVER_1_IP" /tmp/nginx_access.log | grep "$API_2" | wc -l)

# For Upstream server 2 (10.188.208.62)
REQUESTS_TO_SERVER_2_API_1=$(grep "$UPSTREAM_SERVER_2_IP" /tmp/nginx_access.log | grep "$API_1" | wc -l)
REQUESTS_TO_SERVER_2_API_2=$(grep "$UPSTREAM_SERVER_2_IP" /tmp/nginx_access.log | grep "$API_2" | wc -l)

# Calculate total requests for each API
TOTAL_REQUESTS_API_1=$(($REQUESTS_TO_SERVER_1_API_1 + $REQUESTS_TO_SERVER_2_API_1))
TOTAL_REQUESTS_API_2=$(($REQUESTS_TO_SERVER_1_API_2 + $REQUESTS_TO_SERVER_2_API_2))

# Calculate percentages for API 1
if [ $TOTAL_REQUESTS_API_1 -gt 0 ]; then
    PERCENTAGE_SERVER_1_API_1=$(echo "scale=2; $REQUESTS_TO_SERVER_1_API_1 / $TOTAL_REQUESTS_API_1 * 100" | bc)
    PERCENTAGE_SERVER_2_API_1=$(echo "scale=2; $REQUESTS_TO_SERVER_2_API_1 / $TOTAL_REQUESTS_API_1 * 100" | bc)
else
    PERCENTAGE_SERVER_1_API_1=0
    PERCENTAGE_SERVER_2_API_1=0
fi

# Calculate percentages for API 2
if [ $TOTAL_REQUESTS_API_2 -gt 0 ]; then
    PERCENTAGE_SERVER_1_API_2=$(echo "scale=2; $REQUESTS_TO_SERVER_1_API_2 / $TOTAL_REQUESTS_API_2 * 100" | bc)
    PERCENTAGE_SERVER_2_API_2=$(echo "scale=2; $REQUESTS_TO_SERVER_2_API_2 / $TOTAL_REQUESTS_API_2 * 100" | bc)
else
    PERCENTAGE_SERVER_1_API_2=0
    PERCENTAGE_SERVER_2_API_2=0
fi

# Display the results
echo "Requests for $API_1 routed to upstream server $UPSTREAM_SERVER_1_IP: $REQUESTS_TO_SERVER_1_API_1 ($PERCENTAGE_SERVER_1_API_1%)"
echo "Requests for $API_1 routed to upstream server $UPSTREAM_SERVER_2_IP: $REQUESTS_TO_SERVER_2_API_1 ($PERCENTAGE_SERVER_2_API_1%)"
echo "Total requests for $API_1: $TOTAL_REQUESTS_API_1"

echo "Requests for $API_2 routed to upstream server $UPSTREAM_SERVER_1_IP: $REQUESTS_TO_SERVER_1_API_2 ($PERCENTAGE_SERVER_1_API_2%)"
echo "Requests for $API_2 routed to upstream server $UPSTREAM_SERVER_2_IP: $REQUESTS_TO_SERVER_2_API_2 ($PERCENTAGE_SERVER_2_API_2%)"
echo "Total requests for $API_2: $TOTAL_REQUESTS_API_2"

# Update the timestamp file with the current timestamp for the next run
CURRENT_TIMESTAMP=$(date +"%d/%b/%Y:%H:%M:%S")
echo "$CURRENT_TIMESTAMP" > "$TIMESTAMP_FILE"
echo "Updated timestamp for next run: $CURRENT_TIMESTAMP"

# Cleanup local log file (optional)
rm /tmp/nginx_access.log
