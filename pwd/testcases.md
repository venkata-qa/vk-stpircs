# 🧪 Accessibility Data API – Test Cases

## 📘 Purpose
Full list of test cases, grouped by endpoint and scenario. Each case includes steps and expected outcomes.

| Test ID | Endpoint                     | Method | Scenario                                | Steps                                                                 | Expected Result                            | Status       |
|---------|------------------------------|--------|-----------------------------------------|-----------------------------------------------------------------------|--------------------------------------------|--------------|
| TC001   | /accessibility/needs/{id}    | GET    | Valid ID                                | 1. Provide valid ID<br>2. Call API                                    | 200 OK with correct data                   | ✅ Passed     |
| TC002   | /accessibility/needs/{id}    | GET    | Invalid ID                              | 1. Provide non-existent ID<br>2. Call API                             | 404 Not Found                              | ✅ Passed     |
| TC003   | /accessibility/submit        | POST   | Submit valid payload                    | 1. Prepare valid JSON<br>2. Submit to API                             | 201 Created                                | 🔄 In Progress |
| TC004   | /accessibility/submit        | POST   | Submit incomplete payload               | 1. Missing required fields<br>2. Submit API call                      | 400 Bad Request                            | ✅ Passed     |
| TC005   | /accessibility/needs/{id}    | GET    | Unauthorized access                     | 1. Remove token<br>2. Submit request                                  | 401 Unauthorized                           | ✅ Passed     |
| TC006   | /accessibility/submit        | POST   | Simulate API failure & check fallback   | 1. Force API failure<br>2. Monitor logs & process trigger fallback    | Error logged; manual process invoked       | ❌ Failed     |
