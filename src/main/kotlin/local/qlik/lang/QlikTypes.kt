package local.qlik.lang

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

object QlikTypes {
    @JvmField val WHITE_SPACE: IElementType = TokenType.WHITE_SPACE
    @JvmField val BAD_CHARACTER: IElementType = TokenType.BAD_CHARACTER

    @JvmField val KEYWORD: IElementType = QlikTokenType("KEYWORD")
    @JvmField val FUNCTION: IElementType = QlikTokenType("FUNCTION")
    @JvmField val MACRO: IElementType = QlikTokenType("MACRO")
    @JvmField val IDENTIFIER: IElementType = QlikTokenType("IDENTIFIER")
    @JvmField val NUMBER: IElementType = QlikTokenType("NUMBER")
    @JvmField val STRING: IElementType = QlikTokenType("STRING")
    @JvmField val LINE_COMMENT: IElementType = QlikTokenType("LINE_COMMENT")
    @JvmField val BLOCK_COMMENT: IElementType = QlikTokenType("BLOCK_COMMENT")
    @JvmField val OPERATOR: IElementType = QlikTokenType("OPERATOR")
}

