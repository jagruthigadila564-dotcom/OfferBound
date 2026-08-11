import { useEffect, useState } from 'react';
import { Link, useLocation } from 'wouter';
import { motion } from 'framer-motion';
import { ArrowRight, Lightbulb } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { ScoreCard } from '@/components/ScoreCard';
import { SkillsList } from '@/components/SkillsList';
import { useRequireAuth } from '@/lib/auth';

interface AnalysisResult {
  ats_score: number;
  matched_skills: string[];
  missing_skills: string[];
  strengths: string[];
  weaknesses: string[];
  suggestions: string[];
}

export default function Results() {
  useRequireAuth();
  const [, setLocation] = useLocation();
  const [analysis, setAnalysis] = useState<AnalysisResult | null>(null);

  useEffect(() => {
    const raw = sessionStorage.getItem('lastAnalysis');
    if (!raw) {
      // No analysis in this session — send them back to run one
      setLocation('/dashboard');
      return;
    }
    setAnalysis(JSON.parse(raw));
  }, [setLocation]);

  if (!analysis) return null;

  return (
    <div className="min-h-[100dvh] bg-gradient-to-br from-background via-primary/5 to-accent/5">
      {/* Background decorative elements */}
      <div className="absolute inset-0 bg-[radial-gradient(circle_at_30%_20%,rgba(139,92,246,0.1),transparent_50%)] pointer-events-none" />
      <div className="absolute inset-0 bg-[radial-gradient(circle_at_70%_60%,rgba(6,182,212,0.1),transparent_50%)] pointer-events-none" />

      <div className="relative max-w-6xl mx-auto px-6 py-12">
        {/* Header */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          className="text-center mb-12"
        >
          <h1 className="text-4xl lg:text-5xl font-bold mb-3">
            Analysis <span className="bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">Complete</span>
          </h1>
          <p className="text-lg text-muted-foreground">
            Here's how your resume matches the job description
          </p>
        </motion.div>

        {/* ATS Score - Hero */}
        <motion.div
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ duration: 0.6, delay: 0.2 }}
          className="mb-12 flex justify-center"
        >
          <div className="relative p-12 rounded-3xl bg-card border border-border/50 shadow-2xl">
            <ScoreCard score={analysis.ats_score} label="ATS Score" />
            <div className="absolute -inset-1 bg-gradient-to-br from-primary/20 to-accent/20 rounded-3xl blur-2xl -z-10" />
          </div>
        </motion.div>

        {/* Grid of Cards */}
        <div className="grid md:grid-cols-2 gap-6 mb-12">
          {/* Strengths */}
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.5, delay: 0.3 }}
            className="p-8 rounded-2xl bg-card border border-border/50 shadow-lg"
          >
            <h3 className="text-xl font-bold mb-6 flex items-center gap-2">
              <span className="w-2 h-2 rounded-full bg-chart-3" />
              Strengths
            </h3>
            <SkillsList skills={analysis.strengths} variant="positive" />
          </motion.div>

          {/* Matched Skills */}
          <motion.div
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.5, delay: 0.4 }}
            className="p-8 rounded-2xl bg-card border border-border/50 shadow-lg"
          >
            <h3 className="text-xl font-bold mb-6 flex items-center gap-2">
              <span className="w-2 h-2 rounded-full bg-accent" />
              Matched Skills
            </h3>
            <SkillsList skills={analysis.matched_skills} variant="positive" />
          </motion.div>

          {/* Missing Skills */}
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.5, delay: 0.5 }}
            className="p-8 rounded-2xl bg-card border border-border/50 shadow-lg"
          >
            <h3 className="text-xl font-bold mb-6 flex items-center gap-2">
              <span className="w-2 h-2 rounded-full bg-destructive" />
              Missing Skills
            </h3>
            <SkillsList skills={analysis.missing_skills} variant="negative" />
          </motion.div>

          {/* AI Suggestions */}
          <motion.div
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.5, delay: 0.6 }}
            className="p-8 rounded-2xl bg-gradient-to-br from-primary/10 to-accent/10 border border-primary/20 shadow-lg"
          >
            <div className="flex items-center gap-2 mb-6">
              <Lightbulb className="h-5 w-5 text-accent" />
              <h3 className="text-xl font-bold">AI Suggestions</h3>
            </div>
            <div className="space-y-4 text-sm leading-relaxed">
              {analysis.suggestions.map((suggestion, index) => (
                <p key={index} className="flex items-start gap-2">
                  <span className="text-accent mt-1">•</span>
                  <span>{suggestion}</span>
                </p>
              ))}
            </div>
          </motion.div>
        </div>

        {/* CTA */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.7 }}
          className="text-center"
        >
          <div className="relative inline-block">
            <Link href="/interview" data-testid="link-start-interview">
              <Button size="lg" className="glow-accent group h-14 px-8 text-lg">
                Start Personalized Mock Interview
                <ArrowRight className="ml-2 h-5 w-5 group-hover:translate-x-1 transition-transform" />
              </Button>
            </Link>
            <div className="absolute -inset-2 bg-gradient-to-r from-primary/30 to-accent/30 rounded-xl blur-xl -z-10" />
          </div>
          <p className="text-sm text-muted-foreground mt-4">
            Practice your interview skills with AI-powered questions
          </p>
        </motion.div>
      </div>
    </div>
  );
}