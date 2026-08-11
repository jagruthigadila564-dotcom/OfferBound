import { Bot } from 'lucide-react';
import { motion } from 'framer-motion';

interface InterviewCardProps {
  role: string;
  question: string;
  questionNumber: number;
  totalQuestions: number;
}

export function InterviewCard({ role, question, questionNumber, totalQuestions }: InterviewCardProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
      className="p-8 rounded-2xl bg-gradient-to-br from-card to-card/50 border border-border/50"
    >
      <div className="flex items-start gap-4">
        <div className="w-16 h-16 rounded-2xl bg-gradient-to-br from-primary to-accent flex items-center justify-center flex-shrink-0">
          <Bot className="h-8 w-8 text-white" />
        </div>
        
        <div className="flex-1">
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-lg font-semibold" data-testid="text-role">{role}</h3>
            <span className="text-sm text-muted-foreground" data-testid="text-question-number">
              Question {questionNumber} of {totalQuestions}
            </span>
          </div>
          
          <p className="text-foreground/90 leading-relaxed" data-testid="text-question">
            {question}
          </p>
        </div>
      </div>
    </motion.div>
  );
}
