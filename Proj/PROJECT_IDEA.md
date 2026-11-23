# ShotBoard – Film Storyboard Planner

**Author:** Szoboszlai Raul
**Type:** Individual project
**Estimated duration:** 2–3 weeks

---

## 1. Idea

Before a movie, short film, commercial or YouTube video is filmed, the crew plans **every camera shot** in advance using a *storyboard* / *shot list*. Shooting days are expensive (actors, equipment, locations are paid by the hour), so knowing exactly what to film, where and for how long saves time and money.

**ShotBoard** is a web application that replaces the paper/Excel storyboard. It lets a director (or a student / content creator) organize a film as a tree structure, attach sketches and details to every shot, and get an automatic summary of the production (runtime, shots per location, etc.).

## 2. Tree structure

```
Movie
└── Scene
    └── Shot
        ├── Dialogue          (text)
        ├── Storyboard Frame  (image)
        ├── Camera Angle      (Wide / Medium / Close-up / Over-the-shoulder / ...)
        ├── Duration          (seconds)
        └── Location          (text)
```

| Level | Description | Main fields |
|---|---|---|
| **Movie** | The whole film project | title, genre, description, director |
| **Scene** | A continuous moment of the story in one place and time | number, title, description, time of day (day/night) |
| **Shot** | One recording from a single camera position | number, description, dialogue, frame image, camera angle, duration (sec), location |

### Example

```
Movie: "The Last Train"
├── Scene 1: Train station, night
│   ├── Shot 1.1: Wide shot of empty platform   (Wide, 8 sec, Gara de Nord)
│   ├── Shot 1.2: Close-up on character's face  (Close-up, 4 sec, "I'm too late...")
│   └── Shot 1.3: Train leaving, seen from behind (Wide, 6 sec, Gara de Nord)
└── Scene 2: Inside the train
    └── ...
```

## 3. Functionalities

### `<add>`
- Create a movie
- Add scenes to a movie
- Add shots to a scene (with optional storyboard image upload)
- Edit / delete any element of the tree

### `<view>`
- List all movies
- View a movie as an expandable tree (Movie → Scenes → Shots)
- View the details of a single shot, including its storyboard frame

### `<summary>`
For a selected movie:
- **Total estimated runtime** (sum of all shot durations)
- Number of scenes and shots
- Shots per scene and the **longest scene**
- **Shots grouped by location** (useful for planning shooting days)
- **Shots grouped by camera angle**

## 4. Technical stack

| Part | Technology |
|---|---|
| Backend | Java, Spring Boot (RESTful API) |
| Persistence | Spring Data JPA + H2 database (file-based, so data persists between restarts) |
| Frontend | Simple web interface – HTML + CSS + JavaScript (fetch API) |
| Build | Maven |
| Version control | Git + GitHub, one branch per functionality |

## 5. REST API (draft)

| Method | Endpoint | Description |
|---|---|---|
| GET / POST | `/api/movies` | List / create movies |
| GET / PUT / DELETE | `/api/movies/{id}` | Get / update / delete a movie |
| GET / POST | `/api/movies/{movieId}/scenes` | List / add scenes of a movie |
| GET / PUT / DELETE | `/api/scenes/{id}` | Get / update / delete a scene |
| GET / POST | `/api/scenes/{sceneId}/shots` | List / add shots of a scene |
| GET / PUT / DELETE | `/api/shots/{id}` | Get / update / delete a shot |
| POST | `/api/shots/{id}/frame` | Upload storyboard image for a shot |
| GET | `/api/shots/{id}/frame` | Download / display the storyboard image |
| GET | `/api/movies/{id}/tree` | Full tree of a movie |
| GET | `/api/movies/{id}/summary` | Summary statistics of a movie |

### Storyboard frame storage

- The frame is an image file (**PNG / JPG**, max 5 MB) uploaded as `multipart/form-data`.
- The file is saved on disk in `uploads/frames/` (folder ignored by Git).
- The `Shot` entity only stores the **filename as a `String`** (e.g. `frameImage = "shot-12.png"`), `null` if no image was uploaded.
- The UI displays it with `<img src="/api/shots/12/frame">`.

## 6. Development plan (Git branches)

Like the book project, each functionality is developed on its own branch and merged into `main` through a pull request.

| # | Branch | Content | Week |
|---|---|---|---|
| 1 | `proj-idea` | This document | 1 |
| 2 | `proj-setup` | Spring Boot project skeleton, DB configuration | 1 |
| 3 | `proj-movies` | Movie entity, repository, service, REST controller | 1 |
| 4 | `proj-scenes` | Scene entity + Movie → Scene relationship | 1 |
| 5 | `proj-shots` | Shot entity + Scene → Shot relationship, image upload | 2 |
| 6 | `proj-tree-view` | Tree endpoint | 2 |
| 7 | `proj-summary` | Summary endpoint (runtime, locations, camera angles) | 2 |
| 8 | `proj-ui` | Simple web interface (add / view / summary) | 3 |
| 9 | `proj-final` | Tests, README, polish | 3 |
