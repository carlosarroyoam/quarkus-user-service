package com.carlosarroyoam.userservice.core.exception.mapper;

import com.carlosarroyoam.userservice.core.exception.mapper.dto.AppExceptionDto;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

/**
 * Mapeadores de excepciones que traducen los errores de la capa REST a respuestas JSON con el
 * formato de {@link AppExceptionDto}.
 *
 * <p>Las excepciones no controladas se responden con un mensaje genérico y código {@code 500} para
 * no exponer detalles internos al cliente; la traza se escribe únicamente en la salida de error.
 */
class ExceptionMapper {
  private final UriInfo uriInfo;
  private final Clock clock;

  /**
   * Crea el mapeador con los datos de la petición y el reloj.
   *
   * @param uriInfo información de la URI de la petición en curso
   * @param clock reloj utilizado para la marca de tiempo del error
   */
  @Inject
  public ExceptionMapper(UriInfo uriInfo, Clock clock) {
    this.uriInfo = uriInfo;
    this.clock = clock;
  }

  /**
   * Traduce una {@link WebApplicationException} a una respuesta JSON que conserva su código HTTP.
   * El mensaje de la excepción se devuelve como {@code message}.
   *
   * @param ex excepción lanzada durante el procesamiento de la petición
   * @return la respuesta con el {@link AppExceptionDto} como cuerpo
   */
  @ServerExceptionMapper
  public Response mapWebApplicationException(WebApplicationException ex) {
    Status status = Status.fromStatusCode(ex.getResponse().getStatus());

    AppExceptionDto appExceptionDto =
        AppExceptionDto.builder()
            .message(ex.getMessage())
            .code(status.getStatusCode())
            .status(status.getReasonPhrase())
            .path(uriInfo.getPath())
            .timestamp(ZonedDateTime.now(clock))
            .build();

    return Response.status(status).entity(appExceptionDto).type(MediaType.APPLICATION_JSON).build();
  }

  /**
   * Traduce una {@link ConstraintViolationException} a una respuesta 400. Los mensajes de cada
   * violación se devuelven en {@code details}.
   *
   * @param ex excepción de validación de los datos de la petición
   * @return la respuesta con el {@link AppExceptionDto} como cuerpo
   */
  @ServerExceptionMapper
  public Response mapConstraintViolationException(ConstraintViolationException ex) {
    Status status = Status.BAD_REQUEST;

    AppExceptionDto appExceptionDto =
        AppExceptionDto.builder()
            .message("Request data is not valid")
            .code(status.getStatusCode())
            .status(status.getReasonPhrase())
            .path(uriInfo.getPath())
            .timestamp(ZonedDateTime.now(clock))
            .details(
                ex.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet()))
            .build();

    return Response.status(status).entity(appExceptionDto).type(MediaType.APPLICATION_JSON).build();
  }

  /**
   * Traduce cualquier excepción no controlada a una respuesta 500 con un mensaje genérico. La
   * traza de la excepción se escribe en la salida de error.
   *
   * @param ex excepción no controlada
   * @return la respuesta con el {@link AppExceptionDto} como cuerpo
   */
  @ServerExceptionMapper
  public Response mapException(Exception ex) {
    Status status = Status.INTERNAL_SERVER_ERROR;

    AppExceptionDto appExceptionDto =
        AppExceptionDto.builder()
            .message("Whoops! Something went wrong")
            .code(status.getStatusCode())
            .status(status.getReasonPhrase())
            .path(uriInfo.getPath())
            .timestamp(ZonedDateTime.now(clock))
            .build();

    ex.printStackTrace();

    return Response.status(status).entity(appExceptionDto).type(MediaType.APPLICATION_JSON).build();
  }
}
