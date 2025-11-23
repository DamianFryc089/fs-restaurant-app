import React, { useContext, useEffect, useState } from "react";
import { UserCard } from "../components/Cards/UserCard";
import type { User, UserFilters } from "../types/User";
import { useLocation } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { UserForm } from "../components/Forms/UserForm";
import type { Restaurant } from "../types/Restaurant";
import { changeStatusOfUser, fetchUsersFiltered, updateUserAdmin } from "../api/users";
import { ApiStatusDisplay, type ApiError } from "../components/ApiErrorDisplay";

export const UsersPage: React.FC = () => {
	const location = useLocation();
	const restaurant: Restaurant = location.state?.restaurant ?? null;
	const { } = useContext(AuthContext);

	const [users, setUsers] = useState<User[]>([]);
	const [selectedUser, setSelectedUser] = useState<User | null>(null);
	const [updateUserSuccess, setUpdateUserSuccess] = useState<string | null>(null);
	const [updateUserError, setUpdateUserError] = useState<ApiError | null>(null);

	useEffect(() => {
		loadUsers();
	}, [restaurant]);

	const loadUsers = async () => {
		if (restaurant) {
			const filter: UserFilters = { restaurantId: restaurant.id }
			const data = await fetchUsersFiltered(filter);
			setUsers(data);
			return;
		}

		fetchUsersFiltered().then(setUsers);
	}

	const handleBlockUser = async (user: User) => {
		user.status = "BLOCKED";
		await changeStatusOfUser(user);
		loadUsers();
	}

	const handleActivateUser = async (user: User) => {
		user.status = "ACTIVE";
		await changeStatusOfUser(user);
		await loadUsers();
	}
	const handleSelectUser = (user: User) => {
		setSelectedUser(user);
	}

	const handleUserUpdate = async (userData: Partial<User>) => {
		setUpdateUserError(null);
		setUpdateUserSuccess(null);
		try {
			await updateUserAdmin(userData);
			setUpdateUserSuccess("User updated successfully!");
			loadUsers();
		} catch (err: any) {
			setUpdateUserError(err);
		}
	};

	return (
		<div className="page">
			<div className="subpage">
				{restaurant && <h2>Employees at {restaurant.name}</h2>}
				{!restaurant && <h2>Available Users</h2>}
				{users.map((o) => (
					<UserCard key={o.id} user={o} onBlockUser={handleBlockUser} onActivateUser={handleActivateUser} onSelectUser={handleSelectUser} />
				))}
			</div>
			<div className="subpage">
				<h2> Update User </ h2>
				<UserForm user={selectedUser} onSubmit={handleUserUpdate} />
				<ApiStatusDisplay error={updateUserError} success={updateUserSuccess} />
			</div>
		</div>
	);
};