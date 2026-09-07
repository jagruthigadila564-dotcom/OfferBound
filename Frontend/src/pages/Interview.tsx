import { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { ArrowRight, Loader2, CheckCircle } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Textarea } from '@/components/ui/textarea';
import { InterviewCard } from '@/components/InterviewCard';
import { useRequireAuth } from '@/lib/auth';

const questions = [
  "Tell me about your most impactful machine learning project. What was your approach, what challenges did you face, and what was the outcome?",
  "How do you approach model deployment and monitoring in production environments?",
  "Describe a time when you had to optimize a slow-performing ML model. What techniques did you use?",
  "How do you handle class imbalance in your datasets?",
  "What's your experience with containerization and orchestration tools like Docker and Kubernetes?",
];

export default function Interview() {
  useRequireAuth();
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [answer, setAnswer] = useState('');
  const [feedback, setFeedback] = useState<{
    communication: number;
    technical: number;
    suggestions: string[];
  } | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isComplete, setIsComplete] = useState(false);

  const handleSubmit = async () => {
    if (!answer.trim()) return;

    setIsSubmitting(true);
    // Simulate AI feedback
    await new Promise((resolve) => setTimeout(resolve, 1500));
    
    setFeedback({
      communication: 85,
      technical: 78,
      suggestions: [
        'Great structure! Your answer followed the STAR method effectively.',
        'Quantify your results more — mention specific metrics like accuracy improvement or latency reduction.',
        'Consider discussing the business impact of your technical decisions.',
      ],
    });
    setIsSubmitting(false);
  };

  const handleNext = () => {
    if (currentQuestion < questions.length - 1) {
      setCurrentQuestion(currentQuestion + 1);
      setAnswer('');
      setFeedback(null);
    } else {
      setIsComplete(true);
    }
  };

  if (isComplete) {
    return (
      <div className="min-h-[100dvh] bg-gradient-to-br from-background via-primary/5 to-accent/5 flex items-center justify-center px-6">
        <motion.div
          initial={{ opacity: 0, scale: 0.9 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ duration: 0.5 }}
          className="max-w-2xl w-full text-center"
        >
          <div className="relative p-12 rounded-3xl bg-card border border-border/50 shadow-2xl">
            <div className="w-20 h-20 rounded-full bg-gradient-to-br from-chart-3 to-accent flex items-center justify-center mx-auto mb-6">
              <CheckCircle className="h-10 w-10 text-white" />
            </div>
            <h1 className="text-4xl font-bold mb-4">
              Interview <span className="bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">Complete!</span>
            </h1>
            <p className="text-lg text-muted-foreground mb-8">
              You've completed all 5 questions. Great job practicing your interview skills!
            </p>
            
            <div className="grid grid-cols-2 gap-4 mb-8">
              <div className="p-4 rounded-xl bg-primary/5 border border-primary/20">
                <div className="text-3xl font-bold text-primary mb-1">85%</div>
                <div className="text-sm text-muted-foreground">Avg. Communication</div>
              </div>
              <div className="p-4 rounded-xl bg-accent/5 border border-accent/20">
                <div className="text-3xl font-bold text-accent mb-1">78%</div>
                <div className="text-sm text-muted-foreground">Avg. Technical</div>
              </div>
            </div>

            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Button variant="outline" onClick={() => window.location.reload()} data-testid="button-practice-again">
                Practice Again
              </Button>
              <Button className="glow-accent" data-testid="button-back-dashboard">
                Back to Dashboard
              </Button>
            </div>

            <div className="absolute -inset-1 bg-gradient-to-br from-primary/20 to-accent/20 rounded-3xl blur-2xl -z-10" />
          </div>
        </motion.div>
      </div>
    );
  }

  return (
    <div className="min-h-[100dvh] bg-gradient-to-br from-background via-primary/5 to-accent/5">
      {/* Background decorative elements */}
      <div className="absolute inset-0 bg-[radial-gradient(circle_at_30%_20%,rgba(139,92,246,0.1),transparent_50%)] pointer-events-none" />
      <div className="absolute inset-0 bg-[radial-gradient(circle_at_70%_60%,rgba(6,182,212,0.1),transparent_50%)] pointer-events-none" />

      <div className="relative max-w-4xl mx-auto px-6 py-12">
        {/* Header */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          className="text-center mb-8"
        >
          <h1 className="text-3xl lg:text-4xl font-bold mb-2">
            <span className="bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">
              AI/ML Engineer
            </span>{' '}
            Interview
          </h1>
          <p className="text-muted-foreground">Answer each question thoughtfully</p>
        </motion.div>

        {/* Progress */}
        <div className="mb-8">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm font-medium">Progress</span>
            <span className="text-sm text-muted-foreground">
              {currentQuestion + 1} / {questions.length}
            </span>
          </div>
          <div className="h-2 bg-border/50 rounded-full overflow-hidden">
            <motion.div
              className="h-full bg-gradient-to-r from-primary to-accent"
              initial={{ width: 0 }}
              animate={{ width: `${((currentQuestion + 1) / questions.length) * 100}%` }}
              transition={{ duration: 0.5 }}
            />
          </div>
        </div>

        {/* Question Card */}
        <AnimatePresence mode="wait">
          <motion.div
            key={currentQuestion}
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -20 }}
            transition={{ duration: 0.3 }}
            className="mb-8"
          >
            <InterviewCard
              role="AI Interviewer"
              question={questions[currentQuestion]}
              questionNumber={currentQuestion + 1}
              totalQuestions={questions.length}
            />
          </motion.div>
        </AnimatePresence>

        {/* Answer Input */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.2 }}
          className="mb-8"
        >
          <div className="p-6 rounded-2xl bg-card border border-border/50 shadow-lg">
            <label className="block text-sm font-semibold mb-3">Your Answer</label>
            <Textarea
              placeholder="Type your answer here..."
              className="min-h-[200px] resize-none bg-background/50 border-border/50 focus:border-primary/50 transition-colors mb-3"
              value={answer}
              onChange={(e) => setAnswer(e.target.value)}
              disabled={!!feedback}
              data-testid="textarea-answer"
            />
            <div className="flex items-center justify-between">
              <p className="text-xs text-muted-foreground">{answer.length} characters</p>
              {!feedback && (
                <Button
                  onClick={handleSubmit}
                  disabled={!answer.trim() || isSubmitting}
                  data-testid="button-submit-answer"
                >
                  {isSubmitting ? (
                    <>
                      <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                      Analyzing...
                    </>
                  ) : (
                    'Submit Answer'
                  )}
                </Button>
              )}
            </div>
          </div>
        </motion.div>

        {/* Feedback Card */}
        <AnimatePresence>
          {feedback && (
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -20 }}
              transition={{ duration: 0.5 }}
              className="mb-8"
            >
              <div className="p-8 rounded-2xl bg-gradient-to-br from-primary/10 to-accent/10 border border-primary/20 shadow-lg">
                <h3 className="text-xl font-bold mb-6">AI Feedback</h3>
                
                <div className="grid grid-cols-2 gap-4 mb-6">
                  <div className="p-4 rounded-xl bg-card/50 border border-border/30">
                    <div className="text-2xl font-bold text-primary mb-1" data-testid="text-communication-score">
                      {feedback.communication}%
                    </div>
                    <div className="text-sm text-muted-foreground">Communication Score</div>
                  </div>
                  <div className="p-4 rounded-xl bg-card/50 border border-border/30">
                    <div className="text-2xl font-bold text-accent mb-1" data-testid="text-technical-score">
                      {feedback.technical}%
                    </div>
                    <div className="text-sm text-muted-foreground">Technical Score</div>
                  </div>
                </div>

                <div className="space-y-3 mb-6">
                  <p className="text-sm font-semibold">Suggestions:</p>
                  {feedback.suggestions.map((suggestion, idx) => (
                    <p key={idx} className="text-sm flex items-start gap-2" data-testid={`text-suggestion-${idx}`}>
                      <span className="text-accent mt-1">•</span>
                      <span>{suggestion}</span>
                    </p>
                  ))}
                </div>

                <Button
                  onClick={handleNext}
                  className="w-full glow-accent group"
                  data-testid="button-next-question"
                >
                  {currentQuestion < questions.length - 1 ? 'Next Question' : 'Complete Interview'}
                  <ArrowRight className="ml-2 h-4 w-4 group-hover:translate-x-1 transition-transform" />
                </Button>
              </div>
            </motion.div>
          )}
        </AnimatePresence>
      </div>
    </div>
  );
}
