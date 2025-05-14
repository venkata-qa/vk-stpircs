# 🧬 Test Data Reference

## 👤 Claimant Profiles

| Profile             | Description                              |
|---------------------|------------------------------------------|
| Single-benefit      | UC only                                  |
| Multi-benefit       | UC + PIP                                 |
| No accessibility    | No registered accessibility needs        |

## 📝 Sample Payloads

### Valid
```json
{
  "claimantId": "12345",
  "needs": ["screen-reader", "BSL-interpreter"]
}
Invalid
json
Copy
Edit
{
  "claimantId": "",
  "needs": null
}
yaml
Copy
Edit

---

## 📄 `environment-config.md`

```markdown
# 🌐 Environment Configuration

## 🔧 Base URLs

| Environment | Base URL                                | Notes                                   |
|-------------|------------------------------------------|-----------------------------------------|
| Preview     | https://preview.api.accessibility.dwp   | Used for functional and perf testing    |
| INT         | https://int.api.accessibility.dwp       | Connected to integration layer          |
| UAT (TBC)   | TBD                                      | Pending confirmation                    |

## 🔐 Authentication
- OAuth2 Bearer Token required
- Token retrieved from internal IAM

## ♻️ Reset Policy
- Preview: Resets nightly
- INT: Reset weekly or on demand
