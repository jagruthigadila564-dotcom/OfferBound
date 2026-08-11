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