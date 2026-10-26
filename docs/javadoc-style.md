# Guía de estilo Javadoc

Esta guía define cómo se documenta el código Java del proyecto. Aplica a todo el código bajo
`src/main/java`.

## Idioma y tono

Español técnico, neutro e impersonal. Sin primera persona ni coloquialismos. Se abrevia como
"p. ej.".

## Primera frase

Verbo en tercera persona del presente ("Busca…", "Crea…", "Calcula…", "Traduce…"). Debe ser breve,
completa y terminar en punto. Se añade una segunda frase solo para explicar una regla de negocio o
un efecto. No se usa infinitivo ni imperativo.

## Clases

Sustantivo descriptivo ("Representa…", "Contiene la lógica de negocio de…", "Expone…", "Filtro
que…"). Los enums y las entidades simples llevan una sola línea:
`/** Representa un rol que puede asignarse a un {@link User}. */`

## El "por qué"

Solo cuando la decisión no es obvia, en un párrafo aparte con `<p>`. Los métodos triviales llevan
una sola frase.

## Métodos

- Descripción, línea en blanco, luego los tags.
- `@param nombre descripción`: en minúscula, sin punto final, como sintagma nominal.
- `@return descripción`: en minúscula, sin punto final, con artículo ("el usuario encontrado").
- Si la descripción de un tag ocupa más de una línea, la continuación lleva 4 espacios extra.

## Marcado

- `{@link Tipo}` y `{@link #CONSTANTE}` para referencias.
- `{@code …}` para literales: roles, cabeceras, estados, códigos HTTP.
- Se cita el estándar cuando aplique (p. ej. "RFC 9457").

## Seguridad en recursos REST

Los métodos de un `*Resource` restringidos por rol terminan la primera frase con
"Requiere rol `{@code X}`." (p. ej. `@RolesAllowed("Admin")` se documenta como "Requiere rol
{@code Admin}."). Los métodos sin `@RolesAllowed` no mencionan rol. El `@return` describe el
resultado HTTP ("respuesta 201 con la cabecera {@code Location} del usuario creado").

## Cobertura

Clases, métodos públicos, métodos privados y `@Override` cuando aportan contexto.

## Formato

El texto se ajusta a unas 100 columnas (estilo google-java-format). Se permiten cortes dentro de
`{@link …}` y `{@code …}`.

## Excepciones y metadatos

- `@throws`: solo para excepciones de dominio, es decir, las que el código lanza de forma
  deliberada para señalar el incumplimiento de una regla de negocio. En este proyecto son
  `BadRequestException` y `NotFoundException` lanzadas desde `UserService`, y
  `AuthenticationFailedException` lanzada desde `AuthService`. Se documentan en el método que las
  lanza y no en los recursos que las propagan. No se documentan excepciones de
  infraestructura ni de validación (p. ej. `ConstraintViolationException`).
- `@author`: no se usa.
- `@since`: no se usa.

El orden de los tags es `@param`, `@return`, `@throws`. La descripción de `@throws` sigue la misma
regla que los demás tags: minúscula y sin punto final.

## Ejemplos

### Servicio: clase, método público y método privado

```java
/**
 * Contiene la lógica de negocio de {@link User}: consulta por identificador o nombre de usuario,
 * alta validando la unicidad de usuario y correo, actualización de datos, baja lógica y cambio de
 * contraseña.
 */
@ApplicationScoped
public class UserService {

  /**
   * Crea un usuario inactivo con la contraseña cifrada con bcrypt. El nombre de usuario y el
   * correo deben ser únicos.
   *
   * @param requestDto datos del usuario a crear
   * @return el usuario creado
   * @throws BadRequestException si el nombre de usuario o el correo ya están en uso
   */
  @Transactional
  public UserDto create(CreateUserRequestDto requestDto) { ... }

  /**
   * Busca un usuario por su identificador.
   *
   * @param userId identificador del usuario
   * @return el usuario encontrado
   * @throws NotFoundException si no existe un usuario con ese identificador
   */
  private @NonNull User findUserById(Long userId) { ... }
}
```

### Recurso REST: método con restricción de rol

```java
  /**
   * Crea un nuevo usuario. Requiere rol {@code Admin}.
   *
   * @param requestDto datos del usuario a crear
   * @return respuesta 201 con la cabecera {@code Location} del usuario creado
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @RolesAllowed("Admin")
  public RestResponse<Void> create(@Valid CreateUserRequestDto requestDto) { ... }
```

### Infraestructura: clase con el "por qué"

```java
/**
 * Mapeadores de excepciones que traducen los errores de la capa REST a respuestas JSON con el
 * formato de {@link AppExceptionDto}.
 *
 * <p>Las excepciones no controladas se responden con un mensaje genérico y código {@code 500} para
 * no exponer detalles internos al cliente; la traza se escribe únicamente en la salida de error.
 */
class ExceptionMapper { ... }
```

### Entidad simple

```java
/** Representa un rol que puede asignarse a un {@link User}. */
@Entity
@Table(name = "roles")
public class Role { ... }
```
