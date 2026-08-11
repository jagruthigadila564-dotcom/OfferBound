import { Link } from 'wouter';
import { motion } from 'framer-motion';
import { ArrowRight, Brain, Target, MessageSquare, Upload, Search, TrendingUp, CheckCircle } from 'lucide-react';
import { Navbar } from '@/components/Navbar';
import { Footer } from '@/components/Footer';
import { FeatureCard } from '@/components/FeatureCard';
import { Button } from '@/components/ui/button';

export default function Landing() {
  return (
    <div className="min-h-[100dvh] bg-background">
      <Navbar />
      
      {/* Hero Section */}
      <section className="relative pt-32 pb-20 px-6 lg:px-8 overflow-hidden">
        {/* Background gradient */}
        <div className="absolute inset-0 bg-gradient-to-br from-primary/10 via-background to-accent/10 pointer-events-none" />
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_30%_20%,rgba(139,92,246,0.1),transparent_50%)] pointer-events-none" />
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_70%_60%,rgba(6,182,212,0.1),transparent_50%)] pointer-events-none" />
        
        <div className="max-w-7xl mx-auto relative">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            {/* Left: Copy */}
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6 }}
              className="text-center lg:text-left"
            >
              <h1 className="text-5xl lg:text-7xl font-bold mb-6 leading-tight">
                Your AI Career{' '}
                <span className="bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">
                  Companion
                </span>
              </h1>
              <p className="text-xl text-muted-foreground mb-8 leading-relaxed max-w-2xl">
                Analyze your resume, match job descriptions, identify skill gaps, and prepare for interviews with AI.
              </p>
              
              <div className="flex flex-col sm:flex-row gap-4 justify-center lg:justify-start">
                <Link href="/register" data-testid="link-hero-get-started">
                  <Button size="lg" className="glow-accent group">
                    Get Started
                    <ArrowRight className="ml-2 h-5 w-5 group-hover:translate-x-1 transition-transform" />
                  </Button>
                </Link>
                <Button
                  size="lg"
                  variant="outline"
                  onClick={() => {
                    const element = document.getElementById('features');
                    element?.scrollIntoView({ behavior: 'smooth' });
                  }}
                  data-testid="button-learn-more"
                >
                  Learn More
                </Button>
              </div>
            </motion.div>

            {/* Right: Floating dashboard mockup */}
            <motion.div
              initial={{ opacity: 0, scale: 0.9 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ duration: 0.7, delay: 0.2 }}
              className="relative"
            >
              <div className="relative p-6 rounded-2xl bg-gradient-to-br from-card to-card/50 border border-border/50 shadow-2xl">
                {/* Mock dashboard card */}
                <div className="space-y-4">
                  <div className="flex items-center justify-between pb-4 border-b border-border/50">
                    <h3 className="font-semibold">Resume Analysis</h3>
                    <span className="text-xs text-muted-foreground">Just now</span>
                  </div>
                  
                  {/* ATS Score display */}
                  <div className="flex items-center gap-4 p-4 rounded-xl bg-primary/5 border border-primary/20">
                    <div className="relative w-20 h-20">
                      <svg className="transform -rotate-90 w-20 h-20">
                        <circle cx="40" cy="40" r="32" stroke="hsl(var(--border))" strokeWidth="6" fill="none" />
                        <circle
                          cx="40"
                          cy="40"
                          r="32"
                          stroke="hsl(var(--accent))"
                          strokeWidth="6"
                          fill="none"
                          strokeLinecap="round"
                          strokeDasharray={`${2 * Math.PI * 32 * 0.82} ${2 * Math.PI * 32}`}
                        />
                      </svg>
                      <div className="absolute inset-0 flex items-center justify-center">
                        <span className="text-xl font-bold text-accent">82%</span>
                      </div>
                    </div>
                    <div>
                      <p className="font-semibold">ATS Score</p>
                      <p className="text-sm text-muted-foreground">Strong match</p>
                    </div>
                  </div>

                  {/* Skills */}
                  <div className="space-y-2">
                    <p className="text-sm font-medium text-muted-foreground">Matched Skills</p>
                    <div className="flex flex-wrap gap-2">
                      <span className="px-3 py-1 rounded-lg bg-chart-3/10 text-chart-3 text-xs font-medium border border-chart-3/20">
                        Python
                      </span>
                      <span className="px-3 py-1 rounded-lg bg-chart-3/10 text-chart-3 text-xs font-medium border border-chart-3/20">
                        React
                      </span>
                      <span className="px-3 py-1 rounded-lg bg-chart-3/10 text-chart-3 text-xs font-medium border border-chart-3/20">
                        Machine Learning
                      </span>
                    </div>
                  </div>

                  {/* Mock interview question preview */}
                  <div className="p-4 rounded-xl bg-accent/5 border border-accent/20">
                    <p className="text-sm font-medium mb-2">Next: Mock Interview</p>
                    <p className="text-xs text-muted-foreground">
                      Tell me about your most impactful ML project...
                    </p>
                  </div>
                </div>

                {/* Glow effect */}
                <div className="absolute -inset-1 bg-gradient-to-br from-primary/20 to-accent/20 rounded-2xl blur-xl -z-10" />
              </div>
            </motion.div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section id="features" className="py-24 px-6 lg:px-8 bg-card/30">
        <div className="max-w-7xl mx-auto">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.6 }}
            className="text-center mb-16"
          >
            <h2 className="text-4xl lg:text-5xl font-bold mb-4">
              Powered by AI. Built for{' '}
              <span className="bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">
                Results
              </span>
            </h2>
            <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
              Everything you need to land your dream job, powered by cutting-edge AI technology.
            </p>
          </motion.div>

          <div className="grid md:grid-cols-3 gap-8">
            <FeatureCard
              icon={Brain}
              title="AI Resume Analysis"
              description="Get instant ATS score, identify strengths and weaknesses, discover missing skills, and receive AI-powered improvement suggestions."
              delay={0}
            />
            <FeatureCard
              icon={Target}
              title="Job Matching"
              description="Compare your resume against any job description. Understand compatibility, skill gaps, and what hiring managers are looking for."
              delay={0.1}
            />
            <FeatureCard
              icon={MessageSquare}
              title="Personalized Mock Interviews"
              description="Practice with AI-generated interviews tailored to your resume and target role. Get real-time feedback on your answers."
              delay={0.2}
            />
          </div>
        </div>
      </section>

      {/* How It Works Section */}
      <section id="how-it-works" className="py-24 px-6 lg:px-8">
        <div className="max-w-7xl mx-auto">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.6 }}
            className="text-center mb-16"
          >
            <h2 className="text-4xl lg:text-5xl font-bold mb-4">
              Land Your Dream Job in{' '}
              <span className="bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">
                Four Steps
              </span>
            </h2>
            <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
              Our AI-powered platform guides you through every stage of your job search.
            </p>
          </motion.div>

          <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-8">
            {[
              { icon: Upload, title: 'Upload Resume', description: 'Upload your resume PDF and let our AI analyze it' },
              { icon: Search, title: 'Add Job Description', description: 'Paste the job description you want to target' },
              { icon: TrendingUp, title: 'Get AI Analysis', description: 'Receive detailed insights, scores, and recommendations' },
              { icon: MessageSquare, title: 'Practice Interview', description: 'Prepare with personalized AI mock interviews' },
            ].map((step, index) => (
              <motion.div
                key={step.title}
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ duration: 0.5, delay: index * 0.1 }}
                className="text-center"
              >
                <div className="relative mb-6 inline-block">
                  <div className="w-16 h-16 rounded-2xl bg-gradient-to-br from-primary to-accent flex items-center justify-center mx-auto">
                    <step.icon className="h-8 w-8 text-white" />
                  </div>
                  <div className="absolute -top-2 -right-2 w-8 h-8 rounded-full bg-accent text-white flex items-center justify-center text-sm font-bold">
                    {index + 1}
                  </div>
                </div>
                <h3 className="text-xl font-bold mb-2">{step.title}</h3>
                <p className="text-muted-foreground">{step.description}</p>
              </motion.div>
            ))}
          </div>
        </div>
      </section>

      {/* About / Social Proof Section */}
      <section id="about" className="py-24 px-6 lg:px-8 bg-card/30">
        <div className="max-w-4xl mx-auto text-center">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.6 }}
          >
            <h2 className="text-4xl lg:text-5xl font-bold mb-6">
              Built by Engineers,{' '}
              <span className="bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">
                For Engineers
              </span>
            </h2>
            <p className="text-lg text-muted-foreground mb-8 leading-relaxed">
              OfferBound was created by a team that understands the modern job search. We've been through it ourselves —
              the ATS black holes, the skill gap anxiety, the interview prep grind. We built the tool we wish we had.
            </p>
            
            <div className="grid md:grid-cols-3 gap-8 mt-12">
              {[
                { label: 'ATS Compatibility', value: '95%' },
                { label: 'Interview Success Rate', value: '87%' },
                { label: 'Skill Gap Identified', value: '100%' },
              ].map((stat) => (
                <div key={stat.label} className="p-6 rounded-xl bg-card border border-border/50">
                  <div className="text-4xl font-bold bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent mb-2">
                    {stat.value}
                  </div>
                  <div className="text-sm text-muted-foreground">{stat.label}</div>
                </div>
              ))}
            </div>
          </motion.div>
        </div>
      </section>

      {/* Final CTA */}
      <section className="py-24 px-6 lg:px-8">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ duration: 0.6 }}
          className="max-w-4xl mx-auto text-center"
        >
          <div className="relative p-12 rounded-3xl bg-gradient-to-br from-primary/10 to-accent/10 border border-primary/20">
            <h2 className="text-4xl lg:text-5xl font-bold mb-6">
              Ready to get hired?
            </h2>
            <p className="text-lg text-muted-foreground mb-8 max-w-2xl mx-auto">
              Join thousands of job seekers who have transformed their career search with AI-powered insights.
            </p>
            <Link href="/register" data-testid="link-cta-get-started">
              <Button size="lg" className="glow-accent group">
                Get Started Free
                <ArrowRight className="ml-2 h-5 w-5 group-hover:translate-x-1 transition-transform" />
              </Button>
            </Link>

            {/* Decorative elements */}
            <div className="absolute top-0 left-0 w-32 h-32 bg-gradient-to-br from-primary/20 to-transparent rounded-full blur-3xl -z-10" />
            <div className="absolute bottom-0 right-0 w-32 h-32 bg-gradient-to-tl from-accent/20 to-transparent rounded-full blur-3xl -z-10" />
          </div>
        </motion.div>
      </section>

      <Footer />
    </div>
  );
}
