import React, { useContext, useEffect, useState } from "react";
import { CityCard } from "../components/Cards/CityCard.tsx";
import type { City } from "../types/City.ts";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext.tsx";
import { CityForm } from "../components/Forms/CityForm.tsx";
import { createCity, fetchCities, setCityToUser } from "../api/cities.ts";
import { ApiStatusDisplay, type ApiError } from "../components/ApiErrorDisplay.tsx";

export const CitiesPage: React.FC = () => {
    const { id, role, updateMe } = useContext(AuthContext);
    const [cities, setCities] = useState<City[]>([]);
    const navigate = useNavigate();
    const [createCityError, setCreateCityError] = useState<ApiError | null>(null);
    const [createCitysuccess, setCreateCitySuccess] = useState<string | null>(null);


    useEffect(() => {
        loadCities();
    }, []);

    const loadCities = async () => {
        fetchCities().then(setCities);
    }

    const handleShowRestaurants = async (city: City) => {
        navigate(`/restaurants/city/${city.id}`, {
            state: { city }
        });
    };

    const handleShowOffers = async (city: City) => {
        navigate(`/offers/city/${city.id}`, {
            state: { city }
        });
    };

    const handleSelectCity = async (city: City) => {
        if (id) {
            await setCityToUser(city, id);
            await updateMe();
        }
    };

    const handleAddCity = async (city: Omit<City, "id">) => {
        setCreateCityError(null);
        setCreateCitySuccess(null);
        try {
            const newCity = await createCity(city);
            setCities(prev => [...prev, newCity]);
            setCreateCitySuccess("City created successfully!");
            loadCities();
        } catch (err: any) {
            setCreateCityError(err);
        }
    };

    return (
        <div className="page">
            <div className="subpage">
                <h2>Available Cities</h2>
                {cities.map((o) => (
                    <CityCard key={o.id} city={o} onShowRestaurants={handleShowRestaurants} onShowOffers={handleShowOffers} onSelect={handleSelectCity} />
                ))}
            </div>
            {role == "ADMIN" && (
                <div className="subpage">
                    <h2> Create City </ h2>
                    <CityForm onSubmit={handleAddCity} />
                    <ApiStatusDisplay error={createCityError} success={createCitysuccess}/>
                </div>
            )}
        </div>
    );
};