FROM python:3.11.12-slim-bullseye

RUN pip install -U pip
RUN pip install pipenv

WORKDIR /app

COPY [ "Pipfile", "Pipfile.lock", "./" ]

RUN pipenv install --system --deploy

COPY config/ ./config/
COPY service/ ./service/
COPY utils/ ./utils/

COPY [ "main.py", "./"]

ENTRYPOINT ["python", "-m", "main"]