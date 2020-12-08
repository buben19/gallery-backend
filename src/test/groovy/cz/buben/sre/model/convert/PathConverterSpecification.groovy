package cz.buben.sre.model.convert

import spock.lang.Specification

import java.nio.file.Paths

class PathConverterSpecification extends Specification {

    PathConverter converter = new PathConverter()

    def "convert string to path"() {
        expect:
        converter.convertToEntityAttribute('a/b/c/d') == Paths.get('a/b/c/d')
    }

    def "convert path to string"() {
        expect:
        converter.convertToDatabaseColumn(Paths.get('a/b/c/d')) == 'a/b/c/d'
    }

    def "null string is converted to null path"() {
        expect:
        converter.convertToEntityAttribute(null) == null
    }

    def "empty string is converted to null path"() {
        expect:
        converter.convertToEntityAttribute('') == null
    }

    def "null path is converted to null string"() {
        expect:
        converter.convertToDatabaseColumn(null) == null
    }
}
