package chandu0101.macros.tojs

import scala.annotation.StaticAnnotation

/**
 * annotation to provide custom name for fields while converting to json object
 * @param name
 */
class rename(name : String) extends StaticAnnotation
