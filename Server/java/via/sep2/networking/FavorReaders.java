package via.sep2.networking;

import via.sep2.services.exceptions.ValidationException;

/**
 * @author Joseph, Mario, Rodrigo, Rene, Tomasz, Bartosz
 * Singleton to ensure they are using the same instance of the object.
 */
public class FavorReaders implements ReadWrite {
  private int readers;
  private int writers;
  private static FavorReaders instance;

  private FavorReaders() {
    readers = 0;
    writers = 0;
  }

  public static FavorReaders getInstance() {
    if (instance == null) {
      synchronized (FavorReaders.class) {
        // MultiThread safety
        if (instance == null) {
          instance = new FavorReaders();
        }
      }
    }
    return instance;
  }

  @Override
  public synchronized void acquireRead() {
    while (writers > 0) { // Wait if there are active writers
      try {
        wait();
      } catch (InterruptedException e) {
        throw new ValidationException(e.getMessage());
      }
    }
    readers++; // Increment reader count
  }

  @Override
  public synchronized void releaseRead() {
    readers--; // Decrement reader count
    if (readers == 0) {
      notify(); // Notify a waiting writer when no readers remain
    }
  }

  @Override
  public synchronized void acquireWrite() {
    while (readers > 0 || writers > 0) { // Wait if there are active readers or writers
      try {
        wait();
      } catch (InterruptedException e) {
        throw new ValidationException(e.getMessage());
      }
    }
    writers++; // Increment writer count
  }

  @Override
  public synchronized void releaseWrite() {
    writers--; // Decrement writer count
    notifyAll(); // Notify all waiting threads (readers and writers)
  }
}