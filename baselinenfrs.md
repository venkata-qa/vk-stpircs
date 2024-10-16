## Performance Testing NFRs for Kubernetes Migration

### NFRs with JMeter Baselines as Targets for Kubernetes Migration

| **NFR Name**            | **Baseline (from JMeter Report)**                                                                                                     | **Target for Kubernetes**                                                                                     | **Test Type**                                      | **JMeter Metric to Track**                                                 |
|-------------------------|--------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|---------------------------------------------------|-----------------------------------------------------------------------------|
| **Response Time**        | - **Load Test (Total)**: Mean 138ms, 95th Percentile 1055ms<br> - **Stress Test (Total)**: Mean 22ms, 95th Percentile 40ms<br> - **Soak Test (Total)**: Mean 94ms, 95th Percentile 1042ms | - Maintain or improve:<br> - **Load Test**: Mean ≤ 130ms, 95th ≤ 1000ms.<br> - **Stress Test**: Mean ≤ 20ms, 95th ≤ 40ms.<br> - **Soak Test**: Mean ≤ 90ms, 95th ≤ 1000ms. | Load, Stress, Soak Testing                         | - **Mean and 95th Percentile Response Time** from JMeter Summary Report. |
| **Throughput**           | - **Load Test**: 267 RPS (Total)<br> - **Stress Test**: 118 RPS (Total)<br> - **Soak Test**: 262 RPS (Total)                         | - Achieve **≥267 RPS** under load.<br> - **≥118 RPS** under stress.<br> - Maintain **≥262 RPS** during soak testing. | Load, Stress, Soak Testing                         | - **Throughput (Requests/sec)** from JMeter Summary Report.                |
| **Error Rate**           | - **Load Test (Total)**: 0.000178%<br> - **Stress Test (Total)**: 0%<br> - **Soak Test (Total)**: 0.0002077%                         | - **Target**: Keep error rates ≤ 0.001% under load and soak, and **0%** under stress conditions.                | Load, Stress, Soak Testing                         | - **Error Rate (%)** from JMeter report (Response Codes, Failure Counts).  |
| **Latency**              | - **Load Test (Total)**: Min 23ms, Max 60s, 95th Percentile 1055ms<br> - **Stress Test (Total)**: Min 12ms, Max 1058ms, 95th 40ms<br> - **Soak Test (Total)**: Min 22ms, Max 60s, 95th 1042ms | - **Load Test**: Min ≤ 20ms, Max ≤ 55s, 95th ≤ 1000ms.<br> - **Stress Test**: Min ≤ 12ms, Max ≤ 1s, 95th ≤ 40ms.<br> - **Soak Test**: Min ≤ 20ms, Max ≤ 55s, 95th ≤ 1000ms. | Load, Stress, Soak Testing                         | - **Latency (Min, Max, Percentile)** from JMeter report.                   |
| **Resource Utilization** | - Monitored externally through Prometheus/Grafana or similar systems. CPU utilization is not captured in the JMeter report but is essential for testing in Kubernetes. | - **Target**: CPU ≤ 80%, Memory ≤ 70% during peak load.                                                       | Load, Stress, Soak Testing                         | - **CPU/Memory metrics** via external monitoring tools (e.g., Prometheus). |
| **Concurrency**          | - **Max Users in Stress Test**: The system sustained 118 RPS with no errors under peak conditions.                                     | - Kubernetes should sustain at least **118 RPS** with **0% errors** during stress testing.<br> - Handle at least 1500 concurrent users without degradation. | Load, Stress Testing                                | - **Throughput and Error Rate** in JMeter under different concurrency levels. |

### Explanation of the Baseline Data Integration:

1. **Response Time**:
   - Based on your JMeter stats, you have mean response times of **138ms** in the load test, **22ms** in the stress test, and **94ms** in the soak test. The **95th percentile** response times are **1055ms** (load), **40ms** (stress), and **1042ms** (soak).
   - The expectation for Kubernetes should be to either **match or improve** these response times:
     - **Load Test**: Aim for **≤130ms mean** and **≤1000ms at 95th percentile**.
     - **Stress Test**: Aim for **≤20ms mean** and **≤40ms at 95th percentile**.
     - **Soak Test**: Aim for **≤90ms mean** and **≤1000ms at 95th percentile**.

2. **Throughput**:
   - Your JMeter stats show **267 RPS** in the load test, **118 RPS** in the stress test, and **262 RPS** in the soak test.
   - In Kubernetes, ensure that the system sustains **at least these levels of throughput**, ideally showing some improvement.

3. **Error Rate**:
   - The error rates in the provided JMeter stats are exceptionally low (close to **0.00018%** for load and soak, **0% for stress**).
   - The target for Kubernetes is to **maintain or improve** these error rates, aiming for **0% errors under stress** and **≤ 0.001% under load and soak**.

4. **Latency**:
   - JMeter stats show **minimum latencies** as low as **23ms** in the load test and **12ms** in the stress test. The **maximum latency** can go up to **60s**, with **95th percentiles** reaching **1055ms** and **1042ms**.
   - In Kubernetes, aim to reduce the **max latency** (targeting **≤ 55s**) and lower the 95th percentile to improve user experience.

5. **Resource Utilization**:
   - Resource utilization like CPU and memory was not provided in the JMeter report, but this can be monitored externally via tools like **Prometheus** or **Grafana**.
   - The target should be to keep **CPU utilization ≤ 80%** and **memory utilization ≤ 70%**, especially under stress and soak testing.

6. **Concurrency**:
   - Based on your stress test, the system was able to handle **118 RPS** without errors under peak load conditions.
   - In Kubernetes, the goal is to handle at least **1500 concurrent users** without degradation and **maintain throughput (118 RPS or more)** with **0% errors** under stress.

### Testing Plan for Kubernetes:

- **Load Testing**: Validate that Kubernetes can meet or exceed the **267 RPS** throughput, with **mean response times ≤130ms** and **95th percentile response times ≤1000ms**, and an error rate of **≤ 0.001%**.
  
- **Stress Testing**: Ensure that Kubernetes can handle **118 RPS** with **0% errors**, and the response times stay below the **40ms** 95th percentile threshold.

- **Soak Testing**: Test for long-duration stability, aiming for **262 RPS** throughput, with **mean response times ≤90ms**, **95th percentile response times ≤1000ms**, and maintaining a low error rate (≤0.001%).

- **Resource Monitoring**: Use external tools (e.g., **Prometheus, Grafana**) to track CPU and memory usage during load, stress, and soak tests, ensuring Kubernetes optimizes resource usage better than Docker Swarm.
