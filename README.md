# 📉 Team Zero Internships

<p align="center">
  <img src="https://img.shields.io/badge/Internships_Acquired-0-red?style=for-the-badge" alt="Internships: 0">
  <img src="https://img.shields.io/badge/Rejections_Received-404-orange?style=for-the-badge" alt="Rejections: 404">
  <img src="https://img.shields.io/badge/LeetCode_Mediums-500%2B-blue?style=for-the-badge" alt="LeetCode">
  <img src="https://img.shields.io/badge/Copium_Level-Maximum-brightgreen?style=for-the-badge" alt="Copium">
</p>

> *"Status 404: Internship Not Found. But our code still compiles."*

---

## 👥 The Unemployed Dream Team

| Team Member | Role | Defensive Line in Interviews | Application Status | Fuel Source |
| :--- | :--- | :--- | :--- | :--- |
| **Rayed Fahmi** | Lead Architect | *"I know array indexing, so I know 0."* | Under Review (Forever) | Cold Brew |
| **Abid Ahnaf Khan** | Bug Hunter | *"I can fix bugs, just not HR's hiring algorithm."* | Ghosted x50 | Espresso |
| **Sabid Mahmud** | Data Structures Wizard | *"Graph traversal algorithms can't find my offer letter."* | Recruiter Screened | Energy Drinks |
| **Towsif Hassan** | UI / Docs Alchemist | *"My resume formatting is pixel-perfect, I promise."* | Applied 2m ago | Matcha |

---

## ♟️ Current Project: Console Chess (Phase 1)

A two-player chess game that runs in the console, written in Java. The board is drawn as ASCII text
and moves are typed in standard notation such as `E2 E4`.

* 📋 **Team to-do list & merge checkpoints:** [`TODO.md`](TODO.md)
* 📚 **Generated Javadoc:** [`docs/`](docs/) (open `docs/index.html` after generating)

### Project structure

```text
src/
├── board/    Board (8x8 grid, display), Position (row/column square)
├── pieces/   Piece (abstract), Color, Pawn, Rook, Knight, Bishop, Queen, King
├── core/     Game (main loop), Player, Move
├── ui/       ConsoleUI (all console input/output)
├── utils/    Utils (move-format validation & parsing)
└── Main.java entry point
docs/         generated Javadoc
```

### Build & run (JDK 17+)

```bash
# compile everything into out/
javac -d out $(find src -name "*.java")

# play
java -cp out Main
```

### Generate the Javadoc

```bash
javadoc -private -d docs -sourcepath src -subpackages board:pieces:core:ui:utils src/Main.java
```

Add `-Xdoclint:all` to that command to list any class, method or attribute that is missing a comment.

### Move notation

| Input | Meaning |
| :--- | :--- |
| `E2 E4` | Move the piece on E2 to E4. A capture is detected automatically. |
| `O-O` / `O-O-O` | Castle kingside / queenside *(optional)* |
| `E7 E8=Q` | Move a pawn to E8 and promote it to a Queen *(optional)* |

---

## ⚡ Tech Stack & Tools

![Python](https://img.shields.io/badge/Python-3776AB?style=flat-square&logo=python&logoColor=white)
![C++](https://img.shields.io/badge/C++-00599C?style=flat-square&logo=c%2B%2B&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white)
![LinkedIn](https://img.shields.io/badge/LinkedIn_Easy_Apply-0A66C2?style=flat-square&logo=linkedin&logoColor=white)

---

## 🎭 Team Lore & Fun Facts

* 📉 **The Name Origin:** We named ourselves after our combined sum of internship offer letters.
* 👻 **Ghosting Speedrun:** Record time for getting ghosted after a "Great chat!" email is 14 minutes.
* 🎯 **Ultimate Goal:** Make this team name completely inaccurate before the semester ends.
* 💬 **Motto:** *"Entry-level position requiring 5+ years of experience in technologies invented 2 years ago."*
* 📊 **Live Tracker:**
  ```text
  [Applications Sent] ====> 1,200
  [Interviews]         ==> 4
  [Offers]             => 0
  ```
