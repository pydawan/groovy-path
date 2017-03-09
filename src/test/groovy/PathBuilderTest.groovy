import groovy.io.FileType
import org.oreto.groovypath.PathBuilder
import spock.lang.Specification

class PathBuilderTest extends Specification {
    def "static paths work"() {
        setup:
        def pathList = ['a', 'b', 'c.exe']
        def sep = '/'

        when:
        def path = PathBuilder.path(pathList, sep)
        def fullpath = PathBuilder.path(pathList, sep, sep)

        then:
        path == 'a/b/c.exe' && fullpath == '/a/b/c.exe'
    }

    def "basic PathBuilder"() {
        setup:
        def pathList = ['a', 'b', 'c.exe']
        def pathBuilder = PathBuilder.createPathBuilder('\\', 'C:\\')

        when:
        pathBuilder.addAll(pathList)

        then:
        pathBuilder.toString() == "C:\\a\\b\\c.exe"
    }

    def "PathBuilder subpath"() {
        setup:
        def currentDir = PathBuilder.workingDirectory()
        def pathBuilder1 = new PathBuilder(currentDir)
        def pathBuilder2 = new PathBuilder(currentDir)
        def size = pathBuilder1.size()

        when:
        pathBuilder2.addAll(['test', 'ing'])
        pathBuilder2.subPath(size)

        then:
        pathBuilder2.toUrl() == '/test/ing'
    }

    def "PathBuilder exists"() {
        setup:
        def currentDir = PathBuilder.workingDirectory()
        def pathBuilder1 = new PathBuilder(currentDir)
        def pathBuilder2 = new PathBuilder(pathBuilder1)

        when:
        pathBuilder1.addAll(['probably', 'does not', 'exist'])

        then:
        !pathBuilder1.exists() && pathBuilder2.exists()
    }

    def "PathBuilder src file"() {
        setup:
        def srcPath = PathBuilder.classToSrcPath(PathBuilderTest.class, PathBuilder.DEFAULT_TEST_SRC_SET)
        def pathBuilder1 = new PathBuilder(srcPath)

        when:
        def file1 = pathBuilder1.toFile()
        def file2 = new PathBuilder(file1.parentFile).toFile()
        def file3 = new PathBuilder(['src']).toFile()
        def count = 0
        file3.eachFileRecurse (FileType.FILES) { File file ->
            count++
        }

        then:
        file1.exists() && !file1.isDirectory() && file2.isDirectory() && file3.exists() && count >= 2
    }
}
