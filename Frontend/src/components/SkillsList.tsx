import { CheckCircle, XCircle } from 'lucide-react';
import { motion } from 'framer-motion';

interface SkillsListProps {
  skills: string[];
  variant: 'positive' | 'negative';
}

export function SkillsList({ skills, variant }: SkillsListProps) {
  const Icon = variant === 'positive' ? CheckCircle : XCircle;
  const iconColor = variant === 'positive' ? 'text-chart-3' : 'text-destructive';

  return (
    <div className="space-y-3">
      {skills.map((skill, index) => (
        <motion.div
          key={skill}
          initial={{ opacity: 0, x: -20 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.3, delay: index * 0.05 }}
          className="flex items-center gap-3 group"
          data-testid={`skill-${variant}-${index}`}
        >
          <Icon className={`h-5 w-5 ${iconColor} flex-shrink-0 group-hover:scale-110 transition-transform`} />
          <span className="text-sm font-medium">{skill}</span>
        </motion.div>
      ))}
    </div>
  );
}
