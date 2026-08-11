import { useEffect, useState } from 'react';
import { motion } from 'framer-motion';

interface ScoreCardProps {
  score: number;
  label: string;
}

export function ScoreCard({ score, label }: ScoreCardProps) {
  const [displayScore, setDisplayScore] = useState(0);

  useEffect(() => {
    let interval: number | null = null;
    const timer = window.setTimeout(() => {
      interval = window.setInterval(() => {
        setDisplayScore((prev) => {
          if (prev === score) {
            if (interval !== null) {
              window.clearInterval(interval);
            }
            return score;
          }

          const next = prev < score ? prev + 1 : prev - 1;
          return next;
        });
      }, 20);
    }, 200);

    return () => {
      window.clearTimeout(timer);
      if (interval !== null) {
        window.clearInterval(interval);
      }
    };
  }, [score]);

  const circumference = 2 * Math.PI * 80;
  const offset = circumference - (displayScore / 100) * circumference;

  return (
    <motion.div
      initial={{ opacity: 0, scale: 0.9 }}
      animate={{ opacity: 1, scale: 1 }}
      transition={{ duration: 0.5 }}
      className="flex flex-col items-center"
    >
      <div className="relative w-48 h-48">
        <svg className="transform -rotate-90 w-48 h-48">
          <circle
            cx="96"
            cy="96"
            r="80"
            stroke="hsl(var(--border))"
            strokeWidth="12"
            fill="none"
          />
          <motion.circle
            cx="96"
            cy="96"
            r="80"
            stroke="url(#gradient)"
            strokeWidth="12"
            fill="none"
            strokeLinecap="round"
            initial={{ strokeDashoffset: circumference }}
            animate={{ strokeDashoffset: offset }}
            transition={{ duration: 1.5, ease: 'easeOut' }}
            style={{
              strokeDasharray: circumference,
            }}
          />
          <defs>
            <linearGradient id="gradient" x1="0%" y1="0%" x2="100%" y2="100%">
              <stop offset="0%" stopColor="hsl(var(--primary))" />
              <stop offset="100%" stopColor="hsl(var(--accent))" />
            </linearGradient>
          </defs>
        </svg>
        <div className="absolute inset-0 flex flex-col items-center justify-center">
          <span className="text-5xl font-bold bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent" data-testid="text-score">
            {displayScore}%
          </span>
        </div>
      </div>
      <p className="mt-4 text-lg font-semibold text-muted-foreground">{label}</p>
    </motion.div>
  );
}
