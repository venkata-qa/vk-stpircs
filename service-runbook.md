**Runbook Template for Kong (with Prometheus Monitoring)**
----------------------------------------------------------

### **1\. Overview**

-   **Service Name**: Kong API Gateway
-   **Purpose**: Provide operational steps for managing, monitoring, troubleshooting, and maintaining the Kong service running on Kubernetes, with metrics collection via Prometheus.
-   **Dependencies**:
    -   **Kong** running in a Kubernetes cluster.
    -   **Prometheus** for metrics collection.
    -   **Grafana** for metrics visualization.
    -   **ArgoCD** for deployment and management.
    -   **GitLab Runner** for CI/CD pipelines.

* * * * *

### **2\. Key Contacts**

-   **Service Owner**: [Team or individual responsible for the service].
-   **Escalation Points**: [List escalation paths, e.g., on-call engineers, SRE team].
-   **Monitoring Alerts**: [Provide Slack/PagerDuty channels, contact emails for critical alerts].

* * * * *

### **3\. ArgoCD Deployment Process**

#### **3.1 Deployment Steps**

-   **GitOps Process**: [Service Name] is deployed using **ArgoCD** based on the manifests stored in the **Git repository**.
    -   Confirm changes have been merged into the appropriate Git branch.
    -   Sync the application using ArgoCD:
        -   **CLI Command**: `argocd app sync <application-name>`
        -   Check sync status: `argocd app get <application-name>`
    -   Ensure deployment completes successfully and services are running.

#### **3.2 GitLab Runner Deployment Integration**

-   **CI/CD Pipeline Overview**:
    -   **GitLab Runner** triggers deployment pipelines which handle:
        -   Building Docker images.
        -   Deploying changes via **ArgoCD** to Kubernetes.
    -   Monitor the pipeline for successful completion.
    -   Ensure that the **Prometheus monitoring configuration** is updated along with the deployment.

* * * * *

### **4\. Service Information (Kong API Gateway)**

#### **4.1 Service Overview**

-   **Service Type**: API Gateway
-   **Container Image**: [kong:2.6-alpine]
-   **Kubernetes Resources**:
    -   **Namespace**: [Namespace where Kong is running].
    -   **Deployment**: `kubectl get deployment kong -n <namespace>`
    -   **Service**: `kubectl get svc kong -n <namespace>`
    -   **Ingress Controller (if applicable)**: Managed by Kong.

#### **4.2 Prometheus Metrics**

-   **Prometheus Scrape Configuration**: Kong exposes its metrics via `/metrics` on the admin API.
    -   **Kong Prometheus Plugin** must be enabled for scraping metrics.
    -   **Scrape Endpoint**: `http://<kong-admin-url>:8001/metrics`

#### **4.3 Kubernetes Deployment Details**

-   Kong is deployed inside Kubernetes, and the following commands are useful:
    -   **Check Pods**: `kubectl get pods -n <namespace> -l app=kong`
    -   **Check Services**: `kubectl get svc -n <namespace> -l app=kong`

* * * * *

### **5\. Monitoring and Alerts**

#### **5.1 Metrics Collection (Prometheus)**

-   **Kong Metrics**: Kong exposes several useful metrics for monitoring API traffic and performance:

    -   `kong_http_requests_total`: Total HTTP requests processed by Kong.
    -   `kong_latency_bucket`: Latency distribution between Kong, upstream services, and clients.
    -   `kong_http_status`: HTTP response codes for traffic passing through Kong.
    -   `kong_bandwidth_bytes`: Bandwidth used by Kong for requests and responses.
-   **Prometheus Configuration**:

    -   Ensure that the Prometheus scrape job is configured to collect metrics from Kong's `/metrics` endpoint.

        yaml

        Copy code

        `scrape_configs:
          - job_name: 'kong'
            metrics_path: /metrics
            static_configs:
              - targets: ['<kong-admin-url>:8001']`

#### **5.2 Grafana Dashboards**

-   **Dashboard Link**: [Link to Grafana dashboards]
-   **Kong Metrics Visualized**:
    -   **Request Rate**: Graph the number of requests per second.
    -   **Latency**: Track the latency of requests passing through Kong.
    -   **Error Rates**: Monitor HTTP 5xx errors.
    -   **Upstream Health**: Monitor the performance of upstream services Kong is proxying to.

#### **5.3 Alerts (Using Prometheus Alertmanager)**

