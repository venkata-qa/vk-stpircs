# Accessibility Needs – Complete Test Design, Execution & Traceability

## 1. User Stories & Mapped Test Cases

### US-1
**Description:** Generate an event when a claimant adds or updates their accessibility needs.
**Covered Test Cases:** TC-001-ADD, TC-002-ADD, TC-003-ADD, TC-004-ADD, TC-005-ADD, TC-007-ADD, TC-008-ADD, TC-009-ADD, TC-010-ADD, TC-011-ADD, TC-012-ADD, TC-013-ADD, TC-014-ADD, TC-015-ADD, TC-017-ADD, TC-018-ADD, TC-019-ADD, TC-020-ADD, TC-021-ADD, TC-022-ADD

### US-2
**Description:** Process and map 'ACCESSIBILITY_NEEDS_GATHER_EVENT' into the structure required by API020.
**Covered Test Cases:** TC-US2-001

### US-3
**Description:** Send the mapped accessibility data to CIS using API020.
**Covered Test Cases:** TC-US3-001

### US-4
**Description:** Generate an event when a claimant removes their accessibility needs.
**Covered Test Cases:** TC-US4-001

### US-5
**Description:** Process and map 'ACCESSIBILITY_NEEDS_REMOVE_EVENT' into the structure required by API020.
**Covered Test Cases:** TC-006-REM, TC-016-REM, TC-023-REM

## 2. Designed Test Cases

### TC-001-ADD
- **Title:** Verify system logs addition of accessibility need 'Large print' for Greg Davies
- **Mapped User Story:** US-1
- **Claimant Name:** Greg Davies
- **Claimant ID:** cccdlbab-713e-4b9d-8f5f-e7d13631f94d
- **Action:** Added
- **Accessibility Need:** Large print
- **Date:** 10 Nov 2024
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for Large print

### TC-002-ADD
- **Title:** Verify system logs addition of accessibility need 'Hearing or induction loop' for Greg Davies
- **Mapped User Story:** US-1
- **Claimant Name:** Greg Davies
- **Claimant ID:** cccdlbab-713e-4b9d-8f5f-e7d13631f94d
- **Action:** Added
- **Accessibility Need:** Hearing or induction loop
- **Date:** 10 Nov 2024
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for Hearing or induction loop

### TC-003-ADD
- **Title:** Verify system logs addition of accessibility need 'British Sign Language' for Roland Morgan
- **Mapped User Story:** US-1
- **Claimant Name:** Roland Morgan
- **Claimant ID:** ebb28d18-f1b4-4330-a2a7-aб7beca0f4f1
- **Action:** Added
- **Accessibility Need:** British Sign Language
- **Date:** 5 Jan 2023
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for British Sign Language

### TC-004-ADD
- **Title:** Verify system logs addition of accessibility need 'Step-free access' for Roland Morgan
- **Mapped User Story:** US-1
- **Claimant Name:** Roland Morgan
- **Claimant ID:** ebb28d18-f1b4-4330-a2a7-a67beca0f4f1
- **Action:** Added
- **Accessibility Need:** Step-free access
- **Date:** 22 Jan 2025
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for Step-free access

### TC-005-ADD
- **Title:** Verify system logs addition of accessibility need 'Textphone or Minicom' for James Patel
- **Mapped User Story:** US-1
- **Claimant Name:** James Patel
- **Claimant ID:** e6182861-50fb-4fc8-b296-487485faeb74
- **Action:** Added
- **Accessibility Need:** Textphone or Minicom
- **Date:** 12 Mar 2023
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for Textphone or Minicom

### TC-006-REM
- **Title:** Verify system logs removal of accessibility need 'Textphone or Minicom' for James Patel
- **Mapped User Story:** US-5
- **Claimant Name:** James Patel
- **Claimant ID:** e6182861-50fb-4fc8-b296-487485faeb74
- **Action:** Removed
- **Accessibility Need:** Textphone or Minicom
- **Date:** 14 Feb 2025
- **Expected Result:** 'ACCESSIBILITY_NEEDS_REMOVE_EVENT' is generated for Textphone or Minicom

### TC-007-ADD
- **Title:** Verify system logs addition of accessibility need 'Step-free access' for Luke Richards
- **Mapped User Story:** US-1
- **Claimant Name:** Luke Richards
- **Claimant ID:** ac269231-e57d-4ba8-abd7-eaf85a3b4aбc
- **Action:** Added
- **Accessibility Need:** Step-free access
- **Date:** 16 Oct 2024
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for Step-free access

