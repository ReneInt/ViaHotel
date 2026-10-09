package via.sep2.persistence.daos;

import dtos.dining.DiningRequestDto;
import dtos.person.PersonDataDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import types.PersonType;
import via.sep2.model.time.Date;
import via.sep2.model.time.DateImplementation;
import via.sep2.model.time.DateUtility;
import via.sep2.model.time.Hour;
import via.sep2.model.time.HourImplementation;
import via.sep2.model.time.HourUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DiningDAOImplTest {

  @Mock
  private Connection connection;

  @Mock
  private PreparedStatement preparedStatement;

  @Mock
  private ResultSet resultSet;

  @Mock
  private PersonDAOImpl personDAO;

  private MockedStatic<ConnectionUtil> connectionUtilMockedStatic;

  @BeforeEach
  void setUp() throws SQLException, NoSuchFieldException, IllegalAccessException {
    // Initialize mocks
    MockitoAnnotations.openMocks(this);

    // Mock the static ConnectionUtil.getConnection() to return the mocked connection
    connectionUtilMockedStatic = mockStatic(ConnectionUtil.class);
    connectionUtilMockedStatic.when(ConnectionUtil::getConnection).thenReturn(connection);

    // Mock the prepared statement creation for all SQL queries
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);

    // Reset the singleton instances of DiningDAOImpl and PersonDAOImpl to ensure test isolation
    resetSingleton(DiningDAOImpl.class, "instance");
    resetSingleton(PersonDAOImpl.class, "instance");

    // Mock the PersonDAOImpl singleton to return the mocked instance
    PersonDAOImpl personDAOInstance = mock(PersonDAOImpl.class);
    setSingleton(PersonDAOImpl.class, "instance", personDAOInstance);
    this.personDAO = personDAOInstance;
  }

  @AfterEach
  void tearDown() throws NoSuchFieldException, IllegalAccessException {
    // Close the static mock to avoid interference with other tests
    if (connectionUtilMockedStatic != null) {
      connectionUtilMockedStatic.close();
    }

    // Reset the singleton instances after each test
    resetSingleton(DiningDAOImpl.class, "instance");
    resetSingleton(PersonDAOImpl.class, "instance");
  }

  // Helper method to reset a singleton instance using reflection
  private void resetSingleton(Class<?> clazz, String fieldName) throws NoSuchFieldException, IllegalAccessException {
    Field instanceField = clazz.getDeclaredField(fieldName);
    instanceField.setAccessible(true);
    instanceField.set(null, null);
  }

  // Helper method to set a singleton instance using reflection
  private void setSingleton(Class<?> clazz, String fieldName, Object instance) throws NoSuchFieldException, IllegalAccessException {
    Field instanceField = clazz.getDeclaredField(fieldName);
    instanceField.setAccessible(true);
    instanceField.set(null, instance);
  }

  @Test
  void getInstance() throws SQLException {
    // Act
    DiningDAOImpl instance1 = DiningDAOImpl.getInstance();
    DiningDAOImpl instance2 = DiningDAOImpl.getInstance();

    // Assert
    assertSame(instance1, instance2, "Should return the same instance (singleton)");
  }

  @Test
  void create() throws SQLException {
    // Arrange
    Date date = new DateImplementation(2025, 6, 11);
    Hour hour = new HourImplementation(12, 0);
    String email = "test@example.com";
    PersonDataDto customer = new PersonDataDto("John", "Doe", email,PersonType.CUSTOMER);

    // Mock the PERSON table lookup
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getString("first_name")).thenReturn("John");
    when(resultSet.getString("last_name")).thenReturn("Doe");
    when(personDAO.readByEmail(email)).thenReturn(customer);

    // Mock the INSERT into dining table
    when(preparedStatement.executeUpdate()).thenReturn(1);
    when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getInt(1)).thenReturn(1);

    // Act
    DiningRequestDto dining = DiningDAOImpl.getInstance().create(date, hour, email);

    // Assert
    assertNotNull(dining, "Dining object should be created");
    assertEquals(1, dining.id(), "Dining ID should be 1");
    verify(preparedStatement).setDate(eq(1), any());
    verify(preparedStatement).setTime(eq(2), any());
    verify(preparedStatement).setString(3, email.toLowerCase());
  }

  @Test
  void createCustomerNotFound() throws SQLException {
    // Arrange
    Date date = new DateImplementation(2025, 6, 11);
    Hour hour = new HourImplementation(12, 0);
    String email = "test@example.com";

    // Mock the PERSON table lookup to return no result
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);
    when(personDAO.readByEmail(email)).thenReturn(null);

    // Act & Assert
    assertThrows(SQLException.class, () -> DiningDAOImpl.getInstance().create(date, hour, email),
        "Should throw SQLException if customer not found");
  }

  @Test
  void cancel() throws SQLException {
    // Arrange
    int orderId = 1;
    Date futureDate = new DateImplementation(2025, 5, 10); // Future date relative to May 9, 2025
    Hour hour = new HourImplementation(12, 0);
    PersonDataDto customer = new PersonDataDto("John", "Doe", "test@example.com",PersonType.CUSTOMER);

    // Mock readByOrderId (used in checkCancel)
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getInt("order_id")).thenReturn(orderId);
    when(resultSet.getString("email")).thenReturn("test@example.com");
    when(resultSet.getDate("date")).thenReturn(new java.sql.Date(DateUtility.toJavaUtilDate((DateImplementation) futureDate).getTime()));
    when(resultSet.getTime("hour")).thenReturn(HourUtil.toSqlTime((HourImplementation) hour));
    when(personDAO.readByEmail("test@example.com")).thenReturn(customer);

    // Mock the DELETE statement
    when(preparedStatement.executeUpdate()).thenReturn(1);

    // Act
    DiningDAOImpl.getInstance().cancel(orderId);

    // Assert
    verify(preparedStatement, times(2)).setInt(1, orderId); // Once for SELECT, once for DELETE
    verify(preparedStatement).executeUpdate();
  }

  @Test
  void cancelExpiredWindow() throws SQLException {
    // Arrange
    int orderId = 1;
    Date pastDate = new DateImplementation(2025, 5, 1); // Past date relative to May 9, 2025
    Hour hour = new HourImplementation(12, 0);
    PersonDataDto customer = new PersonDataDto("John", "Doe", "test@example.com",PersonType.CUSTOMER);

    // Mock readByOrderId (used in checkCancel)
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getInt("order_id")).thenReturn(orderId);
    when(resultSet.getString("email")).thenReturn("test@example.com");
    when(resultSet.getDate("date")).thenReturn(new java.sql.Date(DateUtility.toJavaUtilDate((DateImplementation) pastDate).getTime()));
    when(resultSet.getTime("hour")).thenReturn(HourUtil.toSqlTime((HourImplementation) hour));
    when(personDAO.readByEmail("test@example.com")).thenReturn(customer);

    // Act & Assert
    assertThrows(SQLException.class, () -> DiningDAOImpl.getInstance().cancel(orderId),
        "Should throw SQLException if cancel window expired");
  }

  @Test
  void readByEmail() throws SQLException {
    // Arrange
    String email = "test@example.com";
    Date date = new DateImplementation(2025, 6, 11);
    Hour hour = new HourImplementation(12, 0);
    PersonDataDto customer = new PersonDataDto("John", "Doe", email, PersonType.CUSTOMER);

    // Mock the SELECT query for dining table
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true, false); // One result
    when(resultSet.getInt("order_id")).thenReturn(1);
    when(resultSet.getString("email")).thenReturn(email);
    when(resultSet.getDate("date")).thenReturn(new java.sql.Date(DateUtility.toJavaUtilDate((DateImplementation) date).getTime()));
    when(resultSet.getTime("hour")).thenReturn(HourUtil.toSqlTime((HourImplementation) hour));
    when(personDAO.readByEmail(email)).thenReturn(customer);

    // Act
    List<DiningRequestDto> diningList = DiningDAOImpl.getInstance().readByEmail(email);

    // Assert
    assertEquals(1, diningList.size(), "Should return one dining reservation");
    DiningRequestDto resultDining = diningList.get(0);
    assertEquals(1, resultDining.id(), "Dining ID should be 1");
    verify(preparedStatement).setString(1, "%" + email.toLowerCase() + "%");
  }

  @Test
  void readByOrderId() throws SQLException {
    // Arrange
    int orderId = 1;
    String email = "test@example.com";
    Date date = new DateImplementation(2025, 6, 11);
    Hour hour = new HourImplementation(12, 0);
    PersonDataDto customer = new PersonDataDto("John", "Doe", email,PersonType.CUSTOMER);

    // Mock the SELECT query
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getInt("order_id")).thenReturn(orderId);
    when(resultSet.getString("email")).thenReturn(email);
    when(resultSet.getDate("date")).thenReturn(new java.sql.Date(DateUtility.toJavaUtilDate((DateImplementation) date).getTime()));
    when(resultSet.getTime("hour")).thenReturn(HourUtil.toSqlTime((HourImplementation) hour));
    when(personDAO.readByEmail(email)).thenReturn(customer);

    // Act
    DiningRequestDto resultDining = DiningDAOImpl.getInstance().readByOrderId(orderId);

    // Assert
    assertNotNull(resultDining, "Dining object should be returned");
    assertEquals(orderId, resultDining.id(), "Dining ID should be 1");
    verify(preparedStatement).setInt(1, orderId);
  }

  @Test
  void readByOrderIdNotFound() throws SQLException {
    // Arrange
    int orderId = 1;

    // Mock the SELECT query to return no result
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);

    // Act & Assert
    assertThrows(SQLException.class, () -> DiningDAOImpl.getInstance().readByOrderId(orderId),
        "Should throw SQLException if order not found");
  }
}

