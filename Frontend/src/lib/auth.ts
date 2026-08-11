import { useEffect } from 'react';
import { useLocation } from 'wouter';

// Redirects to /login if there's no stored JWT. Call at the top of any
// page that requires a logged-in user (Dashboard, Results, Interview).
export function useRequireAuth() {
  const [, setLocation] = useLocation();

  useEffect(() => {
    if (!localStorage.getItem('token')) {
      setLocation('/login');
    }
  }, [setLocation]);
}

export function getUserId(): number | null {
  const raw = localStorage.getItem('userId');
  return raw ? Number(raw) : null;
}

export function logout(setLocation: (path: string) => void) {
  localStorage.removeItem('token');
  localStorage.removeItem('userId');
  setLocation('/login');
}