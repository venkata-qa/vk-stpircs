# Performance Testing Metrics and Collection Tools

This document outlines the performance metrics tracked during **Load**, **Stress**, and **Soak Tests**, and splits them between metrics that can be collected from **JMeter** and those that can be collected from **Grafana/Prometheus**.

## Overview

- **JMeter**: Collects **application-level metrics** related to requests and responses.
- **Grafana/Prometheus**: Collects **infrastructure-level metrics** related to system resource usage (CPU, memory, network, etc.).

## Split of Metrics Between JMeter and Grafana

| **Metric**                      | **Collected from JMeter** | **Collected from Grafana (via Prometheus)** | **Description**                                                                                              |
|----------------------------------|---------------------------|--------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| **Throughput (RPS)**             | ✔️                         | ✖️                                          | Number of successful requests per second.                                                                     |
| **Mean Response Time**           | ✔️                         | ✖️                                          | Average time taken for requests to be processed.                                                              |
| **95th Percentile Response Time**| ✔️                         | ✖️                                          | Time within which 95% of the requests were completed.                                                         |
| **99th Percentile Response Time**| ✔️                         | ✖️                                          | Time within which 99% of the requests were completed.                                                         |
| **Minimum Response Time**        | ✔️                         | ✖️                                          | Minimum time taken to process requests.                                                                       |
| **Maximum Response Time**        | ✔️                         | ✖️                                          | Maximum time taken to process requests.                                                                       |
| **Error Rate**                   | ✔️                         | ✖️                                          | Percentage of requests that resulted in an error.                                                             |
| **Concurrent Users (Threads)**   | ✔️                         | ✖️                                          | Number of simultaneous users or threads simulated in JMeter.                                                  |
| **HTTP Status Codes**            | ✔️                         | ✖️                                          | Breakdown of HTTP response status codes (200, 400, 500, etc.).                                                |
| **CPU Utilization**              | ✖️                         | ✔️                                          | Percentage of CPU used by the system during the test.                                                         |
| **Memory Utilization**           | ✖️                         | ✔️                                          | Percentage of memory (RAM) used by the system during the test.                                                |
| **Disk I/O Utilization**         | ✖️                         | ✔️                                          | Rate of disk read/write operations during the test.                                                           |
| **Network I/O Utilization**      | ✖️                         | ✔️                                          | Amount of data sent and received over the network during the test.                                            |
| **Heap Memory Usage**            | ✖️                         | ✔️                                          | Memory usage, particularly in JVM-based systems, for applications using Java or similar environments.         |
| **Garbage Collection (GC) Activity**| ✖️                       | ✔️                                          | Frequency and duration of garbage collection events in Java or similar environments.                          |
| **Thread Utilization**           | ✖️                         | ✔️                                          | Number of active application threads and their usage levels during the test.                                  |
| **Database Query Time**          | ✖️                         | ✔️ (if integrated)                         | Response time and throughput for database queries during the test (if monitored).                             |
| **Memory Leaks**                 | ✖️                         | ✔️ (Long-term)**                            | Detects gradual memory consumption increases, often measured during long-duration (soak) tests.               |
| **Resource Saturation**          | ✖️                         | ✔️                                          | Tracks how close the system is to using up its available resources (CPU, memory, disk, etc.).                 |

## Detailed Breakdown

### Metrics Collected from JMeter
- **Throughput (RPS)**: Number of successful requests per second.
- **Response Times (Mean, Percentiles, Min, Max)**: Average and percentile response times to track latency.
- **Error Rate**: Percentage of requests that resulted in an error.
- **Concurrent Users (Threads)**: Number of simultaneous users or threads simulated in JMeter.
- **HTTP Status Codes**: Breakdown of HTTP response status codes (200, 400, 500, etc.).

### Metrics Collected from Grafana (via Prometheus)
- **CPU/Memory Utilization**: Percentage of CPU and memory used during the test.
- **Disk I/O and Network I/O Utilization**: Data throughput to and from disk and over the network.
- **Heap Memory Usage & GC Activity**: For Java-based systems, tracks heap memory usage and garbage collection frequency.
- **Thread Utilization**: Number of active application threads during the test.
- **Memory Leaks**: Detected during long-running soak tests to identify gradual memory consumption increases.
- **Database Query Times**: Tracks performance of database queries during the test.
- **Resource Saturation**: Tracks how close the system is to using up available resources (CPU, memory, disk).

---

## Use Cases for JMeter and Grafana Metrics

### Load Testing
- **JMeter**: Tracks **throughput**, **response times**, **error rates**, and **concurrency**.
- **Grafana**: Monitors **CPU, memory, and network utilization** to ensure resource efficiency under normal load.

### Stress Testing
- **JMeter**: Tracks **throughput**, **response times**, and **errors**, expecting degradation as the system approaches limits.
- **Grafana**: Tracks **resource saturation**, ensuring CPU, memory, and I/O resources are monitored under extreme load.

### Soak Testing
- **JMeter**: Tracks **throughput stability** and **error rates** over an extended period.
- **Grafana**: Monitors **long-term resource utilization**, such as **memory leaks**, **GC activity**, and **heap memory usage**.

---

## Example: Metrics by Test Type and Tool

| **Test Type**     | **Metric**                      | **Collected from JMeter** | **Collected from Grafana**  |
|-------------------|----------------------------------|---------------------------|-----------------------------|
| **Load Testing**   | Throughput (RPS)                | ✔️                         | ✖️                           |
|                   | Mean Response Time              | ✔️                         | ✖️                           |
|                   | 95th Percentile Response Time   | ✔️                         | ✖️                           |
|                   | Error Rate                      | ✔️                         | ✖️                           |
|                   | CPU Utilization                 | ✖️                         | ✔️                           |
|                   | Memory Utilization              | ✖️                         | ✔️                           |
|                   | Network I/O Utilization         | ✖️                         | ✔️                           |
|                   | Heap Memory Usage               | ✖️                         | ✔️                           |
| **Stress Testing** | Throughput (RPS)                | ✔️                         | ✖️                           |
|                   | Error Rate                      | ✔️                         | ✖️                           |
|                   | Resource Saturation             | ✖️                         | ✔️                           |
|                   | Disk I/O Utilization            | ✖️                         | ✔️                           |
| **Soak Testing**   | Throughput (RPS)                | ✔️                         | ✖️                           |
|                   | Mean Response Time              | ✔️                         | ✖️                           |
|                   | Memory Leaks                    | ✖️                         | ✔️                           |
|                   | Garbage Collection Activity     | ✖️                         | ✔️                           |

---

## Final Notes
- **JMeter**: Ideal for tracking **application-level performance metrics** such as **throughput**, **response times**, **error rates**, and **concurrent users**.
- **Grafana/Prometheus**: Ideal for monitoring **infrastructure-level metrics**, capturing **CPU/memory usage**, **disk I/O**, **network I/O**, and **long-term stability issues** such as **memory leaks**.

By using JMeter and Grafana/Prometheus together, you can get a comprehensive view of both **application performance** and **system resource usage** during performance testing.
