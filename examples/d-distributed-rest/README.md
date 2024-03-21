# Distributed Application using REST Synchronous communication

A simple distributed application that has two components, each deployed as a
separate Java application.

- Catalogue
- Cart (shopping cart)

The _Cart_ component depends on information available in the _Catalogue_
component, such as the _CatalogueItem_, _id_ and, _caption_. The components are
connected through a synchronous REST API. The _Cart_ component depends on the
_Catalogue_, and it cannot run independently.  Both components are required for
the _Cart_ component to function as expected.

This distributed application gives up _partition tolerance_ and _availability_
in favour of _consistency_. Other tradeoffs are also valid, but not explored
here.

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

  Install [SDKMAN](https://sdkman.io/).

  ```shell
  $ curl -s 'https://get.sdkman.io' | bash
  ```

  Install Oracle Java 17 using SDKMAN.

  ```shell
  $ sdk list java
  $ sdk install java 17.0.7-oracle
  ```

- (_Optional_) Docker runtime

  This is only required if you like to create and run the docker images.

  ```shell
  $ docker --version
  ```

  ```
  Docker version 23.0.5, build bc4487a59e
  ```

  Install [homebrew](https://mac.install.guide/homebrew/index.html).

  ```shell
  $ /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
  ```

  Install docker runtime.

  ```shell
  $ brew install docker
  $ brew install colima
  $ colima start --cpu 2 --memory 4
  ```

## Useful commands

- (_Optional_) Test both components of the application

  ```shell
  $ ./gradlew clean check
  ```

- Build both components of the application using Gradle

  `package` is a custom Gradle task that creates both components' (thin) Jar
  files and copies all the dependencies into the `./build/libs/catalogue` and
  `./build/libs/cart` respective directories.

  ```shell
  $ ./gradlew clean package
  ```

  (_Optional_) List the created artefacts for the _Catalogue_ component.

  ```shell
  $ tree './build/libs/catalogue'
  ```

  There should be 70 JAR files including the `app.jar`.

  ```
  ...
  1 directory, 70 files
  ```

  (_Optional_) List the created artefacts for the _Cart_ component.

  ```shell
  $ tree './build/libs/cart'
  ```

  There should be 71 JAR files including the `app.jar`.

  ```
  ...
  1 directory, 71 files
  ```

- Run both components of the application

  The components are started in the background for convenience, and can be
  started in different terminal sessions if preferred.

  Run the _Catalogue_ component (in the background).

  ```shell
  $ java --class-path './build/libs/catalogue/*' demo.Main --server.port=8081 &
  ```

  Run the _Cart_ component (in the background).

  ```shell
  $ java --class-path './build/libs/cart/*' demo.Main --server.port=8082 &
  ```

  Note that both components are running on different ports.  Also, the _Cart_
  component is expecting the _Catalogue_ component to be listening on part
  `8081`.

  | Component   |   Port |
  | ----------- | -----: |
  | _Catalogue_ | `8081` |
  | _Cart_      | `8082` |

- Try the application

  Both components use an [H2](https://www.h2database.com/html/main.html)
  in-memory database that will always start with the following data.

  **Catalogue Item**

  |   id | caption       | description                                                                                |
  | ---: | ------------- | ------------------------------------------------------------------------------------------ |
  |    1 | Leather Sofa  | A very nice and comfortable sofa                                                           |
  |    2 | Wooden Table  | A large table ideal for 6 to 8 people                                                      |
  |    3 | Plastic Chair | A robust plastic chair ideal for children and adults alike                                 |
  |    4 | Mug           | The ideal way to start the day                                                             |
  |    5 | LED TV        | A very large TV set, ideal for those who like to bring TV shows and spend time watching TV |

  **Cart**

  |   id |
  | ---: |
  |    1 |
  |    2 |
  |    3 |

  **Cart Item**

  | cart_id | item_id | quantity |
  | ------: | ------: | -------: |
  |       1 |       1 |        1 |
  |       1 |       5 |        1 |
  |       2 |       2 |        1 |
  |       2 |       3 |        6 |
  |       3 |       4 |        4 |

  Fetch the catalogue item with id `6`, which does not exists prior to this
  demo.

  ```shell
  $ curl -v 'http://localhost:8081/catalogue/item/6'
  ```

  This will return a `404`, as the catalogue item with id `6` does not exists.

  ```
  ...
  < HTTP/1.1 404
  ...
  ```

  Fetch cart with id `3`.

  ```shell
  $ curl -v 'http://localhost:8082/cart/3' | jq
  ```

  This will return the details of the cart with id `3`.

  ```json
  {
    "id": 3,
    "items": [
      {
        "id": 4,
        "caption": "Mug",
        "quantity": 4
      }
    ]
  }
  ```

  Try to add the non-existent catalogue item with id `6` to an existing cart,
  with id `3`.

  ```shell
  $ curl -v -X POST 'http://localhost:8082/cart/3/item/6'
  ```

  This will return a `404`, as the catalogue item with id `6` does not exists.

  ```
  ...
  < HTTP/1.1 404
  ...
  ```

  Create the catalogue item with id `6`. The ids are automatically generated by
  the database, which is primed with five catalogue items, with ids `1` to `5`.
  The next catalogue item will be assigned id `6` automatically.

  ```shell
  $ curl -X POST 'http://localhost:8081/catalogue/item' \
    -H 'Content-Type: application/json' \
    -d '{"caption":"Green Plant","description":"Put a little life in your living room!!"}' \
    | jq
  ```

  The new catalogue item will added and its details returned.

  ```json
  {
    "id": 6,
    "caption": "Green Plant",
    "description": "Put a little life in your living room!!"
  }
  ```

  Confirm that the catalogue item was added to tha _Catalogue_ component.

  ```shell
  $ curl 'http://localhost:8081/catalogue/item/6' | jq
  ```

  This will return the catalogue item details.

  ```json
  {
    "id": 6,
    "caption": "Green Plant",
    "description": "Put a little life in your living room!!"
  }
  ```

  Try to add the new catalogue item to the cart with id `3`, again.

  ```shell
  $ curl -X POST 'http://localhost:8082/cart/3/item/6' | jq
  ```

  This time it will succeed and return the new cart details.

  ```json
  {
    "id": 3,
    "items": [
      {
        "id": 4,
        "caption": "Mug",
        "quantity": 4
      },
      {
        "id": 6,
        "caption": "Green Plant",
        "quantity": 1
      }
    ]
  }
  ```

- Stop both application components

  Both components were started in the background. Print the components that are
  running in the background.

  ```shell
  $ jobs
  ```

  This will print both components.

  ```
  [1]  - running    java --class-path './build/libs/catalogue/*' demo.Main --server.port=8081
  [2]  + running    java --class-path './build/libs/cart/*' demo.Main --server.port=8082
  ```

  Bring the _Cart_ component (the last one on the list) to the foreground.

  ```shell
  $ fg
  ```

  ```
  [2]  - 70334 running    java --class-path './build/libs/cart/*' demo.Main --server.port=8082
  ```

  Then press `[CTRL]` + `[c]` to stop the _Cart_ component. Once stopped, bring
  the _Catalogue_ component to the foreground.

  ```shell
  $ fg
  ```

  ```
  [1]  - 70288 running    java --class-path './build/libs/catalogue/*' demo.Main --server.port=8081
  ```

  Then press `[CTRL]` + `[c]` to stop the _Catalogue_ component. Both components
  are not stopped.

  (_Optional_) Confirm that no jobs are running in the background.

  ```shell
  $ jobs
  ```

  This will print nothing.
