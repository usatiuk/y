import { NavLink, NavLinkProps, Outlet, useLoaderData } from "react-router-dom";
import { homeLoader, LoaderToType } from "./loaders";
import { isError } from "./api/dto";

import "./Home.scss";

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
                </div>
                <div id="SidebarNav">
                    <NavLink to={"feed"} className={activePendingClassName}>
                        Feed
                    </NavLink>{" "}
                    <NavLink to={"messages"} className={activePendingClassName}>
                        Messages
                    </NavLink>
                    <NavLink to={"profile"} className={activePendingClassName}>
                        Profile
                    </NavLink>
                </div>
            </div>
            <div id="HomeContent">
                <Outlet />
            </div>
        </div>
    );
}
