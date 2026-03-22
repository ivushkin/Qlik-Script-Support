# Qlik Script Support

Плагин для IntelliJ Platform: подсветка синтаксиса Qlik Sense / QlikView Script для файлов `.qvs` (QlikView Script).

**Совместимость:** IDE на базе IntelliJ Platform **2023.3.x** (в т.ч. PyCharm, IntelliJ IDEA и др.).

Идея списков ключевых слов и функций — из грамматики VS Code [bintocher/vscode-qlik-editor]( https://github.com/bintocher/vscode-qlik-editor). 

Ссылка на профиль: https://github.com/bintocher 

## Возможности

- Подсветка: ключевые слова, встроенные функции, строки, числа, комментарии `//` и `/* */`, макросы `$(...)`.
- Зарегистрированное расширение файла: `.qvs`.

## Установка

1. Скачай архив плагина (`.zip`) из раздела **Releases** своего репозитория (GitHub / GitLab и т.п.).
2. В IDE: **Settings** → **Plugins** → ⚙ → **Install Plugin from Disk…** → выбери скачанный `.zip`.
3. Перезапусти IDE.

Подробнее: [docs/INSTALL.md](docs/INSTALL.md).

## Документация

| Файл | Содержание |
|------|------------|
| [docs/INSTALL.md](docs/INSTALL.md) | Установка и быстрая проверка |
| [docs/CONTRIBUTING.md](docs/CONTRIBUTING.md) | Разработка и сборка плагина |

## Структура репозитория

- `src/main/resources/META-INF/plugin.xml` — регистрация языка и подсветки.
- `src/main/kotlin/local/qlik/lang/` — lexer, highlighter, file type.
- `src/main/resources/qlik/keywords.txt` — ключевые слова.
- `src/main/resources/qlik/functions.txt` — встроенные функции.
- `tools/extract_lists.ps1` — перегенерация списков из `qvs.tmLanguage.json` (см. CONTRIBUTING).

## Qlik: какие артефакты касаются скрипта

- **`.qvs`** — переиспользуемые блоки скрипта ([документация Qlik](https://help.qlik.com/en-US/sense/November2025/Subsystems/Hub/Content/Sense_Hub/LoadData/QVS/create-common-scripting-qvs.htm)).

## Лицензия

Лицензия MIT.
