import { Outlet, useLoaderData } from "react-router-dom";
import { homeLoader } from "./loaders";
import { isError } from "./api/dto";

export function Home() {
    const loaderData = useLoaderData() as
        | Awaited<ReturnType<typeof homeLoader>>
        | undefined;

    if (!loaderData || isError(loaderData)) {
        return <div>Error</div>;
    }

    return (
        <>
            <a>username: {loaderData.username}</a>
            <a>name: {loaderData.fullName}</a>
            <Outlet />
        </>
    );
}
