package via.sep2.utilities.logging;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MarkdownLogger implements Logger
{
  private FileWriter fileWriter;
  private static final String LOG_FILE_NAME = "Log.md";
  private static MarkdownLogger instance;//private static instance of the object
  /**
   * Logger class following the singleton pattern
   *<p> This makes a markdown file that stores logs
   * It is only appendable
   * </p>
   */
  private MarkdownLogger()
  {
    try
    {
      File file = new File(LOG_FILE_NAME);
      if (!file.exists())
      {
        file.createNewFile();
      }
      this.fileWriter = new FileWriter(file,
          true); // true to append, false to overwrite.
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Method to return the current instance of the logger
   * @return MarkdownLogger
   */
  public static MarkdownLogger getInstance()
  {
    if (instance == null)
    {
      synchronized (MarkdownLogger.class)
      {
        if (instance == null) //MultiThread safety
        {
          instance = new MarkdownLogger();
        }
      }
    }
    return instance;
  }

  /**
   * This method logs with date and time
   * Also logs to console for developers to see when exceptions happen in testing
   * @param message String
   */
  public void log(String message) {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" yyyy-MM-dd HH:mm:ss");
    String timestamp = now.format(formatter);
    try {
      message = timestamp + " - " + message; // Append timestamp after formatting
      fileWriter.write(message + "\n"); // Write to the file
      fileWriter.flush(); // Ensure data is written to the file
    } catch (IOException e) {
      System.err.println("Error writing to log file: " + e.getMessage() + '\n');
    }
    System.out.println(message); // Also log to console
  }

  /**
   * Function that formats with markdown styling
   * @param message String
   * @param formating Formating enum that has formating markdown styles
   */
  public void log(String message, Formating formating) {
    {
      LocalDateTime now = LocalDateTime.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" yyyy-MM-dd HH:mm:ss");
      String timestamp = now.format(formatter);
      try {
        message = timestamp + " - " + message; // Append timestamp after formatting
        fileWriter.write(formating.toString()+ message + "\n"); // Write to the file
        fileWriter.flush(); // Ensure data is written to the file
      } catch (IOException e) {
        System.err.println("Error writing to log file: " + e.getMessage() + '\n');
      }
      System.out.println(message); // Also log to console
    }
  }

  /**
   * Logger to log exceptions
   * It was designed that exceptions have header1 to be easy to see in the code
   * @param e Exception
   */
  public void log(Exception e) {
  {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" yyyy-MM-dd HH:mm:ss");
    String timestamp = now.format(formatter);
    String message=e.getMessage();
    try {
      message = timestamp + " - " + message; // Append timestamp after formatting
      fileWriter.write(Formating.HEADER1+ message + "\n"); // Write to the file
      fileWriter.flush(); // Ensure data is written to the file
    } catch (IOException ex) {
      System.err.println("Error writing to log file: " + ex.getMessage() + '\n');
    }
    System.out.println(message); // Also log to console
  }
}

  /**
   * Method logging client names when they are connected
   * @param name String
   */
  public void logNewUser(String name)
  {
    log("New user " + name,Formating.HEADER1);
  }
  public void close()
  {
    try
    {
      if (fileWriter != null)
      { // closing the fileWriter if it's not already closed
        fileWriter.close();
      }
    }
    catch (IOException e)
    {
      System.err.println("Error closing log file: " + e.getMessage());
    }
  }
}
