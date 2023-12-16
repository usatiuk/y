import React from "react";
import {
    createBrowserRouter,
    redirect,
    RouterProvider,
} from "react-router-dom";

import "./App.scss";
import { deleteToken, getToken } from "./api/utils";
import { Login } from "./Login";
import { Signup } from "./Signup";
import { Home } from "./Home";
import { loginAction, signupAction } from "./actions";
import { homeLoader } from "./loaders";
import { isError } from "./api/dto";

const router = createBrowserRouter([
    {
        path: "/",
        loader: async () => {
            if (getToken() == null) {
                return redirect("/login");
            } else {
                return redirect("/home");
            }
        },
    },
    {
        path: "/home",
        loader: async () => {
            if (getToken() == null) {
                return redirect("/login");
            }
            const ret = await homeLoader();
            if (isError(ret)) {
                deleteToken();
                return redirect("/");
            }
            return ret;
        },
        element: <Home />,
    },
    {
        path: "/login",
        element: <Login />,
        loader: async () => {
            if (getToken()) {
                return redirect("/");
            }
            return null;
        },
        action: loginAction,
    },
    {
        path: "/signup",
        element: <Signup />,
        loader: async () => {
            if (getToken()) {
                return redirect("/");
            }
            return null;
        },
        action: signupAction,
    },
]);

export function App() {
    return (
        <React.StrictMode>
            <div id={"appRoot"}>
                <RouterProvider router={router} />
            </div>
        </React.StrictMode>
    );
}
