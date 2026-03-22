package local.qlik.lang

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object QlikFileType : LanguageFileType(QlikLanguage) {
    override fun getName(): String = "Qlik Script"
    override fun getDescription(): String = "Qlik Sense / QlikView script (.qvs)"
    override fun getDefaultExtension(): String = "qvs"
    override fun getIcon(): Icon? = null
}

