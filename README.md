# spring-data-web-spec

Annotation-based mapping from **web requests** (query parameters, headers, path variables, and security context) to **Spring Data JPA `Specification`s**.

- Declare filters directly in controller method parameters
- Integrates seamlessly with **Spring MVC** and **Spring Data JPA**
- Extensible **operator** model (`Equal`, `In`, `ContainsIgnoreCase`, `IsNull`, etc.)
- Built-in **access control** support via `@Spec.AccessControl` for security-based filters
- Minimal runtime dependencies

---

## Installation

**Maven**

```xml

<dependency>
    <groupId>com.cariochi.spec</groupId>
    <artifactId>spring-data-web-spec</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Quick start

### Configuration

Register the argument resolver in your MVC configuration:

```java

@Configuration
@RequiredArgsConstructor
class WebConfig implements WebMvcConfigurer {

    private final AutowireCapableBeanFactory beanFactory;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SpecArgumentResolver(beanFactory));
    }
}
```

### Example Usage

```java

@RestController
@RequiredArgsConstructor
class DummyController {

    private final DummyService service;

    @GetMapping("/organizations/{organizationId}/dummy")
    public List<DummyEntity> findAll(
            @Spec.PathVariable(name = "organizationId", path = "organization.id", required = true)
            @Spec.RequestParam(name = "id")
            @Spec.RequestParam(name = "status", operator = In.class)
            @Spec.RequestParam(name = "name", path = "name", operator = ContainsIgnoreCase.class)
            @Spec.RequestHeader(name = "region", path = "organization.region")
            @Spec.AccessControl(path = "organization.region", valueSupplier = AllowedRegions.class, operator = In.class)
            Specification<DummyEntity> specification
    ) {
        return service.findAll(specification);
    }
}
```

## How it works

1. Annotate a Specification<T> method parameter with one or more source annotations:
   • @Spec.RequestParam — query parameter
   • @Spec.RequestHeader — HTTP header
   • @Spec.PathVariable — URI path variable
   • @Spec.AccessControl — security-based, no request value needed

2. SpecArgumentResolver:
   • Reads the values from the HTTP request or security context.
   • Converts them using Spring’s ConversionService.
   • Passes them to the chosen operator.

3. An operator implements:

   ```java
   interface SpecOperator<T, Y, V> {
     Specification<T> buildSpecification(SpecContext<T, Y, V> ctx);
   }
   ```

4. SpecContext provides:
   • JPA path resolution: `ctx.path(root)`
   • Type-safe value conversion: `ctx.valueOf(...)`, `ctx.collectionOf(...)`

5. All generated specifications are combined with AND logic.

---

## Built-in operators

- **Equal**, **NotEqual**
- **In**, **NotIn**
- **Contains**, **ContainsIgnoreCase**
- **StartWith**, **StartWithIgnoreCase**
- **IsNull**, **IsNotNull**
- **GreaterThan**, **GreaterThanOrEqualTo**, **LessThan**, **LessThanOrEqualTo**

You can also implement custom operators.

---

## Access control / security

Use `@Spec.AccessControl` for filters without request input, e.g., enforcing user-specific restrictions.

**Example:**

```java

@Component
@RequiredArgsConstructor
class AllowedRegions implements Supplier<List<String>> {

    private final UserService userService;

    @Override
    public List<String> get() {
        return userService.getAllowedRegions();
    }
}

@Spec.AccessControl(path = "organization.region", valueSupplier = AllowedRegions.class, operator = In.class)
```

This will automatically restrict queries to allowed regions for the current user.

---

## License

The library is licensed under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0). 
