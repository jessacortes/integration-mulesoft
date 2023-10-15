# Custom Connector Deployment Guide

This guide outlines the steps to follow for deploying a custom connector.

## Install in Local `.m2` Directory

To install the connector in your local `.m2` repository for testing on your local machine, use the following Maven command in the project directory where the `pom.xml` file is located:

```bash
mvn clean install

If you want to skip JUnit tests, use the following command:

```bash
mvn clean install -DskipTests

##  Check Your Project Version
You can use this connector in your Mule 4 application by adding the following dependency to your pom.xml:

```bash
<dependency>
    <groupId>your-connector-group-id</groupId>
    <artifactId>your-artifact-id</artifactId>
    <version>1.x.x</version>
    <classifier>mule-plugin</classifier>
</dependency>

This connector is now ready to be used in your Mule 4 application.

## Install in Local Anypoint Exchange
To install the connector in Anypoint Exchange within your organization, use the following Maven command:

```bash
mvn deploy -DskipTests
