# Используйте официальный образ Python
FROM python:3.11

# Установите зависимости
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Копируйте файлы приложения
COPY . .

# Установите команду запуска приложения
CMD ["python", "run.py"]


# docker run --publish=7474:7474 --publish=7687:7687 --volume=$HOME/neo4j/data:/data --env NEO4J_AUTH=admin/admin1234 neo4j