import requests

url = 'http://127.0.0.1:8001/analyze'

payloads = [
    {"resumeText": "test resume text", "jobDescription": "test job"},
    {"resume_text": "test resume text", "job_description": "test job"}
]

for p in payloads:
    r = requests.post(url, json=p)
    print('payload:', p)
    print('status:', r.status_code)
    try:
        print('body:', r.json())
    except Exception:
        print('body_text:', r.text[:500])
    print('---')
