Here’s the README in `.md` (Markdown) format:

```markdown
# Thread Calculation and Load Test Configuration Guide

This document provides a step-by-step guide for calculating the number of threads required for a group of APIs based on requests per hour and configuring the test in JMeter.

---

## Step 1: Understand Inputs

### Request Data for the Group
The peak requests per hour for each API are as follows:
```
[4, 1, 1, 89, 3864, 147, 290, 254, 3, 88, 10, 13, 21, 10]
```

### Assumptions
- **Average Response Time (seconds):** Assume an average of 2 seconds per request (adjust based on real data).
- **Think Time (seconds):** Assume a 1-second delay between requests.

---

## Step 2: Convert Requests/hour to Requests/second

For each API, calculate Requests per Second (RPS) using the formula:

```
RPS = Requests per hour / 3600
```

### Calculations:
| API Request/hour | Requests/second (RPS) |
|------------------|------------------------|
| 4                | 0.0011                |
| 1                | 0.0003                |
| 1                | 0.0003                |
| 89               | 0.0247                |
| 3864             | 1.0733                |
| 147              | 0.0408                |
| 290              | 0.0806                |
| 254              | 0.0706                |
| 3                | 0.0008                |
| 88               | 0.0244                |
| 10               | 0.0028                |
| 13               | 0.0036                |
| 21               | 0.0058                |
| 10               | 0.0028                |

---

## Step 3: Calculate Threads per API

Use the formula:

```
Threads = RPS × (Response Time + Think Time)
```

Assume:
- **Response Time:** 2 seconds
- **Think Time:** 1 second

```
Threads = RPS × 3
```

### Calculations:
| API Request/hour | RPS    | Threads        |
|------------------|--------|----------------|
| 4                | 0.0011 | 0.0011 × 3 = 1 |
| 1                | 0.0003 | 0.0003 × 3 = 1 |
| 1                | 0.0003 | 0.0003 × 3 = 1 |
| 89               | 0.0247 | 0.0247 × 3 = 1 |
| 3864             | 1.0733 | 1.0733 × 3 = 4 |
| 147              | 0.0408 | 0.0408 × 3 = 1 |
| 290              | 0.0806 | 0.0806 × 3 = 1 |
| 254              | 0.0706 | 0.0706 × 3 = 1 |
| 3                | 0.0008 | 0.0008 × 3 = 1 |
| 88               | 0.0244 | 0.0244 × 3 = 1 |
| 10               | 0.0028 | 0.0028 × 3 = 1 |
| 13               | 0.0036 | 0.0036 × 3 = 1 |
| 21               | 0.0058 | 0.0058 × 3 = 1 |
| 10               | 0.0028 | 0.0028 × 3 = 1 |

---

## Step 4: Aggregate Total Threads for the Group

Add up the threads for all APIs:

```
Total Threads = 1 + 1 + 1 + 1 + 4 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 = 17 threads
```

---

## Step 5: Configure in JMeter

### Thread Group Settings:
1. **Number of Threads:** Set the total threads to `17`.
2. **Ramp-Up Time:** Configure ramp-up time to distribute thread activation (e.g., `60 seconds`).

### Distribution Among APIs:
- Use **Throughput Controllers** to assign each API its proportional share of requests.
- Configure each HTTP Request sampler with the API-specific parameters.

---

## Step 6: Validate

1. **Run the Test:** Monitor the actual throughput to ensure traffic matches expected rates.
2. **Adjust if Necessary:** Modify thread counts or ramp-up time based on server response times and performance.

---

This guide ensures accurate thread calculation and effective JMeter configuration for realistic performance testing.
```
