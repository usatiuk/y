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
    chatAction,
    editChatAction,
    homeAction,
    loginAction,
    newChatAction,
    profileSelfAction,
    signupAction,
    userListAction,
} from "./actions";
import {
    chatListLoader,
    chatLoader,
    editChatLoader,
    feedLoader,
    homeLoader,
    newChatLoader,
    profileLoader,
    userListLoader,
} from "./loaders";
import { Feed } from "./Feed";
import { Profile } from "./Profile";
import { UserList } from "./UserList";
import { Chats } from "./Chats";
import { ChatCreate } from "./ChatCreate";
import { Chat } from "./Chat";
import { ChatEdit } from "./ChatEdit";
import { Haters } from "./Haters";

const router = createBrowserRouter(
    [
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
                // { path: "messages", element: <Messages /> },
                {
                    path: "messages/chats",
                    element: <Chats />,
                    loader: chatListLoader,
                },
                {
                    path: "messages/chats/new",
                    element: <ChatCreate />,
                    loader: newChatLoader,
                    action: newChatAction,
                },
                {
                    path: "messages/chat/:id",
                    element: <Chat />,
                    loader: chatLoader,
                    action: chatAction,
                },
                {
                    path: "messages/chat/:id/edit",
                    element: <ChatEdit />,
                    loader: editChatLoader,
                    action: editChatAction,
                },
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
                {
                    path: "haters",
                    element: <Haters />,
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
    ],
    { basename: "/app" },
);

export function App() {
    return (
        <React.StrictMode>
            <div id={"appRoot"}>
                <RouterProvider router={router} />
            </div>
        </React.StrictMode>
    );
}
