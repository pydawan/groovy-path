##### Package
```
gradle jar
gradle --no-daemon jar
```

##### Publish
```
gradle --no-daemon publish
```

#### Add to project

- Add the repository location
```
repositories {
    maven {
        url "s3://org.oreto.maven/snapshots"
    }
}
```
- add dependency
```
compile 'org.oreto:groovy-path:1.0-SNAPSHOT'
```