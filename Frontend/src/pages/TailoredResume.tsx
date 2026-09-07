import { useEffect, useState } from 'react';
import { useLocation } from 'wouter';
import { motion } from 'framer-motion';
import {
  ArrowLeft,
  Download,
  Loader2,
} from 'lucide-react';

import { Button } from '@/components/ui/button';


// =====================================================
// TYPES
// =====================================================

interface Experience {
  title: string;
  company: string;
  duration: string;
  description: string[];
}

interface Project {
  name: string;
  technologies: string[];
  description: string[];
}

interface Education {
  degree: string;
  institution: string;
  year: string;
  details: string;
}

interface TailoredResume {
  professional_summary: string;
  skills: string[];
  experience: Experience[];
  projects: Project[];
  education: Education[];
  certifications: string[];
}


// =====================================================
// COMPONENT
// =====================================================

export default function TailoredResume() {

  const [, setLocation] = useLocation();

  const [resume, setResume] =
    useState<TailoredResume | null>(null);


  // ===================================================
  // LOAD TAILORED RESUME
  // ===================================================

  useEffect(() => {

    const storedResume =
      sessionStorage.getItem('tailoredResume');


    if (!storedResume) {

      setLocation('/results');

      return;
    }


    try {

      const parsedResume =
        JSON.parse(storedResume);

      setResume(parsedResume);

    } catch (error) {

      console.error(
        'Failed to parse tailored resume:',
        error
      );

      sessionStorage.removeItem(
        'tailoredResume'
      );

      setLocation('/results');
    }

  }, [setLocation]);


  // ===================================================
  // PRINT / SAVE PDF
  // ===================================================

  const handlePrint = () => {

    window.print();

  };


  // ===================================================
  // LOADING
  // ===================================================

  if (!resume) {

    return (
      <div className="min-h-[100dvh] flex items-center justify-center">

        <Loader2
          className="h-8 w-8 animate-spin"
        />

      </div>
    );
  }


  // ===================================================
  // SAFE ARRAY HELPERS
  // ===================================================

  const skills =
    Array.isArray(resume.skills)
      ? resume.skills
      : [];


  const experience =
    Array.isArray(resume.experience)
      ? resume.experience
      : [];


  const projects =
    Array.isArray(resume.projects)
      ? resume.projects
      : [];


  const education =
    Array.isArray(resume.education)
      ? resume.education
      : [];


  const certifications =
    Array.isArray(resume.certifications)
      ? resume.certifications
      : [];


  // ===================================================
  // PAGE
  // ===================================================

  return (

    <div className="min-h-[100dvh] bg-muted/30 py-8">


      {/* =================================================
          TOP CONTROLS
          ================================================= */}

      <div className="max-w-5xl mx-auto px-6 mb-6 print:hidden">

        <div className="flex items-center justify-between">

          <Button
            variant="ghost"
            onClick={() =>
              setLocation('/results')
            }
          >

            <ArrowLeft
              className="mr-2 h-4 w-4"
            />

            Back to Analysis

          </Button>


          <Button
            onClick={handlePrint}
            className="gap-2"
          >

            <Download
              className="h-4 w-4"
            />

            Save as PDF

          </Button>

        </div>

      </div>


      {/* =================================================
          RESUME
          ================================================= */}

      <motion.div
        initial={{
          opacity: 0,
          y: 20
        }}
        animate={{
          opacity: 1,
          y: 0
        }}
        className="max-w-5xl mx-auto px-6"
      >

        <div
          id="resume-preview"
          className="
            bg-white
            text-black
            shadow-2xl
            rounded-lg
            p-10
            md:p-14
            print:shadow-none
            print:rounded-none
            print:p-8
          "
        >


          {/* =================================================
              PROFESSIONAL SUMMARY
              ================================================= */}

          {resume.professional_summary && (

            <section className="mb-8">

              <h1 className="text-3xl font-bold mb-5">
                Professional Summary
              </h1>


              <p className="text-base leading-relaxed">

                {resume.professional_summary}

              </p>

            </section>

          )}


          {/* =================================================
              SKILLS
              ================================================= */}

          {skills.length > 0 && (

            <section className="mb-8">

              <h2 className="
                text-xl
                font-bold
                border-b
                border-black
                pb-2
                mb-4
              ">
                Skills
              </h2>


              <div className="flex flex-wrap gap-2">

                {skills.map(
                  (skill, index) => (

                    <span
                      key={index}
                      className="text-sm"
                    >

                      {skill}

                      {index <
                        skills.length - 1
                        ? ' •'
                        : ''}

                    </span>

                  )
                )}

              </div>

            </section>

          )}


          {/* =================================================
              EXPERIENCE
              ================================================= */}

          {experience.length > 0 && (

            <section className="mb-8">

              <h2 className="
                text-xl
                font-bold
                border-b
                border-black
                pb-2
                mb-4
              ">
                Experience
              </h2>


              <div className="space-y-6">

                {experience.map(
                  (item, index) => (

                    <div key={index}>


                      <div className="
                        flex
                        justify-between
                        gap-4
                      ">


                        <div>

                          {item.title && (

                            <h3 className="
                              font-bold
                              text-lg
                            ">

                              {item.title}

                            </h3>

                          )}


                          {item.company && (

                            <p className="font-medium">

                              {item.company}

                            </p>

                          )}

                        </div>


                        {item.duration && (

                          <p className="
                            text-sm
                            whitespace-nowrap
                          ">

                            {item.duration}

                          </p>

                        )}

                      </div>


                      {Array.isArray(
                        item.description
                      ) &&
                        item.description.length >
                          0 && (

                          <ul className="
                            list-disc
                            ml-5
                            mt-3
                            space-y-2
                          ">

                            {item.description.map(
                              (
                                description,
                                descriptionIndex
                              ) => (

                                <li
                                  key={
                                    descriptionIndex
                                  }
                                  className="
                                    text-sm
                                    leading-relaxed
                                  "
                                >

                                  {description}

                                </li>

                              )
                            )}

                          </ul>

                        )}

                    </div>

                  )
                )}

              </div>

            </section>

          )}


          {/* =================================================
              PROJECTS
              ================================================= */}

          {projects.length > 0 && (

            <section className="mb-8">

              <h2 className="
                text-xl
                font-bold
                border-b
                border-black
                pb-2
                mb-4
              ">
                Projects
              </h2>


              <div className="space-y-6">

                {projects.map(
                  (project, index) => (

                    <div key={index}>


                      {project.name && (

                        <h3 className="
                          font-bold
                          text-lg
                        ">

                          {project.name}

                        </h3>

                      )}


                      {Array.isArray(
                        project.technologies
                      ) &&
                        project.technologies.length >
                          0 && (

                          <p className="
                            text-sm
                            font-medium
                            mt-1
                          ">

                            Technologies:{' '}

                            {project.technologies.join(
                              ', '
                            )}

                          </p>

                        )}


                      {Array.isArray(
                        project.description
                      ) &&
                        project.description.length >
                          0 && (

                          <ul className="
                            list-disc
                            ml-5
                            mt-3
                            space-y-2
                          ">

                            {project.description.map(
                              (
                                description,
                                descriptionIndex
                              ) => (

                                <li
                                  key={
                                    descriptionIndex
                                  }
                                  className="
                                    text-sm
                                    leading-relaxed
                                  "
                                >

                                  {description}

                                </li>

                              )
                            )}

                          </ul>

                        )}

                    </div>

                  )
                )}

              </div>

            </section>

          )}


          {/* =================================================
              EDUCATION
              ================================================= */}

          {education.length > 0 && (

            <section className="mb-8">

              <h2 className="
                text-xl
                font-bold
                border-b
                border-black
                pb-2
                mb-4
              ">
                Education
              </h2>


              <div className="space-y-5">

                {education.map(
                  (item, index) => (

                    <div key={index}>


                      {/* Degree */}

                      {item.degree && (

                        <h3 className="
                          font-bold
                          text-lg
                        ">

                          {item.degree}

                        </h3>

                      )}


                      {/* Institution */}

                      {item.institution && (

                        <p className="
                          font-medium
                          text-sm
                        ">

                          {item.institution}

                        </p>

                      )}


                      {/* Year */}

                      {item.year && (

                        <p className="
                          text-sm
                          mt-1
                        ">

                          {item.year}

                        </p>

                      )}


                      {/* Details */}

                      {item.details && (

                        <p className="
                          text-sm
                          mt-1
                          leading-relaxed
                        ">

                          {item.details}

                        </p>

                      )}

                    </div>

                  )
                )}

              </div>

            </section>

          )}


          {/* =================================================
              CERTIFICATIONS
              ================================================= */}

          {certifications.length > 0 && (

            <section className="mb-8">

              <h2 className="
                text-xl
                font-bold
                border-b
                border-black
                pb-2
                mb-4
              ">
                Certifications
              </h2>


              <ul className="space-y-2">

                {certifications.map(
                  (
                    certification,
                    index
                  ) => (

                    <li
                      key={index}
                      className="
                        text-sm
                        leading-relaxed
                      "
                    >

                      {certification}

                    </li>

                  )
                )}

              </ul>

            </section>

          )}

        </div>

      </motion.div>


      {/* =================================================
          PRINT CSS
          ================================================= */}

      <style>{`

        @media print {

          @page {
            size: A4;
            margin: 0.5in;
          }

          body {
            background: white !important;
          }

          #resume-preview {
            width: 100%;
            max-width: none;
            box-shadow: none !important;
            border-radius: 0 !important;
          }

        }

      `}</style>

    </div>
  );
}