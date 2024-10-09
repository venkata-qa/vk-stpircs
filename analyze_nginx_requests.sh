#!/bin/bash

# Define the NGINX server login details
NGINX_SERVER_IP="your_nginx_server_ip"
USER="ec2-user"  # Your SSH user
LOG_FILE="/var/log/nginx/access.log"  # Path to the NGINX log file on the server

# Define the Kong server IPs (the upstream servers)
KONG_SERVER_1_IP="192.168.1.187"
KONG_SERVER_2_IP="192.168.1.179"

# Define the API endpoints you want to count
ENDPOINT_1="/api/v1"
ENDPOINT_2="/api/v2"

# Define the path where the logs will be stored locally
LOCAL_LOG_FILE="/tmp/nginx_access.log"

# SSH into NGINX server and retrieve the logs
echo "Logging into NGINX server $NGINX_SERVER_IP and fetching logs..."
ssh "$USER@$NGINX_SERVER_IP" "cat $LOG_FILE" > $LOCAL_LOG_FILE

# Ensure the log file was fetched successfully
if [ ! -f $LOCAL_LOG_FILE ]; then
    echo "Failed to retrieve logs from $NGINX_SERVER_IP"
    exit 1
fi

# Count requests for each endpoint going through each Kong server
echo "Analyzing the logs for requests to each endpoint on Kong servers..."

# For Kong server 1 (192.168.1.187)
REQUESTS_TO_SERVER_1_ENDPOINT_1=$(grep "$KONG_SERVER_1_IP" $LOCAL_LOG_FILE | grep "$ENDPOINT_1" | wc -l)
REQUESTS_TO_SERVER_1_ENDPOINT_2=$(grep "$KONG_SERVER_1_IP" $LOCAL_LOG_FILE | grep "$ENDPOINT_2" | wc -l)

# For Kong server 2 (192.168.1.179)
REQUESTS_TO_SERVER_2_ENDPOINT_1=$(grep "$KONG_SERVER_2_IP" $LOCAL_LOG_FILE | grep "$ENDPOINT_1" | wc -l)
REQUESTS_TO_SERVER_2_ENDPOINT_2=$(grep "$KONG_SERVER_2_IP" $LOCAL_LOG_FILE | grep "$ENDPOINT_2" | wc -l)

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

# Cleanup local log file (optional)
rm $LOCAL_LOG_FILE



Save the script to a file, for example, analyze_nginx_requests.sh.
Make the script executable:
bash
Copy code
chmod +x analyze_nginx_requests.sh
Ensure you have SSH access to the NGINX server from the machine where you are running this script.
Run the script:
bash
Copy code
./analyze_nginx_requests.sh

