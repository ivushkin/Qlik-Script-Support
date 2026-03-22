$ErrorActionPreference = 'Stop'

$root = Split-Path -Parent $PSScriptRoot
$resDir = Join-Path $root 'src/main/resources/qlik'
New-Item -ItemType Directory -Force -Path $resDir | Out-Null

$url = 'https://raw.githubusercontent.com/bintocher/vscode-qlik-editor/main/syntaxes/qvs.tmLanguage.json'
$json = (Invoke-WebRequest $url).Content | ConvertFrom-Json
$repo = $json.repository

function Extract-OrList {
    param([string]$pattern)
    if ([string]::IsNullOrEmpty($pattern)) { return @() }
    # поддержка обоих видов:
    # 1) \b(AAA|BBB)(?=\s*\()
    # 2) \b(AAA|BBB)\b
    $captures = New-Object System.Collections.Generic.List[string]

    foreach ($re in @(
        '\\b\((?<inner>[^)]*)\)\(\?=\\s\*\\\(',
        '\\b\((?<inner>[^)]*)\)\\b'
    )) {
        $ms = [regex]::Matches($pattern, $re)
        foreach ($m in $ms) {
            $inner = $m.Groups['inner'].Value
            if (-not [string]::IsNullOrEmpty($inner)) { [void]$captures.Add($inner) }
        }
    }

    if ($captures.Count -eq 0) { return @() }

    $out = New-Object System.Collections.Generic.List[string]
    foreach ($inner in $captures) {
        foreach ($part in ($inner -split '\|')) {
            if ([string]::IsNullOrEmpty($part)) { continue }
            # разрезаем multi-word (end\s+if) на отдельные слова
            $clean = ($part -replace '\\\\s\\+',' ') -replace '\\\\s\\*',' '
            # разэкраниваем функции вида Timestamp\#
            $clean = $clean -replace '\\#', '#'
            foreach ($w in ($clean -split '\s+')) {
                if ([string]::IsNullOrEmpty($w)) { continue }
                if ($w -match '^[A-Za-z_][A-Za-z0-9_#]*$') { [void]$out.Add($w) }
            }
        }
    }
    return $out
}

$funcNames = New-Object 'System.Collections.Generic.HashSet[string]' ([StringComparer]::OrdinalIgnoreCase)
foreach ($p in $repo.PSObject.Properties) {
    if ($p.Name -like '*Functions') {
        foreach ($pat in $p.Value.patterns) {
            $words = Extract-OrList -pattern $pat.match
            foreach ($w in $words) {
                if (-not [string]::IsNullOrEmpty($w)) { [void]$funcNames.Add($w) }
            }

            # добираем функции вида Timestamp\# -> Timestamp#
            if (-not [string]::IsNullOrEmpty($pat.match)) {
                foreach ($m in [regex]::Matches($pat.match, '([A-Za-z_][A-Za-z0-9_]*)\\#')) {
                    $name = $m.Groups[1].Value
                    if (-not [string]::IsNullOrEmpty($name)) { [void]$funcNames.Add("$name#") }
                }
            }
        }
    }
}

$keywordNames = New-Object 'System.Collections.Generic.HashSet[string]' ([StringComparer]::OrdinalIgnoreCase)
foreach ($section in @('controlStatements','scriptStatements','prefixes')) {
    foreach ($pat in $repo.$section.patterns) {
        $words = Extract-OrList -pattern $pat.match
        foreach ($w in $words) {
            if (-not [string]::IsNullOrEmpty($w)) { [void]$keywordNames.Add($w) }
        }
    }
}

foreach ($pat in $repo.operators.patterns) {
    if ($pat.name -like 'keyword.operator.logical*') {
        $words = Extract-OrList -pattern $pat.match
        foreach ($w in $words) {
            if (-not [string]::IsNullOrEmpty($w)) { [void]$keywordNames.Add($w) }
        }
    }
}

$kwOut = $keywordNames | ForEach-Object { $_.ToUpperInvariant() } | Sort-Object -Unique
$fnOut = $funcNames | ForEach-Object { $_.ToUpperInvariant() } | Sort-Object -Unique

Set-Content -Encoding UTF8 -Path (Join-Path $resDir 'keywords.txt') -Value $kwOut
Set-Content -Encoding UTF8 -Path (Join-Path $resDir 'functions.txt') -Value $fnOut

Write-Host ("keywords: {0}" -f $kwOut.Count)
Write-Host ("functions: {0}" -f $fnOut.Count)

