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


# =========================================================
# HOME
# =========================================================

@app.get("/")
def home():

    return {
        "message": "OfferBound AI Service is running"
    }


# =========================================================
# RESUME ANALYSIS
# =========================================================

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


@app.post("/analyze")
def analyze_resume(
    request: AnalysisRequest
):

    result = gemini_service.analyze_resume(
        request.resume_text,
        request.job_description
    )

    return result


# =========================================================
# TAILOR RESUME
# =========================================================

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

    result = gemini_service.tailor_resume(
        request.resume_text,
        request.job_description
    )

    return result


# =========================================================
# MOCK INTERVIEW - QUESTIONS
# =========================================================

class InterviewQuestionsRequest(BaseModel):

    resume_text: str = Field(
        ...,
        alias="resumeText"
    )

    job_description: str = Field(
        ...,
        alias="jobDescription"
    )

    num_questions: int = Field(
        5,
        alias="numQuestions"
    )

    model_config = {
        "populate_by_name": True
    }


@app.post("/interview/questions")
def generate_interview_questions(
    request: InterviewQuestionsRequest
):

    if request.num_questions < 1:
        request.num_questions = 5

    if request.num_questions > 10:
        request.num_questions = 10

    return gemini_service.generate_interview_questions(
        request.resume_text,
        request.job_description,
        request.num_questions
    )


# =========================================================
# MOCK INTERVIEW - FEEDBACK
# =========================================================

class InterviewFeedbackRequest(BaseModel):

    resume_text: str = Field(
        ...,
        alias="resumeText"
    )

    job_description: str = Field(
        ...,
        alias="jobDescription"
    )

    transcript: list = Field(
        ...
    )

    model_config = {
        "populate_by_name": True
    }


@app.post("/interview/feedback")
def interview_feedback(
    request: InterviewFeedbackRequest
):

    if not request.transcript:
        return {
            "overall_score": 0,
            "communication_score": 0,
            "technical_score": 0,
            "strengths": [],
            "improvements": [
                "No interview answers were provided."
            ],
            "question_feedback": [],
            "summary": "No interview was completed."
        }

    return gemini_service.generate_interview_feedback(
        request.resume_text,
        request.job_description,
        request.transcript
    )