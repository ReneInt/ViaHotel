package dtos.error;

import java.io.Serializable;
/**
 * @author Troels
 */
public record ErrorResponse(String errorMessage) implements Serializable
{
}