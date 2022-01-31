[![Build Status](https://github.com/coffeenet/coffeenet-frontpage/workflows/CoffeeNet%20Frontpage%20CI/badge.svg)](https://github.com/coffeenet/coffeenet-frontpage/actions?query=workflow%3A%22CoffeeNet+Frontpage+CI%22)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=rocks.coffeenet:frontpage&metric=coverage)](https://sonarcloud.io/dashboard?id=rocks.coffeenet:frontpage)

# OpenCoffee Frontpage

<p align="center">
    <img src="https://raw.githubusercontent.com/coffeenet/coffeenet.github.io/code/themes/coffeenet/static/img/coffeenet_logo.png" width="256px" alt="CoffeeNet Logo" />
</p>

<p align="center">
    the personalisable frontpage of the CoffeeNet
</p>

The CoffeeNet Frontpage is a personalisable landingpage, which uses a plugin
based mechanism for adding widgets like feeds, clock or [isLieb-comic](https://islieb.de/).
You can write your own plugin and intregrate with the frontpage.

## Requirements

* Java 8
* Docker 17.06.0+
* Docker Compose 1.20.x

## Development

Start application and environment ([MongoDB]) is used for persistence)

```
docker-compose up
./mvnw clean spring-boot:run
```

[Spring Boot DevTools] are also included for inflight changes.

### Example Plugin Setup 

All plugins from the CoffeeNet team can be found in the [CoffeeNet Organisation].

## Add Plugin

If you want to use a plugin you can download the `jar` e.g. from the
[Releases][feed plugin Releases] section. The plugin `jar` must be placed in
`/plugins`-directory beside the frontpage application:

```
frontpage.jar
plugins/
 \-- coffeenet-frontpage-plugin-feed.jar
```

## Build your own plugin

An CoffeeNet frontpage plugin have to implement the [API][plugin API]. An
example is the [clock plugin] with [implementation][clock plugin implementation]
of the frontpage plugin interface. Little more complex plugin example is the [feed plugin].


[Spring Boot DevTools]: https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-devtools.html
[MongoDB]: https://www.mongodb.com

[CoffeeNet Organisation]: https://github.com/search?q=topic%3Afrontpage-plugin+org%3Acoffeenet&type=Repositories

[plugin API]: https://github.com/coffeenet/coffeenet-frontpage-plugin-api
[clock plugin]: https://github.com/coffeenet/coffeenet-frontpage-plugin-clock
[clock plugin implementation]: https://github.com/coffeenet/coffeenet-frontpage-plugin-clock/blob/master/src/main/java/coffee/synyx/frontpage/plugin/clock/ClockPlugin.java
[feed plugin]: https://github.com/coffeenet/coffeenet-frontpage-plugin-feed
[feed plugin Releases]: https://github.com/coffeenet/coffeenet-frontpage-plugin-feed/releases
