# 🚗 Lane Dodge


**Lane Dodge** is an Android arcade-style racing game developed as an academic assignment.
The game combines **sensor-based controls**, **real-time gameplay** on a wide road, and a **location-based high score system** utilizing Google Maps fragments.

---

## 🎮 Game Overview

In **Lane Dodge**, the player controls a car on a wide five-lane road. The objective is to avoid obstacles, collect coins, and survive for the longest possible distance.

The game supports multiple control modes (buttons and accelerometer-based tilt controls), selectable speed (Slow/Fast), and persistent storage for tracking top scores.


---

## 🧭 Main Features

### 🏁 Start Menu
The main menu provides clear navigation to game modes and settings:
* **Speed Setting:** A toggle switch to choose between **Slow** and **Fast** gameplay modes.
* **Control Modes:**
    * **Start Button Mode:** Use on-screen buttons for movement.
    * **Start Sensor Mode:** Use device tilt (accelerometer) for movement.
* **Top Ten:** Navigate to the high score screen.
---
### 🚘 Gameplay
* **Environment:** A wide five-lane road allowing for strategic movement.
* **Lives System:** The player starts with **3 hearts (❤️)** displayed on screen. Hitting an obstacle reduces a life.
* **Sensor Controls:**
    * Tilt **Left/Right** to change lanes.
* **Scoring & Feedback:**
  * 🧮 **Score Calculation:** The final score is based on the number of coins collected and the distance traveled.
  * 🪙 **Coins:** Collect coins to increase your score.
  * 📏 **Odometer:** Tracks the distance traveled during the game.
  * 🔊 **Audio Feedback:** Sound cues indicate important game events such as coin collection and collisions.

---
**✍️ Game Over Flow:**
  
When the game ends, if the player’s score qualifies for the Top 10, a dialog is displayed prompting the player to enter their name.  
The name is then saved together with the score and location and added to the Top 10 list.

After game over, the player is presented with two options:
- Navigate to the **Top Ten** screen to view high scores.
- Return to the **Main Menu** to start a new game.
---

### 🏆 Top Ten Screen
This screen is composed of two fragments displayed simultaneously:

1. **Top 10 List Fragment:**  
   Displays a ranked list of the top 10 scores, including the achieved score and the location where it was recorded.

2. **Map Fragment:**  
   An integrated Google Maps view displaying markers for the geographic location of each high score.

When a score is selected from the Top 10 list, the corresponding location is highlighted on the map, allowing the user to see exactly where the score was achieved.  
An active internet connection is required in order to display the map.

---

## 🛠️ Technologies Used

* **💻Language:** Kotlin
* **🧩Architecture:** 
  - Activities for main screens (Menu, Game, TopTen)
  - Fragments for split-screen layout (top 10 list + map view)
* **📱Sensors:** 
  - Accelerometer for directional control (left/right tilt)
* **🌍Google Services:** 
  - Google Maps SDK for location markers
  - Location Services for GPS tracking
* **💾Data Storage:**
  - SharedPreferences + Gson for score persistence
* 🔊 **Audio Feedback:** Sound cues indicate important game events


---

## 🚀 Setup & Installation

To run this project locally:

1.  **Clone the repository.**
2.  **Google Maps API Key:**
    * This project requires a valid Google Maps API Key.
    * Add your key to the `AndroidManifest.xml` within the application tag:
    ```xml
    <meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="YOUR_API_KEY_HERE" />
    ```
3.  **Build and Run** on an Android device or emulator supporting sensor emulation.

---

## 🎥 Demo Video

▶️ **Watch the Gameplay & Features Demo:**
(Demonstrates menu navigation, sensor-based tilt controls, gameplay mechanics, and the split-fragment Top Ten screen)


[![Watch the demo](https://img.youtube.com/vi/31F8GFOLzRg/hqdefault.jpg)](https://www.youtube.com/watch?v=31F8GFOLzRg)
