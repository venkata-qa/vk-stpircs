# Performance Testing Plan for Docker Swarm to Kubernetes Migration

## Objective
The goal is to execute **Load**, **Stress**, and **Soak tests** on both **Docker Swarm** (current production) and **Kubernetes** (target environment), capturing baselines, test results, and comparisons for key performance metrics. The tests will cover **application-level metrics** using **JMeter** and **infrastructure/resource-level metrics** using **Grafana/Prometheus**.

## Test Environment

| **Environment**          | **Details**                                                                                          |
|--------------------------|------------------------------------------------------------------------------------------------------|
| **Docker Swarm (Baseline)** | Current production environment on Docker Swarm.                                                    |
| **Kubernetes (Target)**   | New environment on Kubernetes. Test will be run on IP: `xx.xx.xx.xx`.                                |

---

## Test Types and Execution

We will run **Load**, **Stress**, and **Soak tests** in both the **Docker Swarm** and **Kubernetes** environments.

### 1. **Load Testing**
- **Objective**: Validate system performance under normal load conditions. The goal is to verify **response times**, **throughput**, **concurrency**, and **resource utilization** under typical usage scenarios.
- **Environment**: Both Docker Swarm and Kubernetes
- **Metrics Tracked**:
  - **JMeter**:
    - Throughput (RPS)
    - Mean/95th/99th Percentile Response Times
    - Error Rate
    - HTTP Status Codes
    - Concurrent Users (Threads)
  - **Grafana**:
    - CPU Utilization
    - Memory Utilization
    - Disk I/O and Network I/O Utilization
    - Heap Memory Usage (if applicable)
    - Thread Utilization

### 2. **Stress Testing**
- **Objective**: Test the system's behavior under peak or extreme load. The goal is to determine the system’s breaking point, observe how performance degrades, and monitor resource exhaustion.
- **Environment**: Both Docker Swarm and Kubernetes
- **Metrics Tracked**:
  - **JMeter**:
    - Throughput (RPS)
    - Mean/95th/99th Percentile Response Times
    - Error Rate
    - HTTP Status Codes
    - Concurrent Users (Threads)
  - **Grafana**:
    - CPU/Memory Utilization (Resource Saturation)
    - Disk I/O and Network I/O Utilization
    - Heap Memory Usage & Garbage Collection (GC) Activity
    - Thread Utilization

### 3. **Soak Testing**
- **Objective**: Assess system stability and performance over an extended period under a constant load. Soak testing helps detect **memory leaks**, **resource exhaustion**, and **long-term performance degradation**.
- **Environment**: Both Docker Swarm and Kubernetes
- **Metrics Tracked**:
  - **JMeter**:
    - Throughput (RPS)
    - Mean/95th/99th Percentile Response Times
    - Error Rate
    - HTTP Status Codes
    - Concurrent Users (Threads)
  - **Grafana**:
    - CPU/Memory Utilization
    - Heap Memory Usage & Garbage Collection (GC) Activity
    - Memory Leaks
    - Disk I/O and Network I/O Utilization
    - Thread Utilization

---

## Baseline Metrics and Target Comparisons

- **Docker Swarm Baseline**: Initial tests will be run on Docker Swarm to establish the **baseline metrics**.
- **Kubernetes Target**: Subsequent tests will be run on Kubernetes. The goal is to **meet or exceed** the performance baselines from Docker Swarm.

| **Test Type**     | **Metric**                      | **Baseline (Docker Swarm)**  | **Target (Kubernetes)**     |
|-------------------|----------------------------------|------------------------------|-----------------------------|
| **Load Testing**   | Throughput (RPS)                | 267 RPS                       | ≥ 267 RPS                   |
|                   | Mean Response Time              | 138 ms                        | ≤ 138 ms                    |
|                   | 95th Percentile Response Time   | 1055 ms                       | ≤ 1055 ms                   |
|                   | Error Rate                      | 0.00018%                      | ≤ 0.00018%                  |
|                   | CPU Utilization                 | 75%                           | ≤ 75%                       |
|                   | Memory Utilization              | 65%                           | ≤ 65%                       |
| **Stress Testing** | Throughput (RPS)                | 118 RPS                       | ≥ 118 RPS                   |
|                   | Error Rate                      | 0%                            | 0%                          |
|                   | Resource Saturation (CPU/Memory)| CPU 80%, Mem 70%              | ≤ CPU 80%, Mem 70%          |
| **Soak Testing**   | Throughput (RPS)                | 262 RPS                       | ≥ 262 RPS                   |
|                   | Mean Response Time              | 94 ms                         | ≤ 94 ms                     |
|                   | Memory Leaks                    | None detected                 | None detected               |

---

## Test Execution Plan

### **1. JMeter Test Plan**
- **Test Tool**: JMeter
- **Test Metrics**:
  - Throughput (RPS)
  - Response Times (Mean, Percentile)
  - Error Rates
  - Concurrent Users
  - HTTP Status Codes
- **Execution**:
  - Set up test scripts to simulate **Load**, **Stress**, and **Soak** scenarios.
  - Run the test on **Docker Swarm** first and capture the **JMeter report**.
  - Repeat the test on **Kubernetes**.
- **Reports**:
  - Baseline Report (Docker Swarm): Capture JMeter reports for baseline metrics.
  - Kubernetes Report: Capture JMeter reports for the target Kubernetes environment.
  
  Example Report Format:
  ```text
  JMeter Summary Report:
  - Throughput: 267 RPS (Docker Swarm), 300 RPS (Kubernetes)
  - Mean Response Time: 138 ms (Docker Swarm), 120 ms (Kubernetes)
  - Error Rate: 0.00018% (Docker Swarm), 0.0001% (Kubernetes)
