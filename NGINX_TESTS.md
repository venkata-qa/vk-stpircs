# Test Cases for API Routing and URL Rewriting in Nginx

## Test Case 1: Validate Routing for `/sync/organisations/case-management/open-cases` Based on HTTP Method

**Objective**: Ensure that requests to the `/sync/organisations/case-management/open-cases` API are routed correctly based on the HTTP method (POST vs. non-POST).

### Steps:
1. In JMeter, create a Thread Group:
   - Number of Threads: 50
   - Loop Count: 1
2. Add two HTTP Request Samplers:
   - **First Sampler (POST Request)**:
     - URL: `http://nginx_url/sync/organisations/case-management/open-cases`
     - Method: `POST`
   - **Second Sampler (GET Request)**:
     - URL: `http://nginx_url/sync/organisations/case-management/open-cases`
     - Method: `GET`
3. Add a View Results Tree or Summary Report listener in JMeter.
4. Run the JMeter test and review the logs for both iPaaS and IF environments.

### Expected Behavior:
- The POST request should be routed to iPaaS (`group1.ipaas.dev.eis.ns2n.corp.hmrc.gov.uk`).
- The GET request should be routed to IF (`sync.corp.ist07.if.n.mes.corp.hmrc.gov.uk:8443`).

---

## Test Case 2: Validate URL Rewrite for `/sync/organisations/case-management/open-cases`

**Objective**: Ensure that the `/sync` portion of the URL is stripped before forwarding to the upstream server.

### Steps:
1. In JMeter, create a Thread Group:
   - Number of Threads: 50
   - Loop Count: 1
2. Add an HTTP Request Sampler:
   - URL: `http://nginx_url/sync/organisations/case-management/open-cases`
   - Method: `POST`
3. Add a View Results Tree or Summary Report listener to analyze the requests.
4. Run the JMeter test.
5. Verify in the iPaaS environment logs that the `/sync` portion of the URL has been stripped (i.e., the request received by the upstream server should be `/organisations/case-management/open-cases`).

### Expected Behavior:
- Nginx should remove `/sync` from the URL before forwarding the request to iPaaS (`group1.ipaas.dev.eis.ns2n.corp.hmrc.gov.uk`).
- The upstream server should receive the request as `/organisations/case-management/open-cases`.

---

## Test Case 3: Validate Routing for `/sync/individuals/case-management/openenquiries` Based on HTTP Method

**Objective**: Validate that requests to `/sync/individuals/case-management/openenquiries` are routed correctly based on the HTTP method.

### Steps:
1. In JMeter, create a Thread Group:
   - Number of Threads: 50
   - Loop Count: 1
2. Add two HTTP Request Samplers:
   - **First Sampler (POST Request)**:
     - URL: `http://nginx_url/sync/individuals/case-management/openenquiries`
     - Method: `POST`
   - **Second Sampler (GET Request)**:
     - URL: `http://nginx_url/sync/individuals/case-management/openenquiries`
     - Method: `GET`
3. Run the JMeter test.
4. Check the logs in both iPaaS and IF environments.

### Expected Behavior:
- POST requests should go to iPaaS (`group1.ipaas.dev.eis.ns2n.corp.hmrc.gov.uk`).
- GET requests should go to IF (`sync.corp.ist07.if.n.mes.corp.hmrc.gov.uk:8443`).

---

## Test Case 4: Validate URL Rewrite for `/sync/individuals/case-management/openenquiries`

**Objective**: Ensure that the `/sync` part of the URL is correctly stripped for requests to the `openenquiries` API.

### Steps:
1. In JMeter, create a Thread Group:
   - Number of Threads: 50
   - Loop Count: 1
2. Add an HTTP Request Sampler:
   - URL: `http://nginx_url/sync/individuals/case-management/openenquiries`
   - Method: `POST`
3. Run the JMeter test.
4. Check logs in the iPaaS environment to confirm that `/sync` has been stripped from the URL.

