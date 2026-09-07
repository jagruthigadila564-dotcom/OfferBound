# ai-service/app.py

from dotenv import load_dotenv

load_dotenv()

from fastapi import FastAPI
from pydantic import BaseModel, Field
from typing import Optional

from gemini_service import GeminiService


app = FastAPI(
    title="OfferBound AI Service"
)

gemini_service = GeminiService()


class AnalysisRequest(BaseModel):

    resume_text: str = Field(
        ...,
        alias="resumeText"
    )

    job_description: str = Field(
        ...,
        alias="jobDescription"
    )

    resume_id: Optional[int] = Field(
        None,
        alias="resumeId"
    )

    model_config = {
        "populate_by_name": True
    }


@app.get("/")
def home():

    return {
        "message": "OfferBound AI Service is running"
    }


@app.post("/analyze")
def analyze_resume(
    request: AnalysisRequest
):

    return gemini_service.analyze_resume(
        request.resume_text,
        request.job_description
    )


class TailorResumeRequest(BaseModel):

    resume_text: str = Field(
        ...,
        alias="resumeText"
    )

    job_description: str = Field(
        ...,
        alias="jobDescription"
    )

    model_config = {
        "populate_by_name": True
    }


@app.post("/tailor-resume")
def tailor_resume(
    request: TailorResumeRequest
):

    return gemini_service.tailor_resume(
        request.resume_text,
        request.job_description
    )