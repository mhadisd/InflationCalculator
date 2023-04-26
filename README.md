This code includes several Java classes that serve different purposes. Below is a brief description of each class and its purpose.

Login:
This class is an implementation of a simple login screen using JavaFX. It connects to a MySQL database to validate user credentials and to add new users. It has an overridden method called start() which is responsible for setting up the JavaFX user interface, including the login screen layout, title, and size. It also has a few private helper methods to validate user credentials, add new users to the database, and hash passwords using SHA-256 algorithm.

The start() method sets up the login screen using a JavaFX scene graph that is created from an FXML file called "login.fxml". It loads the FXML file, sets up a LoginController instance to handle the UI interactions, and creates the parent container for the UI elements. It then sets the title, scene, and stage dimensions for the login screen, and displays it on the user's screen.

LoginController:
This class is the controller for the login screen implemented in the Login class. It defines the behavior of the UI elements in the login screen, including the username and password text fields, the login and create account buttons, and the alert dialogs that display messages to the user. It has a few getter methods to access the UI elements for event handling in the Login class.

InflationAPI:
This class retrieves the inflation rate for the previous month from an external API. It calculates the inflation rate based on the consumer price index (CPI) in the United States. The API call is made using the URL of the API endpoint, which includes the start and end dates of the period for which the inflation rate is being calculated. It returns the inflation rate as a double.

MatchSearchController:
This class is the controller for the match search screen implemented in the MatchSearch class. It defines the behavior of the UI elements in the match search screen, including the search text field, the search and add buttons, the results and selected products list views, and the total cost label. It has a few getter methods to access the UI elements for event handling in the MatchSearch class.

MatchSearch:
This class implements a simple match search screen using JavaFX. It connects to a MySQL database to search for products based on user input. It has an overridden method called start() which sets up the JavaFX user interface, including the match search screen layout, title, and size.

The start() method creates a MatchSearchController instance to handle the UI interactions, and sets up the parent container for the UI elements. It then sets the title, scene, and stage dimensions for the match search screen, and displays it on the user's screen. It also establishes a connection to the MySQL database using JDBC, and retrieves the inflation rate from the InflationAPI class.

The MatchSearch class implements event handling for the search, add, and remove buttons in the MatchSearchController class. When the user enters a search term and clicks the search button, the MatchSearch class searches the MySQL database for products that match the search term, and displays them in a list view. When the user selects a product from the list view and clicks the add button, the MatchSearch class adds the selected product to a selected products list view, and updates the total cost label to include the selected product's price multiplied by the inflation rate. When the user selects a product from the selected products list view and clicks the remove button, the MatchSearch class removes the selected product from the selected products list view, and updates the total cost label to exclude the removed product's price multiplied by the inflation rate.
