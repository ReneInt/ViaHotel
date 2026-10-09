package via.sep2.utilities.logging;

/**
 * Formating enum that has markdown formating supported
 */
public enum Formating
{
  HEADER1("#"),
  HEADER2("##"),
  HEADER3("###"),
  HEADER4("####"),
  HEADER5("#####"),
  HEADER6("######"),
  BOLD("**"),
  ITALIC("*"),
  STRIKETHROUGH("~~"),
  BLOCKQUOTE(">"),
  INLINE_CODE("`"),
  CODE_BLOCK("```"),
  UNORDERED_LIST("-"),
  ORDERED_LIST("1."),
  LINK("[text](url)"); // Special case, not just a symbol

  private final String symbol;

  Formating(String symbol) {
    this.symbol = symbol;
  }

  @Override
  public String toString() {
    return symbol + " ";
  }
}
