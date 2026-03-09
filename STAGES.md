# STAGES.md

## Versioning

Keep the app version in `app/src/main/assets/version.txt` aligned with the active stage number.
Use `0.0.<stage>` (example: Stage 2 -> `0.0.2`).

## Stage 1 — Project Setup

Create the Android project.

Steps:

1. Open Android Studio
2. Create **New Project**
3. Select **Empty Activity (Jetpack Compose)**
4. Enable Kotlin + Compose

Verify:

- App builds
- App runs on device
- Default Compose screen appears

### Progress

- [x] Android project created (Empty Activity + Jetpack Compose)
- [x] Kotlin + Compose enabled; Material3 applied
- [x] Gradle wrapper (8.10.2), version catalog, and `app/build.gradle.kts` configured (`compileSdk=35`, `minSdk=26`)
- [x] `MainActivity.kt` skeleton with default Greeting composable and `@Preview`
- [x] `ui/theme/` — `Color.kt`, `Theme.kt`, `Type.kt`
- [x] `AndroidManifest.xml`, resource values, and adaptive launcher icons
- [x] Unit test stub (`ExampleUnitTest.kt`) and instrumented test stub (`ExampleInstrumentedTest.kt`) added

**Status: ✅ Complete**

---

## Stage 2 — Calculation Utilities

Create a utility file:

```
BudgetMath.kt
```

Functions:

```
daysLeftInMonth(today: LocalDate): Int
perDay(balance: Double, daysLeft: Int): Double
colorForPerDay(value: Double): Color
```

Responsibilities:

- Date calculation
- Spending calculation
- Threshold color mapping

Optional:

- Add unit tests for these pure functions.

### Progress

- [x] `BudgetMath.kt` created
- [x] `daysLeftInMonth()` implemented and tested
- [x] `perDay()` implemented and tested
- [x] `colorForPerDay()` implemented and tested

**Status: ✅ Complete**

---

## Stage 3 — ViewModel

Create:

```
BudgetViewModel.kt
```

Responsibilities:

- Manage UI state
- Handle input changes
- Trigger recalculations

Expose:

```
StateFlow<UiState>
```

Functions:

```
onBalanceChanged(text: String)
loadLastBalance()
saveBalanceIfValid()
```

### Progress

- [x] `BudgetViewModel.kt` created
- [x] `UiState` data class defined
- [x] `StateFlow<UiState>` exposed
- [x] `onBalanceChanged()`, `loadLastBalance()`, `saveBalanceIfValid()` implemented

**Status: ✅ Complete**

---

## Stage 4 — Compose UI

Create the screen layout.

Responsibilities:

- Collect state using `collectAsState()`
- Bind TextField input
- Display computed values
- Render bottom color bar

Components:

```
Column
TextField
Text
Spacer
Box (bottom color bar)
```

The bottom bar:

- Full width
- Fixed height
- Background color based on `state.barColor`

### Progress

- [x] Screen layout composable created
- [x] `TextField` input bound to ViewModel
- [x] Computed values (`daysLeft`, `perDay`) displayed
- [x] Bottom color bar rendered with correct threshold colors

**Status: ✅ Complete**

---

## Stage 5 — Persistence Layer (Optional)

Create:

```
PreferencesRepository.kt
```

Implement:

- DataStore preferences
- Save balance
- Load balance

Integrate repository into ViewModel.

Dependency injection is optional for a small project.

### Progress

- [x] `PreferencesRepository.kt` created
- [x] DataStore dependency added to `app/build.gradle.kts`
- [x] Save and load balance operations implemented
- [x] Repository integrated into `BudgetViewModel`

**Status: ✅ Complete**

---

## Stage 6 — Device Testing

Run the app on the phone.

Verify:

- Input correctly recalculates values
- Days remaining match the calendar
- Color thresholds work
- Invalid input does not crash the app
- End-of-month behavior is correct

Example test values:

| Balance | Days Left | Expected |
|---|---|---|
| 400 | 20 | 20 per day |
| 900 | 30 | 30 per day |
| 1600 | 40 | 40 per day |

### Progress

- [ ] App installed on physical device or emulator
- [ ] Input recalculates values correctly
- [ ] Days remaining match the calendar
- [ ] Color thresholds verified (≤20 red, 21–30 amber, 31–40 light green, >40 green)
- [ ] Invalid input handled safely (no crash)
- [ ] End-of-month edge case confirmed

**Status: 🔲 Not Started**

---

## Stage 7 — Personal Deployment

Two options.

### Option 1 — Direct Install

Use Android Studio:

```
Run → Install on device
```

Recommended for development.

---

### Option 2 — Build APK

Build a standalone APK.

```
Build → Build Bundle(s) / APK(s) → Build APK(s)
```

Install the APK manually on the device.

### Progress

- [ ] App deployed to personal device via direct install or APK build
- [ ] App runs stably in everyday use

**Status: 🔲 Not Started**

---

## Edge Case Decisions

Recommended defaults:

| Situation | Behavior |
|---|---|
| Today counted as spending day | Yes |
| Invalid input | Gray bar + error message |
| Negative balance | Allowed, red indicator |
| Last day of month | daysLeft = 1 |

---

## Suggested File Structure

```
app/
 ├── MainActivity.kt
 ├── BudgetViewModel.kt
 ├── UiState.kt
 ├── BudgetMath.kt
 ├── PreferencesRepository.kt
 └── ui/
     └── theme/
```

---

## Done Definition

The app is complete when:

- Entering a balance immediately updates the per-day value
- Remaining days match the calendar month
- Bottom bar color changes at thresholds 20 / 30 / 40
- Invalid input is handled safely
- Optional persistence restores the last balance