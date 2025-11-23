import React, { useContext } from "react";
import type { City } from "../../types/City";
import { AuthContext } from "../../context/AuthContext";

interface Props {
  city: City;
  // onDeleteCity: (city: City) => void;
  onShowRestaurants: (city: City) => void;
  onShowOffers: (city: City) => void;
  onSelect: (city: City) => void;
}

export const CityCard: React.FC<Props> = ({ city, onShowRestaurants, onShowOffers, onSelect }) => {
  const { role, cityName } = useContext(AuthContext);

  return (
    <div className="card card-row">
      <div style={{ flex: 1 }}>
        <p>{city.name} - {city.postalCode}</p>
      </div>

      <div>
        <button onClick={() => onShowRestaurants(city)}>Show Restaurants</button>
        <button onClick={() => onShowOffers(city)}>Show Offers</button>
        {role && cityName != city.name && (<button onClick={() => onSelect(city)}>Select</button>)}
        {/* {role === "ADMIN" && (<button onClick={() => onDeleteCity(city)}>Delete</button>)} */}
      </div>
    </div>
  );
};