# TAI LIEU KIEN TRUC VA THIET KE HE THONG MUSICAPP

Tai lieu nay duoc viet lai dua tren ma nguon hien tai trong cac module:

- `app/backend` (Spring Boot API + JWT)
- `app/mobile` (Android app Java + CameraX + ExoPlayer + TFLite)
- `AI` (Flask service phan tich cam xuc)
- `database` (MySQL schema)

## 1. Kien truc tong quan

### 1.1 Cac khoi he thong

- Client:
  - Android mobile app (`MainActivity`, `HomeFragment`, `SearchFragment`, `PlaylistFragment`, `ProfileFragment`, `EmotionActivity`, `EmotionResultActivity`, `AdminActivity`).
  - Chua co web frontend rieng trong repo hien tai.
- API Server:
  - Spring Boot backend (`/api/**`) voi cac nhom endpoint: auth, songs, genres, emotion, me, favorites, history, playlists, admin.
  - Bao mat JWT qua `JwtAuthenticationFilter`.
- AI Service:
  - Flask service (`AI/app.py`) endpoint `/analyze-emotion` va `/health`.
- Data Layer:
  - MySQL (users, songs, playlists, favorites, history, emotion logs, artists/genres, junction tables).
- Playback Layer (trong mobile):
  - `AudioPlayer` dung ExoPlayer phat audio tu `fileUrl`.

### 1.2 So do kien truc tong quan

```mermaid
flowchart LR
    subgraph MobileClient[Android Mobile Client]
        UI[Activities/Fragments UI]
        APIClient[Retrofit + JwtInterceptor]
        Player[AudioPlayer (ExoPlayer)]
        LocalML[EmotionTFLiteClassifier]
        SP[SharedPreferences Session]
    end

    subgraph Backend[Spring Boot API Server]
        C1[Auth/Song/Playlist/History/Favorite Controllers]
        C2[EmotionController + AdminController + MeController]
        SEC[SecurityConfig + JwtAuthenticationFilter + JwtService]
        REPO[JPA Repositories + JdbcTemplate]
    end

    subgraph AIService[Flask AI Service]
        A1[/POST /analyze-emotion/]
        A2[TensorFlow model infer]
    end

    subgraph DB[MySQL]
        T1[(users, songs, playlists)]
        T2[(favorites, listening_history, emotion_logs)]
        T3[(artists, genres, song_artists, song_genres)]
    end

    UI --> APIClient
    UI --> Player
    UI --> LocalML
    UI --> SP

    APIClient -->|HTTP + Bearer JWT| Backend
    Backend --> REPO
    REPO --> DB

    C2 -->|/api/emotion/analyze| A1
    A1 --> A2
    C2 -->|/api/emotion/recommend| REPO
```

### 1.3 Ghi chu quan trong tu code hien tai

- Security dang cho phep `permitAll` cho mot so endpoint user (`/api/favorites/**`, `/api/history/**`, `/api/playlists/**`, `/api/me`) de tranh 403 khi dev mobile.
- Backend van parse JWT neu co header Bearer hop le.
- Luong emotion co 2 cach:
  - On-device TFLite: mobile classify roi goi `/api/emotion/recommend`.
  - Backend AI: mobile gui base64 image qua `/api/emotion/analyze`, backend goi Flask.

## 2. Bieu do User Case tong quan

### 2.1 Actors

- Khach (`Guest`)
- Nguoi dung (`User`)
- Quan tri (`Admin`)
- He thong AI (`AI Service`) la external service duoc backend goi

### 2.2 So do use case tong quan

```mermaid
flowchart LR
    Guest[Guest]
    User[User]
    Admin[Admin]

    UC1((Dang ky))
    UC2((Dang nhap))
    UC3((Duyet/Tim bai hat))
    UC4((Phat nhac))
    UC5((Luu lich su nghe))
    UC6((Quan ly playlist))
    UC7((Yeu thich bai hat))
    UC8((Cap nhat ho so))
    UC9((Nhan dien cam xuc va goi y nhac))
    UC10((Quan tri nguoi dung))
    UC11((Quan tri bai hat))
    UC12((Quan tri artist/genre + thong ke))

    Guest --> UC1
    Guest --> UC2

    User --> UC3
    User --> UC4
    User --> UC5
    User --> UC6
    User --> UC7
    User --> UC8
    User --> UC9

    Admin --> UC10
    Admin --> UC11
    Admin --> UC12
```

