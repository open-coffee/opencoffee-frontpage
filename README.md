[![Build Status](https://travis-ci.org/coffeenet/coffeenet-frontpage.svg?branch=master)](https://travis-ci.org/coffeenet/coffeenet-frontpage)
[![Coverage Status](https://coveralls.io/repos/github/coffeenet/coffeenet-frontpage/badge.svg?branch=master)](https://coveralls.io/github/coffeenet/coffeenet-frontpage?branch=master)

# CoffeeNet Frontpage

<p align="center">
    <img src="https://raw.githubusercontent.com/coffeenet/coffeenet.github.io/code/static/img/coffenet_logo.png" width="256px" alt="CoffeeNet Logo" />
</p>

<p align="center">
    the personalisable frontpage of the CoffeeNet
</p>

The CoffeeNet Frontpage is a personalisable landingpage, which uses a plugin based mechanism for adding widgets like feeds, clock or [isLieb-comic](https://islieb.de/). You can write your own plugin easily and intregrate with the frontpage.

## Requirements

* Java 8
* Docker 17.06.0+

## Development

Start application and environment ([MongoDB](https://www.mongodb.com/) is used for persistence)

```
docker-compose up
./mvnw clean spring-boot:run
```

[Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-devtools.html) are also included for inflight changes.

### Example Plugin Setup 

All plugins from the CoffeeNet team can be found in the [CoffeeNet Organisation](https://github.com/search?q=topic%3Afrontpage-plugin+org%3Acoffeenet&type=Repositories).

## Add Plugin

If you want to use a plugin you can just download the `jar` e.g. from the [Releases](https://github.com/coffeenet/coffeenet-frontpage-plugin-feed/releases) section. The plugin `jar` must be placed in `/plugins`-directory beside the frontpage application:

```
frontpage.jar
plugins/
 \-- coffeenet-frontpage-plugin-feed.jar
```


