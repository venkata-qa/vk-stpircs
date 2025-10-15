# 🚀 Tranche 1 – Combined Performance Test Report (Structured by Test Type)

---

## 🧩 1. Smoke Test Summary

| **Round** | **Date** | **Purpose / Change Since Last Round** | **Load Profile** | **Status** | **Evidence (Reports / Dashboards)** | **Key Observations** |
|------------|-----------|--------------------------------------|------------------|-------------|------------------------------------|-----------------------|
| **Round 1** | 08 Oct 2025 | Initial environment validation | 3 files per flow (4 journeys) | ✅ Passed | Smoke Gatling Report – R1<br>Grafana R1 | Env stable, all endpoints reachable |
| **Round 2** | 10 Oct 2025 | Re-validation after cert rotation | 3 files per flow | ✅ Passed | Smoke Gatling Report – R2 | No regression |
| **Round 3** | — | — | — | — | — | — |

**Summary:**  
Smoke testing confirms environment readiness across all flows (connectivity, credentials, certs, observability).

---

## ⚙️ 2. Load Test Summary

| **Round** | **Date** | **Purpose / Change Since Last Round** | **Load Profile** | **Throughput (files/hr)** | **Success Rate (%)** | **CPU (%)** | **Status** | **Evidence** | **Notes / Observations** |
|------------|-----------|--------------------------------------|------------------|----------------------------|----------------------|--------------|-------------|---------------|---------------------------|
| **Round 1** | 09 Oct 2025 | Baseline load test | 4 000 files/hour (92/3/5 split) | 3 950 | 99.2 | 70 | ✅ Pass | Gatling R1<br>Grafana R1 | Stable, meets NFRs |
| **Round 2** | 11 Oct 2025 | Post tuning (thread pool, GC) | 4 000 files/hour | 4 020 | 99.5 | 65 | ✅ Pass | Gatling R2 | +2% throughput |
| **Round 3** | — | — | — | — | — | — | — | — | — |

**Summary:**  
Load tests validate system stability under expected throughput. Minor tuning improved CPU utilisation and latency.

---

## 🔺 3. Stress / Peak Test Summary

| **Round** | **Date** | **Purpose / Change Since Last Round** | **Load Profile** | **Target** | **Achieved** | **Status** | **Evidence** | **Observations / Bottlenecks** |
|------------|-----------|--------------------------------------|------------------|-------------|---------------|-------------|---------------|------------------------------|
| **Round 1** | 12 Oct 2025 | Initial stress (burst load) | 4 000 files in 30 min (2.22 req/sec) | 100 % | 96 % | 🟡 Partial | Gatling R1 | Slight queue delay during decrypt step |
| **Round 2** | 13 Oct 2025 | Tuning validation | 4 000 files in 30 min | 100 % | 99 % | ✅ Pass | Gatling R2 | Stable post fix |
| **Round 3** | — | — | — | — | — | — | — | — |

**Summary:**  
Stress/peak testing validates system elasticity and throughput headroom. After tuning, the system sustains near-peak performance.

---

## 🌙 4. Soak Test Summary

| **Round** | **Date** | **Purpose / Change Since Last Round** | **Load Profile** | **Duration** | **Error Rate (%)** | **CPU Trend** | **Memory Drift (%)** | **Status** | **Evidence** | **Observations** |
|------------|-----------|--------------------------------------|------------------|---------------|--------------------|----------------|----------------------|-------------|---------------|------------------|
| **Round 1** | 14 Oct 2025 | 12h stability validation | 4 000 files/hour (92/3/5) | 12 hours | 0.8 | 65 → 68 | +10 | ✅ Pass | Gatling R1<br>Grafana R1 | Stable; no leaks |
| **Round 2** | — | — | — | — | — | — | — | — | — | — |
| **Round 3** | — | — | — | — | — | — | — | — | — | — |

**Summary:**  
Soak test demonstrates consistent performance with minimal drift or resource leakage across 12 hours.

---

## 📈 5. Consolidated Trend (All Rounds)

| **Metric** | **Target** | **R1** | **R2** | **R3** | **Trend** |
|-------------|-------------|--------|--------|--------|-----------|
| **Throughput (files/hr)** | ≥ 4 000 | 3 950 | 4 020 | — | 📈 Improving |
| **Success Rate** | ≥ 99 % | 99.2 | 99.5 | — | ✅ Stable |
| **CPU (%)** | ≤ 80 % | 70 | 65 | — | 📉 Better |
| **p95 Latency (s)** | ≤ 2 / 15 / 20 | 1.8 / 14 / 19 | 1.7 / 13 / 18 | — | ✅ Improved |
| **Memory Drift (Soak)** | ≤ 20 % | 10 % | — | — | ✅ OK |

---

## ⚠️ 6. Risks & Actions

| **Risk** | **Impact** | **Round** | **Owner** | **Mitigation** |
|-----------|-------------|------------|------------|----------------|
| Decrypt service CPU saturation | Minor during R1 | R1 | Infra | Increased cores, tuned GC |
| Missing Grafana panels for Flow 3 | Gaps in metrics | R1 | Ops | Added new dashboard panels |

---

## ✅ 7. Conclusion & Sign-off

All NFRs achieved in **Round 2**.  
Peak & Soak stability confirmed.  

**Recommendation:** System ready for **Tranche 1 migration**.  

**Sign-off:**  
Performance Lead • QA Lead • Tech Lead • Delivery Manager

---

