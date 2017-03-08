import org.oreto.groovypath.PathBuilder
import spock.lang.Specification

class PathBuilderTest extends Specification {
    def "static paths work"() {
        setup:
        def pathList = ['a', 'b', 'c.exe']
        def sep = '/'

        when:
        def path = PathBuilder.path(pathList, sep)
        def fullpath = PathBuilder.fullPath(pathList, sep, sep)

        then:
        path == 'a/b/c.exe' && fullpath == '/a/b/c.exe'
    }
}
