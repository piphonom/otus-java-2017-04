#!/usr/bin/env bash

mvn clean package

cp target/lesson13.war /opt/jetty-distribution-9.4.6.v20170531/webapps/root.war
