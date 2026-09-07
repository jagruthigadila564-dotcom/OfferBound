// Frontend/src/lib/api.ts

const API_BASE =
  import.meta.env.VITE_API_URL ||
  'http://localhost:8081/api';


function getToken() {
  return localStorage.getItem('token');
}


async function request<T>(
  path: string,
  options: RequestInit = {}
): Promise<T> {

  const token = getToken();


  const res = await fetch(
    `${API_BASE}${path}`,
    {
      ...options,

      headers: {

        ...(options.body &&
        !(options.body instanceof FormData)
          ? {
              'Content-Type':
                'application/json'
            }
          : {}),

        ...(token
          ? {
              Authorization:
                `Bearer ${token}`
            }
          : {}),

        ...options.headers,
      },
    }
  );


  if (!res.ok) {

    const text =
      await res.text().catch(
        () => ''
      );

    throw new Error(
      text ||
      `Request failed: ${res.status}`
    );
  }


  const contentType =
    res.headers.get(
      'content-type'
    );


  return (
    contentType?.includes(
      'application/json'
    )
      ? res.json()
      : res.text()
  ) as Promise<T>;
}


export interface InterviewQA {
  question: string;
  answer: string;
}


export interface InterviewFeedback {

  overall_score: number;

  communication_score: number;

  technical_score: number;

  strengths: string[];

  improvements: string[];

  question_feedback: {
    question: string;
    feedback: string;
  }[];

  summary: string;
}


export interface TailoredExperience {

  title: string;

  company: string;

  duration: string;

  description: string[];
}


export interface TailoredProject {

  name: string;

  technologies: string[];

  description: string[];
}


export interface TailoredEducation {

  degree: string;

  institution: string;

  year: string;

  details: string;
}


export interface TailoredResume {

  professional_summary: string;

  skills: string[];

  experience: TailoredExperience[];

  projects: TailoredProject[];

  education: TailoredEducation[];

  certifications: string[];
}


export const api = {

  register: (
    data: {
      name: string;
      email: string;
      password: string;
    }
  ) =>
    request<string>(
      '/auth/register',
      {
        method: 'POST',
        body: JSON.stringify(data),
      }
    ),


  login: (
    data: {
      email: string;
      password: string;
    }
  ) =>
    request<{
      token: string;
      message: string;
      userId: number;
    }>(
      '/auth/login',
      {
        method: 'POST',
        body: JSON.stringify(data),
      }
    ),


  uploadResume: (
    userId: number,
    file: File
  ) => {

    const form =
      new FormData();

    form.append(
      'userId',
      String(userId)
    );

    form.append(
      'file',
      file
    );


    return request<{
      id: number;
      fileName: string;
      fileType: string;
      uploadedAt: string;
    }>(
      '/resume/upload',
      {
        method: 'POST',
        body: form,
      }
    );
  },


  getUserResumes: (
    userId: number
  ) =>
    request<{
      id: number;
      fileName: string;
      fileType: string;
      uploadedAt: string;
    }[]>(
      `/resume/user/${userId}`
    ),


  extractResumeText: (
    resumeId: number
  ) =>
    request<{
      resumeId: number;
      extractedText: string;
    }>(
      `/resume/${resumeId}/extract`
    ),


  deleteResume: (
    resumeId: number
  ) =>
    request<string>(
      `/resume/${resumeId}`,
      {
        method: 'DELETE'
      }
    ),


  analyzeResume: (
    resumeId: number,
    jobDescription: string
  ) =>
    request<{
      ats_score: number;
      matched_skills: string[];
      missing_skills: string[];
      strengths: string[];
      weaknesses: string[];
      suggestions: string[];
    }>(
      '/analysis/analyze',
      {
        method: 'POST',

        body: JSON.stringify({
          resume_id: resumeId,
          job_description:
            jobDescription,
        }),
      }
    ),


  tailorResume: (
    resumeId: number,
    jobDescription: string
  ) =>
    request<TailoredResume>(
      '/resume/tailor',
      {
        method: 'POST',

        body: JSON.stringify({
          resumeId: resumeId,
          jobDescription:
            jobDescription,
        }),
      }
    ),


  getAnalysisHistory: (
    resumeId: number
  ) =>
    request<{
      id: number;
      atsScore: number;
      createdAt: string;
    }[]>(
      `/analysis/history/${resumeId}`
    ),


  startInterview: (
    resumeId: number,
    jobDescription: string
  ) =>
    request<{
      questions: string[];
    }>(
      '/interview/start',
      {
        method: 'POST',

        body: JSON.stringify({
          resumeId: resumeId,
          jobDescription:
            jobDescription,
        }),
      }
    ),


  getInterviewFeedback: (
    resumeId: number,
    jobDescription: string,
    transcript: InterviewQA[]
  ) =>
    request<InterviewFeedback>(
      '/interview/feedback',
      {
        method: 'POST',

        body: JSON.stringify({
          resumeId: resumeId,
          jobDescription:
            jobDescription,
          transcript: transcript,
        }),
      }
    ),
};