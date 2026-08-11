from dotenv import load_dotenv

load_dotenv()

from fastapi import FastAPI
from pydantic import BaseModel
from gemini_service import GeminiService


app = FastAPI(title="OfferBound AI Service")

gemini_service = GeminiService()


class AnalysisRequest(BaseModel):
    resume_text: str
    job_description: str


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