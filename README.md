# Relevant Data
A framework to deal with data that really matters.

## Master Build Status

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/grycap/relevantdata/blob/master/LICENSE)
[![Build Status](https://api.travis-ci.org/grycap/relevantdata.svg)](https://travis-ci.org/grycap/relevantdata/builds)
[![Coverage Status](https://coveralls.io/repos/grycap/relevantdata/badge.svg?branch=master&service=github)](https://coveralls.io/github/grycap/relevantdata?branch=master)

## Environment variables

``GRYCAP_TESTS_PRINT_OUTPUT`` set value to ``true`` to print tests output.

## Installation

### Install from source

``$ mvn clean install relevantdata``

## Development

### Run all tests logging to the console

``$ mvn clean verify -pl relevantdata-core -Dgrycap.tests.print.out=true |& tee /tmp/LOGFILE``

### Run functional and sanity tests logging to the console

``$ mvn clean test -pl relevantdata-core -Dgrycap.tests.print.out=true |& tee /tmp/LOGFILE``

## Continuous integration

``$ mvn clean verify relevantdata``