## 3. Bieu do User Case chi tiet

### Use case chi tiet: Nhan dien cam xuc va goi y nhac

- Actor chinh: `User`
- Muc tieu: Lay danh sach bai hat phu hop theo cam xuc.
- Preconditions:
  - User da dang nhap (co token va userId trong session mobile).
  - Camera duoc cap quyen.

#### Luong chinh

1. User mo `EmotionActivity`.
2. User chup anh khuon mat.
3. He thong thu classify bang `EmotionTFLiteClassifier` tren thiet bi.
4. Neu classify local thanh cong: mobile goi `POST /api/emotion/recommend?userId=...`.
5. Backend map `emotion -> mood`, luu `emotion_logs`, query bai hat theo mood.
6. Backend tra `recommendedSongs`.
7. Mobile mo `EmotionResultActivity` hien thi ket qua va danh sach bai.

#### Luong thay the

- A1: Neu local model loi/khong tai duoc:
  - Mobile gui base64 image qua `POST /api/emotion/analyze?userId=...`.
  - Backend goi Flask `/analyze-emotion`, lay emotion/confidence roi tiep tuc goi y nhac.
- A2: Neu backend/AI loi:
  - Tra message that bai va khong hien thi danh sach goi y.

#### So do use case chi tiet

```mermaid
flowchart TD
    Start([User vao EmotionActivity]) --> Capture[Chup anh]
    Capture --> CheckLocal{Co local TFLite classifier?}

    CheckLocal -->|Yes| LocalInfer[Classify tren thiet bi]
    LocalInfer --> RecommendAPI[POST /api/emotion/recommend]
    RecommendAPI --> SaveLog1[Luu emotion_logs + map mood]
    SaveLog1 --> Songs1[Lay songs theo mood]
    Songs1 --> ShowResult[Mo EmotionResultActivity]

    CheckLocal -->|No| AnalyzeAPI[POST /api/emotion/analyze]
    AnalyzeAPI --> Flask[Backend goi Flask /analyze-emotion]
    Flask --> SaveLog2[Luu emotion_logs + map mood]
    SaveLog2 --> Songs2[Lay songs theo mood]
    Songs2 --> ShowResult
```

## 4. Bieu do lop (Class Diagram)

Luu y: Ma backend hien tai map quan he bang cac truong `*_id` (khong dung object relation JPA truc tiep).

```mermaid
classDiagram
    class User {
      +Long id
      +String username
      +String email
      +String password
      +String fullName
      +String avatarUrl
      +Boolean isActive
      +String role
      +Boolean deleted
      +LocalDateTime createdAt
      +LocalDateTime updatedAt
    }

    class Song {
      +Long id
      +String title
      +String artist <<Transient>>
      +String album
      +String genre <<Transient>>
      +String mood
      +Integer duration
      +String fileUrl
      +String thumbnailUrl
      +String spotifyId
      +String soundcloudId
      +Integer playCount
    }

    class Playlist {
      +Long id
      +Long userId
      +String name
      +String description
      +String coverImageUrl
      +Boolean isPublic
    }

    class PlaylistSong {
      +Long id
      +Long playlistId
      +Long songId
      +Integer position
      +LocalDateTime addedAt
    }

    class Favorite {
      +Long id
      +Long userId
      +Long songId
      +LocalDateTime createdAt
    }

    class ListeningHistory {
      +Long id
      +Long userId
      +Long songId
      +LocalDateTime listenedAt
      +Integer listenDuration
      +Boolean completed
    }

    class EmotionLog {
      +Long id
      +Long userId
      +String emotion
      +BigDecimal confidence
      +String imageUrl
      +LocalDateTime createdAt
    }

    class Artist {
      +Long id
      +String name
    }

    class Genre {
      +Long id
      +String name
    }

    class SongArtist {
      +Long songId
      +Long artistId
    }

    class SongGenre {
      +Long songId
      +Long genreId
    }

    User "1" --> "*" Playlist : userId
    Playlist "1" --> "*" PlaylistSong : playlistId
    Song "1" --> "*" PlaylistSong : songId

    User "1" --> "*" Favorite : userId
    Song "1" --> "*" Favorite : songId

    User "1" --> "*" ListeningHistory : userId
    Song "1" --> "*" ListeningHistory : songId

    User "1" --> "*" EmotionLog : userId

    Song "1" --> "*" SongArtist : songId
    Artist "1" --> "*" SongArtist : artistId

    Song "1" --> "*" SongGenre : songId
    Genre "1" --> "*" SongGenre : genreId
```

