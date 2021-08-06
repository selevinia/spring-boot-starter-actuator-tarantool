# Spring boot starter for using Spring Actuator with Spring Data Tarantool

Spring Boot Actuator includes a number of additional features to help you monitor and 
manage your application when it’s pushed to production. This project provides 
auto-configuration with health indicators and contributors for Tarantool database in your Spring Boot applications.

## How to use in your project

To add the starter to a Maven-based project, add the following dependency:
```maven
<dependencies>
	<dependency>
		<groupId>io.github.selevinia</groupId>
		<artifactId>selevinia-spring-boot-starter-actuator-tarantool</artifactId>
		<version>${version}</version>
	</dependency>
</dependencies>
```

For Gradle, use the following declaration:
```gradle
dependencies {
    implementation "io.github.selevinia:selevinia-spring-boot-starter-actuator-tarantool:$version"
}
```

## Learn more

- [Spring Data Tarantool](https://github.com/selevinia/spring-data-tarantool)
- [Spring Boot Data Tarantool Starter](https://github.com/selevinia/spring-boot-starter-data-tarantool)
- [Reactive Spring Boot Data Tarantool Starter](https://github.com/selevinia/spring-boot-starter-data-tarantool-reactive)

## License

This project is Open Source software released under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).



