FROM gradle:8.7-jdk17

WORKDIR /work
# build выполняем через docker compose (run), чтобы видеть полный вывод и не кешировать ошибки в image
