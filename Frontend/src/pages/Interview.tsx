import { useEffect, useState } from 'react';
import { useLocation } from 'wouter';

import {
  api,
  InterviewQA,
  InterviewFeedback
} from '../lib/api';


export default function Interview() {

  const [, setLocation] =
    useLocation();


  const [questions, setQuestions] =
    useState<string[]>([]);

  const [currentQuestion, setCurrentQuestion] =
    useState(0);

  const [answer, setAnswer] =
    useState('');

  const [transcript, setTranscript] =
    useState<InterviewQA[]>([]);

  const [feedback, setFeedback] =
    useState<InterviewFeedback | null>(null);

  const [loading, setLoading] =
    useState(true);

  const [submitting, setSubmitting] =
    useState(false);

  const [error, setError] =
    useState('');


  useEffect(() => {

    startInterview();

  }, []);


  async function startInterview() {

    try {

      setLoading(true);

      setError('');

      const resumeId =
        sessionStorage.getItem(
          'lastResumeId'
        );

      const jobDescription =
        sessionStorage.getItem(
          'lastJobDescription'
        );


      if (!resumeId) {

        throw new Error(
          'No resume selected.'
        );
      }


      if (!jobDescription) {

        throw new Error(
          'No job description found.'
        );
      }


      const result =
        await api.startInterview(
          Number(resumeId),
          jobDescription
        );


      if (
        !result.questions ||
        result.questions.length === 0
      ) {

        throw new Error(
          'No interview questions were generated.'
        );
      }


      setQuestions(
        result.questions
      );

    } catch (err) {

      setError(
        err instanceof Error
          ? err.message
          : 'Failed to start interview.'
      );

    } finally {

      setLoading(false);
    }
  }


  async function submitAnswer() {

    if (!answer.trim()) {

      setError(
        'Please enter your answer before continuing.'
      );

      return;
    }


    setError('');


    const newTranscript = [
      ...transcript,
      {
        question:
          questions[currentQuestion],

        answer:
          answer.trim()
      }
    ];


    setTranscript(
      newTranscript
    );

    setAnswer('');


    const isLastQuestion =
      currentQuestion ===
      questions.length - 1;


    if (!isLastQuestion) {

      setCurrentQuestion(
        currentQuestion + 1
      );

      return;
    }


    await finishInterview(
      newTranscript
    );
  }


  async function finishInterview(
    finalTranscript: InterviewQA[]
  ) {

    try {

      setSubmitting(true);

      setError('');


      const resumeId =
        sessionStorage.getItem(
          'lastResumeId'
        );

      const jobDescription =
        sessionStorage.getItem(
          'lastJobDescription'
        );


      if (!resumeId ||
          !jobDescription) {

        throw new Error(
          'Resume or job description is missing.'
        );
      }


      const result =
        await api.getInterviewFeedback(
          Number(resumeId),
          jobDescription,
          finalTranscript
        );


      setFeedback(result);

    } catch (err) {

      setError(
        err instanceof Error
          ? err.message
          : 'Failed to generate interview feedback.'
      );

    } finally {

      setSubmitting(false);
    }
  }


  function restartInterview() {

    setQuestions([]);

    setCurrentQuestion(0);

    setAnswer('');

    setTranscript([]);

    setFeedback(null);

    setError('');

    startInterview();
  }


  // =======================================================
  // LOADING
  // =======================================================

  if (loading) {

    return (
      <div className="min-h-screen flex items-center justify-center">

        <div className="text-center">

          <div className="text-2xl font-bold mb-3">
            Preparing your personalized interview...
          </div>

          <p className="text-gray-500">
            Questions are being generated from your resume
            and job description.
          </p>

        </div>

      </div>
    );
  }


  // =======================================================
  // ERROR
  // =======================================================

  if (
    error &&
    questions.length === 0
  ) {

    return (
      <div className="min-h-screen flex items-center justify-center p-6">

        <div className="max-w-xl w-full">

          <div className="bg-red-50 border border-red-200 rounded-xl p-6">

            <h2 className="text-xl font-bold text-red-700 mb-2">
              Interview could not start
            </h2>

            <p className="text-red-600 mb-5">
              {error}
            </p>

            <button
              onClick={() =>
                setLocation('/results')
              }
              className="px-5 py-2 rounded-lg bg-black text-white"
            >
              Back to Results
            </button>

          </div>

        </div>

      </div>
    );
  }


  // =======================================================
  // FEEDBACK
  // =======================================================

  if (feedback) {

    return (
      <div className="min-h-screen bg-gray-50 p-6">

        <div className="max-w-5xl mx-auto">

          <div className="mb-8">

            <h1 className="text-3xl font-bold">
              Mock Interview Results
            </h1>

            <p className="text-gray-500 mt-2">
              Your results are based on your actual
              interview answers.
            </p>

          </div>


          {/* SCORES */}

          <div className="grid md:grid-cols-3 gap-5 mb-8">

            <ScoreCard
              title="Overall Score"
              score={feedback.overall_score}
            />

            <ScoreCard
              title="Technical Score"
              score={feedback.technical_score}
            />

            <ScoreCard
              title="Communication Score"
              score={feedback.communication_score}
            />

          </div>


          {/* SUMMARY */}

          <div className="bg-white rounded-xl shadow-sm p-6 mb-6">

            <h2 className="text-xl font-bold mb-3">
              Overall Feedback
            </h2>

            <p className="text-gray-700 leading-relaxed">
              {feedback.summary}
            </p>

          </div>


          {/* STRENGTHS */}

          <div className="bg-white rounded-xl shadow-sm p-6 mb-6">

            <h2 className="text-xl font-bold mb-4">
              Strengths
            </h2>

            <ul className="space-y-3">

              {feedback.strengths.map(
                (item, index) => (

                  <li
                    key={index}
                    className="flex gap-3"
                  >

                    <span>
                      ✓
                    </span>

                    <span>
                      {item}
                    </span>

                  </li>
                )
              )}

            </ul>

          </div>


          {/* IMPROVEMENTS */}

          <div className="bg-white rounded-xl shadow-sm p-6 mb-6">

            <h2 className="text-xl font-bold mb-4">
              Areas to Improve
            </h2>

            <ul className="space-y-3">

              {feedback.improvements.map(
                (item, index) => (

                  <li
                    key={index}
                    className="flex gap-3"
                  >

                    <span>
                      →
                    </span>

                    <span>
                      {item}
                    </span>

                  </li>
                )
              )}

            </ul>

          </div>


          {/* QUESTION FEEDBACK */}

          <div className="bg-white rounded-xl shadow-sm p-6 mb-8">

            <h2 className="text-xl font-bold mb-5">
              Question-by-Question Feedback
            </h2>

            <div className="space-y-6">

              {feedback.question_feedback.map(
                (item, index) => (

                  <div
                    key={index}
                    className="border-b pb-5 last:border-b-0"
                  >

                    <div className="font-semibold mb-2">
                      Q{index + 1}. {item.question}
                    </div>

                    <p className="text-gray-600">
                      {item.feedback}
                    </p>

                  </div>
                )
              )}

            </div>

          </div>


          <div className="flex gap-4">

            <button
              onClick={restartInterview}
              className="px-6 py-3 rounded-lg bg-black text-white"
            >
              Take Interview Again
            </button>

            <button
              onClick={() =>
                setLocation('/results')
              }
              className="px-6 py-3 rounded-lg border"
            >
              Back to Results
            </button>

          </div>

        </div>

      </div>
    );
  }


  // =======================================================
  // INTERVIEW
  // =======================================================

  if (questions.length === 0) {

    return null;
  }


  const progress =
    ((currentQuestion + 1) /
      questions.length) *
    100;


  return (
    <div className="min-h-screen bg-gray-50 p-6">

      <div className="max-w-4xl mx-auto">

        {/* HEADER */}

        <div className="mb-8">

          <h1 className="text-3xl font-bold">
            Personalized Mock Interview
          </h1>

          <p className="text-gray-500 mt-2">
            Questions are generated from your uploaded
            resume and target job description.
          </p>

        </div>


        {/* PROGRESS */}

        <div className="mb-8">

          <div className="flex justify-between mb-2">

            <span className="text-sm font-medium">
              Question {currentQuestion + 1}
              {' '}of{' '}
              {questions.length}
            </span>

            <span className="text-sm text-gray-500">
              {Math.round(progress)}%
            </span>

          </div>


          <div className="w-full bg-gray-200 rounded-full h-2">

            <div
              className="bg-black h-2 rounded-full transition-all"
              style={{
                width: `${progress}%`
              }}
            />

          </div>

        </div>


        {/* QUESTION */}

        <div className="bg-white rounded-xl shadow-sm p-8 mb-6">

          <div className="text-sm text-gray-500 mb-3">
            Interviewer
          </div>

          <h2 className="text-2xl font-semibold leading-relaxed">
            {questions[currentQuestion]}
          </h2>

        </div>


        {/* ANSWER */}

        <div className="bg-white rounded-xl shadow-sm p-6">

          <label className="block font-semibold mb-3">
            Your Answer
          </label>

          <textarea
            value={answer}
            onChange={(e) =>
              setAnswer(e.target.value)
            }
            placeholder="Type your answer here..."
            rows={8}
            className="w-full border rounded-xl p-4 resize-none focus:outline-none focus:ring-2"
          />


          {error && (

            <p className="text-red-600 mt-3">
              {error}
            </p>

          )}


          <div className="flex justify-end mt-5">

            <button
              onClick={submitAnswer}
              disabled={submitting}
              className="px-6 py-3 rounded-lg bg-black text-white disabled:opacity-50"
            >

              {submitting
                ? 'Evaluating...'
                : currentQuestion ===
                    questions.length - 1
                  ? 'Finish Interview'
                  : 'Next Question'}

            </button>

          </div>

        </div>

      </div>

    </div>
  );
}


// =========================================================
// SCORE CARD
// =========================================================

function ScoreCard({
  title,
  score
}: {
  title: string;
  score: number;
}) {

  return (
    <div className="bg-white rounded-xl shadow-sm p-6 text-center">

      <div className="text-gray-500 mb-2">
        {title}
      </div>

      <div className="text-5xl font-bold">
        {score}
      </div>

      <div className="text-gray-400 mt-1">
        / 100
      </div>

    </div>
  );
}