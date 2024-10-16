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













## Testing Plan for Kubernetes

| **Test Type**   | **Test Objective**                                                                                       | **JMeter Baseline Reference**                                                                                          | **Kubernetes Target**                                                                                 | **Test Status**        | **Additional Data to Collect**                                              |
|-----------------|----------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|------------------------|------------------------------------------------------------------------------|
| **Load Testing** | Validate that Kubernetes can handle expected load with acceptable response times and throughput.          | - **Throughput (Total)**: 267 RPS<br> - **Mean Response Time**: 138ms<br> - **95th Percentile Response Time**: 1055ms    | - **Throughput**: ≥ 267 RPS<br> - **Mean Response Time**: ≤ 130ms<br> - **95th Percentile**: ≤ 1000ms  | Pending/Passed/Failed  | - Resource Utilization (CPU, Memory)<br> - JMeter Throughput and Latency Data |
| **Stress Testing** | Ensure that Kubernetes can handle maximum load and determine the system’s breaking point.                | - **Throughput (Total)**: 118 RPS<br> - **Mean Response Time**: 22ms<br> - **95th Percentile Response Time**: 40ms       | - **Throughput**: ≥ 118 RPS<br> - **Mean Response Time**: ≤ 20ms<br> - **95th Percentile**: ≤ 40ms    | Pending/Passed/Failed  | - Error Rate<br> - CPU and Memory at Peak Load                               |
| **Soak Testing** | Test the stability of Kubernetes under sustained load over an extended period.                           | - **Throughput (Total)**: 262 RPS<br> - **Mean Response Time**: 94ms<br> - **95th Percentile Response Time**: 1042ms     | - **Throughput**: ≥ 262 RPS<br> - **Mean Response Time**: ≤ 90ms<br> - **95th Percentile**: ≤ 1000ms   | Pending/Passed/Failed  | - JMeter Results over Extended Duration<br> - Memory Usage Trends             |
| **Latency Monitoring** | Measure internal and external latency at various loads to ensure minimal delays in response times.   | - **Load Test**: 23ms (min), 60s (max), 95th Percentile: 1055ms<br> - **Stress Test**: 12ms (min), 95th Percentile: 40ms | - **Load Test Min**: ≤ 20ms, **Max**: ≤ 55s<br> - **Stress Test Min**: ≤ 12ms, **Max**: ≤ 1s           | Pending/Passed/Failed  | - Latency at Different Load Levels<br> - Network Latency (Internal/External)  |
| **Error Rate Testing** | Confirm that error rates remain within acceptable thresholds under all conditions.                  | - **Load Test Error Rate**: 0.00018%<br> - **Stress Test Error Rate**: 0%<br> - **Soak Test Error Rate**: 0.0002077%     | - **Error Rate ≤ 0.001%** under Load and Soak<br> - **Error Rate = 0%** under Stress                     | Pending/Passed/Failed  | - JMeter Failure Reports<br> - Error Response Codes Analysis                 |
| **Concurrency Testing** | Test the system's ability to handle a high number of concurrent users and transactions.             | - **Max Concurrency in Stress Test**: 118 RPS (without errors)                                                         | - Handle at least **1500 concurrent users** with **no degradation** and sustain **118+ RPS**          | Pending/Passed/Failed  | - Concurrent Users vs Throughput<br> - Response Times under High Concurrency |
| **Resource Utilization Monitoring** | Ensure that resource consumption (CPU, Memory) stays within acceptable limits under peak load. | - Resource data was not provided in JMeter reports, collect data using external monitoring tools (Prometheus, Grafana). | - **CPU Usage ≤ 80%**<br> - **Memory Usage ≤ 70%** during peak load                                     | Pending/Passed/Failed  | - CPU and Memory Usage<br> - Disk I/O and Network Bandwidth Utilization      |










Explanation of the Columns:
Test Type: The type of test being conducted (e.g., load, stress, soak, etc.).
Test Objective: The specific goal or purpose of the test (what you're trying to measure or validate).
JMeter Baseline Reference: Reference to the JMeter baselines from the Docker Swarm environment for comparison.
Kubernetes Target: The expected performance target for Kubernetes, aiming to match or improve the Docker Swarm baselines.
Test Status: Current status of the test (Pending, Passed, Failed). This will be updated as tests are performed.
Additional Data to Collect: Any extra metrics that need to be captured during the tests, such as CPU usage, error rates, and latency.
Data Collection:
For each test type, you should be collecting:

JMeter metrics (throughput, response times, error rates).
External monitoring data for resource utilization (CPU, memory, disk I/O) using tools like Prometheus or Grafana.
Detailed logs for error analysis (if any errors occur).
This table provides a structured plan for testing Kubernetes performance based on the JMeter baselines from Docker Swarm, along with metrics you should collect during the process.







