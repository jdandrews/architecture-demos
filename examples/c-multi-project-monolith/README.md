# application-microkernel-service-provider-interfaces

```shell
$ ./gradlew clean check
```

```shell
$ ./gradlew clean package
```

```shell
$ tree './build/libs'
```

```
1 directory, 72 files
```

```shell
$ java --class-path './build/libs/*' demo.Main &
```

```shell
$ curl 'http://localhost:8080/catalogue/item/1' | jq
```

```json
{
  "id": 1,
  "caption": "Leather Sofa",
  "description": "A very nice and comfortable sofa"
}
```

```shell
$ curl 'http://localhost:8080/cart/1' | jq
```

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
