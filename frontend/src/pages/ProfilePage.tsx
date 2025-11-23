import React, { useContext, useState } from "react";
import { AuthContext } from "../context/AuthContext";
import { ProfileForm } from "../components/Forms/ProfileForm";
import { ApiStatusDisplay, type ApiError } from "../components/ApiErrorDisplay";
import type { CreateUser, User } from "../types/User";
import { changePassword, updateUser } from "../api/users";
import { RegisterForm } from "../components/Forms/RegisterForm";

export const ProfilePage: React.FC = () => {
    const { id, username, email, role, cityName, restaurantId, status, updateMe } = useContext(AuthContext);
    const [updateUserError, setUpdateUserError] = useState<ApiError | null>(null);
    const [updateUserSuccess, setUpdateUserSuccess] = useState<string | null>(null);

    const [changePasswordError, setChangePasswordError] = useState<ApiError | null>(null);
    const [changePasswordSuccess, setChangePasswordSuccess] = useState<string | null>(null);

    if (!id || !username || !email) return <p>Not logged in</p>;
    const handleUpdate = async (data: Partial<User>) => {
        setUpdateUserError(null);
        setUpdateUserSuccess(null);
        try {
            await updateUser(data);
            await updateMe();
            setUpdateUserSuccess("Profile updated successfully!");
        } catch (err: any) {
            setUpdateUserError(err);
        }
    };

    const handleChangePassword = async (data: CreateUser) => {
        setChangePasswordError(null);
        setChangePasswordSuccess(null);
        try {
            await changePassword(id, data);
            setChangePasswordSuccess("Password updated successfully!");
        } catch (err: any) {
            setChangePasswordError(err);
        }
    };

    return (
        <div className="page">
            <div className="subpage">
                <h2>User Info</h2>
                <p><strong>Username:</strong> {username}</p>
                <p><strong>Email:</strong> {email}</p>
                <p><strong>Role:</strong> {role}</p>
                <p><strong>Status:</strong> {status}</p>
                {cityName && <p><strong>City:</strong> {cityName}</p>}
                {role == "EMPLOYEE" && <p><strong>Restaurant:</strong> {restaurantId || "None"}</p>}

            </div>
            <div className="subpage">
                <h2> Change Profile </ h2>
                <ProfileForm initialData={{ username, email }} onSubmit={handleUpdate} />
                <ApiStatusDisplay error={updateUserError} success={updateUserSuccess} />

                <h2> Change Password </ h2>
                <RegisterForm onSubmit={handleChangePassword} initialData={{ username: username || "", email: email || "", password: "" }} />
                <ApiStatusDisplay error={changePasswordError} success={changePasswordSuccess} />
            </div>
        </div>
    );
};
