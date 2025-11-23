import React, { useState, useContext } from "react";
import { LoginForm } from "../components/Forms/LoginForm";
import { RegisterForm } from "../components/Forms/RegisterForm";
import { AuthContext } from "../context/AuthContext";
import { ApiStatusDisplay, type ApiError } from "../components/ApiErrorDisplay";
import { useNavigate } from "react-router-dom";
import { loginUser } from "../api/auth";
import type { AuthRequest } from "../types/AuthRequest";
import { createUser } from "../api/users";
import type { CreateUser } from "../types/User";

export const LoginPage: React.FC = () => {
  const { updateMe } = useContext(AuthContext);
  const [loginError, setLoginError] = useState<ApiError | null>(null);
  const [registerSuccess, setRegisterSuccess] = useState<string | null>(null);
  const [registerError, setRegisterError] = useState<ApiError | null>(null);
  const navigate = useNavigate();

  const handleLogin = async (data: AuthRequest) => {
    setLoginError(null);
    try {
      const res = await loginUser(data);
      if (res.status === "ok") {
        updateMe();
        navigate("/profile");
      }
    } catch (err: any) {
      setLoginError(err);
    }
  };

  const handleRegister = async (data: CreateUser) => {
    setRegisterSuccess(null);
    setRegisterError(null);
    try {
      await createUser(data);
      setRegisterSuccess("User registered successfully!");
    } catch (err: any) {
      setRegisterError(err);
    }
  };

  return (
    <div className="page">
      <div className="subpage">
        <h2>Log in</h2>
        <LoginForm onSubmit={handleLogin} />
        <ApiStatusDisplay error={loginError} success={null} />
      </div>
      <div className="subpage">
        <h2>Register</h2>
        <RegisterForm onSubmit={handleRegister}  initialData={null}/>
        <ApiStatusDisplay error={registerError} success={registerSuccess} />
      </div>
    </div>
  );
};
