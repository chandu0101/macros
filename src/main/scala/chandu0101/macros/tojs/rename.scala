package chandu0101.macros.tojs

import scala.annotation.StaticAnnotation

/**
 * annotation to provide custom name for fields while converting to json object
 * @param name
 */
class rename(name : String) extends StaticAnnotation

/**
 *  fields with this annotation will be excluded from final json object
 */
class exclude extends StaticAnnotation
