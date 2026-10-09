package dtos.time;

import java.io.Serializable;

public record DateDto(int year, int month, int day) implements Serializable
{
  @Override public String toString(){
  return ""+year+'-'+month+'-'+day;
  }
}
