import { useLoaderData } from "react-router-dom";
import { feedLoader, LoaderToType } from "./loaders";
import { useHomeContext } from "./HomeContext";
import { PostList } from "./PostList";
import { isError } from "./api/dto";

import "./Feed.scss";

export function Feed() {
    const loaderData = useLoaderData() as LoaderToType<typeof feedLoader>;
    if (!loaderData || isError(loaderData)) {
        return <div>error</div>;
    }
    const homeContext = useHomeContext();

    return (
        <div className={"feedView"}>
            <PostList posts={loaderData} selfUuid={homeContext.user.uuid} />
        </div>
    );
}