### Expected Behavior:
- Nginx should remove `/sync` from the URL before proxying to the upstream.
- The upstream server should receive the request as `/individuals/case-management/openenquiries`.

---

## Test Case 5: Validate Response for Unsupported Methods (e.g., PUT)

**Objective**: Ensure that unsupported methods like PUT are correctly handled and routed to IF.

### Steps:
1. In JMeter, create a Thread Group:
   - Number of Threads: 50
   - Loop Count: 1
2. Add an HTTP Request Sampler:
   - Method: `PUT`
   - URL: `http://nginx_url/sync/organisations/case-management/open-cases`
3. Run the JMeter test.
4. Check the logs in the IF environment.

### Expected Behavior:
- All PUT requests should be routed to IF (`sync.corp.ist07.if.n.mes.corp.hmrc.gov.uk:8443`).
- The iPaaS environment should not receive any PUT requests.

---

## Test Case 6: Check Default Behavior for APIs Not in Group

**Objective**: Ensure that APIs not included in any group (not listed in the configuration) return the appropriate response (e.g., 404 Not Found).

### Steps:
1. In JMeter, create a Thread Group:
   - Number of Threads: 50
   - Loop Count: 1
2. Add an HTTP Request Sampler:
   - URL: `http://nginx_url/sync/some/non-existent-api`
   - Method: `POST`
3. Run the JMeter test and check Nginx's response.

### Expected Behavior:
- Nginx should return a 404 Not Found or another appropriate response for APIs that are not defined in any group or location block.

---

## Test Case 7: Validate Behavior When Kong is Down

**Objective**: Simulate a Kong failure and validate how Nginx handles traffic when the upstream is unavailable.

### Steps:
1. Simulate a Kong failure by stopping the Kong service or blocking its access.
2. In JMeter, create a Thread Group:
   - Number of Threads: 50
   - Loop Count: 1
3. Add an HTTP Request Sampler:
   - Method: `POST`
   - URL: `http://nginx_url/sync/organisations/case-management/open-cases`
4. Run the JMeter test and observe Nginx's response.

### Expected Behavior:
- Nginx should return a 502 Bad Gateway or another error when the upstream is unavailable.







# Test Cases Using JMeter for Traffic Generation

## Test Case 1: Validate Traffic Split with Weightage 10/90

**Objective**: Verify that traffic is split 10% to iPaaS and 90% to IF for a specific API using JMeter.

### Configuration:
- Group 1 (iPaaS) weight = 10
- Group 2 (IF) weight = 90

### Steps:
1. In your Nginx configuration, set the weight to 10 for iPaaS and 90 for IF.
2. Open JMeter and create a Thread Group:
   - Number of Threads: 100 (or higher to get enough traffic samples).
   - Loop Count: 1.
3. Add an HTTP Request Sampler to the Thread Group.
   - Set the HTTP Request to POST.
   - URL: `http://nginx_url/sync/organisations/case-management/open-cases`.
4. Add a View Results Tree or Summary Report listener to monitor traffic results.
5. Run the JMeter test.
6. After the test, check the logs from both the iPaaS and IF environments to verify the distribution of traffic.

### Expected Behavior:
- Around 10% of traffic should go to iPaaS (Group 1).
- Around 90% should go to IF (Group 2).

---

## Test Case 2: Validate Traffic Split with Weightage 50/50

**Objective**: Verify that traffic is equally split (50/50) between iPaaS and IF using JMeter.

### Configuration:
- Group 1 (iPaaS) weight = 50
- Group 2 (IF) weight = 50

### Steps:
1. Modify the Nginx configuration to split traffic 50/50 between iPaaS and IF.
2. Set up a Thread Group in JMeter:
   - Number of Threads: 100.
   - Loop Count: 1.
3. Add an HTTP Request Sampler with the following details:
   - URL: `http://nginx_url/sync/organisations/case-management/open-cases`.
   - Method: POST.
