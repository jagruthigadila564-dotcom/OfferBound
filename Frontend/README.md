# OfferBound — Frontend

Standalone React + TypeScript + Vite frontend for OfferBound. Extracted from
the original Replit project, cleaned of all backend, database, and
Replit-specific tooling. This is UI-only — all "AI analysis", login,
registration, and interview feedback flows use mock/simulated data and are
ready to be wired up to a real backend.

## Stack

- React 19 + TypeScript
- Vite 7
- Tailwind CSS v4
- Radix UI primitives (via shadcn-style `components/ui`)
- Framer Motion
- Wouter (routing)
- React Hook Form + Zod

## Getting started

```bash
npm install
npm run dev
```

The app runs at `http://localhost:5173`.

## Build

```bash
npm run build
npm run serve   # preview the production build
```

## Routes

- `/` — Landing
- `/login` — Login (mock auth)
- `/register` — Register (mock auth)
- `/dashboard` — Dashboard (mock analysis)
- `/results` — Results (mock scoring)
- `/interview` — Interview (mock AI feedback)
- any other path — 404

## Project structure

```
src/
├── components/       # Shared UI components
│   └── ui/            # Radix-based primitives (shadcn style)
├── pages/            # Route-level pages
├── hooks/            # Shared hooks
├── lib/              # Utilities (cn, etc.)
├── App.tsx           # Router + providers
├── main.tsx          # Entry point
└── index.css         # Design system (colors, typography, etc.)
```

## Connecting a backend

This project intentionally contains **no backend, API client, or database
code**. Pages currently simulate network calls with `setTimeout`/mock data
(e.g. in `Dashboard.tsx`, `Login.tsx`, `Interview.tsx`). Replace those mock
sections with real API calls when you're ready to connect your backend —
the UI and navigation flow will keep working unchanged in the meantime.
