package dtos;

import java.io.Serializable;
/**
 * @author Troels
 */
public record Response(String status, Object payload) implements Serializable
{
}