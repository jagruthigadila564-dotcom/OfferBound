from dotenv import load_dotenv

load_dotenv()

from fastapi import FastAPI
from pydantic import BaseModel, Field
from typing import Optional
from gemini_service import GeminiService


app = FastAPI(title="OfferBound AI Service")

gemini_service = GeminiService()


class AnalysisRequest(BaseModel):
    resume_text: str = Field(..., alias="resumeText")
    job_description: str = Field(..., alias="jobDescription")
    resume_id: Optional[int] = Field(None, alias="resumeId")

    # Use pydantic v2 style config to allow population by field name
    model_config = {"populate_by_name": True}


@app.get("/")
def home():
    return {
        "message": "OfferBound AI Service is running"
    }


@app.post("/analyze")
def analyze_resume(request: AnalysisRequest):
    result = gemini_service.analyze_resume(
        request.resume_text,
        request.job_description
    )

    return result