## 5. Bieu do tuan tu (Sequence Diagram)

### 5.1 Luong phat nhac + luu lich su

```mermaid
sequenceDiagram
    participant U as User
    participant M as Mobile (Home/Search/Playlist)
    participant P as AudioPlayer (ExoPlayer)
    participant API as Backend API
    participant HC as HistoryController
    participant SR as SongRepository
    participant HR as ListeningHistoryRepository
    participant DB as MySQL

    U->>M: Chon bai hat
    M->>P: play(fileUrl, title)
    P-->>U: Bat dau phat nhac

    M->>API: POST /api/history {songId,userId,username}
    API->>HC: add()
    HC->>SR: findById(songId)
    SR->>DB: SELECT song
    DB-->>SR: song
    HC->>SR: save(playCount + 1)
    SR->>DB: UPDATE songs.play_count
    HC->>HR: save(ListeningHistory)
    HR->>DB: INSERT listening_history
    HC-->>M: {success:true}
```

### 5.2 Luong goi y nhac theo cam xuc (co 2 nhanh)

```mermaid
sequenceDiagram
    participant U as User
    participant EM as EmotionActivity
    participant L as EmotionTFLiteClassifier
    participant API as EmotionController
    participant AI as Flask /analyze-emotion
    participant Repo as SongRepo + EmotionLogRepo
    participant DB as MySQL

    U->>EM: Chup anh

    alt Local model san sang
        EM->>L: classify(bitmap)
        L-->>EM: emotion + confidence
        EM->>API: POST /api/emotion/recommend
        API->>Repo: save EmotionLog
        Repo->>DB: INSERT emotion_logs
        API->>Repo: findByMood(mapEmotionToMood)
        Repo->>DB: SELECT songs by mood
        API-->>EM: recommendedSongs
    else Local model khong san sang/that bai
        EM->>API: POST /api/emotion/analyze (image base64)
        API->>AI: POST /analyze-emotion
        AI-->>API: emotion + confidence
        API->>Repo: save EmotionLog
        Repo->>DB: INSERT emotion_logs
        API->>Repo: findByMood(mapEmotionToMood)
        Repo->>DB: SELECT songs by mood
        API-->>EM: recommendedSongs
    end

    EM-->>U: Hien thi EmotionResultActivity
```

## 6. So do thuc the quan he (ER)

He thong hien tai dung RDBMS (MySQL), vi vay tai lieu dung ER la chinh.

```mermaid
erDiagram
    USERS ||--o{ PLAYLISTS : "user_id"
    USERS ||--o{ FAVORITES : "user_id"
    USERS ||--o{ LISTENING_HISTORY : "user_id"
    USERS ||--o{ EMOTION_LOGS : "user_id"
    USERS ||--o{ JWT_TOKENS : "user_id (optional)"

    SONGS ||--o{ PLAYLIST_SONGS : "song_id"
    PLAYLISTS ||--o{ PLAYLIST_SONGS : "playlist_id"

    SONGS ||--o{ FAVORITES : "song_id"
    SONGS ||--o{ LISTENING_HISTORY : "song_id"

    SONGS ||--o{ SONG_ARTISTS : "song_id"
    ARTISTS ||--o{ SONG_ARTISTS : "artist_id"

    SONGS ||--o{ SONG_GENRES : "song_id"
    GENRES ||--o{ SONG_GENRES : "genre_id"

    USERS {
      bigint id PK
      string username UK
      string email UK
      string password
      string role
      bool is_active
      bool deleted
    }

    SONGS {
      bigint id PK
      string title
      string mood
      int duration
      string file_url
      int play_count
    }

    PLAYLISTS {
      bigint id PK
      bigint user_id FK
      string name
      bool is_public
    }

    PLAYLIST_SONGS {
      bigint id PK
      bigint playlist_id FK
      bigint song_id FK
      int position
    }

    FAVORITES {
      bigint id PK
      bigint user_id FK
      bigint song_id FK
    }

    LISTENING_HISTORY {
      bigint id PK
      bigint user_id FK
      bigint song_id FK
      int listen_duration
      bool completed
    }

    EMOTION_LOGS {
      bigint id PK
      bigint user_id FK
      string emotion
      decimal confidence
      string image_url
    }

    ARTISTS {
      bigint id PK
      string name UK
    }

    GENRES {
      bigint id PK
      string name UK
    }

    SONG_ARTISTS {
      bigint song_id PK,FK
      bigint artist_id PK,FK
    }

    SONG_GENRES {
      bigint song_id PK,FK
      bigint genre_id PK,FK
    }

    JWT_TOKENS {
      bigint id PK
      bigint user_id FK
      string token
      timestamp expires_at
    }
```

