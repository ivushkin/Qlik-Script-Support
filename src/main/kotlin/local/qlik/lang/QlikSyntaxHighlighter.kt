package local.qlik.lang

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

class QlikSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer = QlikLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
        pack(ATTRS[tokenType])

    companion object {
        val KEYWORD = TextAttributesKey.createTextAttributesKey(
            "QLIK_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )

        val FUNCTION = TextAttributesKey.createTextAttributesKey(
            "QLIK_FUNCTION",
            DefaultLanguageHighlighterColors.KEYWORD
        )

        val MACRO = TextAttributesKey.createTextAttributesKey(
            "QLIK_MACRO",
            DefaultLanguageHighlighterColors.METADATA
        )

        val STRING = TextAttributesKey.createTextAttributesKey(
            "QLIK_STRING",
            DefaultLanguageHighlighterColors.STRING
        )

        val NUMBER = TextAttributesKey.createTextAttributesKey(
            "QLIK_NUMBER",
            DefaultLanguageHighlighterColors.NUMBER
        )

        val LINE_COMMENT = TextAttributesKey.createTextAttributesKey(
            "QLIK_LINE_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT
        )

        val BLOCK_COMMENT = TextAttributesKey.createTextAttributesKey(
            "QLIK_BLOCK_COMMENT",
            DefaultLanguageHighlighterColors.BLOCK_COMMENT
        )

        val OPERATOR = TextAttributesKey.createTextAttributesKey(
            "QLIK_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )

        val BAD_CHARACTER = TextAttributesKey.createTextAttributesKey(
            "QLIK_BAD_CHARACTER",
            DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE
        )

        private val ATTRS: Map<IElementType, TextAttributesKey> = hashMapOf(
            QlikTypes.KEYWORD to KEYWORD,
            QlikTypes.FUNCTION to FUNCTION,
            QlikTypes.MACRO to MACRO,
            QlikTypes.STRING to STRING,
            QlikTypes.NUMBER to NUMBER,
            QlikTypes.LINE_COMMENT to LINE_COMMENT,
            QlikTypes.BLOCK_COMMENT to BLOCK_COMMENT,
            QlikTypes.OPERATOR to OPERATOR,
            QlikTypes.BAD_CHARACTER to BAD_CHARACTER
        )
    }
}

