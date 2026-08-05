from fastapi import FastAPI
from pydantic import BaseModel
from gemini_service import analyze_resume

app = FastAPI(title="OfferBound AI Service")


class AnalysisRequest(BaseModel):
    resumeText: str
    jobDescription: str


@app.get("/")
def home():
    return {
        "message": "OfferBound AI Service Running"
    }


@app.post("/analyze")
def analyze(request: AnalysisRequest):

    result = analyze_resume(
        request.resumeText,
        request.jobDescription
    )

    return result