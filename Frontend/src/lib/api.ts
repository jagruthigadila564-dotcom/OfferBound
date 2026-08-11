const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8081/api';

function getToken() {
  return localStorage.getItem('token');
}

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const token = getToken();
  const res = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers: {
      ...(options.body && !(options.body instanceof FormData)
        ? { 'Content-Type': 'application/json' }
        : {}),
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...options.headers,
    },
  });

  if (!res.ok) {
    const text = await res.text().catch(() => '');
    throw new Error(text || `Request failed: ${res.status}`);
  }

  const contentType = res.headers.get('content-type');
  return (contentType?.includes('application/json') ? res.json() : res.text()) as Promise<T>;
}

export const api = {
  register: (data: { name: string; email: string; password: string }) =>
    request<string>('/auth/register', { method: 'POST', body: JSON.stringify(data) }),

  login: (data: { email: string; password: string }) =>
    request<{ token: string; message: string; userId: number }>('/auth/login', {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  uploadResume: (userId: number, file: File) => {
    const form = new FormData();
    form.append('userId', String(userId));
    form.append('file', file);
    return request<{ id: number; fileName: string; fileType: string; uploadedAt: string }>(
      '/resume/upload',
      { method: 'POST', body: form }
    );
  },

  getUserResumes: (userId: number) =>
    request<{ id: number; fileName: string; fileType: string; uploadedAt: string }[]>(
      `/resume/user/${userId}`
    ),

  extractResumeText: (resumeId: number) =>
    request<{ resumeId: number; extractedText: string }>(`/resume/${resumeId}/extract`),

  deleteResume: (resumeId: number) =>
    request<string>(`/resume/${resumeId}`, { method: 'DELETE' }),

  // Uses POST /api/analysis/analyze (AIAnalysisController), which returns the
  // richer response including strengths/weaknesses/suggestions — matches what
  // the Results page displays. The backend extracts the resume text itself
  // from the stored PDF, so only resume_id + job_description are needed.
  analyzeResume: (resumeId: number, jobDescription: string) =>
    request<{
      ats_score: number;
      matched_skills: string[];
      missing_skills: string[];
      strengths: string[];
      weaknesses: string[];
      suggestions: string[];
    }>('/analysis/analyze', {
      method: 'POST',
      body: JSON.stringify({ resume_id: resumeId, job_description: jobDescription }),
    }),

  getAnalysisHistory: (resumeId: number) =>
    request<{ id: number; atsScore: number; createdAt: string }[]>(
      `/analysis/history/${resumeId}`
    ),
};