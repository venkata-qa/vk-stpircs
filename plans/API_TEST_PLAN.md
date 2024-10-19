# Group Test Plan for API Migration

## 1. Objective

The objective of this test plan is to ensure the seamless migration of Group 2 APIs from Docker Swarm (IF) to Kubernetes (iPaaS) through:
- **Functional Testing** to validate API behavior post-migration.
- **Performance Testing** to ensure performance metrics meet or exceed baselines.
- **Operational Acceptance Testing (OAT)** to validate infrastructure readiness, including failover, auto-scaling, and traffic routing.
- **Pre-Production (Pre-Prod) Testing** to validate the APIs in a staging environment before production.
- **Defect Tracking** to monitor and resolve issues.
- **Post-migration traffic routing** to move 10% traffic to iPaaS, gradually increasing to 100%.

---

## 2. Scope

This test plan covers the testing of APIs in Group 2 during the migration to iPaaS. The scope includes:
- **Functional Testing**: Pre- and post-migration validation using Jenkins-based API test repositories.
- **Performance Testing**: Load, stress, and spike testing to ensure NFRs are met.
- **Operational Acceptance Testing (OAT)**: Testing the resilience and performance of Kubernetes (iPaaS) infrastructure.
- **Pre-Prod Environment Testing**: Validation of APIs in the Pre-Production environment.
- **Post-migration Monitoring**: Ensuring that APIs function correctly and perform optimally after the migration.

---

## 3. Environment Setup

This section outlines the test environments and tools used during the migration, focusing on the traffic distribution strategy between Docker Swarm (IF) and Kubernetes (iPaaS).

### Traffic Migration Phases

1. **Phase 1: 10% Traffic to iPaaS, 90% Traffic to IF**:
   - **Objective**: Gradual validation of API performance on iPaaS while maintaining most traffic on Docker Swarm.
   - **Traffic Distribution**: 10% on iPaaS, 90% on IF.
   - **Tools**: NGINX traffic splitting, Prometheus, and Grafana for monitoring.

2. **Phase 2: 100% Traffic to iPaaS**:
   - **Objective**: Full transition to iPaaS after validation in Phase 1.
   - **Traffic Distribution**: 100% on iPaaS.
   - **Tools**: Full load monitoring with Prometheus and Grafana.

### Test Environment Details

| Environment Name      | Type           | Platform                    | Usage                                                        | Tools                     |
|-----------------------|----------------|-----------------------------|--------------------------------------------------------------|---------------------------|
| IST06                 | Performance    | Kubernetes (iPaaS)           | Post-migration performance testing with 100% traffic to iPaaS | JMeter, GitLab Runner     |
| Docker Swarm (IF)      | Baseline       | Docker Swarm                 | Pre-migration functional & performance baseline with 90% traffic | Cucumber, JMeter          |
| iPaaS Kubernetes (Corp Sync) | Target Post-Migration | Kubernetes (AWS EKS)  | Post-migration functional & performance testing (10%, then 100%) | Grafana, Prometheus, JMeter |

### Infrastructure Components

- **Kubernetes Cluster (iPaaS on AWS EKS)**:
  - Node Type: m5.large
  - Autoscaling Enabled: Yes
  - Ingress Controller: NGINX for traffic splitting
  - API Gateway: Kong API Gateway