4. Run the JMeter test.
5. Check the logs or metrics in both iPaaS and IF environments.

### Expected Behavior:
- Traffic should be evenly distributed between iPaaS and IF (50/50).

---

## Test Case 3: Validate Traffic with Two Active Groups

**Objective**: Validate that traffic is split between two active groups with different weights.

### Configuration:
- Group 1 (iPaaS) weight = 30
- Group 2 (IF) weight = 70

### Steps:
1. Set the weights for both groups in your Nginx configuration (30/70).
2. In JMeter, configure a Thread Group:
   - Number of Threads: 100.
   - Loop Count: 1.
3. Add an HTTP Request Sampler:
   - URL: `http://nginx_url/sync/organisations/case-management/open-cases`.
   - Method: POST.
4. Run the JMeter test.
5. Check the logs in iPaaS and IF environments to verify traffic distribution.

### Expected Behavior:
- Around 30% of the traffic should be routed to iPaaS.
- Around 70% of the traffic should be routed to IF.

---

## Test Case 4: Validate Traffic Flow for Incorrect HTTP Methods

**Objective**: Verify that non-POST requests (e.g., GET or PUT) are routed to the correct upstream server (IF).

### Steps:
1. In JMeter, create a Thread Group:
   - Number of Threads: 50.
   - Loop Count: 1.
2. Add an HTTP Request Sampler:
   - Set the HTTP method to GET.
   - URL: `http://nginx_url/sync/organisations/case-management/open-cases`.
3. Add a View Results Tree or Summary Report listener to view results.
4. Run the JMeter test.
5. After the test, check the logs in the IF environment to ensure the GET requests are routed there.

### Expected Behavior:
- All GET requests should be routed to the IF environment (`sync.corp.ist07.if.n.mes.corp.hmrc.gov.uk:8443`).
- iPaaS should not receive any GET requests.

---

## Test Case 5: Test URL Rewrite and Method-Specific Routing for open-cases API

**Objective**: Ensure the `/sync` portion of the URL is stripped and requests are routed correctly based on the HTTP method.

### Steps:
1. In JMeter, create a Thread Group:
   - Number of Threads: 50.
   - Loop Count: 1.
2. Add two HTTP Request Samplers:
   - **First Sampler (POST Request)**:
     - URL: `http://nginx_url/sync/organisations/case-management/open-cases`.
     - Method: POST.
   - **Second Sampler (GET Request)**:
     - URL: `http://nginx_url/sync/organisations/case-management/open-cases`.
     - Method: GET.
3. Run the JMeter test and monitor the results using a Summary Report listener.
4. Verify the logs in both iPaaS and IF environments:
   - The POST request should be routed to iPaaS (`group1.ipaas.dev.eis.ns2n.corp.hmrc.gov.uk`).
   - The GET request should be routed to IF (`sync.corp.ist07.if.n.mes.corp.hmrc.gov.uk:8443`).

### Expected Behavior:
- POST requests should strip `/sync` from the URL and be routed to iPaaS.
- GET requests should strip `/sync` and be routed to IF.

---

## Test Case 6: Validate Traffic Routing for `openenquiries` API

**Objective**: Validate that requests to the `/individuals/case-management/openenquiries` API are routed correctly based on the HTTP method.

### Steps:
1. In JMeter, set up a Thread Group with the following:
   - Number of Threads: 50.
   - Loop Count: 1.
2. Add two HTTP Request Samplers:
   - **First Sampler (POST Request)**:
     - URL: `http://nginx_url/sync/individuals/case-management/openenquiries`.
     - Method: POST.
   - **Second Sampler (GET Request)**:
     - URL: `http://nginx_url/sync/individuals/case-management/openenquiries`.
     - Method: GET.
3. Run the JMeter test and verify the logs in both iPaaS and IF environments.

