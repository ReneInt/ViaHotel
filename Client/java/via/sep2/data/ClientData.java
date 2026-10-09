package via.sep2.data;

import dtos.Request;
import dtos.Response;
import dtos.dining.DiningRequestDto;
import dtos.person.PersonDataDto;
import dtos.reservation.ReservationRoomTypeDto;
import javafx.application.Platform;
import types.PersonType;
import via.sep2.networking.SocketService;
import via.sep2.startup.ViewHandler;
import via.sep2.ui.popup.MessageType;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mario
 */
public class ClientData implements PropertyChangeListener
{
  private static final PropertyChangeSupport support = new PropertyChangeSupport(ClientData.class);
  private static List<ReservationRoomTypeDto> reservations = Collections.synchronizedList(new ArrayList<>());
  private static List<DiningRequestDto> dining = Collections.synchronizedList(new ArrayList<>());
  private static List<PersonDataDto> people = Collections.synchronizedList(new ArrayList<>());
  private static SocketService socketService;
  private static boolean initialized = false;

  private ClientData() {
    // Prevent instantiation
  }

  public static synchronized void initialize(SocketService service)
  {
    if (!initialized)
    {
      socketService = service;
      socketService.addPropertyChangeListener(new ClientData());
      initialized = true;
    }
  }

  public static synchronized List<ReservationRoomTypeDto> getReservations()
  {
    return new ArrayList<>(reservations);
  }

  public static synchronized List<DiningRequestDto> getDining()
  {
    return new ArrayList<>(dining);
  }

  public static synchronized List<PersonDataDto> getPeople()
  {
    return new ArrayList<>(people);
  }

  public static synchronized void refreshAllData()
  {
      refreshReservations();
      refreshDining();
      refreshPeople();
  }

  public static synchronized void refreshReservations()
  {
    try
    {
      Object result = socketService.sendRequest(new Request("reservation", "getAll", null));
      if (result instanceof List)
      {
        reservations = Collections.synchronizedList(new ArrayList<>((List<ReservationRoomTypeDto>) result));
        synchronized (support) {
          support.firePropertyChange("reservationUpdate", null, new ArrayList<>(reservations));
        }
      }
    }
    catch (RuntimeException e)
    {
      Platform.runLater(()->ViewHandler.popupMessage(MessageType.WARNING,"Failed to update reservations with the new data."));
    }
  }
  public static synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }
  public static synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
    support.removePropertyChangeListener(listener);
  }

  public static synchronized void refreshDining()
  {
    try
    {
      Object result = socketService.sendRequest(new Request("dining", "getAll", null));
      if (result instanceof List)
      {
        dining = Collections.synchronizedList(new ArrayList<>((List<DiningRequestDto>) result));
        synchronized (support) {
          support.firePropertyChange("diningUpdate", null, new ArrayList<>(dining));
        }
      }
    }
    catch (RuntimeException e)
    {
      Platform.runLater(()->ViewHandler.popupMessage(MessageType.WARNING,"Failed to update dining with the new data."));
    }
  }

  public static synchronized void refreshPeople()
  {
    try
    {
      Object result = socketService.sendRequest(new Request("person", "getAll", null));
      if (result instanceof List)
      {
        people = Collections.synchronizedList(new ArrayList<>((List<PersonDataDto>) result));
        synchronized (support) {
          support.firePropertyChange("peopleUpdate", null, new ArrayList<>(people));
        }
      }
    }
    catch (RuntimeException e)
    {
      Platform.runLater(()->ViewHandler.popupMessage(MessageType.WARNING,"Failed to update people with the new data."));
    }
  }

  public static void sendNotification(String message)
  {
    synchronized (support) {
      support.firePropertyChange("notification", null, message);
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt)
  {
    if ("responseReceived".equals(evt.getPropertyName()))
    {
      Object responseObj = evt.getNewValue();
      if (responseObj instanceof Response response)
      {
        if ("UPDATE".equals(response.status()))
        {
          String entityType = (String) response.payload();
          switch (entityType)
          {
            case "reservations":
              refreshReservations();
              break;
            case "dining":
              refreshDining();
              break;
            case "person":
              refreshPeople();
              break;
            default:
              ViewHandler.popupMessage(MessageType.WARNING,
                  "Unknown type of data sent by the server" + entityType);
          }
        }
        else if ("notification".equals(response.status()) && response.payload() instanceof String)
        {
          sendNotification((String) response.payload());
        }
      }
    }
    else if ("connectionError".equals(evt.getPropertyName()))
    {
      synchronized (support)
      {
        support.firePropertyChange("connectionError", null, evt.getNewValue());
      }
    }
  }
  public static List<PersonDataDto> getCustomers()
  {
    List<PersonDataDto> customers = new ArrayList<>();
    List<PersonDataDto> temp = ClientData.getPeople();
    for(PersonDataDto customer: temp)
    {
      if(customer.position().equals(PersonType.CUSTOMER))
      {
        customers.add(customer);
      }
    }
    return customers;
  }
  public static List<PersonDataDto> getEmployees()
  {
    List<PersonDataDto> employees = new ArrayList<>();
    List<PersonDataDto> temp = ClientData.getPeople();
    for(PersonDataDto employee: temp)
    {
      if(!employee.position().equals(PersonType.CUSTOMER))
      {
        employees.add(employee);
      }
    }
    return employees;
  }
}