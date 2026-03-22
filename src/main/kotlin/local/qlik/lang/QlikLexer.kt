package local.qlik.lang

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Locale

class QlikLexer : LexerBase() {
    private var buffer: CharSequence = ""
    private var bufferEnd = 0
    private var tokenStart = 0
    private var tokenEnd = 0
    private var tokenType: IElementType? = null

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.bufferEnd = endOffset
        this.tokenStart = startOffset
        this.tokenEnd = startOffset
        this.tokenType = null
        advance()
    }

    override fun getState(): Int = 0
    override fun getTokenType(): IElementType? = tokenType
    override fun getTokenStart(): Int = tokenStart
    override fun getTokenEnd(): Int = tokenEnd
    override fun getBufferSequence(): CharSequence = buffer
    override fun getBufferEnd(): Int = bufferEnd

    override fun advance() {
        if (tokenEnd >= bufferEnd) {
            tokenType = null
            return
        }

        tokenStart = tokenEnd
        val c = buffer[tokenStart]

        // whitespace
        if (c.isWhitespace()) {
            var i = tokenStart + 1
            while (i < bufferEnd && buffer[i].isWhitespace()) i++
            tokenEnd = i
            tokenType = QlikTypes.WHITE_SPACE
            return
        }

        // line comment //
        if (c == '/' && tokenStart + 1 < bufferEnd && buffer[tokenStart + 1] == '/') {
            var i = tokenStart + 2
            while (i < bufferEnd && buffer[i] != '\n' && buffer[i] != '\r') i++
            tokenEnd = i
            tokenType = QlikTypes.LINE_COMMENT
            return
        }

        // block comment /* ... */
        if (c == '/' && tokenStart + 1 < bufferEnd && buffer[tokenStart + 1] == '*') {
            var i = tokenStart + 2
            while (i + 1 < bufferEnd && !(buffer[i] == '*' && buffer[i + 1] == '/')) i++
            tokenEnd = if (i + 1 < bufferEnd) i + 2 else bufferEnd
            tokenType = QlikTypes.BLOCK_COMMENT
            return
        }

        // macro $(...)
        if (c == '$' && tokenStart + 1 < bufferEnd && buffer[tokenStart + 1] == '(') {
            var i = tokenStart + 2
            while (i < bufferEnd && buffer[i] != ')') {
                val ch = buffer[i]
                if (ch == '\n' || ch == '\r') break
                i++
            }
            tokenEnd = if (i < bufferEnd && buffer[i] == ')') i + 1 else i
            tokenType = QlikTypes.MACRO
            return
        }

        // strings '...' or "..."
        if (c == '\'' || c == '"') {
            val quote = c
            var i = tokenStart + 1
            while (i < bufferEnd) {
                val ch = buffer[i]
                if (ch == quote) {
                    // doubled quote escape: '' or ""
                    val next = i + 1
                    if (next < bufferEnd && buffer[next] == quote) {
                        i = next + 1
                        continue
                    }
                    i++
                    break
                }
                if (ch == '\n' || ch == '\r') break
                i++
            }
            tokenEnd = i
            tokenType = QlikTypes.STRING
            return
        }

        // number
        if (c.isDigit()) {
            var i = tokenStart + 1
            var hasDot = false
            while (i < bufferEnd) {
                val ch = buffer[i]
                if (ch.isDigit()) {
                    i++
                    continue
                }
                if (!hasDot && ch == '.' && i + 1 < bufferEnd && buffer[i + 1].isDigit()) {
                    hasDot = true
                    i++
                    continue
                }
                break
            }
            tokenEnd = i
            tokenType = QlikTypes.NUMBER
            return
        }

        // identifier / keyword / function
        if (isIdStart(c)) {
            var i = tokenStart + 1
            while (i < bufferEnd && isIdPart(buffer[i])) i++
            tokenEnd = i

            val raw = buffer.subSequence(tokenStart, tokenEnd).toString()
            val upper = raw.uppercase()
            tokenType = when {
                KEYWORDS.contains(upper) -> QlikTypes.KEYWORD
                FUNCTIONS.contains(upper) -> QlikTypes.FUNCTION
                else -> QlikTypes.IDENTIFIER
            }
            return
        }

        // operators / punctuation
        run {
            val two = if (tokenStart + 1 < bufferEnd) "${buffer[tokenStart]}${buffer[tokenStart + 1]}" else ""
            if (TWO_CHAR_OPS.contains(two)) {
                tokenEnd = tokenStart + 2
                tokenType = QlikTypes.OPERATOR
                return
            }
        }

        if (ONE_CHAR_OPS.contains(c)) {
            tokenEnd = tokenStart + 1
            tokenType = QlikTypes.OPERATOR
            return
        }

        tokenEnd = tokenStart + 1
        tokenType = QlikTypes.BAD_CHARACTER
    }

    private fun isIdStart(c: Char): Boolean =
        c == '_' || c == '$' || c.isLetter()

    private fun isIdPart(c: Char): Boolean =
        isIdStart(c) || c.isDigit() || c == '#'

    companion object {
        private val KEYWORDS by lazy { loadUpperWordSet("/qlik/keywords.txt") }
        private val FUNCTIONS by lazy { loadUpperWordSet("/qlik/functions.txt") }

        private val TWO_CHAR_OPS = hashSetOf(
            ">=", "<=", "<>", "==", "!=", "&&", "||", "->", ":="
        )

        private val ONE_CHAR_OPS = hashSetOf(
            '+', '-', '*', '/', '%', '=', '<', '>', '(', ')', '[', ']', '{', '}', ',', ';', '.', ':', '&', '|', '!', '?'
        )

        private fun loadUpperWordSet(resourcePath: String): Set<String> {
            val stream = QlikLexer::class.java.getResourceAsStream(resourcePath) ?: return emptySet()
            BufferedReader(InputStreamReader(stream, Charsets.UTF_8)).use { br ->
                return br.lineSequence()
                    .map { it.trim() }
                    .filter { it.isNotEmpty() && !it.startsWith("#") }
                    .map { it.uppercase(Locale.ROOT) }
                    .toCollection(HashSet())
            }
        }
    }
}

