🚀 Tranche 1 – Performance Test Report (Combined Execution, Multiple Rounds)
🧩 1. Summary
Objective	Validate Tranche 1 end-to-end flows under mixed production-like load
Flows Covered	1️⃣ Test-Pull-Push-Notify → External→Corp 2️⃣ Test-Pull-Decrypt-Push-Notify → External→Corp 3️⃣ Test-Pull-Scan-Encrypt-Push-Notify → Corp→External 4️⃣ Test-Pull-Scan-Push-Notify → Corp→Corp
Load Mix	4 000 files / hr (92 % Ext→Corp • 3 % Corp→Ext • 5 % Corp→Corp)
Environment	Performance / Pre-Prod
Test Rounds Planned	Round 1 – Baseline • Round 2 – Tuned • Round 3 – Validation
Overall Status	🚧 In Progress
📊 2. Test Execution Matrix – Combined Runs
Test Type	Load Profile	Purpose	Round 1 (Initial)	Round 2 (Post-Tuning)	Round 3 (Final Validation)	Trend / Notes
Smoke	3 files per flow	Env readiness / connectivity	✅ Passed
Report 1
	✅ Passed
Report 2
	–	Env stable / No changes
Baseline	1 small + 1 medium + 1 large per journey	Establish reference latency	✅ Passed
p95 19 s	✅ Passed
p95 17 s	✅ Passed
p95 16 s	Gradual improvement
Load	4 000 files/hr (92/3/5)	Sustained throughput	🚧 In Progress
3 850 files/hr	⏳ Planned	⏳ Planned	96 % throughput; tuning CPU
Concurrency	20 parallel files	Validate parallel processing	⏳ Planned	⏳ Planned	⏳ Planned	–
Peak	4 000 files / 30 min	Burst capacity	⏳ Planned	⏳ Planned	⏳ Planned	–
Soak	12 h steady load	Stability / memory leaks	⏳ Planned	⏳ Planned	⏳ Planned	–
Final Report	—	Consolidated evidence + sign-off	⏳ Pending	⏳ Pending	⏳ Pending	—
💾 Evidence References (per round)
Round	Date	Scenario / Config	Reports	Grafana Dashboards	Comments
Round 1	08 Oct 2025	Default journeys load	Gatling R1
	Grafana R1
	Baseline run
Round 2	10 Oct 2025	Increased thread pools + tuned GC	Gatling R2
	Grafana R2
	Throughput +4 %
Round 3	13 Oct 2025	Final config pre-release	Gatling R3
	Grafana R3
	To be executed
📈 3. KPI Summary (Trend Across Rounds)
Metric	Target (NFR)	Round 1	Round 2	Round 3	Status
Throughput (files/hr)	≥ 4 000	3 850	4 020	—	🟡 Improving
Success Rate	≥ 99 %	98.8 %	99.3 %	—	🟢 Pass
p95 Latency (small/med/large)	≤ 2 / 15 / 20	2.1 / 16 / 21	1.8 / 14 / 19	—	🟢 Pass
CPU Utilisation (%)	≤ 80 %	75 %	68 %	—	🟢 Pass
Memory Usage Drift (Soak)	≤ 20 %	—	—	—	⏳ TBD
📊 4. Jira Dashboard Snapshot
Epic	% Complete	Link
Umbrella (Tranche 1 Perf Testing)	70 %	View in Jira

Framework Enhancements	100 %	View in Jira

(Embed Jira dashboard macro for live story status.)

⚠️ 5. Risks & Actions
Risk	Impact	Round Detected	Owner	Mitigation
CPU spikes > 70 % on Flow 3 encrypt	May hit NFR under peak	R1	Infra	GC tuning, extra vCPU
High scan latency in Corp→Corp	Slight delay in baseline	R1	Perf Team	Increase scan threads
Monitoring gaps	Missed metrics	R2	Ops	Extend Grafana coverage
✅ 6. Conclusion & Sign-off

Summary: Multi-round testing shows consistent improvement.

Next Step: Complete Round 3 (peak + soak) → publish final evidence pack.

Recommendation: Proceed to Go/No-Go review after Round 3.

Sign-off By: Performance Lead | QA Lead | Tech Lead | Delivery Manager

💡 Usage Tips

Keep each round’s results in its own Confluence sub-page or section and link them in the Evidence References table.

Paste Gatling HTML and Grafana screenshots directly into the report after each run.

The Trend table gives a quick executive-view of tuning gains.

Embed a Jira filter macro for live progress.
