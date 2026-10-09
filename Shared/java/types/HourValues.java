package types;

public enum HourValues
{
  EIGHT(8, 0), EIGHT_THIRTY(8, 30), NINE(9, 0), NINE_THIRTY(9, 30),
  TEN(10, 0), TEN_THIRTY(10, 30), ELEVEN(11, 0), ELEVEN_THIRTY(11, 30), TWELVE(12, 0),
  TWELVE_THIRTY(12, 30), THIRTEEN(13, 0), THIRTEEN_THIRTY(13, 30),
  FOURTEEN(14, 0), FOURTEEN_THIRTY(14, 30), FIFTEEN(15, 0), FIFTEEN_THIRTY(15, 30),
  SIXTEEN(16, 0), SIXTEEN_THIRTY(16, 30), SEVENTEEN(17, 0), SEVENTEEN_THIRTY(17, 30),
  EIGHTEEN(18, 0), EIGHTEEN_THIRTY(18, 30), NINETEEN(19, 0), NINETEEN_THIRTY(19, 30), TWENTY(20, 0);
  private final int hour;
  private final int minute;

 HourValues(int hour, int minute)
  {
    this.hour = hour;
    this.minute = minute;
  }

  public int getHour()
  {
    return hour;
  }

  public int getMinute()
  {
    return minute;
  }
  public static HourValues from(int hour, int minute) {
    for (HourValues hv : values()) {
      if (hv.getHour() == hour && hv.getMinute() == minute) {
        return hv;
      }
    }
    throw new IllegalArgumentException("No enum constant for " + hour + ":" + minute);
  }

  @Override public String toString()
  {
    return String.format("%02d:%02d", getHour(), getMinute());
  }

}
