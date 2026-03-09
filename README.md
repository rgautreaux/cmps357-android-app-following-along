
# README.md

# cmps357-android-app-following-along

## Getting Started

This guide explains how to set up your development environment on **Windows** using **VS Code** to build and run the app.

### Prerequisites

Install the following tools before cloning the project:

1. **Git for Windows**
   - Download from [https://git-scm.com/download/win](https://git-scm.com/download/win)
   - Accept defaults during installation

2. **Java Development Kit (JDK) 17**
   - Download the Windows installer from [Adoptium](https://adoptium.net/temurin/releases/?version=17)
   - After installing, verify in a new terminal:
     ```
     java -version
     ```

3. **Android Studio** (required to install the Android SDK)
   - Download from [https://developer.android.com/studio](https://developer.android.com/studio)
   - During setup, accept the default SDK components (API 35 + Build Tools)
   - After installation, note the **Android SDK Location** shown in  
     `File → Settings → Appearance & Behavior → System Settings → Android SDK`

4. **VS Code**
   - Download from [https://code.visualstudio.com](https://code.visualstudio.com)
   - Install the following extensions from the Extensions panel (`Ctrl+Shift+X`):
     - **Kotlin** — syntax highlighting and code intelligence
     - **Extension Pack for Java** — Java language support required by Kotlin tooling
     - **Android iOS Emulator** *(optional)* — launch AVDs from VS Code

### Environment Variables

Set the following user environment variables (search **"Edit environment variables"** in the Windows Start menu):

| Variable | Value (example) |
|---|---|
| `ANDROID_HOME` | `C:\Users\YourName\AppData\Local\Android\Sdk` |
| `JAVA_HOME` | `C:\Program Files\Eclipse Adoptium\jdk-17.x.x.x-hotspot` |

Also add these to your **Path**:

```
%ANDROID_HOME%\platform-tools
%ANDROID_HOME%\emulator
%JAVA_HOME%\bin
```

Restart any open terminals after making these changes.

### Clone the Repository

Open a **Git Bash** or **PowerShell** terminal and run:

```bash
git clone https://github.com/nicholas-g-lipari-phd/monthly-balance-burn.git
cd monthly-balance-burn
```

### Open in VS Code

```bash
code .
```

### Build the Project

Run the Gradle wrapper from the project root in a terminal:

```bash
# Windows Command Prompt / PowerShell
.\gradlew.bat assembleDebug

# Git Bash
./gradlew assembleDebug
```

A successful build produces an APK at:

```
app\build\outputs\apk\debug\app-debug.apk
```

### Run Unit Tests

```bash
.\gradlew.bat test          # Windows
./gradlew test              # Git Bash
```

### Install on a Connected Device or Emulator

1. Start an Android emulator from **Android Studio → Device Manager**, or connect a physical device with **USB Debugging** enabled.
2. Install the debug APK:
   ```bash
   .\gradlew.bat installDebug
   ```
3. The app will launch automatically on the device.

---

## Goal

A personal-use Android app that:

- Accepts a single number representing the **current bank balance**
- Computes the **available spending per day** for the **remaining days in the current month**
- Displays a **bottom bar whose color reflects spending flexibility**

Color thresholds:

| Per-Day Value | Meaning | Color |
|---|---|---|
| ≤ 20 | Low | Red |
| 21–30 | Moderate | Yellow / Amber |
| 31–40 | Good | Light Green |
| > 40 | High | Green |

This is intended as a **minimal personal budgeting indicator**, not a full finance application.

---

## Tech Choices

- **Language:** Kotlin  
- **UI Framework:** Jetpack Compose  
- **Minimum SDK:** 26 (Android 8) or higher  
- **Architecture:** Lightweight MVVM

Primary components:

- `MainActivity` — hosts the UI
- `BudgetViewModel` — manages calculation logic and state
- `PreferencesRepository` — optional persistence layer

---

## Key Calculations

### Remaining Days in Month

Use the device's current date.

Variables:

```
today = LocalDate.now()
lastDay = today.withDayOfMonth(today.lengthOfMonth())
daysLeft = ChronoUnit.DAYS.between(today, lastDay) + 1
```

The `+1` counts **today** as a usable spending day.

---

### Spending Per Day

```
perDay = balance / daysLeft
```

Edge considerations:

- Guard against `daysLeft == 0`
- Negative balances are allowed and produce negative values

---

### Bar Color Thresholds

Map the computed `perDay` value to a color:

| Condition | Color |
|---|---|
| ≤ 20 | Red |
| 21–30 | Amber |
| 31–40 | Light Green |
| > 40 | Green |

---

## Data Flow and State

### UI State Model

Define a single state object:

```
balanceInput: String
balance: Double?
daysLeft: Int
perDay: Double?
barColor: Color
error: String?
```

Descriptions:

- **balanceInput** — raw text from the input field  
- **balance** — parsed numeric value or null if invalid  
- **daysLeft** — computed days remaining in the month  
- **perDay** — computed spending allowance  
- **barColor** — visual indicator color  
- **error** — validation message if parsing fails  

---

### Update Triggers

When input changes:

1. Parse text input to `Double`
2. Recompute `daysLeft`
3. Compute `perDay`
4. Determine `barColor`
5. Update UI state

Optional: persist last valid balance.

---

## Screens and UI Layout

Single-screen application.

### Components

Top section:

- Title: **Daily Spending**

Input section:

- TextField labeled **Current Balance**
- Optional buttons:
  - Clear
  - Use Last Value

Information display:

- Days left in month
- Spending per day formatted as currency

Bottom indicator:

- Full-width bar
- Fixed height
- Colored according to spending level
- Displays numeric `perDay` value centered

---

## Input Formatting

Simplified numeric input:

- Accept digits and decimal point
- Keyboard type: numeric
- Show validation message if parsing fails

Example valid inputs:

```
1200
1200.50
950.25
```

---

## Persistence (Optional)

Use **DataStore Preferences**.

Key:

```
last_balance
```

Behavior:

On startup:

1. Load `last_balance`
2. Populate the input field
3. Recalculate derived values

On valid input change:

- Save value to preferences.