# Group Test Plan for API Migration

## 1. Objective

The objective of this test plan is to ensure the seamless migration of Group 2 APIs from Docker Swarm (IF) to Kubernetes (iPaaS) through:
- **Functional Testing** to validate API behavior post-migration.
- **Performance Testing** to ensure performance metrics meet or exceed baselines.
- **Operational Acceptance Testing (OAT)** to validate infrastructure readiness.
- **Defect Tracking** to monitor and resolve issues.
- **Post-migration traffic routing** to move 10% traffic to iPaaS, gradually increasing to 100%.

---

## 2. Scope

This test plan covers the testing of APIs in Group 2 during the migration to iPaaS. The scope includes:
- **Functional Testing**: Pre- and post-migration validation.
- **Performance Testing**: Load, stress, and spike testing.
- **Operational Acceptance Testing (OAT)**: Testing traffic split, Kong failover, and other key scenarios.
- **Post-migration Monitoring**: Ensuring that APIs function correctly and perform optimally after the migration.

**Out of Scope**:
- Full OAT and security testing, which will be covered separately at the infrastructure level.

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
- **Functional Testing**: Pre- and post-migration validation using Cucumber test cases.
- **Performance Testing**: Load, stress, and spike testing during and after migration.
- **Operational Acceptance Testing (OAT)**: Limited to specific scenarios (Kong failover, NGINX traffic splitting).
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

### Tools

- **Jenkins**: For running pre-configured API tests.
- **Test Repositories**: Each API has its own repository of test cases executed through Jenkins.
- **Reports**: Reports are automatically generated by Jenkins after each test run and will be linked to the test case.

---

## 7. Operational Acceptance Testing (OAT)

| Test Name                | Objective                                                                 | Test Status    | Execution Date | Test Script Location   | Expected Outcome                                | Evidence (Links)            |
|--------------------------|---------------------------------------------------------------------------|----------------|----------------|------------------------|------------------------------------------------|-----------------------------|
| Kong Bearer Token Testing | Ensure iPaaS and IF platforms authenticate API requests using common token | In Progress    | DD/MM/YYYY     | [Bearer Token Script](#) | Successful authentication across both platforms | [Bearer Token Report](#)     |
| NGINX Traffic Split       | Validate traffic split between iPaaS and IF using different weightages     | In Progress    | DD/MM/YYYY     | [Traffic Split Script](#) | Correct traffic distribution                   | [Traffic Split Report](#)    |
| Kong Failover             | Test failover of Kong Gateway in iPaaS                                     | Not Started    | N/A            | [Kong Failover Script](#) | Traffic reroutes to IF without downtime        | [Kong Failover Report](#)    |

---

## 8. Performance Testing

### Load Model for Performance Testing

| Test Type           | Load Level                   | Duration   | Traffic Distribution   | Tool  |
|---------------------|------------------------------|------------|------------------------|-------|
| Smoke Testing       | 10% of normal traffic         | 5 minutes  | 100% to iPaaS          | JMeter |
| Load Testing        | 100% of normal traffic        | 60 minutes | 90% iPaaS, 10% IF      | JMeter |
| Stress Testing      | 120%-150% of peak traffic     | 30 minutes | 100% to iPaaS          | JMeter |
| Spike Testing       | Traffic spike to 200%         | 15 minutes | 100% to iPaaS          | JMeter |

### Performance Test Scripts

| Test Type           | Test Script                  | Execution Date | Test Status    | Baseline Metrics (Pre-Migration) | Post-Migration Metrics | Tool   | Evidence (Links to Results)      |
|---------------------|------------------------------|----------------|----------------|----------------------------------|------------------------|--------|----------------------------------|
| Smoke Testing       | smoke_test_script.jmx         | DD/MM/YYYY     | Completed       | Response Time: 250ms             | 200ms                  | JMeter | [Smoke Test Report](#)           |
| Load Testing        | load_test_script.jmx          | In Progress    | N/A            | Response Time: 300ms             | Pending                | JMeter | [Load Test Report](#)            |
| Stress Testing      | stress_test_script.jmx        | Not Started    | N/A            | Max Traffic: 120%                | N/A                    | JMeter | [Stress Test Plan](#)            |
| Spike Testing       | spike_test_script.jmx         | Not Started    | N/A            | Spike Load: 150%-200%            | N/A                    | JMeter | [Spike Test Report](#)           |

---

## 9. Defect Reporting

| Defect ID   | Description                                 | Test Type           | Priority  | Assignee        | Status          | Jira Link                           | Notes                               |
|-------------|---------------------------------------------|---------------------|-----------|-----------------|-----------------|-------------------------------------|-------------------------------------|
| DEF-12345   | API returns incorrect data when under load   | Load Testing        | High      | [Assignee Name] | In Progress     | [Jira Issue - DEF-12345](#)         | API response time exceeds baseline  |
| DEF-12346   | Kong Gateway does not recover after failover | OAT                 | Critical  | [Assignee Name] | Not Started     | [Jira Issue - DEF-12346](#)         | Kong does not properly reroute traffic |
| DEF-12347   | API authentication fails post-migration      | Functional Testing  | Medium    | [Assignee Name] | In Progress     | [Jira Issue - DEF-12347](#)         | API key not recognized by gateway   |

---

## 10. Jira Progress Tracking

| Jira Task                        | Test Phase               | Assignee          | Status          | Jira Link                           |
|----------------------------------|--------------------------|-------------------|-----------------|-------------------------------------|
| Functional Pre-Migration Testing | Functional Testing       | [Assignee Name]   | Completed       | [Jira Issue - Functional Pre-Migration](#) |
| Functional Post-Migration Testing| Functional Testing       | [Assignee Name]   | In Progress     | [Jira Issue - Functional Post-Migration](#) |
| Load Testing                     | Performance Testing      | [Assignee Name]   | In Progress     | [Jira Issue - Load Testing](#)             |
| Stress Testing                   | Performance Testing      | [Assignee Name]   | Not Started     | [Jira Issue - Stress Testing](#)           |
| OAT: Kong Failover               | OAT                      | [Assignee Name]   | In Progress     | [Jira Issue - Kong Failover](#)            |
| Traffic Split Testing            | OAT                      | [Assignee Name]   | In Progress     | [Jira Issue - Traffic Split](#)            |
| Monitoring Setup                 | Infrastructure           | [Assignee Name]   | Completed       | [Jira Issue - Monitoring Setup](#)         |

---

## 11. Risks and Mitigations

| Risk                               | Mitigation                                                                 |
|------------------------------------|-----------------------------------------------------------------------------|
| Performance degradation            | Thorough performance testing and soak testing to identify bottlenecks       |
| Functional issues post-migration   | Extensive pre- and post-migration functional testing to detect issues early |
| Traffic distribution inconsistencies | Validate traffic routing using different NGINX weightages during testing  |
