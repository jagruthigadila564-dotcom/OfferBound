// Results.tsx
import { useEffect, useState } from 'react';
import { useLocation } from 'wouter';
import { motion } from 'framer-motion';
import {
  ArrowLeft,
  CheckCircle2,
  XCircle,
  Lightbulb,
  TrendingUp,
  Loader2,
  FileText,
} from 'lucide-react';

import { Button } from '@/components/ui/button';
import { api } from '@/lib/api';

interface Analysis {
  ats_score: number;
  matched_skills: string[];
  missing_skills: string[];
  strengths: string[];
  weaknesses: string[];
  suggestions: string[];
}

export default function Results() {

  const [, setLocation] = useLocation();

  const [analysis, setAnalysis] =
    useState<Analysis | null>(null);

  const [isTailoring, setIsTailoring] =
    useState(false);

  useEffect(() => {

    const storedAnalysis =
      sessionStorage.getItem('lastAnalysis');

    if (!storedAnalysis) {
      setLocation('/dashboard');
      return;
    }

    try {

      setAnalysis(
        JSON.parse(storedAnalysis)
      );

    } catch (error) {

      console.error(
        'Failed to parse analysis:',
        error
      );

      setLocation('/dashboard');
    }

  }, [setLocation]);

  const handleTailorResume = async () => {

    const token =
      localStorage.getItem('token');

    const resumeId =
      sessionStorage.getItem('lastResumeId');

    const jobDescription =
      sessionStorage.getItem(
        'lastJobDescription'
      );

    if (!token) {

      alert(
        'Your login session has expired. Please login again.'
      );

      setLocation('/login');
      return;
    }

    if (!resumeId) {

      alert(
        'Resume ID is missing. Please analyze your resume again.'
      );

      setLocation('/dashboard');
      return;
    }

    if (!jobDescription) {

      alert(
        'Job description is missing. Please analyze your resume again.'
      );

      setLocation('/dashboard');
      return;
    }

    setIsTailoring(true);

    try {

      console.log(
        'Tailoring resume...',
        {
          resumeId,
          jobDescriptionLength:
            jobDescription.length
        }
      );

      const tailoredResume =
        await api.tailorResume(
          Number(resumeId),
          jobDescription
        );

      console.log(
        'Tailored resume received:',
        tailoredResume
      );

      sessionStorage.setItem(
        'tailoredResume',
        JSON.stringify(tailoredResume)
      );

      setLocation(
        '/tailored-resume'
      );

    } catch (error) {

      console.error(
        'Tailoring failed:',
        error
      );

      let message =
        'Failed to tailor your resume. Please try again.';

      if (error instanceof Error) {
        message = error.message;
      }

      alert(message);

    } finally {

      setIsTailoring(false);
    }
  };

  if (!analysis) {

    return (
      <div className="min-h-[100dvh] flex items-center justify-center">

        <Loader2
          className="h-8 w-8 animate-spin"
        />

      </div>
    );
  }

  return (
    <div className="min-h-[100dvh] bg-gradient-to-br from-background via-primary/5 to-accent/5">

      <div className="max-w-6xl mx-auto px-6 py-10">

        <Button
          variant="ghost"
          onClick={() =>
            setLocation('/dashboard')
          }
          className="mb-8"
        >

          <ArrowLeft className="mr-2 h-4 w-4" />

          Back to Dashboard

        </Button>

        <motion.div
          initial={{
            opacity: 0,
            y: 20
          }}
          animate={{
            opacity: 1,
            y: 0
          }}
          className="mb-10"
        >

          <h1 className="text-4xl font-bold mb-3">
            Resume Analysis
          </h1>

          <p className="text-muted-foreground text-lg">
            Here's how well your resume matches the job description.
          </p>

        </motion.div>

        <motion.div
          initial={{
            opacity: 0,
            y: 20
          }}
          animate={{
            opacity: 1,
            y: 0
          }}
          transition={{
            delay: 0.1
          }}
          className="bg-card border border-border/50 rounded-3xl p-8 mb-8 shadow-xl"
        >

          <div className="text-center">

            <p className="text-sm font-semibold text-muted-foreground uppercase tracking-wide mb-3">
              ATS Compatibility Score
            </p>

            <div className="text-7xl font-bold bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">
              {analysis.ats_score}
            </div>

            <p className="text-muted-foreground mt-2">
              out of 100
            </p>

          </div>

        </motion.div>

        <div className="grid md:grid-cols-2 gap-6 mb-8">

          <motion.div
            initial={{
              opacity: 0,
              y: 20
            }}
            animate={{
              opacity: 1,
              y: 0
            }}
            transition={{
              delay: 0.2
            }}
            className="bg-card border border-border/50 rounded-3xl p-6 shadow-xl"
          >

            <div className="flex items-center gap-3 mb-5">

              <CheckCircle2 className="h-6 w-6 text-green-500" />

              <h2 className="text-xl font-bold">
                Matched Skills
              </h2>

            </div>

            <div className="flex flex-wrap gap-2">

              {analysis.matched_skills.length > 0 ? (

                analysis.matched_skills.map(
                  (skill, index) => (

                    <span
                      key={index}
                      className="px-3 py-2 rounded-lg bg-green-500/10 text-green-600 text-sm font-medium"
                    >
                      {skill}
                    </span>

                  )
                )

              ) : (

                <p className="text-muted-foreground">
                  No matched skills found.
                </p>

              )}

            </div>

          </motion.div>

          <motion.div
            initial={{
              opacity: 0,
              y: 20
            }}
            animate={{
              opacity: 1,
              y: 0
            }}
            transition={{
              delay: 0.3
            }}
            className="bg-card border border-border/50 rounded-3xl p-6 shadow-xl"
          >

            <div className="flex items-center gap-3 mb-5">

              <XCircle className="h-6 w-6 text-red-500" />

              <h2 className="text-xl font-bold">
                Missing Skills
              </h2>

            </div>

            <div className="flex flex-wrap gap-2">

              {analysis.missing_skills.length > 0 ? (

                analysis.missing_skills.map(
                  (skill, index) => (

                    <span
                      key={index}
                      className="px-3 py-2 rounded-lg bg-red-500/10 text-red-600 text-sm font-medium"
                    >
                      {skill}
                    </span>

                  )
                )

              ) : (

                <p className="text-muted-foreground">
                  No major missing skills found.
                </p>

              )}

            </div>

          </motion.div>

        </div>

        <div className="grid md:grid-cols-2 gap-6 mb-8">

          <motion.div
            initial={{
              opacity: 0,
              y: 20
            }}
            animate={{
              opacity: 1,
              y: 0
            }}
            transition={{
              delay: 0.4
            }}
            className="bg-card border border-border/50 rounded-3xl p-6 shadow-xl"
          >

            <div className="flex items-center gap-3 mb-5">

              <TrendingUp className="h-6 w-6 text-green-500" />

              <h2 className="text-xl font-bold">
                Strengths
              </h2>

            </div>

            <ul className="space-y-3">

              {analysis.strengths.map(
                (strength, index) => (

                  <li
                    key={index}
                    className="flex gap-3 text-sm"
                  >

                    <CheckCircle2 className="h-5 w-5 text-green-500 shrink-0 mt-0.5" />

                    <span>
                      {strength}
                    </span>

                  </li>

                )
              )}

            </ul>

          </motion.div>

          <motion.div
            initial={{
              opacity: 0,
              y: 20
            }}
            animate={{
              opacity: 1,
              y: 0
            }}
            transition={{
              delay: 0.5
            }}
            className="bg-card border border-border/50 rounded-3xl p-6 shadow-xl"
          >

            <div className="flex items-center gap-3 mb-5">

              <XCircle className="h-6 w-6 text-orange-500" />

              <h2 className="text-xl font-bold">
                Weaknesses
              </h2>

            </div>

            <ul className="space-y-3">

              {analysis.weaknesses.map(
                (weakness, index) => (

                  <li
                    key={index}
                    className="flex gap-3 text-sm"
                  >

                    <XCircle className="h-5 w-5 text-orange-500 shrink-0 mt-0.5" />

                    <span>
                      {weakness}
                    </span>

                  </li>

                )
              )}

            </ul>

          </motion.div>

        </div>

        <motion.div
          initial={{
            opacity: 0,
            y: 20
          }}
          animate={{
            opacity: 1,
            y: 0
          }}
          transition={{
            delay: 0.6
          }}
          className="bg-card border border-border/50 rounded-3xl p-6 shadow-xl mb-10"
        >

          <div className="flex items-center gap-3 mb-5">

            <Lightbulb className="h-6 w-6 text-yellow-500" />

            <h2 className="text-xl font-bold">
              Resume Improvement Suggestions
            </h2>

          </div>

          <ul className="space-y-4">

            {analysis.suggestions.map(
              (suggestion, index) => (

                <li
                  key={index}
                  className="flex gap-3"
                >

                  <span className="flex items-center justify-center w-7 h-7 rounded-full bg-primary/10 text-primary text-sm font-bold shrink-0">
                    {index + 1}
                  </span>

                  <span className="text-sm leading-relaxed pt-1">
                    {suggestion}
                  </span>

                </li>

              )
            )}

          </ul>

        </motion.div>

        <motion.div
          initial={{
            opacity: 0,
            y: 20
          }}
          animate={{
            opacity: 1,
            y: 0
          }}
          transition={{
            delay: 0.7
          }}
          className="text-center"
        >

          <Button
            size="lg"
            variant="outline"
            onClick={handleTailorResume}
            disabled={isTailoring}
            className="h-14 px-8 text-lg"
          >

            {isTailoring ? (

              <>

                <Loader2
                  className="mr-2 h-5 w-5 animate-spin"
                />

                Tailoring Resume...

              </>

            ) : (

              <>

                <FileText
                  className="mr-2 h-5 w-5"
                />

                Tailor My Resume for This Job

              </>

            )}

          </Button>

          <p className="text-sm text-muted-foreground mt-3">
            AI will optimize your resume using only information already present in it.
          </p>

        </motion.div>

      </div>

    </div>
  );
}