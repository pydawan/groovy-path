package org.oreto.groovypath

class PathBuilder {

    static List<String> DEFAULT_SRC_SET = ['src', 'main', 'groovy']
    static List<String> DEFAULT_TEST_SRC_SET = ['src', 'test', 'groovy']
    static String DEFAULT_SRC_EXT = 'groovy'

    /**
     * Takes a collection of names and returns the computed path
     * @param names directory/file names
     * @param separator name-separator character
     * @param preface The root of the drive to begin the full path i.e. /c/ or C:\
     * @return The path
     */
    static String path(Collection<String> names, String separator = File.separator, String preface = '') {
        def full = preface ? [preface] : []
        full.addAll(names)
        full.collect{ it.endsWith(separator) ? it.substring(0, it.length() - 1) : it }.join(separator)
    }

    static String workingDirectory() { new File(".").canonicalPath }

    static String classToSrcPath(Class aClass, List<String> srcSet = DEFAULT_SRC_SET, String separator = File.separator) {
        def names = srcSet.collect()
        if(aClass.package) names.addAll(aClass.package.name.split('.'))
        names.add("${aClass.name}.$DEFAULT_SRC_EXT")
        path(names, separator)
    }

    static PathBuilder createPathBuilder(String separator = File.separator, String preface = '') {
        PathBuilder pathBuilder = new PathBuilder()
        pathBuilder.separator = separator
        pathBuilder.preface = preface
        pathBuilder
    }

    protected synchronized List<String> names = []
    String separator = File.separator
    String preface = ''
    List<String> sourceSet = DEFAULT_SRC_SET

    PathBuilder() {}

    PathBuilder(String path) {
        this.names = path.split(getSeperatorRegex())
    }

    PathBuilder(Collection<String> names) {
        this.names.addAll(names)
    }

    PathBuilder(File file) {
        String path = file.canonicalPath
        this.names = path.split(getSeperatorRegex())
    }

    PathBuilder(PathBuilder pathBuilder) {
        this.separator = pathBuilder.separator
        this.preface = pathBuilder.preface
        this.names.addAll(pathBuilder.names)
        this.sourceSet = pathBuilder.sourceSet
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        new PathBuilder(this)
    }

    PathBuilder add(Collection<String> names, String... name) {
        this.names.addAll(names)
        this.names.addAll(name)
        this
    }

    PathBuilder subPath(int start, int end) {
        names = names.subList(start, end)
        this
    }

    PathBuilder subPath(int start) {
        subPath(start, names.size())
    }

    PathBuilder clear() { names.clear() }

    int size() { names.size() }
    boolean exists() { toFile().exists() }

    @Override String toString() { path(names, separator, preface)}

    File toFile() {
        new File(toString())
    }

    String toUrl() {
        '/' + toString().replaceAll(getSeperatorRegex(), '/')
    }

    protected getSeperatorRegex() {
        separator == '\\' ? '\\\\' : separator
    }
}