### 6.1 Ghi chu ve NoSQL

- Hien tai repository khong su dung NoSQL.
- Neu chuyen sang MongoDB, co the map collection tuong ung:
  - `users`, `songs`, `playlists`, `favorites`, `listening_history`, `emotion_logs`.
  - Quan he many-to-many (`song_artists`, `song_genres`) co the dung array `artistIds` va `genreIds` trong document `songs`.

## 7. Giao dien dap ung chuc nang va luong

### 7.1 Danh sach man hinh va chuc nang

- Auth:
  - `LoginActivity`: dang nhap, luu token/user info vao SharedPreferences.
  - `RegisterActivity`: dang ky va auto dang nhap.
- Main app:
  - `MainActivity`: bottom navigation + mini player.
  - `HomeFragment`: trending, recent, quick mood, start emotion flow.
  - `SearchFragment`: tim kiem, loc theo genre/mood.
  - `PlaylistFragment`: playlist cua toi, favorites, recent.
  - `ProfileFragment`: profile, doi mat khau, vao admin (neu role admin).
- Emotion:
  - `EmotionActivity`: camera + classify + recommend.
  - `EmotionResultActivity`: hien thi ket qua va danh sach bai goi y.
- Admin:
  - `AdminActivity` + `AdminHomeFragment`, `AdminUsersFragment`, `AdminSongsFragment`, `AdminArtistsFragment`, `AdminGenresFragment`.

### 7.2 Luong UI chinh (nguoi dung)

```mermaid
flowchart TD
    A[App Launch] --> B{Co token?}
    B -->|No| C[LoginActivity/RegisterActivity]
    C --> D[MainActivity]
    B -->|Yes| D

    D --> H[HomeFragment]
    D --> S[SearchFragment]
    D --> E[EmotionActivity]
    D --> P[PlaylistFragment]
    D --> R[ProfileFragment]

    H --> Play[AudioPlayer.play + addHistory]
    S --> Play
    P --> Play

    E --> ER[EmotionResultActivity]
    ER --> Play
```

### 7.3 Luong UI admin

```mermaid
flowchart TD
    PF[ProfileFragment] --> Check{role == ROLE_ADMIN?}
    Check -->|No| End1[An nut Admin]
    Check -->|Yes| AD[AdminActivity]

    AD --> AH[AdminHomeFragment]
    AD --> AU[AdminUsersFragment]
    AD --> AS[AdminSongsFragment]
    AD --> AA[AdminArtistsFragment]
    AD --> AG[AdminGenresFragment]

    AH --> STATS[/GET /api/admin/stats/]
    AU --> USERS[/CRUD /api/admin/users/]
    AS --> SONGS[/CRUD /api/admin/songs/]
    AA --> ARTISTS[/GET/POST/DELETE /api/admin/artists/]
    AG --> GENRES[/GET/POST/DELETE /api/admin/genres/]
```

### 7.4 Mapping UI -> API (tom tat)

- `LoginActivity` -> `POST /api/auth/login`
- `RegisterActivity` -> `POST /api/auth/register`
- `HomeFragment` -> `GET /api/songs`, `POST /api/history`, `GET /api/favorites`
- `SearchFragment` -> `GET /api/songs/search`, `GET /api/songs/genre/{genre}`, `GET /api/genres`
- `PlaylistFragment` -> `GET/POST /api/playlists`, `GET /api/playlists/{id}/songs`, `POST/DELETE /api/playlists/{id}/songs/{songId}`
- `ProfileFragment` -> `GET/PATCH /api/me`
- `EmotionActivity` -> `POST /api/emotion/recommend` hoac `POST /api/emotion/analyze`
- `Admin*` -> `/api/admin/**`

## 8. Ket luan

Tai lieu da duoc lam lai theo code hien tai cua du an. Trong pham vi repository nay:

- Kien truc la Mobile Android + Spring Boot API + Flask AI + MySQL.
- Da co day du cac flow user, admin, playback, emotion recommendation.
- ER theo SQL la mo hinh du lieu chinh; NoSQL hien khong duoc su dung trong code.
