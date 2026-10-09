package dtos;

import java.io.Serializable;
/**
 * @author Troels
 */
public record Request(String handler, String action, Object payload) implements Serializable
{
}