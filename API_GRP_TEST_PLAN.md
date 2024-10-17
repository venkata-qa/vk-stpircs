# API Migration Performance Test Plan (with Wire Mocks)

## Objective
This test plan ensures the successful migration of **Tranche 01 APIs** from **Docker Swarm** to **Kubernetes**. Backend services are mocked using **Wire Mocks** to ensure consistent responses during testing.

### Targeted APIs:
- **API 1**: `POST /api/corp/test-case/1`
- **API 2**: `GET /api/corp/test-case/2`

### Plan Includes:
1. Execution of **Load**, **Stress**, and **Soak tests**.
2. Capturing performance metrics via **JMeter** and **Grafana**.
3. Tracking test status and generating reports for both **baseline (Docker Swarm)** and **target (Kubernetes)** environments.
4. Comparing performance between **Docker Swarm** and **Kubernetes**.

---

## Baseline Environment - Docker Swarm (with Wire Mocks)

### Wire Mock Integration
- **Wire Mocks** are used to simulate backend services and provide consistent, production-like responses.
- These mocks ensure that the performance tests accurately reflect expected behavior during migration.

### Test Metrics to Capture

- **JMeter Metrics**:
  - Throughput (RPS)
  - Mean Response Time
  - 95th Percentile Response Time
  - 99th Percentile Response Time
  - Error Rate
  - Concurrent Users
  - HTTP Status Codes

- **Grafana Metrics**:
  - CPU Utilization
  - Memory Utilization
  - Disk I/O
  - Network I/O
  - Heap Memory Usage
  - Garbage Collection (GC) activity (for JVM-based systems)

### Test Types to Execute

