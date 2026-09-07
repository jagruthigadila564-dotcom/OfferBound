# OfferBound

## AI-Powered Resume Analyzer & Personalized Mock Interview Platform

OfferBound is an AI-powered career preparation platform designed to help job seekers improve their resumes and prepare for interviews.

Users can upload their resume and provide a job description. OfferBound analyzes the resume using AI techniques and provides:

- Resume-job description matching
- ATS compatibility score
- Matched skills
- Missing skills
- Resume improvement suggestions
- Tailored resume PDF export
- Personalized mock interview questions

The goal of OfferBound is to help students and job seekers understand their strengths, identify skill gaps, and prepare effectively for real-world job opportunities.

---

# Problem Statement

Many students struggle with:

- Creating ATS-friendly resumes
- Understanding why their resume gets rejected
- Identifying missing skills for a role
- Preparing for interviews based on job requirements

OfferBound solves this by providing personalized resume analysis and interview preparation.

## System Architecture

```text
                    User
                     │
                     ▼
              React Frontend
                     │
                     ▼
             Spring Boot Backend
                │          │
                │          ▼
                │        MySQL
                │
                ▼
           FastAPI AI Service
                     │
                     ▼
                Gemini AI
```

### Technology Stack

* **Frontend:** React.js, TypeScript, Vite, Tailwind CSS
* **Backend:** Java, Spring Boot, Spring Security, JWT
* **Database:** MySQL
* **AI Service:** Python, FastAPI, Gemini
* **Resume Processing:** Apache PDFBox

---

## Features

* 📄 Resume upload and parsing
* 🎯 Resume–Job Description matching
* 📊 ATS compatibility score
* ✅ Matched skills identification
* ❌ Missing skills identification
* 💡 AI-powered resume improvement suggestions
* 📝 Tailored resume generation
* 📥 Tailored resume PDF export
* 🎤 Personalized mock interview questions
* 🔐 JWT-based authentication

---

## Setup

### 1. Clone Repository

```bash
git clone https://github.com/jagruthigadila564-dotcom/OfferBound.git
cd OfferBound
```

### 2. Backend

```bash
cd backend
mvn spring-boot:run
```

Configure MySQL and required environment variables.

### 3. AI Service

```bash
cd ai-service
python -m venv venv
venv\Scripts\activate
pip install -r requirements.txt
uvicorn app:app --reload
```

Configure the Gemini API key as an environment variable.

### 4. Frontend

```bash
cd frontend
npm install
npm run dev
```

---

## Future Enhancements

* 🎤 Voice-based AI mock interviews
* 📈 Interview performance analysis
* 🎓 Personalized skill-learning roadmaps
* 💼 Job recommendation system
* 🔗 LinkedIn profile analysis
* 📋 Job application tracking
* 📄 Multiple professional resume templates

---

## Project Status
Currently under active development.....
