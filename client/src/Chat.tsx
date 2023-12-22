import { useLoaderData } from "react-router-dom";
import { chatLoader, LoaderToType } from "./loaders";
import { isError } from "./api/dto";

export function Chat() {
    const loaderData = useLoaderData() as LoaderToType<typeof chatLoader>;
    if (!loaderData || isError(loaderData)) {
        return <div>error</div>;
    }

    return <div className={"chat"}>{loaderData.name}</div>;
}