### TC-008-ADD
- **Title:** Verify system logs addition of accessibility need 'Wide doorway' for Luke Richards
- **Mapped User Story:** US-1
- **Claimant Name:** Luke Richards
- **Claimant ID:** ac269231-e57d-4ba8-abd7-eaf85a3b4aбc
- **Action:** Added
- **Accessibility Need:** Wide doorway
- **Date:** 16 Oct 2024
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for Wide doorway

### TC-009-ADD
- **Title:** Verify system logs addition of accessibility need 'Large print' for Andrew Taylor
- **Mapped User Story:** US-1
- **Claimant Name:** Andrew Taylor
- **Claimant ID:** ac269231-e57d-4ba8-abd7-eaf85a3b4a6c
- **Action:** Added
- **Accessibility Need:** Large print
- **Date:** 5 Mar 2025
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for Large print

### TC-010-ADD
- **Title:** Verify system logs addition of accessibility need 'Relay UK' for Andrew Taylor
- **Mapped User Story:** US-1
- **Claimant Name:** Andrew Taylor
- **Claimant ID:** eOaf5bac-35da-4f32-bc57-6c514ad8cd5b
- **Action:** Added
- **Accessibility Need:** Relay UK
- **Date:** 30 Dec 2024
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for Relay UK

### TC-011-ADD
- **Title:** Verify system logs addition of accessibility need 'British Sign Language' for Andrew Taylor
- **Mapped User Story:** US-1
- **Claimant Name:** Andrew Taylor
- **Claimant ID:** eOaf5bac-35da-4f32-bc57-6c514ad8cd5b
- **Action:** Added
- **Accessibility Need:** British Sign Language
- **Date:** 30 Dec 2024
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for British Sign Language

### TC-012-ADD
- **Title:** Verify system logs addition of accessibility need 'Braille' for Andrew Taylor
- **Mapped User Story:** US-1
- **Claimant Name:** Andrew Taylor
- **Claimant ID:** eOaf5bac-35da-4f32-bc57-6c514ad8cd5b
- **Action:** Added
- **Accessibility Need:** Braille
- **Date:** 30 Dec 2024
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for Braille

### TC-013-ADD
- **Title:** Verify system logs addition of accessibility need 'Relay UK' for Laura Thomas
- **Mapped User Story:** US-1
- **Claimant Name:** Laura Thomas
- **Claimant ID:** 6b84c11e-a832-4d4c-87f9-5f8fbf30ad3e
- **Action:** Added
- **Accessibility Need:** Relay UK
- **Date:** 12/12/2024
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for Relay UK

### TC-014-ADD
- **Title:** Verify system logs addition of accessibility need 'Large print' for Laura Thomas
- **Mapped User Story:** US-1
- **Claimant Name:** Laura Thomas
- **Claimant ID:** 6b84c11e-a832-4d4c-87f9-5f8fbf30ad3e
- **Action:** Added
- **Accessibility Need:** Large print
- **Date:** 9 Jan 2025
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for Large print

### TC-015-ADD
- **Title:** Verify system logs addition of accessibility need 'Step-free access' for Laura Thomas
- **Mapped User Story:** US-1
- **Claimant Name:** Laura Thomas
- **Claimant ID:** 6684c11e-a832-4d4c-87f9-5f8fbf30ad3e
- **Action:** Added
- **Accessibility Need:** Step-free access
- **Date:** 9 Jan 2025
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for Step-free access

### TC-016-REM
- **Title:** Verify system logs removal of accessibility need 'Relay UK' for Laura Thomas
- **Mapped User Story:** US-5
- **Claimant Name:** Laura Thomas
- **Claimant ID:** 6b84c11e-a832-4d4c-87f9-5f8fbf30ad3e
- **Action:** Removed
- **Accessibility Need:** Relay UK
- **Date:** 9 Jan 2025
- **Expected Result:** 'ACCESSIBILITY_NEEDS_REMOVE_EVENT' is generated for Relay UK

### TC-017-ADD
- **Title:** Verify system logs addition of accessibility need 'Wide doorway' for Robert Thomas
- **Mapped User Story:** US-1
- **Claimant Name:** Robert Thomas
- **Claimant ID:** 6b84c11e-a832-4d4c-87f9-5f8fbf30ad3e
- **Action:** Added
- **Accessibility Need:** Wide doorway
- **Date:** 12-Feb-25
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for Wide doorway

