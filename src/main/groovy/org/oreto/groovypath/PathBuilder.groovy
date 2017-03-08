package org.oreto.groovypath

class PathBuilder {

    /**
     * Takes a collection of names and returns the computed relative path
     * Does not modify the original collection
     * @param names directory/file names
     * @param separator name-separator character
     * @return The relative path
     */
    static String path(Collection<String> names, String separator = File.separator) {
        names.join(separator)
    }

    /**
     * Takes a collection of names and returns the computed FULL path
     * Does not modify the original collection
     * @param names directory/file names
     * @param preface The root of the drive to begin the full path i.e. /c/ or C:\
     * @param separator name-separator character
     * @return The full path
     */
    static String fullPath(Collection<String> names, String preface = File.separator, String separator = File.separator) {
        preface + path(names, separator)
    }

    static PathBuilder newPathBuilder(String separator = File.separator, String preface = File.separator) {
        PathBuilder pathBuilder = new PathBuilder()
        pathBuilder.separator = separator
        pathBuilder.preface = preface
        pathBuilder
    }

    protected synchronized List<String> names = []
    String separator
    String preface

    PathBuilder() {
        this.separator = File.separator
        this.preface = File.separator
    }

    PathBuilder(String path) {
        this()
        this.names = path.split(getSeperatorRegex())
    }

    PathBuilder(Collection<String> names) {
        this()
        this.names.addAll(names)
    }

    PathBuilder(File file) {
        this()
        String path = file.isDirectory() ? file.path : file.getParentFile().path
        this.names = path.split(getSeperatorRegex())
    }

    PathBuilder(PathBuilder pathBuilder) {
        this.separator = pathBuilder.separator
        this.preface = pathBuilder.preface
        this.names = pathBuilder.names
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        new PathBuilder(this)
    }

    PathBuilder add(Collection<String> names, String... name) {
        names.addAll(names)
        names.addAll(name)
        this
    }

    PathBuilder subPath(int start, int end) {
        names = names.subList(0, end)
        this
    }

    PathBuilder subPath(int start) {
        subPath(start, names.size())
    }

    PathBuilder clear() { names.clear() }

    int size() { names.size() }
    boolean exists() { toFile().exists() }

    @Override String toString() { path(names, separator) }
    String toPath() { toString() }
    String toFullPath() { fullPath(names, preface, separator) }
    File toFile() {
        new File(toFullPath())
    }

    String toUrl() {
        '/' + toPath().replaceAll(getSeperatorRegex(), '/')
    }

    protected getSeperatorRegex() {
        separator == '\\' ? '\\\\' : separator
    }
}
