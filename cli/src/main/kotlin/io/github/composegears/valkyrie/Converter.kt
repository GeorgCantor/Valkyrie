package io.github.composegears.valkyrie

import io.github.composegears.valkyrie.generator.imagevector.OutputFormat
import io.github.composegears.valkyrie.parser.isSvg
import io.github.composegears.valkyrie.parser.isXml
import kotlin.io.path.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile

/**
 * Converts SVG or XML files to ImageVector files.
 */
internal fun res2iv(
    inputPathString: String,
    outputPathString: String,
    packageName: String,
    iconPackName: String,
    generatePreview: Boolean,
    outputFormat: OutputFormat,
) {
    val inputPath = Path(inputPathString)
    if (inputPath.isRegularFile()) {
        if (!inputPath.isSvg && !inputPath.isXml) {
            outputError("The input file must be an SVG or XML file.")
        }
    }

    val outputPath = Path(outputPathString)
    if (!outputPath.isDirectory()) {
        outputError("The output path must be a directory.")
    }
}
