package via.sep2.networking;

/**
 * @author Joseph, Mario, Rodrigo, Rene, Tomasz, Bartosz
 */
public interface ReadWrite
{
  void acquireRead();
  void releaseRead();
  void acquireWrite();
  void releaseWrite();
}
