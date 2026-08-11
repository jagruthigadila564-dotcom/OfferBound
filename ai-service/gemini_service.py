import os
import json
from google import genai

from prompt import create_resume_analysis_prompt


class GeminiService:

    def __init__(self):
        api_key = os.getenv("GEMINI_API_KEY")

        if not api_key:
            raise ValueError("GEMINI_API_KEY is not set")

        self.client = genai.Client(api_key=api_key)

    def analyze_resume(self, resume_text: str, job_description: str):

        prompt = create_resume_analysis_prompt(
            resume_text,
            job_description
        )

        response = self.client.models.generate_content(
            model="gemini-3.6-flash",
            contents=prompt
        )

        result = response.text.strip()

        # Remove markdown code fences if Gemini adds them
        if result.startswith("```"):
            result = result.replace("```json", "")
            result = result.replace("```", "")
            result = result.strip()

        return json.loads(result)
