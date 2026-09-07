import { useState } from 'react';
import { useLocation } from 'wouter';
import { motion } from 'framer-motion';
import { Sparkles, Loader2 } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Textarea } from '@/components/ui/textarea';
import { UploadBox } from '@/components/UploadBox';
import { api } from '@/lib/api';
import { useRequireAuth, getUserId } from '@/lib/auth';

export default function Dashboard() {
  useRequireAuth();

  const [, setLocation] = useLocation();

  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [jobDescription, setJobDescription] = useState('');
  const [isAnalyzing, setIsAnalyzing] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleAnalyze = async () => {
    if (!selectedFile || !jobDescription.trim()) {
      return;
    }

    const userId = getUserId();

    if (!userId) {
      setLocation('/login');
      return;
    }

    setError(null);
    setIsAnalyzing(true);

    try {
      // 1. Upload resume
      const uploaded = await api.uploadResume(
        userId,
        selectedFile
      );

      // 2. Analyze resume against JD
      const analysis = await api.analyzeResume(
        uploaded.id,
        jobDescription.trim()
      );

      // 3. Save analysis result
      sessionStorage.setItem(
        'lastAnalysis',
        JSON.stringify(analysis)
      );

      // 4. Save resume ID
      sessionStorage.setItem(
        'lastResumeId',
        String(uploaded.id)
      );

      // 5. Save EXACT JD used for analysis
      sessionStorage.setItem(
        'lastJobDescription',
        jobDescription.trim()
      );

      // 6. Go to results
      setLocation('/results');

    } catch (err) {
      console.error('Resume analysis failed:', err);

      setError(
        err instanceof Error
          ? err.message
          : 'Something went wrong while analyzing your resume. Please try again.'
      );
    } finally {
      setIsAnalyzing(false);
    }
  };

  const canAnalyze =
    selectedFile !== null &&
    jobDescription.trim().length > 0;

  return (
    <div className="min-h-[100dvh] bg-gradient-to-br from-background via-primary/5 to-accent/5">

      <div className="absolute inset-0 bg-[radial-gradient(circle_at_30%_20%,rgba(139,92,246,0.1),transparent_50%)] pointer-events-none" />

      <div className="absolute inset-0 bg-[radial-gradient(circle_at_70%_60%,rgba(6,182,212,0.1),transparent_50%)] pointer-events-none" />

      <div className="relative max-w-5xl mx-auto px-6 py-12">

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          className="mb-12"
        >
          <h1 className="text-4xl lg:text-5xl font-bold mb-3">
            Welcome,{' '}
            <span className="bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">
              back
            </span>{' '}
            👋
          </h1>

          <p className="text-lg text-muted-foreground">
            Let's improve your career opportunities with AI.
          </p>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{
            duration: 0.5,
            delay: 0.1,
          }}
          className="relative p-8 lg:p-12 rounded-3xl bg-card border border-border/50 shadow-2xl"
        >

          <div className="flex items-center gap-3 mb-8">

            <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-primary to-accent flex items-center justify-center">
              <Sparkles className="h-6 w-6 text-white" />
            </div>

            <h2 className="text-2xl font-bold">
              AI Resume Analyzer
            </h2>

          </div>

          <div className="space-y-8">

            <div>
              <label className="block text-sm font-semibold mb-3">
                Your Resume
              </label>

              <UploadBox
                onFileSelect={setSelectedFile}
                selectedFile={selectedFile}
              />
            </div>

            <div>

              <label className="block text-sm font-semibold mb-3">
                Job Description
              </label>

              <Textarea
                placeholder={`Paste the job description here...

Example:
We're looking for a Senior AI/ML Engineer with expertise in Python, TensorFlow, and production ML systems. You'll design and deploy machine learning models at scale...`}
                className="min-h-[200px] resize-none bg-background/50 border-border/50 focus:border-primary/50 transition-colors"
                value={jobDescription}
                onChange={(e) =>
                  setJobDescription(e.target.value)
                }
                data-testid="textarea-job-description"
              />

              <p className="text-xs text-muted-foreground mt-2">
                {jobDescription.length} characters
              </p>

            </div>

            {error && (
              <div className="rounded-lg border border-destructive/30 bg-destructive/10 p-4">

                <p
                  className="text-sm text-destructive text-center"
                  data-testid="text-analyze-error"
                >
                  {error}
                </p>

              </div>
            )}

            <Button
              onClick={handleAnalyze}
              disabled={!canAnalyze || isAnalyzing}
              className="w-full h-14 text-lg glow-accent"
              data-testid="button-analyze"
            >

              {isAnalyzing ? (
                <>
                  <Loader2 className="mr-2 h-5 w-5 animate-spin" />
                  Analyzing with AI...
                </>
              ) : (
                <>
                  <Sparkles className="mr-2 h-5 w-5" />
                  Analyze Resume
                </>
              )}

            </Button>

            {!canAnalyze && (
              <p className="text-sm text-muted-foreground text-center">
                Upload your resume and add a job description to get started
              </p>
            )}

          </div>

          <div className="absolute -inset-1 bg-gradient-to-br from-primary/10 to-accent/10 rounded-3xl blur-xl -z-10" />

        </motion.div>

      </div>
    </div>
  );
}