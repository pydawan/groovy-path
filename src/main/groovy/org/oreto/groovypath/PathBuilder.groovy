package org.oreto.groovypath

class PathBuilder implements Iterable<String> {

    static String DEFAULT_SRC_EXT = 'groovy'
    static List<String> DEFAULT_SRC_SET = ['src', 'main', DEFAULT_SRC_EXT]
    static List<String> DEFAULT_TEST_SRC_SET = ['src', 'test', DEFAULT_SRC_EXT]

    /**
     * Takes a collection of names and returns the computed path
     * @param names directory/file names
     * @param separator name-separator character
     * @param preface path to preface the names with
     * @return The path
     */
    static String path(Collection<String> names, String separator = File.separator, String preface = '') {
        def full = []
        names.each { full.addAll(pathToNames(it, separator)) }
        preface + full.join(separator)
    }

    static String workingDirectory() { new File(".").canonicalPath }

    static String classToSrcPath(Class aClass, List<String> srcSet = DEFAULT_SRC_SET, String ext = DEFAULT_SRC_EXT, String separator = File.separator) {
        def names = srcSet.collect()
        if(aClass.package) names.addAll(aClass.package.name.split('.'))
        names.add("${aClass.name}.$ext")
        path(names, separator)
    }

    static PathBuilder createPathBuilder(String separator = File.separator, String preface = '') {
        PathBuilder pathBuilder = new PathBuilder()
        pathBuilder.separator = separator
        pathBuilder.preface = preface
        pathBuilder
    }

    static List<String> pathToNames(String path, String separator = File.separator) {
        path.split(separatorRegex(separator))
    }

    static separatorRegex = { separator ->
        separator == '\\' ? '\\\\' : separator
    }

    protected synchronized List<String> names = []
    String separator = File.separator
    String preface = ''
    List<String> sourceSet = DEFAULT_SRC_SET
    List<String> testSourceSet = DEFAULT_TEST_SRC_SET

    PathBuilder() {}

    PathBuilder(String path) {
        this.names = pathToNames(path, separator)
    }

    PathBuilder(Collection<String> names) {
        this.addAll(names)
    }

    PathBuilder(File file) {
        this(file.canonicalPath)
    }

    PathBuilder(PathBuilder pathBuilder) {
        this.separator = pathBuilder.separator
        this.preface = pathBuilder.preface
        this.names.addAll(pathBuilder.names)
        this.sourceSet = pathBuilder.sourceSet
        this.testSourceSet = pathBuilder.testSourceSet
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        new PathBuilder(this)
    }

    PathBuilder addAll(Collection<String> names, String... name) {
        List<String> collection = names.collect()
        collection.addAll(name)
        def all = []
        collection.each{ all.addAll(pathToNames(it, separator)) }
        this.names.addAll(all)
        this
    }

    PathBuilder add(String... names) {
        addAll(names.toList())
    }

    PathBuilder subPath(int start, int end) {
        names = names.subList(start, end)
        this
    }

    PathBuilder subPath(int start) {
        subPath(start, names.size())
    }

    PathBuilder clear() {
        names.clear()
        this
    }

    PathBuilder create() {
        def file = toFile()
        file.parentFile.mkdirs()
        file.createNewFile()
        this
    }

    PathBuilder createDir() {
        toFile().mkdirs()
        this
    }

    PathBuilder delete() {
        def file = toFile()
        if(file.isDirectory()) file.deleteDir()
        else file.delete()
        this
    }

    PathBuilder deleteRoot() {
        def file = new File(names[0])
        if(file.isDirectory()) file.deleteDir()
        else file.delete()
        this
    }

    int size() { names.size() }
    boolean exists() { toFile().exists() }

    @Override String toString() { path(names, separator, preface)}

    File toFile() {
        new File(toString())
    }

    String toUrl() {
        '/' + toString().replaceAll(getSeparatorRegex(), '/')
    }

    protected getSeparatorRegex() {
        separatorRegex(separator)
    }

    @Override
    Iterator<String> iterator() {
        new PathBuilderIterator(names)
    }

    protected static final class PathBuilderIterator implements Iterator<String> {

        protected int current = 0
        List<String> names

        PathBuilderIterator(List<String> names) {
            this.names = names
        }

        @Override
        boolean hasNext() {
            current < names.size()
        }

        @Override
        String next() {
            if(!hasNext()) throw new NoSuchElementException()
            names.get(current++)
        }
    }
}