**Monitoring URLs**:
- [Prometheus Metrics Dashboard](#)
- [Grafana Dashboard for Resource Usage](#)
- [Kibana for Log Monitoring](#)

---

## 4. API Details for this Group

| API Name                               | Microservice(s) Involved                        | Traffic Type | Environment | Pre-Migration Status | Post-Migration Status |
|----------------------------------------|-------------------------------------------------|--------------|-------------|----------------------|-----------------------|
| API-1847 - Create Individuals Case Details | corp-sync-validation-service, jims-sync-hod-service | Sync         | Corp Only   | Passed               | Pending               |
| API-1973 - Identify Interest And Add Insolvency Flag | corp-sync-validation-service, jims-sync-hod-service | Sync         | Corp Only   | Passed               | Pending               |

---

## 5. Scope of Testing

### In Scope:
- **Functional Testing**: Pre- and post-migration validation using Jenkins-based API test repositories.
- **Performance Testing**: Load, stress, and spike testing during and after migration.
- **Operational Acceptance Testing (OAT)**: Validation of Kubernetes infrastructure for auto-scaling, traffic routing, and failover.
- **Pre-Prod Environment Testing**: Validation of APIs in the Pre-Production environment.
- **Post-migration Monitoring**: Monitoring of API performance and stability.

### Out of Scope:
- Full security testing (handled separately).
- Full OAT testing beyond key scenarios.

---

## 6. Functional Testing

### Overview

Each API has a dedicated **Jenkins** repository where the API tests are stored and executed. The same functional tests will be run both **pre- and post-migration** to ensure the API behaves consistently on both Docker Swarm (IF) and iPaaS platforms.

- **Pre-Migration**: Functional tests are executed on Docker Swarm (IF) to establish a baseline.
- **Post-Migration**: The same tests are executed on iPaaS to confirm that the behavior is consistent after migration.

### Execution

| Test Type                | Test Status    | Pass Rate | Pre-Migration Execution Date | Post-Migration Execution Date | Test Tool | Evidence (Links to Reports & Test Cases) |
|--------------------------|----------------|-----------|------------------------------|-------------------------------|-----------|------------------------------------------|
| Pre-Migration Functional  | Completed      | 100%      | DD/MM/YYYY                   | N/A                           | Jenkins   | [Pre-Migration Test Cases](#)            |
| Post-Migration Functional | In Progress    | N/A       | N/A                           | DD/MM/YYYY                    | Jenkins   | [Post-Migration Test Cases](#)           |

---

## 7. Pre-Prod Environment Testing

### Objective

Pre-Production (Pre-Prod) testing is conducted to validate the APIs in a **staging environment** that closely resembles the production environment. This step ensures that no issues arise during the final deployment to production and confirms that the APIs behave as expected in a fully replicated environment.

### Pre-Prod Testing Plan

1. **Functional Validation**: Re-run the functional test suite on the Pre-Prod environment.
2. **Performance Validation**: Ensure APIs meet performance NFRs in Pre-Prod before production deployment.
3. **Regression Testing**: Ensure new changes or migrations do not break any existing functionality.
4. **Stability Testing**: Run soak tests to ensure stability over time in the Pre-Prod environment.

### Tools

- **Jenkins**: For running automated functional tests.
- **JMeter**: For performance validation.
- **Grafana & Prometheus**: For monitoring API performance and resource utilization in the Pre-Prod environment.

### Pre-Prod Test Execution

| Test Type                | Test Status    | Pass Rate | Execution Date            | Test Tool  | Evidence (Links to Reports) |
|--------------------------|----------------|-----------|---------------------------|------------|-----------------------------|
| Pre-Prod Functional Tests | In Progress    | N/A       | DD/MM/YYYY                | Jenkins    | [Pre-Prod Functional Results](#) |
| Pre-Prod Performance Tests| In Progress    | N/A       | DD/MM/YYYY                | JMeter     | [Pre-Prod Performance Results](#) |
| Pre-Prod Soak Tests       | Not Started    | N/A       | N/A                       | JMeter     | [Pre-Prod Soak Results](#)       |

---

## 8. Operational Acceptance Testing (OAT)

### Objective

OAT testing ensures that the iPaaS environment is resilient, scalable, and capable of handling traffic loads and operational challenges. Key tests include validating:
- **Auto-scaling**: Ensuring that Kubernetes dynamically scales pods based on traffic and resource usage.
- **Traffic Routing**: Ensuring that traffic is correctly routed using NGINX and API gateways like Kong.
- **Failover Testing**: Verifying that the system can recover from pod failures and route traffic seamlessly.

### OAT Test Scenarios

| **Test Name**                | **Objective**                                                               | **Test Status** | **Execution Date** | **Test Script Location** | **Expected Outcome**                              | **Evidence (Links)**     |
|------------------------------|-----------------------------------------------------------------------------|-----------------|--------------------|--------------------------|--------------------------------------------------|--------------------------|
| Kong Bearer Token Testing     | Ensure iPaaS and IF platforms authenticate API requests using a common token.| In Progress      | DD/MM/YYYY         | [Bearer Token Script](#) | Successful authentication across both platforms   | [Bearer Token Report](#) |
| NGINX Traffic Split Scenarios | Validate traffic split between iPaaS and IF using different weightages.      | In Progress      | DD/MM/YYYY         | [Traffic Split Script](#) | Correct traffic distribution.                    | [Traffic Split Report](#) |
| Kong Failover Scenario        | Test failover of Kong Gateway in iPaaS.                                      | Not Started      | N/A                | [Kong Failover Script](#) | Traffic reroutes to IF without downtime.          | [Kong Failover Report](#) |
| Auto-scaling Validation       | Ensure iPaaS scales pods based on traffic load.                              | Not Started      | N/A                | [Auto-scaling Script](#)  | Pods dynamically scale based on traffic usage.    | [Auto-scaling Report](#)  |

### Tools for OAT
- **Kubernetes Dashboard**: For real-time monitoring of pods and resources.
- **Prometheus & Grafana**: For tracking performance metrics during OAT tests.
- **Chaos Engineering**: Tools like Gremlin or Chaos Monkey for failover testing.

---

## 9. Performance Testing

## 9. Performance Testing

### Performance Metrics to Capture

We will capture the following metrics using **JMeter** and **Grafana** to evaluate performance during migration:

#### **JMeter Metrics**:
- **Throughput (RPS)**: Number of requests handled per second.
- **Mean Response Time**: Average response time of the system.
- **95th Percentile Response Time**: Response time for 95% of the requests.
- **99th Percentile Response Time**: Response time for 99% of the requests.
- **Error Rate**: Percentage of requests resulting in errors.
- **Concurrent Users**: Number of users simulated during the test.
- **HTTP Status Codes**: Distribution of status codes (2xx, 4xx, 5xx) returned by the system.

#### **Grafana Metrics**:
- **CPU Utilization**: CPU usage across nodes during the test.
- **Memory Utilization**: Memory usage during the test.
- **Disk I/O**: Disk input/output operations monitored during the test.
- **Network I/O**: Network input/output operations.
- **Heap Memory Usage**: For JVM-based systems, tracks heap memory usage.
- **Garbage Collection (GC) activity**: GC activity, including any pauses or impact on response time.

### Performance NFRs

The following Non-Functional Requirements (NFRs) will be validated using the metrics captured:

- **Response Time**: Average response time must be ≤ 300ms under normal load.
- **Throughput**: The system must handle 5000 requests per second at peak load.
- **Error Rate**: Error rate should not exceed 1% under peak load.
- **CPU Utilization**: CPU usage must not exceed 75% under peak load.
- **Memory Utilization**: Memory usage must remain below 80%.


### Pre-Migration Baseline Metrics

Pre-migration performance metrics are collected on **Docker Swarm (IF)** to serve as a baseline for comparison:

| **API Name**                    | **Response Time (ms)** | **Throughput (Reqs/sec)** | **Error Rate (%)** | **CPU Utilization** | **Memory Utilization** |
|---------------------------------|------------------------|---------------------------|--------------------|---------------------|------------------------|
| API-1847 - Create Case Details   | 250ms                  | 4000 reqs/sec              | 0.5%               | 65%                 | 70%                    |
| API-1973 - Add Insolvency Flag   | 270ms                  | 4500 reqs/sec              | 0.3%               | 60%                 | 75%                    |

### Post-Migration Performance Testing

Post-migration, the same performance tests will be executed on the **Kubernetes (iPaaS)** environment to ensure that the APIs meet or exceed the baseline metrics. The results will be compared directly with the pre-migration baselines to evaluate the success of the migration.

| **API Name**                    | **Response Time (ms)** | **Throughput (Reqs/sec)** | **Error Rate (%)** | **CPU Utilization** | **Memory Utilization** | **Comparison with Baseline** |
|---------------------------------|------------------------|---------------------------|--------------------|---------------------|------------------------|-----------------------------|
| API-1847 - Create Case Details   | TBD                    | TBD                       | TBD                | TBD                 | TBD                    | Meets/Exceeds/Falls Short    |
| API-1973 - Add Insolvency Flag   | TBD                    | TBD                       | TBD                | TBD                 | TBD                    | Meets/Exceeds/Falls Short    |

---

## 10. Defect Reporting

| **Defect ID** | **Description**                                | **Test Type**             | **Priority** | **Assignee**    | **Status**        | **Jira Link**                         | **Notes**                             |
|---------------|------------------------------------------------|---------------------------|--------------|-----------------|-------------------|---------------------------------------|---------------------------------------|
| DEF-12345     | API returns incorrect data when under load.     | Load Testing              | High         | [Assignee Name] | In Progress       | [Jira Issue - DEF-12345](#)           | API response time exceeds baseline.   |
| DEF-12346     | Kong Gateway does not recover after failover.   | OAT                       | Critical     | [Assignee Name] | Not Started       | [Jira Issue - DEF-12346](#)           | Kong does not properly reroute traffic. |
| DEF-12347     | API authentication fails post-migration.        | Functional Testing        | Medium       | [Assignee Name] | In Progress       | [Jira Issue - DEF-12347](#)           | API key not recognized by the gateway. |

---

## 11. Jira Progress Tracking

| **Jira Task**                       | **Test Phase**               | **Assignee**     | **Status**        | **Jira Link**                |
|-------------------------------------|------------------------------|------------------|-------------------|------------------------------|
| Functional Pre-Migration Testing    | Functional Testing            | [Assignee Name]  | Completed         | [Jira Issue - Functional Pre-Migration](#) |
| Functional Post-Migration Testing   | Functional Testing            | [Assignee Name]  | In Progress        | [Jira Issue - Functional Post-Migration](#) |
| Load Testing                        | Performance Testing           | [Assignee Name]  | In Progress        | [Jira Issue - Load Testing](#) |
| Stress Testing                      | Performance Testing           | [Assignee Name]  | Not Started        | [Jira Issue - Stress Testing](#) |
| OAT: Kong Failover                  | OAT                           | [Assignee Name]  | In Progress        | [Jira Issue - Kong Failover](#) |
| Traffic Split Testing               | OAT                           | [Assignee Name]  | In Progress        | [Jira Issue - Traffic Split](#) |
| Monitoring Setup                    | Infrastructure                | [Assignee Name]  | Completed          | [Jira Issue - Monitoring Setup](#) |

---

## 12. Risks and Mitigations

| **Risk**                          | **Mitigation**                                                                 |
|------------------------------------|--------------------------------------------------------------------------------|
| Performance degradation            | Thorough performance testing and soak testing to identify bottlenecks.          |
| Functional issues post-migration   | Extensive pre- and post-migration functional testing to detect issues early.    |
| Traffic distribution inconsistencies | Validate traffic routing using different NGINX weightages during testing.      |

