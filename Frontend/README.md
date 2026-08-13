

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

