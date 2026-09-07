# ai-service/prompt.py

def create_resume_analysis_prompt(resume_text: str, job_description: str):

    return f"""
You are an expert ATS resume analyzer and career coach.

Analyze the candidate's resume against the given job description.

RESUME:
{resume_text}

JOB DESCRIPTION:
{job_description}

Return ONLY valid JSON in this exact structure:

{{
    "ats_score": 0,
    "matched_skills": [],
    "missing_skills": [],
    "strengths": [],
    "weaknesses": [],
    "suggestions": []
}}

Rules:
- ats_score must be a number from 0 to 100.
- matched_skills should contain skills present in both the resume and job description.
- missing_skills should contain important job requirements missing from the resume.
- strengths should contain specific strengths of the candidate.
- weaknesses should contain specific gaps.
- suggestions should contain actionable resume improvement suggestions.
- Do not return Markdown.
- Do not return explanations outside the JSON.
"""


def create_interview_questions_prompt(
    resume_text: str,
    job_description: str,
    num_questions: int = 5
):

    return f"""
You are an experienced technical interviewer conducting a one-on-one mock interview.

Base your questions on this candidate's resume and the target job description.

RESUME:
{resume_text}

JOB DESCRIPTION:
{job_description}

Return ONLY valid JSON in this exact structure:

{{
    "questions": []
}}

Rules:
- Return exactly {num_questions} questions.
- Each question must be a single string.
- Order them from warm-up to deeper technical/behavioral.
- Reference specific skills, projects, or technologies mentioned in the resume where relevant.
- Do not return Markdown.
- Do not return explanations outside the JSON.
"""


def create_interview_feedback_prompt(
    resume_text: str,
    job_description: str,
    transcript: list
):

    qa_text = "\n\n".join(
        f"Q{i + 1}: {item['question']}\nCandidate's Answer: {item['answer']}"
        for i, item in enumerate(transcript)
    )

    return f"""
You are an experienced technical interviewer who just finished a one-on-one mock interview.

Review the ENTIRE interview transcript as a whole.

RESUME:
{resume_text}

JOB DESCRIPTION:
{job_description}

FULL INTERVIEW TRANSCRIPT:
{qa_text}

Return ONLY valid JSON in this exact structure:

{{
    "overall_score": 0,
    "communication_score": 0,
    "technical_score": 0,
    "strengths": [],
    "improvements": [],
    "question_feedback": [
        {{
            "question": "",
            "feedback": ""
        }}
    ],
    "summary": ""
}}

Rules:
- overall_score, communication_score, technical_score must each be numbers from 0 to 100.
- strengths must contain specific things the candidate did well.
- improvements must contain specific actionable improvements.
- question_feedback must contain one item per question.
- summary must be a short overall verdict.
- Do not return Markdown.
- Do not return explanations outside the JSON.
"""


def create_tailored_resume_prompt(
    resume_text: str,
    job_description: str
):

    return f"""
You are an expert resume writer and ATS optimization specialist.

Create a tailored version of the candidate's resume specifically for the given job description.

IMPORTANT:

- Use ONLY information that already exists in the original resume.
- NEVER invent skills, technologies, projects, work experience,
  education, certifications, achievements, job titles, companies,
  responsibilities, dates, or qualifications.
- Do not add a skill merely because it appears in the job description.
- You may reorganize existing information.
- You may rewrite sentences to make them clearer and more ATS-friendly.
- Prioritize information relevant to the job.
- Use job-description keywords ONLY when the candidate genuinely has
  that skill or experience in the original resume.
- Keep the resume professional and concise.
- Do not mention that the resume was AI-generated.

ORIGINAL RESUME:
{resume_text}

JOB DESCRIPTION:
{job_description}

Return ONLY valid JSON in EXACTLY this structure:

{{
    "professional_summary": "",
    "skills": [],
    "experience": [
        {{
            "title": "",
            "company": "",
            "duration": "",
            "description": []
        }}
    ],
    "projects": [
        {{
            "name": "",
            "technologies": [],
            "description": []
        }}
    ],
    "education": [
        {{
            "degree": "",
            "institution": "",
            "year": "",
            "details": ""
        }}
    ],
    "certifications": []
}}

Rules:

- professional_summary must be 2-4 sentences.
- skills must contain ONLY skills found in the original resume.
- experience must contain ONLY real experience from the original resume.
- projects must contain ONLY projects from the original resume.
- education must contain ONLY education from the original resume.
- certifications must contain ONLY certifications from the original resume.
- Do not invent missing education fields.
- Do not invent dates.
- Improve wording without changing facts.
- Do not return Markdown.
- Do not return explanations outside the JSON.
"""