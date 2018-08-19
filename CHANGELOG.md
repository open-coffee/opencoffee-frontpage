# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).


## [Unreleased]
### Added
- Sonarcloud check
- Possibility to install a plugin via the frontpage

### Changed
- CoffeeNet Starter Parent to version `0.38.2`
- GroupId to 'rocks.coffeenet'

## [0.5.0]
### Changed
- CoffeeNet Starter Parent to version `0.34.0`


## [0.4.0]
### Changed
- CoffeeNet Starter Parent to version `0.33.1`


## [0.3.0]
### Added
- New design with counter
- Plugin instances are now editable
- Actions/control menu on the top right of a plugin card


## [0.2.0]
### Added
- Configuration Validation

### Changed
- CoffeeNet Starter Parent to 0.31.0
- Frontpage-Plugin-Api to 0.6.0

### Fixed
- Overlapping scroll and close area


## [0.1.1]
### Fixed
- Do not use Snapshot versions for `frontpage-plugin-api`


## 0.1.0
### Added
- Loads plugin and displays them
  - Add new plugins next to the jar in the `plugins/`
    directory or change the location with `loader.path`
    in the `loader.properties`.
  - Add persistence layer with a mongodb
  - Plugin instance configuration
  - Minor validation of of a new plugin instance

[Unreleased]: https://github.com/coffeenet/coffeenet-frontpage/compare/0.5.0...HEAD
[0.5.0]: https://github.com/coffeenet/coffeenet-frontpage/compare/0.4.0...0.5.0
[0.4.0]: https://github.com/coffeenet/coffeenet-frontpage/compare/0.3.0...0.4.0
[0.3.0]: https://github.com/coffeenet/coffeenet-frontpage/compare/0.2.0...0.3.0
[0.2.0]: https://github.com/coffeenet/coffeenet-frontpage/compare/0.1.1...0.2.0
[0.1.1]: https://github.com/coffeenet/coffeenet-frontpage/compare/0.1.0...0.1.1