-   **Key Alerts for Kong**:
    -   **High Latency**:

        -   **Condition**: `kong_latency_bucket{le="1"} > 10` (example threshold for latency spikes).
        -   **Alert**: High latency for API responses.
    -   **High Error Rate (5xx)**:

        -   **Condition**: `increase(kong_http_requests_total{status=~"5.."}[5m]) > 5`
        -   **Alert**: An alert for when the number of 5xx errors exceeds a threshold within 5 minutes.
    -   **Increased CPU/Memory Usage**:

        -   **Condition**: CPU/memory usage exceeding 80% for more than 5 minutes.
        -   **Alert**: Alerts for resource exhaustion in Kong pods.
    -   **Prometheus Alert Configuration Example**:

        yaml

        Copy code

        `groups:
          - name: kong_alerts
            rules:
              - alert: HighLatencyKong
                expr: rate(kong_latency_bucket{le="1"}[5m]) > 10
                for: 5m
                labels:
                  severity: warning
                annotations:
                  summary: "High latency in Kong"
                  description: "Latency is greater than 1 second for more than 10 requests per second."`

    -   **Alert Routing**: Ensure alerts are sent to the appropriate channels (Slack, PagerDuty, email).

* * * * *

### **6\. Logs and Debugging**

#### **6.1 Accessing Logs**

-   **Kubernetes Logs**:
    -   View logs for Kong to troubleshoot issues:

        bash

        Copy code

        `kubectl logs <kong-pod-name> -n <namespace>`

#### **6.2 Centralized Logging (Loki/ELK Stack)**

-   Logs are forwarded to **Loki** or **Elasticsearch** for centralized management.

    -   **Search Logs in Grafana/Kibana**:
        -   Query logs for error messages, performance bottlenecks, or request traces:
            -   `{"service": "kong", "level": "error"}`

    **Common Log Errors**:

    -   **502 Bad Gateway**: Caused by issues with upstream services.
    -   **Connection Timeouts**: Indicate network or upstream service issues.

* * * * *

### **7\. Incident Response and Troubleshooting**

#### **7.1 Common Issues and Resolutions**

-   **High Latency in Requests**:

    -   **Cause**: Issues with upstream services, resource limits in Kong, or configuration issues.
    -   **Resolution**:
        -   Check Prometheus metrics for upstream service response times.
        -   Ensure Kong is not resource-constrained (monitor CPU/memory usage).
        -   Restart affected Kong pods if necessary.
-   **Increased 5xx Errors**:

    -   **Cause**: Upstream services are down or misconfigured.
    -   **Resolution**:
        -   Verify upstream service health.
        -   Check if the upstream is correctly configured in Kong.
        -   Restart Kong pods if necessary: `kubectl rollout restart deployment kong -n <namespace>`.

#### **7.2 Rollback Procedure**

-   **ArgoCD Rollback**:
    -   Rollback to a previous Git commit using ArgoCD:

        bash

        Copy code

        `argocd app rollback <application-name> <revision>`

-   **GitLab Rollback**:
    -   Revert the code change in GitLab and push the rollback commit.
    -   GitLab Runner will trigger a redeployment.

* * * * *

### **8\. Scaling and Maintenance**

#### **8.1 Scaling Kong Pods**

-   **Horizontal Pod Autoscaler (HPA)**:
    -   Kong can scale based on CPU or memory thresholds.

        bash

        Copy code

        `kubectl get hpa -n <namespace>`

    -   Manually scale if needed:

        bash

        Copy code

        `kubectl scale --replicas=<num> deployment/kong -n <namespace>`

#### **8.2 Scheduled Maintenance**

-   **Planned Maintenance Windows**:
    -   Drain traffic from Kong pods before maintenance:

        bash

        Copy code

        `kubectl cordon <node-name>
        kubectl drain <node-name> --ignore-daemonsets --delete-local-data`

    -   Scale down pods or remove from the load balancer temporarily.

* * * * *

### **9\. Backup and Recovery**

#### **9.1 Backup Procedure**

-   **Database Backups** (PostgreSQL for Kong DB mode):
    -   Ensure backups are scheduled regularly using tools like `pg_dump` or cloud database backup services.

#### **9.2 Recovery Procedure**

-   **Database Restore**:
    -   Restore PostgreSQL using `pg_restore`.

* * * * *

### **10\. Access Management**

#### **10.1 Role-Based Access Control (RBAC)**

-   **ArgoCD RBAC**: Control access for who can trigger deployments or rollbacks.
-   **Kubernetes RBAC**: Ensure that only authorized users can modify Kong resources (pods, services, etc.).

* * * * *

### **11\. References**

-   **Prometheus Documentation**: [Link to Prometheus Docs]
-   **Kong Documentation**: [Link to Kong Docs]
-   **ArgoCD Documentation**: [Link to ArgoCD Docs]
-   **GitLab CI/CD Documentation**: [Link to GitLab CI/CD Docs]

* * * * *

### **12\. Version History**

-   Version 1.0: Initial runbook created.
-   Version 1.1: Added Prometheus integration and troubleshooting section.
