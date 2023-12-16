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
import { loginAction, profileSelfAction, signupAction } from "./actions";
import { homeLoader, profileLoader } from "./loaders";
import { isError } from "./api/dto";
import { Feed } from "./Feed";
import { Messages } from "./Messages";
import { Profile } from "./Profile";
import { getSelf } from "./api/Person";

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
            return await homeLoader();
        },
        element: <Home />,
        children: [
            { path: "feed", element: <Feed /> },
            { path: "messages", element: <Messages /> },
            {
                path: "profile",
                loader: profileLoader,
                action: profileSelfAction,
                element: <Profile self={true} />,
            },
            {
                path: "profile/:username",
                loader: profileLoader,
                // action: profileSelfAction,
                element: <Profile self={false} />,
            },
        ],
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