### TC-018-ADD
- **Title:** Verify system logs addition of accessibility need 'British Sign Language' for Robert Thomas
- **Mapped User Story:** US-1
- **Claimant Name:** Robert Thomas
- **Claimant ID:** ad262ea6-3178-4cb5-8ece-45bd0622e8d2
- **Action:** Added
- **Accessibility Need:** British Sign Language
- **Date:** 21 Dec 2024
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for British Sign Language

### TC-019-ADD
- **Title:** Verify system logs addition of accessibility need 'Textphone or Minicom' for Robert Thomas
- **Mapped User Story:** US-1
- **Claimant Name:** Robert Thomas
- **Claimant ID:** ad262ea6-3178-4cb5-8ece-45bd0622e8d2
- **Action:** Added
- **Accessibility Need:** Textphone or Minicom
- **Date:** 21 Dec 2024
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for Textphone or Minicom

### TC-020-ADD
- **Title:** Verify system logs addition of accessibility need 'Hearing or induction loop' for Scott Thomas
- **Mapped User Story:** US-1
- **Claimant Name:** Scott Thomas
- **Claimant ID:** 53180dbc-c8ca-43c8-9340-6f8f4efa0cd7
- **Action:** Added
- **Accessibility Need:** Hearing or induction loop
- **Date:** 9 Oct 2022
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for Hearing or induction loop

### TC-021-ADD
- **Title:** Verify system logs addition of accessibility need 'Step-free access' for Emily Scott
- **Mapped User Story:** US-1
- **Claimant Name:** Emily Scott
- **Claimant ID:** 53180d6c-c8ca-43c8-9340-6f8f4efa0cd7
- **Action:** Added
- **Accessibility Need:** Step-free access
- **Date:** 18 Feb 2025
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for Step-free access

### TC-022-ADD
- **Title:** Verify system logs addition of accessibility need 'Lift or ground floor appointment' for Emily Scott
- **Mapped User Story:** US-1
- **Claimant Name:** Emily Scott
- **Claimant ID:** 53180d6c-c8ca-43c8-9340-6f8f4efa0cd7
- **Action:** Added
- **Accessibility Need:** Lift or ground floor appointment
- **Date:** 18 Feb 2025
- **Expected Result:** 'ACCESSIBILITY_NEEDS_GATHER_EVENT' is generated with data for Lift or ground floor appointment

### TC-023-REM
- **Title:** Verify system logs removal of accessibility need 'Hearing or induction loop' for Emily Scott
- **Mapped User Story:** US-5
- **Claimant Name:** Emily Scott
- **Claimant ID:** 53180d6c-c8ca-43c8-9340-6f8f4efa0cd7
- **Action:** Removed
- **Accessibility Need:** Hearing or induction loop
- **Date:** 18 Feb 2025
- **Expected Result:** 'ACCESSIBILITY_NEEDS_REMOVE_EVENT' is generated for Hearing or induction loop

### TC-US2-001
- **Title:** Verify system maps GATHER_EVENT to API020 format
- **Mapped User Story:** US-2
- **Claimant Name:** System
- **Claimant ID:** N/A
- **Action:** Process
- **Accessibility Need:** Any
- **Date:** N/A
- **Expected Result:** System transforms event to API020 schema

### TC-US3-001
- **Title:** Verify system sends mapped data to CIS via API020
- **Mapped User Story:** US-3
- **Claimant Name:** System
- **Claimant ID:** N/A
- **Action:** Transmit
- **Accessibility Need:** Any
- **Date:** N/A
- **Expected Result:** System sends correctly mapped data to CIS endpoint

### TC-US4-001
- **Title:** Verify system generates REMOVE_EVENT when claimant removes accessibility need
- **Mapped User Story:** US-4
- **Claimant Name:** Any Claimant
- **Claimant ID:** Sample-ID
- **Action:** Remove
- **Accessibility Need:** Any
- **Date:** Any Valid Date
- **Expected Result:** 'ACCESSIBILITY_NEEDS_REMOVE_EVENT' is generated

## 3. Test Data Executions

