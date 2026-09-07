# ai-service/gemini_service.py

import os
import json
import time

from google import genai

from prompt import (
    create_resume_analysis_prompt,
    create_tailored_resume_prompt
)


class GeminiService:

    def __init__(self):

        api_key = os.getenv("GEMINI_API_KEY")

        if not api_key:
            raise ValueError("GEMINI_API_KEY is not set")

        self.client = genai.Client(api_key=api_key)

        self.primary_model = "gemini-3.6-flash"
        self.fallback_model = "gemini-3.5-flash"


    def _generate(self, prompt: str):

        models = [
            self.primary_model,
            self.fallback_model
        ]

        last_error = None

        for model in models:

            for attempt in range(3):

                try:

                    print(
                        f"Calling Gemini model: {model} "
                        f"(attempt {attempt + 1}/3)"
                    )

                    response = self.client.models.generate_content(
                        model=model,
                        contents=prompt
                    )

                    if not response.text:
                        raise RuntimeError(
                            "Gemini returned an empty response"
                        )

                    result = response.text.strip()

                    if result.startswith("```"):
                        result = result.replace("```json", "")
                        result = result.replace("```", "")
                        result = result.strip()

                    return json.loads(result)

                except Exception as e:

                    last_error = e

                    print(
                        f"Gemini error using {model}: {e}"
                    )

                    if attempt < 2:

                        wait_time = 2 ** attempt

                        print(
                            f"Retrying in {wait_time} seconds..."
                        )

                        time.sleep(wait_time)

            print(
                f"Model {model} failed. "
                f"Trying fallback model..."
            )

        raise RuntimeError(
            "Gemini service failed after retries: "
            + str(last_error)
        )


    def analyze_resume(
        self,
        resume_text: str,
        job_description: str
    ):

        prompt = create_resume_analysis_prompt(
            resume_text,
            job_description
        )

        return self._generate(prompt)


    def tailor_resume(
        self,
        resume_text: str,
        job_description: str
    ):

        prompt = create_tailored_resume_prompt(
            resume_text,
            job_description
        )

        return self._generate(prompt)