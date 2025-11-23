import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Navbar } from "./components/Navbar/Navbar";
import { LoginPage } from "./pages/LoginPage";
import { RestaurantsPage } from "./pages/RestaurantsPage";
import { OffersPage } from "./pages/OffersPage";
import { OrdersPage } from "./pages/OrdersPage";
import { UsersPage } from "./pages/UsersPage";
import { ProfilePage } from "./pages/ProfilePage";
import { CitiesPage } from "./pages/CitiesPage";
import { LogoutPage } from "./pages/LogoutPage";

function App() {
  return (
    <Router>
      <Navbar />
      <div className="main-content">
        <Routes>

          <Route path="/restaurants" element={<RestaurantsPage />} />
          <Route path="/restaurants/:restaurantId" element={<RestaurantsPage />} />
          <Route path="/restaurants/city/:cityId" element={<RestaurantsPage />} />

          <Route path="/offers" element={<OffersPage />} />
          <Route path="/offers/restaurant/:restaurantId" element={<OffersPage />} />
          <Route path="/offers/city/:cityId" element={<OffersPage />} />
          <Route path="/offers/:offerId" element={<OffersPage />} />

          <Route path="/orders" element={<OrdersPage />} />

          <Route path="/users" element={<UsersPage />} />
          <Route path="/users/restaurant/:restaurantId" element={<UsersPage />} />
          <Route path="/cities" element={<CitiesPage />} />

          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/logout" element={<LogoutPage />} />
          <Route path="*" element={<OffersPage />} /> domyślnie
        </Routes>
      </div>
    </Router>
  );
}

export default App;
