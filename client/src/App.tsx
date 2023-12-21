import React from "react";
import {
    createBrowserRouter,
    redirect,
    RouterProvider,
} from "react-router-dom";

import "./App.scss";
import { getToken } from "./api/utils";
import { Login } from "./Login";
import { Signup } from "./Signup";
import { Home } from "./Home";
import {
    homeAction,
    loginAction,
    profileSelfAction,
    signupAction,
    userListAction,
} from "./actions";
import {
    feedLoader,
    homeLoader,
    profileLoader,
    userListLoader,
} from "./loaders";
import { Feed } from "./Feed";
import { Messages } from "./Messages";
import { Profile } from "./Profile";
import { UserList } from "./UserList";

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
        loader: homeLoader,
        action: homeAction,
        element: <Home />,
        children: [
            { path: "feed", element: <Feed />, loader: feedLoader },
            { path: "messages", element: <Messages /> },
            {
                path: "users",
                element: <UserList />,
                loader: userListLoader,
                action: userListAction,
            },
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
