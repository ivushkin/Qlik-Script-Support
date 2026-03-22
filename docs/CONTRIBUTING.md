# Разработка

## Окружение

- **JDK 17**
- (опционально) **Docker** — см. `docker-compose.yml`

## Сборка плагина

```bash
docker compose run --rm qlik-idea-plugin-build
```

или локально при установленном Gradle:

```bash
gradle buildPlugin
```

Артефакт: `build/distributions/*.zip`.

## Запуск IDE с плагином

```bash
gradle runIde
```

## Обновление `keywords.txt` / `functions.txt`

Источник — грамматика VS Code (https://github.com/bintocher/vscode-qlik-editor )

```powershell
powershell -NoProfile -File .\tools\extract_lists.ps1
```

Файлы правятся в `src/main/resources/qlik/`.
