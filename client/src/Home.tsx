import { Form, NavLink, Outlet, useLoaderData } from "react-router-dom";
import { homeLoader, LoaderToType } from "./loaders";
import { isError } from "./api/dto";

import "./Home.scss";
import { HomeContextType } from "./HomeContext";

export function Home() {
    const loaderData = useLoaderData() as LoaderToType<typeof homeLoader>;

    if (!loaderData || isError(loaderData)) {
        return <div>Error</div>;
    }

    const activePendingClassName = ({
        isActive,
        isPending,
    }: {
        isActive: boolean;
        isPending: boolean;
    }) => (isActive ? "active" : isPending ? "pending" : "");

    return (
        <div id="Home">
            <div id="HomeSidebar">
                <div id="SidebarUserInfo">
                    <a> username: {loaderData.username}</a>
                    <a>name: {loaderData.fullName}</a>
                    {/*method post for everything to reload*/}
                    <Form method={"post"}>
                        <button
                            type={"submit"}
                            name={"intent"}
                            value={"logout"}
                        >
                            logout
                        </button>
                    </Form>
                </div>
                <div id="SidebarNav">
                    <NavLink to={"feed"} className={activePendingClassName}>
                        Feed
                    </NavLink>{" "}
                    <NavLink
                        to={"messages/chats"}
                        className={activePendingClassName}
                    >
                        Messages
                    </NavLink>
                    <NavLink to={"users"} className={activePendingClassName}>
                        Users
                    </NavLink>
                    <NavLink to={"profile"} className={activePendingClassName}>
                        Profile
                    </NavLink>
                    {loaderData.isAdmin && (
                        <NavLink
                            to={"haters"}
                            className={activePendingClassName}
                        >
                            Haters
                        </NavLink>
                    )}
                </div>
            </div>
            <div id="HomeContent">
                <Outlet
                    context={{ user: loaderData } satisfies HomeContextType}
                />
            </div>
        </div>
    );
}
