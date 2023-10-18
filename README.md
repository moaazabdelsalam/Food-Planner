# Food Planner Android App

   <img src="./images/food_planner_home.jpg" alt="Home screenshot" width="216" height="480"> <img src="./images/food_planner_fav.jpg" alt="favorite screenshot" width="216" height="480">
   <img src="./images/food_planner_search.jpg" alt="favorite screenshot" width="216" height="480"> <img src="./images/food_planner_filter_by_name.jpg" alt="search screenshot" width="216" height="480">
   <img src="./images/food_planner_filter_by_category.jpg" alt="search screenshot" width="216" height="480"> <img src="./images/food_planner_filter_by_ingredient.jpg" alt="search screenshot" width="216" height="480">
   <img src="./images/food_planner_filter_by_country.jpg" alt="search screenshot" width="216" height="480"> <img src="./images/food_planner_fav.jpg" alt="favorite screenshot" width="216" height="480">
   <img src="./images/food_planner_plan.jpg" alt="plan screenshot" width="216" height="480">
   <img src="./images/food_planner_login.jpg" alt="login screenshot" width="216" height="480"> <img src="./images/food_planner_register.jpg" alt="register screenshot" width="216" height="480">

**Description:**
The Food Planner Android app is your culinary companion, designed to make meal planning and cooking a delightful experience. This app allows users to search for meals, access detailed recipes, plan their monthly meals, and more. Here's an overview of its features and the technologies used.

## Features

1. **Meal Search:**
   - Search for meals by name, ingredients, category, and area of origin.

      <img src="./images/food_planner_filter_by_name.jpg" alt="search screenshot" width="270" height="600"> <img src="./images/food_planner_filter_by_category.jpg" alt="search screenshot" width="270" height="600">
      <img src="./images/food_planner_filter_by_ingredient.jpg" alt="search screenshot" width="270" height="600"> <img src="./images/food_planner_filter_by_country.jpg" alt="search screenshot" width="270" height="600">


2. **Detailed Meal Information:**
   - View complete details for each meal, including ingredients and step-by-step instructions.
   - Watch instructional cooking videos for each meal.
     
      <img src="./images/food_planner_meal_details.gif" alt="details screenshot" width="240" height="426">


3. **Favorites:**
   - Add meals to your list of favorite dishes for quick access.

      <img src="./images/food_planner_fav.jpg" alt="favorite screenshot" width="270" height="600">


4. **Meal Planning:**
   - Plan your entire month by scheduling meals for specific days.

      <img src="./images/food_planner_plan.jpg" alt="plan screenshot" width="270" height="600">


5. **User Modes:**
   - The app supports both guest and registered user modes.
   - Some features are available exclusively to registered users.

      <img src="./images/food_planner_not_logged_in_message.jpg" alt="plan screenshot" width="270" height="600"> <img src="./images/food_planner_home_guest.jpg" alt="plan screenshot" width="270" height="600">


6. **Registration:**
   - Users can register using their email and password.
   - Users can also login with their existing account.
   - Alternatively, they can register quickly and securely using their Google account.

      <img src="./images/food_planner_login.jpg" alt="login screenshot" width="270" height="600"> <img src="./images/food_planner_register.jpg" alt="register screenshot" width="270" height="600">

4. **Offline Mode:**
   - Support ofline mode with limited features.

      <img src="./images/food_planner_no_network.jpg" alt="plan screenshot" width="270" height="600">

      
## Technologies Used

- **Programming Language:** Java
- **API Requests:** Retrofit
- **Data Source:** TheMealDB API
- **Navigation:** Navigation Component
- **Local Caching:** Room Database
- **Concurrency and Threading:** RxJava with Retrofit and Room
- **Animations:** Lottie for animations and splash screen
- **Authentication:** Firebase Auth and Realtime Database
- **Cloud Storage:** Firestore for local cache synchronization
- **Architecture Pattern:** MVP (Model-View-Presenter)
- **UI Design:** Google Material Design components
