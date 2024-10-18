Test Case 1: Validate Routing for /sync/organisations/case-management/open-cases Based on HTTP Method
Objective: Ensure that requests to the /sync/organisations/case-management/open-cases API are routed correctly based on the HTTP method (POST vs. non-POST).

Steps:

In JMeter, create a Thread Group:
Number of Threads: 50.
Loop Count: 1.
Add two HTTP Request Samplers:
First Sampler (POST Request):
URL: http://nginx_url/sync/organisations/case-management/open-cases.
Method: POST.
Second Sampler (GET Request):
URL: http://nginx_url/sync/organisations/case-management/open-cases.
Method: GET.
Add a View Results Tree or Summary Report listener in JMeter.
Run the JMeter test and review the logs for both iPaaS and IF environments.
Expected Behavior:

The POST request should be routed to iPaaS (group1.ipaas.dev.eis.ns2n.corp.hmrc.gov.uk).
The GET request should be routed to IF (sync.corp.ist07.if.n.mes.corp.hmrc.gov.uk:8443).
Test Case 2: Validate URL Rewrite for /sync/organisations/case-management/open-cases
Objective: Ensure that the /sync portion of the URL is stripped before forwarding to the upstream server.

Steps:

In JMeter, create a Thread Group:
Number of Threads: 50.
Loop Count: 1.
Add an HTTP Request Sampler:
URL: http://nginx_url/sync/organisations/case-management/open-cases.
Method: POST.
Add a View Results Tree or Summary Report listener to analyze the requests.
Run the JMeter test.
Verify in the iPaaS environment logs that the /sync portion of the URL has been stripped (i.e., the request received by the upstream server should be /organisations/case-management/open-cases).
Expected Behavior:

Nginx should remove /sync from the URL before forwarding the request to iPaaS (group1.ipaas.dev.eis.ns2n.corp.hmrc.gov.uk).
The upstream server should receive the request as /organisations/case-management/open-cases.
Test Case 3: Validate Routing for /sync/individuals/case-management/openenquiries Based on HTTP Method
Objective: Validate that requests to /sync/individuals/case-management/openenquiries are routed correctly based on the HTTP method.

Steps:

In JMeter, create a Thread Group:
Number of Threads: 50.
Loop Count: 1.
Add two HTTP Request Samplers:
First Sampler (POST Request):
URL: http://nginx_url/sync/individuals/case-management/openenquiries.
Method: POST.
Second Sampler (GET Request):
URL: http://nginx_url/sync/individuals/case-management/openenquiries.
Method: GET.
Run the JMeter test.
Check the logs in both iPaaS and IF environments.
Expected Behavior:

POST requests should go to iPaaS (group1.ipaas.dev.eis.ns2n.corp.hmrc.gov.uk).
GET requests should go to IF (sync.corp.ist07.if.n.mes.corp.hmrc.gov.uk:8443).
Test Case 4: Validate URL Rewrite for /sync/individuals/case-management/openenquiries
Objective: Ensure that the /sync part of the URL is correctly stripped for requests to the openenquiries API.

Steps:

In JMeter, create a Thread Group:
Number of Threads: 50.
Loop Count: 1.
Add an HTTP Request Sampler:
URL: http://nginx_url/sync/individuals/case-management/openenquiries.
Method: POST.
Run the JMeter test.
Check logs in the iPaaS environment to confirm that /sync has been stripped from the URL.
Expected Behavior:

Nginx should remove /sync from the URL before proxying to the upstream.
The upstream server should receive the request as /individuals/case-management/openenquiries.
Test Case 5: Validate Response for Unsupported Methods (e.g., PUT)
Objective: Ensure that unsupported methods like PUT are correctly handled and routed to IF.

Steps:

In JMeter, create a Thread Group:
Number of Threads: 50.
Loop Count: 1.
Add an HTTP Request Sampler:
Method: PUT.
URL: http://nginx_url/sync/organisations/case-management/open-cases.
Run the JMeter test.
Check the logs in the IF environment.
Expected Behavior:

All PUT requests should be routed to IF (sync.corp.ist07.if.n.mes.corp.hmrc.gov.uk:8443).
The iPaaS environment should not receive any PUT requests.
Test Case 6: Check Default Behavior for APIs Not in Group
Objective: Ensure that APIs not included in any group (not listed in the configuration) return the appropriate response (e.g., 404 Not Found).

Steps:

In JMeter, create a Thread Group:
Number of Threads: 50.
Loop Count: 1.
Add an HTTP Request Sampler:
URL: http://nginx_url/sync/some/non-existent-api.
Method: POST.
Run the JMeter test and check Nginx's response.
Expected Behavior:

Nginx should return a 404 Not Found or another appropriate response for APIs that are not defined in any group or location block.
Test Case 7: Validate Behavior When Kong is Down
Objective: Simulate a Kong failure and validate how Nginx handles traffic when the upstream is unavailable.

Steps:

Simulate a Kong failure by stopping the Kong service or blocking its access.
In JMeter, create a Thread Group with:
Number of Threads: 50.
Loop Count: 1.
Add an HTTP Request Sampler:
Method: POST.
URL: http://nginx_url/sync/organisations/case-management/open-cases.
Run the JMeter test and observe Nginx's response.
Expected Behavior:

Nginx should return a 502 Bad Gateway or another error when the upstream is unavailable.
