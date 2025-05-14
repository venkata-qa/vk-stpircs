# 📄 Accessibility Data API – QA Documentation

## 📘 Overview
This page documents the testing strategy, scenarios, tools, and environments for the **Accessibility Data API**, which enables the automatic sharing of claimants’ accessibility needs across DWP services. This supports:

- Reducing manual interventions (e.g., CIS 500 forms)
- Ensuring compliance with the **Public Sector Equality Duty (PSED)**
- Maintaining secure and reliable system communication

## 🎯 Test Objectives

- ✅ Automatically distribute accessibility needs from UC to all relevant DWP services  
- ✅ Validate data accuracy, completeness, and **API020** compliance  
- ✅ Ensure secure transmission and correct fallback for failures  
- ✅ Limit manual workarounds by ensuring system reliability

## 🧪 Test Types & Strategy

| Test Type              | Description                                                        | Status     |
|------------------------|--------------------------------------------------------------------|------------|
| Functional Testing     | Validating each endpoint’s expected behavior under various inputs | ✅ In Scope |
| Integration Testing    | Ensuring correct interaction with external systems, queues, and DBs | ✅ In Scope |
| End-to-End Testing     | Simulating real user flows across the full system                  | ✅ In Scope |
| Non-functional Testing | Includes performance, security, usability, reliability, etc.       | 🔄 To Be Confirmed |

> Non-functional testing scope will be finalized based on risk assessment and timelines.

## 🌐 Environment Details

| Environment   | Purpose                                 | Used For                                 |
|--------------|------------------------------------------|-------------------------------------------|
| Preview      | Safe for early-stage testing             | Functional Testing, Basic Performance     |
| INT (Integration) | Connected to back-end and queues | Integration Testing, End-to-End Testing   |
| UAT (TBC)    | Final business validation                | TBD                                       |

> All environments use OAuth2 authentication and anonymized test data.

## 🔗 Traceability Matrix

| User Story ID | Requirement                                             | Linked Test Cases       |
|---------------|---------------------------------------------------------|--------------------------|
| US001         | Notify all DWP services when accessibility needs update | TC001, TC002, TC003      |
| US002         | Agents view current claimant accessibility data         | TC004, TC005             |
| US003         | Graceful failure handling and fallback                  | TC006, TC007             |

📄 [View Full Test Cases](./test-cases.md)

## 🧬 Test Data

### Claimant Profiles
- Single-benefit claimant  
- Multi-benefit claimant  
- Claimant with no accessibility needs  

### Accessibility Needs
- Requires large print  
- Needs sign language interpreter  
- Requires home visit  

## 🛠️ Tools & Frameworks

| Tool              | Purpose                            | Notes                           |
|-------------------|------------------------------------|----------------------------------|
| Postman           | Manual & exploratory testing        | Collections for all endpoints   |
| REST-assured      | Automated API testing (Java)        | Integrated with CI/CD           |
| JMeter            | Performance/load simulation         | Preview environment             |
| Swagger UI        | API schema validation               | Based on API020                 |
| Jenkins / GitHub  | CI test automation                  | Post-deployment validation      |
| Jira              | Bug tracking & traceability         | Linked to test cases and runs   |

## 📊 Test Execution & Reporting

- Manual: via Postman
- Automated: via Jenkins/GitHub CI pipelines
- Results published to dashboards
- Defects tracked in Jira

📎 [Latest Test Report](./test-reports/sprint-24.md)  
🐞 [Known Issues](./known-issues.md)

## 🔐 Security & Compliance

- OAuth2 authentication required
- TLS encryption in transit
- API020 schema compliance
- Regular vulnerability scans performed

## 📚 References

- [API020 Specification](#)
- [Business Requirements](#)
- [User Stories](#)
- [Test Data Setup](./test-data.md)
