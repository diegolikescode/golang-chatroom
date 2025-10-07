# Project: **ParrotChat** — Minimal, Snappy, Observable WebSocket Chat

## Premise
A lightweight, real-time chatroom where users join temporary “rooms” and exchange messages over WebSockets. It’s small enough to finish in days, yet rich enough to demonstrate clean architecture, context-aware shutdown, testing, and observability.

---

## Functional Requirements
- **Rooms & Session Flow**
  - Create/join public rooms by name (auto-create on first join).
  - Ephemeral guest identity: username chosen on join (validated/unique per room).
  - List active rooms and current member counts.
  - Leave room (explicit or on disconnect).

- **Messaging**
  - Broadcast text messages to everyone in the room.
  - System messages for join/leave, room creation, and room closure.
  - Basic Markdown subset (e.g., `*bold*`, `_italics_`) with safe rendering.

- **Presence & Typing**
  - Presence updates: who’s online, last-seen in room.
  - Optional typing indicator with short TTL (e.g., 5s).

- **History**
  - In-memory rolling history per room (e.g., last 50 messages).
  - On join, server sends the recent history snapshot.

- **Moderation (MVP)**
  - Simple profanity filter (deny + redact list).
  - Kick/ban by room owner (owner = first user in empty room).

- **Client API**
  - REST: health, list rooms, room stats.
  - WebSocket: authenticate (guest name), join/leave, send message, typing event, fetch history.

---

## Non-Functional Requirements
- **Performance**
  - Handle 1K concurrent connections on a modest machine.
  - P99 message broadcast latency < 150ms within a room under moderate load.

- **Reliability**
  - Graceful shutdown: drain connections, flush final messages, persist no state (MVP).
  - Backpressure: prevent slow consumers from stalling room broadcasts.

- **Security**
  - Input validation & length limits (usernames, messages, room names).
  - Rate limiting per connection (messages/min, join attempts).
  - CSRF not applicable to WS, but require an origin allow-list for browser clients.

- **Observability**
  - Structured logs (`slog`) with request IDs and connection IDs.
  - Prometheus metrics: `connected_clients`, `msgs_in`, `msgs_out`, `dropped_msgs`, `room_count`, `broadcast_latency_histogram`.
  - Basic traces around join/broadcast paths (OpenTelemetry optional).

- **Maintainability**
  - Clean architecture (ports/adapters): domain free of `net/http`.
  - Small interfaces at the edges; explicit dependency wiring.
  - Table-driven tests for domain/usecases; integration test for WS handler.

- **Operability**
  - Single binary with env-based config.
  - Dockerfile + Docker Compose example.
  - Makefile/Taskfile for `build/test/run/lint`.

---

## Business Rules
- **Identity & Ownership**
  - Usernames must be unique within a room (case-insensitive).
  - First entrant becomes **room owner**; ownership transfers to the next oldest member on owner disconnect.
  - Owner can **kick** (1h cooldown) or **ban** (ban survives owner handover until room empties).

- **Message Limits**
  - Message length capped (e.g., 512 UTF-8 chars).
  - Burst rate limit (e.g., 5 msgs/2s) and sustained rate (e.g., 60 msgs/min).
  - Messages with blocked terms are rejected with a system notice (not broadcast).

- **Room Lifecycle**
  - Rooms are **ephemeral**: deleted when last member leaves.
  - Room name constraints: `^[a-z0-9-]{3,30}$`; reserved names (e.g., `admin`, `system`) blocked.
  - Max room size (e.g., 100 users). Attempting to exceed returns an error.

- **History & Privacy**
  - Only the last N messages are retained in memory; no disk persistence by default.
  - History is shared with anyone who joins that room; no private messages in MVP.

- **Connection Health**
  - Server sends ping frames (e.g., every 20s); drop connection after 2 missed pongs.
  - Idle connections (no activity for 10m) are closed with a gentle reason.

- **Error Transparency**
  - Client receives structured error events (code, reason) for all denials (rate, profanity, size, auth).
  - System events are always prefixed and non-spoofable.

---

## Nice-to-Have (Stretch Goals)
- JWT auth (optional) to bind a stable user ID.
- Private rooms with invite tokens.
- File/emoji messages via signed URLs (stubbed).
- Pluggable history store (Redis) behind a port interface.
- Minimal web UI (static HTML/JS) for quick demo.
