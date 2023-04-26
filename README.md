README
This code includes several Java classes that serve different purposes. Below is a brief description of each class and its purpose.

Login:
This class implements a simple login screen using JavaFX. It connects to a MySQL database to validate user credentials and to add new users. When the user logs in or creates a new account, a message is displayed using JavaFX's Alert class.

LoginController:
This class is the controller for the login screen implemented in the Login class. It defines the behavior of the UI elements in the login screen.

InflationAPI:
This class retrieves the inflation rate for the previous month from an external API. It calculates the inflation rate based on the consumer price index (CPI) in the United States.

MatchSearchController:
This class is the controller for the match search screen implemented in the MatchSearch class. It defines the behavior of the UI elements in the match search screen.

MatchSearch:
This class implements a simple match search screen using JavaFX. It connects to a MySQL database to search for products based on user input. The price of the selected product is displayed, and the user can add it to a list of selected products. The total cost of the selected products is displayed, taking into account the inflation rate retrieved from the InflationAPI class.

Overall, this code demonstrates the use of JavaFX for implementing simple user interfaces, as well as the use of external APIs and databases in Java applications.
