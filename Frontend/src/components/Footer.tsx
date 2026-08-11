import { Zap } from 'lucide-react';

export function Footer() {
  return (
    <footer className="border-t border-border/40 bg-card">
      <div className="max-w-7xl mx-auto px-6 lg:px-8 py-12">
        <div className="flex flex-col md:flex-row items-center justify-between gap-4">
          <div className="flex items-center gap-2">
            <Zap className="h-5 w-5 text-accent" fill="currentColor" />
            <span className="text-lg font-bold bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">
              OfferBound
            </span>
          </div>
          <p className="text-sm text-muted-foreground">
            © {new Date().getFullYear()} OfferBound. Your AI career companion.
          </p>
        </div>
      </div>
    </footer>
  );
}
