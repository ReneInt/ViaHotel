package via.sep2.networking;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EventManager
{
  private static final PropertyChangeSupport support = new PropertyChangeSupport(EventManager.class);

  private EventManager() {
    // Prevent instantiation
  }

  public static void addPropertyChangeListener(PropertyChangeListener listener)
  {
    synchronized (support) {
      support.addPropertyChangeListener(listener);
    }
  }

  public static void removePropertyChangeListener(PropertyChangeListener listener)
  {
    synchronized (support) {
      support.removePropertyChangeListener(listener);
    }
  }

  public static void fireUpdate(String entityType)
  {
    synchronized (support) {
      support.firePropertyChange(entityType + "_update", null, entityType);
    }
  }
}