# Установка и проверка

## Требования

- IDE на базе **IntelliJ Platform 2023.3.x** (сборка плагина `233.*`, см. `plugin.xml` / `build.gradle.kts`).

## Установка из архива

1. Скачай `.zip` плагина из релизов репозитория.
2. **Settings** → **Plugins** → ⚙ → **Install Plugin from Disk…**
3. Укажи файл `.zip`, подтверди, перезапусти IDE.

## Быстрая проверка

1. Создай файл `test.qvs`.
2. Вставь фрагмент:

```qvs
// comment
LET v = 'abc';
LOAD
    Sum(Sales) as TotalSales,
    Date#('2025-01-01','YYYY-MM-DD') as D,
    $(Must_Include=lib://Files/test.qvs)
RESIDENT SalesTable
WHERE Year(OrderDate) >= 2024;
```

3. Убедись, что подсвечиваются операторы (`LOAD`, `RESIDENT`, `WHERE`, `LET`), функции (`Sum`, `Date#`, `Year`) и макрос `$(...)`.
