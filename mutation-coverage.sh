#!/bin/sh
exec ./mvnw clean test pitest:mutationCoverage