| **Test Type**  | **APIs**                          | **Load Model**   | **Status**   | **Metrics to Capture**                                                     | **Report Link**                              |
|----------------|-----------------------------------|------------------|--------------|------------------------------------------------------------------------------|----------------------------------------------|
| **Load Test**  | POST /api/corp/test-case/1 <br> POST /api/corp/test-case/2 | 100% on Docker Swarm | Completed   | - JMeter: Throughput, Response Time, Error Rate <br> - Grafana: CPU, Memory, Disk I/O, Network I/O | [Baseline Load Test Report](#)               |
| **Stress Test**| POST /api/corp/test-case/1 <br> POST /api/corp/test-case/2 | 100% on Docker Swarm | Completed   | - JMeter: Throughput, Response Time, Error Rate <br> - Grafana: CPU, Memory, Disk I/O, Network I/O | [Baseline Stress Test Report](#)             |
| **Soak Test**  | POST /api/corp/test-case/1 <br> POST /api/corp/test-case/2 | 100% on Docker Swarm | Completed   | - JMeter: Throughput, Response Time, Error Rate <br> - Grafana: CPU, Memory, Disk I/O, Network I/O <br> - Memory Leaks | [Baseline Soak Test Report](#)               |

### Test Status and Results for Baseline Environment (Docker Swarm)

| **Test Type**  | **Throughput (RPS)**   | **Mean Response Time (ms)**  | **95th Percentile RT (ms)**  | **99th Percentile RT (ms)**  | **Error Rate**    | **CPU Utilization**  | **Memory Utilization**  | **Disk I/O (KB/s)**  | **Network I/O (KB/s)** |
|----------------|------------------------|------------------------------|------------------------------|------------------------------|--------------------|----------------------|-------------------------|----------------------|-------------------------|
| **Load Test**  | 267.60                 | 138.85                       | 85                           | 1,054.99                     | 0.00018%            | 75%                  | 65%                     | 34.24                | 734.86                  |
| **Stress Test**| 118.38                 | 21.99                        | 32                           | 40                           | 0.0%                | 80%                  | 70%                     | 44.90                | 316.12                  |
| **Soak Test**  | 262.11                 | 94.27                        | 67                           | 1,042                        | 0.00021%            | 78%                  | 68%                     | 33.53                | 719.77                  |

---

## Target Environment - Kubernetes (with Wire Mocks)

### Wire Mock Integration
- The same **Wire Mocks** used in Docker Swarm will be applied to Kubernetes to ensure consistent backend responses during testing.

### Test Metrics to Capture

- **JMeter Metrics**:
  - Throughput (RPS)
  - Mean Response Time
  - 95th Percentile Response Time
  - 99th Percentile Response Time
  - Error Rate
  - Concurrent Users
  - HTTP Status Codes

- **Grafana Metrics**:
  - CPU Utilization
  - Memory Utilization
  - Disk I/O
  - Network I/O
  - Heap Memory Usage
  - Garbage Collection (GC) activity (for JVM-based systems)

### Test Types to Execute

| **Test Type**  | **APIs**                          | **Load Model**   | **Status**   | **Metrics to Capture**                                                     | **Report Link**                              |
|----------------|-----------------------------------|------------------|--------------|------------------------------------------------------------------------------|----------------------------------------------|
| **Load Test**  | POST /api/corp/test-case/1 <br> POST /api/corp/test-case/2 | 10% on Kubernetes, 90% on Docker Swarm | Pending     | - JMeter: Throughput, Response Time, Error Rate <br> - Grafana: CPU, Memory, Disk I/O, Network I/O | **Pending**                                  |
| **Stress Test**| POST /api/corp/test-case/1 <br> POST /api/corp/test-case/2 | 10% on Kubernetes, 90% on Docker Swarm | Pending     | - JMeter: Throughput, Response Time, Error Rate <br> - Grafana: CPU, Memory, Disk I/O, Network I/O | **Pending**                                  |
| **Soak Test**  | POST /api/corp/test-case/1 <br> POST /api/corp/test-case/2 | 10% on Kubernetes, 90% on Docker Swarm | Pending     | - JMeter: Throughput, Response Time, Error Rate <br> - Grafana: CPU, Memory, Disk I/O, Network I/O <br> - Memory Leaks | **Pending**                                  |
| **Load Test**  | POST /api/corp/test-case/1 <br> POST /api/corp/test-case/2 | 100% on Kubernetes | Pending     | - JMeter: Throughput, Response Time, Error Rate <br> - Grafana: CPU, Memory, Disk I/O, Network I/O | **Pending**                                  |
| **Stress Test**| POST /api/corp/test-case/1 <br> POST /api/corp/test-case/2 | 100% on Kubernetes | Pending     | - JMeter: Throughput, Response Time, Error Rate <br> - Grafana: CPU, Memory, Disk I/O, Network I/O | **Pending**                                  |
| **Soak Test**  | POST /api/corp/test-case/1 <br> POST /api/corp/test-case/2 | 100% on Kubernetes | Pending     | - JMeter: Throughput, Response Time, Error Rate <br> - Grafana: CPU, Memory, Disk I/O, Network I/O <br> - Memory Leaks | **Pending**                                  |

---

## Comparison Table: Docker Swarm vs. Kubernetes (with Wire Mocks)

| **Metric**               | **Baseline (Docker Swarm)**  | **Kubernetes (10% Traffic)**  | **Kubernetes (100% Traffic)**  | **Difference (10%)**  | **Difference (100%)** |
|--------------------------|------------------------------|-------------------------------|--------------------------------|-----------------------|-----------------------|
| **Throughput (RPS)**      | 267.60 RPS                   | Pending                       | Pending                        | Pending               | Pending               |
| **Mean Response Time**    | 138.85 ms                    | Pending                       | Pending                        | Pending               | Pending               |
| **95th Percentile RT**    | 85 ms                        | Pending                       | Pending                        | Pending               | Pending               |
| **99th Percentile RT**    | 1,054.99 ms                  | Pending                       | Pending                        | Pending               | Pending               |
| **Error Rate**            | 0.00018%                     | Pending                       | Pending                        | Pending               | Pending               |
| **CPU Utilization**       | 75%                          | Pending                       | Pending                        | Pending               | Pending               |
| **Memory Utilization**    | 65%                          | Pending                       | Pending                        | Pending               | Pending               |
| **Disk I/O (KB/s)**       | 34.24                        | Pending                       | Pending                        | Pending               | Pending               |
| **Network I/O (KB/s)**    | 734.86                       | Pending                       | Pending                        | Pending               | Pending               |

---

## Risks

1. **Performance Degradation on Kubernetes**:
   - APIs may perform worse on Kubernetes (e.g., higher latency, lower throughput).
   - **Mitigation**: Start with 10% traffic, compare with the baseline, and adjust resources as needed.

2. **Traffic Routing Issues**:
   - Incorrect traffic routing between Docker Swarm and Kubernetes during migration could affect results.
   - **Mitigation**: Use reliable traffic routing tools and monitor the distribution closely.

3. **Resource Exhaustion on Kubernetes**:
   - Kubernetes might run out of resources like CPU or memory.
   - **Mitigation**: Monitor resource usage and set up auto-scaling.

4. **Unexpected Errors During Migration**:
   - Errors such as timeouts or API failures might occur.
   - **Mitigation**: Run tests with 10% traffic first, and have rollback strategies in place.

5. **Inconsistent Mock Behavior**:
   - If wire mocks aren’t set up consistently between environments, test results could be skewed.
   - **Mitigation**: Ensure that the same wire mock configurations and response times are used across Docker Swarm and Kubernetes environments.

6. **Incomplete or Skewed Metrics**:
   - Monitoring tools may miss or skew performance data.
   - **Mitigation**: Validate tools with a small test and cross-check with other monitoring tools.

---

## Assumptions

1. **Kubernetes is Properly Set Up**:
   - The Kubernetes environment is properly configured and has sufficient resources (CPU, memory, etc.).

2. **APIs Are Functionally Stable**:
   - The APIs are stable, and performance is the only focus of the tests.

3. **Wire Mocks Simulate Real Backend Behavior**:
   - Wire mocks have been configured to provide realistic, production-like responses.

4. **Consistent Test Data**:
   - Both Docker Swarm and Kubernetes use the same test data and traffic patterns.

5. **Adequate Time for Testing**:
   - Sufficient time is allocated for running all tests (Load, Stress, Soak) in both environments.

6. **Monitoring Tools are Operational**:
   - JMeter, Grafana, and Prometheus are set up correctly and capturing all necessary metrics.

7. **Docker Swarm Baseline is Reliable**:
   - The baseline metrics from Docker Swarm accurately reflect the current system performance.

8. **Traffic Routing is Accurate**:
   - The mechanism to split traffic between Docker Swarm and Kubernetes is properly configured.

---

## Test Reports and Evidence

1. **[Baseline Load Test Report (Docker Swarm)](#)** – Contains detailed results of the **Load Test** in Docker Swarm.
2. **[Baseline Stress Test Report (Docker Swarm)](#)** – Contains detailed results of the **Stress Test** in Docker Swarm.
3. **[Baseline Soak Test Report (Docker Swarm)](#)** – Contains detailed results of the **Soak Test** in Docker Swarm.
4. **[Kubernetes Load Test Report (10% Traffic)](#)** – Pending results for the **Load Test** with 10% traffic on Kubernetes.
5. **[Kubernetes Stress Test Report (10% Traffic)](#)** – Pending results for the **Stress Test** with 10% traffic on Kubernetes.
6. **[Kubernetes Soak Test Report (10% Traffic)](#)** – Pending results for the **Soak Test** with 10% traffic on Kubernetes.
7. **[Kubernetes Load Test Report (100% Traffic)](#)** – Pending results for the **Load Test** with 100% traffic on Kubernetes.
8. **[Kubernetes Stress Test Report (100% Traffic)](#)** – Pending results for the **Stress Test** with 100% traffic on Kubernetes.
9. **[Kubernetes Soak Test Report (100% Traffic)](#)** – Pending results for the **Soak Test** with 100% traffic on Kubernetes.

---
