#### Installation

- Add the repository location
```
repositories {
    maven {
        url "https://s3-us-west-2.amazonaws.com/org.oreto.maven/snapshots"
    }
}
```
- add dependency
```
compile 'org.oreto:groovy-path:+'
```

### Development

##### Package
```
gradle --no-daemon jar
```

##### Publish
```
gradle --no-daemon publish
```