import { useState } from 'react';
import { Link } from 'wouter';
import { Menu, X, Zap } from 'lucide-react';
import { Button } from '@/components/ui/button';

export function Navbar() {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  const scrollToSection = (id: string) => {
    const element = document.getElementById(id);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
      setMobileMenuOpen(false);
    }
  };

  return (
    <nav className="fixed top-0 left-0 right-0 z-50 border-b border-border/40 bg-background/80 backdrop-blur-xl">
      <div className="max-w-7xl mx-auto px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <Link href="/" className="flex items-center gap-2 group" data-testid="link-home">
            <div className="relative">
              <Zap className="h-6 w-6 text-accent group-hover:text-accent/80 transition-colors" fill="currentColor" />
              <div className="absolute inset-0 blur-md bg-accent/50 group-hover:bg-accent/70 transition-all" />
            </div>
            <span className="text-xl font-bold bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">
              OfferBound
            </span>
          </Link>

          {/* Desktop Nav */}
          <div className="hidden md:flex items-center gap-8">
            <button
              onClick={() => scrollToSection('features')}
              className="text-sm font-medium text-foreground/70 hover:text-foreground transition-colors"
              data-testid="button-nav-features"
            >
              Features
            </button>
            <button
              onClick={() => scrollToSection('how-it-works')}
              className="text-sm font-medium text-foreground/70 hover:text-foreground transition-colors"
              data-testid="button-nav-how-it-works"
            >
              How it Works
            </button>
            <button
              onClick={() => scrollToSection('about')}
              className="text-sm font-medium text-foreground/70 hover:text-foreground transition-colors"
              data-testid="button-nav-about"
            >
              About
            </button>
            <Link href="/login" data-testid="link-login">
              <Button variant="ghost" size="sm">
                Login
              </Button>
            </Link>
            <Link href="/register" data-testid="link-register">
              <Button size="sm" className="glow-accent">
                Get Started
              </Button>
            </Link>
          </div>

          {/* Mobile Menu Button */}
          <button
            onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
            className="md:hidden p-2 text-foreground"
            data-testid="button-mobile-menu"
          >
            {mobileMenuOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
          </button>
        </div>
      </div>

      {/* Mobile Menu */}
      {mobileMenuOpen && (
        <div className="md:hidden border-t border-border/40 bg-background/95 backdrop-blur-xl">
          <div className="px-6 py-4 space-y-3">
            <button
              onClick={() => scrollToSection('features')}
              className="block w-full text-left text-sm font-medium text-foreground/70 hover:text-foreground transition-colors py-2"
              data-testid="button-mobile-features"
            >
              Features
            </button>
            <button
              onClick={() => scrollToSection('how-it-works')}
              className="block w-full text-left text-sm font-medium text-foreground/70 hover:text-foreground transition-colors py-2"
              data-testid="button-mobile-how-it-works"
            >
              How it Works
            </button>
            <button
              onClick={() => scrollToSection('about')}
              className="block w-full text-left text-sm font-medium text-foreground/70 hover:text-foreground transition-colors py-2"
              data-testid="button-mobile-about"
            >
              About
            </button>
            <Link href="/login" className="block" data-testid="link-mobile-login">
              <Button variant="ghost" size="sm" className="w-full justify-start">
                Login
              </Button>
            </Link>
            <Link href="/register" className="block" data-testid="link-mobile-register">
              <Button size="sm" className="w-full">
                Get Started
              </Button>
            </Link>
          </div>
        </div>
      )}
    </nav>
  );
}
