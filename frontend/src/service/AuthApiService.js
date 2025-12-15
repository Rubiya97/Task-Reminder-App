import axios from "axios";

axios.defaults.withCredentials = true;

const BASE_URL = "http://localhost:8080";

export const loginApi = async (username, password) => {
    const loginData = { username, password };
    const response = await axios.post(`${BASE_URL}/public/login`, loginData, {
        withCredentials: true
    });
    return response.data;
};

export const registerApi = async (user) => {
    const response = await axios.post(`${BASE_URL}/public/signup`, user, {
        withCredentials: true
    });
    return response.data;
};

export const saveLoggedUser = (userObj) => {
    sessionStorage.setItem("loggedUser", JSON.stringify(userObj));
};

export const getLoggedUser = () => {
    const data = sessionStorage.getItem("loggedUser");
    return data ? JSON.parse(data) : null;
};

export const removeLoggedUser = () => {
    sessionStorage.removeItem("loggedUser");
};

export const isUserLoggedIn = () => {
    //return getLoggedUser() !== null;
      return sessionStorage.getItem("loggedUser") !== null;
};

// Clear user from sessionStorage
export const clearLoggedUser = () => {
  sessionStorage.removeItem("loggedUser");
};
export const logout = async () => {
  try {
    await axios.post(`${BASE_URL}/logout`, {}, { withCredentials: true });
  } catch (err) {
    console.warn("Logout request failed. Still clearing local session.");
  }

  clearLoggedUser();
};

