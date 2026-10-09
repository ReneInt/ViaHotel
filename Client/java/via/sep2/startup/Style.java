package via.sep2.startup;

public enum Style
{
  LIGHT,
  DARK;

  @Override public String toString()
  {
    switch (this)
    {
      case LIGHT :
      return "../ui/style.css";
      case DARK:
        return "../ui/style_darkmode.css";
      case null, default:
      return "";
    }
  }
}
