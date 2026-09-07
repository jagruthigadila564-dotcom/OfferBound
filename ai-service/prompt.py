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


def create_tailored_resume_prompt(
    resume_text: str,
    job_description: str
):

    return f"""
You are an expert resume writer and ATS optimization specialist.

Create a tailored version of the candidate's resume specifically for the
given job description.

IMPORTANT:

- Use ONLY information that already exists in the original resume.
- NEVER invent skills, technologies, projects, work experience,
  education, certifications, achievements, job titles, or responsibilities.
- Do not add a skill merely because it appears in the job description.
- You may reorganize existing information.
- You may rewrite sentences to make them clearer and more ATS-friendly.
- Prioritize experience, projects, and skills that are relevant to the job.
- Naturally include relevant keywords from the job description ONLY when
  the candidate genuinely has that skill or experience in the original resume.
- Keep the resume professional and concise.
- Do not mention that the resume was AI-generated.
- Do not include explanations or comments.

ORIGINAL RESUME:
{resume_text}

JOB DESCRIPTION:
{job_description}

Return ONLY valid JSON in exactly this structure:

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
- skills must contain only skills found in the original resume.
- experience must contain only real experience from the original resume.
- projects must contain only projects from the original resume.
- education must contain only education from the original resume.
- certifications must contain only certifications from the original resume.
- Improve wording and relevance without changing facts.
- Do not return Markdown.
- Do not return explanations outside the JSON.
"""


def create_interview_questions_prompt(
    resume_text: str,
    job_description: str,
    num_questions: int = 5
):

    return f"""
You are an experienced technical interviewer conducting a personalized
one-on-one mock interview.

Your job is to interview THIS candidate.

You MUST generate questions using the candidate's ACTUAL resume and the
ACTUAL target job description provided below.

CANDIDATE RESUME:
{resume_text}

TARGET JOB DESCRIPTION:
{job_description}

IMPORTANT PERSONALIZATION RULES:

1. Every question must be connected to something actually present in the
   candidate's resume or job description.

2. You may ask about:
   - Projects listed in the resume
   - Technologies listed in the resume
   - Skills listed in the resume
   - Education listed in the resume
   - Experience listed in the resume
   - Certifications listed in the resume
   - Responsibilities or achievements actually mentioned in the resume
   - Requirements from the job description that relate to the candidate's
     actual background

3. NEVER invent a project, technology, company, internship, certification,
   achievement, responsibility, or experience.

4. Do NOT ask generic questions such as:
   "Tell me about yourself"
   unless the question is specifically connected to information in the resume.

5. At least some questions should mention a SPECIFIC project, technology,
   skill, or experience from the resume.

6. Questions should become progressively more difficult.

7. Include a mixture of:
   - Resume/project questions
   - Technical questions related to the candidate's actual skills
   - Problem-solving questions related to their projects
   - Behavioral questions connected to their actual experience

8. The questions should sound like a real interviewer speaking to the
   candidate.

9. Do not provide answers.

Return ONLY valid JSON:

{{
    "questions": []
}}

Rules:

- Return exactly {num_questions} questions.
- Each question must be a single string.
- Do not return Markdown.
- Do not return explanations outside the JSON.
"""


def create_interview_feedback_prompt(
    resume_text: str,
    job_description: str,
    transcript: list
):

    qa_text = "\n\n".join(
        f"Q{i + 1}: {item['question']}\n"
        f"Candidate's Answer: {item['answer']}"
        for i, item in enumerate(transcript)
    )

    return f"""
You are an experienced technical interviewer evaluating a REAL mock interview.

Evaluate the candidate based ONLY on their actual answers in the transcript.

CANDIDATE RESUME:
{resume_text}

TARGET JOB DESCRIPTION:
{job_description}

FULL INTERVIEW TRANSCRIPT:
{qa_text}

IMPORTANT:

- Evaluate what the candidate actually said.
- Do NOT assume the candidate knows something they did not demonstrate.
- Do NOT give high scores simply because a technology appears in the resume.
- Compare the candidate's answers with their resume and the job description.
- Check whether the candidate's answers are technically correct.
- Identify whether the candidate actually understands the projects and
  technologies they claim on the resume.
- Give specific feedback based on their actual answers.
- Do not invent facts about the candidate.

Return ONLY valid JSON:

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

- overall_score must be from 0 to 100.
- communication_score must be from 0 to 100.
- technical_score must be from 0 to 100.
- strengths must be based on the actual answers.
- improvements must be specific and actionable.
- question_feedback must contain exactly one item for every question.
- Keep question_feedback in the same order as the transcript.
- summary must be 2-4 sentences.
- Do not return Markdown.
- Do not return explanations outside the JSON.
"""