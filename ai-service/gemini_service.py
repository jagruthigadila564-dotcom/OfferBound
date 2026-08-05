import os
import json

import google.generativeai as genai
from dotenv import load_dotenv

from prompt import SYSTEM_PROMPT

load_dotenv()

genai.configure(api_key=os.getenv("GEMINI_API_KEY"))

model = genai.GenerativeModel("gemini-1.5-flash")


def analyze_resume(resume_text, job_description):

    prompt = f"""
{SYSTEM_PROMPT}

Resume:

{resume_text}


Job Description:

{job_description}
"""

    response = model.generate_content(prompt)

    text = response.text.strip()

    if text.startswith("```json"):
        text = text.replace("```json", "").replace("```", "").strip()

    return json.loads(text)
