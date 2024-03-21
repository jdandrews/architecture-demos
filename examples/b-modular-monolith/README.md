# Monolithic Application

A simple monolithic application that has two components.

- Catalogue
- Cart (shopping cart)

The _Cart_ component depends on information available in the _Catalogue_
component, such as the _CatalogueItem_ _id_ and _caption_.

Both components are deployed as a single Java application.

## Prerequisites

- Oracle Java 17, or newer, is required to run this example

  ```shell
  $ java --version
  ```

  ```
  java 17.0.7 2023-04-18 LTS
  Java(TM) SE Runtime Environment (build 17.0.7+8-LTS-224)
  Java HotSpot(TM) 64-Bit Server VM (build 17.0.7+8-LTS-224, mixed mode, sharing)
  ```

  Install [SDKMAN](https://sdkman.io/)

  ```shell
  $ curl -s 'https://get.sdkman.io' | bash
  ```

  Install Oracle Java 17 using SDKMAN

  ```shell
  $ sdk list java
  $ sdk install java 17.0.7-oracle
  ```

- (_Optional_) Docker runtime

  This is only required if you like to create and run the docker images

  ```shell
  $ docker --version
  ```

  ```
  Docker version 23.0.5, build bc4487a59e
  ```

  Install [homebrew](https://mac.install.guide/homebrew/index.html)

  ```shell
  $ /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
  ```

  Install docker runtime

  ```shell
  $ brew install docker
  $ brew install colima
  $ colima start --cpu 2 --memory 4
  ```

## Useful commands

- (_Optional_) Test the application

  ```shell
  $ ./gradlew clean check
  ```

- Build the application using Gradle

  `package` is a custom Gradle task that creates the application (thin) Jar file
  and copies all the dependencies into the `./build/libs` directory.

  ```shell
  $ ./gradlew clean package
  ```

  (_Optional_) List the created artefacts

  ```shell
  $ tree './build/libs'
  ```

  There should be 67 JAR files including the `app.jar`

  ```
  ...
  1 directory, 67 files
  ```

- Run the application

  This depends on the JAR file created by the Gradle `package` custom task. See
  _Build the application using Gradle_ above for more information about this.

  ```shell
  $ java --class-path './build/libs/*' demo.Main
  ```

- (_Optional_) Create docker image

  This depends on the JAR file created by the Gradle `package` custom task. See
  _Build the application using Gradle_ above for more information about this.

  ```shell
  $ docker buildx build \
    --file './docker/Dockerfile' \
    --tag 'application-monolithic' \
    --load \
    .
  ```

  Alternative, there is another docker file (`./docker/Dockerfile-with-builder`)
  that builds the application too.

  ```shell
  $ docker buildx build \
    --file './docker/Dockerfile-with-builder' \
    --tag 'application-monolithic' \
    --load \
    .
  ```

- (_Optional_) Run the docker image

  This depends on the docker image to be available. See _Create docker image_
  above for more information about this.

  ```shell
  $ docker run \
    --rm \
    --detach \
    --publish 8080:8080 \
    --name 'application-monolithic' \
    'application-monolithic'
  ```

- Try the application

  The application needs to be running. See _Run the application_ or
  _Run the docker image_ above for more information about this.

  Fetch item with id `1`

  ```shell
  $ curl 'http://localhost:8080/catalogue/item/1' | jq
  ```

  It will return the details of item with id `1`

  ```json
  {
    "id": 1,
    "caption": "Leather Sofa",
    "description": "A very nice and comfortable sofa"
  }
  ```

  Fetch item with id `5`

  ```shell
  $ curl 'http://localhost:8080/catalogue/item/5' | jq
  ```

  It will return the details of item with id `5`

  ```json
  {
    "id": 5,
    "caption": "LED TV",
    "description": "A very large TV set, ideal for those who like to bring TV shows and spend time watching TV"
  }
  ```

  Fetch cart with id `1`. This cart contains two items with ids `1` and `5`.

  ```shell
  $ curl 'http://localhost:8080/cart/1' | jq
  ```

  It will return the details of cart with id `1`

  ```json
  {
    "id": 1,
    "items": [
      {
        "id": 1,
        "caption": "Leather Sofa",
        "quantity": 1
      },
      {
        "id": 5,
        "caption": "LED TV",
        "quantity": 1
      }
    ]
  }
  ```

- (_Optional_) Stop the docker container

  ```shell
  $ docker stop 'application-monolithic'
  ```

## Troubleshooting docker

- Docker container not starting

  Comment out the `ENTRYPOINT` from the docker file and build the docker image.
  Then run the image using the following to open a command shell into the
  container (instead of running the Java application).

  ```shell
  $ docker run \
    --rm \
    --interactive \
    --tty \
    --name 'application-monolithic' \
    'application-monolithic' \
    /bin/sh
  ```

- Log into a running docker container

  ```shell
  $ docker exec \
    --interactive \
    --tty \
    'application-monolithic' \
    /bin/sh
  ```