| Claimant Name | Claimant ID | Action | Accessibility Need | Date | Test Case ID |
|---------------|-------------|--------|---------------------|------|---------------|
| Greg Davies | cccdlbab-713e-4b9d-8f5f-e7d13631f94d | Added | Large print | 10 Nov 2024 | TC-001-ADD |
| Greg Davies | cccdlbab-713e-4b9d-8f5f-e7d13631f94d | Added | Hearing or induction loop | 10 Nov 2024 | TC-002-ADD |
| Roland Morgan | ebb28d18-f1b4-4330-a2a7-aб7beca0f4f1 | Added | British Sign Language | 5 Jan 2023 | TC-003-ADD |
| Roland Morgan | ebb28d18-f1b4-4330-a2a7-a67beca0f4f1 | Added | Step-free access | 22 Jan 2025 | TC-004-ADD |
| James Patel | e6182861-50fb-4fc8-b296-487485faeb74 | Added | Textphone or Minicom | 12 Mar 2023 | TC-005-ADD |
| James Patel | e6182861-50fb-4fc8-b296-487485faeb74 | Removed | Textphone or Minicom | 14 Feb 2025 | TC-006-REM |
| Luke Richards | ac269231-e57d-4ba8-abd7-eaf85a3b4aбc | Added | Step-free access | 16 Oct 2024 | TC-007-ADD |
| Luke Richards | ac269231-e57d-4ba8-abd7-eaf85a3b4aбc | Added | Wide doorway | 16 Oct 2024 | TC-008-ADD |
| Andrew Taylor | ac269231-e57d-4ba8-abd7-eaf85a3b4a6c | Added | Large print | 5 Mar 2025 | TC-009-ADD |
| Andrew Taylor | eOaf5bac-35da-4f32-bc57-6c514ad8cd5b | Added | Relay UK | 30 Dec 2024 | TC-010-ADD |
| Andrew Taylor | eOaf5bac-35da-4f32-bc57-6c514ad8cd5b | Added | British Sign Language | 30 Dec 2024 | TC-011-ADD |
| Andrew Taylor | eOaf5bac-35da-4f32-bc57-6c514ad8cd5b | Added | Braille | 30 Dec 2024 | TC-012-ADD |
| Laura Thomas | 6b84c11e-a832-4d4c-87f9-5f8fbf30ad3e | Added | Relay UK | 12/12/2024 | TC-013-ADD |
| Laura Thomas | 6b84c11e-a832-4d4c-87f9-5f8fbf30ad3e | Added | Large print | 9 Jan 2025 | TC-014-ADD |
| Laura Thomas | 6684c11e-a832-4d4c-87f9-5f8fbf30ad3e | Added | Step-free access | 9 Jan 2025 | TC-015-ADD |
| Laura Thomas | 6b84c11e-a832-4d4c-87f9-5f8fbf30ad3e | Removed | Relay UK | 9 Jan 2025 | TC-016-REM |
| Robert Thomas | 6b84c11e-a832-4d4c-87f9-5f8fbf30ad3e | Added | Wide doorway | 12-Feb-25 | TC-017-ADD |
| Robert Thomas | ad262ea6-3178-4cb5-8ece-45bd0622e8d2 | Added | British Sign Language | 21 Dec 2024 | TC-018-ADD |
| Robert Thomas | ad262ea6-3178-4cb5-8ece-45bd0622e8d2 | Added | Textphone or Minicom | 21 Dec 2024 | TC-019-ADD |
| Scott Thomas | 53180dbc-c8ca-43c8-9340-6f8f4efa0cd7 | Added | Hearing or induction loop | 9 Oct 2022 | TC-020-ADD |
| Emily Scott | 53180d6c-c8ca-43c8-9340-6f8f4efa0cd7 | Added | Step-free access | 18 Feb 2025 | TC-021-ADD |
| Emily Scott | 53180d6c-c8ca-43c8-9340-6f8f4efa0cd7 | Added | Lift or ground floor appointment | 18 Feb 2025 | TC-022-ADD |
| Emily Scott | 53180d6c-c8ca-43c8-9340-6f8f4efa0cd7 | Removed | Hearing or induction loop | 18 Feb 2025 | TC-023-REM |

## 4. Requirement to Test Case Traceability Matrix

| User Story | Covered Test Case IDs |
|------------|------------------------|
| US-1 | TC-001-ADD, TC-002-ADD, TC-003-ADD, TC-004-ADD, TC-005-ADD, TC-007-ADD, TC-008-ADD, TC-009-ADD, TC-010-ADD, TC-011-ADD, TC-012-ADD, TC-013-ADD, TC-014-ADD, TC-015-ADD, TC-017-ADD, TC-018-ADD, TC-019-ADD, TC-020-ADD, TC-021-ADD, TC-022-ADD |
| US-2 | TC-US2-001 |
| US-3 | TC-US3-001 |
| US-4 | TC-US4-001 |
| US-5 | TC-006-REM, TC-016-REM, TC-023-REM |