### Expected Behavior:
- POST requests should be routed to iPaaS (`group1.ipaas.dev.eis.ns2n.corp.hmrc.gov.uk`).
- GET requests should be routed to IF (`sync.corp.ist07.if.n.mes.corp.hmrc.gov.uk:8443`).

---

## Test Case 7: Invalid HTTP Methods (e.g., PUT)

**Objective**: Test an invalid method like PUT and verify that it is routed to the IF environment (since the Nginx config is designed to route non-POST requests to IF).

### Steps:
1. In JMeter, create a Thread Group:
   - Number of Threads: 50.
   - Loop Count: 1.
2. Add an HTTP Request Sampler:
   - Method: PUT.
   - URL: `http://nginx_url/sync/organisations/case-management/open-cases`.
3. Run the JMeter test and monitor the logs or use a Summary Report listener.
4. Verify in the IF environment logs that the traffic is routed there.

### Expected Behavior:
- All PUT requests should be routed to IF (`sync.corp.ist07.if.n.mes.corp.hmrc.gov.uk:8443`).

---

## Test Case 8: Check Traffic Flow When Kong is Down

**Objective**: Simulate Kong being down and check how traffic is handled by Nginx.

### Steps:
1. Simulate a Kong failure (stop the service or block access to Kong's upstream URL).
2. Run a JMeter test by sending traffic to `http://nginx_url/sync/organisations/case-management/open-cases`:
   - Use a Thread Group with 50 threads and 1 loop.
   - Add a HTTP Request Sampler with a POST method.
3. Monitor Nginx’s behavior and responses (e.g., 502 Bad Gateway).

### Expected Behavior:
- Nginx should return a 502 Bad Gateway or similar error when Kong is down.
- No traffic should be routed to iPaaS or IF as the upstreams are unavailable.

---

## Test Case 9: Validate Location-Based Traffic

**Objective**: Ensure that the configuration correctly handles traffic based on the client’s geographic location (if applicable).

### Steps:
1. Set up Nginx to route traffic based on geo-location (if applicable) using the geo directive.
2. In JMeter, set up a Thread Group to simulate traffic from different geographic locations (using a geo-location tool or VPN).
3. Send traffic to the specified URL using POST requests:
   - URL: `http://nginx_url/sync/organisations/case-management/open-cases`
4. Monitor the upstream logs and verify that traffic is routed based on geographic location.

### Expected Behavior:
- Requests from different locations should be routed as per the geo-location-based configuration.






# Traffic Routing and Validation Scenarios for Nginx

## Scenario 1: Validate Traffic Split Between iPaaS and IF Using Different Weightages

### 1.1 Weightage 10/90

**Objective**: Validate that traffic is split with 10% going to iPaaS and 90% to IF.

### Configuration Example:
- Group 1 (iPaaS) weight = 10
- Group 2 (IF) weight = 90

### Steps:
1. Modify the weightage in your Nginx upstream configuration to match the 10/90 split.
2. Set the appropriate weights in your load balancing configuration file for iPaaS and IF. Example:
    ```json
    "swarm_weight": 10,
    "hip_weight": 90
    ```
3. Deploy the updated Nginx configuration.

### Validation:
1. Send multiple API requests (at least 100 or more) to ensure that you can observe a clear split.
2. Use tools like cURL, Postman, or load-testing tools like JMeter or Apache Benchmark (ab):
    ```bash
    ab -n 100 -c 10 http://your_nginx_url/organisations/case-management/open-cases
    ```
3. Check logs from both iPaaS and IF environments.
4. Verify if ~10% of the requests are routed to iPaaS and ~90% to IF by analyzing logs or using monitoring systems.
5. You can also enable monitoring at the load balancer or use tools like Grafana or Kibana to visualize traffic flow.

---

### 1.2 Weightage 50/50

**Objective**: Validate that traffic is equally split between iPaaS and IF.

### Configuration Example:
- Group 1 (iPaaS) weight = 50
- Group 2 (IF) weight = 50

### Steps:
1. Modify the weightage in your Nginx upstream configuration to match the 50/50 split.
2. Update the configuration file:
    ```json
    "swarm_weight": 50,
    "hip_weight": 50
    ```
3. Deploy the updated Nginx configuration.

### Validation:
1. Send the same number of API requests as before (at least 100 or more).
2. Check logs and monitoring systems.
3. Ensure that approximately 50% of the traffic is routed to iPaaS and the remaining 50% to IF.

---

### 1.3 Weightage with Two Groups

**Objective**: Validate that both groups receive traffic based on their assigned weights when both groups are active.

### Configuration Example:
- Group 1 (iPaaS) weight = 30
- Group 2 (IF) weight = 70

### Steps:
1. Modify the weights for both groups and ensure they are active.
2. Deploy the updated configuration.

### Validation:
1. Send API requests to the Nginx server.
2. Validate the logs and ensure that 30% of the requests are routed to Group 1 and 70% to Group 2.

---

## Scenario 2: Check Traffic Flow of APIs Not in Group

**Objective**: Check what happens when the API being requested is not associated with any of the defined groups.

### Steps:
1. Identify or create an API endpoint that isn't listed in the `apis` array of either group.
2. Send API requests to that endpoint.

### Validation:
1. Check the response of the requests.
2. Ensure that traffic is either routed to a default group or handled as expected by the fallback logic in your Nginx configuration.
3. If no routing group is matched, ensure the requests are being rejected or handled according to your fallback strategy (e.g., return a 404 error or redirect).

---

## Scenario 3: Check Traffic Flow When Kong is Down

**Objective**: Validate how traffic behaves when Kong (which manages your upstream services) is unavailable.

### Steps:
1. Simulate a failure in the Kong instance by stopping the Kong service or blocking access to Kong's upstream URLs.
2. Send API requests to the Nginx server.

### Validation:
1. Ensure the Nginx server responds with appropriate error status codes (e.g., 502 Bad Gateway).
2. Monitor your logs and verify that no traffic is being routed to Kong when it is down.
3. If there is a fallback or failover system in place, ensure traffic is routed accordingly.

---

## Scenario 4: Location-Based Traffic Split

**Objective**: Route traffic based on the geographic location of the user or request origin.

### Steps:
1. Configure geo-location-based routing in your Nginx configuration using the geo module:
    ```nginx
    geo $geo_country {
        default 1;
        US 2;
        UK 3;
    }
    ```
2. Deploy the updated configuration.
3. Send API requests from different geographic locations using VPNs or geo-location testing tools.

### Validation:
1. Ensure that traffic is routed based on the originating location as defined.
2. Check logs and confirm that traffic from specific locations is being routed to the correct group (iPaaS or IF).

---

## Scenario 5: Different HTTP Methods

**Objective**: Validate traffic routing based on different HTTP methods (e.g., GET, POST, PUT).

### Steps:
1. Identify the different methods (e.g., GET, POST, PUT) defined for the APIs in your configuration.
2. Send requests using different methods to the same API context.

### Validation:
1. Ensure that traffic is routed as expected based on the method type.
2. Verify that the method-specific traffic is reaching the correct group by checking logs or traffic monitoring tools.
3. If the method is not supported, ensure that Nginx responds with the appropriate status code (e.g., 405 Method Not Allowed).

---

## Scenario 6: Using Flags (Enable/Disable Swarm)

**Objective**: Validate traffic routing behavior when the `swarm` flag is toggled on or off.

### Steps:
1. Enable and disable the `swarm` flag in your configuration for different groups.
2. Deploy the updated configuration.

### Validation:
1. When the flag is enabled, traffic should be routed to the swarm environment.
2. When disabled, traffic should only flow to the primary upstream (IF).
3. Check logs and verify that traffic is routed as expected based on the flag’s state.
